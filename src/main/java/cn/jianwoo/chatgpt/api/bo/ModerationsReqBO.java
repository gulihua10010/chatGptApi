package cn.jianwoo.chatgpt.api.bo;

import cn.jianwoo.chatgpt.api.base.BaseAuthReqBO;
import lombok.Data;

/**
 * @author gulihua
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 */
@Data
public class ModerationsReqBO extends BaseAuthReqBO
{

    private static final long serialVersionUID = -6815107739882680299L;
    private String conversationId;
    private String content;
    private String id;

}
