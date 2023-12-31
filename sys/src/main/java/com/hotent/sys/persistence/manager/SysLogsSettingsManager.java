package com.hotent.sys.persistence.manager;

import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysLogsSettings;

/**
 * 
 * <pre> 
 * 描述：日志配置 处理接口
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-08-31 16:19:34
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysLogsSettingsManager extends BaseManager<SysLogsSettings>{
	/**
	 * 获取日志配置的状态
	 * <p>返回每个服务的日志配置状态（即是否保存日志）</p>
	 * @return
	 */
	Map<String, String> getSysLogsSettingStatusMap();
	
	/**
	 * 获取日志配置的保留天数
	 * <p>返回每个服务的日志配置中保留天数</p>
	 * @return
	 */
	Map<String, Integer> getSysLogsSettingDaysMap();
}
