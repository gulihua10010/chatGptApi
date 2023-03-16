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
public class ConversationsReqBO extends BaseAuthReqBO
{

    private static final long serialVersionUID = -6815107739882680259L;
    private Integer offset;
    private Integer limit;

    public Integer getOffset()
    {
        return this.offset == null ? 1 : offset;
    }


    public void setOffset(Integer offset)
    {
        this.offset = offset;
    }


    public Integer getLimit()
    {
        return this.limit == null ? 20 : limit;
    }


    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }
}
