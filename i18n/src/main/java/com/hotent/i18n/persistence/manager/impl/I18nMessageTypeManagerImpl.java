package com.hotent.i18n.persistence.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.i18n.persistence.dao.I18nMessageTypeDao;
import com.hotent.i18n.persistence.manager.I18nMessageTypeManager;
import com.hotent.i18n.persistence.model.I18nMessageType;

/**
 * 
 * <pre> 
 * 描述：国际化资源支持的语言类型 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-09-29 11:05:32
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("i18nMessageTypeManager")
public class I18nMessageTypeManagerImpl extends BaseManagerImpl<I18nMessageTypeDao, I18nMessageType> implements I18nMessageTypeManager {

	@Resource
	I18nMessageTypeDao i18nMessageTypeDao;
	@Override
	public I18nMessageType getByType(String type) {
		return i18nMessageTypeDao.getByType(type);
	}
	
	@Override
	@Cacheable(value = CacheKeyConst.I18N_MESSAGE_TYPE_CACHENAME, key = CacheKeyConst.I18N_MESSAGE_TYPE, pureKey = true)
	public List<I18nMessageType> getAllType() {
		return this.list();
	}
	
	@Override
	@CacheEvict(value = CacheKeyConst.I18N_MESSAGE_TYPE_CACHENAME, key = CacheKeyConst.I18N_MESSAGE_TYPE, pureKey = true)
	public void delMessageTypeCache() {}
}
