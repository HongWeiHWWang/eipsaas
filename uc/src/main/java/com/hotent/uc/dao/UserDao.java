package com.hotent.uc.dao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.uc.model.User;
import com.hotent.base.constants.SQLConst;

/**
 * 
 * <pre> 
 * 描述：用户表 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:50
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserDao extends BaseMapper<User>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 根据Account取定义对象。
	 * @param account
	 * @return
	 */
	User getByAccount(@Param("account") String account);

	/**
	 * 根据account获取记录数
	 * @param account
	 * @return
	 */
	Integer getCountByAccount(@Param("account")String account);

    /**
     * 根据mobile取定义对象。
     * @param mobile
     * @return
     */
    User getByMobile(@Param("mobile") String mobile);
	
	/**
	 * 不含用户组织关系
	 */
	List<User> getUserListByOrgId(@Param("orgId") String orgId);
	
	
	/**
	 * 不含用户组织关系
	 * @param wrapper
	 * @return
	 */
	List<User> queryOrgUser(@Param(Constants.WRAPPER) Wrapper<User> wrapper);
	
	/**
	 * 含组织用户关系表数据
	 * @param wrapper
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List queryOrgUserRel(@Param(Constants.WRAPPER)Wrapper<User> wrapper);
	
	/**
	 * 根据岗位编码获取用户列表
	 * @param postCode
	 * @return
	 */
	List<User> getListByPostCode(@Param("postCode") String postCode);
	
	/**
	 * 根据岗位ID获取用户列表
	 * @param relCode
	 * @return
	 */
	List<User> getListByPostId(@Param("postId") String postId);
	
	
	/**
	 * 根据角色ID获取用户列表
	 * @param roleId
	 * @return
	 */
	List<User> getUserListByRoleId(@Param("roleId") String roleId);
	
	/**
	 * 根据角色Code获取用户列表
	 * @param roleCode
	 * @return
	 */
	List<User> getUserListByRoleCode(@Param("roleCode") String roleCode);
	
	/**
	 * 获取角色用户（含分页）
	 * @param iPage 
	 * @param map
	 * @return
	 */
	IPage<User> getUserListByRoleCodeMap(IPage<User> iPage,@Param(Constants.WRAPPER) Wrapper<User> params);
	
	/**
	 * 获取用户列表（分页）
	 * @param iPage
	 * @param params
	 * @return
	 */
	IPage<User> getUserListByOrgQuery(IPage<User> iPage,@Param(Constants.WRAPPER) Wrapper<User>  wrapper);
	
	/**
	 * 根据 email查询用户
	 * @param email
	 * @return
	 */
	List<User> getByUserEmail(@Param("email") String email);
	
	/**
	 * 获取用户的所有上级
	 * @param underUserId
	 * @return
	 */
	List<User> getUpUsersByUserId(@Param("underUserId") String underUserId);
	
	/**
	 * 获取用户某组织下的上级
	 * @param underUserId
	 * @param orgId
	 * @return
	 */
	User getUpUserByUserIdAndOrgId(@Param("underUserId") String underUserId,@Param("orgId") String orgId);
	
	/**
	 * 获取用户的所有下级
	 * @param upUserId
	 * @return
	 */
	List<User> getUnderUsersByUserId(@Param("upUserId") String upUserId);
	
	/**
	 * 获取用户某组织下的下级用户
	 * @param upUserId
	 * @param orgId
	 * @return
	 */
	List<User> getUnderUserByUserIdAndOrgId(@Param("upUserId") String upUserId,@Param("orgId") String orgId);
	
	/**
	 * 根据工号取定义对象。
	 * @param userNumber
	 * @return
	 */
	User getByNumber(@Param("userNumber") String userNumber);
	
	/**
	 * 获取组织下人员
	 * @param map
	 * @return
	 */
	List<User> getOrgUsers(Map<String,String> map);
	
	/**
	 * 根据组织编码、职务编码获取
	 * @param orgCode
	 * @param jobCode
	 * @return
	 */
	List<User> getByJobCodeAndOrgCode(@Param("orgCode") String orgCode,@Param("jobCode") String jobCode);
	
	
	/**
	 * 通过岗位编码获取用户
	 * @param postCode
	 * @return
	 */
	List<User> getUserByPost(@Param("postCode") String postCode);
	
	/**
	 * 通过账号批量设置用户状态
	 * @param status
	 * @param accounts
	 */
	void updateStatusByAccounts(@Param("status")Integer status, @Param("accounts")List<String> accounts,@Param("updateTime")LocalDateTime updateTime);
	/**
	 * 获取指定群组下人员分页信息
	 * @param convert2iPage 
	 * @param params
	 * @return
	 */
	IPage<User> getGroupUsersPage(IPage<User> convert2iPage, @Param(SQLConst.QUERY_FILTER) Map<String, Object> params);
	
	/**
	 * 根据多维sql获取用户
	 * @param wrapper
	 * @param demSql
	 * @return
	 */
	List<User> queryByDim(@Param(Constants.WRAPPER)Wrapper<User> wrapper);
	
	/**
	 * 查询账号是否已存在
	 * @param account
	 * @return
	 */
	Integer queryByAccount(String account);
	
	/**
	 * 查询工号是否已存在
	 * @param userNumber
	 * @return
	 */
	Integer queryByUserNumber(@Param("account")String account, @Param("userNumber")String userNumber);
	
	/**
	 * 获取维度下的用户
	 * @param iPage 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	IPage<User> getDemUserQuery(IPage<User> iPage,@Param(Constants.WRAPPER) Wrapper<User> params) throws Exception;
	
	/**
	 * 根据帐号获取已被逻辑删除的数据
	 * @param account
	 * @return
	 * @throws Exception
	 */
	User getDelDataByAccount(@Param("account") String account) throws Exception;

	IPage<User> queryByType(IPage<User> iPage, @Param(Constants.WRAPPER)Wrapper<User> convert2Wrapper);
	
	/**
	 * 通过组织中的下属设置获取上级人员
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<User> getSuperFromUnder(Map<String, Object> params)throws Exception;
	
	/**
	 * 根据userid查询姓名及组织
	 * @param userId
	 * @return
	 */
	Map<String,Object> getUserDetailed(@Param("userId")String userId);
	
	/**
	 * 模糊查询用户信息
	 * @param query
	 * @return
	 */
	List<User> getUserByName(@Param("query")String query);
	
	/**
	 * 根据openId查询用户信息
	 * @param openId
	 * @return
	 */
	User getUserByOpenId(@Param("openId") String openId);
}
