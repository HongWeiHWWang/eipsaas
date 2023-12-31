package com.hotent.base.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.feign.PortalFeignService;

/**
 * 获取系统属性的服务类
 * <p>添加了缓存</p>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
@Service
public class PropertyService {
	@Resource
	PortalFeignService portalFeignService;

	@Cacheable(value = "syspropertys", key = "#alias")
	public String getProperty(String alias, String defaultValue) {
		return portalFeignService.getByAlias(alias, defaultValue);
	}
}
