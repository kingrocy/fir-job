package com.yunhui.job.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yunhui.job.common.proto.RequestProto;
import com.yunhui.job.connect.ConnectPool;
import com.yunhui.job.dao.BasicDao;
import com.yunhui.job.handler.builder.RequestHandlerFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Date : 2020/1/21 9:39 上午
 * @Author : dushaoyun
 */
@Slf4j
@AllArgsConstructor
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ConnectPool connectPool;

    @Getter
    private BasicDao basicDao;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestProto.Request request = decode(msg);
        log.info("channelRead request:{}", request);
        RequestHandlerFactoryBuilder.getRequestHandler(request.getType()).createRequestHandler().handlerRequest(request, ctx);
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
