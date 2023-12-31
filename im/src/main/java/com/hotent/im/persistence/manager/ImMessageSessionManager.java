package com.hotent.im.persistence.manager;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.manager.Manager;
import com.hotent.base.model.CommonResult;
import com.hotent.im.persistence.model.ImMessageSession;

/**
 * 
 * <pre> 
 * 描述：聊天会话 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:44:37
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface ImMessageSessionManager extends Manager<String, ImMessageSession>{

	ImMessageSession getBySessionCode(String code);

	ImMessageSession createP2PSession(String scene, String userStr) throws Exception;

	ImMessageSession createTeamSession(String scene, String userStr) throws Exception;

	void updateLastText(String sessionCode, String text);

	ImMessageSession getByCodeAndUser(String sessionCode, String currentUserId);

	ImMessageSession getTeamSessionDetail(String sessionCode);

	void updateTeamMessage(Map<String, Object> params);

	Map<String, Object> getSessionHistory(String sessionCode, String account) throws Exception;

	void updateMessageArrived(Map<String, Object> param);

	void updateEarliestAsOwner(String sessionCode);

	void updateSessionOwner(String sessionCode, String userAccount);

	CommonResult sendSystemMessage(Map<String,Object> params, String account);
}
