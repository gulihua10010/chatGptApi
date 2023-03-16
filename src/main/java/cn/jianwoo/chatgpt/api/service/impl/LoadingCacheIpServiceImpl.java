package cn.jianwoo.chatgpt.api.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;

import cn.jianwoo.chatgpt.api.service.LoadingCacheIpService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gulihua
 * @Description
 * @date 2022-04-28 15:26
 */
@Service
@Slf4j
public class LoadingCacheIpServiceImpl implements LoadingCacheIpService
{
    @Autowired
    private ApplicationConfigUtil applicationConfigUtil;
    LoadingCache<String, RateLimiter> ipRequestCaches = CacheBuilder.newBuilder().maximumSize(1000)// 设置缓存个数
            .expireAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String s) throws Exception
                {
                    if (s.endsWith("completions"))
                    {
                        return RateLimiter.create(Double.parseDouble(applicationConfigUtil.getCompletionsIpLimit()));
                    }
                    return RateLimiter.create(Double.parseDouble(applicationConfigUtil.getIpLimit()));
                }
            });

    @Override
    public RateLimiter getIpLimiter(String ipWithInter) throws ExecutionException
    {
        return ipRequestCaches.get(ipWithInter);
    }

}
