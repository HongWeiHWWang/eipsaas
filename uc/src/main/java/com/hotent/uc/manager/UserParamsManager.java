package com.hotent.uc.manager;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.uc.model.UserParams;
import com.hotent.base.manager.BaseManager;

/**
 * 
 * <pre> 
 * 描述：用户参数 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2016-11-01 17:11:33
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserParamsManager extends BaseManager<UserParams>{
	
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	

	List<UserParams> getByUserId(String id);

	void saveParams(String userId, List<ObjectNode> lists) throws SQLException;

	UserParams getByUserIdAndAlias(String userId, String key);
}
