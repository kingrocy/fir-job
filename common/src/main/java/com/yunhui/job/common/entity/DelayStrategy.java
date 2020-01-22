package com.yunhui.job.common.entity;
/**
 * @Date : 2020/1/10 11:37 上午
 * @Author : dushaoyun
 */
public interface DelayStrategy{

    /**
     * 类型
     * 自动还是手动 0手动 1自动
     * @return
     */
    int type();


    /**
     * 下一次执行时间
     * @return
     */
    long next();


    /**
     * 设置下一次执行时间
     */
    void updateNext();

}
