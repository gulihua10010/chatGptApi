package cn.jianwoo.chatgpt.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author gulihua
 */
@Configuration
public class CORSConfig
{
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        // 允许白名单域名进行跨域调用
        config.addAllowedOriginPattern("*");
        // 允许跨越发送cookie
        config.setAllowCredentials(true);
        // 放行全部原始头信息
        config.addAllowedHeader("*");
        // 允许所有请求方法跨域调用
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
