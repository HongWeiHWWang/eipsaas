package com.hotent.sys.persistence.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysDataSourceDef;

public interface SysDataSourceDefManager extends BaseManager<SysDataSourceDef>{

	/**
	 * 获取这个className的拥有setter的字段
	 * @param className
	 * @return 
	 * JSONArray
	 * @exception 
	 * @since  1.0.0
	 */
	List<Map<String,String>> getHasSetterFieldsJsonArray(String classPath);
	
}
