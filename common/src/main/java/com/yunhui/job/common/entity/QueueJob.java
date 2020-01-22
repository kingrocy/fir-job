package com.yunhui.job.common.entity;

import lombok.*;

/**
 * @Date : 2020-01-10 10:22
 * @Author : dushaoyun
 */

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class QueueJob extends Job {

    private Integer id;
    private String name;
    /**
     * 延时策略
     **/
    private DelayStrategy strategy;

    /**
     * job所属应用
     */
    private String appName;

    private String jobHandler;

    private String params;

    @Override
    public Integer getJobId() {
        return id;
    }

    @Override
    public String getJobName() {
        return name;
    }

    @Override
    public DelayStrategy getDelayStrategy() {
        return strategy;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getJobHandler() {
        return jobHandler;
    }

    @Override
    public String getParams() {
        return params;
    }
}
