package org.activiti.engine.impl.listener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.event.ProcessInstanceEndEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.activiti.def.BpmDefUtil;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCustomSignDataManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmSignDataManager;
import com.hotent.bpm.persistence.model.BpmExeStack;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.util.BpmCheckOpinionUtil;


/**
 * 流程结束监听器。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-17-下午4:51:21
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
@Transactional
public class ProcessInstEndListener implements ApplicationListener<ProcessInstanceEndEvent>,Ordered {

	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmSignDataManager bpmSignDataManager;
	@Resource
	BpmCustomSignDataManager bpmCustomSignDataManager;
	
	
	@Override
	public void onApplicationEvent(ProcessInstanceEndEvent ev) {
		BpmDelegateExecution execution=(BpmDelegateExecution) ev.getSource();
		//更新流程实例状态。
		updProcessInstance(execution);
		
		Integer instCount=(Integer) execution.getSupperVariable(BpmConstants.NUMBER_OF_INSTANCES);
		Integer completeInstCount=(Integer) execution.getSupperVariable(BpmConstants.NUMBER_OF_COMPLETED_INSTANCES);
		//外部子流程的情况需要传递变量。
		//外部子流程结束时构建CMD。
		if(StringUtil.isNotZeroEmpty(execution.getSupperExecutionId())){
			//单实例的情况。//多实例结束
			if(instCount==null || instCount.equals(completeInstCount)){
				Map<String,Object> commuVars_=execution.getVariables();
				Map<String, Object> commuVars = ContextThreadUtil.getCommuVars();
				commuVars_.putAll(commuVars);
				ContextThreadUtil.setCommuVars(commuVars_);
				
				String parentProcInstId=(String) commuVars_.get(BpmConstants.PROCESS_PARENT_INST_ID);
				
				BpmDelegateExecution supperExecution = execution.getSupperExecution();
				// 外部子流程结束后  可以直来直往回到原来的驳回的节点
				if(BeanUtils.isNotEmpty(supperExecution)){
					String destination = getDestination(parentProcInstId,supperExecution.getNodeId(),ContextThreadUtil.getActionCmd());
					if(StringUtil.isNotEmpty(destination)){
						BpmDefUtil.prepare(supperExecution.getBpmnDefId(), supperExecution.getNodeId(), new String[]{destination});
					}
				}
				
				converCmd(parentProcInstId);
			}
		}
	}

	/**
	 * 转换CMD。
	 * @param parentProcInstId
	 * @param instId 
	 * void
	 */
	private void converCmd(String parentProcInstId){
		BaseActionCmd baseCmd=(BaseActionCmd)ContextThreadUtil.getActionCmd();
		
		BpmProcessInstance parentProcessInst=bpmProcessInstanceManager.get(parentProcInstId);
		DefaultProcessInstCmd cmd=new DefaultProcessInstCmd();
		cmd.setInstId(parentProcInstId);
		cmd.setActionName(baseCmd.getActionName());
		cmd.setBpmIdentities(baseCmd.getBpmIdentities());
		cmd.addTransitVars(BpmConstants.PROCESS_INST,parentProcessInst);
		cmd.addTransitVars(BpmConstants.BPM_TASK, baseCmd.getTransitVars(BpmConstants.BPM_TASK));
		cmd.addTransitVars(BpmConstants.PREVIOUS_CMD,baseCmd);
		ContextThreadUtil.setActionCmd(cmd);
	}
	
	/**
	 * 
	 *更新流程实例状态。
	 * @param bpmnInstId 
	 * void
	 */
	private void updProcessInstance(BpmDelegateExecution execution){
		String instId=(String) execution.getVariable(BpmConstants.PROCESS_INST_ID);
		BaseActionCmd cmd=(BaseActionCmd)ContextThreadUtil.getActionCmd();
		DefaultBpmProcessInstance instance= bpmProcessInstanceManager.get(instId);
		String procInstId = instance.getId();
		
		//添加多一个结束的审核意见记录
		DefaultBpmCheckOpinion entity=BpmCheckOpinionUtil.buildBpmCheckOpinion(execution,instId,true);
		bpmCheckOpinionManager.create(entity);
		
		//将审批意见归档为历史，并删除流程实例的审批意见
		bpmCheckOpinionManager.archiveHistory(procInstId);
		
		//更新流程实例状态
		updateStatus(instance, cmd.getActionName());
		
		bpmProStatusManager.archiveHistory(procInstId);
		
		//流程结束时，清除会签结果数据。
		List<String> instList=new ArrayList<String>();
		instList.add(procInstId);
		bpmSignDataManager.delByInstList(instList);
		//流程结束时，清除bpm_custom_signdata相关数据。
		bpmCustomSignDataManager.removeByInstId(procInstId);
	}

	private void updateStatus(DefaultBpmProcessInstance instance,String actionName){
		instance.setStatus(ProcessInstanceStatus.STATUS_END.getKey());
		instance.setDuration(getDuration(instance.getCreateTime()));
		instance.setResultType(actionName);
		instance.setEndTime(LocalDateTime.now());
		bpmProcessInstanceManager.update(instance);
	}
	
	private Long getDuration(LocalDateTime localDateTime){
		Long duration=TimeUtil.getTime(LocalDateTime.now(), localDateTime);
		return duration;
	}
	
	@Override
	public int getOrder() {
		return 1;
	}
	
	private String getDestination(String instId, String nodeId, Object transitVars) {
		BpmExeStackManager bpmExeStackManager = AppUtil.getBean(BpmExeStackManager.class);
		BpmExeStack stack = bpmExeStackManager.getStack(instId, nodeId, null);
		if(BeanUtils.isNotEmpty(stack) && StringUtil.isNotEmpty(stack.getTargetNode())){
			DefaultTaskFinishCmd cmd = (DefaultTaskFinishCmd) transitVars;
			cmd.setDestination(stack.getTargetNode());
			return stack.getTargetNode();
		}
		return "";
	}


}
