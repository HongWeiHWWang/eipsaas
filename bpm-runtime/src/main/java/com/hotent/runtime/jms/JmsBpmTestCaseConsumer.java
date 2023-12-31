package com.hotent.runtime.jms;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.JmsConstant;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.jms.JmsProducer;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.ExceptionUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.impl.BpmCheckOpinionManagerImpl;
import com.hotent.bpm.persistence.model.AutoTestModel;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.util.BpmUtil;
import com.hotent.bpm.persistence.util.PublishAutoTestEventUtil;
import com.hotent.runtime.constant.SysObjTypeConstants;
import com.hotent.runtime.manager.BpmTestCaseManager;
import com.hotent.runtime.manager.IFlowManager;
import com.hotent.runtime.manager.IProcessManager;
import com.hotent.runtime.model.BpmTestCase;
import com.hotent.runtime.params.DoEndParamObject;
import com.hotent.runtime.params.DoNextParamObject;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.impl.util.ContextUtil;

@Service
@ConditionalOnProperty(value="jms.enable", matchIfMissing = true)
public class JmsBpmTestCaseConsumer {
	private static final Logger logger = LoggerFactory.getLogger(JmsBpmTestCaseConsumer.class);

	@JmsListener(destination = JmsConstant.BPM_TEST_CASE	, containerFactory="jmsListenerContainerQueue")
	public void receiveQueue(AutoTestModel model) throws Exception {
		logger.debug("[JMS]: queue message is :"+model.getClass().getName()+"---"+model);
		autoTest(model);
	}

	// 流程仿真测试
		private void autoTest(Object obj) throws Exception{
			if(!(obj instanceof AutoTestModel)) return;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			AutoTestModel model = (AutoTestModel) obj;
			
			if(StringUtil.isEmpty(model.getProcInstId())) return;
			
			BpmProcessInstanceManager bean = AppUtil.getBean(BpmProcessInstanceManager.class);
			BpmTestCaseManager testCase = AppUtil.getBean(BpmTestCaseManager.class);
			DefaultBpmProcessInstance instance = bean.get(model.getProcInstId());
			if(BeanUtils.isEmpty(instance)) return;
			String flowKey = instance.getProcDefKey();
			String nodeId = model.getNodeId();
			instance = (DefaultBpmProcessInstance) bean.getTopBpmProcessInstance(instance);
			
			String sysCode = instance.getSysCode();
			if(StringUtil.isEmpty(sysCode) || !sysCode.startsWith(SysObjTypeConstants.BPMX_AUTO_TEST) ) return;
			
			BpmTestCase bpmTestCase = testCase.get(sysCode.replace(SysObjTypeConstants.BPMX_AUTO_TEST, ""));
			
			// 获取当前的审批动作 {nodeId:actionName}==>> {"userTask1":"agree"}
			String actionName = "agree";
			String destination = "";
			int count = 1;
			String actionType = bpmTestCase.getActionType();
			if(StringUtil.isNotEmpty(actionType) &&  JsonUtil.toJsonNode(actionType).has(flowKey) ){
				ArrayNode jsonArray = (ArrayNode) JsonUtil.toJsonNode(actionType).get(flowKey);
				ObjectNode parse = JsonUtil.arrayToObject(jsonArray, "nodeId");
				if(parse.has(model.getNodeId())){
					parse = (ObjectNode) parse.get(model.getNodeId());
					actionName = JsonUtil.getString(parse, "actionName",actionName);
					count =  JsonUtil.getInt(parse, "count",count);
				}
			}
			
		   try {
			   
			   IProcessManager processService = AppUtil.getBean(IProcessManager.class);
			   IFlowManager flowManager = AppUtil.getBean(IFlowManager.class);
			   
			    if("endProcess".equals(actionName)){
			    	DoEndParamObject doEndParamObject = new DoEndParamObject();
			    	doEndParamObject.setAccount(model.getRandomAccount());
			    	doEndParamObject.setEndReason("流程仿真测试--人工结束流程");
			    	doEndParamObject.setTaskId(model.getTaskId());
			    	processService.doEndProcess(doEndParamObject);
			    	return;
				}
			    
			    // bpmDebugger
			    String debugger = bpmTestCase.getBpmDebugger();
			    if(StringUtil.isNotEmpty(debugger) && !model.getSkipDebugger() ){
			    	ObjectNode debuggerJo = (ObjectNode) JsonUtil.toJsonNode(debugger);
			    	if(debuggerJo.has(flowKey)){
			    		ArrayNode jsonArray = (ArrayNode) debuggerJo.get(flowKey);
			    		for (JsonNode jsonNode : jsonArray) {
			    			if(nodeId.equals(jsonNode.asText())){
				    			throw new RuntimeException("设置了断点, 流程审批到该节点停止了测试用例往下执行,需要继续运行的请在流程实例中点击继续运行。 ");
				    		}
						}
			    		
			    	}
			    }
			    
			    if(OpinionStatus.BACK_TO_START.getKey().equals(actionName) || OpinionStatus.REJECT.getKey().equals(actionName)  ){
			    	
			    	if( OpinionStatus.REJECT.getKey().equals(actionName)){
			    		destination = BpmUtil.getRejectPreDestination(model.getTaskId());
			    		if(StringUtil.isEmpty(destination)){
			    			throw new RuntimeException("在审批节点[ "+model.getNodeName()+" ] 不支持驳回到上一步的设置，请修改测试用例。 ");
			    		}
			    	}
			    	
			    	// 判断驳回次数
			    	QueryFilter queryFilter = QueryFilter.build();
			    	queryFilter.addFilter("proc_inst_id_", model.getProcInstId(), QueryOP.EQUAL);
			    	queryFilter.addFilter("task_key_", model.getNodeId(), QueryOP.EQUAL);
			    	queryFilter.addFilter("status_", OpinionStatus.BACK_TO_START.getKey().equals(actionName)?"backToStart":"reject", QueryOP.EQUAL);
			    	BpmCheckOpinionManagerImpl bpmCheckOpinionManagerImpl = (BpmCheckOpinionManagerImpl) AppUtil.getBean("bpmCheckOpinionManager");
			    	
			    	List<DefaultBpmCheckOpinion> query = (List<DefaultBpmCheckOpinion>) bpmCheckOpinionManagerImpl.query(queryFilter);
			    	if(BeanUtils.isNotEmpty(query) && query.size()>=count){
			    		actionName = "agree";
			    	}
			    	
			    }
			   
			    //自动下一任务
				
				DoNextParamObject doNextParamObject = new DoNextParamObject();
				doNextParamObject.setAccount(model.getRandomAccount());
				doNextParamObject.setActionName(actionName);
				doNextParamObject.setData(Base64.getBase64(bpmTestCase.getBoFormData()));
				doNextParamObject.setTaskId(model.getTaskId());
				doNextParamObject.setDestination(destination);
				doNextParamObject.setOpinion("流程仿真测试");
				UserFacade userFacade = new UserFacade();
				userFacade.setAccount(doNextParamObject.getAccount());
				ContextUtil.setCurrentUser(ContextUtil.getUserByAccount(doNextParamObject.getAccount()));
				CommonResult<String> doNext = flowManager.complete(doNextParamObject);
				
				if(doNext.getState()){
					PublishAutoTestEventUtil.publishAutoTestEvent(instance.getId());
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			 ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
			 objectNode.put("id", instance.getId());
			 objectNode.put("opeName", "流程[ "+instance.getSubject()+" ]未正常结束： ");
			 objectNode.put("moduleType", "bpm-runtime");
			 objectNode.put("reqUrl", "");
			 objectNode.put("content", "在审批任务【"+model.getNodeName()+"】出现异常" +
					  ExceptionUtil.getExceptionMessage(e)); 
			 objectNode.put("type", "sysLog");
			 objectNode.put("logType", "异常日志");
			 String tenantId = HttpUtil.getTenantId();
			 if(BeanUtils.isEmpty(tenantId)) {
			 	tenantId = TenantConstant.PLATFORM_TENANT_ID;
			 }
			 objectNode.put("tenantId", tenantId);
			 JmsProducer jmsProducer = AppUtil.getBean(JmsProducer.class);
			 jmsProducer.sendToQueue(JsonUtil.toJson(objectNode),JmsConstant.SYS_LOG_QUEUE);
			
				
			}
			
			
			
		}
		
}
