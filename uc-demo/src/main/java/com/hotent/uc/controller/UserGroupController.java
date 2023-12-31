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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.exception.BaseException;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserGroup;
import com.hotent.uc.params.userGroup.UserGroupVo;

/**
 * 群组管理接口
 * @author liangqf
 *
 */
@RestController
@RequestMapping("/api/userGroup/v1/")
@Api(tags="UserGroupController")
public class UserGroupController extends BaseController {
	
	/**
	 * 获取群组列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroups/getUserGroupPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取群组列表（带分页信息）", httpMethod = "POST", notes = "获取群组列表")
	public PageList<UserGroup> getUserGroupPage(@ApiParam(name="filter",value="查询对象")
	 @RequestBody QueryFilter filter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取所有群组
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroups/getUserGroupList",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有群组", httpMethod = "GET", notes = "获取所有群组")
	public List<UserGroup> getUserGroupList() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 添加群组
	 * @param userGroupVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroup/addUserGroup",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加群组", httpMethod = "POST", notes = "添加群组")
	public CommonResult<String> addUserGroup(@ApiParam(name="userGroupVo",value="群组对象",required=true) @RequestBody UserGroupVo userGroupVo) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 更新群组
	 * @param userGroupVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroup/updateUserGroup",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新群组", httpMethod = "PUT", notes = "更新群组")
	public CommonResult<String> updateUserGroup(@ApiParam(name="userGroupVo",value="群组对象",required=true) @RequestBody UserGroupVo userGroupVo) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取指定群组
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroup/getUserGroup",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定群组", httpMethod = "GET", notes = "获取指定群组")
	public UserGroup getUserGroup(@ApiParam(name = "code", value = "群组编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 删除群组
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroup/delUserGroup",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "删除群组", httpMethod = "POST", notes = "删除群组")
	public CommonResult<String> delUserGroup(@ApiParam(name="codes",value="群组代码（多个用,号隔开）",required=true) @RequestBody String codes) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 群组添加用户组
	 * @param code
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userGroup/addGroupUsers", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "群组添加用户组", httpMethod = "POST", notes = "群组添加用户组，json为[{\"type\":\"user\",\"codes\":\"admin\"},...]，其中type可填user、org、pos、role四种类型，分别代表用户、组织、岗位、角色，codes代表它们的代码，用户的填写account信息，多个用户英文逗号隔开")
	public CommonResult<String> addGroupUsers(
			@ApiParam(name = "code", value = "群组代码", required = true) @RequestParam String code,
			@ApiParam(name = "json", value = "用户组信息", required = true) @RequestBody List<ObjectNode> json)
			throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取指定群组下人员信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroup/getGroupUsers",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定群组下人员信息", httpMethod = "GET", notes = "获取指定群组下人员信息")
	public List<User> getGroupUsers(@ApiParam(name = "code", value = "群组代码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的用户组数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroup/deleteGroupPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的用户组数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的用户组数据")
	public CommonResult<Integer> deleteGroupPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据时间获取群组数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroups/getUserGroupByTime",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取群组数据（数据同步）", httpMethod = "GET", notes = "根据时间获取群组数据（数据同步）")
	public List<UserGroup> getUserGroupByTime(@ApiParam(name="btime",value="开始时间（格式：2018-01-01 12:00:00或2018-01-01）") @RequestParam(required=false) String btime,@ApiParam(name="etime",value="结束时间（格式：2018-02-01 12:00:00或2018-02-01）") @RequestParam(required=false) String etime) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取指定群组下人员信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroups/getGroupUsersPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定群组下人员分页信息", httpMethod = "POST", notes = "获取指定群组下人员分页信息")
	public PageList<User> getGroupUsersPage(@ApiParam(name = "code", value = "群组代码", required = true) @RequestParam String code,@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}

	@RequestMapping(value="userGroup/isCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询群组编码是否已存在", httpMethod = "GET", notes = "查询群组编码是否已存在")
	public CommonResult<Boolean> isCodeExist(@ApiParam(name="code",value="群组编码")
												@RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 更新群组管理员
	 * @param code
	 * @param account
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userGroup/updateGroupAuth",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新群组管理员", httpMethod = "PUT", notes = "更新群组管理员")
	public CommonResult<String> updateGroupAuth(@ApiParam(name="code",value="群组编码")@RequestParam(required=true) String code,
			@ApiParam(name="account",value="用户账号")@RequestParam(required=true) String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
}
