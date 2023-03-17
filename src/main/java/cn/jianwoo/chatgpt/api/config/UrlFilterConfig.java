package cn.jianwoo.chatgpt.api.config;

import javax.annotation.Resource;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.jianwoo.chatgpt.api.filter.StatusControlFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gulihua
 * @Description
 * @date 2022-04-15 15:36
 */
@Configuration
@Slf4j
public class UrlFilterConfig
{
    @Resource
    private StatusControlFilter statusControlFilter;

    @Bean
    public FilterRegistrationBean ipControlFilterBean()
    {
        FilterRegistrationBean registration = new FilterRegistrationBean(statusControlFilter);
        registration.addUrlPatterns("/backend-api","/api");
        registration.setName("statusControlFilter");
        registration.setOrder(1);
        return registration;
    }
}
