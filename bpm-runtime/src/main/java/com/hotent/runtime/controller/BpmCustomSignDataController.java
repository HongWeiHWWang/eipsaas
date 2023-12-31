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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.controller.BaseController;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.persistence.manager.BpmCustomSignDataManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.BpmCustomSignData;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.util.BpmIdentityUtil;



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
@RequestMapping("/runtime/customsign/v1/")
@Api(tags="BpmTaskSignLineController")
public class BpmCustomSignDataController extends BaseController<BpmCustomSignDataManager,BpmCustomSignData>{

	@Resource
	private BpmCustomSignDataManager bpmCustomSignDataManager;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	UCFeignService ucFeignService;
	
	@RequestMapping(value = "getParallelRevokeTasks", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取并行签署节点", httpMethod = "GET", notes = "获取并行签署节点")
	public ArrayNode getSignLinesInstIdNodeId(
			@ApiParam(name="instanceId",value="流程实例id",required=true) @RequestParam(required=true) String instanceId,
			@ApiParam(name="nodeId",value="节点id",required=true) @RequestParam(required=true) String nodeId,
			@ApiParam(name="taskId",value="任务id",required=false) @RequestParam(required=false) String taskId) throws Exception{
		List<BpmCustomSignData> parallelDatas = new ArrayList<BpmCustomSignData>();
		ArrayNode listToArrayNode = null;
		List<DefaultBpmTask> tasks = bpmTaskManager.getByInstId(instanceId);
		if(BeanUtils.isEmpty(tasks)) {
			throw new BaseException("没有可撤回任务");
		}
		DefaultBpmTask currentTask = tasks.get(0);
		String status = currentTask.getStatus();
		if(TaskType.SIGNLINEED.getKey().equals(status)) {
			if(nodeId.equals(currentTask.getNodeId())) {
				// An 撤回An1 ... Ann
				parallelDatas = bpmCustomSignDataManager.getParallelAllSonByTaskId(taskId);
			}else {
				// A 撤回
				parallelDatas = bpmCustomSignDataManager.getAllSignDataByBeforeSignTaskId(instanceId, taskId);
			}
			
			
			if(BeanUtils.isNotEmpty(parallelDatas)){
				List<String> userIds = new ArrayList<String>();
				for (BpmCustomSignData bpmCustomSignData : parallelDatas) {
					List<BpmIdentity> qualfields2BpmIdentity = BpmIdentityUtil.qualfields2BpmIdentity(bpmCustomSignData.getExecutor());
					userIds.add(qualfields2BpmIdentity.get(0).getId());
				}
				
				listToArrayNode = JsonUtil.listToArrayNode(parallelDatas);
				listToArrayNode =  ucFeignService.getUserInfoBySignData(listToArrayNode);
				
				DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instanceId);
				DefaultBpmDefinition def = bpmDefinitionManager.getById(instance.getProcDefId());
				String isReadRevoke = def.getIsReadRevoke();
				if("true".equals(isReadRevoke)){
					for (JsonNode jsonNode : listToArrayNode) {
						ObjectNode objectNode = (ObjectNode) jsonNode;
						objectNode.put("isRead", 0);
					}
				}
			}
		}
		
		return listToArrayNode;
	}
	
}
