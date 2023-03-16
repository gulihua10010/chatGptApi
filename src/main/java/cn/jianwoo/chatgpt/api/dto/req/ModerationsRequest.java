package cn.jianwoo.chatgpt.api.dto.req;

import java.io.Serializable;

import cn.jianwoo.chatgpt.api.base.BaseRequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2021-07-18 0:56
 */
@Data
@NoArgsConstructor
public class ModerationsRequest extends BaseRequestDto implements Serializable
{
    private static final long serialVersionUID = -7982293393643864915L;
    private String conversationId;
    private String content;
    private String id;

}
