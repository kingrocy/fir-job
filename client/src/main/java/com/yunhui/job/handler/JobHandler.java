package com.yunhui.job.handler;

import com.yunhui.job.common.entity.Result;

/**
 * @Date : 2020/1/21 1:42 下午
 * @Author : dushaoyun
 */
public interface JobHandler {

    /**
     * job执行接口
     * @param params
     * @return
     */
    Result<String> invoke(String params);
}
