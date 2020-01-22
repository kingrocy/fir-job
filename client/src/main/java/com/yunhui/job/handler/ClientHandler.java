package com.yunhui.job.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yunhui.job.common.proto.RequestProto;
import com.yunhui.job.config.JobConfig;
import com.yunhui.job.factory.JobHandlerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Date : 2020/1/21 10:16 上午
 * @Author : dushaoyun
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private JobConfig config;

    private JobHandlerFactory jobHandlerFactory;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //将自身信息注册到server中
        log.info("start register,JobConfig:{}", config);
        register(ctx);
    }

    public void register(ChannelHandlerContext ctx) {
        RequestProto.Request request = RequestProto.Request.newBuilder()
                .setType(RequestProto.Type.REGISTER)
                .addParams(RequestProto.Param.newBuilder().setKey("ip").setValue(config.getIp()).build())
                .addParams(RequestProto.Param.newBuilder().setKey("appName").setValue(config.getAppName()).build())
                .build();
        sendMsg(ctx, request);
    }

    public ChannelFuture sendMsg(ChannelHandlerContext ctx, Object msg) {
        RequestProto.Request request = (RequestProto.Request) msg;
        ByteBuf byteBuf = ctx.channel().alloc().heapBuffer();
        byteBuf.writeBytes(request.toByteArray());
        if (ctx.channel().isActive()) {
            try {
                return ctx.channel().writeAndFlush(byteBuf).sync();
            } catch (InterruptedException e) {
                log.error("sendMsg error", e);
            }
        }
        return null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestProto.Request request = decode(msg);
        log.info("channelRead request:{}", request);
        switch (request.getType()) {
            case REQUEST:
                List<RequestProto.Param> paramsList = request.getParamsList();
                String jobHandler = null;
                String recordId = null;
                String params = null;
                for (RequestProto.Param param : paramsList) {
                    if (param.getKey().equals("recordId")) {
                        recordId = param.getValue();
                    }
                    if (param.getKey().equals("jobHandler")) {
                        jobHandler = param.getValue();
                    }
                    if (param.getKey().equals("params")) {
                        params = param.getValue();
                    }
                    processRequest(recordId, jobHandler, params, ctx);
                }
                break;
            default:
                break;
        }
    }

    public void processRequest(String recordId, String jobHandlerName, String param, ChannelHandlerContext ctx) {
        CompletableFuture.supplyAsync(() -> {
            //取出jobHandler
            JobHandler jobHandler = jobHandlerFactory.getJobHandler(jobHandlerName);
            //执行逻辑
            return jobHandler.invoke(param);
        }).whenComplete((r, t) -> {
            log.info("start execute callback...");
            //发送执行结果
            RequestProto.Request.Builder builder = RequestProto.Request.newBuilder().setType(RequestProto.Type.RESPONSE)
                    .addParams(RequestProto.Param.newBuilder().setKey("recordId").setValue(recordId).build());
            if (r.equals("SUCCESS") && t == null) {
                builder.addParams(RequestProto.Param.newBuilder().setKey("status").setValue("1").build());
                sendMsg(ctx, builder.build());
            } else {
                builder.addParams(RequestProto.Param.newBuilder().setKey("status").setValue("2").build());
                sendMsg(ctx, builder.build());
            }
        });
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
