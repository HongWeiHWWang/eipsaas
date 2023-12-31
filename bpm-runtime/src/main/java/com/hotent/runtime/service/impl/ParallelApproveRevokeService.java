package com.hotent.runtime.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.exception.BaseException;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.model.process.nodedef.ext.CustomSignNodeDef;
import com.hotent.bpm.persistence.manager.BpmCustomSignDataManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmReadRecordManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.BpmCustomSignData;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.runtime.params.CustomSignRevokeParam;
import com.hotent.runtime.service.RevokeService;

/**
 * 并行审批撤回处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年3月11日
 */
@Service
@Transactional
public class ParallelApproveRevokeService implements RevokeService {
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	BpmReadRecordManager bpmReadRecordManager;
	@Resource
	BpmCustomSignDataManager bpmCustomSignDataManager;

	@Override
	public void canRevoke(Map<String, Object> map, List<DefaultBpmCheckOpinion> list, String isReadRevoke) {
		String taskId = MapUtil.getString(map, "taskId");
		if (StringUtil.isEmpty(taskId)) {
			return;
		}
		List<DefaultBpmCheckOpinion> afterSignOpinions = getOpinionBySignType(list, CustomSignNodeDef.AFTER_SIGN);
		// 已经产生AfterSign任务
		if (afterSignOpinions.size() > 0) {
			List<String> opinionStatusList = new ArrayList<>();
			// 待审批的可被撤回
			opinionStatusList.add(OpinionStatus.AWAITING_CHECK.getKey());
			// 被驳回的可被撤回
			opinionStatusList.add(OpinionStatus.REJECT.getKey());
			// 被撤回的
			opinionStatusList.add(OpinionStatus.SIGN_RECOVER_CANCEL.getKey());
			opinionStatusList.add(OpinionStatus.RETRACTED.getKey());
			// 撤回的可被撤回
			opinionStatusList.add(OpinionStatus.REVOKER.getKey());
			if (hasOpinionWithStatus(afterSignOpinions, opinionStatusList)) {
				// 通过堆栈查找后续任务
				String toTaskId = bpmExeStackManager.getToTaskIdByFromTaskId(taskId);
				if (toTaskId == null) {
					// A 发起A1 A2 A3 , B待办  ，A3 撤回后A1,A2无法撤回
					List<BpmCustomSignData> approvalDatas = bpmCustomSignDataManager.getByInstIdAndStatus(
							afterSignOpinions.get(0).getProcInstId(), Arrays.asList(BpmCustomSignData.STATUS_APPROVAL));
					if (BeanUtils.isEmpty(approvalDatas)) {
						map.put("revoke", true);
						return;
					}
				}
				// 后续任务的审批记录
				DefaultBpmCheckOpinion toTaskOpinion = getOpinionByTaskId(afterSignOpinions, toTaskId);
				if (toTaskOpinion != null) {
					List<DefaultBpmCheckOpinion> toTaskOpinionList = new ArrayList<>();
					toTaskOpinionList.add(toTaskOpinion);
					// 审批记录为指定状态
					if (hasOpinionWithStatus(toTaskOpinionList, opinionStatusList)) {
						setRevoke(map, toTaskId, isReadRevoke, bpmReadRecordManager);
					}
				}
			}
		}
		// 未产生AfterSign任务时可撤回
		else {
			map.put("revoke", true);
		}
	}

	/**
	 * <pre>
	 * 正在运行的任务的节点id和撤回的节点id一样
	 * 则说明B还没产生  An 自身任务撤回
	 * 否则B已经产生 An撤回
	 * </pre>
	 */
	@Override
	public void doRevoke(CustomSignRevokeParam revokeParamObject) throws Exception {
		List<DefaultBpmTask> tasks = bpmTaskManager.getByInstId(revokeParamObject.getInstanceId());
		if (BeanUtils.isEmpty(tasks)) {
			throw new BaseException("已没有任务可以撤回");
		}
		DefaultBpmTask runningBpmTask = tasks.get(0);

		if (revokeParamObject.getTargetNodeId().equals(runningBpmTask.getNodeId())) {
			bpmTaskManager.approvalTaskRevoke(runningBpmTask.getId(), revokeParamObject.getTargetTaskId());
		} else {

			bpmTaskManager.taskAnRevoke(revokeParamObject.getInstanceId(), runningBpmTask.getId(),
					revokeParamObject.getTargetNodeId(), revokeParamObject.getTargetTaskId());

		}
	}
}
