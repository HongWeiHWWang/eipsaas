package com.hotent.bpm.persistence.manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmSecretaryManage;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * <pre> 
 * 描述：秘书管理表 处理接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2019-09-16 10:07:13
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BpmSecretaryManageManager extends BaseManager<BpmSecretaryManage>{
	
	/**
	 * 根据秘书id查询所有领导共享的流程
	 * @param secretaryId 秘书id
	 * @return  Map<领导id, 共享的流程key集合> 。分类id会查询出分类下的流程key
	 */
	Map<String, Set<String>> getShareDefsBySecretaryId(String secretaryId,String  rightType);
	
	/**
	 * 获取领导所共享的流程，以及领导自身的组织角色map
	 * @param secretaryId
	 * @return
	 */
	Map<String, Object> getLeadersRigthMapBySecretaryId(String secretaryId,String  rightType,Boolean isUser);
	
	/**
	 * 根据领导id和共享的流程key
	 * @param leaderIds
	 * @param defKey
	 * @return
	 */
	Map<String, Set<String>> getSecretaryByleaderIds(Set<String> leaderIds,String defKey);

    /**
     * 根据当前登录用户ID获取该用户的领导
     * @param userId
     * @return
     */
    List<BpmSecretaryManage> getSecretaryByUserId(String userId);
}
