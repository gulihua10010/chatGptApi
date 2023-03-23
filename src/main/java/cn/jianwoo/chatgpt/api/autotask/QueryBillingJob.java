package cn.jianwoo.chatgpt.api.autotask;

import java.util.Calendar;

import cn.hutool.cache.Cache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.jianwoo.chatgpt.api.bo.CreditGrantsResBO;
import cn.jianwoo.chatgpt.api.constants.CacheKey;
import cn.jianwoo.chatgpt.api.constants.ChatGptServiceBean;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.service.ChatGptService;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import cn.jianwoo.chatgpt.api.util.NotifiyUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GuLihua
 * @Description
 * @date 2021-06-24 20:10
 */
@Slf4j
public class QueryBillingJob implements Job
{

    @Override
    public void doProcess() throws JwBlogException
    {
        log.info("====>>AutoTask::QueryBillingJob start...");
        try
        {
            ChatGptService chatGptService = SpringUtil.getBean(ChatGptServiceBean.API_KEY.getBean());
            NotifiyUtil notifiyUtil = SpringUtil.getBean(NotifiyUtil.class);
            ApplicationConfigUtil applicationConfigUtil = SpringUtil.getBean(ApplicationConfigUtil.class);
            AsyncTaskExec asyncTaskExec = SpringUtil.getBean(AsyncTaskExec.class);
            Cache<String, String> cache = SpringUtil.getBean("fifuCache");

            CreditGrantsResBO resBO = chatGptService
                    .queryBillingCreditGrants("Bearer " + cache.get(CacheKey.DEMO_API_KEY));
            asyncTaskExec.execTask(param1 -> {
                try
                {
                    String expired = "";
                    if (CollUtil.isNotEmpty(resBO.getGrants().getData()))
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(resBO.getGrants().getData().get(0).getExpiresAt() * 1000);
                        expired = DateUtil.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
                    }

                    String context = String.format("账号余额:%s<br>过期时间:%s", resBO.getTotalAvailable(), expired);
                    notifiyUtil.sendEmail(applicationConfigUtil.getEmail(), "【chatGpt】Billing", context);
                }
                catch (JwBlogException e)
                {
                    log.error(">>>>>notifiyUtil.sendEmail failed, e: ", e);
                }

            });

        }
        catch (Exception e)
        {
            log.error(">>AutoTask::QueryBillingJob exec failed, e:\r\n", e);
        }

        log.info("====>>AutoTask::QueryBillingJob end...");
    }

}
