package cn.jianwoo.chatgpt.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.StrUtil;
import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.constants.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GuLihua
 * @Description
 * @date 2021-01-26 9:37
 */
@Component
@Slf4j
public class LoginHandleInterceptor implements HandlerInterceptor
{

    public static final String AUTHORIZATION = "Authorization";
    @Autowired
    private Cache<String, String> fifuCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {

        String auth = request.getHeader(AUTHORIZATION);
        if (StrUtil.isNotBlank(auth))
        {
            return true;
        }
        response.getWriter().write(JSONObject
                .toJSONString(new BaseResponseDto(ExceptionConstants.UNAUTHORIZED, "Authorization is null!")));
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception
    {

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {

    }
}
