package cn.jianwoo.chatgpt.api.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件发送工具类
 *
 * @author GuLihua
 * @Description
 * @date 2021-07-13 10:42
 */
@Slf4j
@Component
public class NotifiyUtil
{

    @Autowired
    private ApplicationConfigUtil applicationConfigUtil;

    private void sendEmail(List<String> emailTo, String subject, String content, boolean isHtml) throws JwBlogException
    {
        log.info(">>Start Send Email::emailTo = [{}], subject = [{}], content = [{}]", JSON.toJSONString(emailTo),
                subject, content);
        try
        {

            MailAccount account = fetchMailAcct();
            log.info("<<Fetch Email account::[{}]", JSON.toJSONString(account));

            MailUtil.send(account, emailTo, subject, content, isHtml);

        }
        catch (Exception e)
        {
            log.error(">>Start Send Email failed, e\r\n", e);
            throw new JwBlogException(e);

        }
    }


    private MailAccount fetchMailAcct() throws JwBlogException
    {
        MailAccount account = null;

        String host = applicationConfigUtil.getHost();
        String port = applicationConfigUtil.getPort();
        String sender = applicationConfigUtil.getSender();
        String user = applicationConfigUtil.getSender();
        String senderNick = applicationConfigUtil.getSenderNick();
        String pwd = applicationConfigUtil.getPwd();

        account = new MailAccount();
        account.setHost(host);
        account.setPort(new BigDecimal(port).intValue());
        account.setAuth(true);
        account.setFrom(String.format("%s <%s>", senderNick, sender));
        account.setUser(user);
        account.setPass(pwd);
        account.setSslEnable(true);
        account.setSocketFactoryClass("javax.net.ssl.SSLSocketFactory");
        account.setSocketFactoryFallback(false);
        account.setSocketFactoryPort(new BigDecimal(port).intValue());

        account.setStarttlsEnable(true);

        return account;

    }


    /**
     * 发送邮件(纯文本)
     *
     * @param emailTos 收件人(支持多个)
     * @param subject 主题
     * @param content 内容
     * @author gulihua
     */
    public void sendEmailText(List<String> emailTos, String subject, String content) throws JwBlogException
    {
        sendEmail(emailTos, subject, content, false);
    }


    /**
     * 发送邮件(纯文本)
     *
     * @param emailTo 收件人
     * @param subject 主题
     * @param content 内容
     * @author gulihua
     */
    public void sendEmailText(String emailTo, String subject, String content) throws JwBlogException
    {
        sendEmail(Collections.singletonList(emailTo), subject, content, false);
    }


    /**
     * 发送邮件(支持HTML)
     *
     * @param emailTo 收件人
     * @param subject 主题
     * @param content 内容
     * @author gulihua
     */
    public void sendEmail(String emailTo, String subject, String content) throws JwBlogException
    {
        sendEmail(Collections.singletonList(emailTo), subject, content, true);
    }

}
