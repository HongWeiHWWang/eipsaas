package com.hotent.bpm.api.service;

import java.util.List;
import java.util.Map;

import com.hotent.base.groovy.IScript;
import com.hotent.bpm.api.constant.PrivilegeMode;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.uc.api.model.IUser;

/**
 * 会签节点服务。
 * 
 * <pre>
 *  
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-9-29-下午3:13:03
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface SignService extends IScript {

	/**
	 * 添加补签用户。
	 * 
	 * <pre>
	 * rtnMessage.addVariable("actTask", actTask);
	 *	rtnMessage.addVariable("users", users);
	 *	如果会签添加成功，可以获取添加的任务（actTask）和用户（users）。
	 * actTask ：为ActTask类的实例。
	 * users：为List&lt;{@linkplain BpmIdentity BpmIdentity} >类型。
	 * </pre>
	 * 
	 * @param taskId   bpm_task主键ID
	 * @param aryUsers 需要添加的会签用户数组。
	 * @return ResultMessage
	 * @throws Exception
	 */
	ResultMessage addSignTask(String taskId, String[] aryUsers) throws Exception;

	ResultMessage addCustomSignTask(String taskId, String[] aryUsers) throws Exception;

	/**
	 * 获取用户的特权列表。
	 * 
	 * @param userId    用户ID
	 * @param bpmnDefId 流程定义ID
	 * @param nodeId    节点ID
	 * @param variables 流程变量
	 * @return boolean
	 * @throws Exception
	 */
	List<PrivilegeMode> getPrivilege(String userId, SignNodeDef signNodeDef, Map<String, Object> variables)
			throws Exception;

	/**
	 * 发送通知
	 * 
	 * @param receiverUsers 收件人集合
	 * @param msgTypeKeys   消息类型
	 * @param typeKey       消息模板类型
	 * @param vars          变量
	 * @throws Exception
	 */
	void sendNotify(List<IUser> receiverUsers, List<String> msgTypeKeys, String typeKey, Map<String, Object> vars)
			throws Exception;
}
