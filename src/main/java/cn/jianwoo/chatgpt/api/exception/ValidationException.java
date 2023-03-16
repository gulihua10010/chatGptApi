package cn.jianwoo.chatgpt.api.exception;

import cn.jianwoo.chatgpt.api.constants.ExceptionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author GuLihua
 * @Description
 * @date 2020-08-04 16:09
 */
public class ValidationException extends JwBlogException
{
    public static final String DEFAULT_VALIDATION_MSG = "Parameter verified failed,the content is emtpy";
    public static final String DEFAULT_FILE_SIZE_VALIDATION_MSG = "Parameter verified failed,the file size exceeds the maximum limit";
    public static final ValidationException VALIDATOR_PARAM_IS_NULL = new ValidationException(
            ExceptionConstants.VALIDATION_FAILED_NULL, DEFAULT_VALIDATION_MSG);
    public static final ValidationException VALIDATOR_PARAM_IS_EMPTY = new ValidationException(
            ExceptionConstants.VALIDATION_FAILED_EMPTY, DEFAULT_VALIDATION_MSG);
    public static final ValidationException VALIDATOR_LIST_PARAM_IS_EMPTY = new ValidationException(
            ExceptionConstants.VALIDATION_FAILED_LIST_EMPTY, DEFAULT_VALIDATION_MSG);
    public static final ValidationException VALIDATOR_ARRAY_PARAM_IS_EMPTY = new ValidationException(
            ExceptionConstants.VALIDATION_FAILED_ARRAY_EMPTY, DEFAULT_VALIDATION_MSG);
    public static final ValidationException VALIDATOR_FILE_SIZE_MAX = new ValidationException(
            ExceptionConstants.VALIDATOR_FILE_SIZE_MAX, DEFAULT_FILE_SIZE_VALIDATION_MSG);
    private static final Logger logger = LoggerFactory.getLogger(ValidationException.class);
    private static final long serialVersionUID = -2634310359657973291L;

    private String paramName;

    public String getParamName()
    {
        return this.paramName;
    }


    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }


    public ValidationException()
    {
    }


    public ValidationException(String code, String msg, String paramName)
    {
        super(code, msg);
        this.paramName = paramName;

    }


    public ValidationException(String code, String msg, Object... args)
    {
        super(code, msg, args);
    }


    public ValidationException(String message)
    {
        super(message);
    }


    public ValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public ValidationException(Throwable cause)
    {
        super(cause);
    }


    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public ValidationException formatMsg(String msg, Object... args)
    {
        return new ValidationException(code, msg, args);
    }


    public ValidationException getNewInstance(String code, String msg, Object... args)
    {
        return new ValidationException(code, msg, args);
    }


    @Override
    public ValidationException print()
    {
        logger.warn("==>ValidationException, code:" + this.code + ", msg:" + this.msg);
        return this;
    }
}
