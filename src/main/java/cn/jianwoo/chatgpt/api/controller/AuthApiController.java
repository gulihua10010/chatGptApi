package cn.jianwoo.chatgpt.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.cache.Cache;
import cn.hutool.extra.spring.SpringUtil;
import cn.jianwoo.chatgpt.api.annotation.IpLimit;
import cn.jianwoo.chatgpt.api.base.BaseController;
import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.constants.ChatGptServiceBean;
import cn.jianwoo.chatgpt.api.dto.req.AuthRequest;
import cn.jianwoo.chatgpt.api.dto.res.AuthResponse;
import cn.jianwoo.chatgpt.api.service.ChatGptService;
import cn.jianwoo.chatgpt.api.service.OpenAiAuthService;
import cn.jianwoo.chatgpt.api.validation.BizValidation;
import cn.jianwoo.openai.auth.SessionRes;
import lombok.extern.slf4j.Slf4j;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-15 16:19
 */
@RestController
@RequestMapping("/api1")
@Slf4j
public class AuthApiController extends BaseController
{
    @Autowired
    private OpenAiAuthService openAiAuthService;
    @Autowired
    private Cache<String, String> fifuCache;

    /**
     * 授权登录 openAI<br/>
     * url:/api/auth<br/>
     *
     * @param param JSON 参数({@link AuthRequest})<br/>
     *            email<br/>
     *            password<br/>
     * @return 返回响应 {@link AuthResponse} status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         msg<br/>
     *         accessToken<br/>
     *         expires<br/>
     *         id<br/>
     *         image<br/>
     *         email<br/>
     *         secureNextAuthSessionToken<br/>
     * @author gulihua
     */
    @PostMapping("/auth")
    @IpLimit
    public String doAuth(@RequestBody
    String param)
    {
        try
        {
            AuthRequest req = this.convertParam(param, AuthRequest.class);
            BizValidation.paramValidate(req.getEmail(), "email", "邮箱不能为空!");
            BizValidation.paramValidate(req.getPassword(), "password", "密码不能为空!");

            AuthResponse response = AuthResponse.getInstance();
            SessionRes sessionRes = openAiAuthService.auth(req.getEmail(), req.getPassword());

            response.setSecureNextAuthSessionToken(sessionRes.getSecureNextAuthSessionToken());
            response.setAccessToken(sessionRes.getAccessToken());
            response.setExpires(sessionRes.getExpires());
            response.setEmail(sessionRes.getEmail());
            response.setImage(sessionRes.getImage());
            response.setId(sessionRes.getId());
            return super.responseToJSONString(response);
        }
        catch (Exception e)
        {
            return super.exceptionToString(e);

        }
    }


    /**
     * 验证 apiKey<br/>
     * url:/auth/apikey/verify<br/>
     *
     * @param param JSON 参数({@link AuthRequest})<br/>
     *            apiKey<br/>
     * @return 返回响应 {@link AuthResponse} status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         msg<br/>
     * @author gulihua
     */
    @PostMapping("/auth/apikey/verify")
    @IpLimit
    public String doVerifyApi(@RequestBody
    String param)
    {
        try
        {
            AuthRequest req = this.convertParam(param, AuthRequest.class);
            BizValidation.paramValidate(req.getApiKey(), "apiKey", "apiKey不能为空!");


            ChatGptService chatGptService = SpringUtil.getBean(ChatGptServiceBean.get("api"));
            chatGptService.verify(req.getApiKey());

            return super.responseToJSONString(BaseResponseDto.success());
        }
        catch (Exception e)
        {
            return super.exceptionToString(e);

        }
    }


    /**
     * 验证 token<br/>
     * url:/auth/token/verify<br/>
     *
     * @param param JSON 参数({@link AuthRequest})<br/>
     *            accessToken<br/>
     * @return 返回响应 {@link AuthResponse} accessToken<br/>
     *         status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         msg<br/>
     * @author gulihua
     */
    @IpLimit
    @PostMapping("/auth/token/verify")
    public String doVerifyToken(@RequestBody
    String param)
    {
        try
        {
            AuthRequest req = this.convertParam(param, AuthRequest.class);
            BizValidation.paramValidate(req.getAccessToken(), "accessToken", "accessToken不能为空!");

            ChatGptService chatGptService = SpringUtil.getBean(ChatGptServiceBean.get("token"));
            chatGptService.verify(req.getAccessToken());

            return super.responseToJSONString(BaseResponseDto.success());

        }
        catch (Exception e)
        {
            return super.exceptionToString(e);

        }
    }

}
