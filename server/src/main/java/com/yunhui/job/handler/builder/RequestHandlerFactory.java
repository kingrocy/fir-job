package com.yunhui.job.handler.builder;

import com.yunhui.job.connect.ConnectPool;
import com.yunhui.job.dao.BasicDao;
import com.yunhui.job.handler.RegisterRequestHandler;
import com.yunhui.job.handler.RequestHandler;
import com.yunhui.job.handler.ResponseRequestHandler;
import com.yunhui.utils.ApplicationContextUtils;

/**
 * @Date : 2021/1/18 10:44 上午
 * @Author : dushaoyun
 */
public interface RequestHandlerFactory {

    RequestHandler createRequestHandler();

}

class RegisterRequestHandlerFactory implements RequestHandlerFactory {

    @Override
    public RequestHandler createRequestHandler() {
        return new RegisterRequestHandler(ApplicationContextUtils.getBean(ConnectPool.class));
    }
}


class ResponseRequestHandlerFactory implements RequestHandlerFactory {

    @Override
    public RequestHandler createRequestHandler() {
        return new ResponseRequestHandler(ApplicationContextUtils.getBean(BasicDao.class));
    }
}

class RequestRequestHandlerFactory implements RequestHandlerFactory {

    @Override
    public RequestHandler createRequestHandler() {
        return null;
    }
}

class PingRequestHandlerFactory implements RequestHandlerFactory {

    @Override
    public RequestHandler createRequestHandler() {
        return null;
    }
}

class PongRequestHandlerFactory implements RequestHandlerFactory {

    @Override
    public RequestHandler createRequestHandler() {
        return null;
    }
}
