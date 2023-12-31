package com.hotent.portal.persistence.manager.impl;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.MessageRead;
import com.hotent.portal.persistence.dao.MessageReadDao;
import com.hotent.portal.persistence.manager.MessageReadManager;
import com.hotent.uc.api.model.IUser;

/**
 * 系统信息读取处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author hugh
 * @email zxh@jee-soft.cn
 * @date 2018年6月21日
 */
@Service("messageReadManager")
public class MessageReadManagerImpl extends BaseManagerImpl<MessageReadDao, MessageRead> implements MessageReadManager{
	
	@Override
	public void addMessageRead(String msgId, IUser sysUser) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", sysUser.getUserId());
		params.put("msgId", msgId);
		MessageRead msgRead = baseMapper.getReadByUser(params);
		if(msgRead==null){
			MessageRead messageRead = new MessageRead();
			messageRead.setId(UniqueIdUtil.getSuid());
			messageRead.setMsgId(msgId);
			messageRead.setReceiverId(sysUser.getUserId());
			messageRead.setReceiver(sysUser.getFullname());
			messageRead.setReceiverTime(LocalDateTime.now());
			this.create(messageRead);
		}
		
	}
	@Override
	public List<MessageRead> getByMessageId(String messageId) {
		return baseMapper.getByMessageId(messageId);
	}
}
