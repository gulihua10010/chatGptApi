package cn.jianwoo.chatgpt.api.websocket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;

import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.IdUtil;
import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.constants.CacheKey;
import cn.jianwoo.chatgpt.api.constants.ExceptionConstants;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.cache.Cache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.jianwoo.chatgpt.api.bo.ChatGptAskReqBO;
import cn.jianwoo.chatgpt.api.bo.ConversationResBO;
import cn.jianwoo.chatgpt.api.config.WebSocketConfigurator;
import cn.jianwoo.chatgpt.api.constants.ChatGptServiceBean;
import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.dto.req.ConversationRequest;
import cn.jianwoo.chatgpt.api.exception.ControllerBizException;
import cn.jianwoo.chatgpt.api.service.ChatGptService;
import cn.jianwoo.chatgpt.api.util.JwUtil;
import cn.jianwoo.chatgpt.api.util.WebsocketUtil;
import cn.jianwoo.chatgpt.api.validation.BizValidation;
import lombok.extern.log4j.Log4j2;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 */
@Component
@ServerEndpoint(value = "/websocket", configurator = WebSocketConfigurator.class)
@Log4j2
public class WebsocketServer
{

    /**
     *
     * 用线程安全的CopyOnWriteArraySet来存放客户端连接的信息
     */
    private static CopyOnWriteArraySet<Session> socketServers = new CopyOnWriteArraySet<>();
    /**
     *
     * websocket封装的session,信息推送，就是通过它来信息推送
     */
    private Session session;
    private String ip;

    private Cache<String, String> cache;
    private ApplicationConfigUtil applicationConfigUtil;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config)
    {
        this.session = session;
        socketServers.add(session);
        this.cache = SpringUtil.getBean("fifuCache");
        this.applicationConfigUtil = SpringUtil.getBean(ApplicationConfigUtil.class);

        HandshakeRequest request = (HandshakeRequest) config.getUserProperties().get(HandshakeRequest.class.getName());
        List<String> strings = request.getHeaders().get("x-real-ip");
        if (CollUtil.isNotEmpty(strings))
        {
            log.debug(">>x-real-ip list:{}", strings);
            this.ip = strings.get(0);
        }
        if (StringUtils.isBlank(this.ip))
        {
            this.ip = WebsocketUtil.getRemoteAddress(session).getAddress().toString().substring(1);
        }
        log.info(">>>>Establish connection,ip:{}", this.ip);
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose()
    {
        socketServers.forEach(session -> {
            if (session.getId().equals(session.getId()))
            {
                socketServers.remove(session);

            }
        });
        log.info(">>>Connection closure");

    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {

        log.info("Message received from session {}: ip {}", session.getId(), this.ip);
        try
        {
            ConversationRequest req = this.convertParam(message, ConversationRequest.class);
            log.info("Message received content: {}", req);
            // socket心跳检测
            if ("ping".equals(req.getHeart()))
            {
                JSONObject data = new JSONObject();
                data.put("heart", "pong");
                data.put("time", System.currentTimeMillis());
                session.getBasicRemote().sendText(JSONObject.toJSONString(data));
                return;
            }
            String status = cache.get(CacheKey.STATUS);
            if (!Constants.TRUE.equalsIgnoreCase(status))
            {
                ConversationResBO resBO = new ConversationResBO();
                resBO.setConversationId(req.getConversationId());
                resBO.setContent(ExceptionConstants.SERVER_SHUT_DOWN_DESC);
                resBO.setIsDone(true);
                resBO.setIsSuccess(false);
                resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                session.getBasicRemote().sendText(JSONObject.toJSONString(resBO));
                return;
            }

            // IP 限流：30 个请求/min/ip
            if (!WebsocketUtil.limit(this.ip))
            {
                ConversationResBO resBO = new ConversationResBO();
                resBO.setConversationId(req.getConversationId());
                resBO.setContent("请求频繁!");
                resBO.setIsDone(true);
                resBO.setIsSuccess(false);
                resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                session.getBasicRemote().sendText(JSONObject.toJSONString(resBO));
                return;
            }
            if (ChatGptServiceBean.API_KEY.getName().equals(req.getAuthType()))
            {
                BizValidation.paramValidate(req.getAuthValue(), "authValue", "Authorization不能为空!");
            }
            if (ChatGptServiceBean.ACCESS_TOKEN.getName().equals(req.getContent()) || req.getIsGenImg())
            {
                BizValidation.paramValidate(req.getContent(), "content", "content不能为空!");
            }
            else if ((ChatGptServiceBean.API_KEY.getName().equals(req.getAuthType()) || req.getIsDemo()) && !req.getIsGenImg())
            {
                BizValidation.paramValidate(req.getMessages(), "message", "message列表不能为空!");
                for (ConversationRequest.ChatMessage msg : req.getMessages())
                {
                    BizValidation.paramValidate(msg.getContent(), "content", "content不能为空!");
                }
            }
            // 获取对应的服务 bean
            ChatGptService chatGptService = SpringUtil.getBean(ChatGptServiceBean.get(req.getAuthType()));

            ChatGptAskReqBO reqBO = new ChatGptAskReqBO();
            reqBO.setContent(req.getContent());

            reqBO.setId(req.getId());
            // 针对 API 方式进行组装数据
            if (CollUtil.isNotEmpty(req.getMessages()))
            {
                List<ChatGptAskReqBO.ChatMessageBO> list = new ArrayList<>();
                for (ConversationRequest.ChatMessage msg : req.getMessages())
                {
                    if (StrUtil.isBlank(msg.getContent()))
                    {
                        continue;
                    }
                    ChatGptAskReqBO.ChatMessageBO chat = new ChatGptAskReqBO.ChatMessageBO();
                    chat.setRole(msg.getRole());
                    chat.setContent(msg.getContent());
                    list.add(chat);
                }
                reqBO.setMessages(list);

            }
            /***** 测试代码***** TEST *****可删 *****/

            if (Constants.TRUE.equals(cache.get(CacheKey.IS_DEBUG)))
            {
                if (!WebsocketUtil.check(this.ip, 5))
                {
                    log.info("WebsocketUtil.check{} {} ", this.ip, 5);
                    ConversationResBO resBO = new ConversationResBO();
                    resBO.setConversationId(req.getConversationId());
                    resBO.setContent("今天请求已达最大限制数。");
                    resBO.setIsDone(true);
                    resBO.setIsSuccess(false);
                    resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    session.getBasicRemote().sendText(JSONObject.toJSONString(resBO));
                    return;

                }
                if (StrUtil.isBlank(reqBO.getContent()) && CollUtil.isNotEmpty(reqBO.getMessages())) {
                    reqBO.setContent(reqBO.getMessages().get(0).getContent());
                }
                req.setAuthType(ChatGptServiceBean.BAIXING.getName());
                chatGptService = SpringUtil.getBean(ChatGptServiceBean.get(req.getAuthType()));
                chatGptService.conversation(reqBO, param -> {
                    if (!param.getIsSuccess())
                    {
                        WebsocketUtil.cleanCache(this.ip);

                    }
                    if (null != param.getContent())
                    {
                        param.setConversationId(req.getConversationId());
                        try
                        {
                            log.info("onMessageon.send: {}", param.toString());

                            session.getBasicRemote().sendText(JSONObject.toJSONString(param));
                        }
                        catch (IOException e)
                        {
                            WebsocketUtil.cleanCache(this.ip);
                            log.error("websocket.onMessage.sendText:", e);
                        }
                    }
                });
                sendDone(req.getConversationId());
                return;
            }
            /**************** TEST ****************/

            if (!req.getIsDemo())
            {
                reqBO.setParentId(req.getParentId());
                reqBO.setConversationId(req.getConversationId());
                reqBO.setAuthValue(req.getAuthValue());
            }
            else
            {
                // 未登录用户的请求
                req.setAuthType(ChatGptServiceBean.DEMO.getName());
                chatGptService = SpringUtil.getBean(ChatGptServiceBean.get(req.getAuthType()));

                if ("查询次数".equals(StrUtil.trimToEmpty(req.getContent())))
                {

                    ConversationResBO resBO = new ConversationResBO();
                    resBO.setConversationId(req.getConversationId());
                    resBO.setContent(String
                            .valueOf(WebsocketUtil.query(this.ip, Integer.parseInt(cache.get(CacheKey.DEMO_LIMIT)))));
                    resBO.setIsDone(true);
                    resBO.setIsSuccess(false);
                    resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    session.getBasicRemote().sendText(JSONObject.toJSONString(resBO));
                    return;
                }
                // 未登录用户每天只能 20 个请求
                if (req.getIsSender()
                        && !WebsocketUtil.check(this.ip, Integer.parseInt(cache.get(CacheKey.DEMO_LIMIT))))
                {
                    log.info("WebsocketUtil.debug.check{} {} ", this.ip,
                            Integer.parseInt(cache.get(CacheKey.DEMO_LIMIT)));
                    ConversationResBO resBO = new ConversationResBO();
                    resBO.setConversationId(req.getConversationId());
                    resBO.setContent("今天请求已达最大限制数。");
                    resBO.setIsDone(true);
                    resBO.setIsSuccess(false);
                    resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    session.getBasicRemote().sendText(JSONObject.toJSONString(resBO));
                    return;

                }
            }

            log.debug("onMessageon.receive: {}", reqBO.toString());
            if (!req.getIsGenImg())
            {
                // 异步流式请求
                chatGptService.conversation(reqBO, param -> {
                    if (!param.getIsSuccess())
                    {
                        WebsocketUtil.cleanCache(this.ip);

                    }
                    if (null != param.getContent())
                    {
                        param.setConversationId(req.getConversationId());
                        try
                        {
                            log.info("onMessageon.send: {}", param.toString());

                            session.getBasicRemote().sendText(JSONObject.toJSONString(param));
                        }
                        catch (IOException e)
                        {
                            WebsocketUtil.cleanCache(this.ip);
                            log.error("websocket.onMessage.sendText:", e);
                        }
                    }
                });
            }
            else
            {
                // AI 生成图片
                String url = chatGptService.aiGenImage(reqBO);
                if (null != url)
                {

                    // 由于微信小程序不支持未备案的域名，所以只能保存在本地 QAQ
                    String fileName = IdUtil.fastUUID() + ".png";
                    HttpUtil.downloadFile(url,
                            FileUtil.file(applicationConfigUtil.getUploadPath() + File.separator + fileName),
                            new StreamProgress() {

                                @Override
                                public void start()
                                {
                                }


                                @Override
                                public void progress(long progressSize)
                                {
                                }


                                @Override
                                public void finish()
                                {
                                    log.debug(">>>>download complete");
                                    try
                                    {

                                        String newUrl = applicationConfigUtil.getBaseUrl() + "/res/" + fileName;
                                        log.info("onMessageon.send: {}", newUrl);

                                        ConversationResBO resBO = new ConversationResBO();
                                        resBO.setConversationId(req.getConversationId());
                                        resBO.setUrl(newUrl);
                                        resBO.setContent("[图片]");
                                        resBO.setIsImg(true);
                                        resBO.setId(JwUtil.generateUserId());
                                        resBO.setIsDone(false);
                                        resBO.setIsSuccess(true);
                                        resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                        session.getBasicRemote().sendText(JSONObject.toJSONString(resBO));
                                        sendDone(req.getConversationId());

                                    }
                                    catch (Exception e)
                                    {
                                        WebsocketUtil.cleanCache(ip);
                                        log.error("websocket.onMessage.sendText:", e);
                                    }
                                }
                            });

                }
            }
        }
        catch (Exception e)
        {
            log.error("websocket.onMessage:", e);
            try
            {
                ConversationRequest req = this.convertParam(message, ConversationRequest.class);
                ConversationResBO resBO = new ConversationResBO();
                resBO.setConversationId(req.getConversationId());
                resBO.setContent(e.getMessage());
                resBO.setIsDone(true);
                resBO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                session.getBasicRemote().sendText(JSONObject.toJSONString(resBO));
            }
            catch (Exception ex)
            {
                log.error("websocket.onMessage.sendText:", e);
            }
        }

    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error)
    {
        socketServers.forEach(session_ -> {
            if (session_.getId().equals(session.getId()))
            {
                socketServers.remove(session_);
            }
        });
        log.error("websocket.onError:", error);

    }


    public <T> T convertParam(String param, Class<T> class1) throws ControllerBizException
    {
        T result;
        if (StringUtils.isBlank(param))
        {
            throw ControllerBizException.JSON_IS_NULL.print();
        }
        try
        {
            result = JSONObject.parseObject(param, class1);
        }
        catch (Exception e)
        {
            log.error("Parameter conversion failed, JSON string exception: e" + e.getMessage(), e);
            throw ControllerBizException.JSON_CONVERT_ERROR.print();
        }
        return result;
    }


    /***
     * 发送完成标志位
     */
    private void sendDone(String sessionId) throws IOException
    {
        ConversationResBO resBO = new ConversationResBO();
        resBO.setConversationId(sessionId);
        resBO.setIsDone(true);
        resBO.setIsSuccess(true);
        resBO.setContent("DONE");
        session.getBasicRemote().sendText(JSONObject.toJSONString(resBO));
    }
}
