package com.csgroup.service.impl;

import com.csgroup.service.JsonFileService;
import com.csgroup.service.LogProcessorService;
import com.csgroup.service.tasks.DbEventTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogProcessorServiceImpl implements LogProcessorService, InitializingBean {

    @Autowired
    private JsonFileService fileService;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${db.queue.read.threads:1}")
    private int dbThreads;

    private ThreadPoolTaskExecutor executor;

    public void process(String filename) {

        long perfStarted = System.currentTimeMillis();
        log.info("PerformanceTimer started=" + perfStarted);
        fileService.readFile(filename);

        executor.shutdown();
        log.info("PerformanceTimer ended=" + (System.currentTimeMillis() - perfStarted));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(dbThreads);
        executor.setMaxPoolSize(dbThreads);
        executor.setThreadNamePrefix("consumer");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();

        for(int i = 0; i<executor.getCorePoolSize();i++) {
            DbEventTask task = applicationContext.getBean(DbEventTask.class);
            executor.execute(task);
        }

    }
}
