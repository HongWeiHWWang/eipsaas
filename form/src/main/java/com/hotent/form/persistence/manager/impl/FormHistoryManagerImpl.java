package com.hotent.form.persistence.manager.impl;

import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.model.FormHistory;
import com.hotent.form.persistence.dao.FormHistoryDao;
import com.hotent.form.persistence.manager.FormHistoryManager;



/**
 * 程表单HTML设计历史记录
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("bpmFormHistoryManager")
public class FormHistoryManagerImpl extends BaseManagerImpl<FormHistoryDao, FormHistory> implements FormHistoryManager{
}
