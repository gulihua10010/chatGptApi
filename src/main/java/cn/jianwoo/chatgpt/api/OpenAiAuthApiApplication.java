package cn.jianwoo.chatgpt.api;

import cn.hutool.cron.CronUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpenAiAuthApiApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(OpenAiAuthApiApplication.class, args);
        CronUtil.start();

    }

}
