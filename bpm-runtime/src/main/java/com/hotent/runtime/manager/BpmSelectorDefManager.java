package com.hotent.runtime.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.runtime.model.BpmSelectorDef;

public interface BpmSelectorDefManager extends BaseManager<BpmSelectorDef>{
	/**
	 * 检查别名是否唯一
	 * @param alias
	 * @param id
	 */
	boolean isExistAlias(String alias, String id);

	public BpmSelectorDef getByAlias(String alias);
}
