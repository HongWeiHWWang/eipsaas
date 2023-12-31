package com.hotent.bpm.plugin.task.tasknotify.helper;

import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Service;

import com.hotent.base.jms.Notice;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.util.AppUtil;
import com.hotent.bpm.plugin.core.util.UserAssignRuleQueryHelper;
import com.hotent.bpm.plugin.task.tasknotify.def.model.NotifyItem;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.uc.api.model.IUser;
@Service
public class NotifyHelper {
	
	public void notify(NotifyItem notifyItem,String typeKey,Map<String,Object> vars) throws Exception{
		List<IUser> pluginUsers = UserAssignRuleQueryHelper.queryUsersWithExtract(notifyItem.getUserAssignRules(),vars);
		notify(pluginUsers, notifyItem.getMsgTypes(),typeKey , vars);
	}
	
	/**
	 * 向指定的接收人发送消息
	 * @param receiverUsers
	 * @param typeKey
	 * @param recevierType
	 * @param msgTypeKeys
	 * @param templateVars 
	 * void
	 */
	public void notify(List<IUser> receiverUsers,List<String> msgTypeKeys,String typeKey,Map<String,Object> vars) throws Exception{
		IUser sender = ContextUtil.getCurrentUser();
		NoticeMessageType[] messageTypes = MessageUtil.parseNotifyType(msgTypeKeys);
		String[] receivers = MessageUtil.parseAccountOfUser(receiverUsers);
		Notice notice = new Notice();
		//设置为使用模板
		notice.setUseTemplate(true);
		notice.setMessageTypes(messageTypes);
		notice.setSender(sender.getAccount());
		notice.setTemplateType(typeKey);
		notice.setVars(vars);
		notice.setReceivers(receivers);
		PortalFeignService PortalFeignService = AppUtil.getBean(PortalFeignService.class);
		PortalFeignService.sendNoticeToQueue(notice);
	}
}
