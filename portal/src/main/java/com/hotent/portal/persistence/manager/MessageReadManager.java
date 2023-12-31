package com.hotent.portal.persistence.manager;



import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.portal.model.MessageRead;
import com.hotent.uc.api.model.IUser;


/**
 * 系统信息读取处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author hugh
 * @email zxh@jee-soft.cn
 * @date 2018年6月21日
 */
public interface MessageReadManager extends BaseManager<MessageRead>{
	
	/**
	 * 添加已读信息
	 * @param msgId 信息id
	 * @param currentUser 当前用户
	 */
	void addMessageRead(String msgId, IUser currentUser);
	
	/**
	 * 获取已读信息通过信息id
	 * @param messageId 信息id
	 * @return
	 */
	List<MessageRead> getByMessageId(String messageId);
	
}
