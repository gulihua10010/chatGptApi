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
public class MessageVO implements Serializable
{
    private static final long serialVersionUID = -2906181698518484034L;
    private String id;
    private String parent;
    private String role;
    private String type;
    private String content;
    private String html;
    private String createTime;
    private Boolean isSend;

    private String conversationId;
    private Boolean isDone;

    private Boolean isSuccess;
    private Boolean isImg;

    public String getHtml()
    {
        if (null == html)
        {
            return this.content;
        }
        return this.html;
    }
}
