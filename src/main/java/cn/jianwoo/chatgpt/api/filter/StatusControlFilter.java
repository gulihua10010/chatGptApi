package cn.jianwoo.chatgpt.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.cache.Cache;
import cn.jianwoo.chatgpt.api.base.BaseResponseDto;
import cn.jianwoo.chatgpt.api.constants.CacheKey;
import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.constants.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gulihua
 * @Description
 * @date 2022-04-15 15:11
 */

@Component
@Slf4j
public class StatusControlFilter implements Filter
{

    @Autowired
    private Cache<String, String> fifuCache;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String status = fifuCache.get(CacheKey.STATUS);
        if (!Constants.TRUE.equalsIgnoreCase(status))
        {
            httpServletResponse.setContentType(Constants.CONTENT_TYPE_JSON);
            httpServletResponse.getWriter()
                    .write(JSONObject.toJSONString(new BaseResponseDto(ExceptionConstants.SERVER_SHUT_DOWN,
                            ExceptionConstants.SERVER_SHUT_DOWN_DESC)));
            return;
        }

        chain.doFilter(request, response);
    }
}
