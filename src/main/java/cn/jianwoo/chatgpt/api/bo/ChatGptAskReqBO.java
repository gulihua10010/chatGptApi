package cn.jianwoo.chatgpt.api.bo;

import cn.jianwoo.chatgpt.api.base.BaseAuthReqBO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author gulihua
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 */
@Data
@ToString
public class ChatGptAskReqBO extends BaseAuthReqBO
{

    private static final long serialVersionUID = -6815107739882680259L;
    private String conversationId;
    private String parentId;
    private String content;
    private String id;
    private String model;

    private List<ChatMessageBO> messages;

    @Data
    @ToString
    public static class ChatMessageBO
    {

        private String role;
        private String content;

    }
}
