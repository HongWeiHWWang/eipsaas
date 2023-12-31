package com.hotent.uc.manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.OrgParams;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgRole;
import com.hotent.uc.model.OrgTree;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.UserUnder;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.org.OrgAuthVo;
import com.hotent.uc.params.org.OrgPostVo;
import com.hotent.uc.params.org.OrgUserVo;
import com.hotent.uc.params.org.OrgVo;
import com.hotent.uc.params.orgRole.OrgRoleVo;
import com.hotent.uc.params.post.PostDueVo;
import com.hotent.uc.params.user.UserUnderVo;
import com.hotent.uc.params.user.UserVo;

/**
 * 
 * <pre> 
 * 描述：组织架构 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-28 15:13:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgManager extends BaseManager<Org>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 根据Code取定义对象。
	 * @param code
	 * @return
	 */
	Org getByCode(String code);

	/**
	 * 根据用户ID获取组织列表
	 * @param userId
	 * @return
	 */
	List<Org> getOrgListByUserId(String userId);
	
	/**
	 * 通过用户ID获取组织ID、是否主组织Map
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getOrgIdMapByUserId(String userId);
	
	/**
	 * 通过父组织ID集合查询所有子组织ID
	 * @param parentIds
	 * @return
	 */
	List<String> getSubOrgByIds(Set<String> parentIds);
	
	/**
	 * 根据用户账号获取组织列表
	 * @param account
	 * @return
	 * @throws Exception 
	 */
	List<Org> getOrgListByAccount(String account) throws Exception;
	
	/**
	 * 获取主组织。
	 * @param userId
	 * @param demId
	 * @return
	 */
	Org getMainGroup(String userId,String demId);
	
	/**
	 * 根据父组织id获取其下子组织
	 * @param parentId
	 * @return
	 */
	List<Org> getByParentId(String parentId);
	
	List<Org> getByOrgName(String orgName);
	
	/**
	 * 根据路径名获取组织
	 * @param pathName
	 * @return
	 */
	List<Org> getByPathName(String pathName);
	
	/**
	 * 根据父级id以及维度id获取组织
	 * @param queryFilter
	 * @return
	 */
	List<Org> getByParentAndDem(QueryFilter queryFilter);
	
	/**
	 * 通过子组织获取父组织
	 * @param demId
	 * @param sonId
	 * @return
	 */
	Org getByDemIdAndSonId(String demId, String sonId);
	
	/**
	 * 更新组织，包括其子组织
	 * @param org
	 */
	void updateByOrg(Org org) throws Exception;
	
	/**
	 * 获取用户主组织
	 * @param userId
	 * @param demId
	 * @return
	 */
	List<Org> getMainOrgListByUser(String userId,String demId);
	
	/**
	 * 根据维度ID获取组织列表
	 * @param userId
	 * @return
	 */
	List<Org> getOrgListByDemId(String demId);
	
	/**
	 * 获取用户所属（主）组织
	 * @param map
	 * @return
	 */
	List<Org> getUserOrg(String userId, String demId, Boolean isMain);
	
	/**
	 * 添加组织
	 * @param orgVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addOrg(OrgVo orgVo) throws Exception;
	
	/**
	 * 根据组织代码删除组织
	 * @param codes 组织编码
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> deleteOrg(String codes) throws Exception;
	
	/**
	 * 更新组织，连同子组织一起更新
	 * @param orgVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateOrg(OrgVo orgVo) throws Exception;
	
	/**
	 * 获取组织信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	Org getOrg(String code) throws Exception;
	
	/**
	 * 保存组织参数
	 * @param orgCode
	 * @param params 格式[{alias:"a1",value:"v1"},{alias:"a2",value:"v2"}...]
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> saveOrgParams(String orgCode,List<ObjectNode> params) throws Exception;
	
	/**
	 * 根据组织代码获取组织参数
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	List<OrgParams> getOrgParams(String orgCode) throws Exception;
	
	/**
	 * 根据参数别名获取组织参数
	 * @param orgCode 组织编码
	 * @param alias 组织参数别名
	 * @return
	 * @throws Exception
	 */
	CommonResult<OrgParams> getParamByAlias(String orgCode,String alias) throws Exception;
	
	/**
	 * 加入用户
	 * @param orgUserVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addOrgUser(OrgUserVo orgUserVo) throws Exception;
	
	/**
	 * 用户取消加入组织
	 * @param ids 用户组织关系ids，多个用英文逗号隔开
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> delOrgUser(String ids) throws Exception;
	
	/**
	 * 判断用户是否有主组织
	 * @param account
	 * @param demCode
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> getUserIsMaster(String account,String demCode) throws Exception;
	
	/**
	 * 设置人员（取消）主岗位
	 * @param account
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> setMaster(String account,String postCode) throws Exception;
	
	/**
	 * 获取组织树
	 * @param demCode 组织维度编码
	 * @param pOrgCode 父组织id
	 * @return
	 * @throws Exception
	 */
	List<OrgTree> getTreeDataByDem(String demCode,String pOrgCode) throws Exception;
	
	/**
	 * 设置（取消）（主）负责人
	 * @param account
	 * @param orgCode
	 * @param isCharge true表示设置主负责人，为false时，若此时为负责人，则降为非负责人
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> setOrgCharge(String account,String orgCode,Boolean isCharge) throws Exception;
	
	/**
	 * 组织人员添加下属
	 * @param userUnderObj
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addUserUnders(UserUnderVo userUnderObj) throws Exception;
	
	/**
	 * 组织人员删除下属
	 * @param userUnderObj
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> delUserUnders(String account,String orgCode) throws Exception;
	
	/**
	 * 获取用户在某组织下的下属
	 * @param account
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	List<UserVo> getUserUnders(String account,String orgCode) throws Exception;
	
	/**
	 * 添加分级管理
	 * @param orgAuthVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> saveOrgAuth(OrgAuthVo orgAuthVo) throws Exception;
	
	/**
	 * 删除分级管理
	 * @param id
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> deleteOrgAuth(String id) throws Exception;
	
	/**
	 * 获取分级管理列表
	 * @param account
	 * @param orgCode
	 * @param demCode
	 * @return
	 * @throws Exception
	 */
	Page<OrgAuth> getOrgAuthList(String account,String orgCode,String demCode) throws Exception;
	
	/**
	 * 获取分级管理
	 * @param id
	 * @return
	 * @throws Exception
	 */
	OrgAuth getOrgAuth(String id) throws Exception;
	
	/**
	 * 获取用户所属（主）组织
	 * @param account 获取
	 * @param demCode 维度代码（选填）
	 * @param isMain 是否主组织
	 * @return
	 * @throws Exception
	 */
	List<Org> getUserOrgs(String account,String demCode,Boolean isMain) throws Exception;
	
	/**
	 * 获取组织下的人员
	 * @param orgCodes 组织编码，多个用英文逗号隔开
	 * @param isMain 是否主组织
	 * @return
	 * @throws Exception
	 */
	List<UserVo> getUsersByOrgCodes(String orgCodes,Boolean isMain) throws Exception;
	
	/**
	 * 获取组织下的岗位
	 * @param orgCodes 组织编码，多个用英文逗号隔开
	 * @param isMain 是否主组织
	 * @return
	 * @throws Exception
	 */
	List<OrgPost> getPostsByOrgCodes(String orgCodes,Boolean isMain) throws Exception;
	
	/**
	 * 根据级别获取组织
	 * @param level 组织级别
	 * @param demCode 维度编码（选填）
	 * @return
	 * @throws Exception
	 */
	List<Org> getByLevel(String level,String demCode) throws Exception;
	
	/**
	 * 通过用户账号获取所属组织
	 * @param account 账号
	 * @return
	 * @throws Exception
	 */
	List<Org> getOrgsByAccount(String account) throws Exception;
	
	/**
	 * 用户加入到岗位
	 * @param accounts 用户帐号，多个用英文逗号隔开
	 * @param postCode 岗位代码
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> saveUserPost(String accounts,String postCode) throws Exception;
	
	/**
	 * 用户加入到岗位
	 * @param account 用户帐号
	 * @param postCode 岗位代码，多个用英文逗号隔开
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> saveUserPosts(String account,String postCodes) throws Exception;
	
	/**
	 *  用户退出岗位
	 * @param accounts 用户帐号，多个用英文逗号隔开
	 * @param postCode 岗位代码
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> delUserPost(String accounts,String postCode) throws Exception;
	
	/**
	 * 组织添加岗位
	 * @param orgPostVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> saveOrgPost(OrgPostVo orgPostVo) throws Exception;
	
	/**
	 * 组织删除岗位，连同岗位下的人员信息一起删除
	 * @param postCodes 岗位编码，多个用英文逗号隔开
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> deleteOrgPost(String postCodes) throws Exception;
	
	/**
	 * 设置组织（取消）主岗位
	 * @param postCode
	 * @param isMain
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> setPostMaster(String postCode,Boolean isMain) throws Exception;
	
	/**
	 * 设置人员岗位有效期
	 * @param postDueVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> setUserPostDueTime(PostDueVo postDueVo) throws Exception;
	
	/**
	 * 校验所有用户岗位是否有效
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> validOrgUser() throws Exception;
	
	/**
	 * 组织添加角色
	 * @param orgRoleVo
	 * @return 
	 * @throws Exception
	 */
	CommonResult<String> addOrgRole(OrgRoleVo orgRoleVo) throws Exception;
	
	/**
	 * 删除组织指定的角色
	 * @param orgCode
	 * @param roleCodes
	 * @throws Exception
	 */
	void delOrgRoleByCode(String orgCode,String roleCodes) throws Exception;
	
	/**
	 * 删除组织所拥有的角色
	 * @param orgCode
	 * @throws Exception
	 */
	void delAllOrgRole(String orgCode) throws Exception;
	
	/**
	 * 获取组织所拥有的角色
	 * 1.首先查找组织自身的角色，有则返回
	 * 2.若组织自身没有角色，则从下往上找父组织的角色（可继承的），有则返回最近的
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	List<Role> getOrgRoleByCode(String orgCode) throws Exception;
	
	/**
	 * 更新岗位
	 * @param postVo
	 * @throws Exception
	 */
	CommonResult<String> updateOrgPost(OrgPostVo postVo) throws Exception;
	
	/**
	 * 添加下属
	 * @param orgId
	 * @param account
	 * @param underAccounts
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> setUnderUsers(String orgId,String account,String underAccounts) throws Exception;
	
	
	/**
	 * 组织批量添加用户
	 * @param orgCode
	 * @param accounts
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addUsersForOrg(String orgCode,String accounts) throws Exception;
	
	/**
	 *  根据时间获取组织数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<Org> getOrgByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 *  根据时间获取组织参数数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<OrgParams> getOrgParamByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 * 根据时间获取组织岗位数据（数据同步）
	 * @param exportObject
	 * @return
	 * @throws Exception
	 */
	List<OrgPost> getOrgPostByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 * 根据时间获取组织角色数据（数据同步）
	 * @param exportObject
	 * @return
	 * @throws Exception
	 */
	List<OrgRole> getOrgRoleByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 *  根据时间获取用户组织关系数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<OrgUser> getOrgUserByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 * 根据时间获取组织中下属数据（数据同步）
	 * @param exportObject
	 * @return
	 * @throws Exception
	 */
	List<UserUnder> getUserUnderByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePostPhysical() throws Exception ;
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removeOrgUserPhysical() throws Exception ;
	
	/**
	 * 查询组织编码是否已存在
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isCodeExist(String code) throws Exception ;
	
	/**
	 * 查询岗位编码是否已存在
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isPostCodeExist(String code) throws Exception ;

	CommonResult<String> delOrgRoleById(String id) throws Exception;
	
	/**
	 * 更新组织所在树的位置
	 * @param orgId
	 * @param parentId
	 * @return
	 */
	CommonResult<String> updateOrgPos(String orgId,String parentId) throws Exception ;
	
	/**
	 * 导出组织用户数据
	 * @param orgCode
	 * @param isChildre
	 * @return
	 * @throws Exception
	 */
	String exportData(String orgCode,boolean isChildre) throws Exception;

	List<Map<String, String>> getPathNames(List<String> userIds);
	
	/**
	 * 获取子组织ids
	 * @param ids
	 * @return
	 */
	Map<String, Set<String>> getChildrenIds(Map<String,String> ids);

	/**
	 * 根据用户id 查询主组织
	 * @param userId
	 * @return
	 */
	Org getOrgMaster(String account);

	/**
	 * 从第三方获取组织数据添加到本系统
	 * @param orgVo
	 * @return
	 */
	CommonResult<String> addOrgFromExterUni(Org orgVo);

	/**
	 * 获取子组织(只获取底下一层子组织)
	 * @param parentId
	 * @return
	 */
	List<Org> getOrgsByparentId(String parentId);
	
	/**
	 * 获取填制单位
	 * @param demId
	 * @param grade
	 * @return
	 */
	CommonResult<Org> getFillOrg(String demId,String grade);
}
