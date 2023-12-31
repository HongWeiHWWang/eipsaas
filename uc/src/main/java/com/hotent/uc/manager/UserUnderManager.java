package com.hotent.uc.manager;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.UserUnder;

/**
 * 
 * <pre> 
 * 描述：下属管理 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-25 09:24:29
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserUnderManager extends BaseManager<UserUnder>{
	
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	/**
	 * 获取下级用户
	 * @param params
	 * @return
	 */
	IPage<UserUnder> getUserUnder(QueryFilter queryFilter);
	
	/**
	 * 根据上级id与下级id删除上下级关系
	 * @param orgId
	 * @param underUserId
	 */
	void delByUpIdAndUderId(String orgId,String underUserId);
	
	/**
	 * 删除用户在某组织下的下属
	 * @param userId
	 * @param orgId
	 */
	void delByUserIdAndOrgId(String userId,String orgId);
	
	/**
	 * 根据组织id删除上下属关系
	 * @param orgId
	 */
	void delByOrgId(String orgId);
	
	/**
	 * 获取下属用户
	 * @param params
	 * @return
	 */
	List<UserUnder> getUserUnder(Map<String,Object> params);
	
}
