package com.yunhui.job.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Date : 2020/1/21 5:33 下午
 * @Author : dushaoyun
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface JobHandlerName {

    String value();
}
