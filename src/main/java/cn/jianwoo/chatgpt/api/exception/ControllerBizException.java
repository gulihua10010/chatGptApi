package cn.jianwoo.chatgpt.api.exception;

import cn.jianwoo.chatgpt.api.constants.ExceptionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author GuLihua
 * @Description
 * @date 2020-08-04 14:56
 */
public class ControllerBizException extends JwBlogException
{
    public static final ControllerBizException JSON_CONVERT_ERROR = new ControllerBizException(
            ExceptionConstants.BIZ_HAS_EXIST, "Parameter conversion failed, JSON string exception.");
    public static final ControllerBizException JSON_IS_NULL = new ControllerBizException(
            ExceptionConstants.BIZ_NOT_EXIST, "Parameter conversion failed, JSON is empty.");
    public static final ControllerBizException INVALID_PARAM = new ControllerBizException(
            ExceptionConstants.BIZ_CREATE_FAIL, "Invalid method name parameter.");
    private static final long serialVersionUID = -4477787493913372810L;
    private final Logger logger = LoggerFactory.getLogger(ControllerBizException.class);

    public ControllerBizException()
    {
    }


    public ControllerBizException(String code, String msg, Object... args)
    {
        super(code, msg, args);
    }


    public ControllerBizException(String message)
    {
        super(message);
    }


    public ControllerBizException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public ControllerBizException(Throwable cause)
    {
        super(cause);
    }


    public ControllerBizException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    @Override
    public ControllerBizException print()
    {
        logger.warn("==>ControllerBizException, code:" + this.code + ", msg:" + this.msg);
        return this;
    }
}
