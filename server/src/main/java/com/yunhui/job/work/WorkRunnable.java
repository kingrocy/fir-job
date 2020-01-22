package com.yunhui.job.work;

import com.yunhui.job.bean.JobExecuteRecord;
import com.yunhui.job.common.entity.DelayStrategy;
import com.yunhui.job.common.entity.Job;
import com.yunhui.job.common.proto.RequestProto;
import com.yunhui.job.connect.ConnectPool;
import com.yunhui.job.dao.BasicDao;
import com.yunhui.job.handler.ServerHandler;
import com.yunhui.job.queue.JobQueue;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Date : 2020/1/10 2:46 下午
 * @Author : dushaoyun
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class WorkRunnable implements Runnable {

    private Job job;

    private JobQueue jobQueue;

    private ConnectPool pool;

    private ServerHandler serverHandler;

    private JobExecuteRecord addExecuteRecord(Job job){
        JobExecuteRecord jobExecuteRecord=new JobExecuteRecord();
        jobExecuteRecord.setJobId(job.getJobId());
        jobExecuteRecord.setStatus(0);
        BasicDao basicDao = serverHandler.getBasicDao();
        basicDao.addJobExecuteRecord(jobExecuteRecord);
        return jobExecuteRecord;
    }

    private void notifyClientExecute(Job job) {
        //添加job执行记录
        JobExecuteRecord jobExecuteRecord = addExecuteRecord(job);
        //随机挑选机器
        String appName = job.getAppName();
        String ip = pool.randomIp(appName);
        ChannelHandlerContext ctx = pool.getConnect(ip);
        if (ctx == null) {
            //todo 可以重试 换一台机器
            log.error("appName:{},ip:{} ctx is null",appName,ip);
        }
        //封装消息
        RequestProto.Request request = RequestProto.Request.newBuilder().setType(RequestProto.Type.REQUEST)
                .addParams(RequestProto.Param.newBuilder().setKey("recordId").setValue(jobExecuteRecord.getId().toString()).build())
                .addParams(RequestProto.Param.newBuilder().setKey("jobHandler").setValue(job.getJobHandler()).build())
                .build();
        //发送
        serverHandler.sendMsg(ctx,request);
    }

    @Override
    public void run() {
        log.info("start run job:{}", job);
        DelayStrategy strategy = job.getDelayStrategy();
        /**
         * 0手动 1自动
         */
        int type = strategy.type();
        if (type == 0) {
            //手动执行一次就好了
            notifyClientExecute(job);
        } else {
            //自动需要更新下次执行时间
            notifyClientExecute(job);
            strategy.updateNext();
            //重新把job丢到队列里面去
            jobQueue.add(job);
            log.info("本次自动执行任务完成，job重新进入队列等待下次执行,Job:{}", job);
        }
    }
}
