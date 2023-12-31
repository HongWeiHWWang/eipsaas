package com.hotent.runtime.service.impl;

import java.util.ArrayList;
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
 * 串行签署撤回处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年3月11日
 */
@Service
@Transactional
public class SequentialRevokeService implements RevokeService{
	@Resource
	BpmCustomSignDataManager bpmCustomSignDataManager;
	@Resource
	BpmReadRecordManager bpmReadRecordManager;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	
	@Override
	public void canRevoke(Map<String, Object> map, List<DefaultBpmCheckOpinion> list, String isReadRevoke) {
		String taskId = MapUtil.getString(map, "taskId");
		if(StringUtil.isEmpty(taskId)) {
			return;
		}
		BpmCustomSignData bpmCustomSignData = bpmCustomSignDataManager.getSequentialSonByTaskId(taskId);
		// 如果有直接后代
		if(bpmCustomSignData!=null) {
			String status = bpmCustomSignData.getStatus();
			// 直接后代处于审批或撤回审批状态，则允许撤回
			if(BpmCustomSignData.STATUS_APPROVAL.equals(status)) {
				setRevoke(map, bpmCustomSignData.getTaskId(), isReadRevoke, bpmReadRecordManager);
			}
		}
		// 没有直接后代
		else {
			List<DefaultBpmCheckOpinion> afterSignOpinions = getOpinionBySignType(list, CustomSignNodeDef.AFTER_SIGN);
			
			if(afterSignOpinions.size() > 0) {
				List<String> opinionStatusList = new ArrayList<>();
				// 待审批的可被撤回
				opinionStatusList.add(OpinionStatus.AWAITING_CHECK.getKey());
				// 被驳回的可被撤回
				opinionStatusList.add(OpinionStatus.REJECT.getKey());
				// 撤回的可被撤回
				opinionStatusList.add(OpinionStatus.REVOKER.getKey());
				if(hasOpinionWithStatus(afterSignOpinions, opinionStatusList)) {
					// 通过堆栈查找后续任务
					String toTaskId = bpmExeStackManager.getToTaskIdByFromTaskId(taskId);
					// 后续任务的审批记录
					DefaultBpmCheckOpinion toTaskOpinion = getOpinionByTaskId(afterSignOpinions, toTaskId);
					if(toTaskOpinion!=null) {
						List<DefaultBpmCheckOpinion> toTaskOpinionList = new ArrayList<>();
						toTaskOpinionList.add(toTaskOpinion);
						// 审批记录为指定状态
						if(hasOpinionWithStatus(toTaskOpinionList, opinionStatusList)) {
							setRevoke(map, toTaskId, isReadRevoke, bpmReadRecordManager);
						}
					}
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * 正在运行的任务的节点id和撤回的节点id一样
	 * 则说明B还没产生  An-1 撤回An
	 * 否则B已经产生 An撤回
	 * </pre>
	 */
	@Override
	public void doRevoke(CustomSignRevokeParam revokeParamObject)  throws Exception{
		
		String targetNodeId = revokeParamObject.getTargetNodeId();
		List<DefaultBpmTask> tasks = bpmTaskManager.getByInstId(revokeParamObject.getInstanceId());
		if(BeanUtils.isEmpty(tasks)) {
			throw new BaseException("已没有任务可以撤回");
		}
		DefaultBpmTask currentTask = tasks.get(0);
		if(targetNodeId.equals(currentTask.getNodeId())) {
			bpmTaskManager.sequentialTaskRevoke(currentTask.getId(), revokeParamObject.getTargetTaskId());
		}else {
			bpmTaskManager.taskAnRevoke(revokeParamObject.getInstanceId(), currentTask.getId(), targetNodeId,revokeParamObject.getTargetTaskId());
			
		}
	}
}
