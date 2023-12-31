package com.hotent.bpm.persistence.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmReminderHistoryDao;
import com.hotent.bpm.persistence.manager.BpmReminderHistoryManager;
import com.hotent.bpm.persistence.model.BpmReminderHistory;

/**
 * 
 * <pre> 
 * 描述：催办历史 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:miaojf	
 * 邮箱:miaojf@jee-soft.cn
 * 日期:2016-07-28 16:46:44
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmReminderHistoryManager")
public class BpmReminderHistoryManagerImpl extends BaseManagerImpl<BpmReminderHistoryDao, BpmReminderHistory> implements BpmReminderHistoryManager{

}
