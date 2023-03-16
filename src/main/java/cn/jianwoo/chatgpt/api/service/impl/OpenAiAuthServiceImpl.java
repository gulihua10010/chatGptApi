package cn.jianwoo.chatgpt.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jianwoo.chatgpt.api.bo.ProxyBO;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.service.OpenAiAuthService;
import cn.jianwoo.openai.auth.OpenAiAuth;
import cn.jianwoo.openai.auth.PostException;
import cn.jianwoo.openai.auth.SessionRes;
import lombok.extern.log4j.Log4j2;

/**
 * 实现类
 *
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-15 19:03
 */
@Service
@Log4j2
public class OpenAiAuthServiceImpl implements OpenAiAuthService
{

    @Autowired
    private ProxyBO proxyBO;

    @Override
    public SessionRes auth(String email, String password) throws JwBlogException
    {

        try
        {
            return new OpenAiAuth(email, password, proxyBO.getProxy()).auth();
        }
        catch (PostException e)
        {
            log.error(">>>>OpenAiAuthServiceImpl.auth exec failed, e:", e);
            throw new JwBlogException(e.getCode(), e.getMsg());
        }
    }

}
