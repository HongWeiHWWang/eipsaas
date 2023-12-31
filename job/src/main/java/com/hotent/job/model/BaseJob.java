package com.hotent.job.model;



import java.time.LocalDateTime;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.ResourceTransactionManager;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.job.api.IJobLogService;

/**
 * 定时计划
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月14日
 */
@DisallowConcurrentExecution
public abstract class BaseJob implements Job {

	//抽象方法。
	public abstract void executeJob(JobExecutionContext context) throws Exception;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//日志记录
		String jobName=context.getJobDetail().getKey().getName();
		Boolean trans = false;
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		//是否加上事务控制
		Object transObj = jobDataMap.get("transaction");
		if(BeanUtils.isNotEmpty(transObj)){
			trans = Boolean.parseBoolean(transObj.toString());
		}
		String trigName="directExec";
		Trigger trig=context.getTrigger();
		if(trig!=null)
			trigName=trig.getKey().getName();
		LocalDateTime strStartTime=LocalDateTime.now();
		long startTime=System.currentTimeMillis();
		ResourceTransactionManager transactionManager = null;
		TransactionStatus status = null;
		if(trans){
			transactionManager = (ResourceTransactionManager)AppUtil.getBean("transactionManager");
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();  
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);  
			status = transactionManager.getTransaction(def);  
		}
		try{
			executeJob(context);
			long endTime=System.currentTimeMillis();
			LocalDateTime strEndTime=LocalDateTime.now();
			//记录日志
			long runTime=(endTime-startTime) /1000;
			addLog(jobName, trigName, strStartTime, strEndTime, runTime, "任务执行成功!", "1");
			if(trans){
				transactionManager.commit(status);
			}
		}
		catch(Exception ex)
		{
			if(trans){
				transactionManager.rollback(status);
			}
			long endTime=System.currentTimeMillis();
			LocalDateTime strEndTime=LocalDateTime.now();
			long runTime=(endTime-startTime) /1000;
			try {
				String rootCause = ExceptionUtils.getRootCauseMessage(ex);
				addLog(jobName, trigName, strStartTime, strEndTime, runTime, rootCause, "0");
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	private void addLog(String jobName, String trigName, LocalDateTime startTime,
			LocalDateTime endTime, long runTime,String content,String state) throws Exception {
		IJobLogService logService=AppUtil.getBean(IJobLogService.class);

		SysJobLog jobLog=new SysJobLog(jobName, trigName, startTime, endTime, content, state,runTime);

		String id=UniqueIdUtil.getSuid();
		jobLog.setId(id);
		logService.createLog(jobLog);
	}

}
