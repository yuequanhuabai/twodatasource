package com.he.multi.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyJob implements Job {


    private static Logger logger=LoggerFactory.getLogger(MyJob.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("正在运行作业!");
    }
}
