package com.hotent.runtime.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.runtime.model.BpmSelectorDef;

public interface BpmSelectorDefDao extends BaseMapper<BpmSelectorDef> {
	/**
	 * 检查别名是否唯一
	 * @param alias
	 * @param id
	 */
	boolean isExistAlias(String alias, String id);
	
	BpmSelectorDef getByAlias(String alias); 
}
