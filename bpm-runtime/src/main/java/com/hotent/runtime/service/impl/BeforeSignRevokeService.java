package com.hotent.runtime.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.exception.BaseException;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.manager.BpmCustomSignDataManager;
import com.hotent.bpm.persistence.manager.BpmReadRecordManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.BpmCustomSignData;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.runtime.params.CustomSignRevokeParam;
import com.hotent.runtime.service.RevokeService;

/**
 * 串并签 前任务的撤回处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年3月12日
 */
@Service
@Transactional
public class BeforeSignRevokeService implements RevokeService {
	@Resource
	BpmCustomSignDataManager bpmCustomSignDataManager;
	@Resource
	BpmReadRecordManager bpmReadRecordManager;
	@Resource
	BpmTaskManager bpmTaskManager;

	@Override
	public void canRevoke(Map<String, Object> map, List<DefaultBpmCheckOpinion> list, String isReadRevoke) {
		String instId = MapUtil.getString(map, "id");
		String taskId = MapUtil.getString(map, "taskId");
		if (StringUtil.isEmpty(instId) || StringUtil.isEmpty(taskId)) {
			return;
		}
		// 获取串并签前置任务对应的处于审批中和撤回审批中的数据，有审批中或撤回审批中的数据时前置任务才允许撤回
		List<BpmCustomSignData> signDataList = bpmCustomSignDataManager.getSignDataByBeforeSignTaskId(instId, taskId);
		if (BeanUtils.isEmpty(signDataList)) {
			return;
		}
		String type = signDataList.get(0).getType();
		// 设置当前前置任务的后续任务类型
		switch (type) {
		// 串签
		case BpmCustomSignData.TYPE_SEQUENTIAL:
			setRevoke(map, signDataList.get(0).getTaskId(), isReadRevoke, bpmReadRecordManager);
			break;
		// 并签
		case BpmCustomSignData.TYPE_PARALLEL:
			// 设置是否需要获取可撤回待办任务
			map.put("needGetRevokeTasks", true);
			// 并签有审批中或撤回审批中的数据时，前置任务显示撤回按钮，已阅的判定在显示的撤回对话框中再判断。
			map.put("revoke", true);
			break;
		// 并审
		case BpmCustomSignData.TYPE_PARALLEL_APPROVAL:
			
			List<BpmCustomSignData> allSignDataByBeforeSignTaskId = bpmCustomSignDataManager.getAllSignDataByBeforeSignTaskId(instId, taskId);
			// 如果产生的并审任务  和 当前处于审批中/撤回审批中的待办数相等时才允许撤回
			if(BeanUtils.isNotEmpty(allSignDataByBeforeSignTaskId) && allSignDataByBeforeSignTaskId.size()==signDataList.size()) {
				boolean revoke = true;
				for (BpmCustomSignData bpmCustomSignData : allSignDataByBeforeSignTaskId) {
					// 并审 不允许已阅撤回
					Boolean isRead = bpmReadRecordManager.isTaskReadByOwner(bpmCustomSignData.getTaskId());
					if("false".equals(isReadRevoke) && isRead ) {
						revoke = false;
						break;
					}
				}
				map.put("revoke", revoke);
			}
			break;
		}
	}

	@Override
	public void doRevoke(CustomSignRevokeParam revokeParamObject) throws Exception {
		String instanceId = revokeParamObject.getInstanceId();
		List<DefaultBpmTask> currentTasks = bpmTaskManager.getByInstId(instanceId);
		if (BeanUtils.isEmpty(currentTasks)) {
			throw new BaseException("A后续没有任务可以撤回");
		}
		DefaultBpmTask currentTask = currentTasks.get(0);
		String currentTaskIds = revokeParamObject.getCurrentTaskIds();
		String status = currentTask.getStatus();
		switch (status) {
		case "SIGNSEQUENCEED":
			// A 顺签撤回
			bpmTaskManager.sequentialTaskARevoke(instanceId, currentTask.getTaskId(),
					revokeParamObject.getTargetNodeId());
			break;
		case "APPROVELINEED":
			// A 并批撤回
			bpmTaskManager.approvalTaskARevoke(instanceId, currentTask.getTaskId(),revokeParamObject.getTargetTaskId(),
					revokeParamObject.getTargetNodeId());
			break;
		case "SIGNLINEED":
			if (StringUtil.isEmpty(currentTaskIds)) {
				throw new BaseException("请选择撤回的并签任务");
			}
			// A 并签撤回
			bpmTaskManager.parallaelARevoke(instanceId, revokeParamObject.getTargetNodeId(),
					revokeParamObject.getCurrentTaskIds());
			break;

		default:
			throw new BaseException("撤回任务类型必须是签署并审任务");
		}

	}
}
