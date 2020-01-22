package com.yunhui.job.work;

import com.yunhui.job.common.entity.Job;
import com.yunhui.job.queue.JobQueue;

import java.util.concurrent.CompletableFuture;

/**
 * @Date : 2020/1/13 9:30 上午
 * @Author : dushaoyun
 */
public class WorkFactory {

    public static void startWork(JobQueue queue, Worker work) {

        CompletableFuture.runAsync(() -> {
            while (true) {
                Job job = null;
                try {
                    //阻塞式的去查找并移除首元素
                    job = queue.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                work.work(job, queue);
            }
        });
    }

}
