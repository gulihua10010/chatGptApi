package cn.jianwoo.chatgpt.api.dto.res;

import java.util.List;

import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.dto.vo.MessageVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2020-11-26 14:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ConversationDetResponse extends BaseResponseDto
{
    private static final long serialVersionUID = 1970975993765800815L;
    private String currentNode;
    private String title;
    private List<MessageVO> messageList;

    public static ConversationDetResponse getInstance()
    {
        return new ConversationDetResponse();
    }

}