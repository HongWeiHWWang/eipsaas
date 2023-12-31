package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.runtime.manager.BpmTaskSignLineManager;
import com.hotent.runtime.model.BpmTaskSignLine;



/**
 * 
 * <pre> 
 * 描述：BpmTaskSignLine管理
 * 构建组：x5-bpmx-platform
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-15 14:47:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/runtime/signline/v1/")
@Api(tags="并行签署")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmTaskSignLineController extends BaseController<BpmTaskSignLineManager,BpmTaskSignLine>{

	@Resource
	private BpmTaskSignLineManager signLineManager;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	
	@RequestMapping(value = "getSignLinesInstIdNodeId", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取并行签署节点", httpMethod = "GET", notes = "获取并行签署节点")
	public List<BpmTaskSignLine> getSignLinesInstIdNodeId(@ApiParam(name="instanceId",value="流程实例id",required=true) @RequestParam(required=true) String instanceId,
			@ApiParam(name="nodeId",value="节点id",required=true) @RequestParam(required=true) String nodeId,
			@ApiParam(name="taskId",value="任务id",required=false) @RequestParam(required=false) String taskId) throws Exception{
		List<BpmTaskSignLine> signLines = new ArrayList<BpmTaskSignLine>();
		BpmTaskSignLine signLine = signLineManager.getByTaskId(taskId);
		if(BeanUtils.isNotEmpty(signLine)){
			// An 撤回Ann
			signLines =  signLineManager.getByPathChildAndStatus(String.format("%s.%s", signLine.getPath(),signLine.getTaskId()), null);
		}else{
			signLines = signLineManager.getByInstNodeIdAndStatus(instanceId,taskId, nodeId, null);
		}
		if(BeanUtils.isNotEmpty(signLines)){
			DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instanceId);
			DefaultBpmDefinition def = bpmDefinitionManager.getById(instance.getProcDefId());
			String isReadRevoke = def.getIsReadRevoke();
			if("true".equals(isReadRevoke)){
				for (BpmTaskSignLine bpmTaskSignLine : signLines) {
					bpmTaskSignLine.setIsRead(0);
				}
			}
		}
		return signLines;
	}
	
}
