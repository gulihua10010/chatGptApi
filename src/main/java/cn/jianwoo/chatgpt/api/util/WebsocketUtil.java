package cn.jianwoo.chatgpt.api.util;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import com.google.common.util.concurrent.RateLimiter;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.jianwoo.chatgpt.api.service.LoadingCacheIpService;
import cn.jianwoo.chatgpt.api.service.impl.LoadingCacheIpServiceImpl;
import lombok.extern.log4j.Log4j2;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-27 22:02
 */
@Log4j2
public class WebsocketUtil
{
    /**
     *
     * 获取 ip
     */
    public static InetSocketAddress getRemoteAddress(Session session)
    {
        if (session == null)
        {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();

        // 在Tomcat 8.0.x版本有效
        InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async,
                "base#sos#socketWrapper#socket#sc#remoteAddress");
        // 在Tomcat 8.5以上版本有效
        InetSocketAddress addr1 = (InetSocketAddress) getFieldInstance(async,
                "base#socketWrapper#socket#sc#remoteAddress");
        return addr1;
    }


    private static Object getFieldInstance(Object obj, String fieldPath)
    {
        String fields[] = fieldPath.split("#");
        for (String field : fields)
        {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null)
            {
                return null;
            }
        }

        return obj;
    }


    private static Object getField(Object obj, Class<?> clazz, String fieldName)
    {
        for (; clazz != Object.class; clazz = clazz.getSuperclass())
        {
            try
            {
                Field field;
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            }
            catch (Exception e)
            {}
        }

        return null;
    }


    /**
     *
     * 清除缓存中的限制
     */
    public static void cleanCache(String ip)
    {
        TimedCache<String, String> timedCache = SpringUtil.getBean("timedCache");
        String key = "req_limit_".concat(ip);
        timedCache.remove(key);
    }


    /**
     * 限制未登录用户每天请求次数(基于 ip)
     **/
    public static boolean check(String ip, int limitCount)
    {
        try
        {
            if (StrUtil.isBlank(ip))
            {
                log.warn(">>>>The ip address to be checked is empty!!!!");
                return true;
            }
            TimedCache<String, String> timedCache = SpringUtil.getBean("timedCache");

            long limitTime = DateUnit.DAY.getMillis();

            String key = "req_limit_".concat(ip);

            String cache = timedCache.get(key, false);
            if (null == cache)
            {
                String value = "1_" + System.currentTimeMillis();
                timedCache.put(key, value, limitTime);
            }
            else
            {
                String value = cache;
                String[] s = value.split("_");
                int count = Integer.parseInt(s[0]);

                if (count >= limitCount)
                {
                    log.info("User IP[{}], exceeded the limit number of times [{}]", ip, limitCount);
                    return false;
                }

                value = (count + 1) + "_" + s[1];
                long last = limitTime - (System.currentTimeMillis() - Long.parseLong(s[1]));
                if (last > 0)
                {
                    timedCache.put(key, value, limitTime);
                }
            }

        }
        catch (Exception e)
        {
            log.error(">>>>check user request limit exec failed, e:", e);
            return true;
        }
        return true;

    }


    /**
     * 查询用户每天请求剩余次数(基于 ip)
     **/
    public static int query(String ip, int limitCount)
    {
        try
        {
            if (StrUtil.isBlank(ip))
            {
                log.warn(">>>>The ip address to be checked is empty!!!!");
                return limitCount;
            }
            TimedCache<String, String> timedCache = SpringUtil.getBean("timedCache");

            String key = "req_limit_".concat(ip);

            String cache = timedCache.get(key, false);
            if (null == cache)
            {
                return limitCount;
            }
            else
            {
                String value = cache;
                String[] s = value.split("_");
                int count = Integer.parseInt(s[0]);
                return limitCount - count;
            }

        }
        catch (Exception e)
        {
            log.error(">>>>query user request limit exec failed, e:", e);
            return limitCount;
        }

    }


    /**
     * 限制用户每天请求次数(基于 ip)
     **/
    public static boolean limit(String ip)
    {
        try
        {
            if (StrUtil.isBlank(ip))
            {
                log.warn(">>>>The ip address to be limited is empty!!!!");
                return true;
            }
            LoadingCacheIpService loadingCacheIpService = SpringUtil.getBean(LoadingCacheIpServiceImpl.class);
            RateLimiter limiter = loadingCacheIpService.getIpLimiter(ip + "@completions");
            if (limiter.tryAcquire(200, TimeUnit.MILLISECONDS))
            {
                // 获得令牌（不限制访问）
                return true;
            }
            else
            {
                // 未获得令牌（限制访问）
                return false;
            }

        }
        catch (Exception e)
        {
            log.error(">>>>check user request limit exec failed, e:", e);
            return true;
        }

    }

}
