package com.hotent.job.api;

import com.hotent.job.model.SysJobLog;

/**
 * 任务日志接口类。
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月14日
 */
public interface IJobLogService {
	
	/**
	 * 创建日志。
	 * @param jobLog
	 */
	void createLog(SysJobLog jobLog);

}
