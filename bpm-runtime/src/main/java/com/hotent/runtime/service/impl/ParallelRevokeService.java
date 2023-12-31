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
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.runtime.params.CustomSignRevokeParam;
import com.hotent.runtime.service.RevokeService;
import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * 并行签署撤回处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年3月11日
 */
@Service
@Transactional
public class ParallelRevokeService implements RevokeService{
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	BpmCustomSignDataManager bpmCustomSignDataManager;
	@Resource
	BpmReadRecordManager bpmReadRecordManager;

	@Override
	public void canRevoke(Map<String, Object> map, List<DefaultBpmCheckOpinion> list, String isReadRevoke) {
		String taskId = MapUtil.getString(map, "taskId");
		if (StringUtil.isEmpty(taskId)) {
			return;
		}
		List<BpmCustomSignData> parallelAllSonByTaskId = bpmCustomSignDataManager.getParallelAllSonByTaskId(taskId);
		if(parallelAllSonByTaskId==null) {
			return;
		}
		// 有后代任务，则判断这些后代任务中是否有处于审批中或撤回审批中的任务
		if(BeanUtils.isNotEmpty(parallelAllSonByTaskId)) {
			List<String> signDataStatusList = new ArrayList<>();
			// 待审批的可被撤回
			signDataStatusList.add(BpmCustomSignData.STATUS_APPROVAL);
			// 撤回审批中的可被撤回
			signDataStatusList.add(BpmCustomSignData.STATUS_WITHDRAW_APPROVAL);
			if(hasSignDataWithStatus(parallelAllSonByTaskId, signDataStatusList)) {
				// 这里是否显示撤回按钮的判定不再对待办的已阅进行判断，在打开撤回对话框时显示可撤回待办时再进行判定
				map.put("revoke", true);
				// 设置是否需要获取可撤回待办任务
				map.put("needGetRevokeTasks", true);
			}
		}
		// 无后代任务时，判断是否有AfterSign任务处于审批中
		else {
			List<DefaultBpmCheckOpinion> afterSignOpinions = getOpinionBySignType(list, CustomSignNodeDef.AFTER_SIGN);
			// 已经产生AfterSign任务
			if(afterSignOpinions.size() > 0) {
				List<String> opinionStatusList = new ArrayList<>();
				// 待审批的可被撤回
				opinionStatusList.add(OpinionStatus.AWAITING_CHECK.getKey());
				// 被驳回的可被撤回
				opinionStatusList.add(OpinionStatus.REJECT.getKey());
				opinionStatusList.add(OpinionStatus.SIGN_RECOVER_CANCEL.getKey());
				opinionStatusList.add(OpinionStatus.RETRACTED.getKey());
				// 撤回的可被撤回
				opinionStatusList.add(OpinionStatus.REVOKER.getKey());
				if(hasOpinionWithStatus(afterSignOpinions, opinionStatusList)) {
					// 通过堆栈查找后续任务
					String toTaskId = bpmExeStackManager.getToTaskIdByFromTaskId(taskId);
					if(toTaskId==null) {
						List<DefaultBpmTask> tasks = bpmTaskManager.getByInstId(afterSignOpinions.get(0).getProcInstId());
						if(BeanUtils.isNotEmpty(tasks)) {	
							DefaultBpmTask bpmTask = tasks.get(0);
							String fromNodeId = bpmExeStackManager.getCurrentTaskFromNodeId(bpmTask.getId());
							if(StringUtil.isNotEmpty(fromNodeId) && fromNodeId.equals(String.valueOf(map.get("taskKey")))) {
								toTaskId = tasks.get(0).getId();
							}
							if(toTaskId==null){
								List<BpmCustomSignData> withDrawApproval = bpmCustomSignDataManager.getByInstIdAndStatus(bpmTask.getProcInstId(), Arrays.asList(BpmCustomSignData.STATUS_WITHDRAW_APPROVAL));
								if(BeanUtils.isNotEmpty(withDrawApproval)) {
									map.put("revoke", true);
									return;
								}
							}
						}
					}
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
			// 未产生AfterSign任务时可撤回
			else {
				map.put("revoke", true);
			}
		}
	}
	
	// 判断串并签记录列表中是否至少有一条为指定状态
	private Boolean hasSignDataWithStatus(List<BpmCustomSignData> list, List<String> signDataStatusList) {
		Boolean result = false;
		if(BeanUtils.isEmpty(list) || BeanUtils.isEmpty(signDataStatusList)) {
			return result;
		}
		for(BpmCustomSignData signData : list) {
			if(signDataStatusList.contains(signData.getStatus())) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * <pre>
	 * 正在运行的任务的节点id和撤回的节点id一样
	 * 则说明B还没产生 
	 * A11 撤回A111 ，A112
	 * 如果撤回后 A11 下级没有待办且没有 则需要产生A11 任务
	 * 
	 * 否则B已经产生 An撤回
	 * 产生An的任务
	 * </pre>
	 */
	@Override
	public void doRevoke(CustomSignRevokeParam revokeParamObject) throws Exception {
		List<DefaultBpmTask> tasks = bpmTaskManager.getByInstId(revokeParamObject.getInstanceId());
		if(BeanUtils.isEmpty(tasks)) {
			throw new BaseException("已没有任务可以撤回");
		}
		
		DefaultBpmTask runningBpmTask = tasks.get(0);
		
		List<BpmCustomSignData> anSignDatas = bpmCustomSignDataManager.getParallelAllSonByTaskId(revokeParamObject.getTargetTaskId());
		boolean sameNodeId  = revokeParamObject.getTargetNodeId().equals(runningBpmTask.getNodeId());
		// An 撤回
		if(BeanUtils.isEmpty(anSignDatas)) {
			if(sameNodeId) {
				// B 已经撤回 或者 A1..An 签署为结束
				bpmTaskManager.addCustomSignTask(runningBpmTask.getId(), new String[] { ContextUtil.getCurrentUserId() },false);
				String newCreateTaskId = getNewCreateTaskId();
				bpmCustomSignDataManager.updateStatusByTaskId(revokeParamObject.getTargetTaskId(), BpmCustomSignData.STATUS_COMPLETE, BpmCustomSignData.STATUS_WITHDRAW_APPROVAL, newCreateTaskId);
				BpmCheckOpinionUtil.updateCheckRevoker(revokeParamObject.getTargetTaskId());			
			}else {				
				// B 未处理
				bpmTaskManager.taskAnRevoke(revokeParamObject.getInstanceId(), runningBpmTask.getId(), revokeParamObject.getTargetNodeId(), revokeParamObject.getTargetTaskId());
			}
		}else if(sameNodeId) {
			// A11 撤回 A111, A112
			bpmTaskManager.parallelRevoke(revokeParamObject.getCurrentTaskIds(),revokeParamObject.getTargetTaskId());
		}
	}
	
	
}
