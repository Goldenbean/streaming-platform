package raptor.streaming.server.component;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
public class ThreadPoolConfig {

  private static final int corePoolSize = 4;          // 核心线程数（默认线程数）
  private static final int maxPoolSize = 10;          // 最大线程数
  private static final int keepAliveTime = 10;      // 允许线程空闲时间（单位：默认为秒）
  private static final int queueCapacity = 10;      // 缓冲队列数
  private static final String threadNamePrefix = "Async-Service-"; // 线程池名前缀

  @Bean("clusterExecutor") // bean的名称，默认为首字母小写的方法名
  public ThreadPoolTaskExecutor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setKeepAliveSeconds(keepAliveTime);
    executor.setThreadNamePrefix(threadNamePrefix);

    // 线程池对拒绝任务的处理策略
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 初始化
    executor.initialize();
    return executor;
  }
}