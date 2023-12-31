package com.hotent.portal.persistence.manager.impl;



import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.portal.model.MessageReceiver;
import com.hotent.portal.persistence.dao.MessageReceiverDao;
import com.hotent.portal.persistence.manager.MessageReadManager;
import com.hotent.portal.persistence.manager.MessageReceiverManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 系统信息处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月21日
 */
@Service("messageReceiverManager")
public class MessageReceiverManagerImpl extends BaseManagerImpl<MessageReceiverDao, MessageReceiver> implements MessageReceiverManager{
	@Resource
	MessageReadManager messageReadManager;
	
	@Override
	public void updateReadStatus(String[] lAryId) {
		if (lAryId.length==0) return;
		IUser currentUser = ContextUtil.getCurrentUser();
		for (String id :lAryId ){
			MessageReceiver messageReceiver = this.get(id);
			if (BeanUtils.isEmpty(messageReceiver)) continue;
			messageReadManager.addMessageRead(messageReceiver.getMsgId(),currentUser);
			
		}
	}
}
