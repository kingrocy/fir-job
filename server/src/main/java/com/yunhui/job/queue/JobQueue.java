package com.yunhui.job.queue;

import com.yunhui.job.common.entity.Job;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;

/**
 * @Date : 2020/1/10 2:14 下午
 * @Author : dushaoyun
 */
@Component
public class JobQueue {

    private DelayQueue<Job> delayQueue;

    public JobQueue() {
        delayQueue = new DelayQueue<>();
    }

    public Job get() throws InterruptedException {
        return delayQueue.take();
    }

    public boolean add(Job job) {
        return delayQueue.add(job);
    }
}
