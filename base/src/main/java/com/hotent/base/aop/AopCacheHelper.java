package com.hotent.base.aop;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.util.CacheKeyConst;

@Service
public class AopCacheHelper {
	
	@Resource 
	PortalFeignService portalFeignService;
	
	@Cacheable(value = CacheKeyConst.DATA_PERMISSION_CACHENAME, key="#key")
	protected String getDataPermissionFromCache(String key) {
		return null;
	}
	
	@Cacheable(value = CacheKeyConst.LOGS_SETTING_STATUS_CACHENAME, key=CacheKeyConst.SYS_LOGS_SETTING_STATUS, pureKey = true)
	protected Map<String, String> getSysLogsSettingStatusMap(){
		return portalFeignService.getSysLogsSettingStatusMap();
	}
}
