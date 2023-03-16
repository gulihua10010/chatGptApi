package cn.jianwoo.chatgpt.api.exception;

import cn.jianwoo.chatgpt.api.constants.ExceptionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author GuLihua
 * @Description
 * @date 2020-08-04 14:21
 */
public class JwBlogException extends Exception {
    public static final JwBlogException SYSTEM_ERROR = new JwBlogException(ExceptionConstants.SYSTEM_EXCEPTION,
            ExceptionConstants.SYSTEM_EXCEPTION_DESC);
    protected static final Logger logger = LoggerFactory.getLogger(JwBlogException.class);
    private static final long serialVersionUID = -4668522971027224346L;
    protected String code;
    protected String msg;

    public JwBlogException() {
    }


    public JwBlogException(String code, String msg, Object... args) {
        super(args == null || args.length == 0 ? msg : String.format(msg, args));
        this.code = code;
        this.msg = args == null || args.length == 0 ? msg : String.format(msg, args);
    }


    public JwBlogException(String message) {
        super(message);
        this.msg = message;
    }


    public JwBlogException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;

    }


    public JwBlogException(Throwable cause) {
        super(cause);
    }

    public JwBlogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msg = message;

    }


    public JwBlogException getNewInstance(String code, String msg, Object... args) {
        return new JwBlogException(code, msg, args);
    }

    public JwBlogException formatMsg(String msg, Object... args) {
        return new JwBlogException(code, msg, args);
    }


    public JwBlogException print() {
        logger.error("======>  System Error, code : {},  msg : {}.", code, msg);
        return this;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public JwBlogException format(Object... args) {
        return new JwBlogException(this.code, this.msg, args);
    }
}
