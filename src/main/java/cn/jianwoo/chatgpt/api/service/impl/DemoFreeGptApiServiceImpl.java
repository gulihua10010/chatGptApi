package cn.jianwoo.chatgpt.api.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.jianwoo.chatgpt.api.autotask.AsyncTaskExec;
import cn.jianwoo.chatgpt.api.bo.ChatGptAskReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationDetResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsResBO;
import cn.jianwoo.chatgpt.api.bo.GenTitleReqBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsResBO;
import cn.jianwoo.chatgpt.api.bo.ProxyBO;
import cn.jianwoo.chatgpt.api.constants.CacheKey;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.service.ChatGptService;
import cn.jianwoo.chatgpt.api.stream.Callback;
import cn.jianwoo.chatgpt.api.stream.HttpAsyncClientUtil;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import cn.jianwoo.chatgpt.api.util.MarkdownToHtmlUtils;
import cn.jianwoo.chatgpt.api.util.NotifiyUtil;
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
public class DemoFreeGptApiServiceImpl extends ChatGptApiServiceImpl implements ChatGptService
{

    @Autowired
    private ProxyBO proxyBO;

    @Autowired
    private TimedCache<String, String> timedCache;

    @Autowired
    private Cache<String, String> fifuCache;

    @Autowired
    private NotifiyUtil notifiyUtil;
    @Autowired
    private ApplicationConfigUtil applicationConfigUtil;
    @Autowired
    private AsyncTaskExec asyncTaskExec;

    public static final String BASE_URL = "https://api.openai.com/v1";

    @Override
    public void verify(String authValue) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported methods!");

    }


    @Override
    public void conversation(ChatGptAskReqBO askBO, Callback<ConversationResBO> callback) throws JwBlogException
    {
        askBO.setAuthValue("Bearer " + fifuCache.get(CacheKey.DEMO_API_KEY));
        super.conversation(askBO, callback);
    }


    @Override
    public String aiGenImage(ChatGptAskReqBO askBO) throws JwBlogException
    {
        askBO.setAuthValue("Bearer " + fifuCache.get(CacheKey.DEMO_API_KEY));
        return super.aiGenImage(askBO);

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
