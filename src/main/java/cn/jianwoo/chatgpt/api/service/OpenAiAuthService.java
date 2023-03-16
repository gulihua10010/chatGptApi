package cn.jianwoo.chatgpt.api.service;

import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.openai.auth.SessionRes;

/**
 * openAi授权服务
 * 
 * @author gulihua
 * @date 2023-02-14 19:01
 */
public interface OpenAiAuthService
{

    /**
     *
     * 授权登录
     *
     * @author gulihua
     * @param email 邮箱
     * @param password 密码
     * @return
     */
    SessionRes auth(String email, String password) throws JwBlogException;
}
