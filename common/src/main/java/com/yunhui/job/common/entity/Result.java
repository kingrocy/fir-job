package com.yunhui.job.common.entity;

import lombok.*;

/**
 * @Date : 2020/1/21 1:42 下午
 * @Author : dushaoyun
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private String msg;
    private Integer code;
    private T data;


    public static <T> Result<T> success(T t){
        Result result=new Result("",200,t);
        return result;
    }

    public static <T> Result<T> fail(T t){
        Result result=new Result("",500,t);
        return result;
    }

}
