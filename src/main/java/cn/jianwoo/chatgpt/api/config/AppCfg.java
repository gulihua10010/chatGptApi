package cn.jianwoo.chatgpt.api.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.StrUtil;
import cn.jianwoo.chatgpt.api.bo.FreeDemoApiKeyBO;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cn.jianwoo.chatgpt.api.bo.ProxyBO;
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
        cache.put("isDebug", "FALSE");
        cache.put("demoLimit", applicationConfigUtil.getDemoLimit());
        cache.put("proxyBaseUrl", applicationConfigUtil.getChatgptProxy());
        return cache;
    }


    @Bean("freeDemoApiKeyBO")
    @ConditionalOnMissingBean
    public FreeDemoApiKeyBO freeDemoApiKeyBO()
    {
        String listStr = applicationConfigUtil.getDemoTokenList();
        List<String> list = StrUtil.splitTrim(listStr,",");
        FreeDemoApiKeyBO demo = new FreeDemoApiKeyBO();
        demo.setApiKeyList(list);
        demo.setIndex(-1);
        return demo;
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
