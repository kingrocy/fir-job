package com.yunhui.job.handler;

import com.yunhui.job.common.constant.RequestStatus;

/**
 * @Date : 2020/1/21 1:42 下午
 * @Author : dushaoyun
 */
public interface JobHandler {

    /**
     * job执行接口
     *
     * @param params
     * @return
     */
    RequestStatus invoke(String params);
}
