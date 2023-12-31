package com.hotent.job.persistence.manager.impl;


import com.hotent.base.manager.impl.BaseManagerImpl;
import org.springframework.stereotype.Service;
import com.hotent.job.model.SysJobLog;
import com.hotent.job.persistence.dao.SysJobLogDao;
import com.hotent.job.persistence.manager.SysJobLogManager;



/**
 * portal_sys_joblog Service类
 *
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月7日
 */
@Service
public class SysJobLogManagerImpl extends BaseManagerImpl<SysJobLogDao, SysJobLog> implements SysJobLogManager{

	@Override
	public void createLog(SysJobLog jobLog) {
		this.create(jobLog);
	}
}
