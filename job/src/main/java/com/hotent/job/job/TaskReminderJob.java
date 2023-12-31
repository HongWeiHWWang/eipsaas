package com.hotent.job.job;

import org.quartz.JobExecutionContext;

import com.hotent.base.constants.SystemConstants;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.BpmModelFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.job.model.BaseJob;
import com.hotent.uc.api.impl.util.ContextUtil;


/**
 * 任务催办定时计划
 * @author liyg
 */
public class TaskReminderJob extends BaseJob{
	@Override
	public void executeJob(JobExecutionContext context) throws Exception {
		BpmModelFeignService bean = AppUtil.getBean(BpmModelFeignService.class);
		String defaultAccount = SystemConstants.SYSTEM_ACCOUNT;
		// 定时任务中没有当前登录用户，所以需要设置到当前用户上下文中
		ContextUtil.setCurrentUserByAccount(defaultAccount);
		CommonResult<String> executeTaskReminderJob = bean.executeTaskReminderJob();
		if(!executeTaskReminderJob.getState()){
			throw new BaseException(StringUtil.isNotEmpty(executeTaskReminderJob.getMessage())?executeTaskReminderJob.getMessage():"执行催办任务失败");
		}
	}
	
}
