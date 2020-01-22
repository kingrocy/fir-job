package com.yunhui.job.bean;

import lombok.*;

import java.util.Date;

/**
 * @Date : 2020/1/21 4:12 下午
 * @Author : dushaoyun
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JobExecuteRecord {

    private Long id;
    private Integer jobId;
    /**
     * job执行状态 0--执行中  1--执行成功  2---执行失败
     */
    private Integer status;
    private Date createTime;
    private Date updateTime;

}
