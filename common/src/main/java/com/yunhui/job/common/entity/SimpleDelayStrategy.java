package com.yunhui.job.common.entity;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * @Date : 2020/1/13 9:36 上午
 * @Author : dushaoyun
 * @Desc 一个简单的延时策略 每隔多久执行一次
 */
@NoArgsConstructor
@ToString
public class SimpleDelayStrategy implements DelayStrategy {

    /**
     * 延时多久
     */
    private Integer delay;
    /**
     * 延时单位
     */
    private TimeUnit unit;
    /**
     * 下次执行时间
     */
    private Long next;

    public SimpleDelayStrategy(Integer delay,TimeUnit unit){
        this.delay=delay;
        this.unit=unit;
        setNext();
    }


    private void setNext() {
        switch (unit) {
            case DAYS:
                this.next = System.currentTimeMillis() + 24 * 60 * 60 * 1000 * delay;
                break;
            case HOURS:
                this.next = System.currentTimeMillis() + 60 * 60 * 1000 * delay;
                break;
            case MINUTES:
                this.next = System.currentTimeMillis() + 60 * 1000 * delay;
                break;
            case SECONDS:
                this.next = System.currentTimeMillis() + 1000 * delay;
                break;
            case MILLISECONDS:
                this.next = System.currentTimeMillis() + delay;
                break;
            default:
                throw new UnsupportedOperationException("Upsupported TimeUnit Parameter");
        }
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public long next() {
        return next;
    }

    @Override
    public void updateNext() {
        setNext();
    }
}
