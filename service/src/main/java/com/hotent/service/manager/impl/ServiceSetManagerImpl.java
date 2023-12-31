package com.hotent.service.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.exception.NotFoundException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.service.dao.ServiceParamDao;
import com.hotent.service.dao.ServiceSetDao;
import com.hotent.service.manager.ServiceSetManager;
import com.hotent.service.model.ServiceParam;
import com.hotent.service.model.ServiceSet;

@Service
public class ServiceSetManagerImpl extends BaseManagerImpl<ServiceSetDao, ServiceSet> implements ServiceSetManager{
	@Resource
	ServiceParamDao serviceParamDao;
	
	@Override
	public ServiceSet getByAlias(String alias) {
		ServiceSet serviceSet = baseMapper.getByAlias(alias);
		if(BeanUtils.isEmpty(serviceSet)) {
			throw new NotFoundException(String.format("未找到别名为: %s 的记录", alias));
		}
		String setId = serviceSet.getId();
		List<ServiceParam> paramList = serviceParamDao.getBySetId(setId);
		serviceSet.setServiceParamList(paramList);
		return serviceSet;
	}
	
	@Override
	@Transactional
	public void saveData(ServiceSet serviceSet) {
		String setId = serviceSet.getId();
		if(StringUtil.isEmpty(setId)) {
			setId = UniqueIdUtil.getSuid();
			serviceSet.setId(setId);
			this.create(serviceSet);
		}
		else {
			this.update(serviceSet);
		}
		serviceParamDao.removeBySetId(setId);
		List<ServiceParam> sysServiceParamList = serviceSet.getServiceParamList();
		for(ServiceParam param : sysServiceParamList) {
			param.setSetId(setId);
			param.setId(UniqueIdUtil.getSuid());
			serviceParamDao.insert(param);
		}
	}

	@Override
	@Transactional
	public void removeByIds(String... ids) {
		for(String id : ids) {
			serviceParamDao.removeBySetId(id);
			this.remove(id);
		}
	}
}
