package com.yunhui.job.mapper;

import com.yunhui.job.bean.JobExecuteRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @Date : 2020/1/21 4:58 下午
 * @Author : dushaoyun
 */
public interface JobExecuteRecordMapper {

    void insert(JobExecuteRecord jobExecuteRecord);

    int update(@Param("recordId") Long recordId, @Param("status") Integer status);
}
