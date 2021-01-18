package com.yunhui.job.handler;

import com.yunhui.job.common.proto.RequestProto;
import com.yunhui.job.dao.BasicDao;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Date : 2021/1/18 11:16 上午
 * @Author : dushaoyun
 */
@Slf4j
public class ResponseRequestHandler implements RequestHandler {

    private BasicDao basicDao;

    public ResponseRequestHandler(BasicDao basicDao) {
        if (basicDao == null) {
            throw new IllegalArgumentException("basicDao null");
        }
        this.basicDao = basicDao;
    }

    @Override
    public void handlerRequest(RequestProto.Request request, ChannelHandlerContext ctx) {
        List<RequestProto.Param> paramsList = request.getParamsList();
        //job执行回调
        String recordId = null;
        String status = null;
        for (RequestProto.Param param : paramsList) {
            if (param.getKey().equals("status")) {
                status = param.getValue();
            }
            if (param.getKey().equals("recordId")) {
                recordId = param.getValue();
            }
        }
        processResponse(recordId, status);
    }

    private void processResponse(String recordId, String status) {
        int result = basicDao.updateJobExecuteRecord(Long.valueOf(recordId), Integer.parseInt(status));
        log.info("updateJobExecuteRecord,recordId:{},status:{},result:{}", recordId, status, result);
    }
}
