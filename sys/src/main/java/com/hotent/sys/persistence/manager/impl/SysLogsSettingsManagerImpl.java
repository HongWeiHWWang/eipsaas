package com.hotent.sys.persistence.manager.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.sys.persistence.dao.SysLogsSettingsDao;
import com.hotent.sys.persistence.manager.SysLogsSettingsManager;
import com.hotent.sys.persistence.model.SysLogsSettings;

/**
 * 
 * <pre> 
 * 描述：日志配置 处理实现类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-08-31 16:19:34
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("sysLogsSettingsManager")
public class SysLogsSettingsManagerImpl extends BaseManagerImpl<SysLogsSettingsDao, SysLogsSettings> implements SysLogsSettingsManager{
	@Override
	public void remove(Serializable id) {
		super.remove(id);
		SysLogsSettingsManagerImpl bean = AppUtil.getBean(getClass());
		bean.cleanStatus();
		bean.cleanDays();
	}

	@Override
	public void removeByIds(String... ids) {
		super.removeByIds(ids);
		SysLogsSettingsManagerImpl bean = AppUtil.getBean(getClass());
		bean.cleanStatus();
		bean.cleanDays();
	}

	@Override
	public void create(SysLogsSettings t) {
		super.create(t);
		SysLogsSettingsManagerImpl bean = AppUtil.getBean(getClass());
		bean.cleanStatus();
		bean.cleanDays();
	}

	@Override
	public void update(SysLogsSettings t) {
		super.update(t);
		SysLogsSettingsManagerImpl bean = AppUtil.getBean(getClass());
		bean.cleanStatus();
		bean.cleanDays();
	}
	
	/**
	 * 清除缓存
	 */
	@CacheEvict(value = CacheKeyConst.LOGS_SETTING_STATUS_CACHENAME, key=CacheKeyConst.SYS_LOGS_SETTING_STATUS, pureKey = true)
	protected void cleanStatus() {}
	
	@CacheEvict(value = CacheKeyConst.SYS_LOGS_SETTING_DAY_CACHENAME, key=CacheKeyConst.SYS_LOGS_SETTING_SAVE_DAY, pureKey = true)
	protected void cleanDays() {}

	@Override
	public Map<String, String> getSysLogsSettingStatusMap() {
		List<SysLogsSettings> all = this.list();
		HashMap<String, String> statusMap = new HashMap<String, String>();
		for (SysLogsSettings sysLogsSettings : all) {
			statusMap.put(sysLogsSettings.getModuleType(), sysLogsSettings.getStatus());
		}
		return statusMap;
	}

	@Override
	@Cacheable(value = CacheKeyConst.SYS_LOGS_SETTING_DAY_CACHENAME, key=CacheKeyConst.SYS_LOGS_SETTING_SAVE_DAY, pureKey = true)
	public Map<String, Integer> getSysLogsSettingDaysMap() {
		List<SysLogsSettings> all = this.list();
		HashMap<String, Integer> saveDayMap = new HashMap<String, Integer>();
		for (SysLogsSettings sysLogsSettings : all) {
			saveDayMap.put(sysLogsSettings.getModuleType(), sysLogsSettings.getSaveDays());
		}
		return saveDayMap;
	}
}
