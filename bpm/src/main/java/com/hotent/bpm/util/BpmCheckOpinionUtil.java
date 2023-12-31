package com.hotent.bpm.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.model.ActTask;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;

public class BpmCheckOpinionUtil {
	
	public static final List<String> statusList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(OpinionStatus.AGREE.getKey());
			add(OpinionStatus.OPPOSE.getKey());
			add(OpinionStatus.SKIP.getKey());
			add(OpinionStatus.REJECT.getKey());
			add(OpinionStatus.DELIVERTO_AGREE.getKey());
			add(OpinionStatus.DELIVERTO_OPPOSE.getKey());
			add(OpinionStatus.BACK_TO_START.getKey());
		}
	};

	public static DefaultBpmCheckOpinion buildBpmCheckOpinion(BpmDelegateTask delegateTask, String procInstId) {
		DefaultBpmCheckOpinion bpmCheckOpinion = new DefaultBpmCheckOpinion();
		bpmCheckOpinion.setProcDefId(delegateTask.getBpmnDefId());
		String superInstId = (String) delegateTask.getSupperVariable(BpmConstants.PROCESS_INST_ID);
		if (StringUtil.isEmpty(superInstId)) {
			superInstId = "0";
		}
		bpmCheckOpinion.setSupInstId(superInstId);
		bpmCheckOpinion.setProcInstId(procInstId);
		bpmCheckOpinion.setTaskId(delegateTask.getId());
		bpmCheckOpinion.setTaskKey(delegateTask.getTaskDefinitionKey());
		bpmCheckOpinion.setTaskName(delegateTask.getName());
		bpmCheckOpinion.setStatus(OpinionStatus.AWAITING_CHECK.getKey());
		bpmCheckOpinion.setCreateTime(delegateTask.getCreateTime());

		return bpmCheckOpinion;
	}

	/**
	 * 获取有资格审批人Json数据。
	 * 
	 * <pre>
	 * 	数据格式如下:
	 *  [{id:"",type:""},{id:"",type:""}]
	 * </pre>
	 * 
	 * @param identityList
	 * @return String
	 */
	public static String getIdentityIds(List<BpmIdentity> identityList) {
		if (BeanUtils.isEmpty(identityList))
			return "";
		ArrayNode identityArray = JsonUtil.getMapper().createArrayNode();
		for (int i = 0; i < identityList.size(); i++) {
			BpmIdentity identity = identityList.get(i);
			ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
			objectNode.put("id", identity.getId());
			objectNode.put("type", identity.getType());
			objectNode.put("groupType", identity.getGroupType());
			objectNode.put("name", identity.getName());
			identityArray.add(objectNode);
		}
		try {
			return JsonUtil.toJson(identityArray);
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 * 获取有资格审批人的名字,以逗号隔开。
	 * 
	 * @param identityList
	 * @return String
	 */
	public static String getIdentityNames(List<BpmIdentity> identityList) {
		if (BeanUtils.isEmpty(identityList))
			return "";

		StringBuffer names = new StringBuffer();
		for (BpmIdentity bpmIdentity : identityList) {
			names.append(bpmIdentity.getName());
			names.append(",");
		}
		String result = names.toString();
		return result.substring(0, result.length() - 1);

	}

	public static DefaultBpmCheckOpinion buildBpmCheckOpinion(ActTask actTask, String superInstId, String procInstId) {
		IUser user = ContextUtil.getCurrentUser();
		String id = UniqueIdUtil.getSuid();

		List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setType(IdentityType.USER);
		bpmIdentity.setId(user.getUserId());
		bpmIdentity.setName(user.getFullname());
		identityList.add(bpmIdentity);

		DefaultBpmCheckOpinion bpmCheckOpinion = new DefaultBpmCheckOpinion();
		bpmCheckOpinion.setId(id);
		bpmCheckOpinion.setProcDefId(actTask.getProcDefId());
		bpmCheckOpinion.setSupInstId(superInstId);
		bpmCheckOpinion.setProcInstId(procInstId);
		bpmCheckOpinion.setTaskId(actTask.getId());
		bpmCheckOpinion.setTaskKey(actTask.getTaskDefKey());
		bpmCheckOpinion.setTaskName(actTask.getName());
		bpmCheckOpinion.setStatus(OpinionStatus.AWAITING_CHECK.getKey());
		bpmCheckOpinion.setCreateTime(actTask.getCreateTime());
		bpmCheckOpinion.setQualfieds(getIdentityIds(identityList));
		bpmCheckOpinion.setQualfiedNames(user.getFullname());

		return bpmCheckOpinion;
	}

	/**
	 * 获取发起节点或结束结点的审核意见
	 * 
	 * @param delegateExecution
	 * @param procInstId
	 * @return
	 */
	public static DefaultBpmCheckOpinion buildBpmCheckOpinion(BpmDelegateExecution delegateExecution, String procInstId,
			Boolean isEndNote) {
		IUser user = ContextUtil.getCurrentUser();
		List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setType(IdentityType.USER);
		bpmIdentity.setId(user.getUserId());
		bpmIdentity.setName(user.getFullname());
		identityList.add(bpmIdentity);

		DefaultBpmCheckOpinion bpmCheckOpinion = new DefaultBpmCheckOpinion();
		bpmCheckOpinion.setProcDefId(delegateExecution.getBpmnDefId());
		String superInstId = (String) delegateExecution.getSupperVariable(BpmConstants.PROCESS_INST_ID);
		bpmCheckOpinion.setSupInstId(superInstId);
		bpmCheckOpinion.setProcInstId(procInstId);
		bpmCheckOpinion.setTaskId(null);
		bpmCheckOpinion.setTaskKey(delegateExecution.getNodeId());
		bpmCheckOpinion.setTaskName("发起节点");
		bpmCheckOpinion.setIsRead(1);
		if (isEndNote) {
			// 对于从一个开始节点出发，可能会走向不同的结束节点，故需要记录具体的结束节点
			bpmCheckOpinion.setTaskKey(delegateExecution.getNodeId());
			bpmCheckOpinion.setTaskName(delegateExecution.getNodeName());
			bpmCheckOpinion.setStatus("end");
			bpmCheckOpinion.setOpinion("结束流程");
		}
		bpmCheckOpinion.setCreateTime(LocalDateTime.now());
		bpmCheckOpinion.setCompleteTime(bpmCheckOpinion.getCreateTime());

		bpmCheckOpinion.setQualfieds(getIdentityIds(identityList));
		bpmCheckOpinion.setQualfiedNames(user.getFullname());
		bpmCheckOpinion.setAuditor(user.getUserId());
		bpmCheckOpinion.setAuditorName(user.getFullname());
		BpmUtil.setOpinionOrgInfo(user.getUserId(), bpmCheckOpinion);
		bpmCheckOpinion.setDurMs(0L);
		return bpmCheckOpinion;
	}

	/**
	 * 获取发起节点或结束结点的审核意见
	 * 
	 * @param delegateExecution
	 * @param procInstId
	 * @return
	 */
	public static void updateExtraPropCheckOpinion(DefaultBpmCheckOpinion bpmCheckOpinion, DefaultBpmTask bpmTask) {
		if (BeanUtils.isEmpty(bpmCheckOpinion) || BeanUtils.isEmpty(bpmTask)) {
			return;
		}
		boolean isUpdate = false;
		if (StringUtil.isNotEmpty(bpmTask.getProp1())) {
			isUpdate = true;
			bpmCheckOpinion.setProp1(bpmTask.getProp1());
		}
		if (StringUtil.isNotEmpty(bpmTask.getProp2())) {
			isUpdate = true;
			bpmCheckOpinion.setProp2(bpmTask.getProp2());
		}
		if (StringUtil.isNotEmpty(bpmTask.getProp3())) {
			isUpdate = true;
			bpmCheckOpinion.setProp3(bpmTask.getProp3());
		}
		if (StringUtil.isNotEmpty(bpmTask.getProp4())) {
			isUpdate = true;
			bpmCheckOpinion.setProp4(bpmTask.getProp4());
		}
		if (StringUtil.isNotEmpty(bpmTask.getProp5())) {
			isUpdate = true;
			bpmCheckOpinion.setProp5(bpmTask.getProp5());
		}
		if (StringUtil.isNotEmpty(bpmTask.getProp6())) {
			isUpdate = true;
			bpmCheckOpinion.setProp6(bpmTask.getProp6());
		}
		if (isUpdate) {
			BpmCheckOpinionManager bpmCheckOpinionManager = AppUtil.getBean(BpmCheckOpinionManager.class);
			bpmCheckOpinionManager.updateExtraProps(bpmCheckOpinion);
		}
	}

	public static void addCheckOpinion(DefaultBpmTask bpmTask, OpinionStatus opinionStatus, String userId,
			String opinion, boolean isCompleted) {
		String bpmnInstId = bpmTask.getBpmnInstId();
		NatProInstanceService natProInstanceService = AppUtil.getBean(NatProInstanceService.class);
		String superInstId = (String) natProInstanceService.getSuperVariable(bpmnInstId, BpmConstants.PROCESS_INST_ID);

		IUser user = BpmUtil.getUser(StringUtil.isEmpty(userId) ? bpmTask.getAssigneeId() : userId);
		List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setType(IdentityType.USER);
		bpmIdentity.setId(user.getUserId());
		bpmIdentity.setName(user.getFullname());
		identityList.add(bpmIdentity);

		DefaultBpmCheckOpinion checkOpinion = new DefaultBpmCheckOpinion();
		checkOpinion.setId(UniqueIdUtil.getSuid());
		checkOpinion.setProcDefId(bpmTask.getBpmnDefId());
		checkOpinion.setSupInstId(superInstId);
		checkOpinion.setProcInstId(bpmTask.getProcInstId());
		checkOpinion.setTaskId(bpmTask.getTaskId());
		checkOpinion.setTaskKey(bpmTask.getNodeId());
		checkOpinion.setTaskName(bpmTask.getName());
		checkOpinion.setStatus(opinionStatus.getKey());

		checkOpinion.setCreateTime(LocalDateTime.now());

		checkOpinion.setQualfieds(BpmCheckOpinionUtil.getIdentityIds(identityList));
		checkOpinion.setQualfiedNames(user.getFullname());

		if (isCompleted) {
			checkOpinion.setAuditor(user.getUserId());
			checkOpinion.setAuditorName(user.getFullname());
			checkOpinion.setCompleteTime(LocalDateTime.now());
			checkOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(bpmTask.getCreateTime()));
			checkOpinion.setOpinion(opinion);
		}
		BpmCheckOpinionManager bpmCheckOpinionManager = AppUtil.getBean(BpmCheckOpinionManager.class);
		bpmCheckOpinionManager.create(checkOpinion);
	}

	/**
	 * <pre>
	 * 原来同意的审批意见更新为撤回
	 * </pre>
	 * 
	 * @param customSignTaskId
	 */
	public static void updateCheckRevoker(String customSignTaskId) {
		BpmCheckOpinionManager bpmCheckOpinionManager = AppUtil.getBean(BpmCheckOpinionManager.class);
		QueryFilter<DefaultBpmCheckOpinion> queryFilter = QueryFilter.build();
		queryFilter.addFilter("task_id_", customSignTaskId, QueryOP.EQUAL);
		PageList<DefaultBpmCheckOpinion> query = bpmCheckOpinionManager.query(queryFilter);
		if (BeanUtils.isEmpty(query.getRows())) {
			return;
		}
		DefaultBpmCheckOpinion bpmCheckOpinion = null;
		Map<String, DefaultBpmCheckOpinion> opnionMap = new HashMap<>();
		for (DefaultBpmCheckOpinion opinion : query.getRows()) {
			opnionMap.put(opinion.getStatus(), opinion);
		}

		for (String status : statusList) {
			if (opnionMap.containsKey(status)) {
				bpmCheckOpinion = opnionMap.get(status);
				break;
			}
		}
		if (BeanUtils.isNotEmpty(bpmCheckOpinion)) {
			bpmCheckOpinion.setStatus(OpinionStatus.REVOKER.getKey());
			bpmCheckOpinionManager.update(bpmCheckOpinion);
		}
	}
}
