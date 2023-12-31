package com.hotent.uc.manager;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserGroup;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.userGroup.UserGroupVo;

/**
 * 
 * <pre> 
 * 描述：群组管理  处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-11-27 17:55:17
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserGroupManager extends BaseManager<UserGroup>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	/**
	 * 根据alias取定义对象。
	 * @param code
	 * @return
	 */
	UserGroup getByCode(String code);
	
	/**
	 * 根据群组id抽取群组中的用户列表
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	List<User> getUserListByGroupId(String id) throws Exception;
	
	/**
	 * 根据群组id抽取群组中的用户列表
	 * @param id
	 * @return
	 */
	List<UserGroup> getByUserId(String id);
	
	/**
	 * 添加群组
	 * @param userGroupVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addUserGroup(UserGroupVo userGroupVo) throws Exception;
	
	/**
	 * 修改群组
	 * @param userGroupVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateUserGroup(UserGroupVo userGroupVo) throws Exception;
	
	/**
	 * 删除群组
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> delUserGroup(String codes) throws Exception;
	
	/**
	 * 群组添加用户组
	 * @param code
	 * @param json [{"type":"user","codes":"admin"},...]，其中type可填user、org、pos、role四种类型，分别代表用户、组织、岗位、角色，codes代表它们的代码，用户的填写account信息，多个用户英文逗号隔开
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addGroupUsers(String code,List<ObjectNode> json) throws Exception;
	
	/**
	 * 获取指定群组下的人员信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	List<User> getGroupUsers(String code) throws Exception;
	
	/**
	 *  根据时间获取群组数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<UserGroup> getUserGroupByTime(String btime,String etime) throws Exception ;
	/**
	 * 获取指定群组下人员分页信息
	 * @param code 
	 * @param queryFilter
	 * @return
	 */
	IPage<User> getGroupUsersPage(String code, QueryFilter queryFilter);
	
	/**
	 * 查询群组编码是否已存在
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isCodeExist(String code) throws Exception ;
	
	/**
	 * 更新群组管理员
	 * @param code
	 * @param account
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateGroupAuth(String code,String account) throws Exception ;
}
