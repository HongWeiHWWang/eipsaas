package com.hotent.uc.manager;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.uc.model.OrgParams;
import com.hotent.base.manager.BaseManager;

/**
 * 
 * <pre> 
 * 描述：组织参数 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2016-11-04 11:39:44
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgParamsManager extends   BaseManager<OrgParams>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	

	List<OrgParams> getByOrgId(String id);

	void saveParams(String orgId, List<ObjectNode> lists);

	OrgParams getByOrgIdAndAlias(String groupId, String key);
	
	/**
	 * 根据组织id删除组织参数
	 * @param orgId
	 */
	void removeByOrgId(String orgId);
}
