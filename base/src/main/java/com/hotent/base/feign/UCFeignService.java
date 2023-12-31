package com.hotent.base.feign;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.conf.FeignConfig;
import com.hotent.base.feign.impl.UCFeignServiceImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.api.model.Group;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author liyg
 *
 */
@FeignClient(name="uc-eureka",fallback=UCFeignServiceImpl.class, configuration=FeignConfig.class)
public interface UCFeignService {

	/**
	 * 根据用户账号获取用户信息
	 * @param account
	 * @return
	 */
	@RequestMapping(value="/api/user/v1/user/loadUserByUsername",method=RequestMethod.POST)
	public JsonNode loadUserByUsername(@RequestParam(value="account",required=true) String account);

	@RequestMapping(value="/api/user/v1/users/getAllUser",method=RequestMethod.GET)
	public JsonNode getAllUser();

	/**
	 * 根据用户id获取用户信息
	 */
	@RequestMapping(value="/api/user/v1/user/getUserById",method=RequestMethod.GET)
	public CommonResult<JsonNode> getUserById( @RequestParam(value="userId",required=true) String userId);

	/**
	 * 获取用户信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/user/v1/user/getUser",method=RequestMethod.GET)
	public JsonNode getUser(@RequestParam(value="account",required=true) String account, @RequestParam(value="userNumber",required=false) String userNumber);

	/**
	 * 根据多个用户账号获取用户信息(以逗号隔开)
	 */
	@RequestMapping(value="/api/user/v1/user/getUserByAccounts",method=RequestMethod.GET)
	public ArrayNode getUserByAccounts(@RequestParam(value="accounts",required=true) String accounts);  

	@RequestMapping(value="/api/user/v1/users/postUserByAccount",method=RequestMethod.GET)
	public CommonResult<String> postUserByAccount(@RequestParam(value="account",required=true) String accounts,@RequestParam(value="openid",required=true) String openid);

	/**
	 * 获取所有组织人员（带分页信息）
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/user/v1/users/getAllOrgUsers",method=RequestMethod.POST)
	public ObjectNode getAllOrgUsers(@RequestBody QueryFilter queryFilter);


	/**
	 * 获取角色（多个）中的用户
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/role/v1/role/getUsersByRoleCode",method=RequestMethod.POST)
	public ArrayNode getUsersByRoleCode( @RequestBody String codes);

	/**
	 * 通过岗位编码获取用户
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/user/v1/users/getUserByPost",method=RequestMethod.GET)
	public ArrayNode getUserByPost( @RequestParam(value="postCode",required=true) String postCode);

	/**
	 * 获取职务（多个）下的所有人员
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/job/v1/jobUser/getUsersByJob",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取职务（多个）下的所有人员", httpMethod = "GET", notes = "获取职务下的所有人员")
	public ArrayNode getUsersByJob(@RequestParam(value="codes",required=true) String codes);

	/**
	 * 根据email查询用户信息
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/api/user/v1/user/getUserByEmail",method=RequestMethod.GET)
	public ArrayNode getUserByEmail(@RequestParam(value="email",required=true) String email);


	/** List<OrgUser> orgUsers = orgUserManager.getChargesByOrgId(id,isMain);
	 *根据组织ID获取组织的负责人组织关系
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/orgUsers/getChargesByOrgId",method=RequestMethod.GET)
	public List<ObjectNode> getChargesByOrgId(@RequestParam(value="orgId",required=true) String orgId,@RequestParam(value="isMain",required=true)boolean isMain);

	/** Org org =  orgManager.get(orgId);
	 * 根据组织id获取组织
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/org/get",method=RequestMethod.GET)
	public ObjectNode getOrgByIdOrCode(@RequestParam(value="id",required=true) String orgId);

	/**
	 * 根据角色id或编码获取角色
	 * @return
	 */
	@RequestMapping(value="/api/role/v1/role/getRole",method=RequestMethod.GET)
	public CommonResult<ObjectNode> getRoleByIdOrCode(@RequestParam(value="code",required=true) String code);

	/**
	 * 根据岗位id或编码获取岗位
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/orgPost/getOrgPost",method=RequestMethod.GET)
	public CommonResult<ObjectNode> getPostByIdOrCode(@RequestParam(value="postCode",required=true) String code);

	/**
	 * 根据职务id或编码获取职务(返回CommonResult<?>)
	 * @return
	 */
	@RequestMapping(value="/api/job/v1/job/getOrgJob",method=RequestMethod.GET)
	public CommonResult<ObjectNode> getJobByOrgCode(@RequestParam(value="code",required=true) String code);

	/**
	 * 根据职务id或编码获取职务
	 * @return
	 */
	@RequestMapping(value="/api/job/v1/job/getJob",method=RequestMethod.GET)
	public ObjectNode getJobByIdOrCode(@RequestParam(value="code",required=true) String code);

	/**
	 * OrgUser orgUser = orgUserManager.getOrgUserMaster(ContextUtil.getCurrentUserId());
	 * 获取当前用户的主部门
	 */
	@RequestMapping(value="/api/org/v1/org/getSupOrgByCurrMain",method=RequestMethod.GET)
	public ObjectNode getOrgUserMaster(@RequestParam(value="userId",required=true)String userId,@RequestParam(value="demId",required=true)String demId);

	/**
	 * 根据id或别名获取系统分类
	 *
	 */
	 @RequestMapping(value="/sys/sysType/v1/getJson",method=RequestMethod.GET)
	public ObjectNode getSysTypeByIdOrKey(@RequestParam(value="userId",required=true)String id);

	/**
	 * List<SysUserRel> sysUserRels = sysUserRelManager.getSuperUser(userId, level, sysType.get("id").asText());
	 * 根據分類編碼和用戶賬號獲取匯報線
	 */

	@RequestMapping(value="/api/userRel/v1/userRel/getSuperUser",method=RequestMethod.POST)
	public List<ObjectNode> getSuperUser(@RequestBody(required=true)ObjectNode obj);

	/**
	 * List<Role> listRole= roleManager.getAll();
	 */
	@RequestMapping(value="/api/role/v1/roles/getAll",method=RequestMethod.GET)
	public List<ObjectNode> getAllRole();

	/**
	 * List<Role> listRole = roleManager.getListByUserId(userId);
	 * 根據用戶賬號獲取角色
	 */
	@RequestMapping(value="/api/role/v1/role/getRolesByUser",method=RequestMethod.GET)
	public List<ObjectNode> getRoleListByAccount(@RequestParam(value="account",required=true)String account);

	/**
	 * 獲取用戶默認維度下的主組織
	 * Org org = orgManager.getMainGroup(ContextUtil.getCurrentUser().getUserId());
	 */
	@RequestMapping(value="/api/org/v1/org/getMainGroup",method=RequestMethod.GET)
	public ObjectNode getMainGroup(@RequestParam(value="userId",required=true)String userId);

	/**
	 * List<OrgRel> orgRels = orgRelManager.getListByUserId(ContextUtil.getCurrentUser().getUserId());
	 * 獲取當前用戶所有崗位
	 */
	@RequestMapping(value="/api/org/v1/orgPost/getOrgPostByUserAccount",method=RequestMethod.GET)
	public List<ObjectNode> getPosListByAccount(@RequestParam(value="account",required=true)String account);


	/**
	 * 获取当前用户的组织布局管理权限
	 */
	@RequestMapping(value="/api/orgAuth/v1/orgAuths/getCurrentUserAuthOrgLayout",method=RequestMethod.GET)
	public ArrayNode getCurrentUserAuthOrgLayout(@RequestParam(value="userId", required=true) String userId);

	/**
	 * 获取用户所属组织
	 */
	@RequestMapping(value = "/api/org/v1/orgs/getOrgListByUserId", method = RequestMethod.GET)
	public ArrayNode getOrgListByUserId(@RequestParam(value="userId",required=true) String userId);


	/**
	 * 根据职务编码、组织编码获取对应人员
	 * @param redDefCode
	 * @param orgCode
	 * @return
	 */
	@RequestMapping(value = "/api/user/v1/users/getByJobCodeAndOrgCode", method = RequestMethod.GET)
	public List<ObjectNode> getByOrgRelDefCode(@RequestParam(value="jobCode", required=true)String jobCode,@RequestParam(value="orgCode", required=true) String orgCode);

	/**
	 * 根据岗位编码、组织编码获取对应人员
	 * @param redDefCode
	 * @param orgCode
	 * @return
	 */
	@RequestMapping(value = "/api/user/v1/users/getByPostCodeAndOrgCode", method = RequestMethod.GET)
	public List<ObjectNode> getByOrgRelCode(@RequestParam(value="postCode", required=true)String postCode, @RequestParam(value="orgCode", required=true)String orgCode);

	/**
	 * 获取用户主组织
	 */
	@RequestMapping(value = "/api/org/v1/org/getMainGroup", method = RequestMethod.GET)
	public ObjectNode getMainGroup(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="demId",required=true) String demId);

	/**
	 * 获取组织（主）负责人
	 * @param userId 用户id
	 * @param isMain 是否主组织
	 * @param isCurrent 是否上级
	 * @return
	 */
	@RequestMapping(value = "/api/user/v1/users/getCharges", method = RequestMethod.GET)
	public JsonNode getCharges(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="isMain") Boolean isMain,@RequestParam(value="isP") Boolean isP);

	/**
	 * 获取用户所有职务
	 */
	@RequestMapping(value = "/api/org/v1/orgJobs/getJobsByUserId", method = RequestMethod.GET)
	public JsonNode getJobsByUserId(@RequestParam(value="userId",required=true) String userId);

	/**
	 * 判断当前用户主部门是否有上级
	 */
	@RequestMapping(value = "/api/org/v1/org/isSupOrgByCurrMain", method = RequestMethod.GET)
	public boolean isSupOrgByCurrMain(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="demId",required=true) String demId,@RequestParam(value="level",required=true)Integer level);

	/**
	 * 通过组织中的下属设置获取上级人员
	 */
	@RequestMapping(value = "/api/user/v1/users/getSuperFromUnder", method = RequestMethod.GET)
	public ArrayNode getSuperFromUnder(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="orgId",required=false) String orgId,@RequestParam(value="demId",required=false) String demId);

	/**
	   * 获取发起人指定级别组织的负责人
	 * @param userId
	 * @param level  2
	 * @param isMainCharge true/false
	 * @return
	 */
	@RequestMapping(value = "/api/org/v1/orgusers/getCustomLevelCharge", method = RequestMethod.GET)
	public ArrayNode getCustomLevelCharge(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="level",required=true)  String level,@RequestParam(value="isMainCharge",required=true)  boolean isMainCharge);

	/**
	 * 获取发起人指定级别组织的指定岗位的用户
	 * @param userId
	 * @param level
	 * @param postCode
	 * @return
	 */
	@RequestMapping(value = "/api/org/v1/orgusers/getCustomLevelPost", method = RequestMethod.GET)
	public  Set<ObjectNode> getCustomLevelPost(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="level",required=true)  String level,@RequestParam(value="postCode",required=true)  String postCode); 

	/**
	 * 获取发起人指定级别组织的指定职务的用户
	 * @param userId
	 * @param level
	 * @param postCode
	 * @return
	 */
	@RequestMapping(value = "/api/org/v1/orgusers/getCustomLevelJob", method = RequestMethod.GET)
	public  Set<ObjectNode> getCustomLevelJob(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="level",required=true)  String level,@RequestParam(value="jobCode",required=true)  String jobCode);

	/**
	 * 获取发起人组织的指定扩展参数值
	 * @param userId
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/api/org/v1/orgusers/getStartOrgParam", method = RequestMethod.GET)
	public String getStartOrgParam(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="param",required=true)  String param);

	/**
	 * 通过用户ID和参数代码获取用户参数
	 * @param userId
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/api/params/v1/userParam/getUserParamsById", method = RequestMethod.GET)
	public ObjectNode getUserParamsById(@RequestParam(value="userId",required=true) String userId, @RequestParam(value="code",required=true) String code);

	/**
	 * 通过组织ID和参数代码获取组织参数
	 * @param orgId
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/api/params/v1/orgParam/getOrgParamsById", method = RequestMethod.GET)
	public ObjectNode getOrgParamsById(@RequestParam(value="orgId",required=true) String orgId, @RequestParam(value="code",required=true) String code);

	/**
	 * 根据id或者账号串获取用户
	 */
	@RequestMapping(value = "/api/user/v1/users/postUserByIds", method = RequestMethod.POST)
	public ArrayNode getUserByIdsOrAccounts(@RequestBody(required=false)String ids);

	/**
	 * 获取子组织
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/orgs/getByParentId",method=RequestMethod.GET)
	public List<ObjectNode> getChildOrg(@RequestParam(value="parentId",required=true) String parentId);
	
	/**
	 * 获取子组织（只获取底下一层子组织）
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/orgs/getOrgsByparentId",method=RequestMethod.GET)
	public List<ObjectNode> getOrgsByparentId(@RequestParam(value="parentId",required=true) String parentId);

	/**
	 * 根据用户id组获取主组织路径
	 */
	@RequestMapping(value = "/api/org/v1/org/getPathNames", method = RequestMethod.POST)
	public List<Map<String, String>> getPathNames(@RequestParam(value="userIds",required=true) List<String>  userIds);

	/**
	 * 根据用户id获取用户所在部门负责人
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/user/v1/user/getDepHeader",method=RequestMethod.GET)
	public ArrayNode getDepHeader(@RequestParam(value="userId",required=false) String userId,@RequestParam(value="isMain",required=true) Boolean isMain);


	/**
	 * 根据部门id获取部门负责人
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/user/v1/user/getDepHeaderByOrg",method=RequestMethod.GET)
	public ArrayNode getDepHeaderByOrg(@RequestParam(value="orgId",required=true) String orgId,@RequestParam(value="isMain",required=true) Boolean isMain);


	/**
	 * 根据用户id获取其相关用户组id
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/api/user/v1/user/getGroupsByUidAndType",method=RequestMethod.GET)
	public  List<Group>  getGroupsByUidAndType(@RequestParam(value="userId",required=true) String userId , @RequestParam(value="type",required=true) String type);


	/**
	 * 获取子组织ids
	 */
	@RequestMapping(value="/api/org/v1/org/getChildrenIds",method=RequestMethod.POST)
	public Map<String, Set<String>> getChildrenIds(@RequestParam(value="ids",required=true) Map<String,String> ids);

	/**
	 * 查询用户组织关系
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/user/v1/user/queryOrgUserRel",method=RequestMethod.POST)
	public List queryOrgUserRel(@RequestBody QueryFilter queryFilter);

	/**
	 * 模糊查询用户列表
	 */
	@RequestMapping(value="/api/user/v1/users/getUserByNameaAndEmal",method=RequestMethod.GET)
	public List<ObjectNode> getUserByNameaAndEmal(@RequestParam(value="query",required=true) String query);

	/**
	 * 结算节点人员
	 */
	@RequestMapping(value = "/api/user/v1/users/calculateNodeUser", method = RequestMethod.POST)
	public Map<String, Object> calculateNodeUser(@RequestBody(required=true) Map<String, Object> result);

	/**
	 * 根据维度获取组织
	 * @param demId
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/orgs/getOrgListByDemId",method=RequestMethod.GET)
	public List<ObjectNode> getOrgListByDemId(@RequestParam(value="demId",required=true) String demId);

	/**
	 * 根据微信公众号openId获取用户信息
	 * @param openId
	 * @return
	 */
	@RequestMapping(value="/api/user/v1/user/getUserByOpenId",method=RequestMethod.GET)
	public CommonResult<JsonNode> getUserByOpenId(@RequestParam(value="openId",required=true) String openId);


	/**
	 * 根据传入的用户id集合，获取用户的权限集合
	 */
	@RequestMapping(value = "/api/user/v1/user/getUserRightMapByIds", method = RequestMethod.GET)
	public Map<String, Map<String, String>> getUserRightMapByIds(@RequestParam(value="ids",required=true) Set<String> ids);

	/**
	 * 获取默认维度信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/demension/v1/dem/getDefaultDem",method=RequestMethod.GET)
	public ObjectNode getDefaultDem();
	
	/**
	 * 获取用户的主岗位(优先获取默认维度的主岗位、主组织，没有时获取其他维度主岗位、主组织)
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/org/getMainPostOrOrgByUserId",method=RequestMethod.GET)
	public ObjectNode getMainPostOrOrgByUserId(@RequestParam(value="userId",required=true) String userId);

	/**
	 * 获取用户的主岗位组织关系
	 * @param userId
	 * @param demId
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/org/getSupOrgByCurrMain",method=RequestMethod.GET)
	public ObjectNode getSupOrgByCurrMain(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="demId",required=true) String demId);

	/**
	 * 获取所有维度列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/demension/v1/dems/getAll",method=RequestMethod.GET)
	public List<ObjectNode> getAllDems();
	
	/**
	 * 根据用户账号获取用户组织岗位相关信息
	 * @param account
	 * @return
	 */
	@RequestMapping(value="/api/user/v1/user/getDetailByAccountOrId",method=RequestMethod.GET)
	public Map<String, Object> getDetailByAccountOrId(@RequestParam(value="account",required=true) String account);

    /**
     * 根据多个用户id获取用户信息(以逗号隔开)
     */
    @RequestMapping(value="/api/user/v1/user/getUserByIds",method=RequestMethod.GET)
    public ArrayNode getUserByIds(@RequestParam(value="ids",required=true) String ids);
    
    /**
	 * 将第三方通讯录组织框架拉取至本系统
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/org/addOrgFromExterUni",method=RequestMethod.POST)
	public CommonResult<String> addOrgFromExterUni(@RequestBody ObjectNode org);

	/**
	 * 根据手机号获取用户
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/org/getUserByMobile",method=RequestMethod.GET)
	CommonResult<JsonNode> getUserByMobile(String mobile);

	/**
	 * 将第三方通讯录成员拉取至本系统
	 * @return
	 */
	@RequestMapping(value="/api/user/v1/user/addUserFromExterUni",method=RequestMethod.POST)
	public CommonResult<String> addUserFromExterUni(@RequestBody ObjectNode newUser);

	/**
	 * 组织批量加入用户
	 * @return
	 */
	@RequestMapping(value="/api/org/v1/orgUsers/addUsersForOrg",method=RequestMethod.POST)
	public CommonResult<String> addUsersForOrg( @RequestParam(value="orgCode",required=true) String orgCode, @RequestParam(value="accounts",required=true) String accounts);
	
    /**
	 * 根据签署任务信息获取审批用户的姓名，账号， 主部门等信息
	 * @param customSignDatas
	 * @return
	 */
	@RequestMapping(value = "/api/user/v1/user/getUserInfoBySignData", method = RequestMethod.POST)
	public ArrayNode getUserInfoBySignData(@RequestBody ArrayNode customSignDatas) throws Exception;
	
	/**
	 * 根据租户id获取其被禁用菜单别名
	 * @param tenantId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/uc/tenantIgnoreMenu/v1/getIgnoreMenuCodes",method=RequestMethod.GET)
	public List<String> getIgnoreMenuCodes(@RequestParam(value="tenantId",required=true) String tenantId) throws Exception;

	/**
	 * 通过租户ID获取租户信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/uc/tenantManage/v1/getJson",method=RequestMethod.GET)
	public JsonNode getTenantById(@RequestParam(value="id", required=true) String id);
	
	/**
	 * 获取当前用户填制单位
	 * @param demId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/api/org/v1/org/getFillOrg",method=RequestMethod.GET)
	public CommonResult<Object> getFillOrg(@RequestParam(value="demId",required=false) String demId) throws Exception;
	
}
