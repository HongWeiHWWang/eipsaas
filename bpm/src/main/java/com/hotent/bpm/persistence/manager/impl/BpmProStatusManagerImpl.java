package com.hotent.bpm.persistence.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.persistence.dao.BpmProStatusDao;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.model.DefaultBpmProStatus;
import com.hotent.uc.api.impl.util.ContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public class BpmProStatusManagerImpl extends BaseManagerImpl<BpmProStatusDao, DefaultBpmProStatus> implements BpmProStatusManager{

	@Override
	public List<DefaultBpmProStatus> queryHistorys(String procInstId) {
		return baseMapper.queryHistorys(procInstId);
	}


	@Override
    @Transactional
	public void archiveHistory(String procInstId) {
		baseMapper.archiveHistory(procInstId);
		
	}

	@Override
    @Transactional
	public void createOrUpd(String instId, String defId, String nodeId,
			String nodeName, NodeStatus nodeStatus) {
		DefaultBpmProStatus rtn=baseMapper.getByInstNodeId(instId,nodeId);
		//更新记录
		if(rtn==null){
			String userId=BeanUtils.isNotEmpty(ContextUtil.getCurrentUser())?ContextUtil.getCurrentUser().getUserId():"-1";
			DefaultBpmProStatus proStatus=new DefaultBpmProStatus(instId, defId, nodeId, nodeName, nodeStatus.getKey(),userId );
			
			this.create(proStatus);
		}
		else{
			rtn.setStatus(nodeStatus.getKey());
			super.update(rtn);
		}
	}


	@Override
    @Transactional
	public void delByInstList(List<String> instList) {
		baseMapper.delByInstList(instList);
	}



	@Override
	public DefaultBpmProStatus getByInstNodeId(String instId, String nodeId) {
		return baseMapper.getByInstNodeId(instId,nodeId);
	}



	@Override
    @Transactional
	public void updStatusByInstList(List<String> list, NodeStatus status) {
		baseMapper.updStatusByInstList(list,status.getKey());
	}
		
    private Map<String, String> statusColorMap = new HashMap<String, String>();
	
	public void setStatusColor(Map<String, String> statusColorMap) {
		this.statusColorMap = statusColorMap;
	}


	@Override
	public Map<String, String> getProcessInstanceStatus(String bpmnInstId) {
		QueryFilter filter = QueryFilter.build()
										.withPage(new PageBean(1, Integer.MAX_VALUE))
										.withQuery(new QueryField("proc_inst_id_", bpmnInstId, QueryOP.EQUAL));
		PageList<DefaultBpmProStatus> list =  this.query(filter);
		List<DefaultBpmProStatus> statusList = list.getRows();
		if(BeanUtils.isEmpty(statusList)){
			statusList = this.queryHistorys(bpmnInstId);
		}
		Map<String, String> colourMap = new HashMap<String, String>();
		if(BeanUtils.isEmpty(statusList)){
			return colourMap;
		}else{
			for(DefaultBpmProStatus bpmProStatus:statusList){
				String nodeId = bpmProStatus.getNodeId();
				String status = bpmProStatus.getStatus();
				String color = statusColorMap.get(status);
				colourMap.put(nodeId, color);
			}
		}
		return colourMap;
	}
}
