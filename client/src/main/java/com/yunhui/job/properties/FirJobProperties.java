package com.yunhui.job.properties;

import lombok.*;

/**
 * @Date : 2020/5/12 2:47 下午
 * @Author : dushaoyun
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FirJobProperties {

    private String appName;
    private String ip;
    private String serverIp;
    private Integer serverPort;

}
