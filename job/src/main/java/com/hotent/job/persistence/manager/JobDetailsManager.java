package com.hotent.job.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.job.api.IJobLogService;
import com.hotent.job.model.JobDetails;
import com.hotent.job.model.SchedulerVo;
import com.hotent.job.model.SysJobLog;

import java.io.IOException;


/**
 * portal_sys_joblog Service类
 *
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月7日
 */
public interface JobDetailsManager extends BaseManager<JobDetails> {
    public void addJob(SchedulerVo schedulerVo) throws IOException, ClassNotFoundException;

    boolean isJobExists(String jobName);
}
