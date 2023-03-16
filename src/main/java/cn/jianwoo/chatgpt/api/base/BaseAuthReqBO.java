package cn.jianwoo.chatgpt.api.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-22 14:44
 */
@Data
public class BaseAuthReqBO implements Serializable
{
    private static final long serialVersionUID = -2874841047890759976L;
    private String authValue;

}