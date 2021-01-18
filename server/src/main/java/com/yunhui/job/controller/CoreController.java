package com.yunhui.job.controller;

import com.yunhui.job.common.entity.DelayStrategy;
import com.yunhui.job.common.entity.QueueJob;
import com.yunhui.job.common.entity.Result;
import com.yunhui.job.common.entity.SimpleDelayStrategy;
import com.yunhui.job.queue.JobQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Date : 2020/1/22 9:26 上午
 * @Author : dushaoyun
 */
@RestController
public class CoreController {

    @Autowired
    JobQueue jobQueue;

    @PostMapping
    public Result addJob() {
        QueueJob job = new QueueJob();
        job.setId(1);
        job.setAppName("test.client");
        job.setJobHandler("testJobHandler");
        DelayStrategy delayStrategy = new SimpleDelayStrategy(5, TimeUnit.SECONDS);
        job.setStrategy(delayStrategy);
        boolean result = jobQueue.add(job);
        return Result.success(result);
    }
}
