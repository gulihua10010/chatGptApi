package cn.jianwoo.chatgpt.api.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;

import cn.jianwoo.chatgpt.api.annotation.IpLimit;
import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.constants.ExceptionConstants;
import cn.jianwoo.chatgpt.api.service.LoadingCacheIpService;
import cn.jianwoo.chatgpt.api.util.JwUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gulihua
 * @Description
 * @date 2022-04-28 15:23
 */
@Component
@Scope
@Aspect
@Slf4j
public class IPLimitAspect
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoadingCacheIpService loadingCacheIpService;

    @Pointcut("@annotation(cn.jianwoo.chatgpt.api.annotation.IpLimit)")
    public void ipLimit()
    {

    }


    @Around("ipLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 拿limit的注解
        IpLimit limit = method.getAnnotation(IpLimit.class);
        if (limit != null)
        {
            Object obj = null;
            String ipAddr = JwUtil.getRealIpAddress(request);

            RateLimiter limiter = loadingCacheIpService.getIpLimiter(ipAddr);
            if (limiter.tryAcquire(limit.timeout(), limit.timeunit()))
            {
                // 获得令牌（不限制访问）
                obj = joinPoint.proceed();
            }
            else
            {
                // 未获得令牌（限制访问）
                responseFail();
            }
            return obj;
        }
        return joinPoint.proceed();

    }


    /**
     * 直接向前端抛出异常
     */
    private void responseFail() throws Exception
    {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(Constants.CONTENT_TYPE_JSON);
        response.getWriter().write(JSONObject.toJSONString(new BaseResponseDto(ExceptionConstants.BIZ_ACCESS_FREQUENTLY,
                ExceptionConstants.ACCESS_FREQUENTLY_DESC)));

    }

}
