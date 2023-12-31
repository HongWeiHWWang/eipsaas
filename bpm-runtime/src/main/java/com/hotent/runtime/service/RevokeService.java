package com.hotent.runtime.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.persistence.manager.BpmReadRecordManager;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.runtime.params.CustomSignRevokeParam;
import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * 撤回服务接口
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年3月11日
 */
public interface RevokeService {
	/**
	 * 根据是否允许已阅撤回来设置撤回属性
	 * @param map
	 * @param taskId
	 * @param isReadRevoke
	 * @param bpmReadRecordManager
	 */
	default void setRevoke(Map<String, Object> map, String taskId, String isReadRevoke, BpmReadRecordManager bpmReadRecordManager) {
		// 不允许已阅撤回
		if("false".equals(isReadRevoke)) {
			// 直接后代任务是否已阅
			Boolean taskReadByOwner = bpmReadRecordManager.isTaskReadByOwner(taskId);
			map.put("revoke", !taskReadByOwner);
		}
		// 允许已阅撤回时无需查询是否已阅
		else {
			map.put("revoke", true);
		}
	}

	/**
	 *  在审批记录列表中，过滤出指定signType的记录
	 * @param list
	 * @param signType
	 * @return
	 */
	default List<DefaultBpmCheckOpinion> getOpinionBySignType(List<DefaultBpmCheckOpinion> list, String signType) {
		List<DefaultBpmCheckOpinion> newList = new ArrayList<>();
		if(BeanUtils.isEmpty(list) || StringUtil.isEmpty(signType)) {
			return newList;
		}
		for(DefaultBpmCheckOpinion opinion : list) {
			if(signType.equals(opinion.getSignType())) {
				newList.add(opinion);
			}
		}
		return newList;
	}

	/**
	 *  从审批记录列表中获取指定taskId的那条记录
	 * @param list
	 * @param taskId
	 * @return
	 */
	default DefaultBpmCheckOpinion getOpinionByTaskId(List<DefaultBpmCheckOpinion> list, String taskId) {
		if(BeanUtils.isEmpty(list) || StringUtil.isEmpty(taskId)) {
			return null;
		}
		for(DefaultBpmCheckOpinion opinion : list) {
			if(taskId.equals(opinion.getTaskId())) {
				return opinion;
			}
		}
		return null;
	}
	
	default String getNewCreateTaskId() {
		String createTaskId = null;
		Object commuVar = ContextThreadUtil.getCommuVar(BpmConstants.CREATE_BPM_TASK+ContextUtil.getCurrentUserId(), null);
		if(BeanUtils.isNotEmpty(commuVar)) {
			createTaskId = ((BpmTask)commuVar).getId();
		}
		return createTaskId;
	};
	
	/**
	 * 判断审批记录列表中是否至少有一条为指定状态
	 * @param list
	 * @param opinionStatusList
	 * @return
	 */
	default Boolean hasOpinionWithStatus(List<DefaultBpmCheckOpinion> list, List<String> opinionStatusList) {
		Boolean result = false;
		if(BeanUtils.isEmpty(list) || BeanUtils.isEmpty(opinionStatusList)) {
			return result;
		}
		for(DefaultBpmCheckOpinion opinion : list) {
			if(opinionStatusList.contains(opinion.getStatus())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 判断已办是否可以撤回
	 * @param map			已办
	 * @param list			审批记录
	 * @param isReadRevoke	是否允许已阅撤回
	 */
	void canRevoke(Map<String, Object> map, List<DefaultBpmCheckOpinion> list, String isReadRevoke);

	/**
	 * 执行撤回操作
	 * @param revokeParamObject 撤回操作参数
	 */
	void doRevoke(CustomSignRevokeParam revokeParam) throws Exception;
}
