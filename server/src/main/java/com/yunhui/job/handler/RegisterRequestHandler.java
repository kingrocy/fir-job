package com.yunhui.job.handler;

import com.yunhui.job.common.proto.RequestProto;
import com.yunhui.job.connect.ConnectPool;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Date : 2021/1/18 11:05 上午
 * @Author : dushaoyun
 */
@Slf4j
public class RegisterRequestHandler implements RequestHandler {

    private ConnectPool connectPool;

    public RegisterRequestHandler(ConnectPool connectPool) {
        if(connectPool==null){
            throw new IllegalArgumentException("connectPool is null");
        }
        this.connectPool = connectPool;
    }

    @Override
    public void handlerRequest(RequestProto.Request request, ChannelHandlerContext ctx) {
        List<RequestProto.Param> paramsList = request.getParamsList();
        //拿appName+ip 与当前连接一起注册起来
        String appName = null;
        String ip = null;
        for (RequestProto.Param param : paramsList) {
            if (param.getKey().equals("ip")) {
                ip = param.getValue();
            }
            if (param.getKey().equals("appName")) {
                appName = param.getValue();
            }
        }
        if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(appName)) {
            log.error("ip or appName has a null value");
            return;
        }
        //将连接注册到连接池中
        connectPool.registerConnect(ip, appName, ctx);
        log.info("appName:{},ip:{} register success", appName, ip);
    }
}
