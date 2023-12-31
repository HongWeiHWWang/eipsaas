package com.hotent.bpm.persistence.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.persistence.dao.BpmReadRecordDao;
import com.hotent.bpm.persistence.manager.BpmReadRecordManager;
import com.hotent.bpm.persistence.model.BpmReadRecord;
import com.hotent.bpm.persistence.model.DefaultBpmTask;

@Service("bpmReadRecordManager")
public class BpmReadRecordManagerImpl extends BaseManagerImpl<BpmReadRecordDao, BpmReadRecord> implements BpmReadRecordManager{

	
	@Override
	public List<BpmReadRecord> getByInstId(String instId) {
		//取得顶级的流程实例ID
		String supInstId=getTopInstId(instId);
		List<String> instList=getListByInstId(supInstId);
		return baseMapper.getByInstIds(instList);
	}
	
	/**
	 * 向上查询得到顶级的流程实例。
	 * @param instId
	 * @return String
	 */
	@Override
	public String getTopInstId(String instId){
		String rtn=instId;
		String supInstId=baseMapper.getSupInstByInstId(instId);
		while(StringUtil.isNotZeroEmpty(supInstId)){
			rtn=supInstId;
			supInstId=baseMapper.getSupInstByInstId(supInstId);
		}
		return rtn;
	}

	/**
	 * 向下查询流程实例。
	 * @param supperId
	 * @param instList 
	 * void
	 */
	private void getChildInst(String supperId,List<String> instList){
		List<String> list=baseMapper.getBySupInstId(supperId);
		if(BeanUtils.isEmpty(list)) return ;
		for(String instId:list){
			instList.add(instId);
			getChildInst(instId,instList);
		}
	}
	
	
	@Override
	public List<String> getListByInstId(String supInstId) {
		List<String> instList=new ArrayList<String>();
		instList.add(supInstId);
		//递归往下查询
		getChildInst(supInstId,instList);
		return instList;
	}
	
	
	@Override
	public List<BpmReadRecord> getByInstNodeId(String instId, String nodeId) {
		return baseMapper.getByInstNodeId(instId,nodeId);
	}

	
	@Override
	public List<BpmReadRecord> getFormOpinionByInstId(String instId) {
		List<BpmReadRecord> rtnList=new ArrayList<BpmReadRecord>();
		List<BpmReadRecord> list= getByInstId(instId);
		for(BpmReadRecord opinion:list){
			rtnList.add(opinion);
		}
		return rtnList;
	}
	
	@Override
	public List<BpmReadRecord> getByTaskIdandrecord(String taskId, String...reader) {
		// TODO Auto-generated method stub
		return baseMapper.getByTaskIdandrecord(taskId, reader);
	}
	@Override
	public List<BpmReadRecord> getByinstidandrecord(String instId, String reader) {
		return baseMapper.getByinstidandrecord(instId, reader);
	}
	@Override
	public Boolean isTaskReadByOwner(String taskId) {
		return baseMapper.getReadByOwnerCountWithTaskId(taskId) > 0;
	}
	@Override
	public List<BpmReadRecord> getByTaskIds(List<DefaultBpmTask> bpmTasks) {
		List<String> taskIds = new ArrayList<String>();
		bpmTasks.forEach(bpmTask -> {
			if(!TaskType.TRANSFORMING.getKey().equals(bpmTask.getStatus())) {
				taskIds.add(bpmTask.getId());
			}
		} );
		return baseMapper.getByTaskIds(taskIds);
	}
}
