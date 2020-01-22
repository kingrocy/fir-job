package com.yunhui.job.factory;

import com.yunhui.job.anno.JobHandlerName;
import com.yunhui.job.handler.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date : 2020/1/21 5:36 下午
 * @Author : dushaoyun
 */
@Slf4j
public class JobHandlerFactory {

    private Map<String, JobHandler> jobHandlerMap = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    public JobHandlerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        init();
    }

    public JobHandler getJobHandler(String name) {
        return jobHandlerMap.get(name);
    }

    private void init() {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(JobHandlerName.class);
        if (serviceBeanMap == null) {
            log.error("application has no jobHandler...");
        }
        for (Map.Entry<String, Object> entry : serviceBeanMap.entrySet()) {
            Object value = entry.getValue();
            JobHandler jobHandler = (JobHandler) value;
            String name = jobHandler.getClass().getAnnotation(JobHandlerName.class).value();
            jobHandlerMap.put(name, jobHandler);
        }
    }

}
