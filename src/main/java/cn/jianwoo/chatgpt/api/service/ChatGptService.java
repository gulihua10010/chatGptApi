package cn.jianwoo.chatgpt.api.service;

import cn.jianwoo.chatgpt.api.bo.ChatGptAskReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationDetResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsResBO;
import cn.jianwoo.chatgpt.api.bo.GenTitleReqBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsResBO;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.stream.Callback;

import java.util.List;

/**
 * chatGpt服务
 *
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @date 2023-02-15 14:29
 */
public interface ChatGptService
{

    /**
     *
     * 验证 apiKey 或者 token是否正确
     *
     * @author gulihua
     * @param authValue apiKey 或者 token
     */
    void verify(String authValue) throws JwBlogException;


    /**
     *
     * 聊天
     *
     * @author gulihua
     * @param askBO 问题参数
     * @param callback 接收事件流时的回调方法
     */
    void conversation(ChatGptAskReqBO askBO, Callback<ConversationResBO> callback) throws JwBlogException;


    /**
     *
     * AI 生成图片
     *
     * @author gulihua
     * @param askBO 描述参数
     * @return Base64图片数据
     *
     */
    String aiGenImage(ChatGptAskReqBO askBO) throws JwBlogException;




    /**
     * 查询会话详情
     *
     * @param id 会话 id
     * @param authValue apiKey 或者 token
     * @date 16:21 2023/2/22
     * @author gulihua
     *
     * @return ConversationDetResBO
     **/
    ConversationDetResBO getConversation(String authValue, String id) throws JwBlogException;


    /**
     * 获取会话列表
     *
     * @param reqBO 请求参数
     * @date 16:21 2023/2/22
     * @author gulihua
     *
     * @return List<ConversationsResBO>
     **/
    List<ConversationsResBO> getConversationList(ConversationsReqBO reqBO) throws JwBlogException;


    /**
     * 同步消息/会话
     *
     * @param reqBO 请求参数
     * @date 16:21 2023/2/22
     * @author gulihua
     *
     * @return ModerationsResBO
     **/
    ModerationsResBO moderation(ModerationsReqBO reqBO) throws JwBlogException;


    /**
     * 生成标题
     *
     * @param reqBO 请求参数
     * @date 16:21 2023/2/22
     * @author gulihua
     *
     * @return String
     **/
    String genTitle(GenTitleReqBO reqBO) throws JwBlogException;
}
