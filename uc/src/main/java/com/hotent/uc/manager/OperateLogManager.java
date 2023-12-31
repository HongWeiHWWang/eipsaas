package com.hotent.uc.manager;

import com.hotent.uc.model.OperateLog;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;

/**
 * 城市业务逻辑接口类
 *
 */
public interface OperateLogManager extends   BaseManager<OperateLog>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	CommonResult<String>  removeByIdStr(String ids);
	
}