package com.hotent.uc.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserRelManager;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRel;
import com.hotent.uc.params.user.UserRelFilterObject;
import com.hotent.uc.params.user.UserRelVo;
import com.hotent.uc.params.user.UserVo;
import com.hotent.uc.util.OrgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户关系汇报线模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/userRel/v1/")
@Api(tags="汇报关系")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class UserRelController extends BaseController<UserRelManager, UserRel> {
	
	@Resource
	UserRelManager userRelService;
	@Resource
	UserManager userService;
	@Autowired
	PortalFeignService portalFeignService;
	
	/**
	 * 查询用户关系定义
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRels/getUserRelPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户关系定义列表（带分页信息）", httpMethod = "POST", notes = "获取用户关系定义列表")
	public PageList<UserRel> getUserRelPage(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
    	return userRelService.query(filter);
	}
	
	/**
	 * 查询用户关系定义
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRels/getUserRelByTypeId",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据汇报线分类编码获取下属的所有汇报线", httpMethod = "GET", notes = "根据汇报线分类编码获取下属的所有汇报线")
	public List<UserRel> getUserRelByTypeId(@ApiParam(name = "typeId", value = "分类Id", required = true) @RequestParam String typeId) throws Exception{
		List<UserRel> list = userRelService.getUserRelByTypeId(typeId);
		List<UserRel> rtnList = BeanUtils.listToTree(list);
		return  rtnList;
	}
	
	/**
	 * 查询用户关系定义
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRels/getChildRelByAilas",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据汇报线别名获取其直接子节点", httpMethod = "GET", notes = "根据汇报线别名获取其直接子节点")
	public PageList<UserRel> getChildRelByAilas(@ApiParam(name = "alias", value = "查询参数", required = true) @RequestParam String alias) throws Exception{
    	return  userRelService.getChildRelByAilas(alias);
	}
	
	/**
	 * 添加用户关系定义
	 * @param userRelVo
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="userRel/addUserRel",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加用户关系定义", httpMethod = "POST", notes = "添加用户关系定义")
	public CommonResult<String> addUserRel(@ApiParam(name="userRelVo",value="用户关系定义参数对象", required = true) @RequestBody List<UserRelVo> userRelVo) throws Exception{
		return userRelService.addUserRel(userRelVo);
	}
	
	/**
	 * 根据用户关系定义编码删除用户关系定义
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRel/deleteUserRel",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户关系定义编码删除用户关系定义", httpMethod = "POST", notes = "根据用户关系定义编码删除用户关系定义")
	public CommonResult<String> deleteUserRel(@ApiParam(name="codes",value="用户关系定义编码", required = true) @RequestBody String codes) throws Exception{
		return userRelService.deleteUserRel(codes);
	}
	
	
	/**
	 * 更新用户关系定义
	 * @param userRelVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRel/updateUserRel",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新用户关系定义", httpMethod = "POST", notes = "更新用户关系定义")
	public CommonResult<String> updateUserRel(@ApiParam(name="userRelVo",value="用户关系定义参数对象", required = true) @RequestBody  UserRelVo userRelVo) throws Exception{
		return userRelService.updateUserRel(userRelVo);
	}
	
	
	/**
	 * 获取用户关系定义信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRel/getUserRel",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户关系定义别名获取用户关系定义信息", httpMethod = "GET", notes = "获取用户关系定义信息")
	public UserRel getUserRel(@ApiParam(name="alias",value="用户关系定义别名", required = true) @RequestParam String alias) throws Exception{
		return userRelService.getByAlias(alias);
	}
	
	
	/**
	 * 获取直接上级用户
	 * @param userRelFilterObject
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="userRel/getSuperUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取直接上级用户", httpMethod = "POST", notes = "获取直接上级用户")
	public List<UserVo> getSuperUser(@ApiParam(name="userRelFilterObject",value="用户关系定义编码", required = true) @RequestBody UserRelFilterObject userRelFilterObject) throws Exception{
		String typeId = userRelService.getRelTypeId(userRelFilterObject);
		List<User> users = userRelService.getSuperUser(userRelFilterObject.getAccount() , typeId);
		return OrgUtil.convertToUserVoList(users);
	}
	
	/**
	 * 获取所有上级用户
	 * @param userRelFilterObject
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="userRel/getAllSuperUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有上级用户", httpMethod = "POST", notes = "获取所有上级用户")
	public List<UserVo> getAllSuperUser(@ApiParam(name="userRelFilterObject",value="用户关系定义编码", required = true) @RequestBody UserRelFilterObject userRelFilterObject) throws Exception{
		String typeId = userRelService.getRelTypeId(userRelFilterObject);
		List<User> users = userRelService.getAllSuperUser(userRelFilterObject.getAccount(), typeId);
		return OrgUtil.convertToUserVoList(users);
	}
	
	/**
	 * 获取直接下级用户
	 * @param userRelFilterObject
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="userRel/getLowerUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取直接下级用户", httpMethod = "POST", notes = "获取直接下级用户")
	public List<UserVo> getLowerUser(@ApiParam(name="userRelFilterObject",value="用户关系定义编码", required = true) @RequestBody UserRelFilterObject userRelFilterObject) throws Exception{
		String typeId = userRelService.getRelTypeId(userRelFilterObject);
		List<User> users = userRelService.getLowerUser(userRelFilterObject.getAccount(), typeId);
		return OrgUtil.convertToUserVoList(users);
	}
	
	/**
	 * 获取所有下级用户
	 * @param userRelFilterObject
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="userRel/getAllLowerUser",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有下级用户", httpMethod = "POST", notes = "获取所有下级用户")
	public List<UserVo> getAllLowerUser(@ApiParam(name="userRelFilterObject",value="用户关系定义编码", required = true) @RequestBody UserRelFilterObject userRelFilterObject) throws Exception{
		String typeId = userRelService.getRelTypeId(userRelFilterObject);
		List<User> users = userRelService.getAllLowerUser(userRelFilterObject.getAccount(), typeId);
		return OrgUtil.convertToUserVoList(users);
	}
	
	/**
	 * 物理删除所有逻辑删除了的用户关系汇报线数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRel/deleteUserRelPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的用户关系汇报线数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的用户关系汇报线数据")
	public CommonResult<Integer> deleteUserRelPhysical() throws Exception{
		Integer num = userRelService.removePhysical();
		return OrgUtil.getRemovePhysiMsg(num);
	}
	
	/**
	 * 根据时间获取汇报线节点数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userRels/getUserRelByTime",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取汇报线节点数据（数据同步）", httpMethod = "GET", notes = "根据时间获取汇报线节点数据（数据同步）")
	public List<UserRel> getUserRelByTime(@ApiParam(name="btime",value="开始时间（格式：2018-01-01 12:00:00或2018-01-01）") @RequestParam(required=false) String btime,@ApiParam(name="etime",value="结束时间（格式：2018-02-01 12:00:00或2018-02-01）") @RequestParam(required=false) String etime) throws Exception{
		return userRelService.getUserRelByTime(btime,etime);
	}
	
	
	
	/**
	 * 更新汇报线节点所在树的位置
	 * @param request
	 * @param response
	 * @throws Exception void
	 * @exception
	 */
	@RequestMapping(value="userRels/updateRelPos",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新汇报线节点所在树的位置", httpMethod = "POST", notes = "更新汇报线节点所在树的位置（树结构拖动保存）")
	public CommonResult<String> updateRelPos(@ApiParam(name="relId",value="移动节点id", required = true) @RequestParam String relId,@ApiParam(name="parentId",value="移至（目标）节点id", required = true) @RequestParam String parentId) throws Exception {
		
		return userRelService.updateRelPos(relId, parentId);
	}
	
}
