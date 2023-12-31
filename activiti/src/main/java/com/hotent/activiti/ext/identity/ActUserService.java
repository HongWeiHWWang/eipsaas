package com.hotent.activiti.ext.identity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.exception.ProcessDefException;
import com.hotent.bpm.api.helper.identity.UserQueryPluginHelper;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.BpmSignDataService;
import com.hotent.bpm.exception.NoTaskUserException;

/**
 * 多实例人员获取。
 * <pre> 
 * 功能：多实例人员的获取包括。
 * 1.会签
 * 2.多实例内部子流程。
 * 3.多实例外部子流程。
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-30-下午10:06:29
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ActUserService {
	
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;  
	@Resource
	BpmDefinitionService bpmDefinitionService;  
	@Resource
	UserQueryPluginHelper userQueryPluginHelper;
	@Resource
	BpmSignDataService bpmSignDataService;
	
	
	private final static Logger logger = LoggerFactory.getLogger(ActUserService.class);
	
	/**
	 * 获取多实例外部子流程的执行人员。 
	 * @param execution
	 * @return
	 * @throws Exception 
	 * List &lt;BpmIdentity>
	 * @exception 
	 */
	public List<BpmIdentity> getExtSubProcessMultipleUser(final ActivityExecution execution) throws Exception{
		
		List<BpmIdentity> list=getCommonUser(execution,new INodeDef() {
			
			@Override
			public BpmNodeDef getNodeDef(String bpmnDefId, String nodeId) throws Exception {
				BpmNodeDef bpmNodeDef=bpmDefinitionService.getBpmNodeDef(bpmnDefId, nodeId);
				CallActivityNodeDef callNodeDef=(CallActivityNodeDef)bpmNodeDef;
				String flowKey=callNodeDef.getFlowKey();
				
				BpmDefinition bpmDef= bpmDefinitionService.getBpmDefinitionByDefKey(flowKey,false);
				if(BeanUtils.isEmpty(bpmDef)){
					String str=execution.getCurrentActivityName() +"未获取到外部子流程节点定义!";
					logger.debug(str);
					throw new ProcessDefException(str);
				}
				
				BpmProcessDef<BpmProcessDefExt> subProcessDef= bpmDefinitionAccessor.getBpmProcessDef(bpmDef.getDefId());
				
				List<BpmNodeDef> bpmNodeDefList= subProcessDef.getStartNodes();
				
				if(bpmNodeDefList.size()>1){
					String str=execution.getCurrentActivityName() +"外部多实例子流程发起节点后只能有一个后续节点!";
					logger.debug(str);
					throw new ProcessDefException(str);
				}
				BpmNodeDef firstNode=bpmNodeDefList.get(0);
				return firstNode;
			}
		},3);
		
		return list;
		

	}
	
	
	
	/**
	 * 取得内部子流程的用户。
	 * @param execution
	 * @return	  List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	public List<BpmIdentity> getSubProcessUser(final ActivityExecution execution) throws Exception{
		
		List<BpmIdentity> list=getCommonUser(execution,new INodeDef() {
			
			@Override
			public BpmNodeDef getNodeDef(String bpmnDefId, String nodeId) throws Exception {
				BpmNodeDef bpmNodeDef=bpmDefinitionService.getBpmNodeDef(bpmnDefId, nodeId);
				SubProcessNodeDef subProcessNodeDef=(SubProcessNodeDef)bpmNodeDef;
				BpmProcessDef<? extends BpmProcessDefExt> subProcessDef= subProcessNodeDef.getChildBpmProcessDef();
				List<BpmNodeDef> bpmNodeDefList=subProcessDef.getStartNodes();
				if(bpmNodeDefList.size()>1){
					String str=execution.getCurrentActivityName() +"内部多实例子流程发起节点后只能有一个后续节点!";
					logger.debug(str);
					throw new ProcessDefException(str);
				}
				BpmNodeDef firstNode=bpmNodeDefList.get(0);
				return firstNode;
			}
		},2);
		return list;
	}
	
	/**
	 * 获取会签节点人员。
	 * @param execution
	 * @return 
	 * List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	public List<BpmIdentity> getSignUser(ActivityExecution execution) throws Exception{
		
		List<BpmIdentity> list=getCommonUser(execution,new INodeDef() {
			
			@Override
			public BpmNodeDef getNodeDef(String bpmnDefId, String nodeId) throws Exception {
				String defId=bpmDefinitionService.getDefIdByBpmnDefId(bpmnDefId);
				BpmNodeDef bpmNodeDef= bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
				return bpmNodeDef;
			}
		},1);
		return list;
	}
	
	/**
	 * 多实例人员的计算。
	 * <pre>
	 * 这个支持多种算法。
	 * 1.会签人员计算。
	 * 2.多实例内部子流程。
	 * 3.多实例外部子流程。
	 * 
	 * 计算方法描述：
	 * 1.如果是多实例串行的情况，先尝试根据 "当前节点ID_signUsers" 查找人员。
	 * 2.如果找到则直接返回，未找到则从上下文获取。
	 * 3.从上下文获取这里需要依赖INodeDef接口。
	 * 		这个获取人员插件对应的节点定义，不同的任务计算策略不一样。
	 * 		1.会签节点就获取当前节点的定义就可以了。
	 * 		2.内部子流程需要获取这个子流程的起始节点后的第一个节点。
	 * 		3.外部子流程需要获取这个子例程的其实节点后的第一个节点。
	 * 		但是内部流程和外部子流程获取的原理是不一样的。		
	 * 
	 * 4.如果获取不到则读出插件配置获取用户，获取到以后把人员信息添加到上下文中。
	 * 		添加到上下文键值是第三步获取到的节点ID。
	 * 
	 * 	获取用户后具体的计算参考  {@linkplain com.hotent.bpmx.plugin.core.runtime.BaseUserAssignPlugin 基础用户分配插件}
	 * </pre>
	 * @param execution
	 * @param nodeDef
	 * @param type		1，会签，2，内部子流程，3，外部子流程
	 * @return  List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private List<BpmIdentity>  getCommonUser(ActivityExecution execution,INodeDef nodeDef,Integer type) throws Exception{
		String actDefId=execution.getProcessDefinitionId();
		String nodeId=execution.getCurrentActivityId();
		String nodeName=execution.getCurrentActivityName();
		
		List<BpmIdentity> bpmIdentities=new ArrayList<BpmIdentity>();
		
		String multiInstance=(String)execution.getActivity().getProperty(BpmConstants.MULTI_INSTANCE);
		String varName=BpmConstants.SIGN_USERIDS + nodeId;
		
		//获取上下文中的CMD对象。
		BaseActionCmd actionCmd=(BaseActionCmd) ContextThreadUtil.getActionCmd();
		
		//1 是串行：会签人员首先从流程变量中获取。
		if(BpmConstants.MULTI_INSTANCE_SEQUENTIAL .equals(multiInstance)){
			bpmIdentities=(List<BpmIdentity>)execution.getVariable(varName);
			if(BeanUtils.isNotEmpty(bpmIdentities) ) {
				return bpmIdentities;
			}
		}
		BpmNodeDef bpmNodeDef=nodeDef.getNodeDef(actDefId, nodeId);	
		String currentNodeId=bpmNodeDef.getNodeId();
		
		ActionCmd cmd=ContextThreadUtil.getActionCmd();
		//处理动作
		String actionName=cmd.getActionName();
		
		//2.驳回的情况处理。如果是获取历史人员。
		if("reject".equals(actionName) && cmd.getTransitVars("IsDoneUnused") == null && bpmNodeDef instanceof SignNodeDef  ){
			NodeProperties nodeProperties= bpmNodeDef.getLocalProperties();
			String backUserMode= nodeProperties.getBackUserMode();
			if(StringUtil.isEmpty(backUserMode)||backUserMode.equals("history")){ 
				//会签的情况
				if(type==1){
					bpmIdentities=bpmSignDataService.getHistoryAuditor(execution.getParentId(), nodeId);
					if(BeanUtils.isNotEmpty(bpmIdentities) ) {
						saveSequnceExecutorVar(multiInstance,execution,bpmIdentities);
						actionCmd.setBpmIdentity(currentNodeId, bpmIdentities);
						return bpmIdentities;
					}
				}
			}
		}
		
		//3 从上下文获取人员
		//如果有直接设置到流程变量，并返回。
		//调用接口获取合适的流程节点定义。
		
		
		Map<String,List<BpmIdentity>> identityMap= actionCmd.getBpmIdentities();
		bpmIdentities=identityMap.get(currentNodeId);
		if(BeanUtils.isNotEmpty(bpmIdentities)){
			saveSequnceExecutorVar(multiInstance,execution,bpmIdentities);
			return bpmIdentities;
		}
		
		// 4 指定执行人
		Map<String,List< BpmIdentity>> nodeUsers=(Map<String,List< BpmIdentity>>) actionCmd.getTransitVars(BpmConstants.BPM_NODE_USERS);
		// 正常跳转指定的执行人
		if(actionCmd.getTransitVars(BpmConstants.BPM_NEXT_NODE_USERS)!=null){
			bpmIdentities = (List<BpmIdentity>) actionCmd.getTransitVars(BpmConstants.BPM_NEXT_NODE_USERS);
			actionCmd.setBpmIdentity(currentNodeId, bpmIdentities);
		}
		// 已有指定执行人
		if(nodeUsers!=null && nodeUsers.containsKey(nodeId)){
		    bpmIdentities=nodeUsers.get(nodeId);
			actionCmd.setBpmIdentity(currentNodeId, bpmIdentities);
		}
		
		if(BeanUtils.isNotEmpty(bpmIdentities)){
			saveSequnceExecutorVar(multiInstance,execution,bpmIdentities);
			actionCmd.setBpmIdentity(currentNodeId, bpmIdentities);
			return bpmIdentities;
		}	
		
		
		//5 从数据库查询
		List<BpmPluginContext> bpmPluginContexts= bpmNodeDef.getBpmPluginContexts();		
		bpmIdentities = userQueryPluginHelper.query(bpmPluginContexts, execution.getVariables(),UserQueryPluginHelper.TYPE_USER);
		
		//如果为串行,保存到变量到数据库中。
		if(BeanUtils.isNotEmpty(bpmIdentities)){
			saveSequnceExecutorVar(multiInstance,execution,bpmIdentities);
			actionCmd.setBpmIdentity(currentNodeId, bpmIdentities);
		}		
		
		if(BeanUtils.isEmpty(bpmIdentities)){
			String str=nodeName+ "多实例节点下没有配置执行人!";
			logger.debug(str);		
			throw new RuntimeException(str);
		}
		
		return bpmIdentities;
	}
	
	
	
	
	
	
	
	
	/**
	 * 如果为串行，那么将人员变量保存到数据库中。
	 * @param execution
	 * @param userIds
	 */
	private void saveSequnceExecutorVar(String multiInstance, ActivityExecution execution,List<BpmIdentity> userIds){
		String nodeId=execution.getActivity().getId();
		if(!BpmConstants.MULTI_INSTANCE_SEQUENTIAL.equals(multiInstance)) return;
				
		String varName=BpmConstants.SIGN_USERIDS + nodeId ;
		execution.setVariable(varName, userIds);
		
	}
	
}
