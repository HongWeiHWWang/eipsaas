package com.hotent.bpm.persistence.manager.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.persistence.dao.BpmCustomSignDataDao;
import com.hotent.bpm.persistence.manager.BpmCustomSignDataManager;
import com.hotent.bpm.persistence.model.BpmCustomSignData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre>
 *  
 * 描述：bpm_custom_signdata 处理实现类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-03 17:18:18
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("bpmCustomSignDataManager")
public class BpmCustomSignDataManagerImpl extends BaseManagerImpl<BpmCustomSignDataDao, BpmCustomSignData>
		implements BpmCustomSignDataManager {
	@Resource
	BpmCustomSignDataDao bpmCustomSignDataDao;

	@Override
    @Transactional
	public void addCustomSignData(BpmTask task, String preTaskId) {
		BpmCustomSignData signData = new BpmCustomSignData();
		String id = UniqueIdUtil.getSuid();
		String path = id + ".";
		String parentId = null;

		String type = "";
		String status = task.getStatus();
		String customSignStatus = null;
		if (TaskType.SIGNLINEED.getKey().equals(status)) {
			type = BpmCustomSignData.TYPE_PARALLEL;
		}
		if (TaskType.SIGNSEQUENCEED.getKey().equals(status)) {
			type = BpmCustomSignData.TYPE_SEQUENTIAL;
			customSignStatus = BpmCustomSignData.STATUS_COMPLETE;
		}
		if (TaskType.APPROVELINEED.getKey().equals(status)) {
			type = BpmCustomSignData.TYPE_PARALLEL_APPROVAL;
			path = null;
		}

		if (BeanUtils.isNotEmpty(preTaskId) && !TaskType.APPROVELINEED.getKey().equals(status)) {
			BpmCustomSignData curSignData = (BpmCustomSignData) bpmCustomSignDataDao.getByTaskIdAndStatus(preTaskId,
					customSignStatus);
			if (BeanUtils.isNotEmpty(curSignData)) {
				path = curSignData.getPath() + path;
				parentId = curSignData.getId();
			}

		}
		signData.setId(id);
		signData.setTaskId(task.getTaskId());
		signData.setStatus(BpmCustomSignData.STATUS_APPROVAL);
		signData.setInstId(task.getProcInstId());
		signData.setNodeId(task.getNodeId());
		signData.setParentId(parentId);
		signData.setPath(path);
		signData.setType(type);
		super.create(signData);
	}

	@Override
    @Transactional
	public void addCustomSignDatas(BpmTask task, String preTaskId, List<BpmIdentity> idList) {
		idList.forEach((item) -> {
			this.addCustomSignData(task, preTaskId);
		});

	}

	@Override
	public List<BpmCustomSignData> getByInstIdAndStatus(String instId, String status) {
		return bpmCustomSignDataDao.getByInstIdAndStatus(instId, status);
	}

	@Override
    @Transactional
	public void updateStatusByTaskId(String taskId, List<String> oldStatus, String newStatus, String newCreateTaskId) {
		bpmCustomSignDataDao.updateStatusByTaskId(taskId, oldStatus, newStatus, newCreateTaskId);
	}

	@Override
    @Transactional
	public void updateStatusByTaskId(String taskId, String oldStatus, String newStatus, String newCreateTaskId) {
		this.updateStatusByTaskId(taskId, Arrays.asList(oldStatus), newStatus, newCreateTaskId);
	}

	@Override
	public List<BpmCustomSignData> getByInstIdAndStatus(String instId, List<String> status) {
		return bpmCustomSignDataDao.getByInstIdAndStatusList(instId, status);
	}

	@Override
    @Transactional
	public void updateStatusByInstId(String instId, List<String> oldStatusList, String newStatus) {
		bpmCustomSignDataDao.updateStatusByInstId(instId, oldStatusList, newStatus);
	}

	@Override
	public BpmCustomSignData getSequentialSonByTaskId(String taskId) {
		return bpmCustomSignDataDao.getSequentialSonByTaskId(taskId);
	}

	@Override
	public List<BpmCustomSignData> getSignDataByBeforeSignTaskId(String instId, String taskId) {
		return bpmCustomSignDataDao.getSignDataByBeforeSignTaskId(instId, taskId);
	}

	@Override
    @Transactional
	public void removeByInstId(String instId) {
		bpmCustomSignDataDao.removeByInstId(instId);
	}

	@Override
	public List<BpmCustomSignData> getParallelSonByTaskId(String taskId) {
		BpmCustomSignData completeSignData = bpmCustomSignDataDao.getByTaskIdAndStatus(taskId,
				BpmCustomSignData.STATUS_COMPLETE);
		return bpmCustomSignDataDao.getParallelSonByPath(completeSignData.getPath());
	}

	@Override
	public List<BpmCustomSignData> getParallelAllSonByTaskId(String taskId) {
		BpmCustomSignData completeSignData = bpmCustomSignDataDao.getByTaskIdAndStatus(taskId,
				BpmCustomSignData.STATUS_COMPLETE);
		if(completeSignData==null) {
			return null;
		}
		return bpmCustomSignDataDao.getParallelAllSonByPath(completeSignData.getPath());
	}

	@Override
	public List<BpmCustomSignData> getAllSignDataByBeforeSignTaskId(String instanceId, String taskId) {
		return bpmCustomSignDataDao.getAllSignDataByBeforeSignTaskId(instanceId, taskId);
	}

	@Override
	public List<BpmCustomSignData> getBrotherByTaskId(String taskId, List<String> status) {
		return bpmCustomSignDataDao.getBrotherByTaskId(taskId,status);
	}
}
