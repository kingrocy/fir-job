package com.yunhui.job.connect;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date : 2020/1/21 1:58 下午
 * @Author : dushaoyun
 */
@Component
public class ConnectPool {

    private Map<String, ChannelHandlerContext> ipMapingCtx = new ConcurrentHashMap<>();

    private Map<String, Set<String>> appNameIpMap = new ConcurrentHashMap<>();


    public void registerConnect(String ip, String appName, ChannelHandlerContext ctx) {
        ipMapingCtx.put(ip, ctx);
        if (appNameIpMap.containsKey(appName)) {
            appNameIpMap.get(appName).add(ip);
        } else {
            appNameIpMap.put(appName, ImmutableSet.of(ip));
        }
    }

    public ChannelHandlerContext getConnect(String ip) {
        return ipMapingCtx.get(ip);
    }

    public String randomIp(String appName) {
        Set<String> ips = appNameIpMap.get(appName);
        if (CollectionUtils.isEmpty(ips)) {
            return null;
        }
        int index = RandomUtils.nextInt(0, ips.size());
        String ip = Lists.newArrayList(ips).get(index);
        return ip;
    }


}
