package com.hotent.job.persistence.manager;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.Test;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.job.JobTestCase;
import com.hotent.job.model.SysJobLog;


public class SysJobLogManagerTest extends JobTestCase{
	@Resource
	SysJobLogManager sysJobLogManager;

	 @Test
	 public void testCrud(){
         String suid = UniqueIdUtil.getSuid();
         String jobName = "任务名";
         SysJobLog sysJobLog = new SysJobLog();
         sysJobLog.setId(suid);
         sysJobLog.setJobName(jobName);
         sysJobLog.setTrigName("触发器名称");
         sysJobLog.setStartTime(LocalDateTime.now());
         sysJobLog.setEndTime(LocalDateTime.now());
         sysJobLog.setState("1");
         sysJobLog.setContent("这个内容");
         sysJobLog.setRunTime(1000L);
         // 添加
         sysJobLogManager.create(sysJobLog);
         // 查询
         SysJobLog jobLog = sysJobLogManager.get(suid);
         assertEquals(jobName, jobLog.getJobName());

         String content = "新的名称";
         sysJobLog.setContent(content);
         // 更新
         sysJobLogManager.update(jobLog);

         SysJobLog log = sysJobLogManager.get(suid);
         assertEquals(content, log.getContent());

         String suid2 = UniqueIdUtil.getSuid();
         String suid3 = UniqueIdUtil.getSuid();
         SysJobLog sysJobLog1 = new SysJobLog();
         sysJobLog1.setId(suid2);
         sysJobLog1.setJobName("test1");
         sysJobLog1.setTrigName("test1");
         sysJobLog1.setStartTime(LocalDateTime.now());
         sysJobLog1.setEndTime(LocalDateTime.now());
         sysJobLog1.setState("1");
         sysJobLog1.setContent("test1");
         sysJobLog1.setRunTime(1000L);

         SysJobLog sysJobLog2 = new SysJobLog();
         sysJobLog2.setId(suid3);
         sysJobLog2.setJobName("test2");
         sysJobLog2.setTrigName("test2");
         sysJobLog2.setStartTime(LocalDateTime.now());
         sysJobLog2.setEndTime(LocalDateTime.now());
         sysJobLog2.setState("0");
         sysJobLog2.setContent("test2");
         sysJobLog2.setRunTime(2000L);


         sysJobLogManager.create(sysJobLog1);
         sysJobLogManager.create(sysJobLog2);

         // 通过ID列表批量查询
         List<SysJobLog> byIds = sysJobLogManager.listByIds(Arrays.asList(new String[]{suid2,suid3}));
         assertEquals(2, byIds.size());

         QueryFilter<SysJobLog> queryFilter = QueryFilter.<SysJobLog>build()
                 .withDefaultPage()
                 .withQuery(new QueryField("jibName", "test2"));
         // 通过通用查询条件查询
         PageList<SysJobLog> query = sysJobLogManager.query(queryFilter);
         assertEquals(1, query.getTotal());

         // 通过指定列查询唯一记录
         SysJobLog bab = sysJobLogManager.getOne(Wrappers.<SysJobLog>lambdaQuery().eq(SysJobLog::getJobName, jobName));
         assertEquals(jobName, bab.getJobName());

         // 查询所有数据
         List<SysJobLog> all = sysJobLogManager.list();
         assertEquals(3, all.size());

         // 查询所有数据(分页)
         PageList<SysJobLog> allByPage = sysJobLogManager.page(new PageBean(1, 2));
         assertEquals(3, allByPage.getTotal());
         assertEquals(2, allByPage.getRows().size());



         // 通过ID删除数据
         sysJobLogManager.remove(suid);
         SysJobLog sstd = sysJobLogManager.getOne(Wrappers.<SysJobLog>lambdaQuery().eq(SysJobLog::getJobName, jobName));
         assertTrue(sstd==null);

         // 通过ID集合批量删除数据
         sysJobLogManager.removeByIds(Arrays.asList(new String[]{ suid2, suid3}));
         List<SysJobLog> nowAll = sysJobLogManager.list();
         assertEquals(0, nowAll.size());
	 }
}
