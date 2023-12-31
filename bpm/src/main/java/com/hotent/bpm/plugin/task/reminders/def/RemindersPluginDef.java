package com.hotent.bpm.plugin.task.reminders.def;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.plugin.core.plugindef.AbstractBpmTaskPluginDef;
import com.hotent.bpm.plugin.task.reminders.entity.Reminders;

public class RemindersPluginDef extends AbstractBpmTaskPluginDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2787890606261585685L;
	private List<Reminder> ReminderList;
	
	
	public List<Reminder> getReminderList() {
		return ReminderList;
	}

	public void setReminderList(List<Reminder> reminderList) {
		ReminderList = reminderList;
	}
	
	
	public static RemindersPluginDef getReminders(Reminders remindersExt){
		List<com.hotent.bpm.plugin.task.reminders.entity.Reminder> reminderExtList = remindersExt.getReminder();
		if(BeanUtils.isEmpty(reminderExtList))return null;
		
		List<Reminder> reminderList = new ArrayList<Reminder>();
		for (com.hotent.bpm.plugin.task.reminders.entity.Reminder r : reminderExtList) {
			Reminder reminder = convertExt2Reminder(r);
			reminderList.add(reminder);
		}
		RemindersPluginDef def = new RemindersPluginDef();
		def.setReminderList(reminderList);
		return def;
	}
	
	public static com.hotent.bpm.plugin.task.reminders.entity.Reminders getReminderExt(RemindersPluginDef reminders){
		List<Reminder> reminderList = reminders.getReminderList();
		if(BeanUtils.isEmpty(reminderList))return null;
		
		List<com.hotent.bpm.plugin.task.reminders.entity.Reminder> reminderExtList = new ArrayList<com.hotent.bpm.plugin.task.reminders.entity.Reminder>();
		for (Reminder r : reminderList) {
			com.hotent.bpm.plugin.task.reminders.entity.Reminder reminder = convertReminder2Ext(r);
			reminderExtList.add(reminder);
		}
		com.hotent.bpm.plugin.task.reminders.entity.Reminders remindersExt = new com.hotent.bpm.plugin.task.reminders.entity.Reminders();
		remindersExt.setReminder(reminderExtList);
		return remindersExt;
	}
	
	
	public static Reminder convertExt2Reminder(com.hotent.bpm.plugin.task.reminders.entity.Reminder reminderExt){
		Reminder  reminder = new Reminder();
		reminder.setCondition(reminderExt.getCondition());
		reminder.setDateType(reminderExt.getDateType());
		reminder.setDueAction(reminderExt.getDueAction());
		reminder.setDueScript(reminderExt.getDueScript());
		reminder.setDueTime(reminderExt.getDueTime());
		reminder.setHtmlMsg(reminderExt.getHtmlMsg());
		reminder.setIsSendMsg(reminderExt.isSendMsg());
		reminder.setMsgCount(reminderExt.getMsgCount());
		reminder.setMsgType(reminderExt.getMsgType());
		reminder.setMsgInterval(reminderExt.getMsgInterval());
		reminder.setSendPerson(reminderExt.getSendPerson());
		reminder.setMsgSendTime(reminderExt.getMsgSendTime());
		reminder.setName(reminderExt.getName());
		reminder.setRelNodeId(reminderExt.getRelNodeId());
		reminder.setRelNodeEvent(reminderExt.getRelNodeEvent());
		reminder.setPlainMsg(reminderExt.getPlainMsg());
		
		if(BeanUtils.isEmpty(reminderExt.getWarningSet()))return reminder;
		
		List<WarningSet> warningSetList = new ArrayList<WarningSet>(); 
		for ( com.hotent.bpm.plugin.task.reminders.entity.WarningSet warningSetExt:reminderExt.getWarningSet()) {
			
			WarningSet warningSet = new WarningSet();
			warningSet.setLevel(warningSetExt.getLevel());
			warningSet.setWarnName(warningSetExt.getWarnName());
			warningSet.setWarnTime(warningSetExt.getWarnTime());
			warningSetList.add(warningSet);
		}
		reminder.setWarningSetList(warningSetList);
		return reminder;
	}
	
	
	public static com.hotent.bpm.plugin.task.reminders.entity.Reminder convertReminder2Ext(Reminder reminder){
		com.hotent.bpm.plugin.task.reminders.entity.Reminder reminderExt = new com.hotent.bpm.plugin.task.reminders.entity.Reminder();
		reminderExt.setCondition	(reminder.getCondition());
		reminderExt.setDateType		(reminder.getDateType());
		reminderExt.setDueAction	(reminder.getDueAction());
		reminderExt.setDueScript	(reminder.getDueScript());
		reminderExt.setDueTime		(reminder.getDueTime());
		reminderExt.setHtmlMsg		(reminder.getHtmlMsg());
		reminderExt.setSendMsg(reminder.getIsSendMsg());
		reminderExt.setMsgCount		(reminder.getMsgCount());
		reminderExt.setMsgType(reminder.getMsgType());
		reminderExt.setMsgInterval	(reminder.getMsgInterval());
		reminderExt.setSendPerson	(reminder.getSendPerson());
		reminderExt.setMsgSendTime	(reminder.getMsgSendTime());
		reminderExt.setName			(reminder.getName());
		reminderExt.setRelNodeId	(reminder.getRelNodeId());
		reminderExt.setRelNodeEvent	(reminder.getRelNodeEvent());
		reminderExt.setPlainMsg		(reminder.getPlainMsg());
		
		if(BeanUtils.isEmpty	(reminder.getWarningSetList()))return reminderExt;
		
		List<com.hotent.bpm.plugin.task.reminders.entity.WarningSet> warningSetExtList = new ArrayList<com.hotent.bpm.plugin.task.reminders.entity.WarningSet>(); 
		for ( WarningSet warningSet:reminder.getWarningSetList()) {
			com.hotent.bpm.plugin.task.reminders.entity.WarningSet warningSetExt = new com.hotent.bpm.plugin.task.reminders.entity.WarningSet();
			warningSetExt.setLevel(warningSet.getLevel());
			warningSetExt.setWarnName(warningSet.getWarnName());
			warningSetExt.setWarnTime(warningSet.getWarnTime());
			
			warningSetExtList.add(warningSetExt);
		}
		
		reminderExt.setWarningSet(warningSetExtList);
		return reminderExt;
	}
}
