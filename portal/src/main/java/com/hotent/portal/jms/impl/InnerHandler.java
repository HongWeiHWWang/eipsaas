package com.hotent.portal.jms.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.jms.JmsActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.util.BeanUtils;
import com.hotent.portal.jms.JmsHandler;
import com.hotent.portal.model.SysExecutor;
import com.hotent.portal.persistence.manager.MessageLogManager;
import com.hotent.portal.persistence.manager.SysMessageManager;

/**
 * 内部消息处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
@Service
public class InnerHandler implements JmsHandler {
	private static final Logger logger = LoggerFactory.getLogger(InnerHandler.class);
	@Resource
	SysMessageManager sysMessageManager;
	@Resource
	MessageLogManager messageLogManager;
	
	@Override
	public String getType() {
		//return NoticeMessageType.INNER.key();
        return null;
	}

	@Override
	public boolean send(JmsMessage jmsMessage) {
		try {
			//处理消息
			List<JmsActor> receivers = jmsMessage.getReceivers();
			//构造接收人
			List<SysExecutor> list = new ArrayList<SysExecutor>();
			for(JmsActor actor : receivers){
				SysExecutor executor = new SysExecutor(actor.getId(), actor.getName(), SysExecutor.TYPE_USER);
				list.add(executor);
			}
			if(BeanUtils.isEmpty(list)) return false;
			
			String senderId = null;
			String senderName = null;
			JmsActor sender = jmsMessage.getSender();
			if(BeanUtils.isNotEmpty(sender)) {
				senderId = sender.getId();
				senderName = sender.getName();
			}
			sysMessageManager.sendMsg(jmsMessage.getSubject(), jmsMessage.getContent(), jmsMessage.getType(), senderId, senderName, list);
			messageLogManager.handLogByMsgHander(jmsMessage, true, "");
			return true;
		}
		catch(Exception ex) {
			logger.error(ex.getMessage());
			messageLogManager.handLogByMsgHander(jmsMessage, false, ex.getMessage());
			return false;
		}
	}

	@Override
	public String getTitle() {
		return "内部消息";
	}

	@Override
	public boolean getIsDefault() {
		return true;
	}

	@Override
	public boolean getSupportHtml() {
		return false;
	}
}