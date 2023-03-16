package cn.jianwoo.chatgpt.api.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.jianwoo.chatgpt.api.bo.ChatGptAskReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationDetResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsResBO;
import cn.jianwoo.chatgpt.api.bo.FreeDemoApiKeyBO;
import cn.jianwoo.chatgpt.api.bo.GenTitleReqBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsResBO;
import cn.jianwoo.chatgpt.api.bo.ProxyBO;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.service.ChatGptService;
import cn.jianwoo.chatgpt.api.stream.Callback;
import cn.jianwoo.chatgpt.api.stream.HttpAsyncClientUtil;
import cn.jianwoo.chatgpt.api.util.MarkdownToHtmlUtils;
import lombok.extern.log4j.Log4j2;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Demo Gpt<br>
 * <a href="https://freegpt.one/">https://freegpt.one/</a><br>
 * 
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-22 14:32
 */
@Service("demoFreeGptApiService")
@Log4j2
public class DemoFreeGptApiServiceImpl implements ChatGptService
{

    @Autowired
    private ProxyBO proxyBO;

    @Autowired
    private TimedCache<String, String> timedCache;

    @Autowired
    private FreeDemoApiKeyBO freeDemoApiKeyBO;
    public static final String BASE_URL = "https://api.openai.com/v1";

    @Override
    public void verify(String authValue) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported methods!");

    }


    @Override
    public void conversation(ChatGptAskReqBO askBO, Callback<ConversationResBO> callback) throws JwBlogException
    {
        JSONObject params = new JSONObject();
        String model = askBO.getModel();
        if (StringUtils.isBlank(model))
        {
            model = "gpt-3.5-turbo";
        }
        params.put("model", model);
        params.put("messages", askBO.getMessages());
        params.put("temperature", 0);
        params.put("max_tokens", 2048);
        params.put("stream", true);
        askBO.setAuthValue(fetchApiKey());
        log.debug(">>>>>/chat/completions request: {}", params.toJSONString());

        Request request = new Request.Builder().url(BASE_URL + "/chat/completions")
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), params.toJSONString()))
                .header("Authorization", "Bearer " + askBO.getAuthValue())
                .header("Accept", "text/event-stream").build();

        OkHttpClient client = HttpAsyncClientUtil.createHttpClient(proxyBO.getProxy());
        ;
        HttpAsyncClientUtil.execute(client, request, param -> {
            log.debug(">>>>conversation res:: {}", param);
            ConversationResBO resBO = new ConversationResBO();
            try
            {
                resBO = parseConversation(param);
                resBO.setConversationId(askBO.getConversationId());
                String content = cacheAppendText(resBO);
                resBO.setContent(content);
                resBO.setHtml(MarkdownToHtmlUtils.markdownToHtmlExtensions(StrUtil.trimToEmpty(content)));

            }
            catch (Exception e)
            {
                log.error("conversation.parseConversation exec failed, e:", e);
                resBO.setIsSuccess(false);
                resBO.setRole("assistant");
                resBO.setContent(e.getMessage());
                resBO.setIsDone(false);
                resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
            callback.call(resBO);

        }, done -> {
            ConversationResBO resBO = new ConversationResBO();
            resBO.setIsSuccess(true);
            resBO.setRole("assistant");
            resBO.setIsDone(true);
            resBO.setContent("DONE");
            resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            callback.call(resBO);
        }, fail -> {
            ConversationResBO resBO = new ConversationResBO();
            resBO.setIsSuccess(false);
            resBO.setRole("assistant");
            resBO.setIsDone(true);
            resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            resBO.setContent("服务出错!");
            try
            {
                JSONObject jsonObject = JSONObject.parseObject(fail);
                JSONObject error = jsonObject.getJSONObject("error");
                if (null != error)
                {
                    String msg = error.getString("message");
                    resBO.setContent(msg);
                }
            }
            catch (Exception e)
            {
                log.error("conversation.parse error msg failed, e: ", e);
            }
            callback.call(resBO);
        });
    }


    /**
     *
     * 缓存返回的文本数据，使其追加式返回，不一个一个字符返回，提升解析效率
     *
     * @author gulihua
     * @param %param name% %param description%
     * @return
     */
    private String cacheAppendText(ConversationResBO resBO)
    {
        String id = resBO.getId();
        if (id == null || !resBO.getIsSuccess() || null == resBO.getContent())
        {
            return resBO.getContent();
        }
        String text = timedCache.get(id);
        if (null == text)
        {
            // 十分钟过期
            timedCache.put(id, resBO.getContent(), DateUnit.MINUTE.getMillis() * 10);
            return resBO.getContent();
        }
        // 追加数据
        String newText = text.concat(resBO.getContent());
        timedCache.put(id, newText, DateUnit.MINUTE.getMillis() * 10);
        return newText;

    }


    @Override
    public String aiGenImage(ChatGptAskReqBO askBO) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported Token methods!");

    }


    private static ConversationResBO parseConversation(String res)
    {
        ConversationResBO resBO = new ConversationResBO();
        resBO.setRole("assistant");
        resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        resBO.setIsSuccess(true);
        resBO.setIsDone(false);
        List<String> resArr = StrUtil.splitTrim(res, "\n");

        StringBuilder sb = new StringBuilder();
        if (CollUtil.isNotEmpty(resArr))
        {
            for (String data : resArr)
            {
                if (data.contains("[DONE]"))
                {
                    break;
                }

                try
                {
                    if (StrUtil.isBlank(data) || !JSONObject.isValidObject(data))
                    {
                        continue;
                    }
                }
                catch (Exception e)
                {
                    continue;
                }

                JSONObject jsonObject = JSONObject.parseObject(data);

                JSONArray choices = jsonObject.getJSONArray("choices");
                for (Object o : choices)
                {
                    JSONObject message = ((JSONObject) o).getJSONObject("delta");
                    if (null != message)
                    {
                        sb.append(Optional.ofNullable(message.getString("content")).orElse(""));
                    }
                }

                resBO.setId(jsonObject.getString("id"));
                Long time = jsonObject.getLong("created") * 1000;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                resBO.setCreateTime(DateUtil.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
                resBO.setIsDone(false);

            }
            resBO.setContent(sb.toString());
        }
        return resBO;
    }


    private String fetchApiKey() throws JwBlogException
    {
        if (CollectionUtils.isEmpty(freeDemoApiKeyBO.getApiKeyList()))
        {
            throw new JwBlogException("400001", "API 授权失败!Api-Key 列表为空!");
        }

        return freeDemoApiKeyBO.getNextApiKey();
    }


    @Override
    public ConversationDetResBO getConversation(String authValue, String id) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported methods!");
    }


    @Override
    public List<ConversationsResBO> getConversationList(ConversationsReqBO reqBO) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported methods!");

    }


    @Override
    public ModerationsResBO moderation(ModerationsReqBO reqBO) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported methods!");

    }


    @Override
    public String genTitle(GenTitleReqBO reqBO) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported methods!");
    }
}
