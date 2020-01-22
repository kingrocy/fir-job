package com.yunhui.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-01-18 10:39
 */
@SpringBootApplication
@MapperScan("com.yunhui.job.mapper")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}