package com.hotent.job.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.job.api.IJobLogService;
import com.hotent.job.model.SysJobLog;


/**
 * portal_sys_joblog Service类
 *
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月7日
 */
public interface SysJobLogManager extends BaseManager<SysJobLog>,IJobLogService
{
}
