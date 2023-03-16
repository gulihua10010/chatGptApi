package cn.jianwoo.chatgpt.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.jianwoo.chatgpt.api.base.BaseRequestDto;
import cn.jianwoo.chatgpt.api.bo.ConversationDetResBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationsResBO;
import cn.jianwoo.chatgpt.api.bo.GenTitleReqBO;
import cn.jianwoo.chatgpt.api.dto.req.GenTitleRequest;
import cn.jianwoo.chatgpt.api.dto.res.AuthResponse;
import cn.jianwoo.chatgpt.api.dto.res.ConversationDetResponse;
import cn.jianwoo.chatgpt.api.dto.res.ConversationsResponse;
import cn.jianwoo.chatgpt.api.dto.res.GenTitleResponse;
import cn.jianwoo.chatgpt.api.dto.vo.ConversationsVO;
import cn.jianwoo.chatgpt.api.dto.vo.MessageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.extra.spring.SpringUtil;
import cn.jianwoo.chatgpt.api.base.BaseController;
import cn.jianwoo.chatgpt.api.bo.ModerationsReqBO;
import cn.jianwoo.chatgpt.api.bo.ModerationsResBO;
import cn.jianwoo.chatgpt.api.constants.ChatGptServiceBean;
import cn.jianwoo.chatgpt.api.dto.req.ModerationsRequest;
import cn.jianwoo.chatgpt.api.dto.res.ModerationsResponse;
import cn.jianwoo.chatgpt.api.service.ChatGptService;
import cn.jianwoo.chatgpt.api.validation.BizValidation;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-15 16:19
 */
@RestController
@RequestMapping("/backend-api")
@Slf4j
public class ChatGptApiController extends BaseController
{
    /**
     * 获取会话列表<br/>
     * url:/backend-api/conversations<br/>
     *
     * @param req JSON 参数({@link BaseRequestDto})<br/>
     *            offset<br/>
     *            limit<br/>
     * @return 返回响应 {@link AuthResponse}<br/>
     *         status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         msg<br/>
     *         list<br/>
     *         --createTime<br/>
     *         --id<br/>
     *         --title<br/>
     * @author gulihua
     */
    @GetMapping("/conversations")
    public String fetchConversations(@RequestHeader("Authorization")
    String token, BaseRequestDto req)
    {
        try
        {
            ConversationsReqBO reqBO = new ConversationsReqBO();
            reqBO.setAuthValue(token);
            reqBO.setLimit(req.getLimit());
            reqBO.setOffset(req.getOffset());
            ChatGptService chatGptService = SpringUtil.getBean(ChatGptServiceBean.get(req.getAuthType()));
            List<ConversationsResBO> resList = chatGptService.getConversationList(reqBO);
            ConversationsResponse response = ConversationsResponse.getInstance();
            List<ConversationsVO> list = new ArrayList<>();
            if (CollUtil.isNotEmpty(resList))
            {
                for (ConversationsResBO res : resList)
                {
                    ConversationsVO vo = new ConversationsVO();
                    vo.setId(res.getId());
                    vo.setCreateTime(res.getCreateTime());
                    vo.setTitle(res.getTitle());
                    list.add(vo);
                }
            }
            response.setList(list);
            return super.responseToJSONString(response);
        }
        catch (Exception e)
        {
            return super.exceptionToString(e);

        }
    }


    /**
     * 获取会话详情<br/>
     * url:/backend-api/conversation<br/>
     *
     * @param req JSON 参数({@link BaseRequestDto})<br/>
     *            id<br/>
     * @return 返回响应 {@link AuthResponse}<br/>
     *         status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         msg<br/>
     *         currentNode<br/>
     *         title<br/>
     *         messageList<br/>
     *         --type<br/>
     *         --content<br/>
     *         --id<br/>
     *         --parent<br/>
     *         --title<br/>
     *         --role<br/>
     *         --createTime<br/>
     *         --isSend<br/>
     * @author gulihua
     */
    @GetMapping("/conversation")
    public String fetchConversationDet(@RequestHeader("Authorization")
    String token, BaseRequestDto req)
    {
        try
        {
            BizValidation.paramValidate(req.getId(), "id", "id不能为空!");
            ChatGptService chatGptService = SpringUtil.getBean(ChatGptServiceBean.get(req.getAuthType()));
            ConversationDetResBO resBO = chatGptService.getConversation(token, req.getId());
            ConversationDetResponse response = ConversationDetResponse.getInstance();
            List<MessageVO> list = new ArrayList<>();
            resBO.setCurrentNode(resBO.getCurrentNode());
            resBO.setTitle(resBO.getTitle());
            if (CollUtil.isNotEmpty(resBO.getMessageList()))
            {
                for (ConversationDetResBO.MessageBO res : resBO.getMessageList())
                {
                    MessageVO vo = new MessageVO();
                    vo.setId(res.getId());
                    vo.setType(res.getType());
                    vo.setParent(res.getParent());
                    vo.setContent(res.getContent());
                    vo.setHtml(res.getHtml());
                    vo.setRole(res.getRole());
                    vo.setCreateTime(res.getCreateTime());
                    vo.setIsSend("user".equals(res.getRole()));
                    list.add(vo);
                }
            }
            response.setMessageList(list);
            return super.responseToJSONString(response);
        }
        catch (Exception e)
        {
            return super.exceptionToString(e);

        }
    }


    /**
     * 同步消息/会话<br/>
     * url:/backend-api/moderation<br/>
     *
     * @param param JSON 参数({@link ModerationsRequest})<br/>
     *            conversationId<br/>
     *            content<br/>
     *            id<br/>
     * @return 返回响应 {@link ModerationsResponse}<br/>
     *         status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         msg<br/>
     *         blocked<br/>
     *         flagged<br/>
     *         moderationId<br/>
     * @author gulihua
     */
    @PostMapping("/moderation")
    public String doModeration(@RequestHeader("Authorization")
    String token, @RequestBody
    String param)
    {
        try
        {
            ModerationsRequest req = this.convertParam(param, ModerationsRequest.class);
            BizValidation.paramValidate(req.getId(), "id", "id不能为空!");
            BizValidation.paramValidate(req.getContent(), "content", "content不能为空!");
            BizValidation.paramValidate(req.getConversationId(), "conversationId", "conversationId不能为空!");
            ChatGptService chatGptService = SpringUtil.getBean(ChatGptServiceBean.get(req.getAuthType()));
            ModerationsReqBO reqBO = new ModerationsReqBO();
            reqBO.setConversationId(req.getConversationId());
            reqBO.setId(req.getId());
            reqBO.setContent(req.getContent());
            reqBO.setAuthValue(token);
            ModerationsResBO resBO = chatGptService.moderation(reqBO);
            ModerationsResponse response = ModerationsResponse.getInstance();
            response.setBlocked(resBO.getBlocked());
            response.setFlagged(resBO.getFlagged());
            response.setModerationId(resBO.getModerationId());
            return super.responseToJSONString(response);
        }
        catch (Exception e)
        {
            return super.exceptionToString(e);

        }
    }


    /**
     * 生成标题<br/>
     * url:/backend-api/gentitle<br/>
     *
     * @param param JSON 参数({@link GenTitleRequest})<br/>
     *            conversationId<br/>
     *            id<br/>
     * @return 返回响应 {@link AuthResponse}<br/>
     *         status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         msg<br/>
     *         title<br/>
     * @author gulihua
     */
    @PostMapping("/gentitle")
    public String doGenTitle(@RequestHeader("Authorization")
    String token, @RequestBody
    String param)
    {
        try
        {
            GenTitleRequest req = this.convertParam(param, GenTitleRequest.class);
            BizValidation.paramValidate(req.getId(), "id", "id不能为空!");
            BizValidation.paramValidate(req.getConversationId(), "conversationId", "conversationId不能为空!");
            ChatGptService chatGptService = SpringUtil.getBean(ChatGptServiceBean.get(req.getAuthType()));
            GenTitleReqBO reqBO = new GenTitleReqBO();
            reqBO.setConversationId(req.getConversationId());
            reqBO.setId(req.getId());
            reqBO.setAuthValue(token);
            String title = chatGptService.genTitle(reqBO);
            GenTitleResponse response = GenTitleResponse.getInstance();
            response.setTitle(title);
            return super.responseToJSONString(response);
        }
        catch (Exception e)
        {
            return super.exceptionToString(e);

        }
    }

}
