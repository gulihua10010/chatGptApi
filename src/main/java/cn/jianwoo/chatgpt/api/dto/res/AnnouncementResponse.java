package cn.jianwoo.chatgpt.api.dto.res;

import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
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
public class AnnouncementResponse extends BaseResponseDto
{
    private static final long serialVersionUID = -6815107739882680299L;
    private String content;

    public static AnnouncementResponse getInstance()
    {
        return new AnnouncementResponse();
    }

}