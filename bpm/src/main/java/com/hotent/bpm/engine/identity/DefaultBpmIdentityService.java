/**
 * 描述：TODO
 * 包名：com.hotent.runtime.core.engine.identity
 * 文件名：BpmIdentityServiceImpl.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-4-2-上午11:32:39
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.engine.identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.helper.identity.BpmIdentityConverter;
import com.hotent.bpm.api.helper.identity.UserQueryPluginHelper;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmIdentityService;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskCandidate;
import com.hotent.uc.api.model.IUser;

/**
 * <pre>
 * 描述：流程组织架构服务的实现
 * 构建组：x5-bpmx-core
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-2-上午11:32:39
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class DefaultBpmIdentityService implements BpmIdentityService {
	@Resource
	private NatProInstanceService natProInstanceService;

	@Resource
	private BpmProcessInstanceManager bpmProcessInstanceManager;

	@Resource
	private BpmDefinitionAccessor bpmDefinitionAccessor;

	@Resource
	private UserQueryPluginHelper userQueryPluginHelper;

	@Resource
	private BpmTaskManager bpmTaskManager;

	@Resource
	private BpmTaskCandidateManager bpmTaskCandidateManager;

	@Resource
	private BpmIdentityConverter bpmIdentityConverter;

	@Override
	public List<BpmIdentity> searchByNode(String procInstId, String nodeId) throws Exception {
		// 流程实例
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(procInstId);

		// 流程变量
		Map<String, Object> variables = gotVariableMap(instance.getBpmnInstId());

		// 节点定义
		List<BpmPluginContext> bpmPluginContexts = getBpmPluginContexts(instance.getProcDefId(),nodeId);

		// 计算
		List<BpmIdentity> bpmIdentities = userQueryPluginHelper.query(bpmPluginContexts, variables,UserQueryPluginHelper.TYPE_USER);
		
		return bpmIdentities;
	}

	private Map<String, Object> gotVariableMap(String bpmnInstId){ 		
		// 流程变量
		Map<String, Object> variables = natProInstanceService.getVariables(bpmnInstId);		
		return variables;
	}
	
	private List<BpmPluginContext> getBpmPluginContexts(String processDefinitionId,String nodeId) throws Exception{
		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(
				processDefinitionId, nodeId);
		List<BpmPluginContext> bpmPluginContexts = bpmNodeDef.getBpmPluginContexts();
				
		return bpmPluginContexts;
	}
	
	public List<IUser> queryUsersByNode(String procInstId, String nodeId) throws Exception {
		// 流程实例
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager
				.get(procInstId);

		// 流程变量
		Map<String, Object> variables = gotVariableMap(instance.getBpmnInstId());

		// 节点定义
		List<BpmPluginContext> bpmPluginContexts = getBpmPluginContexts(instance.getProcDefId(),nodeId);
		
		List<IUser> users = userQueryPluginHelper.queryUsers(bpmPluginContexts, variables);
		
		return users;
	}

	@Override
	public List<BpmIdentity> queryByTask(String taskId) {		
		// 查询任务
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskId);

		return queryByBpmTask(bpmTask);
	}

	@Override
	public List<BpmIdentity> queryByBpmTask(String bpmnTaskId) {
		// 查询任务
		DefaultBpmTask bpmTask = bpmTaskManager.getByRelateTaskId(bpmnTaskId);
		
		return queryByBpmTask(bpmTask);
	}
	
	@Override
	public List<BpmIdentity> queryByBpmTask(BpmTask bpmTask) {
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
		// 如果执行人不为空则返回执行人，如果为空则查询候选人返回
		if (StringUtil.isNotZeroEmpty(bpmTask.getAssigneeId())) {
			BpmIdentity bpmIdentity = bpmIdentityConverter.convert(
					BpmIdentity.TYPE_USER, bpmTask.getAssigneeId());
			bpmIdentities.add(bpmIdentity);
		} else {
			List<DefaultBpmTaskCandidate> bpmTaskCandidates = bpmTaskCandidateManager
					.queryByTaskId(bpmTask.getId());
			bpmIdentities = convert(bpmTaskCandidates);
		}
		return bpmIdentities;
	}

	

	@Override
	public List<BpmIdentity> queryListByBpmTask(BpmTask bpmTask) {
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
		// 如果执行人不为空则返回执行人，如果为空则查询候选人返回
		if (StringUtil.isNotZeroEmpty(bpmTask.getAssigneeId()) ) {
			BpmIdentity bpmIdentity = bpmIdentityConverter.convertValue(BpmIdentity.TYPE_USER, bpmTask.getAssigneeId());
			bpmIdentities.add(bpmIdentity);
			
			// 如果是设置代理时 原处理人也需要返回
			if(!bpmTask.getAssigneeId().equals(bpmTask.getOwnerId()) && StringUtil.isNotZeroEmpty(bpmTask.getOwnerId())) {
				bpmIdentities.add(bpmIdentityConverter.convertValue(BpmIdentity.TYPE_USER, bpmTask.getOwnerId()));
			}
			
		} else {
			
			List<DefaultBpmTaskCandidate> bpmTaskCandidates = bpmTaskCandidateManager
					.queryByTaskId(bpmTask.getId());
			bpmIdentities = convertValue(bpmTaskCandidates);
		}
		return bpmIdentities;
	}

	private List<BpmIdentity> convertValue(List<DefaultBpmTaskCandidate> candidates) {
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
		for (DefaultBpmTaskCandidate candidate : candidates) {
			BpmIdentity bpmIdentity = bpmIdentityConverter.convertValue(candidate.getType(), candidate.getExecutor());
			bpmIdentities.add(bpmIdentity);
		}
		return bpmIdentities;
	}

	
	private List<BpmIdentity> convert(List<DefaultBpmTaskCandidate> candidates) {
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
		for (DefaultBpmTaskCandidate candidate : candidates) {
			
			BpmIdentity bpmIdentity = bpmIdentityConverter.convert(
					candidate.getType(), candidate.getExecutor());
			bpmIdentities.add(bpmIdentity);
		}
		return bpmIdentities;
	}

	@Override
	public List<BpmIdentity> searchStartByNode(String defId, String nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BpmIdentity> searchByNodeIdOnStartEvent(String procDefId,
			String nodeId) throws Exception {

		// 流程变量
		Map<String, Object> variables = new HashMap<String, Object>();

		// 节点定义
		List<BpmPluginContext> bpmPluginContexts = getBpmPluginContexts(procDefId,nodeId);

		// 计算
		List<BpmIdentity> bpmIdentities = userQueryPluginHelper.query(bpmPluginContexts, variables,UserQueryPluginHelper.TYPE_USER);
		
		return bpmIdentities;
	}
	
	
}
