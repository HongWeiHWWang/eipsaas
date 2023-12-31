package com.hotent.bpm.persistence.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.dao.BpmProBoDao;
import com.hotent.bpm.persistence.manager.BpmProBoManager;
import com.hotent.bpm.persistence.model.BpmProBo;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmProBoManager")
public class BpmProBoManagerImpl extends BaseManagerImpl<BpmProBoDao, BpmProBo> implements BpmProBoManager {

	@Override
    @Transactional
	public void removeByProcessId(String processId) {
		if (StringUtil.isEmpty(processId)) {
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processId", processId);
		baseMapper.removeByProcess(params);
	}

	@Override
    @Transactional
	public void removeByProcessKey(String processKey) {
		if (StringUtil.isEmpty(processKey)) {
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processKey", processKey);
		baseMapper.removeByProcess(params);
	}

	@Override
    @Transactional
	public void removeByBoCode(String boCode) {
		if (StringUtil.isEmpty(boCode)) {
			return;
		}
		baseMapper.removeByBoCode(boCode);
	}

	@Override
	public List<BpmProBo> getByProcessId(String processId) {
		if (StringUtil.isEmpty(processId)) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processId", processId);
		return baseMapper.getByProcess(params);
	}

	@Override
	public List<BpmProBo> getByProcessKey(String processKey) {
		if (StringUtil.isEmpty(processKey)) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processKey", processKey);
		return baseMapper.getByProcess(params);
	}

	@Override
	public List<BpmProBo> getByBoCode(String boCode) {
		if (StringUtil.isEmpty(boCode)) {
			return null;
		}
		return baseMapper.getByBoCode(boCode);
	}

	@Override
    @Transactional
	public void createByBpmProBoList(List<BpmProBo> bpmProBoList) {
		if (bpmProBoList == null || bpmProBoList.size() <= 0) {
			return;
		}
		for (BpmProBo bpmProBo : bpmProBoList) {
			String btId = bpmProBo.getId();
			if (StringUtil.isEmpty(btId)) {
				btId = UniqueIdUtil.getSuid();
				bpmProBo.setId(btId);
			}
			super.create(bpmProBo);
		}
	}

	@Override
	public List<BpmProBo> getByProcess(Map<String, Object> params) {
		return baseMapper.getByProcess(params);
	}

}
