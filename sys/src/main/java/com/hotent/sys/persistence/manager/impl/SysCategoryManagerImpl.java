package com.hotent.sys.persistence.manager.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.sys.persistence.dao.SysCategoryDao;
import com.hotent.sys.persistence.dao.SysTypeDao;
import com.hotent.sys.persistence.manager.SysCategoryManager;
import com.hotent.sys.persistence.model.SysCategory;
import com.hotent.sys.persistence.model.SysType;

@Service("sysCategoryManager")
public class SysCategoryManagerImpl extends BaseManagerImpl<SysCategoryDao, SysCategory> implements SysCategoryManager{
	@Resource
	SysTypeDao sysTypeDao;
	
	@Override
	public Boolean isKeyExist(String id, String groupKey) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put("groupKey", groupKey);
		int i = baseMapper.isKeyExist(params);
		return (i > 0);
	}

	@Override
	public SysCategory getByTypeKey(String typeKey) {
		return baseMapper.getByKey(typeKey);
	}

	@Override
	public void remove(Serializable entityId) {
		List<SysType> sysTypes = sysTypeDao.getByParentId(entityId.toString());
		if(BeanUtils.isNotEmpty(sysTypes)) {
			String val=sysTypes.get(0).getTypeGroupKey();
			SysCategory sysCategory=baseMapper.getByKey(val);
			if(BeanUtils.isNotEmpty(sysCategory)){
				val=sysCategory.getName();
			}
			throw new BaseException(String.format("分类标识[%s]下已经添加分类信息暂无法删除。", val));
		}
		
		super.remove(entityId);
	}
	
}
