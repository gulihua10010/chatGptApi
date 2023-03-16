package cn.jianwoo.chatgpt.api.service.impl;

import java.util.Date;
import java.util.List;

import cn.jianwoo.chatgpt.api.bo.ConversationDetResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsResBO;
import cn.jianwoo.chatgpt.api.bo.GenTitleReqBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.jianwoo.chatgpt.api.bo.ChatGptAskReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationResBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsResBO;
import cn.jianwoo.chatgpt.api.bo.ProxyBO;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.service.ChatGptService;
import cn.jianwoo.chatgpt.api.stream.Callback;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import cn.jianwoo.chatgpt.api.util.JwUtil;
import lombok.extern.log4j.Log4j2;

/**
 * 百姓网提供的 API服务<br>
 * <a href="https://gpt.baixing.com">https://gpt.baixing.com</a><br>
 * 申请地址：<a href="https://jinshuju.net/f/gzUO2t">https://jinshuju.net/f/gzUO2t</a><br>
 * 
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-22 14:32
 */
@Service("baixingGptApiService")
@Log4j2
public class BaixingGptApiServiceImpl implements ChatGptService
{

    @Autowired
    private ProxyBO proxyBO;

    @Autowired
    private ApplicationConfigUtil applicationConfigUtil;

    public static final String BASE_URL = "https://gpt.baixing.com";

    @Override
    public void verify(String authValue) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported methods!");

    }


    @Override
    public void conversation(ChatGptAskReqBO askBO, Callback<ConversationResBO> callback) throws JwBlogException
    {
        JSONObject params = new JSONObject();
        params.put("p", askBO.getContent());
        params.put("k", applicationConfigUtil.getBaixingApiKey());
        log.debug(">>>>>/conversation request: {}", params.toJSONString());
        HttpResponse response = HttpRequest.post(BASE_URL).body(params.toJSONString()).setProxy(proxyBO.getProxy())
                .execute();
        if (response.getStatus() != 200)
        {
            log.error(">>>>conversation err:{}", response.body());
            throw new JwBlogException("500001", "服务异常!");
        }
        JSONObject res = JSONObject.parseObject(response.body());
        ConversationResBO resBO = new ConversationResBO();
        if (res.getIntValue("code") != 0)
        {
            resBO.setIsSuccess(false);
            resBO.setRole("assistant");
            resBO.setContent(res.getString("message"));
            resBO.setIsDone(false);
            resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            callback.call(resBO);
        }
        else
        {
            resBO.setContent(res.getString("data").replace("[*]", ""));
            resBO.setId(JwUtil.generateUserId());
            resBO.setIsDone(false);
            resBO.setIsSuccess(true);
            resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            callback.call(resBO);

        }
    }


    @Override
    public String aiGenImage(ChatGptAskReqBO askBO) throws JwBlogException
    {
        throw new JwBlogException("500001", "Unsupported methods!");

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
