package com.hotent.i18n.support.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.cache.CacheManager;
import com.hotent.base.cache.ICache;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.i18n.persistence.manager.I18nMessageManager;
import com.hotent.i18n.persistence.model.I18nMessage;
import com.hotent.i18n.support.service.MessageService;

/**
 * Redis实现的资源获取服务
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
@Service
public class RedisMessageService implements MessageService{
	@Resource
	I18nMessageManager i18nMessageManager;
	@Resource
	CacheManager cacheManager;

	private Map<String, Map<String, String>> getFromI18nMessage(List<I18nMessage> list){
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		for (I18nMessage i18nMessage : list) {
			String key = i18nMessage.getKey();
			String type = i18nMessage.getType();
			String value = i18nMessage.getValue();
			Map<String, String> subMap = map.get(key);
			if(BeanUtils.isEmpty(subMap)){
				subMap = new HashMap<String, String>();
				map.put(key, subMap);
			}
			subMap.put(type, value);
		}
		return map;
	}
	
	private ICache getCache() {
		return cacheManager.getCache(CacheKeyConst.I18N_RESOURCES_CACHENAME, CacheKeyConst.I18N_RESOURCES_CACHESETTING);
	}

	@Override
	public String getMessage(String code, String type) {
		Map<String, String> batchGet = getCache().getAll(Arrays.asList(new String[]{code}), type);
		return batchGet.get(code);
	}

	@Override
	public Map<String, String> getMessages(List<String> codes, String type) {
		return getCache().getAll(codes, type);
	}

	@Override
	public void initMessage() {
		List<I18nMessage> list = i18nMessageManager.list();
		getCache().putAll(getFromI18nMessage(list));
	}

	@Override
	public void clearAllMessage() {
		getCache().clear();
	}

	@Override
	public void delByKey(String key) {
		getCache().evict(key);
	}

	@Override
	public void hdel(String key, String field) {
		getCache().hdel(key, field);
	}
}
