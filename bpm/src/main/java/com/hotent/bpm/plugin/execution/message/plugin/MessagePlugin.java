package com.hotent.bpm.plugin.execution.message.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.jms.JmsActor;
import com.hotent.base.jms.Notice;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.template.impl.FreeMarkerEngine;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.plugin.core.def.BpmExecutionPluginDef;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.plugin.core.session.BpmExecutionPluginSession;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractBpmExecutionPlugin;
import com.hotent.bpm.plugin.core.util.UserAssignRuleQueryHelper;
import com.hotent.bpm.plugin.execution.message.context.IExternalData;
import com.hotent.bpm.plugin.execution.message.def.HtmlSetting;
import com.hotent.bpm.plugin.execution.message.def.MessagePluginDef;
import com.hotent.bpm.plugin.execution.message.def.PlainTextSetting;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

public class MessagePlugin extends AbstractBpmExecutionPlugin{
	
	private Log logger = LogFactory.getLog(GroovyScriptEngine.class);
	
	@Resource
	BpmPluginSessionFactory sessionFactory ;

	@Resource
	private FreeMarkerEngine FreeMarkerEngine;
	
	public Void execute(BpmExecutionPluginSession pluginSession,
			BpmExecutionPluginDef pluginDef) throws Exception {
		
		BpmDelegateExecution delegateExecution= pluginSession.getBpmDelegateExecution();
		
		String nodeName = delegateExecution.getNodeName();
		
		Map<String,Object> vars=delegateExecution.getVariables();
		
		vars.put("nodeName", nodeName);
		
		BpmUserCalcPluginSession bpmUserCalcPluginSession = sessionFactory.buildBpmUserCalcPluginSession(vars);
		
		MessagePluginDef messageDef=(MessagePluginDef)pluginDef;
		
		//处理流程变量
		handFlowVars(vars, delegateExecution);
		
		//处理变量数据。
		handData(messageDef, vars,delegateExecution);
		
		PlainTextSetting plainSetting=messageDef.getPlainTextSetting();
		
		HtmlSetting htmlSetting=messageDef.getHtmlSetting();
		
		if(plainSetting!=null){
			//查询要通知的用户
			List<BpmIdentity> notifyIdentities =UserAssignRuleQueryHelper.queryExtract(plainSetting.getRuleList(),bpmUserCalcPluginSession);
			List<IUser> receivers= queryAndConvert(notifyIdentities, pluginSession.getOrgEngine().getUserService());
			if(BeanUtils.isNotEmpty(receivers)) {				
				String content=plainSetting.getContent();
				content=parse(content,vars);
				String notifyType=plainSetting.getMsgType();
				send("",content,receivers,notifyType);
			}
			
		}
		
		if(htmlSetting!=null){
			//查询要通知的用户
			List<BpmIdentity> notifyIdentities =UserAssignRuleQueryHelper.queryExtract(htmlSetting.getRuleList(),bpmUserCalcPluginSession);
			List<IUser> receivers= queryAndConvert(notifyIdentities, pluginSession.getOrgEngine().getUserService());
			if(BeanUtils.isNotEmpty(receivers)) {
				String subject=htmlSetting.getSubject();
				String content=htmlSetting.getContent();
				subject=parse(subject,vars);
				content=parse(content,vars);
				String notifyType=htmlSetting.getMsgType();
				send(subject,content,receivers,notifyType);
			}
		}
		return null;
	}
	
	private  void send(String subject, String content, List<IUser> receivers, String notifyType) throws Exception{
		if(StringUtil.isEmpty(notifyType)) return;
		IUser currentUser = ContextUtil.getCurrentUser();
		
		NoticeMessageType[] messageTypes = MessageUtil.parseNotifyType(notifyType);
		String[] recieverAccounts = MessageUtil.parseAccountOfUser(receivers);
		Notice notice = new Notice();
		notice.setMessageTypes(messageTypes);
		notice.setSender(currentUser.getAccount());
		List<JmsActor> receiver = new ArrayList<JmsActor>();
		receiver = MessageUtil.parseJmsActor(receivers);
		notice.setReceiver(receiver );
		notice.setReceivers(recieverAccounts);
		notice.setSubject(subject);
		notice.setContent(content);
		PortalFeignService PortalFeignService = AppUtil.getBean(PortalFeignService.class);
		PortalFeignService.sendNoticeToQueue(notice);
	}
	
	/**
	 * 处理外部数据并添加到表单中。
	 * @param messageDef
	 * @param vars
	 * @param execution 
	 * void
	 */
	@SuppressWarnings("rawtypes")
	private void handData(MessagePluginDef messageDef,Map<String,Object> vars,BpmDelegateExecution execution){
		String externalClass=messageDef.getExternalClass();
		if(StringUtil.isEmpty(externalClass)) return;
		
		String instId=(String)vars.get(BpmConstants.PROCESS_INST_ID);
		String bpmnDefId=execution.getBpmnDefId();
		String bpmnInstId=execution.getBpmnInstId();
		String nodeId=execution.getNodeId();
		String executionId=execution.getId();
		
		try {
			Class cls= Class.forName(externalClass);
			IExternalData data=(IExternalData) cls.newInstance();
			Map<String,Object> varMap= data.getData(bpmnDefId,bpmnInstId, instId, nodeId, executionId);
			
			vars.putAll(varMap);
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage());
		} catch (InstantiationException e) {
			logger.debug(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.debug(e.getMessage());
		}
		
	}
	
	private String parse(String template,Object obj) throws Exception{
		return FreeMarkerEngine.parseByTemplate(template,obj);
	}
	
	private List<IUser> queryAndConvert(List<BpmIdentity> bpmIdentities,IUserService userService){
		List<IUser> userList = new ArrayList<IUser>();
		for(BpmIdentity bpmIdentity:bpmIdentities){
			IUser user = userService.getUserById(bpmIdentity.getId());
			userList.add(user);
		}
		return userList;
	}
	
	/**
	 * 加入流程变量
	 * @param vars
	 * @param execution
	 */
	private void handFlowVars(Map<String,Object> vars,BpmDelegateExecution execution){
		try {
			ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
			if(BeanUtils.isNotEmpty(taskCmd)){
				BpmProcessInstance instance= (BpmProcessInstance) taskCmd.getTransitVars(BpmConstants.PROCESS_INST);
				if(BeanUtils.isNotEmpty(instance)){
					String userId = instance.getCreateBy();
					if(BeanUtils.isNotEmpty(userId)){
						IUserService userService = AppUtil.getBean(IUserService.class);
						if(BeanUtils.isNotEmpty(userService)){
							IUser user = userService.getUserById(userId);
							if(BeanUtils.isNotEmpty(user)){
								vars.put("startorName", user.getFullname());
							}
						}
					}
					vars.put("startDate",DateFormatUtil.format(instance.getCreateTime(), StringPool.DATE_FORMAT_DATETIME));
					vars.put("businessKey", taskCmd.getBusinessKey());
				}
				
			}
			
			Map<String,ObjectNode> boMap=BpmContextUtil.getBoFromContext();
			if(BeanUtils.isNotEmpty(boMap)){
				Collection<ObjectNode> dataObjects = boMap.values();
				for (ObjectNode boData : dataObjects){
					ObjectNode bodef=(ObjectNode) boData.get("boDef");
					String boName=bodef.get("alias").asText();
					ObjectNode dataMap=  (ObjectNode) boData.get("data");
					for (Iterator<Entry<String, JsonNode>> iterator = dataMap.fields(); iterator.hasNext();) {
						Entry<String, JsonNode> entry = iterator.next();
						vars.put(boName +"_" + entry.getKey(),  entry.getValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
