package com.hotent.portal.job;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.quartz.JobExecutionContext;

import com.hotent.base.util.AppUtil;
import com.hotent.job.model.BaseJob;
import com.hotent.sys.persistence.manager.SysLogsManager;
import com.hotent.sys.persistence.manager.SysLogsSettingsManager;

/**
 * 系统日志定时删除操作
 * 添加定时任务类
 * com.hotent.portal.job.SysLogsJob 
 * @author liyanggui
 *
 */
public class SysLogsJob extends BaseJob {
	@Override
	public void executeJob(JobExecutionContext context) throws Exception {
		SysLogsManager sysLogsManager = AppUtil.getBean(SysLogsManager.class);
		SysLogsSettingsManager sysLogsSettingsManager = AppUtil.getBean(SysLogsSettingsManager.class);
		Map<String, Integer> cacheMap = sysLogsSettingsManager.getSysLogsSettingDaysMap();
		List<Map<String, Object>> params = new ArrayList<Map<String,Object>>();
		
		Set<Entry<String, Integer>> entrySet = cacheMap.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			String key = entry.getKey();
			Integer days = entry.getValue();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("executionTime", LocalDateTime.now().minusDays(days));
			map.put("moduleType", key);
			params.add(map);
		}
		sysLogsManager.removeByEexcutionTime(params);
	}
}
