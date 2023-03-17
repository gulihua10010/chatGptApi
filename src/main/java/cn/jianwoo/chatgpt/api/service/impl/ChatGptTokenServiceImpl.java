package cn.jianwoo.chatgpt.api.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.jianwoo.chatgpt.api.constants.CacheKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import cn.hutool.cache.Cache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.jianwoo.chatgpt.api.bo.ChatGptAskReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationDetResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsResBO;
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
 * ChatGpt 服务
 *
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-22 14:32
 */
@Service("chatGptTokenService")
@Log4j2
public class ChatGptTokenServiceImpl implements ChatGptService
{

    @Autowired
    private ProxyBO proxyBO;

    @Autowired
    private Cache<String, String> fifuCache;

    @Override
    public void verify(String authValue) throws JwBlogException
    {

        String baseUrl = fifuCache.get(CacheKey.PROXY_BASE_URL);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authValue);
        HttpResponse response = HttpRequest.get(baseUrl + "/conversations").headerMap(headers, true)
                .setProxy(proxyBO.getProxy()).execute();
        log.error(">>>>verify token  :{}", response.body());

        if (response.getStatus() != 200)
        {
            log.error(">>>>verify token err:{}", response.body());
            if (response.body().contains("expired"))
            {
                throw new JwBlogException("400001", "Access Token过期");
            }
            else if (response.body().contains("Incorrect"))
            {
                throw new JwBlogException("400001", "Access Token不正确");
            }
            else if (response.body().contains("Rate limited"))
            {
                throw new JwBlogException("500001", "当前使用人数过多，请稍后再试~");
            }
            throw new JwBlogException("500001", "请求异常!");
        }
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        if (jsonObject.getString("detail") != null)
        {
            log.error(">>>>verify token err:{}", response.body());
            throw new JwBlogException("400001", "Access Token不正确");
        }

    }


    @Override
    public void conversation(ChatGptAskReqBO askBO, Callback<ConversationResBO> callback) throws JwBlogException
    {
        JSONObject params = new JSONObject();
        params.put("action", "next");
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("id", askBO.getId());
        message.put("role", "user");
        JSONObject author = new JSONObject();
        author.put("role", "user");
        message.put("author", author);
        JSONObject content = new JSONObject();
        content.put("content_type", "text");
        JSONArray parts = new JSONArray();
        parts.add(askBO.getContent());
        content.put("parts", parts);
        message.put("content", content);
        messages.add(message);
        params.put("messages", messages);
        params.put("parent_message_id", askBO.getParentId());
        if (!"-1".equals(askBO.getConversationId()))
        {
            params.put("conversation_id", askBO.getConversationId());
        }
        params.put("model", "text-davinci-002-render-sha");

        log.debug(">>>>>/conversation request: {}", params.toJSONString());
        String baseUrl = fifuCache.get(CacheKey.PROXY_BASE_URL);
        Request request = new Request.Builder().url(baseUrl + "/conversation")
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), params.toJSONString()))
                .header("Authorization", askBO.getAuthValue()).header("Accept", "text/event-stream").build();

        OkHttpClient client = HttpAsyncClientUtil.createHttpClient(proxyBO.getProxy());
        HttpAsyncClientUtil.execute(client, request, param -> {
            log.debug(">>>>conversation res:: {}", param);
            ConversationResBO res = new ConversationResBO();
            try
            {
                res = parseConversation(param);
                if (askBO.getId() != null && !askBO.getId().equals(res.getId()))
                {
                    callback.call(res);
                }
            }
            catch (Exception e)
            {
                log.error("conversation.parseConversation exec failed, e:", e);
                res.setRole("assistant");
                res.setIsSuccess(false);
                res.setContent(e.getMessage());
                res.setIsDone(false);
                res.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                callback.call(res);
            }

        }, done -> {
            ConversationResBO resBO = new ConversationResBO();
            resBO.setRole("assistant");
            resBO.setIsDone(true);
            resBO.setIsSuccess(true);
            resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            resBO.setContent("DONE");
            callback.call(resBO);
        }, fail -> {
            ConversationResBO resBO = new ConversationResBO();
            resBO.setRole("assistant");
            resBO.setIsDone(true);
            resBO.setContent("服务出错!");
            resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            resBO.setIsSuccess(false);
            try
            {
                log.error("conversation failed msg:{}", fail);
                if (fail.contains("expired"))
                {
                    resBO.setContent("Access Token过期");
                }
                else if (fail.contains("Incorrect"))
                {
                    resBO.setContent("Access Token不正确");
                }
                else if (fail.contains("Rate limited"))
                {
                    resBO.setContent("当前使用人数过多，请稍后再试~");
                }
                else
                {
                    JSONObject jsonObject = JSONObject.parseObject(fail);
                    String detail = jsonObject.getString("detail");
                    if (null != detail)
                    {
                        String msg = detail;
                        if (JSONObject.isValidObject(detail))
                        {
                            msg = JSONObject.parseObject(detail).getString("message");
                        }
                        resBO.setContent(msg);
                    }
                }

            }
            catch (Exception e)
            {
                log.error("conversation.parse error msg failed, e: ", e);
            }
            callback.call(resBO);
        });

    }


    @Override
    public String aiGenImage(ChatGptAskReqBO askBO) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported Token methods!");

    }


    private static ConversationResBO parseConversation(String res)
    {
        List<String> resArr = StrUtil.splitTrim(res, "\n");
        ConversationResBO resBO = new ConversationResBO();
        resBO.setRole("assistant");
        resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        resBO.setIsSuccess(true);
        resBO.setIsDone(false);
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

                resBO.setId(jsonObject.getJSONObject("message").getString("id"));
                JSONObject content = jsonObject.getJSONObject("message").getJSONObject("content");
                if (content == null)
                {
                    continue;
                }
                JSONArray parts = content.getJSONArray("parts");
                if (parts == null || parts.size() == 0)
                {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                for (Object o : parts)
                {
                    sb.append(o);
                }
                resBO.setId(jsonObject.getJSONObject("message").getString("id"));
                resBO.setHtml(MarkdownToHtmlUtils.markdownToHtmlExtensions(sb.toString()));
                resBO.setContent(sb.toString());
                resBO.setConversationId(jsonObject.getString("conversation_id"));

            }
        }
        return resBO;
    }


    @Override
    public ConversationDetResBO getConversation(String authValue, String id) throws JwBlogException
    {
        String baseUrl = fifuCache.get(CacheKey.PROXY_BASE_URL);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authValue);
        HttpResponse response = HttpRequest.get(baseUrl + "/conversation/" + id).headerMap(headers, true)
                .setProxy(proxyBO.getProxy()).execute();
        if (response.getStatus() != 200)
        {
            log.error(">>>>getConversation err:{}", response.body());
            if (response.body().contains("expired"))
            {
                throw new JwBlogException("400001", "Access Token过期");
            }
            else if (response.body().contains("Incorrect"))
            {
                throw new JwBlogException("400001", "Access Token不正确");
            }
            else if (response.body().contains("Rate limited"))
            {
                throw new JwBlogException("500001", "当前使用人数过多，请稍后再试~");
            }
            throw new JwBlogException("400001", "请求异常!");
        }
        log.debug(">getConversation {}", response.body());
        try
        {
            if (!JSONObject.isValidObject(response.body()))
            {
                log.error(">>>>getConversation err:{}", response.body());
                throw new JwBlogException("400001", response.body());
            }
        }
        catch (Exception e)
        {
            log.error(">>>>parse response failed, e:", e);
            throw new JwBlogException("400001", response.body());

        }
        LinkedHashMap jsonObject = JSONObject.parseObject(response.body(), LinkedHashMap.class, Feature.OrderedField);
        if (jsonObject.get("detail") != null)
        {
            JSONObject detail = (JSONObject) jsonObject.get("detail");
            log.error(">>>>getConversation err:{}", response.body());
            throw new JwBlogException("400001", detail.getString("message"));
        }
        ConversationDetResBO resBO = new ConversationDetResBO();
        resBO.setTitle((String) jsonObject.get("title"));
        resBO.setCurrentNode((String) jsonObject.get("current_node"));
        List<ConversationDetResBO.MessageBO> list = new ArrayList<>();
        JSONObject mapping = (JSONObject) jsonObject.get("mapping");
        if (mapping != null)
        {
            for (Map.Entry entry : mapping.entrySet())
            {
                JSONObject o = (JSONObject) entry.getValue();
                ConversationDetResBO.MessageBO res = new ConversationDetResBO.MessageBO();
                JSONObject message = o.getJSONObject("message");
                if (message == null)
                {
                    continue;
                }
                JSONObject author = message.getJSONObject("author");
                if (author != null)
                {
                    res.setRole(author.getString("role"));
                }
                JSONObject content = message.getJSONObject("content");
                Double createTime = message.getDouble("create_time");
                StringBuilder sb = new StringBuilder();
                if (content != null)
                {
                    res.setType(content.getString("content_type"));
                    JSONArray parts = content.getJSONArray("parts");
                    for (Object part : parts)
                    {
                        sb.append(part.toString());
                    }
                    if (StringUtils.isBlank(sb.toString()))
                    {
                        continue;
                    }
                }
                res.setId(o.getString("id"));
                res.setParent(o.getString("parent"));
                res.setHtml(MarkdownToHtmlUtils.markdownToHtmlExtensions(sb.toString()));
                res.setContent(sb.toString());

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(new Double(createTime * 1000).longValue());
                res.setCreateTime(DateUtil.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
                list.add(res);
            }
            resBO.setMessageList(list);
        }

        return resBO;
    }


    @Override
    public List<ConversationsResBO> getConversationList(ConversationsReqBO reqBO) throws JwBlogException
    {
        String baseUrl = fifuCache.get(CacheKey.PROXY_BASE_URL);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", reqBO.getAuthValue());
        HttpResponse response = HttpRequest
                .get(baseUrl + "/conversations?offset=" + reqBO.getOffset() + "&limit=" + reqBO.getLimit())
                .headerMap(headers, true).setProxy(proxyBO.getProxy()).execute();
        if (response.getStatus() != 200)
        {
            log.error(">>>>getConversationList err:{}", response.body());
            if (response.body().contains("expired"))
            {
                throw new JwBlogException("400001", "Access Token过期");
            }
            else if (response.body().contains("Incorrect"))
            {
                throw new JwBlogException("400001", "Access Token不正确");
            }
            else if (response.body().contains("Rate limited"))
            {
                throw new JwBlogException("500001", "当前使用人数过多，请稍后再试~");
            }
            throw new JwBlogException("400001", "请求异常!");
        }
        LinkedHashMap jsonObject = JSONObject.parseObject(response.body(), LinkedHashMap.class, Feature.OrderedField);

        if (jsonObject.get("detail") != null)
        {
            JSONObject detail = (JSONObject) jsonObject.get("detail");
            log.error(">>>>getConversationList err:{}", response.body());
            throw new JwBlogException("400001", detail.getString("message"));
        }
        List<ConversationsResBO> list = new ArrayList<>();
        JSONArray items = (JSONArray) jsonObject.get("items");
        if (items != null)
        {
            for (Object item : items)
            {
                JSONObject o = (JSONObject) item;
                ConversationsResBO res = new ConversationsResBO();
                res.setId(o.getString("id"));
                res.setCreateTime(o.getString("create_time"));
                res.setTitle(o.getString("title"));
                list.add(res);
            }
        }

        return list;

    }


    @Override
    public ModerationsResBO moderation(ModerationsReqBO reqBO) throws JwBlogException
    {

        String baseUrl = fifuCache.get(CacheKey.PROXY_BASE_URL);
        JSONObject params = new JSONObject();
        params.put("conversation_id", reqBO.getConversationId());
        params.put("input", reqBO.getContent());
        params.put("message_id", reqBO.getId());
        params.put("model", "text-moderation-playground");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", reqBO.getAuthValue());
        HttpResponse response = HttpRequest.post(baseUrl + "/moderations").headerMap(headers, true)
                .body(params.toJSONString()).setProxy(proxyBO.getProxy()).execute();
        if (response.getStatus() != 200)
        {
            log.error(">>>>moderation err:{}", response.body());
            if (response.body().contains("expired"))
            {
                throw new JwBlogException("400001", "Access Token过期");
            }
            else if (response.body().contains("Incorrect"))
            {
                throw new JwBlogException("400001", "Access Token不正确");
            }
            else if (response.body().contains("Rate limited"))
            {
                throw new JwBlogException("500001", "当前使用人数过多，请稍后再试~");
            }
            throw new JwBlogException("400001", "请求异常!");
        }
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        if (jsonObject.get("detail") != null)
        {
            JSONObject detail = jsonObject.getJSONObject("detail");
            log.error(">>>>moderation err:{}", response.body());
            throw new JwBlogException("400001", detail.getString("message"));
        }
        ModerationsResBO resBO = new ModerationsResBO();
        resBO.setFlagged(jsonObject.getBoolean("flagged"));
        resBO.setBlocked(jsonObject.getBoolean("blocked"));
        resBO.setModerationId(jsonObject.getString("moderation_id"));
        return resBO;
    }


    @Override
    public String genTitle(GenTitleReqBO reqBO) throws JwBlogException
    {
        String baseUrl = fifuCache.get(CacheKey.PROXY_BASE_URL);
        JSONObject params = new JSONObject();
        params.put("message_id", reqBO.getId());
        params.put("model", "text-davinci-002-render-sha");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", reqBO.getAuthValue());
        HttpResponse response = HttpRequest.post(baseUrl + "/conversation/gen_title/" + reqBO.getConversationId())
                .headerMap(headers, true).body(params.toJSONString()).setProxy(proxyBO.getProxy()).execute();
        if (response.getStatus() != 200)
        {
            log.error(">>>>genTitle err:{}", response.body());
            if (response.body().contains("expired"))
            {
                throw new JwBlogException("400001", "Access Token过期");
            }
            else if (response.body().contains("Incorrect"))
            {
                throw new JwBlogException("400001", "Access Token不正确");
            }
            else if (response.body().contains("Rate limited"))
            {
                throw new JwBlogException("500001", "当前使用人数过多，请稍后再试~");
            }
            throw new JwBlogException("400001", "请求异常!");
        }
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        if (jsonObject.get("detail") != null)
        {
            log.error(">>>>genTitle err:{}", response.body());
            String detail = jsonObject.getString("detail");
            if (JSONObject.isValidObject(detail))
            {
                detail = JSONObject.parseObject(detail).getString("message");
            }
            throw new JwBlogException("400001", detail);

        }
        return jsonObject.getString("title");
    }

}
