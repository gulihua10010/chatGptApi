package cn.jianwoo.chatgpt.api.dto.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @date 2023-02-18 16:00
 */
@Data
@ToString
public class ConversationsVO implements Serializable
{
    private static final long serialVersionUID = 8977480847981522225L;
    private String createTime;
    private String id;
    private String title;
}
