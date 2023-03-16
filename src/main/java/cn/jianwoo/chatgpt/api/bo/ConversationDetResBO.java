package cn.jianwoo.chatgpt.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @date 2023-02-22 16:02
 */
@Data
public class ConversationDetResBO implements Serializable
{
    private static final long serialVersionUID = 2924490244735282032L;
    private String currentNode;
    private String title;
    private List<MessageBO> messageList;

    @Data
    public static class MessageBO implements Serializable
    {

        private static final long serialVersionUID = -2906181698518484034L;
        private String id;
        private String parent;
        private String role;
        private String type;
        private String content;
        private String html;
        private String createTime;

    }
}
