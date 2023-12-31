package com.hotent.bpm.persistence.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.TaskTurnAssignDao;
import com.hotent.bpm.persistence.manager.TaskTurnAssignManager;
import com.hotent.bpm.persistence.model.TaskTurnAssign;

@Service("taskTurnAssignManager")
public class TaskTurnAssignManagerImpl extends BaseManagerImpl<TaskTurnAssignDao, TaskTurnAssign>
		implements TaskTurnAssignManager {

	@Override
	public List<TaskTurnAssign> getByTaskTurnId(String id) {
		return baseMapper.getByTaskTurnId(id, true);
	}

	@Override
	public TaskTurnAssign getLastTaskTurn(String taskTurnId) {
		List<TaskTurnAssign> list = baseMapper.getByTaskTurnId(taskTurnId, false);
		return list.get(0);
	}

	@Override
	public List<TaskTurnAssign> getByTaskTurnId(String taskTurnId, boolean b) {
		return baseMapper.getByTaskTurnId(taskTurnId, b);
	}
}
