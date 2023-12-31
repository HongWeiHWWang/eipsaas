package com.hotent.job.persistence.manager;
import com.hotent.job.JobTestCase;
import com.hotent.job.model.SchedulerVo;
import org.junit.Test;
import javax.annotation.Resource;


import static org.junit.Assert.assertTrue;

public class JobDetailsManagerTest extends JobTestCase {

    @Resource
    JobDetailsManager jobDetailsManager;


    @Test
	public void testManage() throws Exception {
        SchedulerVo jobDetails=new SchedulerVo();
        jobDetails.setJobName("单元测试定时任务");
        jobDetails.setDescription("单元测试定时任务");
        jobDetails.setClassName("com.hotent.job.MyJob");
        jobDetails.setParameterJson("[{\"name\":\"type\",\"type\":\"string\",\"value\":\"ttt\"}]");
        jobDetailsManager.addJob(jobDetails);
        boolean res=jobDetailsManager.isJobExists("单元测试定时任务");
        assertTrue(res) ;
    }
}
