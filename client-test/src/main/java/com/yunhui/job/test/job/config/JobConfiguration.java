package com.yunhui.job.test.job.config;

import com.yunhui.job.client.FirClient;
import com.yunhui.job.properties.FirJobProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Date : 2020/1/22 9:24 上午
 * @Author : dushaoyun
 */
@Configuration
public class JobConfiguration {

    @Bean(initMethod = "start")
    public FirClient firClient() {
        return new FirClient(null);
    }
}
