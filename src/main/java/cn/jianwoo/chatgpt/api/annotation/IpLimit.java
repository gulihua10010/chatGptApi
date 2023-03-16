package cn.jianwoo.chatgpt.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IpLimit {
    /**
     * 每秒最多多少次请求
     *
     * @return
     */
    int limit() default -1;

    /**
     * 获取令牌最大等待时间
     */
    long timeout() default 200;

    /**
     * 获取令牌最大等待时间,单位(例:分钟/秒/毫秒) 默认:毫秒
     */
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

}
