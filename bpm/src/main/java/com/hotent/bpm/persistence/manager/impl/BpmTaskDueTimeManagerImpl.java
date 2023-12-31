package com.hotent.bpm.persistence.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.dao.BpmTaskDueTimeDao;
import com.hotent.bpm.persistence.manager.BpmTaskDueTimeManager;
import com.hotent.bpm.persistence.model.BpmTaskDueTime;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：任务期限统计 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-05-16 16:25:22
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmTaskDueTimeManager")
public class BpmTaskDueTimeManagerImpl extends BaseManagerImpl<BpmTaskDueTimeDao, BpmTaskDueTime> implements BpmTaskDueTimeManager{
	@Resource
	BpmTaskDueTimeDao bpmTaskDueTimeDao;

	
	/**
	 * 根据任务id, 获取最新的任务期限
	 */
	@Override
	public BpmTaskDueTime getByTaskId(String taskId) {
		return bpmTaskDueTimeDao.getByTaskId(taskId);
	}

	@Override
    @Transactional
	public void updateAndSave(BpmTaskDueTime bpmTaskDueTime) {
		BpmTaskDueTime dueTime =  super.get(bpmTaskDueTime.getId());
		dueTime.setIsNew((short)0);
		super.update(dueTime);
		bpmTaskDueTime.setId(UniqueIdUtil.getSuid());
		super.create(bpmTaskDueTime);
	}
}
