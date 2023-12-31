package com.hotent.im.persistence.manager;

import java.util.List;

import com.hotent.base.manager.Manager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.im.persistence.model.ImMessageHistory;

/**
 * 
 * <pre> 
 * 描述：聊天消息历史表 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:52
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface ImMessageHistoryManager extends Manager<String, ImMessageHistory>{

	List<ImMessageHistory> sessionMessage(QueryFilter queryFilter);

	List<ImMessageHistory> getMsgHistory(QueryFilter filter);

	PageList<ImMessageHistory> queryHistory(QueryFilter queryFilter);

}
