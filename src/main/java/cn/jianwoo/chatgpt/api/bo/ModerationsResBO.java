package cn.jianwoo.chatgpt.api.bo;

import cn.jianwoo.chatgpt.api.base.BaseAuthReqBO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gulihua
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 */
@Data
public class ModerationsResBO implements Serializable
{

    private static final long serialVersionUID = -6815107739882680299L;
    private Boolean blocked;
    private Boolean flagged;
    private String moderationId;

}
