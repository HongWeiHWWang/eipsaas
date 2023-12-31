package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.DataPermission;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.exception.BaseException;
import com.hotent.uc.mock.MockUCDataUtil;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.params.role.RoleVo;
import com.hotent.uc.params.user.UserVo;

/**
 * 角色组织模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/role/v1/")
@Api(tags="RoleController")
public class RoleController extends BaseController {
	
	
	/**
	 * 查询角色
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="roles/getRolePage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取角色列表（带分页信息）", httpMethod = "POST", notes = "获取角色列表")
	@DataPermission
	public PageList<Role> getRolePage(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
    	//PageList<Role> list = roleService.query(filter);
		PageList<Role> list = MockUCDataUtil.getRoleList();
		return list;
	}
	
	/**
	 * 获取所有角色
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="roles/getAll",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有角色", httpMethod = "POST", notes = "获取所有角色")
	public List<Role> getAll() throws Exception{
		//return roleService.getAll();
		PageList<Role> list = MockUCDataUtil.getRoleList();
		return list.getRows();
	}
	
	/**
	 * 添加角色
	 * @param Role
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="role/addRole",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加角色", httpMethod = "POST", notes = "添加角色")
	public CommonResult<String> addRole(@ApiParam(name="role",value="角色参数对象", required = true) @RequestBody RoleVo role) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据角色帐号删除角色
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/deleteRole",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据角色编码删除角色", httpMethod = "POST", notes = "根据角编码识删除角色")
	@DataPermission
	public CommonResult<String> deleteRole(@ApiParam(name="codes",value="角色编码（多个用,号隔开）", required = true) @RequestBody String codes) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据角色id删除角色
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/deleteRoleByIds",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据角色id删除角色", httpMethod = "DELETE", notes = "根据角色id删除角色")
    @DataPermission
	public CommonResult<String> deleteRoleByIds(@ApiParam(name="ids",value="角色id（多个用,号隔开）", required = true) @RequestParam String ids) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 更新角色
	 * @param Role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/updateRole",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新角色", httpMethod = "PUT", notes = "更新角色")
	@DataPermission
	public CommonResult<String> updateRole(@ApiParam(name="role",value="角色参数对象", required = true) @RequestBody  RoleVo role) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 获取角色信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/getRole",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据角色编码获取角色信息", httpMethod = "GET", notes = "获取角色信息")
	public CommonResult<Role> getRole(@ApiParam(name="code",value="角色编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 分配用户（按用户）
	 * @param Role
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="roleUser/saveUserRole",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "分配用户（按用户）", httpMethod = "POST", notes = "分配用户（按用户）")
	public CommonResult<String> saveUserRole(@ApiParam(name="code",value="角色编码", required = true) @RequestParam String code,
			@ApiParam(name="accounts",value="用户帐号，多个用“,”号隔开", required = true) @RequestParam String accounts) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口,需要改成第三方系统保存用户角色关系");
	}
	
	/**
	 * 分配用户（按用户）
	 * @param Role
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="roleUser/saveUserRoles",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "分配用户（按用户）", httpMethod = "POST", notes = "分配用户（按用户）")
	public CommonResult<String> saveUserRoles(@ApiParam(name="codes",value="角色编码，多个用“,”号隔开", required = true) @RequestParam String codes,
			@ApiParam(name="account",value="用户帐号", required = true) @RequestParam String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口,需要改成第三方系统保存用户角色关系");
	}
	
	/**
	 * 分配用户（按组织）
	 * @param Role
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="roleUser/addUserRoleByOrg",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "分配用户（按组织）", httpMethod = "POST", notes = "分配用户（按组织）")
	public CommonResult<String> addUserRoleByOrg(@ApiParam(name="code",value="角色编码", required = true) @RequestParam String code,
			@ApiParam(name="orgCodes",value="组织编码，多个用“,”号隔开", required = true) @RequestParam String orgCodes) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 角色移除用户
	 * @param Role
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="roleUser/deleteUserRole",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "角色移除用户", httpMethod = "DELETE", notes = "角色移除用户")
	public CommonResult<String> deleteUserRole(@ApiParam(name="code",value="角色编码", required = true) @RequestParam String code,
			@ApiParam(name="accounts",value="用户帐号，多个用“,”号隔开", required = true) @RequestParam String accounts) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取用户所属角色列表
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/getRolesByUser",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所属角色列表", httpMethod = "GET", notes = "获取用户所属角色列表")
	public List<Role> getRolesByUser(@ApiParam(name="account",value="用户帐号", required = true) @RequestParam String account) throws Exception{
		//return roleService.getRolesByUser(account);
		return MockUCDataUtil.getRolesByAccount(account);
	}
	
	/**
	 * 获取角色（多个）中的用户
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/getUsersByRoleCode",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取角色（多个）中的用户", httpMethod = "POST", notes = "获取角色（多个）中的用户")
	public List<UserVo> getUsersByRoleCode(@ApiParam(name="codes",value="角色编码，多个用“,”号隔开", required = true) @RequestBody String codes) throws Exception{
		// return roleService.getUsersByRoleCode(codes);
		return MockUCDataUtil.getUsersByRoleCode(codes);
	}
	
	/**
	 * 物理删除所有逻辑删除了的角色数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/deleteRolePhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的角色数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的角色数据")
	public com.hotent.base.model.CommonResult<Integer> deleteRolePhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的用户角色关系数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/deleteUserRolePhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的用户角色关系数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的用户角色关系数据")
	public com.hotent.base.model.CommonResult<Integer> deleteUserRolePhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 禁用角色（多个用,号隔开）
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/forbiddenRoles",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "禁用角色（多个用,号隔开）", httpMethod = "PUT", notes = "禁用角色（多个用,号隔开）")
	public CommonResult<String> forbiddenRoles(@ApiParam(name="codes",value="角色编码，多个用“,”号隔开", required = true) @RequestBody String codes) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 激活角色（多个用,号隔开）
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/activateRoles",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "激活角色（多个用,号隔开）", httpMethod = "PUT", notes = "激活角色（多个用,号隔开）")
	public CommonResult<String> activateRoles(@ApiParam(name="codes",value="角色编码，多个用“,”号隔开", required = true) @RequestBody String codes) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据时间获取角色数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="roles/getRoleByTime",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取角色数据（数据同步）", httpMethod = "GET", notes = "根据时间获取角色数据（数据同步）")
	public List<Role> getRoleByTime(@ApiParam(name="btime",value="开始时间（格式：2018-01-01 12:00:00或2018-01-01）") @RequestParam(required=false) String btime,@ApiParam(name="etime",value="结束时间（格式：2018-02-01 12:00:00或2018-02-01）") @RequestParam(required=false) String etime) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取角色用户（分页）
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="role/getRoleUsers",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取角色用户（分页）", httpMethod = "POST", notes = "获取角色用户（分页）",hidden=false)
	public PageList<User> getRoleUsers(@ApiParam(name="filter",value="查询参数", required = true) @RequestBody QueryFilter filter,@ApiParam(name="code",value="角色编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="role/isCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询角色编码是否已存在", httpMethod = "GET", notes = "查询角色编码是否已存在")
	public CommonResult<Boolean> isCodeExist(@ApiParam(name="code",value="角色编码")
												@RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}

	/**
	 *  根据角色别名获取除这个角色之外的所有角色
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="roles/getNotCodeAll",method=RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据角色别名获取除这个角色之外的所有角色", httpMethod = "GET", notes = "获取所有角色")
	public List<Role> getNotCodeAll(@ApiParam(name="code",value="角色编码") @RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
}
