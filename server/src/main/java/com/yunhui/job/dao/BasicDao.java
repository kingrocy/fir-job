package com.yunhui.job.dao;

import com.yunhui.job.bean.JobExecuteRecord;
import com.yunhui.job.mapper.JobExecuteRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Date : 2020/1/21 4:56 下午
 * @Author : dushaoyun
 */
@Component
public class BasicDao {


    @Autowired
    JobExecuteRecordMapper jobExecuteRecordMapper;

    public void addJobExecuteRecord(JobExecuteRecord record){
        jobExecuteRecordMapper.insert(record);
    }

    public int  updateJobExecuteRecord(Long recordId,Integer status){
        return jobExecuteRecordMapper.update(recordId, status);
    }
}
