package com.yunhui.job.handler;

import com.yunhui.job.common.proto.RequestProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Date : 2021/1/18 10:43 上午
 * @Author : dushaoyun
 */
public interface RequestHandler {

    void handlerRequest(RequestProto.Request request, ChannelHandlerContext ctx);


    default ChannelFuture sendMsg(ChannelHandlerContext ctx, Object msg) {
        RequestProto.Request request = (RequestProto.Request) msg;
        ByteBuf byteBuf = ctx.channel().alloc().heapBuffer();
        byteBuf.writeBytes(request.toByteArray());
        if (ctx.channel().isActive()) {
            return ctx.channel().writeAndFlush(byteBuf);
        }
        return null;
    }
}
