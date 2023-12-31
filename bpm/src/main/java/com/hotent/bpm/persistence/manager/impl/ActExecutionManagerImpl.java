package com.hotent.bpm.persistence.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.ActExecutionDao;
import com.hotent.bpm.persistence.dao.ActTaskDao;
import com.hotent.bpm.persistence.manager.ActExecutionManager;
import com.hotent.bpm.persistence.model.ActExecution;

@Service("actExecutionManager")
public class ActExecutionManagerImpl extends BaseManagerImpl<ActExecutionDao, ActExecution> implements ActExecutionManager{
	@Resource
	ActTaskDao actTaskDao;
	@Override
    @Transactional
	public void delByInstList(List<String> instList) {
		//删除运行的数据
		//实例数据
		baseMapper.delVarsByInstList(instList);
		//删除候选人
		baseMapper.delCandidateByInstList(instList);
		//删除任务
		baseMapper.delTaskByByInstList(instList);
		//删除事件订阅
		baseMapper.delEventSubByInstList(instList);
		//删除实例
		baseMapper.delExectionByInstList(instList);
		
		//历史数据删除
		//历史变量清除
		baseMapper.delHiVarByInstList(instList);
		//历史候选人清除
		baseMapper.delHiCandidateByInstList(instList);
		//历史任务清除
		baseMapper.delHiTaskByInstList(instList);
		//历史实例清除
		baseMapper.delHiActInstByInstList(instList);
		//历史流程实例清除
		baseMapper.delHiProcinstByInstList(instList);
		
	}

	@Override
	public List<String> getByParentsId(String id) {
		return baseMapper.getByParentsId(id);
	}

	@Override
	public void delActiveByInstList(List<String> includeBpmnIdList) {
		baseMapper.delActiveVarsByInstList(includeBpmnIdList);
		actTaskDao.delTaskByInstList(includeBpmnIdList);
		baseMapper.delActiveByInstList(includeBpmnIdList);
	}
	
	
}
