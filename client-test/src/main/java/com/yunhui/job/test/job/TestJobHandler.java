package com.yunhui.job.test.job;

import com.yunhui.job.anno.JobHandlerName;
import com.yunhui.job.common.constant.RequestStatus;
import com.yunhui.job.handler.JobHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Date : 2020/1/21 5:58 下午
 * @Author : dushaoyun
 */
@JobHandlerName("testJobHandler")
@Slf4j
public class TestJobHandler implements JobHandler {


    @Override
    public RequestStatus invoke(String params) {
        log.info("start execute job,params:{},date:{}", params, new Date());
        return RequestStatus.SUCCESS;
    }
}
