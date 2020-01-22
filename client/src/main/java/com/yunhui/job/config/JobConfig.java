package com.yunhui.job.config;

import lombok.*;

/**
 * @Date : 2020/1/21 10:19 上午
 * @Author : dushaoyun
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JobConfig {

    private String appName;
    private String ip;
    private String serverIp;
    private Integer serverPort;
    
}
