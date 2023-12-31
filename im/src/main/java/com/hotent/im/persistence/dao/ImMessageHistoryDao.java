package com.hotent.im.persistence.dao;
import java.util.List;
import java.util.Map;

import com.hotent.base.dao.MyBatisDao;
import com.hotent.base.query.PageList;
import com.hotent.im.persistence.model.ImMessageHistory;

/**
 * 
 * <pre> 
 * 描述：聊天消息历史表 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:52
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface ImMessageHistoryDao extends MyBatisDao<String, ImMessageHistory> {

	List<ImMessageHistory> sessionMessage(Map<String, Object> param);

	List<ImMessageHistory> getMsgHistory(Map<String, Object> param);

	List<ImMessageHistory> queryHistory(Map<String, Object> param);
}
