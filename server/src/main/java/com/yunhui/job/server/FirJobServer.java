package com.yunhui.job.server;

import com.yunhui.job.connect.ConnectPool;
import com.yunhui.job.dao.BasicDao;
import com.yunhui.job.handler.ServerHandler;
import com.yunhui.job.queue.JobQueue;
import com.yunhui.job.work.WorkFactory;
import com.yunhui.job.work.Worker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Date : 2020/1/21 9:36 上午
 * @Author : dushaoyun
 */
@Slf4j
@Service
public class FirJobServer {

    private ServerHandler serverHandler;

    @Autowired
    BasicDao basicDao;

    @Autowired
    JobQueue jobQueue;

    @Autowired
    ConnectPool connectPool;

    @PostConstruct
    public void start() {
        serverHandler = new ServerHandler(basicDao);
        /**
         * 启动netty
         */
        startNetty();
        /**
         * 启动工作线程池
         */
        startWork();
    }

    private void startWork() {
        WorkFactory.startWork(jobQueue, new Worker(connectPool, serverHandler));
    }

    private void startNetty() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        log.info("server start...");
                        ch.pipeline().addLast("decoder", new ProtobufVarint32FrameDecoder());
                        ch.pipeline().addLast("encoder", new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast("handler", serverHandler);
                    }
                });

        serverBootstrap.bind(8080).addListener(future -> {
            if (future.isSuccess()) {
                log.info("bind port:8080 success!");
            } else {
                log.error("bind port:8080 fail");
            }
        });
    }
}
