package cn.jianwoo.chatgpt.api.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author GuLihua
 * @Description 异步任务线程池配置
 * @date 2020-12-16 17:59
 */
@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {
    /**
     * 核心线程数（默认线程数）线程池创建时候初始化的线程数
     */
    private static final int CORE_POOL_SIZE = 10;
    /**
     * 最大线程数 线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
     */
    private static final int MAX_POOL_SIZE = 100;
    /**
     * 允许线程空闲时间（单位：默认为秒）当超过了核心线程之外的线程在空闲时间到达之后会被销毁
     */
    private static final int KEEP_ALIVE_TIME = 10;
    /**
     * 缓冲队列数 用来缓冲执行任务的队列
     */
    private static final int QUEUE_CAPACITY = 200;
    /**
     * 线程池名前缀 方便我们定位处理任务所在的线程池
     */
    private static final String THREAD_NAME_PREFIX = "Jianwoo-Async-Service-";

    @Bean("jianwooTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        // 线程池对拒绝任务的处理策略 采用了CallerRunsPolicy策略，
        // 当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；
        // 如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }


    @Bean("jianwooAsyncTaskExecutor")
    public ThreadPoolTaskExecutor jianwooAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        // 线程池对拒绝任务的处理策略 采用了CallerRunsPolicy策略，
        // 当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；
        // 如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
