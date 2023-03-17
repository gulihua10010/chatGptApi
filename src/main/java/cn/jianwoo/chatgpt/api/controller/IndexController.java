package cn.jianwoo.chatgpt.api.controller;

import cn.jianwoo.chatgpt.api.constants.CacheKey;
import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.dto.res.StatusResponse;
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
