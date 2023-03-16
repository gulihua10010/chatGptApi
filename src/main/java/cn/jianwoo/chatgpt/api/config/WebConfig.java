package cn.jianwoo.chatgpt.api.config;

import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.jianwoo.chatgpt.api.interceptor.LoginHandleInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer
{

    @Autowired
    private ApplicationConfigUtil applicationConfigUtil;
    private final String RES = "/res/**";

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(loginHandleInterceptor()).addPathPatterns("/backend-api/**");

    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler(RES).addResourceLocations(
                Constants.FILE_PROTOCOL + applicationConfigUtil.getUploadPath() + Constants.URL_SEPARATOR);

    }


    @Bean
    public LoginHandleInterceptor loginHandleInterceptor()
    {
        return new LoginHandleInterceptor();
    }

}
