package cn.jianwoo.chatgpt.api.autotask;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import cn.jianwoo.chatgpt.api.stream.Callback;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gulihua
 * @Description
 * @date 2022-05-07 11:54
 */
@Component
@Async("jianwooAsyncTaskExecutor")
@Slf4j
public class AsyncTaskExec
{
    public Future<String> execTask(Callback callback)
    {
        log.info(">> Start async task jianwooAsyncTaskExecutor");

        callback.call(null);

        log.info(">> End async task jianwooAsyncTaskExecutor");

        return new AsyncResult<>("jianwooAsyncTaskExecutor");

    }

}
