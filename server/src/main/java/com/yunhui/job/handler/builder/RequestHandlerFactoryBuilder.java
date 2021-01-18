package com.yunhui.job.handler.builder;

import com.google.common.collect.Maps;
import com.yunhui.job.common.proto.RequestProto;

import java.util.Map;

/**
 * @Date : 2021/1/18 11:00 上午
 * @Author : dushaoyun
 */
public class RequestHandlerFactoryBuilder {

    private static final Map<RequestProto.Type, RequestHandlerFactory> HOLDER = Maps.newConcurrentMap();

    static {
        HOLDER.put(RequestProto.Type.PING, new PingRequestHandlerFactory());
        HOLDER.put(RequestProto.Type.PONG, new PongRequestHandlerFactory());
        HOLDER.put(RequestProto.Type.REQUEST, new RequestRequestHandlerFactory());
        HOLDER.put(RequestProto.Type.REGISTER, new RegisterRequestHandlerFactory());
        HOLDER.put(RequestProto.Type.RESPONSE, new ResponseRequestHandlerFactory());
    }


    public static RequestHandlerFactory getRequestHandler(RequestProto.Type type) {
        RequestHandlerFactory handlerFactory = HOLDER.get(type);
        return handlerFactory;
    }
}
