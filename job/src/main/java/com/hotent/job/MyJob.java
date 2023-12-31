package com.hotent.job;


import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.job.model.BaseJob;



public class MyJob extends BaseJob {

	protected Logger logger = LoggerFactory.getLogger(MyJob.class);
	@Override
	public void executeJob(JobExecutionContext context)  {
		//获取上下文参数。
		//context.getJobDetail().getJobDataMap()
		//com.hotent.platform.job.MyJob
		logger.info("定时计划测试正常com.hotent.job.MyJob");
		//
	}
}
