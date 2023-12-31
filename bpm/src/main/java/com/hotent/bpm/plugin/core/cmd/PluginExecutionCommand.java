package com.hotent.bpm.plugin.core.cmd;

import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.plugin.core.cmd.ExecutionCommand;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmExecutionPluginDef;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginFactory;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.plugin.core.runtime.BpmExecutionPlugin;
import com.hotent.bpm.api.plugin.core.session.BpmExecutionPluginSession;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.plugin.core.session.DefaultBpmExecutionPluginSession;

public class PluginExecutionCommand implements ExecutionCommand{

	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmPluginFactory bpmPluginFactory;
	@Resource
	BpmPluginSessionFactory bpmPluginSessionFactory;
	@Resource
	BpmDefinitionService bpmDefinitionService;
	
	@Override
	public void execute(EventType eventType, BpmDelegateExecution execution) throws Exception {
		String bpmnDefId=execution.getBpmnDefId();
		String defId= bpmDefinitionService.getDefIdByBpmnDefId(bpmnDefId);
		//改造执行插件的会话数据
		BpmExecutionPluginSession bpmExecutionPluginSession = bpmPluginSessionFactory.buildBpmExecutionPluginSession(execution);
		((DefaultBpmExecutionPluginSession)bpmExecutionPluginSession).setEventType(eventType);
		
		//处理流程的执行类插件
		BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		List<BpmPluginContext> pluginContextList=bpmProcessDef.getProcessDefExt().getBpmPluginContexts();
		if(BeanUtils.isNotEmpty(pluginContextList)){
			
			for(BpmPluginContext bpmPluginContext:pluginContextList){
				executeContext(bpmPluginContext,bpmExecutionPluginSession,eventType);
			}
		}
		//开始事件，节点为空。
		if(StringUtil.isNotEmpty(execution.getNodeId())){
			//处理流程节点的执行类插件
			BpmNodeDef bpmNodeDef=bpmDefinitionAccessor.getBpmNodeDef(defId, execution.getNodeId());
			
			//GateWayBpmNodeDef not support getBpmPluginContexts method
			if(BeanUtils.isNotEmpty(bpmNodeDef) && !bpmNodeDef.getType().equals(NodeType.PARALLELGATEWAY)){
				try {
					List<BpmPluginContext> nodePluginList=bpmNodeDef.getBpmPluginContexts();
					
					if(BeanUtils.isNotEmpty(nodePluginList)){
						for(BpmPluginContext bpmPluginContext:nodePluginList){
							executeContext(bpmPluginContext,bpmExecutionPluginSession,eventType);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private void executeContext(BpmPluginContext bpmPluginContext,BpmExecutionPluginSession bpmExecutionPluginSession,EventType eventType) throws Exception{
		BpmPluginDef bpmPluginDef =bpmPluginContext.getBpmPluginDef();
		if(bpmPluginDef instanceof BpmExecutionPluginDef){
			BpmExecutionPluginDef bpmExecutionPluginDef = (BpmExecutionPluginDef)bpmPluginDef;
			BpmExecutionPlugin bpmExecutionPlugin = bpmPluginFactory.buildExecutionPlugin(bpmPluginContext, eventType);
			if(bpmExecutionPlugin!=null){
				if(bpmPluginContext.getEventTypes().contains(eventType)){
					bpmExecutionPlugin.execute(bpmExecutionPluginSession, bpmExecutionPluginDef);
				}
			}	
		}
	}
}
