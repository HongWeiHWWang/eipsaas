package com.hotent.bpm.engine.his;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.model.process.hi.BpmProcessInstanceHistory;
import com.hotent.bpm.api.service.BpmHistoryService;

@Service
public class DefaultBpmHistoryService implements BpmHistoryService{

	@Override
	public BpmProcessInstanceHistory getProcessInstHistory(String processInstId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeProcessInstHistory(String processInstId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BpmProcessInstanceHistory> getAll(QueryFilter queryFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BpmProcessInstanceHistory> getProcessInstancesByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BpmProcessInstanceHistory> getProcessInstancesByUserId(String userId, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BpmProcessInstanceHistory> getProcessInstancesByUserId(String userId, QueryFilter queryFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BpmProcessInstanceHistory> getAttendProcessInstancesByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BpmProcessInstanceHistory> getAttendProcessInstancesByUserId(String userId, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BpmProcessInstanceHistory> getAttendProcessInstancesByUserId(String userId, QueryFilter queryFilter) {
		// TODO Auto-generated method stub
		return null;
	}

}
