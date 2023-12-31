package com.hotent.bpm.plugin.core.runtime;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.exception.ProcessDefException;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.MultiInstanceDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.plugin.core.def.BpmTaskPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;

/**
 * <pre> 
 * 基础用户分配插件， 用户计算插件继承这个类,默认的用户插件为{@linkplain com.hotent.bpm.plugin.task.userassign.plugin.UserAssignPlugin 用户插件}。
 * 这个类优先处理了如下事情，而不是从插件计算出来的用户。
 * 1.如果是多实例内部子流程的情况。
 * 从变量取得用户调用delegateTask.addExecutor方法添加用户并返回,
 *这个变量的获取需要参考,{@linkplain com.hotent.runtime.activiti.ext.identity.ActUserService 多实例用户计算}类。
 * 
 * 2.如果是多实例外部子流程的情况。
 * 从变量取得用户调用delegateTask.addExecutor方法添加用户并返回,
 *这个变量的获取需要参考:{@linkplain com.hotent.runtime.activiti.ext.identity.ActUserService 多实例用户计算}类。
 * 
 * 3.从上下文获取人员获取到了直接返回。
 * 	
 * 4.实现子类实现抽象方法获取从插件获取人员。
 * 
 * 最终人员分配参考 {@linkplain com.hotent.runtime.listener.TaskCreateEventListener 任务创建事件监听器}类。
 * 
 * 描述：
 * 构建组：x5-bpmx-plugin-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-18-上午11:03:04
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class BaseUserAssignPlugin extends AbstractBpmTaskPlugin {
	
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;  
	@Resource
	BpmDefinitionService bpmDefinitionService;  
	
	public abstract void executeExt(BpmTaskPluginSession pluginSession,BpmTaskPluginDef pluginDef) throws Exception;

	@Override
	public Void execute(BpmTaskPluginSession pluginSession,
			BpmTaskPluginDef pluginDef) throws Exception {
		
		BpmDelegateTask bpmTask=pluginSession.getBpmDelegateTask();
		ActionCmd taskCmd= ContextThreadUtil.getActionCmd();
		String nodeId=bpmTask.getTaskDefinitionKey();
		
		//子流程多实例处理情况。
		boolean isSubProcessMulti= handSubProcessUser(bpmTask);
		
		if(isSubProcessMulti) return null;
		
		//多实例外部子流程
		boolean isExtProcessMulti=handExtSubProcessUser(bpmTask);
		if(isExtProcessMulti) return null;
		
		
		Map<String,List<BpmIdentity>> identityMap= taskCmd.getBpmIdentities();
		
		
		List<BpmIdentity> identityList=identityMap.get(nodeId);
		
		//如果已经指定了人员则直接终止，不再执行从配置读取人员。
		if(BeanUtils.isNotEmpty(identityList) ){
			return null;
		};
		
		//调用其他的插件进行运算。
		executeExt(pluginSession,pluginDef);
		
		return null;
	}
	
	/**
	 * 处理多实例内部子流程用户。
	 * @param delegateTask
	 * @return boolean
	 * @throws Exception 
	 */
	private boolean handSubProcessUser(BpmDelegateTask delegateTask) throws Exception{
		String bpmnDefId = delegateTask.getBpmnDefId();
		BpmNodeDef bpmNodeDef=bpmDefinitionService.getBpmNodeDef(bpmnDefId, delegateTask.getTaskDefinitionKey());
		BpmNodeDef parentBpmNodeDef= bpmNodeDef.getParentBpmNodeDef();
		if(parentBpmNodeDef==null ) return false;
		if(!(parentBpmNodeDef instanceof MultiInstanceDef)) return false;
		
		MultiInstanceDef multiDef=(MultiInstanceDef)parentBpmNodeDef;
		if(!multiDef.supportMuliInstance()) return false;
		
		//子流程的第一个节点才获取人员，否则不进行处理。
		SubProcessNodeDef subDef=(SubProcessNodeDef) multiDef;
		BpmProcessDef<? extends BpmProcessDefExt> subProcessDef= subDef.getChildBpmProcessDef();
		List<BpmNodeDef> nodeList= subProcessDef.getStartNodes();
		BpmNodeDef startNode=nodeList.get(0);
		if(!startNode.getNodeId().equals(delegateTask.getTaskDefinitionKey())) return false;
		
		
		//若为多实例子流程中的任务，则从线程中的人员取出，并且把该人员从线程中删除
		BpmIdentity bpmIdentity=(BpmIdentity)delegateTask.getVariable(BpmConstants.ASIGNEE);
		
		if(bpmIdentity!=null){
			delegateTask.addExecutor(bpmIdentity);
			delegateTask.removeVariable(BpmConstants.ASIGNEE);
			return true;
		}
		return false;
	}
	
	/**
	 * 处理多实例外部子流程多实例流程的人员。
	 * @param delegateTask
	 * @return boolean
	 * @throws Exception 
	 */
	private boolean handExtSubProcessUser(BpmDelegateTask delegateTask) throws Exception{
		String nodeId=delegateTask.getTaskDefinitionKey();
		//判断当是否为外部子流程，判断是否有上级执行ID，没有则返回
		String supperExeId=delegateTask.getSupperExecutionId();
		if(StringUtil.isEmpty(supperExeId)) return false;
		//是否为多实例，不是则返回
		MultiInstanceType multiType=delegateTask.supperMultiInstanceType();
		if(multiType.equals(MultiInstanceType.NO)) return false;
		
		String bpmnDefId = delegateTask.getBpmnDefId();
		BpmNodeDef bpmNodeDef=bpmDefinitionService.getBpmNodeDef(bpmnDefId, nodeId);
		BpmProcessDef<?> procDef=bpmNodeDef.getBpmProcessDef();
		
		List<BpmNodeDef> bpmNodeDefList= procDef.getStartNodes();
		if(bpmNodeDefList.size()>1){
			throw new  ProcessDefException("多实例子流程发起节点后只能有一个用户任务节点");
		}
		
		
		BpmNodeDef startNode=bpmNodeDefList.get(0);
		if(!startNode.getNodeId().equals(nodeId)) return false;
		
		
		//若为多实例子流程中的任务，则从线程中的人员取出，并且把该人员从线程中删除
		BpmIdentity bpmIdentity=(BpmIdentity)delegateTask.getSupperVariable(BpmConstants.ASIGNEE); 
		
		if(bpmIdentity!=null){
			delegateTask.addExecutor(bpmIdentity);
			return true;
		}
		return false;
	}

}
