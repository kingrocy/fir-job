package com.yunhui.job.work;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yunhui.job.common.entity.Job;
import com.yunhui.job.connect.ConnectPool;

import java.util.concurrent.*;

import com.yunhui.job.handler.ServerHandler;
import com.yunhui.job.queue.JobQueue;

/**
 * @Date : 2020/1/10 2:36 下午
 * @Author : dushaoyun
 */
public class Worker {

    private ConnectPool pool;

    private Executor executor;

    private ServerHandler serverHandler;

    public Worker(ConnectPool connectPool,ServerHandler serverHandler) {
        pool = connectPool;
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Record-Bury-Thread-%d").build();
        executor = new ThreadPoolExecutor(10, 20, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        this.serverHandler=serverHandler;
    }


    public void work(Job job, JobQueue jobQueue) {
        executor.execute(new WorkRunnable(job, jobQueue, pool,serverHandler));
    }

}
