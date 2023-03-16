package cn.jianwoo.chatgpt.api.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import cn.jianwoo.chatgpt.api.util.NotifiyUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gulihua
 * @Description
 * @date 2022-05-12 17:55
 */
@Component
@EnableAspectJAutoProxy
@Aspect
@Slf4j
public class ExceptionAOPHandler
{

    @Autowired
    private ApplicationConfigUtil applicationConfigUtil;
    @Autowired
    private NotifiyUtil notifiyUtil;

    // 创建切入点,在service层切入
    @Pointcut(value = "(execution(* cn.jianwoo.openai.auth.*.*(..))||"
            + "execution(* cn.jianwoo.chatgpt.api.service.impl.*.*(..))||"
            + "execution(* cn.jianwoo.chatgpt.api.util.*.*(..))) ")
    public void servicePointCut()
    {
    }


    @AfterThrowing(value = "servicePointCut()", throwing = "e")
    public void sendExceptionByMail(JoinPoint joinPoint, Exception e)
    {
        if (e instanceof JwBlogException)
        {
            return;
        }
        if (!applicationConfigUtil.getIsNotify())
        {
            return;
        }
        String name = joinPoint.getSignature().getName();
        List<Object> args = new ArrayList<>();
        if (joinPoint.getArgs() != null)
        {
            args = Arrays.asList(joinPoint.getArgs());
        }

        log.error("===>> [ExceptionAOPHandler]Exception occurs,  method: {}", name);
        log.error("===>> exception: \r\n", e);
        StringBuilder msg = new StringBuilder(
                String.format("===>> [ExceptionAOPHandler]Exception occurs,  method: %s\r\n", name));
        msg.append(e.toString()).append(":").append(e.getMessage()).append("\r\n");
        msg.append(log(e));
        // 发送邮件
        try
        {

            notifiyUtil.sendEmail(applicationConfigUtil.getEmail(), "【简窝chatGpt】系统异常", msg.toString());
        }
        catch (JwBlogException ex)
        {
            log.error("defaultExceptionHandler(AsyncProcAutoTaskD0099Impl).sendEmail failed, exception:\r\n", ex);
        }

    }


    private String log(Throwable ex)
    {
        log.error("Exception request param:");
        StringBuilder msg = new StringBuilder();

        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error)
        {
            msg.append(stackTraceElement.toString()).append("\r\n");

        }
//        log.error(msg.toString());
        return msg.toString();
    }

}
