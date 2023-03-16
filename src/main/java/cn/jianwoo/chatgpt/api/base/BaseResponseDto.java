package cn.jianwoo.chatgpt.api.base;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class BaseResponseDto implements Serializable
{
    private static final long serialVersionUID = 8005270127305292401L;

    private static final String SUCCESS_REQ_CODE = "000000";
    private static final String SUCCESS_REQ_MSG = "SUCCESS";
    public static final BaseResponseDto SUCCESS = new BaseResponseDto(SUCCESS_REQ_CODE, SUCCESS_REQ_MSG);
    private static final String FAIL_REQ_CODE = "999999";
//    private static final String FAIL_REQ_MSG = "SYSTEM ERROR";
    private static final String FAIL_REQ_MSG = "服务异常!";
    public static final BaseResponseDto SYSTEM_ERROR = new BaseResponseDto(FAIL_REQ_CODE, FAIL_REQ_MSG);
    private String status;
    private String msg;

    private String token;

    private Long timestamp;

    public static BaseResponseDto buildResponse(String status, String msg)
    {
        return new BaseResponseDto(status, msg);
    }


    public BaseResponseDto(String status, String msg)
    {
        this.status = status;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }


    public BaseResponseDto()
    {
        this.status = SUCCESS_REQ_CODE;
        this.msg = SUCCESS_REQ_MSG;
        this.timestamp = System.currentTimeMillis();
    }


    public BaseResponseDto(String msg)
    {
        this.status = SUCCESS_REQ_CODE;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public static BaseResponseDto success()
    {
        return new BaseResponseDto();
    }


    public static BaseResponseDto error(String msg)
    {
        return new BaseResponseDto(FAIL_REQ_CODE, msg);
    }


    public static BaseResponseDto error()
    {
        return new BaseResponseDto(FAIL_REQ_CODE, FAIL_REQ_MSG);
    }


    public BaseResponseDto success(String msg)
    {
        return new BaseResponseDto(msg);
    }


    public Boolean isSuccess()
    {
        return SUCCESS_REQ_CODE.equals(this.status);
    }


    public String getStatus()
    {
        return status;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getMsg()
    {
        return msg;
    }


    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public String getToken()
    {
        return this.token;
    }


    public void setToken(String token)
    {
        this.token = token;
    }


    public Long getTimestamp()
    {
        return this.timestamp;
    }


    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }
}
