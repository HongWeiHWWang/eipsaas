package com.hotent.uc.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.exception.SystemException;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.api.model.Group;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserGroup;
import com.hotent.uc.model.UserParams;
import com.hotent.uc.model.UserRole;
import com.hotent.uc.params.common.DataSyncObject;
import com.hotent.uc.params.common.DataSyncVo;
import com.hotent.uc.params.common.UserExportObject;
import com.hotent.uc.params.echarts.ChartOption;
import com.hotent.uc.params.group.GroupIdentity;
import com.hotent.uc.params.params.ParamObject;
import com.hotent.uc.params.user.TriggerVo;
import com.hotent.uc.params.user.UserMarkObject;
import com.hotent.uc.params.user.UserPolymer;
import com.hotent.uc.params.user.UserPwdObject;
import com.hotent.uc.params.user.UserRelObject;
import com.hotent.uc.params.user.UserStatusVo;
import com.hotent.uc.params.user.UserVo;


/**
 * 
 * <pre>
 * 描述：用户表 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:50
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserManager extends BaseManager<User>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	/**
	 * 添加用户
	 * @param user
	 */
	void addUser(UserVo user) throws Exception ;
	
	/**
	 * 保存用户
	 * @param user
	 * @throws Exception
	 */
	void saveUser(UserPolymer userPolymer) throws Exception;
	
	/**
	 * 更新用户
	 * @param user
	 */
	void updateUser(UserVo user)throws Exception ;
	
	/**
	 * 删除用户
	 * @param userMark
	 * @throws Exception
	 */
	CommonResult<String> deleteUser(UserMarkObject userMark)throws Exception ;
	
	/**
	 * 获取用户
	 * @param json
	 * @return
	 * @throws Exception
	 */
	UserVo getUser(String json) throws Exception;
	
	/**
	 * 根据Account取定义对象。
	 * @param code
	 * @return
	 */
	User getByAccount(String account) throws Exception;
	
	/**
	 * 不含用户组织关系
	 */
	List<User> getUserListByOrgId(String orgId) throws Exception;
	
	/**
	 * 不含用户组织关系
	 * @param queryFilter
	 * @return
	 */
	List<User> queryOrgUser(QueryFilter queryFilter) throws Exception;
	
	/**
	 * 含组织用户关系表数据
	 * @param queryFilter
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List queryOrgUserRel(QueryFilter queryFilter) throws Exception;
	
	/**
	 *  根据岗位编码获取用户列表
	 * @param postCode
	 * @return
	 */
	List<User> getListByPostCode(String postCode) throws Exception;
	
	/**
	 * 根据岗位ID获取用户列表
	 * @param postId
	 * @return
	 */
	List<User> getListByPostId(String postId) throws Exception;
	
	/**
	 * 根据职务ID获取用户列表
	 * @param jobId
	 * @return
	 */
	List<User> getListByJobId(String jobId) throws Exception;
	
	/**
	 * 根据角色ID获取用户列表
	 * @param roleId
	 * @return
	 */
	List<User> getUserListByRoleId(String roleId) throws Exception;
	
	/**
	 * 根据角色Code获取用户列表
	 * @param roleCode
	 * @return
	 */
	List<User> getUserListByRoleCode(String roleCode) throws Exception;
	
	/**
	 * 获取角色用户
	 * @param filter
	 * @return
	 */
	IPage<User> getRoleUserQuery(QueryFilter filter) throws Exception;
	
	/**
	 * 获取用户列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	IPage<User> getOrgUserQuery(QueryFilter<User> queryFilter) throws Exception;
	
	/**
	 * 根据email查询用户
	 * @param userId
	 * @param email
	 * @return
	 */
	List<User> getByUserEmail(String email) throws Exception;
	/**
	 * 获取用户的所有上级
	 * @param underUserId
	 * @return
	 */
	List<User> getUpUsersByUserId(String underUserId) throws Exception;
	
	/**
	 * 获取用户某组织下的上级
	 * @param account
	 * @param orgCode
	 * @return
	 */
	User getUpUserByUserIdAndOrgId(String account,String orgCode) throws Exception;
	
	/**
	 * 获取用户的所有下级
	 * @param upUserId
	 * @return
	 */
	List<User> getUnderUsersByUserId(String upUserId) throws Exception;
	
	/**
	 * 获取用户某组织下的下级用户
	 * @param account
	 * @param orgCode
	 * @return
	 */
	List<User> getUnderUserByUserIdAndOrgId(String account,String orgCode) throws Exception;
	
	/**
	 * 根据工号取定义对象。
	 * @param userNumber
	 * @return
	 */
     User getByNumber(String userNumber) throws Exception;
     
     /**
      * 用户修改密码
      */
     CommonResult<String> changUserPsd(UserPwdObject userPwdObject) throws Exception;
     
     /**
      * 修改用户密码
      */
     CommonResult<String> updateUserPsw(UserPwdObject userPwdObject) throws Exception;
     
     /**
      * 保存用户参数
      * @param account
      * @param params
      * @return
      */
     CommonResult<String> saveUserParams(String account,List<ParamObject> params) throws Exception;
     
     /**
      * 获取用户所有参数
      * @param account
      * @return
      * @throws Exception
      */
     List<UserParams> getUserParams(String account)throws Exception;
     
     /**
      * 获取用户所有参数
      * @param account
      * @param code
      * @return
      * @throws Exception
      */
     UserParams getParamByCode(String account,String code)throws Exception;
    /**
     * 获取用户所属主组织信息
     * @param userRelObject
     * @return
     * @throws Exception
     */
     CommonResult<Org> getMainOrgByDemCode(UserRelObject userRelObject)throws Exception;
     
     /**
      * 获取用户所属岗位信息
      * @param userRelObject
      * @return
      * @throws Exception
      */
     List<OrgPost> getUserPosts(UserRelObject userRelObject) throws Exception;
     
     /**
      * 获取用户直属上级信息
      * @param userRelObject
      * @return
      * @throws Exception
      */
     Set<GroupIdentity> getImmeSuperior(UserRelObject userRelObject) throws Exception;
     
     /**
      * 根据用户账号（或工号）、维度、级别，获取用户直属下级信息
      * @param userRelObject
      * @return
      * @throws Exception
      */
     Set<GroupIdentity> getImmeUnders(UserRelObject userRelObject) throws Exception;
     
     /**
      * 根据用户账号（或工号）获取用户职务信息
      * @param json
      * @return
      * @throws Exception
      */
     List<OrgJob> getUserJobs(String json) throws Exception;
     
     /**
      * 根据用户账号（或工号）获取用户群组信息
      * @param json
      * @return
      * @throws Exception
      */
     List<UserGroup> getUserGroups(String json) throws Exception;
     /**
 	 * 定时获取AD新增人员信息
 	 * @param date AD同步时间
 	 * @return
 	 * @throws Exception
 	 */
 	List<UserVo> getNewUsersFromAD(String date)throws Exception;
 	
 	/**
 	 * 根据角色编码、组织编码获取对应人员
 	 * @param roleCode
 	 * @param orgCode
 	 * @return
 	 * @throws Exception
 	 */
 	Set<GroupIdentity> getByRoleCodeAndOrgCode(String roleCode,String orgCode)throws Exception;
 	
 	/**
 	 * 根据职务编码、组织编码获取对应人员
 	 * @param jobCode
 	 * @param orgCode
 	 * @return
 	 * @throws Exception
 	 */
 	Set<GroupIdentity> getByJobCodeAndOrgCode(String jobCode,String orgCode)throws Exception;
 	
 	/**
 	 * 根据岗位编码、组织编码获取对应人员
 	 * @param postCode
 	 * @param orgCode
 	 * @return
 	 * @throws Exception
 	 */
 	Set<GroupIdentity> getByPostCodeAndOrgCode(String postCode,String orgCode)throws Exception;
 	
 	/**
 	 * 获取指定用户的所有上级（下属管理）
 	 * @param account
 	 * @return
 	 * @throws Exception
 	 */
 	List<UserVo> getUpUsersByUser(String account)throws Exception;
 	
 	/**
 	 * 获取指定用户在指定组织中的上级（下属管理）
 	 * @param account
 	 * @param orgCode
 	 * @return
 	 * @throws Exception
 	 */
 	UserVo getUpUserByUserAndOrg(String account,String orgCode)throws Exception;
 	
 	/**
 	 * 获取指定用户的所有下级（下属管理）
 	 * @param account
 	 * @return
 	 * @throws Exception
 	 */
 	List<UserVo> getUnderUsersByUser(String account)throws Exception;
 	
 	/**
 	 * 获取指定用户在指定组织中的下级（下属管理）
 	 * @param account
 	 * @param orgCode
 	 * @return
 	 * @throws Exception
 	 */
 	List<UserVo> getUnderUserByUserAndOrg(String account,String orgCode)throws Exception;
 	
 	 /**
 	 * 获取组织下人员
 	 * @param orgId
 	 * @param isMain 是否主岗位，不填则获取所有
 	 * @return
 	 */
 	List<User> getOrgUsers(String orgId,Boolean isMain) throws Exception;
 	
 	/**
 	 * 更新用户头像
 	 * @param account
 	 * @param photo
 	 * @throws Exception
 	 */
 	void updatePhoto(String account,String photo)throws Exception;
 	
 	/**
 	 * 禁用用户
 	 * @param userMark
 	 * @throws Exception
 	 */
 	CommonResult<String> forbiddenUser(UserMarkObject userMark) throws Exception;
 	
 	/**
 	 * 激活用户
 	 * @param userMark
 	 * @throws Exception
 	 */
 	CommonResult<String> activateUser(UserMarkObject userMark) throws Exception;
 	
 	/**
 	 * 用户离职
 	 * @param userMark
 	 * @throws Exception
 	 */
 	CommonResult<String> leaveUser(UserMarkObject userMark) throws Exception;
 	
 	/**
 	 * 导出用户
 	 * @param isOrg
 	 * @param isRole
 	 * @param isAll
 	 * @param queryFilter
 	 * @return
 	 * @throws Exception
 	 */
 	String exportUsers(boolean isOrg,boolean isRole,boolean isAll,QueryFilter queryFilter) throws Exception;
 	
	/**
	 * 通过岗位编码获取用户
	 * @param postCode
	 * @return
	 */
	List<UserVo> getUserByPost(String postCode) throws Exception;
	
	/**
	 * 根据时间获取用户数据（数据同步）
	 * @param userExport
	 * @return
	 * @throws Exception
	 */
	List<User> getUserByTime(UserExportObject userExport) throws Exception ;
	
	/**
	 *  根据时间获取用户参数数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<UserParams> getUserParamByTime(String btime,String etime) throws Exception ;
	
	/**
	 *  根据时间获取用户角色关系数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<UserRole> getUserRoleByTime(String btime,String etime) throws Exception ;
	
	/**
	 * 获取副本数据集合
	 * @param dataSync
	 * @return
	 * @throws Exception
	 */
	DataSyncVo getSyncDataByTime(DataSyncObject dataSync) throws Exception ;
	
	/**
	 * 根据用户标识获取用户汇报关系图信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	ChartOption getUserRelCharts(String json) throws Exception ;
	/**
	 * 根据组织编码获取组织的负责人
	 * @param orgCode
	 * @param isMain
	 * @return
	 * @throws Exception
	 */
	List<User> getChargesByOrg(String orgCode,Boolean isMain) throws Exception ;
	
	/**
	 * 用于是否显示AD增量同步按钮
	 * @return
	 */
	boolean showADButton();
	/**
	 * 批量设置用户状态
	 * @return
	 * @throws Exception
	 */
	void setStatus(UserStatusVo userStatusVo) throws Exception;
	
	/**
	 * 判断账号是否已存在
	 * @param account
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isAccountExist(String account) throws Exception;
	
	/**
	 * 判断工号是否已存在
	 * @param userNumber
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isUserNumberExist(String account, String userNumber) throws Exception;
	/**
	 * 用户修改个人密码
	 * @param userPwdObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateOneselfPsw(UserPwdObject userPwdObject) throws Exception;
	/**
     * 上传头像文件
     * @param file
     * @return
     */
    CommonResult<String> uploadPortrait(String account, MultipartFile file) throws Exception;
    /**
     * 下载头像文件
     * @param filename
     * @return
     * @throws Exception
     */
    Resource downloadPortrait(String account, String filename) throws Exception;
    
    /**
     * 获取维度下人员(不传维度或组织信息，则获取的是所有加入组织的用户)
     * @param filter
     * @return
     * @throws Exception
     */
    IPage<User> getDemUserQuery(QueryFilter filter) throws Exception;
    
    /**
     * 修改计划定时任务执行时间
     * @param triggerVo
     * @return
     * @throws Exception
     */
    CommonResult<String> setTrigger(TriggerVo triggerVo,String ip) throws Exception;
    
    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    CommonResult<UserVo> getUserById(String userId) throws Exception;
   /**
    * 修改用户基本信息
    * @param vo
    * @return
    * @throws Exception
    */
	CommonResult<String> saveUser(UserVo vo) throws Exception;
	
	 /**
     * 查询实体对象
     * @param queryFilter	通用查询对象
     * @return				分页结果
     * @throws SystemException
     */
    public PageList<User> queryByType(QueryFilter queryFilter) throws SystemException;
    
    /**
     * 根据用户id删除
     * @param ids
     * @return
     */
	CommonResult<String> deleteUserByIds(String ids);

	/**
	 * 根据
	 * @param queryFilter
	 * @return
	 */
	List<UserVo> queryUser(QueryFilter queryFilter);
	
	/**
	 * 通过组织中的下属设置获取上级人员
	 * @param userId
	 * @param orgId
	 * @param demId
	 * @return
	 * @throws Exception
	 */
	List<User> getSuperFromUnder(String userId,String orgId,String demId)throws Exception;
	
	/**
	 * 根据userid查询姓名及组织
	 * @param userId
	 * @return
	 */
	Map<String,Object> getUserDetailed(String userId);

	/**
	 * 根据用户id获取用户所在部门负责人
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<UserVo>  getDepHeader(String userId, Boolean isMain) throws Exception;

	
	/**
	 * 根据部门id获取部门负责人
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	List<UserVo>  getDepHeaderByOrg(String orgId , Boolean isMain) throws Exception;

	/**
	 * 根据用户id获取用户所属组织，岗位，角色相关信息
	 * @param userId
	 * @return
	 */
	List<Group> getGroupsByUserId(String userId,String type);

	/**
	 * 根据用户id取用户信息，包含组织岗位
	 * @param userId
	 * @return
	 * @throws IOException 
	 */
	Map<String, Object> getUserDetailByAccountOrId(String userId) throws IOException;
	
	/**
	 * 模糊查询用户列表
	 * @param query
	 * @return
	 */
	public List<User> getUserByName(String query);

	Map<String, Object> calculateNodeUser(Map<String, Object> nodeMap);
	
	/**
	 * 根据公众号id获取用户信息
	 * @param openId
	 * @return
	 */
	CommonResult<UserVo> getUserByOpenId(String openId);

	Map<String, Map<String, String>> getUserRightMapByIds(Set<String> ids);

	CommonResult<UserVo> getUserByMobile(String mobile);

	
	/**
	 * 根据签署数据获取用户信息
	 * @param customSignDatas
	 * @return
	 */
	ArrayNode getUserInfoBySignData(ArrayNode customSignDatas) throws Exception ;



	IPage<User> getGroupUsersPage(UserGroup userGroup, QueryFilter queryFilter);

	CommonResult<String> getTokenByUserName(String userName);
}
