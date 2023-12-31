package com.hotent.runtime.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.runtime.dao.MessageTypeDao;
import com.hotent.runtime.manager.MessageTypeManager;
import com.hotent.runtime.model.MessageType;

/**
 * 
 * <pre> 
 * 描述：分类管理 处理实现类
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-15 18:35:27
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("messageTypeManager")
public class MessageTypeManagerImpl extends BaseManagerImpl<MessageTypeDao, MessageType> implements MessageTypeManager{
	
}
