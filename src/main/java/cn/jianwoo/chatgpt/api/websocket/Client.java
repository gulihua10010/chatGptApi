package cn.jianwoo.chatgpt.api.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.websocket.Session;

/**
 * @Author gulihua
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client implements Serializable
{

    private static final long serialVersionUID = 8957107006902627635L;

    private String email;
    private String conversationId;
    private String parentId;

    private Session session;
}
