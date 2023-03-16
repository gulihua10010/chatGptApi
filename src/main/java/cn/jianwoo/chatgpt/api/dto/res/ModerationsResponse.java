package cn.jianwoo.chatgpt.api.dto.res;

import java.util.List;

import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.dto.vo.ConversationsVO;
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
public class ModerationsResponse extends BaseResponseDto
{
    private static final long serialVersionUID = -6815107739882680299L;
    private Boolean blocked;
    private Boolean flagged;
    private String moderationId;

    public static ModerationsResponse getInstance()
    {
        return new ModerationsResponse();
    }

}