package com.yunhui.job.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yunhui.job.common.proto.RequestProto;
import com.yunhui.job.connect.ConnectPool;
import com.yunhui.job.dao.BasicDao;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Date : 2020/1/21 9:39 上午
 * @Author : dushaoyun
 */
@Slf4j
@AllArgsConstructor
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ConnectPool connectPool;

    @Getter
    private BasicDao basicDao;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestProto.Request request = decode(msg);
        log.info("channelRead request:{}", request);
        processRequest(request, ctx);
    }


    private void processRequest(RequestProto.Request request, ChannelHandlerContext ctx) {
        List<RequestProto.Param> paramsList = request.getParamsList();
        switch (request.getType()) {
            case PING:
                break;
            case PONG:
                break;
            case REQUEST:
                //注册
                break;
            case RESPONSE:
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
                break;
            case REGISTER:
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
                    break;
                }
                //将连接注册到连接池中
                connectPool.registerConnect(ip, appName, ctx);
                log.info("appName:{},ip:{} register success", appName, ip);
                break;
            default:
                break;
        }
    }

    private void processResponse(String recordId, String status) {
        int result = basicDao.updateJobExecuteRecord(Long.valueOf(recordId), Integer.parseInt(status));
        log.info("updateJobExecuteRecord,recordId:{},status:{},result:{}", recordId, status, result);
    }

    public ChannelFuture sendMsg(ChannelHandlerContext ctx, Object msg) {
        RequestProto.Request request = (RequestProto.Request) msg;
        ByteBuf byteBuf = ctx.channel().alloc().heapBuffer();
        byteBuf.writeBytes(request.toByteArray());
        if (ctx.channel().isActive()) {
            return ctx.channel().writeAndFlush(byteBuf);
        }
        return null;
    }

    public RequestProto.Request decode(Object msg) throws InvalidProtocolBufferException {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        byteBuf.release();
        RequestProto.Request request = RequestProto.Request.parseFrom(data);
        return request;
    }
}
