package cn.jianwoo.chatgpt.api.dto.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.jianwoo.chatgpt.api.base.BaseRequestDto;
import cn.jianwoo.chatgpt.api.bo.ChatGptAskReqBO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2021-07-18 0:56
 */
@Data
@NoArgsConstructor
public class ConversationRequest extends BaseRequestDto implements Serializable
{
    private static final long serialVersionUID = -7982293393643864915L;
    private String conversationId;
    private String parentId;
    private String content;
    private String id;
    private String heart;
    private Long time;
    private String model;// api有效
    private Boolean isDemo = false;
    private Boolean isGenImg = false;
    private Boolean isSender = true;

    private List<ChatMessage> messages;

    @Override
    public String toString() {
        return "ConversationRequest{" +
                "conversationId='" + conversationId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", heart='" + heart + '\'' +
                ", time='" + time + '\'' +
                ", model='" + model + '\'' +
                ", isDemo=" + isDemo +
                ", isGenImg=" + isGenImg +
                ", messages=" + messages +
                ", isSender=" + isSender +
                '}';
    }

    @ToString
    @Data
    public static class ChatMessage
    {

        private String role;
        private String content;

    }

}
