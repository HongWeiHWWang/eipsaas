package com.hotent.bpmModel.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.helper.identity.UserQueryPluginHelper;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.uc.api.model.GroupType;
import com.hotent.uc.api.service.IUserGroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 *  描述： 用户计算控制器  
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/node/usercalc/v1/")
@Api(tags="人员查找策略")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class UsercalcController {
	@Resource
	BpmDefinitionService bpmDefinitionService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	IUserGroupService userGroupService;
	@Resource
	private UserQueryPluginHelper userQueryPluginHelper;

	/**
	 * 用户组列表
	 */
	@RequestMapping(value="groupRelSelect",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "用户组列表", httpMethod = "GET", notes = "用户组列表")
	public List<GroupType> groupRelSelect() throws Exception{
		List<GroupType> dimensionList= userGroupService.getGroupTypes();
		return dimensionList;
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping(value="roleSelect",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "角色列表", httpMethod = "GET", notes = "角色列表")
	public List<GroupType> roleSelect() throws Exception{
		List<GroupType> dimensionList= userGroupService.getGroupTypes();
		return dimensionList ;
	}
	
	/**
	 * 相同节点执行人
	 */
	@RequestMapping(value="sameNodeSelect",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "相同节点执行人", httpMethod = "GET", notes = "相同节点执行人")
	public List<BpmNodeDef>  sameNodeSelect(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId) throws Exception {
		List<BpmNodeDef> nodeDefList = bpmDefinitionAccessor.getNodeDefs(defId);
		return nodeDefList;
	}
	
	/**
	 * 获取节点用户插件列表
	 */
	@RequestMapping(value="getNodeUserPluginList",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取节点用户插件列表", httpMethod = "GET", notes = "获取节点用户插件列表")
	public Object  getNodeUserPluginList() throws Exception {
		Object obj=AppUtil.getBean("nodeUserPluginList");
		return obj;
	}
}
