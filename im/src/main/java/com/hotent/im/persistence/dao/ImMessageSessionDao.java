package com.hotent.im.persistence.dao;
import java.util.List;
import java.util.Map;

import com.hotent.base.dao.MyBatisDao;
import com.hotent.im.persistence.model.ImMessageSession;

/**
 * 
 * <pre> 
 * 描述：聊天会话 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:44:37
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface ImMessageSessionDao extends MyBatisDao<String, ImMessageSession> {

	List<ImMessageSession> getBySessionCode(String code);

	void updateLastText(String sessionCode, String text);

	List<ImMessageSession> getByCodeAndUser(Map<String, Object> params);

	List<ImMessageSession> getTeamSessionDetail(Map<String, Object> params);

	void updateImMessageSessionByKey(Map params);

	List<ImMessageSession> getByDynamicSql(Map<String, Object> params);

	void updateMessageArrived(Map param);

	void updateEarliestAsOwner(Map param);

	void updateSessionOwner(Map<String, Object> params);

	void updateTitle(Map<String, Object> params);

	void updateDescription(Map<String, Object> params);

	void updateIcon(Map<String, Object> params);

}
