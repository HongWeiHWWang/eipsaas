package com.hotent.bpm.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.jms.JmsActor;
import com.hotent.base.jms.JmsProducer;
import com.hotent.base.jms.Notice;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.event.NodeNotifyModel;
import com.hotent.bpm.api.event.NotifyTaskModel;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

import freemarker.template.TemplateException;

public class MessageUtil {
	
	/**
	 * 获取通知支持的消息类型
	 * <pre>
	 * 按照key,label的方式返回，例如：{"inner":"内部消息","mail":"邮件"}
	 * </pre>
	 * @return	通知消息类型
	 */
	public static Map<String, String> getHandlerTypes(){
		Map<String, String> map = new HashMap<>();
		EnumSet<NoticeMessageType> it = EnumSet.allOf(NoticeMessageType.class);
        for (NoticeMessageType type : it) {
        	map.put(type.key(), type.label());
        }
        return map;
	}
	
	/**
	 * 判断通知类型是否支持html。
	 * @param notifyType	通知类型为字符串。
	 * @return
	 */
	public static boolean isSupportHtml(String notifyType){
		EnumSet<NoticeMessageType> it = EnumSet.allOf(NoticeMessageType.class);
        for (NoticeMessageType type : it) {
        	if(type.key().equals(notifyType)) {
        		return !type.isPlain();
        	}
        }
		return false;
	}
	
	/**
	 * 解析通知消息类型为枚举数组
	 * @param nofifyTypes	通知消息类型key集合
	 * @return				通知消息类型枚举数组
	 */
	public static NoticeMessageType[] parseNotifyType(List<String> nofifyTypes) {
		List<NoticeMessageType> messageTypeArrays = new ArrayList<>();
		EnumSet<NoticeMessageType> it = EnumSet.allOf(NoticeMessageType.class);
        for (NoticeMessageType type : it) {
            if(nofifyTypes.indexOf(type.key()) > -1) {
            	messageTypeArrays.add(type);
            }
        }
        NoticeMessageType[] messageTypes = new NoticeMessageType[messageTypeArrays.size()];
        messageTypeArrays.toArray(messageTypes);
        return messageTypes;
	}
	
	/**
	 * 解析通知消息类型为枚举数组
	 * @param notifyType	通知消息类型(多个类型之间用逗号分隔)
	 * @return				通知消息类型枚举数组
	 */
	public static NoticeMessageType[] parseNotifyType(String notifyType) {
		if(notifyType==null) {
			notifyType= "";
		}
		String[] arys = notifyType.split(",");
		List<String> list = new ArrayList<String>(Arrays.asList(arys));
		return parseNotifyType(list);
	}
	
	/**
	 * 解析账号数组
	 * <pre>
	 * 将用户集合中各用户的账号取出来构建为一个字符串数组
	 * </pre>
	 * @param users	用户集合
	 * @return		账号数组
	 */
	public static String[] parseAccountOfUser(List<IUser> users) {
		List<String> receiverAccounts = new ArrayList<>();
		for(IUser receiver : users) {
			if(StringUtil.isEmpty(receiver.getAccount())&&StringUtil.isNotEmpty(receiver.getUserId())){
				receiverAccounts.add(receiver.getUserId());
			}else{
				receiverAccounts.add(receiver.getAccount());
			}
		}
		String[] receivers = new String[receiverAccounts.size()];
		receiverAccounts.toArray(receivers);
		return receivers;
	}
	
	/**
	 * 发送通知
	 * @param bpmTask
	 * @param opinion
	 * @param receiver
	 * @param notifyType
	 * @param typeKey 
	 * void
	 */
	public static void notify(DefaultBpmTask bpmTask,String opinion,IUser receiver,String notifyType,String typeKey) throws Exception {
		NotifyTaskModel model=new NotifyTaskModel();
		String baseUrl=PortalDataUtil.getPropertyByAlias(TemplateConstants.TEMP_VAR.BASE_URL);
		model.addVars(TemplateConstants.TEMP_VAR.BASE_URL, baseUrl)
		.addVars(TemplateConstants.TEMP_VAR.TASK_SUBJECT, bpmTask.getSubject())
		.addVars(TemplateConstants.TEMP_VAR.INST_SUBJECT, bpmTask.getSubject())
		.addVars(TemplateConstants.TEMP_VAR.TASK_ID, bpmTask.getId())
		.addVars(TemplateConstants.TEMP_VAR.NODE_NAME, bpmTask.getName())
		.addVars(TemplateConstants.TEMP_VAR.CAUSE, opinion)
		.addVars(TemplateConstants.TEMP_VAR.NODE_NAME, bpmTask.getName())
		.addVars(TemplateConstants.TEMP_VAR.RECEIVERID,receiver.getUserId())
		.addVars(TemplateConstants.TEMP_VAR.RECEIVER,receiver.getFullname());
		
		List<IUser> identitys = new ArrayList<IUser>();
		identitys.add(receiver);
		model.setIdentitys(identitys);
		
		MessageUtil.send(model, notifyType, typeKey);
		
	}
	
	/**
	 * 发送通知消息。
	 * @param model
	 * @param notifyType
	 * @param typeKey 
	 * void
	 */
	public static void send(NotifyTaskModel model,String notifyType,String typeKey) throws Exception{
		List<IUser> userList= model.getIdentitys();
		sendMsg(typeKey,notifyType,userList,model.getVars());
	}

	public static void sendMsg(NodeNotifyModel model,String notifyType,String typeKey) throws Exception{
		NoticeMessageType[] messageTypes = parseNotifyType(notifyType);
		List<JmsActor> recieverAccounts = model.getJmsActors();

		IUser sender = ContextUtil.getCurrentUser();
		Map<String, Object> vars = model.getVars();

		vars.put(TemplateConstants.TEMP_VAR.SENDER, BeanUtils.isNotEmpty(sender)?sender.getFullname():"系统执行人");
		vars.put(TemplateConstants.TEMP_VAR.SENDERID, BeanUtils.isNotEmpty(sender)?sender.getUserId():"-1");
		if(recieverAccounts.size() == 1) vars.put(TemplateConstants.TEMP_VAR.RECEIVER,recieverAccounts.get(0).getAccount());

		Notice notice = new Notice();
		// 设置为使用模板
		notice.setSubject(model.getSubject());
		notice.setContent(model.getContent());
		notice.setUseTemplate(false);
		notice.setMessageTypes(messageTypes);
		notice.setSender(sender.getAccount());
		notice.setTemplateType(typeKey);
		notice.setVars(vars);
		notice.setReceiver(recieverAccounts);
		JmsProducer jmsProducer = AppUtil.getBean(JmsProducer.class);
		jmsProducer.sendToQueue(notice);
	}
	
	/**
	 * 发送通知消息
	 * @param typeKey			模版类型
	 * @param notifyType		通知类型
	 * @param recievers			接收人
	 * @param vars				变量
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static void sendMsg(String typeKey,String notifyType, List<IUser> recievers,Map<String, Object> vars) throws Exception{
		NoticeMessageType[] messageTypes = parseNotifyType(notifyType);
		List<JmsActor> recieverAccounts = parseJmsActor(recievers);
		
		IUser sender = ContextUtil.getCurrentUser();
		
		vars.put(TemplateConstants.TEMP_VAR.SENDER, BeanUtils.isNotEmpty(sender)?sender.getFullname():"系统执行人"); 
		vars.put(TemplateConstants.TEMP_VAR.SENDERID, BeanUtils.isNotEmpty(sender)?sender.getUserId():"-1");
		if(recievers.size() == 1) vars.put(TemplateConstants.TEMP_VAR.RECEIVER,recievers.get(0).getFullname()); 
		
		Notice notice = new Notice();
		// 设置为使用模板
		notice.setUseTemplate(true);
		notice.setMessageTypes(messageTypes);
		notice.setSender(sender.getAccount());
		notice.setTemplateType(typeKey);
		notice.setVars(vars);
		notice.setReceiver(recieverAccounts);
		JmsProducer jmsProducer = AppUtil.getBean(JmsProducer.class);
		jmsProducer.sendToQueue(notice);
	}

	/**
	 * IUser 转换为 JmsActor
	 * @param users
	 * @return
	 */
	public static List<JmsActor> parseJmsActor(List<IUser> users){
		List<JmsActor> actors = new ArrayList<>();
		for (IUser user:users){
			JmsActor actor = new JmsActor();
			actor.setId(user.getUserId());
			actor.setAccount(user.getAccount());
			actor.setEmail(user.getEmail());
			actor.setMobile(user.getMobile());
			actor.setWeixin(user.getWeixin());
			actors.add(actor);
		}
		return actors;
	}

    /**
     * 发送通知消息
     * @param typeKey			模版类型
     * @param notifyType		通知类型
     * @param recievers			接收人
     * @param vars				变量
     * @throws TemplateException
     * @throws IOException
     */
    public static void sendMsgnew(String notifyType, List<IUser> recievers,String subject,String content) throws Exception{
        NoticeMessageType[] messageTypes = parseNotifyType(notifyType);
        List<JmsActor> recieverAccounts = parseJmsActor(recievers);

        IUser sender = ContextUtil.getCurrentUser();

        Notice notice = new Notice();
        // 设置为使用模板
        notice.setUseTemplate(true);
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setMessageTypes(messageTypes);
        notice.setSender(sender.getAccount());
        notice.setReceiver(recieverAccounts);
		JmsProducer jmsProducer = AppUtil.getBean(JmsProducer.class);
		jmsProducer.sendToQueue(notice);
    }
}
