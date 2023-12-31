package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.model.Group;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.exception.BaseException;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.mock.MockUCDataUtil;
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
import com.hotent.uc.service.UserManagerDetailsServiceImpl;
import com.hotent.uc.util.ContextUtil;
import com.hotent.uc.util.OrgUtil;


/**
 * 用户组织模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/user/v1/")
@Api(tags="UserController")
public class UserController extends BaseController {
	 @Autowired
	 UserManagerDetailsServiceImpl userManagerDetailsServiceImpl;
	/**
	 * 查询用户
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUserPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户列表（带分页信息，UserVo对象）", httpMethod = "POST", notes = "获取用户列表（带分页信息，UserVo对象）")
	public PageList<UserVo> getUserPage(@ApiParam(name="queryFilter",value="通用查询对象")
	 @RequestBody QueryFilter queryFilter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 用户列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/listJson",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户列表（带分页信息，User对象）", httpMethod = "POST", notes = "获取用户列表（带分页信息，User对象）")
	public PageList<User> listJson(@ApiParam(name="queryFilter",value="通用查询对象")
	 @RequestBody QueryFilter queryFilter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 查询用户
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/queryByType",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户列表（带分页信息）", httpMethod = "POST", notes = "获取用户列表")
	public PageList<UserVo> queryByType(@ApiParam(name="queryFilter",value="通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception{
		//queryFilter.addFilter("ucuser.IS_DELE_", User.DELETE_NO, QueryOP.EQUAL, FieldRelation.AND, "delete_group");
		//PageList<User> query = userService.queryByType(queryFilter);
		PageList<User> query = MockUCDataUtil.queryByType(queryFilter);
		return convertVoPageList(query);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="user/addUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加用户", httpMethod = "POST", notes = "添加用户")
	public CommonResult<String> addUser(@ApiParam(name="user",value="用户参数对象", required = true) @RequestBody UserVo user) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="user/saveUser",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "修改，保存用户(包括角色、组织和岗位信息)", httpMethod = "POST", notes = "添加用户(包括角色、组织和岗位信息)")
	public CommonResult<String> saveUser(@ApiParam(name="userPolymer",value="用户参数对象", required = true) @RequestBody UserPolymer userPolymer) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="user/saveUserBaseInfo",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "修改用户基本信息", httpMethod = "POST", notes = "修改用户基本信息")
	public CommonResult<String> saveUserBaseInfo(@ApiParam(name="vo",value="用户参数对象", required = true) @RequestBody UserVo vo) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户帐号删除用户
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/deleteUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户标识删除用户（多个用,号隔开）", httpMethod = "POST", notes = "根据用户标识（多个用,号隔开）删除用户，参数 （任传一个）")
	public CommonResult<String> deleteUser(@ApiParam(name="userMark",value="用户标识") @RequestBody(required=false) UserMarkObject userMark) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户id删除用户
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/deleteUserByIds",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户id删除用户（多个用,号隔开）", httpMethod = "DELETE", notes = "根据用户id（多个用,号隔开）删除用户，参数 （任传一个）")
	public CommonResult<String> deleteUserByIds(@ApiParam(name="ids",value="用户标识") @RequestParam(required=false) String ids) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 更新用户，不会更新id、密码、帐号信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/updateUser",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新用户", httpMethod = "PUT", notes = "更新用户（不会更新id、密码、帐号、头像、来源、是否删除、版本号信息）")
	public CommonResult<String> updateUser(@ApiParam(name="user",value="用户参数对象", required = true) @RequestBody  UserVo user) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 获取用户信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getUser",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户标识获取用户信息", httpMethod = "GET", notes = "获取用户信息，参数 （任传一个）{\"account\":\"用户账号\",\"userNumber\":\"用户工号\"}")
	public UserVo getUser(@ApiParam(name="account",value="用户账号") @RequestParam(required=true) String account,
			@ApiParam(name="userNumber",value="用户工号") @RequestParam(required=false) String userNumber) throws Exception{
		// return userService.getUser(getJsonString(account,userNumber));
		return MockUCDataUtil.getUserByAccountOrUserNumber(getJsonString(account,userNumber));
	}
	
	@RequestMapping(value="user/getByAccount",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户账号获取用户信息", httpMethod = "POST", notes = "根据用户账号获取用户信息")
	public User getByAccount(@ApiParam(name="account",value="用户账号") @RequestParam(required=true) Optional<String> account) throws Exception{
		//return userService.getByAccount(account.orElse(current()));
		return MockUCDataUtil.getUserByAccount(account.orElse(current()));
	}
	
	@RequestMapping(value="user/getUserByAccounts",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据多个用户账号获取用户信息(以逗号隔开)", httpMethod = "GET", notes = "根据多个用户账号获取用户信息(以逗号隔开)")
	public List<UserVo> getUserByAccounts(@ApiParam(name="accounts",value="用户账号") @RequestParam(required=true) String accounts) throws Exception{
		//QueryFilter queryFilter = QueryFilter.build();
		//queryFilter.addFilter("account_", accounts, QueryOP.IN);
		//List<UserVo> users = userService.queryUser(queryFilter);
		List<UserVo> users = MockUCDataUtil.getUserByAccounts(accounts);
		return users;
	}
	
	
	
	@RequestMapping(value="user/loadUserByUsername",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	//@ApiOperation(value = "根据账号获取userDetails信息", httpMethod = "POST", notes = "根据账号获取userDetails信息")
	public IUser loadUserByUsername(@ApiParam(name="account",value="用户账号") @RequestParam(required=true) String account) throws Exception{
		IUser loadUserByUsername = (IUser) userManagerDetailsServiceImpl.loadUserByUsername(account);
		return loadUserByUsername;
	}
	
	/**
	 * 用户修改密码
	 * @param userPwdObject
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/changUserPsd",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "用户修改密码", httpMethod = "POST", notes = "修改用户密码（账号和工号任传其一，两个都有值时，只用账号）")
	public CommonResult<String> changUserPsd(@ApiParam(name="userPwdObject",value="用户密码相关参数",required=true) @RequestBody UserPwdObject userPwdObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 管理员修改用户密码
	 * @param userPwdObject
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/updateUserPsw",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "管理员修改用户密码", httpMethod = "POST", notes = "管理员修改用户密码（不用传旧密码；账号和工号任传其一，两个都有值时，只用账号）")
	public CommonResult<String> updateUserPsw(@ApiParam(name="userPwdObject",value="用户密码相关参数",required=true) @RequestBody UserPwdObject userPwdObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 导入用户
	 * @param user
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/importExcelUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "导入Excel用户", httpMethod = "POST", notes = "导入Excel用户")
	public CommonResult<String> importExcelUser(@ApiParam(name="demCode",value="维度编码",required=true)  @RequestParam String demCode,
			@ApiParam(name="file",value="导入的Excel文件",required=true) @RequestBody MultipartFile file){
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 保存用户参数
	 * @param user
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/saveUserParams",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "保存用户参数", httpMethod = "POST", notes = "保存用户参数")
	public CommonResult<String> saveUserParams(@ApiParam(name="account",value="用户账号",required=true)  @RequestParam String account,
			@ApiParam(name="params",value="用户参数",required=true) @RequestBody List<ParamObject> params) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取用户所有参数
	 * @param account
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getUserParams",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所有参数", httpMethod = "GET", notes = "获取用户所有参数")
	public List<UserParams> getUserParams(@ApiParam(name="account",value="用户账号",required=true) @RequestParam String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取用户所有参数
	 * @param account
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getParamByCode",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所有参数", httpMethod = "GET", notes = "获取用户所有参数")
	public UserParams getParamByCode(@ApiParam(name="account",value="用户账号",required=true) @RequestParam String account,@ApiParam(name="code",value="参数别名",required=true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户账号（或工号）、维度，获取用户所属主组织信息
	 * @param userRelObject
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userOrg/getMainOrgByDemCode",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户账号（或工号）、维度，获取用户所属主组织信息", httpMethod = "POST", notes = "获取用户所属主组织信息，参数对象的属性isMain与level是无效参数，该方法只获取主组织信息")
	public CommonResult<Org> getMainOrgByDemCode(@ApiParam(name = "userRelObject", value = "用户组织关系参数", required = true) @RequestBody UserRelObject userRelObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户账号（或工号）、维度，获取用户所属岗位信息
	 * @param userRelObject
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userPost/getUserPosts",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户账号（或工号）、维度，获取用户所属岗位信息", httpMethod = "POST", notes = "获取用户所属岗位信息")
	public List<OrgPost> getUserPosts(@ApiParam(name = "userRelObject", value = "用户组织关系参数", required = true) @RequestBody UserRelObject userRelObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户账号（或工号）、维度，获取用户直属上级信息
	 * @param userRelObject
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userOrgs/getImmeSuperior",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户账号（或工号）、维度、级别，获取用户直属上级信息", httpMethod = "POST", notes = "获取用户直属上级信息（直属组织中的主负责人）")
	public Set<GroupIdentity> getImmeSuperior(@ApiParam(name = "userRelObject", value = "用户组织关系参数", required = true) @RequestBody UserRelObject userRelObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 根据用户账号（或工号）、维度，获取用户直属下级信息
	 * @param userRelObject
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userOrgs/getImmeUnders",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户账号（或工号）、维度、级别，获取用户直属下级信息", httpMethod = "POST", notes = "获取用户直属下级（直属组织中的用户组织关系）信息（获取规则：用户所在组织，1、组织中有责任岗位，用户在责任岗位中；2、组织中没有责任岗位，该用户为（主）负责人）")
	public Set<GroupIdentity> getImmeUnders(@ApiParam(name = "userRelObject", value = "用户组织关系参数", required = true) @RequestBody UserRelObject userRelObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户账号（或工号）获取职务信息
	 * @param userMark
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userJob/getUserJobs",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户账号（或工号）获取用户职务信息", httpMethod = "GET", notes = "获取用户职务信息，参数 （任传一个）{\"account\":\"用户账号\",\"userNumber\":\"用户工号\"}")
	public List<OrgJob> getUserJobs(@ApiParam(name="account",value="用户账号") @RequestParam(required=false) String account,
			@ApiParam(name="userNumber",value="用户工号") @RequestParam(required=false) String userNumber) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户账号（或工号）获取群组信息
	 * @param userMark
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroup/getUserGroups",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户账号（或工号）获取用户群组信息", httpMethod = "GET", notes = "获取用户群组信息，参数 （任传一个）{\"account\":\"用户账号\",\"userNumber\":\"用户工号\"}")
	public List<UserGroup> getUserGroups(@ApiParam(name="account",value="用户账号") @RequestParam(required=false) String account,
			@ApiParam(name="userNumber",value="用户工号") @RequestParam(required=false) String userNumber) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 定时获取AD新增人员信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getNewUsersFromAD",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "定时获取AD新增人员信息", httpMethod = "GET", notes = "定时获取AD新增人员信息（获取传入时间及之后从AD域同步过来的人员列表）")
	public List<UserVo> getNewUsersFromAD(@ApiParam(name="date",value="AD同步时间（如：2018-01-01 12:00:00或2018-01-01）",required=true) @RequestParam String date) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * AD域同步
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/syncADUsers",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "AD域同步", httpMethod = "GET", notes = "定时AD域同步")
	public CommonResult<String> syncADUsers(@ApiParam(name = "action", value = "同步类型：“all”为全量，其他字符为增量", required = true) @RequestParam String action,HttpServletRequest request) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}

	/**
	 * 根据角色编码、组织编码获取对应人员
	 * @param roleCode
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getByRoleCodeAndOrgCode",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据角色编码、组织编码获取对应人员", httpMethod = "GET", notes = "根据角色编码、组织编码获取对应人员")
	public Set<GroupIdentity> getByRoleCodeAndOrgCode(@ApiParam(name="roleCode",value="角色编码",required=true)  @RequestParam String roleCode,
			@ApiParam(name="orgCode",value="组织编码",required=true)   @RequestParam String orgCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据职务编码、组织编码获取对应人员
	 * @param jobCode
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getByJobCodeAndOrgCode",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据职务编码、组织编码获取对应人员", httpMethod = "GET", notes = "根据职务编码、组织编码获取对应人员")
	public Set<GroupIdentity> getByJobCodeAndOrgCode(@ApiParam(name="jobCode",value="职务编码",required=true)  @RequestParam String jobCode,
			@ApiParam(name="orgCode",value="组织编码",required=true)   @RequestParam String orgCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据岗位编码、组织编码获取对应人员
	 * @param postCode
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getByPostCodeAndOrgCode",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据岗位编码、组织编码获取对应人员", httpMethod = "GET", notes = "根据岗位编码、组织编码获取对应人员")
	public Set<GroupIdentity> getByPostCodeAndOrgCode(@ApiParam(name="postCode",value="岗位编码",required=true)  @RequestParam String postCode,
			@ApiParam(name="orgCode",value="组织编码",required=true)   @RequestParam String orgCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 *获取指定用户的所有上级（下属管理）
	 * @param postCode
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUpUsersByUser",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定用户的所有上级（下属管理）", httpMethod = "GET", notes = "获取指定用户的所有上级（下属管理）")
	public List<UserVo> getUpUsersByUser(@ApiParam(name="account",value="账号",required=true)  @RequestParam String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 *获取指定用户的所属上级（下属管理）
	 * @param postCode
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUpUserByUserAndOrg",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定用户在指定组织中的上级（下属管理）", httpMethod = "GET", notes = "获取指定用户在指定组织中的上级（下属管理）")
	public UserVo getUpUserByUserAndOrg(@ApiParam(name="account",value="账号",required=true)  @RequestParam String account,
			@ApiParam(name="orgCode",value="组织编码",required=true)   @RequestParam String orgCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 获取指定用户的所有下级（下属管理）
	 * @param postCode
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUnderUsersByUser",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定用户的所有下级（下属管理）", httpMethod = "GET", notes = "获取指定用户的所有下级（下属管理）")
	public List<UserVo> getUnderUsersByUser(@ApiParam(name="account",value="账号",required=true)  @RequestParam String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取指定用户在指定组织中的下级（下属管理）
	 * @param postCode
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUnderUserByUserAndOrg",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定用户在指定组织中的下级（下属管理）", httpMethod = "GET", notes = "获取指定用户在指定组织中的下级（下属管理）")
	public List<UserVo> getUnderUserByUserAndOrg(@ApiParam(name="account",value="账号",required=true)  @RequestParam String account,
			@ApiParam(name="orgCode",value="组织编码",required=true)   @RequestParam String orgCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的用户数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/deleteUserPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的用户数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的用户数据")
	public CommonResult<Integer> deleteUserPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 禁用用户
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/forbiddenUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户标识禁用用户（多个用,号隔开）", httpMethod = "POST", notes = "根据用户标识禁用用户（多个用,号隔开）参数 （任传一个）")
	public CommonResult<String> forbiddenUser(@ApiParam(name="userMark",value="用户标识") @RequestBody(required=false) UserMarkObject userMark) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 激活用户
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/activateUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户标识激活用户（多个用,号隔开）", httpMethod = "POST", notes = "根据用户标识激活用户（多个用,号隔开）参数 （任传一个）")
	public CommonResult<String> activateUser(@ApiParam(name="userMark",value="用户标识") @RequestBody(required=false) UserMarkObject userMark) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 用户离职
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/leaveUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户标识用户离职（多个用,号隔开）", httpMethod = "POST", notes = "根据用户标识用户离职（多个用,号隔开）参数 （任传一个）")
	public CommonResult<String> leaveUser(@ApiParam(name="userMark",value="用户标识") @RequestBody(required=false) UserMarkObject userMark) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 通过岗位编码获取用户
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUserByPost",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "通过岗位编码获取用户", httpMethod = "GET", notes = "通过岗位编码获取用户")
	public List<UserVo> getUserByPost(@ApiParam(name="postCode",value="岗位编码",required=true)   @RequestParam String postCode) throws Exception{
	//	return userService.getUserByPost(postCode);
		return MockUCDataUtil.getUserByPost(postCode);
	}
	
	@RequestMapping(value="users/exportUsers",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "导出用户组织数据", httpMethod = "POST", notes = "导出用户组织数据（可包含用户、机构、维度、组织、角色数据）")
	public void exportUsers(@ApiParam(name="isOrg",value="是否导出组织相关数据（包括维度、组织、职务、岗位已经之间的关系表数据）。默认为true",required=true)   @RequestParam Boolean isOrg,
			@ApiParam(name="isRole",value="是否导出角色以及用户角色关系数据。默认为true",required=true)   @RequestParam Boolean isRole,
			@ApiParam(name="isAll",value="是否导出查询条件的全部数据（false：只导出当前页）。默认为true",required=true)   @RequestParam Boolean isAll,
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 导入用户
	 * @param user
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/importZipUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "导入用户组织关系", httpMethod = "POST", notes = "导入用户组织关系")
	public CommonResult<String> importZipUser(@ApiParam(name="isNewCode",value="是否生成新编码。当编码在系统中已存在且对应名称不同时：true,编码加后缀生成新的编码导入；false：不导入数据及相关连数据。默认为true",required=true)   @RequestParam boolean isNewCode,
			@ApiParam(name="isCover",value="是否覆盖更新。如果编码和名称一样，则默认为同一条数据：true，将已导入数据为准，更新其他字段，false，不更新除关联字段以外的字段。默认为true",required=true)   @RequestParam boolean isCover,
			@ApiParam(name="isOrg",value="是否导入组织相关数据（包括维度、组织、职务、岗位已经之间的关系表数据）。默认为true",required=true)   @RequestParam boolean isOrg,
			@ApiParam(name="isRole",value="是否导入角色以及用户角色关系数据。默认为true",required=true)   @RequestParam boolean isRole,@ApiParam(name="file",value="导入的zip文件",required=true)   @RequestBody MultipartFile file) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	} 
	
	/**
	 * 根据时间获取用户数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUserByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取用户数据（数据同步）", httpMethod = "POST", notes = "根据时间获取用户数据（数据同步）")
	public List<User> getUserByTime(@ApiParam(name="userExport",value="获取用户参数",required=true) @RequestBody UserExportObject userExport) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据时间获取用户参数数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUserParamByTime",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取用户参数数据（数据同步）", httpMethod = "GET", notes = "根据时间获取用户参数数据（数据同步）")
	public List<UserParams> getUserParamByTime(@ApiParam(name="btime",value="开始时间（格式：2018-01-01 12:00:00或2018-01-01）") @RequestParam(required=false) String btime,@ApiParam(name="etime",value="结束时间（格式：2018-02-01 12:00:00或2018-02-01）") @RequestParam(required=false) String etime) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据时间获取用户角色关系数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRoles/getUserRoleByTime",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取用户角色关系数据（数据同步）", httpMethod = "GET", notes = "根据时间获取用户角色关系数据（数据同步）")
	public List<UserRole> getUserRoleByTime(@ApiParam(name="btime",value="开始时间（格式：2018-01-01 12:00:00或2018-01-01）") @RequestParam(required=false) String btime,@ApiParam(name="etime",value="结束时间（格式：2018-02-01 12:00:00或2018-02-01）") @RequestParam(required=false) String etime) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据时间获取用户角色关系数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRoles/userRolePage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户的角色列表", httpMethod = "POST", notes = "获取用户的角色列表")
	public List<UserRole> userRolePage(@ApiParam(name="queryFilter",value="通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 获取同步副本数据集合
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dataSync/getSyncDataByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取同步副本数据集合", httpMethod = "POST", notes = "获取同步副本数据集合（未填写开始和结束时间时为全量）")
	public DataSyncVo getSyncDataByTime(@ApiParam(name="dataSync",value="副本数据同步获取参数类") @RequestBody(required=true) DataSyncObject dataSync) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取用户汇报关系图信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getUserRelCharts",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户标识获取用户汇报关系图信息", httpMethod = "GET", notes = "获取用户汇报关系图信息，参数 （账号、工号任传一个）")
	public ChartOption getUserRelCharts(@ApiParam(name="account",value="用户账号") @RequestParam(required=false) String account,
			@ApiParam(name="userNumber",value="用户工号") @RequestParam(required=false) String userNumber) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="user/setStatus",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量设置用户状态", httpMethod = "POST", notes = "批量设置用户状态")
	public CommonResult<String> setStatus(@ApiParam(name="userStatusVo",value="用户状态变更请求参数") 
										  @RequestBody(required=true) UserStatusVo userStatusVo) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="user/isAccountExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询账号是否已存在", httpMethod = "GET", notes = "查询账号是否已存在")
	public CommonResult<Boolean> isAccountExist(@ApiParam(name="account",value="账号")
												@RequestParam(required=true) String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="user/isUserNumberExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询工号是否已存在", httpMethod = "GET", notes = "查询工号是否已存在")
	public CommonResult<Boolean> isUserNumberExist(@ApiParam(name="account",value="账号")
												   @RequestParam(required=true) String account,
												   @ApiParam(name="userNumber",value="工号")
												   @RequestParam(required=true) String userNumber) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	private PageList<UserVo> convertVoPageList(PageList<User> pageList){
		PageList<UserVo> voPageList = new PageList<UserVo>();
		 voPageList.setPage(pageList.getPage());
		 voPageList.setPageSize(pageList.getPageSize());
		 voPageList.setTotal(pageList.getTotal());
		 voPageList.setRows(OrgUtil.convertToUserVoList(pageList.getRows()));
		 return voPageList;
	 }
	
	private String getJsonString(String account,String userNumber) throws IOException{
		if(StringUtil.isEmpty(account)&&StringUtil.isEmpty(userNumber)){
			throw new RequiredException("帐号和工号必须填写其中一个，不能同时为空！");
		}
		ObjectNode json = JsonUtil.getMapper().createObjectNode();
		json.put("account", account);
		json.put("userNumber", userNumber);
		return JsonUtil.toJson(json);
	}
	
	/**
	 * 是否显示AD增量同步按钮
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/showADButton",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "用于是否显示AD增量同步按钮", httpMethod = "GET", notes = "用于是否显示AD增量同步按钮")
	public boolean showADButton() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 修改个人密码
	 * @param userPwdObject
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/updateOneselfPsw",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "修改个人密码", httpMethod = "POST", notes = "用户修改个人密码）")
	public CommonResult<String> updateOneselfPsw(@ApiParam(name="userPwdObject",value="用户密码相关参数",required=true) @RequestBody UserPwdObject userPwdObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="user/uploadPortrait",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "上传头像", httpMethod = "POST", notes = "上传头像")
	public CommonResult<String> uploadPortrait(@ApiParam(name="account",value="用户账号") 
											   @RequestParam(required=false) String account,
											   @ApiParam(name="file",value="上传的头像", required=true) 
											   @RequestParam MultipartFile file) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "user/portrait/{account}/{filename:.+}", method = RequestMethod.GET)
	@ApiOperation(value = "下载头像", httpMethod = "GET", notes = "下载头像")
	public ResponseEntity<?> getFile(@PathVariable String account, @PathVariable String filename) {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取所有组织人员（带分页信息）
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getAllOrgUsers",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有组织人员（带分页信息）", httpMethod = "POST", notes = "获取所有组织人员")
	public PageList<UserVo> getAllOrgUsers(@ApiParam(name="queryFilter",value="通用查询对象")
	 @RequestBody QueryFilter queryFilter) throws Exception{
//    	Page<User> query = (Page<User>)userService.getDemUserQuery(queryFilter);
    	Page<User> query = MockUCDataUtil.getUserByOrgId(queryFilter);
    	return convertVoPageList(new PageList<User>(query));
	}
	
	@RequestMapping(value="user/setTrigger",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "修改计划定时任务执行时间", httpMethod = "POST", notes = "修改计划定时任务执行时间")
	public CommonResult<String> setTrigger(@ApiParam(name="triggerVo",value="计划定时任务vo类") 
										  @RequestBody(required=true) TriggerVo triggerVo,HttpServletRequest request) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取用户信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getUserById",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户id获取用户信息", httpMethod = "GET", notes = "根据用户id获取用户信息")
	public CommonResult<UserVo> getUserById(@ApiParam(name="userId",value="用户ID") @RequestParam(required=false) String userId) throws Exception{
		//return userService.getUserById(userId);
		User user = MockUCDataUtil.getUserByUserId(userId);
		return new CommonResult<UserVo>(true, "根据用户id获取用户信息成功", new UserVo(user));
	}
	
	/**
	 * 根据email查询用户信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getUserByEmail",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据email查询用户信息", httpMethod = "GET", notes = "根据email查询用户信息")
	public List<User> getUserByEmail(@ApiParam(name="email",value="用户账号") @RequestParam(required=true) String email
			 ) throws Exception{
//		return userService.getByUserEmail(email);
		return MockUCDataUtil.getByUserEmail(email);
	}
	
	@RequestMapping(value = "users/getCharges", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户主组织", httpMethod = "GET", notes = "获取用户主组织")
	public List<User> getCharges(
		@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
		@ApiParam(name = "isMain", value = "是否主负责人", required = true) @RequestParam Boolean isMain,
		@ApiParam(name = "isP", value = "是否上级部门", required = true) @RequestParam Boolean isP
		)throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "user/isAdmin", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取当前用户是否超级管理员", httpMethod = "GET", notes = "获取用户主组织")
	public CommonResult<Boolean> isAdmin()throws Exception {
		User user = ContextUtil.getCurrentUser();
		return new CommonResult<Boolean>(true,"获取成功！",user.isAdmin());
	}
	
	@RequestMapping(value = "users/getSuperFromUnder", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "通过组织中的下属设置获取上级人员", httpMethod = "GET", notes = "通过组织中的下属设置获取上级人员")
	public List<User> getSuperFromUnder(
		@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
		@ApiParam(name = "orgId", value = "组织id或编码", required = false) @RequestParam(required=false) String orgId,
		@ApiParam(name = "demId", value = "维度id或编码", required = false) @RequestParam(required=false) String demId
		)throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "user/getUserMsg", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取app用户信息", httpMethod = "GET", notes = "获取app用户信息")
	public ResponseEntity<?> getUserMsg(
			@ApiParam(name = "account", value = "用户别名", required = false) @RequestParam String account
		)throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "user/getOrgMsg", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取app用户组织信息", httpMethod = "GET", notes = "获取app用户组织信息")
	public ResponseEntity<?> getOrgMsg(@ApiParam(name = "orgCode", value = "组织别名", required = false) @RequestParam String orgCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "user/getUnderUsers", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取app用户的所有下属", httpMethod = "GET", notes = "获取app用户的所有下属")
	public ResponseEntity<?> getUnderUsers(@ApiParam(name = "userId", value = "用户id", required = false) @RequestParam String userId) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "user/updateUserMessage", method = RequestMethod.POST, produces = {"application/json; charset=utf-8" })
	@ApiOperation(value = "修改用户资料参数", httpMethod = "POST", notes = "修改用户资料参数")
	public Map<String,Object> updateUserMessage(@ApiParam(name = "params", value = "修改用户资料参数", required = false) @RequestBody Map<String,Object> params) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="users/postUserByIds",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据多个用户id获取用户信息(以逗号隔开)", httpMethod = "POST", notes = "根据多个用户id获取用户信息(以逗号隔开)")
	public List<UserVo> postUserByIds(@ApiParam(name="ids",value="用户id") @RequestBody(required=true) String ids) throws Exception{
		if (StringUtil.isNotEmpty(ids)) {
			/*String[] split = ids.split(",");
			QueryFilter queryFilter = QueryFilter.build();
			queryFilter.addFilter("id_", split, QueryOP.IN,FieldRelation.OR);
			queryFilter.addFilter("account_", split, QueryOP.IN,FieldRelation.OR);
			List<UserVo> users = userService.queryUser(queryFilter);*/
			
			List<UserVo> users = MockUCDataUtil.getUserByUserIds(ids);
			return users;
		}
		return new ArrayList<UserVo>();
	}

    @RequestMapping(value="users/getUserByAccount",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "根据用户账号获取用户信息并修改用户微信字段信息", httpMethod = "GET", notes = "根据用户账号获取用户信息并修改用户微信字段信息")
    public CommonResult<String> postUserByAccount(@ApiParam(name="account",value="用户账号") @RequestParam String account,
                                                  @ApiParam(name="openid",value="用户openid") @RequestParam String openid) throws Exception{
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }
	
	@RequestMapping(value = "user/getUserDetailed", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取app用户的所有下属", httpMethod = "GET", notes = "按用户id查询所属组织")
	public Map<String,Object> getUserDetailed(@ApiParam(name = "userId", value = "用户id", required = false) @RequestParam String userId) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户id获取用户所在部门负责人
	 * @param 用户账号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getDepHeader",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户id获取用户所在部门负责人", httpMethod = "GET", notes = "根据用户id获取用户所在部门负责人（不传为当前人）")
	public List<UserVo>  getDepHeader(@ApiParam(name = "userId", value = "用户id", required = false) @RequestParam Optional<String> userId,
			                          @ApiParam(name = "isMain", value = "是否只取主负责人", required = false) @RequestParam Optional<Boolean> isMain) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据部门id获取部门负责人
	 * @param 部门id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getDepHeaderByOrg",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据部门id获取部门负责人", httpMethod = "GET", notes = "根据部门id获取部门负责人")
	public List<UserVo>  getDepHeaderByOrg(@ApiParam(name = "orgId", value = "部门id", required = false) @RequestParam String orgId,
			                               @ApiParam(name = "isMain", value = "是否只取主负责人", required = false) @RequestParam Optional<Boolean> isMain) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据用户id和用户组类型获取其相关用户组
	 * @param 部门id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="user/getGroupsByUidAndType",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户id和用户组类型获取其相关用户组", httpMethod = "GET", notes = "根据用户id和用户组类型获取其相关用户组")
	public List<Group>  getGroupsByUidAndType(@ApiParam(name = "userId", value = "用户id", required = false) @RequestParam String userId,
			                                @ApiParam(name = "type", value = "类型", required = false) @RequestParam String type) throws Exception{
//		return userService.getGroupsByUserId(userId , type);
		return MockUCDataUtil.getGroupsByUserId(userId,type);
	}
	
	/**
	 * 查询用户组织关系
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="user/queryOrgUserRel",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户id和用户组类型获取其相关用户组", httpMethod = "POST", notes = "根据用户id和用户组类型获取其相关用户组")
	public List queryOrgUserRel(@ApiParam(name="queryFilter",value="通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	@RequestMapping(value="user/getDetailByAccountOrId",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户账号获取用户信息", httpMethod = "POST", notes = "根据用户账号获取用户信息")
	public Map<String, Object> getDetailByAccountOrId(@ApiParam(name="account",value="用户账号") @RequestParam(required=true) String account) throws Exception{
		//return userService.getUserDetailByAccountOrId(account);
		return MockUCDataUtil.getUserDetailByAccountOrId(account);
	}
	
	/**
	 * 模糊查询获取用户列表
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/getUserByNameaAndEmal",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "查询条件", httpMethod = "GET", notes = "模糊查询获取用户列表")
	public List<User> getUserByNameaAndEmal(@ApiParam(name="key",value="查询条件", required = false)  @RequestParam String query) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据节点处理人对象抽取处理人
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="users/calculateNodeUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据节点处理人对象抽取处理人", httpMethod = "POST", notes = "根据节点处理人对象抽取处理人")
	public Map<String, Object> calculateNodeUser(@ApiParam(name="nodeMap",value="通用查询对象")
	 @RequestBody Map<String, Object> nodeMap) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
    
    @RequestMapping(value = "user/getUserByOpenId", method = RequestMethod.GET, produces = {"application/json; charset=utf-8" })
    @ApiOperation(value = "根据微信公众号openId获取用户信息", httpMethod = "GET", notes = "根据微信公众号openId获取用户信息")
    public CommonResult<UserVo> getUserByOpenId(@ApiParam(name="openId",value="公众号openId", required = false)  @RequestParam String openId) throws Exception {
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }


    
    @RequestMapping(value = "user/getUserRightMapByIds", method = RequestMethod.GET, produces = {"application/json; charset=utf-8" })
    @ApiOperation(value = "根据传入的用户id集合，获取用户的权限集合", httpMethod = "GET", notes = "根据传入的用户id集合，获取用户的权限集合")
    public Map<String, Map<String, String>> getUserRightMapByIds(@ApiParam(name="ids",value="用户id集合", required = true)  @RequestParam Set<String> ids) throws Exception {
    	//return userService.getUserRightMapByIds(ids);
    	return MockUCDataUtil.getUserRightMapByIds(ids);
    }

}
