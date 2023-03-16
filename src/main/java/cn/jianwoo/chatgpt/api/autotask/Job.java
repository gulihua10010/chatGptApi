package cn.jianwoo.chatgpt.api.autotask;


import cn.jianwoo.chatgpt.api.exception.JwBlogException;

/**
 * @author GuLihua
 * @Description
 * @date 2021-07-02 16:22
 */
public interface Job {
    /**
     * 自动任务处理
     *
     * @return
     * @author gulihua
     */
    void doProcess() throws JwBlogException;
}
