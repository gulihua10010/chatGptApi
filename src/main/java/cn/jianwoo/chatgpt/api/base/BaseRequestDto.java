package cn.jianwoo.chatgpt.api.base;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author GuLihua
 * @Description
 * @date 2020-08-25 17:01
 */
@Data
@EqualsAndHashCode()
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequestDto implements Serializable
{
    private static final long serialVersionUID = 2854392503356329692L;

    private String requestId;
    private String id;
    private String actor;
    private String clientIp;
    private String clientName;
    private String sessionId;
    private String authType = "token";

    private String authValue;

    private Integer offset;
    private Integer limit;
    private Date requestDate = new Date();



}
