package cn.jianwoo.chatgpt.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author gulihua
 * @Description
 * @date 2022-08-04 15:07
 */
@Slf4j
@Component
public class ApplicationConfigUtil
{

    @Value("${default.token}")
    private String defaultToken;

    @Value("${aes.secret}")
    private String aesSecret;

    @Value("${notify.email.host}")
    private String host;
    @Value("${exception.notify}")
    private Boolean isNotify;

    @Value("${notify.email.port}")
    private String port;

    @Value("${notify.email.sender}")
    private String sender;

    @Value("${notify.email.senderNick}")
    private String senderNick;

    @Value("${notify.email.pwd}")
    private String pwd;

    @Value("${admin.email}")
    private String email;

    @Value("${demo.access.token}")
    private String demoToken;

    @Value("${demo.access.limit}")
    private String demoLimit;

    @Value("${chatgpt.proxy}")
    private String chatgptProxy;

    @Value("${ip.limit}")
    private String ipLimit;
    @Value("${completions.ip.limit}")
    private String completionsIpLimit;
    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${base.url}")
    private String baseUrl;

    @Value("${baixing.api.key}")
    private String baixingApiKey;

    @Value("${img.file.delete}")
    private String imgFileDelete;

    public String getCompletionsIpLimit()
    {
        return this.completionsIpLimit;
    }


    public void setCompletionsIpLimit(String completionsIpLimit)
    {
        this.completionsIpLimit = completionsIpLimit;
    }


    public String getImgFileDelete()
    {
        return this.imgFileDelete;
    }


    public void setImgFileDelete(String imgFileDelete)
    {
        this.imgFileDelete = imgFileDelete;
    }


    public String getBaixingApiKey()
    {
        return this.baixingApiKey;
    }


    public void setBaixingApiKey(String baixingApiKey)
    {
        this.baixingApiKey = baixingApiKey;
    }


    public String getBaseUrl()
    {
        return this.baseUrl;
    }


    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }


    public String getUploadPath()
    {
        return this.uploadPath;
    }


    public void setUploadPath(String uploadPath)
    {
        this.uploadPath = uploadPath;
    }


    public String getIpLimit()
    {
        return this.ipLimit;
    }


    public void setIpLimit(String ipLimit)
    {
        this.ipLimit = ipLimit;
    }


    public String getChatgptProxy()
    {
        return this.chatgptProxy;
    }


    public void setChatgptProxy(String chatgptProxy)
    {
        this.chatgptProxy = chatgptProxy;
    }


    public String getDemoLimit()
    {
        return this.demoLimit;
    }


    public void setDemoLimit(String demoLimit)
    {
        this.demoLimit = demoLimit;
    }


    public String getDemoToken()
    {
        return this.demoToken;
    }


    public void setDemoToken(String demoToken)
    {
        this.demoToken = demoToken;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getHost()
    {
        return this.host;
    }


    public void setHost(String host)
    {
        this.host = host;
    }


    public String getPort()
    {
        return this.port;
    }


    public void setPort(String port)
    {
        this.port = port;
    }


    public String getSender()
    {
        return this.sender;
    }


    public void setSender(String sender)
    {
        this.sender = sender;
    }


    public String getSenderNick()
    {
        return this.senderNick;
    }


    public void setSenderNick(String senderNick)
    {
        this.senderNick = senderNick;
    }


    public String getPwd()
    {
        return this.pwd;
    }


    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }


    public String getAesSecret()
    {
        return this.aesSecret;
    }


    public void setAesSecret(String aesSecret)
    {
        this.aesSecret = aesSecret;
    }


    public String getDefaultToken()
    {
        return this.defaultToken;
    }


    public void setDefaultToken(String defaultToken)
    {
        this.defaultToken = defaultToken;
    }


    public Boolean getIsNotify()
    {
        return this.isNotify;
    }


    public void setIsNotify(Boolean notify)
    {
        this.isNotify = notify;
    }
}
