package com.hotent.portal.persistence.manager;




import com.hotent.base.manager.BaseManager;
import com.hotent.portal.model.MessageReceiver;


/**
 * 系统信息接收处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author hugh
 * @email zxh@jee-soft.cn
 * @date 2018年6月21日
 */
public interface MessageReceiverManager extends BaseManager<MessageReceiver>{

	/*Map<String, String> getMsgType();*/
	
	/**
	 * 更新读取状态
	 * @param lAryId 信息id
	 */
	void updateReadStatus(String[] lAryId);

	
}
