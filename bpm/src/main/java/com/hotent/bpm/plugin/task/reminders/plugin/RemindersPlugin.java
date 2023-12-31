package com.hotent.bpm.plugin.task.reminders.plugin;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmTaskPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmTaskReminderManager;
import com.hotent.bpm.persistence.model.BpmTaskReminder;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.plugin.core.runtime.AbstractBpmTaskPlugin;
import com.hotent.bpm.plugin.task.reminders.def.Reminder;
import com.hotent.bpm.plugin.task.reminders.def.RemindersPluginDef;
import com.hotent.bpm.plugin.task.reminders.def.WarningSet;

/**
 * 催办节点插件运行时。
 * @author miaojf
 *
 */
public class RemindersPlugin extends AbstractBpmTaskPlugin{
	
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	BpmTaskReminderManager bpmTaskReminderManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	PortalFeignService PortalFeignService;

	@Override
	public Void execute(BpmTaskPluginSession pluginSession,BpmTaskPluginDef pluginDef) throws Exception {
		//任务结束事件删除催办项
		if(pluginSession.getEventType()==EventType.TASK_COMPLETE_EVENT){
				bpmTaskReminderManager.deleteByTaskId(pluginSession.getBpmDelegateTask().getId());
				return null;
		}

		RemindersPluginDef reminderDef = (RemindersPluginDef) pluginDef;
		List<Reminder>  reminderList = reminderDef.getReminderList();
		for (Reminder reminder : reminderList) {
			createRminder(reminder,pluginSession);
		}
		
		return null;
	}
	
	
	//创建一条催办项
	private void createRminder(Reminder reminder, BpmTaskPluginSession pluginSession) throws Exception {
		BpmDelegateTask task =pluginSession.getBpmDelegateTask();
		String condition = reminder.getCondition();
		//如果条件不通过则跳过
		if(StringUtil.isNotEmpty(condition)){
			Object object=	executeScript(pluginSession, condition);
				if(object instanceof Boolean) {
					if(!((Boolean) object)) return;
				}
		}
		
		BpmTaskReminder taskReminder =  new BpmTaskReminder();
		taskReminder.setName(reminder.getName());
		taskReminder.setTaskId(task.getId());
		taskReminder.setDueAction(reminder.getDueAction());
		taskReminder.setDueScript(reminder.getDueScript());
		
		Boolean isSendMsg= reminder.getIsSendMsg();
		int sendMsg=isSendMsg?1:0;
		
		taskReminder.setIsSendMsg(sendMsg);
		if(sendMsg==1){
			taskReminder.setHtmlMsg(reminder.getHtmlMsg());
			taskReminder.setPlainMsg(reminder.getPlainMsg());
			taskReminder.setMsgCount(reminder.getMsgCount());
			taskReminder.setMsgType(reminder.getMsgType());
			taskReminder.setMsgInterval(reminder.getMsgInterval());
			taskReminder.setSendPerson(reminder.getSendPerson());
		}
		
		boolean calcSuccess = calcReminderDates(task,reminder,taskReminder);
		if(!calcSuccess) return;
		
		bpmTaskReminderManager.create(taskReminder);
	}
	
	
	// 计算任务到期时间，催办开始时间，预警开始时间
	private boolean calcReminderDates(BpmDelegateTask task,Reminder reminder, BpmTaskReminder taskReminder) throws Exception {
		LocalDateTime relDate = null ;
		String relNodeId = reminder.getRelNodeId();
		String relNodeEvent =reminder.getRelNodeEvent();
		boolean isRelCreateEvent = Reminder.TASK_EVENT_CREATE.equals(relNodeEvent);
		
		/**获取相对时间,如果当前节点与相对节点相同则不需要从历史意见中获取*/
		if(task.getTaskDefinitionKey().equals(relNodeId)&& isRelCreateEvent){
			relDate = task.getCreateTime();
		}else{
			List<DefaultBpmCheckOpinion> dpcOpinions = bpmCheckOpinionManager.getByInstNodeId(ContextThreadUtil.getActionCmd().getInstId(),relNodeId);
			if(BeanUtils.isNotEmpty(dpcOpinions)){
				DefaultBpmCheckOpinion dpcOpinion = dpcOpinions.get(dpcOpinions.size()-1);
				if (isRelCreateEvent) {
					relDate = dpcOpinion.getCreateTime();
				} else {
					relDate = dpcOpinion.getCompleteTime();
				}
			}else{
				throw new RuntimeException("催办插件相对节点尚未处理。计算相对时间出现异常！");
			}
		}
		//发起流程的时候为了避免用户任务1自动完成排序会在发起流程的审批记录前面，所以用户任务1的完成时间被手动的加1秒。此次加2秒比较，以免抛异常
		if(relDate== null || (TimeUtil.getTimeMillis(relDate) > (TimeUtil.getTimeMillis(LocalDateTime.now())+2000))){
			throw new RuntimeException("催办插件相对时间计算出现异常");
		}

		/**计算到期时间，   日历日和工作日*/
		LocalDateTime dueDate ,msgBeginDate = null;
		if(Reminder.TASK_TIME_TYPE_CALTIME.equals(reminder.getDateType())){
			//催办过期时间
			dueDate = TimeUtil.getLocalDateTimeByMills(TimeUtil.getNextTime(TimeUtil.MINUTE, reminder.getDueTime(),TimeUtil.getTimeMillis(relDate)));
			//如果发送催办消息
			if(reminder.getIsSendMsg()){
				msgBeginDate = TimeUtil.getLocalDateTimeByMills(TimeUtil.getNextTime(TimeUtil.MINUTE, reminder.getMsgSendTime(),TimeUtil.getTimeMillis(relDate)));
			}
		}else{
			// 获取第一个用户的工作日历
			//如果用户为空。我们不去执行催办
			List<BpmIdentity> list =task.getExecutors();
			if(BeanUtils.isNotEmpty(list)){
				ObjectNode params=JsonUtil.getMapper().createObjectNode();
				params.put("userId", list.get(0).getId());
				if(BeanUtils.isNotEmpty(relDate)){
					params.put("startTime", DateFormatUtil.formaDatetTime(relDate));
				}
				params.put("time", reminder.getDueTime());
				String dueDateStr = PortalFeignService.getEndTimeByUser(params);
				dueDate = DateFormatUtil.parse(dueDateStr);
			}else{
				return false;
			}
			
			if(reminder.getIsSendMsg()){
				msgBeginDate =TimeUtil.getLocalDateTimeByMills(TimeUtil.getNextTime(TimeUtil.MINUTE, reminder.getMsgSendTime(),TimeUtil.getTimeMillis(relDate)));
			}
		}
		//到期时间，和发送短信时间
		taskReminder.setDueDate(dueDate);
		taskReminder.setTriggerDate(dueDate);
		if(reminder.getIsSendMsg()){
			taskReminder.setMsgSendDate(msgBeginDate);
			//如果发消息时间更早。则发消息时间为下次triggerDate
			if(msgBeginDate.isBefore(dueDate))taskReminder.setTriggerDate(msgBeginDate);
		}
		
		if(BeanUtils.isNotEmpty(reminder.getWarningSetList()))
			getWarningSet(reminder,task.getExecutors(),relDate,taskReminder);
		
		taskReminder.setRelDate(relDate);
		return true;
	}
	
	
	// 处理warningSet
	private void getWarningSet(Reminder reminder, List<BpmIdentity> executors, LocalDateTime relDate, BpmTaskReminder taskReminder) throws Exception {
		ArrayNode warings = JsonUtil.getMapper().createArrayNode();
		
		for (WarningSet waringSet : reminder.getWarningSetList()) {
			ObjectNode jsonObject = (ObjectNode) JsonUtil.toJsonNode(waringSet);
			LocalDateTime warnDate=null;
			int warnTime = jsonObject.get("warnTime").asInt();
			if(Reminder.TASK_TIME_TYPE_CALTIME.equals(reminder.getDateType())){
				warnDate = TimeUtil.getLocalDateTimeByMills(TimeUtil.getNextTime(TimeUtil.MINUTE, warnTime,TimeUtil.getTimeMillis(relDate)));
			}else{
				ObjectNode params=JsonUtil.getMapper().createObjectNode();
				params.put("userId", executors.get(0).getId());
				if(BeanUtils.isNotEmpty(relDate)){
					params.put("startTime", DateFormatUtil.formaDatetTime(relDate));
				}
				params.put("time", warnTime);
				String warnDateDateStr = PortalFeignService.getEndTimeByUser(params);
				warnDate = DateFormatUtil.parse(warnDateDateStr);
			}
			// 修改最早triggerDate
			if(taskReminder.getTriggerDate()==null || warnDate.isBefore(taskReminder.getTriggerDate())){
				taskReminder.setTriggerDate(warnDate);
			}
			
			jsonObject.put("warnDate", TimeUtil.getDateTimeString(warnDate));
			warings.add(jsonObject);
		}
		
		taskReminder.setWarningset(warings.toString());
	}

	//执行脚本
	private Object executeScript(BpmTaskPluginSession pluginSession,String script){
		Map<String, Object> vars=new HashMap<String, Object>();
		vars.putAll( pluginSession.getBpmDelegateTask().getVariables());
		
		Map<String,ObjectNode> boDatas= BpmContextUtil.getBoFromContext();
		vars.putAll(boDatas);
		
		return groovyScriptEngine.executeObject(script, vars);
	}

}
