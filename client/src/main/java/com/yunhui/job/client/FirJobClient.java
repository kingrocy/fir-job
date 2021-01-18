package com.yunhui.job.client;

import com.yunhui.job.config.JobConfig;
import com.yunhui.job.factory.JobHandlerFactory;
import com.yunhui.job.handler.ClientHandler;
import com.yunhui.job.properties.FirJobProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * @Date : 2020/1/21 10:14 上午
 * @Author : dushaoyun
 */
@Slf4j
public class FirJobClient implements ApplicationContextAware {

    private JobConfig jobConfig;

    private ApplicationContext applicationContext;

    public void start() {
        startClient();
    }

    public FirJobClient(FirJobProperties firJobProperties) {
        init(firJobProperties);
    }

    public void init(FirJobProperties firJobProperties) {
        if (firJobProperties == null) {
            //扫描配置文件properties
            InputStream inputStream = FirJobClient.class.getClassLoader().getResourceAsStream("fir-job.properties");
            Properties properties = new Properties();
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                log.info("load properties exception", e);
            }
            firJobProperties = new FirJobProperties();
            firJobProperties.setAppName(properties.getProperty("fir.job.appName"));
            firJobProperties.setIp(properties.getProperty("fir.job.ip"));
            firJobProperties.setServerIp(properties.getProperty("fir.job.serverIp"));
            firJobProperties.setServerPort(Integer.parseInt(properties.getProperty("fir.job.serverPort", "0")));
        }
        jobConfig = initConfig(firJobProperties);
    }

    private JobConfig initConfig(FirJobProperties firJobProperties) {
        //appName 必填
        String appName = firJobProperties.getAppName();
        String ip = firJobProperties.getIp();
        String serverIp = firJobProperties.getServerIp();
        Integer serverPort = firJobProperties.getServerPort();
        if (StringUtils.isEmpty(appName)) {
            throw new IllegalArgumentException("client.appName missing....");
        }
        if (StringUtils.isEmpty(serverIp)) {
            throw new IllegalArgumentException("server.ip missing....");
        }
        if (serverPort == null || serverPort == 0) {
            throw new IllegalArgumentException("server.port missing....");
        }
        if (StringUtils.isEmpty(ip)) {
            //解析本机ip
            try {
                String address = InetAddress.getLocalHost().getHostAddress();
                log.info("local address:{}", address);
                ip = address;
            } catch (UnknownHostException e) {
                log.error("getAddress error", e);
            }
        }
        return new JobConfig(appName, ip, serverIp, serverPort);
    }

    private void startClient() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast("decoder", new ProtobufVarint32FrameDecoder());
                        ch.pipeline().addLast("encoder", new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast("handle", new ClientHandler(jobConfig, new JobHandlerFactory(applicationContext)));
                    }
                });
        // 4.建立连接
        bootstrap.connect(jobConfig.getServerIp(), jobConfig.getServerPort()).addListener(future -> {
            if (future.isSuccess()) {
                log.info("ip:{},port:{} connect success...", jobConfig.getServerIp(), jobConfig.getServerPort());
            } else {
                log.error("ip:{},port:{} connect fail...", jobConfig.getServerIp(), jobConfig.getServerPort());
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
