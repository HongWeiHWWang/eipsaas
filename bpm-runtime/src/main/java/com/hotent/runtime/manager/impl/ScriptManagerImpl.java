package com.hotent.runtime.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.runtime.dao.ScriptDao;
import com.hotent.runtime.manager.ScriptManager;
import com.hotent.runtime.model.Script;
 

@Service("scriptManager")
public class ScriptManagerImpl extends BaseManagerImpl<ScriptDao, Script> implements ScriptManager{
	@Override
	public List<String> getDistinctCategory() {
		return baseMapper.getDistinctCategory();
	}

	@Override
	public Integer isNameExists(String name) {
		return baseMapper.isNameExists(name);
	}

}
