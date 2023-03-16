package cn.jianwoo.chatgpt.api.dto.res;

import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.dto.vo.ConversationsVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class ConversationsResponse extends BaseResponseDto
{
    private static final long serialVersionUID = 1970975993765800815L;
    private List<ConversationsVO> list;

    public static ConversationsResponse getInstance()
    {
        return new ConversationsResponse();
    }

}