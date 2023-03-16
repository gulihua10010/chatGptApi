package cn.jianwoo.chatgpt.api.base;

import java.io.Serializable;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.jianwoo.chatgpt.api.exception.ControllerBizException;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.exception.ValidationException;
import cn.jianwoo.chatgpt.api.util.JwUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController implements Serializable
{
    private static final long serialVersionUID = -4016109185391802117L;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;

    @Autowired
    protected ServletContext application;

    protected void printRequestParams(String param)
    {
        String clazzName = Thread.currentThread().getStackTrace()[2].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.debug(">> [IP:{}] receive the request in method [{}.{}] with param :: {}", JwUtil.getRealIpAddress(request),
                clazzName, methodName, param);
    }


    public <T> T convertParam(String param, Class<T> class1) throws ControllerBizException
    {
        T result;
        if (StringUtils.isBlank(param))
        {
            throw ControllerBizException.JSON_IS_NULL.print();
        }
        try
        {
            result = JSONObject.parseObject(param, class1);
        }
        catch (Exception e)
        {
            log.error("Parameter conversion failed, JSON string exception: e" + e.getMessage(), e);
            throw ControllerBizException.JSON_CONVERT_ERROR.print();
        }

        String clazzName = Thread.currentThread().getStackTrace()[2].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.debug("convertParam the request in method {}.{} with param :: {}", clazzName, methodName,
                JSONObject.toJSONString(result));
        return result;
    }


    private String getRequestId()
    {
        StringBuilder sb = new StringBuilder();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString();
        String prefix = uuid.substring(0, 8);
        String suffix = uuid.substring(28, 36);
        String requestId = sb.append(prefix).append(currentTime).append(suffix).toString();

        return requestId;
    }


    protected String exceptionToString(Exception e)
    {
        log.error("Occur system error, ex : " + e.getMessage(), e);

        if (e instanceof ControllerBizException)
        {
            return responseToJSONString(
                    new BaseResponseDto(((ControllerBizException) e).getCode(), ((ControllerBizException) e).getMsg()));
        }
        else if (e instanceof ValidationException)
        {
            return responseToJSONString(
                    new BaseResponseDto(((ValidationException) e).getCode(), ((ValidationException) e).getMsg()));
        }
        else if (e instanceof JwBlogException)
        {
            return responseToJSONString(
                    new BaseResponseDto(((JwBlogException) e).getCode(), ((JwBlogException) e).getMsg()));
        }
        else
        {
            return responseToJSONString(BaseResponseDto.SYSTEM_ERROR);
        }
    }


    protected BaseResponseDto exceptionToRespDto(Exception e)
    {
        if (e instanceof ControllerBizException)
        {
            return new BaseResponseDto(((ControllerBizException) e).getCode(), ((ControllerBizException) e).getMsg());
        }
        else if (e instanceof ValidationException)
        {
            return new BaseResponseDto(((ValidationException) e).getCode(), ((ValidationException) e).getMsg());
        }
        else if (e instanceof JwBlogException)
        {
            return new BaseResponseDto(((JwBlogException) e).getCode(), ((JwBlogException) e).getMsg());
        }
        else
        {
            log.error("SYSTEM ERROR : " + e.getMessage(), e);
            return BaseResponseDto.SYSTEM_ERROR;
        }
    }


    protected String responseToJSONString(Object object)
    {
        String clazzName = Thread.currentThread().getStackTrace()[2].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String resp = "";
        if (null == object)
        {
            return resp;
        }
        else
        {
            resp = JSON.toJSONString(object);
        }
        log.debug(">> API response data in method [{}.{}]: {}", clazzName, methodName, resp);
        return resp;
    }


    public String getServerIPPort()
    {
        // + ":" + request.getServerPort()
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
