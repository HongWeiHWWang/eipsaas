package com.hotent.sys.persistence.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.persistence.dao.SysPropertiesDao;
import com.hotent.sys.persistence.manager.SysPropertiesManager;
import com.hotent.sys.persistence.model.SysProperties;

/**
 * 
 * <pre> 
 * 描述：portal_sys_properties 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-07-28 09:19:53
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("sysPropertiesManager")
public class SysPropertiesManagerImpl extends BaseManagerImpl<SysPropertiesDao, SysProperties> implements SysPropertiesManager{

	@Override
	public List<String> getGroups() {
		return baseMapper.getGroups();
	}
	@Override
	public boolean isExist(SysProperties sysProperties) {
		return baseMapper.isExist(sysProperties);
	}

	@Override
	public String getByAlias(String alias) {
		return getByAlias(alias, null);
	}

	@Override
	public Integer getIntByAlias(String alias) {
		String val= getByAlias(alias);
		if(StringUtil.isEmpty(val)) return 0;
		Integer rtn=Integer.parseInt(val);
		return rtn;
	}
	@Override
	public Integer getIntByAlias(String alias, Integer defaulValue) {
		String val= getByAlias(alias);
		if(StringUtil.isEmpty(val)) return defaulValue;
		Integer rtn=Integer.parseInt(val);
		return rtn;
	}
	@Override
	public Long getLongByAlias(String alias) {
		String val= getByAlias(alias);
		if(StringUtil.isEmpty(val)) return 0L;
		Long rtn=Long.parseLong(val);
		return rtn;
	}
	@Override
	public boolean getBooleanByAlias(String alias) {
		String val= getByAlias(alias);
		return Boolean.parseBoolean(val);
	}
	@Override
	public boolean getBooleanByAlias(String alias, boolean defaulValue) {
		String val= getByAlias(alias);
		if(StringUtil.isEmpty(val)) return defaulValue;
		if("1".equals(val)) return true;
		return Boolean.parseBoolean(val);
	}
	
	@Override
	public String getByAlias(String alias, String defaultValue) {
		SysProperties sysProperties = getByAliasFromDb(alias);
		if(BeanUtils.isEmpty(sysProperties)) {
			return defaultValue;
		}
		String val = sysProperties.getValue();
		if(StringUtil.isEmpty(val)) {
			return defaultValue;
		}
		return val;
	}
	
	@Override
	public SysProperties getByAliasFromDb(String alias) {
		QueryWrapper<SysProperties> queryWrapper = new QueryWrapper<SysProperties>();
		queryWrapper.and(wq->wq.eq("alias", alias));
		return this.baseMapper.selectOne(queryWrapper);
	}
}
