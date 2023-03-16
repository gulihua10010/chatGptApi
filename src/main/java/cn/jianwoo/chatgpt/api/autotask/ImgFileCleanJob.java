package cn.jianwoo.chatgpt.api.autotask;

import java.io.File;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.jianwoo.chatgpt.api.constants.Constants;
import cn.jianwoo.chatgpt.api.exception.JwBlogException;
import cn.jianwoo.chatgpt.api.util.ApplicationConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GuLihua
 * @Description
 * @date 2021-06-24 20:10
 */
@Slf4j
public class ImgFileCleanJob implements Job
{

    @Override
    public void doProcess() throws JwBlogException
    {
        log.info("====>>AutoTask::ImgFileCleanJob start...");
        try
        {
            ApplicationConfigUtil applicationConfigUtil = SpringUtil.getBean(ApplicationConfigUtil.class);
            if (!Constants.TRUE.equalsIgnoreCase(applicationConfigUtil.getImgFileDelete()))
            {
                return;
            }
            String path = applicationConfigUtil.getUploadPath();
            File uploadDir = new File(path);
            if (uploadDir.exists())
            {
                File[] files = uploadDir.listFiles();
                if (files != null)
                {
                    for (File file : files)
                    {
                        // 删除一天前的数据
                        if (System.currentTimeMillis() - DateUnit.DAY.getMillis() > file.lastModified())
                        {
                            log.info(">>>>Delete file: {}", file.getAbsolutePath());
                            FileUtil.del(file);
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            log.error(">>AutoTask::ImgFileCleanJob exec failed, e:\r\n", e);
        }

        log.info("====>>AutoTask::ImgFileCleanJob end...");
    }

}
