package com.yunhui.job.common.entity;

import lombok.ToString;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Date : 2020-01-10 10:27
 * @Author : dushaoyun
 */
@ToString
public abstract class Job implements Delayed {

    /**
     * 获取jobId
     *
     * @return
     */
    public abstract Integer getJobId();

    /**
     * 获取jobName
     *
     * @return
     */
    public abstract String getJobName();

    /**
     * 获取延时策略
     *
     * @return
     */
    public abstract DelayStrategy getDelayStrategy();

    public abstract String getAppName();

    public abstract String getJobHandler();

    public abstract String getParams();

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.getDelayStrategy().next() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }
}
