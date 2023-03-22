package cn.jianwoo.chatgpt.api.bo;

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
public class ConversationResBO implements Serializable
{
    private static final long serialVersionUID = 8977480847981522225L;
    private String conversationId;
    private String id;
    private String content;
    private String html;
    private String url;
    private String role;
    private String createTime;
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
