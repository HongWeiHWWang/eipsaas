package com.hotent.runtime.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.runtime.dao.BpmSelectorDefDao;
import com.hotent.runtime.manager.BpmSelectorDefManager;
import com.hotent.runtime.model.BpmSelectorDef;

@Service("bpmSelectorDefManager")
public class BpmSelectorDefManagerImpl extends BaseManagerImpl<BpmSelectorDefDao, BpmSelectorDef> implements BpmSelectorDefManager {

	@Override
	public boolean isExistAlias(String alias, String id) {
		return baseMapper.isExistAlias(alias,id);
	}
	@Override
	public BpmSelectorDef getByAlias(String alias) {
		return baseMapper.getByAlias(alias);
	}
}
