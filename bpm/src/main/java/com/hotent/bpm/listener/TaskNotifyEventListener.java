package com.hotent.bpm.listener;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.NotifyTaskModel;
import com.hotent.bpm.api.event.TaskNotifyEvent;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.bpm.util.PortalDataUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

@Service
public class TaskNotifyEventListener implements  ApplicationListener<TaskNotifyEvent>,Ordered{
	private static final Log logger= LogFactory.getLog(TaskNotifyEventListener.class);
	@Resource
	IUserService userServiceImpl;

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public void onApplicationEvent(TaskNotifyEvent ev) {
		NotifyTaskModel model=(NotifyTaskModel) ev.getSource(); 

		ActionType actionType=model.getActionType();

		ActionCmd taskCmd= ContextThreadUtil.getActionCmd();

		//通知类型
		String notifyType = null;
		try {
			notifyType = BpmUtil.getNotifyType((BpmProcessInstance) taskCmd.getTransitVars(BpmConstants.PROCESS_INST),model.getNodeId());

			//是否配置了通知类型。
			if(StringUtil.isEmpty(notifyType)) return;

			//获取基础的URL
			String baseUrl=PortalDataUtil.getPropertyByAlias(TemplateConstants.TEMP_VAR.BASE_URL);

			model.addVars(TemplateConstants.TEMP_VAR.BASE_URL, baseUrl)
			.addVars(TemplateConstants.TEMP_VAR.TASK_SUBJECT, model.getSubject()) // 
			.addVars(TemplateConstants.TEMP_VAR.TASK_ID, model.getTaskId()) // 任务id
			.addVars(TemplateConstants.TEMP_VAR.CAUSE, model.getOpinion()) // 原因
			.addVars(TemplateConstants.TEMP_VAR.NODE_NAME, model.getNodeName())  // 节点名称
			.addVars(TemplateConstants.TEMP_VAR.AGENT, BeanUtils.isEmpty(model.getAgent())? "":model.getAgent().getFullname())// 代理人
			.addVars(TemplateConstants.TEMP_VAR.INST_SUBJECT,  model.getSubject())
			.addVars(TemplateConstants.TEMP_VAR.INST_ID, taskCmd.getInstId());
            DefaultBpmProcessInstance defaultBpmProcessInstance = (DefaultBpmProcessInstance) taskCmd.getTransitVars().get(BpmConstants.PROCESS_INST);
            if(BeanUtils.isNotEmpty(defaultBpmProcessInstance)){
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                model.addVars(TemplateConstants.TEMP_VAR.BPMNAME, defaultBpmProcessInstance.getProcDefName())
                     .addVars(TemplateConstants.TEMP_VAR.DATE, defaultBpmProcessInstance.getCreateTime().format(dateTimeFormatter))
                     .addVars(TemplateConstants.TEMP_VAR.CREATOR, defaultBpmProcessInstance.getCreator());
            }

			if(ActionType.APPROVE.equals( model.getActionType())){
				//代理
				if(model.isAgent()){
					handAgent(model,notifyType);
				}
				//普通审批
				else{
					MessageUtil.send(model,notifyType,TemplateConstants.TYPE_KEY.BPMN_APPROVAL);
				}
			}
			//驳回时
			else if(ActionType.BACK.equals(actionType) || ActionType.BACK_TO_START.equals(actionType)){
				MessageUtil.send(model,notifyType,TemplateConstants.TYPE_KEY.BPMN_BACK);
			}
			//撤销
			else{
				MessageUtil.send(model,notifyType,TemplateConstants.TYPE_KEY.BPMN_RECOVER);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 处理代理通知。
	 * @param model
	 * @param notifyType 
	 * void
	 */
	private void handAgent(NotifyTaskModel model,String notifyType){
		//代理人
		IUser agent=model.getAgent();
		IUser delegateUser=model.getDelegator();
		model.addVars("delegate", delegateUser.getFullname());
		model.addVars("agent", agent.getFullname());
		//发送给代理人。
		List<IUser> agentReceivers=new ArrayList<IUser>();
		agentReceivers.add(agent);
		model.setIdentitys(agentReceivers);
		
		try {
			MessageUtil.send(model, notifyType,TemplateConstants.TYPE_KEY.BPMN_AGENT);
			//发送给委托人
			List<IUser> delegateReceivers=new ArrayList<IUser>();
			delegateReceivers.add(delegateUser);
			model.setIdentitys(delegateReceivers);
			MessageUtil.send(model, notifyType,TemplateConstants.TYPE_KEY.BPMN_DELEGATE);
		}
		catch(Exception e) {
			logger.error(e);
		}
	}
}