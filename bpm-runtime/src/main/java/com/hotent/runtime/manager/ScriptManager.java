package com.hotent.runtime.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.runtime.model.Script;
 

public interface ScriptManager extends BaseManager<Script> {

	/**
	 * 返回所有脚本的分类
	 * @return
	 */
	public List<String> getDistinctCategory();
	
	/**
	 * 判断脚本名称是否存在
	 * @param name
	 * @return
	 */
	public Integer isNameExists(String name);
}
