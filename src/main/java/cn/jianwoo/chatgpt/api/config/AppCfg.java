package cn.jianwoo.chatgpt.api.config;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import cn.jianwoo.chatgpt.api.bo.ProxyBO;
import cn.jianwoo.chatgpt.api.constants.CacheKey;
import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-21 15:45
 */
@Configuration
@ComponentScan(basePackages = { "cn.hutool.extra.spring" })
@Slf4j
public class AppCfg
{

    @Autowired
    private ApplicationArguments applicationArguments;

    @Autowired
    private ApplicationConfigUtil applicationConfigUtil;

    @Bean
    @ConditionalOnMissingBean
    public ProxyBO getProxy()
    {
        ProxyBO proxyBO = new ProxyBO();
        proxyBO.setFlagArg(false);
        // 启动参数 --proxy=127.0.0.1:7890
        String proxyArgs = null;
        Set<String> optionNames = applicationArguments.getOptionNames();
        for (String optionName : optionNames)
        {
            List<String> optionValues = applicationArguments.getOptionValues(optionName);
            log.warn(">>>>Arguments {}对应的value:{}", optionName, optionValues);
            if ("proxy".equals(optionName))
            {
                proxyArgs = optionValues.get(0);
            }
        }
        if (StringUtils.isNotBlank(proxyArgs))
        {
            String[] argArr = proxyArgs.split(":");
            String proxyHost = argArr[0];
            int proxyPort = 7890;
            if (argArr.length > 1)
            {
                proxyPort = Integer.parseInt(argArr[1]);
            }
            log.info("Using proxy::host={},port={}", proxyHost, proxyPort);
            proxyBO.setFlagArg(true);
            proxyBO.setHost(proxyHost);
            proxyBO.setPort(proxyPort);

        }

        return proxyBO;
    }


    @Bean("fifuCache")
    @ConditionalOnMissingBean
    public Cache fifuCache()
    {
        Cache cache = CacheUtil.newFIFOCache(16);
        cache.put(CacheKey.IS_DEBUG, Constants.FALSE);
        cache.put(CacheKey.STATUS, Constants.TRUE);
        cache.put(CacheKey.DEMO_LIMIT, applicationConfigUtil.getDemoLimit());
        cache.put(CacheKey.DEMO_API_KEY, applicationConfigUtil.getDemoToken());
        cache.put(CacheKey.PROXY_BASE_URL, applicationConfigUtil.getChatgptProxy());
        return cache;
    }


    @Bean("timedCache")
    @ConditionalOnMissingBean
    public TimedCache timedCache()
    {
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(DateUnit.DAY.getMillis());
        timedCache.schedulePrune(DateUnit.DAY.getMillis());
        return timedCache;
    }
}
