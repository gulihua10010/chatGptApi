package cn.jianwoo.chatgpt.api.controller;

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
        String announcement = fifuCache.get("announcement");
        AnnouncementResponse response = AnnouncementResponse.getInstance();
        if (announcement != null)
        {
            response.setContent(announcement);
        }
        return super.responseToJSONString(response);

    }

}
