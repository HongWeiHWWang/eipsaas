package com.hotent.sys.util;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.sys.persistence.manager.SysLogsManager;
import com.hotent.sys.persistence.model.SysLogs;

/**
 * 系统日志添加工具类
 * @author liyanggui
 *
 */
public class SysLogsUtil {
	public static void addSysLogs(String id,String opeName,String executor,String ip,String logType,String moduleType,String reqUrl,String opeContent){
		SysLogsManager sysLogsManager = AppUtil.getBean(SysLogsManager.class);
		if(StringUtil.isEmpty(executor) || executor.startsWith("null") ){
			executor = "系统[无用户登录系统]";
		}
		// 对于操作内容的记录设置最多记录3999
		if(StringUtil.isNotEmpty(opeContent) && opeContent.length() > 3999) {
			opeContent = opeContent.substring(0, 3999);
		}
		SysLogs sysLogs = new SysLogs(opeName, DateUtil.getCurrentDate(), executor, ip, logType, moduleType,reqUrl, opeContent);
		sysLogs.setId(id);
		if(StringUtil.isNotEmpty(id)) {
			SysLogs isExist = sysLogsManager.get(id);
			if(BeanUtils.isEmpty(isExist)) {
				sysLogsManager.create(sysLogs);
			}else {
				sysLogsManager.update(sysLogs);
			}
		}else {			
			sysLogsManager.create(sysLogs);
		}
		
	}
}
