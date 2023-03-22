package cn.jianwoo.chatgpt.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.jianwoo.chatgpt.api.constants.CacheKey;
import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.dto.res.ConversationDetResponse;
import cn.jianwoo.chatgpt.api.dto.res.ConversationResponse;
import cn.jianwoo.chatgpt.api.dto.res.StatusResponse;
import cn.jianwoo.chatgpt.api.dto.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.cache.Cache;
import cn.jianwoo.chatgpt.api.annotation.IpLimit;
import cn.jianwoo.chatgpt.api.base.BaseController;
import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.dto.res.AnnouncementResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-15 16:19
 */
@RestController
@RequestMapping("/")
@Slf4j
public class IndexController extends BaseController
{
    @Autowired
    private Cache<String, String> fifuCache;

    @GetMapping
    @IpLimit
    public String index()
    {
        return super.responseToJSONString(BaseResponseDto.success());

    }


    /**
     * 获取公告<br/>
     * url:/announcement<br/>
     *
     * @return 返回响应 {@link AnnouncementResponse} status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         content<br/>
     * @author gulihua
     */

    @GetMapping("/announcement")
    @IpLimit
    public String announcement()
    {
        String announcement = fifuCache.get(CacheKey.ANNOUNCEMENT);
        AnnouncementResponse response = AnnouncementResponse.getInstance();
        if (announcement != null)
        {
            response.setContent(announcement);
        }
        return super.responseToJSONString(response);

    }


    @GetMapping("/demoChatList")
    @IpLimit
    public String demoChatList()
    {
        String demoChat = fifuCache.get(CacheKey.DEMO_CHAT);
        ConversationDetResponse response = ConversationDetResponse.getInstance();
        if ("null".equals(demoChat))
        {
            return super.responseToJSONString(response);
        }
        List<MessageVO> list = new ArrayList<>();
        if (demoChat != null)
        {
            List<String> chatList = StrUtil.splitTrim(demoChat, '|');
            if (CollUtil.isNotEmpty(chatList))
            {
                for (String chat : chatList)
                {
                    if (StrUtil.isBlank(chat))
                    {

                        continue;
                    }
                    MessageVO vo = new MessageVO();
                    vo.setId(IdUtil.fastUUID());
                    vo.setRole("assistant");
                    vo.setParent("1");
                    vo.setContent(chat);
                    vo.setHtml(chat.replaceAll("\n", "<br>"));
                    vo.setIsSend(false);
                    vo.setIsDone(true);
                    vo.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    list.add(vo);
                }
            }
            response.setMessageList(list);
        }
        return super.responseToJSONString(response);

    }


    /**
     * 服务状态<br/>
     * url:/status<br/>
     *
     * @return 返回响应 {@link StatusResponse} status(000000-SUCCESS,999999-SYSTEM ERROR)<br/>
     *         status<br/>
     * @author gulihua
     */

    @GetMapping("/status")
    @IpLimit
    public String status()
    {
        String status = fifuCache.get(CacheKey.STATUS);
        StatusResponse response = StatusResponse.getInstance();
        response.setState(false);
        if (Constants.TRUE.equalsIgnoreCase(status))
        {
            response.setState(true);
        }
        return super.responseToJSONString(response);

    }
}
