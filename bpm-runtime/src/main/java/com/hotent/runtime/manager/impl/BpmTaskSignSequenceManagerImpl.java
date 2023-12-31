package com.hotent.runtime.manager.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.exception.BusinessException;
import com.hotent.runtime.constant.SignSequenceStatus;
import com.hotent.runtime.dao.BpmTaskSignSequenceDao;
import com.hotent.runtime.manager.BpmTaskSignSequenceManager;
import com.hotent.runtime.model.BpmTaskSignSequence;

/**
 * 
 * <pre> 
 * 描述：顺序签署人员 处理实现类
 * 构建组：x7
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-09 10:40:32
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("bpmTaskSignSequenceManager")
public class BpmTaskSignSequenceManagerImpl extends BaseManagerImpl<BpmTaskSignSequenceDao, BpmTaskSignSequence> implements BpmTaskSignSequenceManager{
	
	@Override
	@Transactional
	public void updateStatus(String procInstId, String rootTaskId, String statusa, String statusb) {
		baseMapper.updateStatus(procInstId, rootTaskId, statusa, statusb);
	}
	
	@Override
	public BpmTaskSignSequence getByTaskId(String id) {
		return baseMapper.getByTaskId(id);
	}

	@Override
	@Transactional
	public Map<String, String> getNextExecutor(String taskId) {
		BpmTaskSignSequence curSignSequence = this.getByTaskId(taskId);
		String path = curSignSequence.getPath();
		curSignSequence.setStatus(SignSequenceStatus.COMPLETE.getKey());
		String nextTaskId = UniqueIdUtil.getSuid();
		curSignSequence.setNextTaskId(nextTaskId);
		super.update(curSignSequence);
		List<BpmTaskSignSequence> pathSign = baseMapper.getByStatusAndPath(SignSequenceStatus.WAITINGFORGENERATIONSIGNATURETASK.getKey(),path);
		while(BeanUtils.isEmpty(pathSign) && path.length()>0 && path.contains(".") ){
		   path = path.substring(0,path.lastIndexOf("."));
		   baseMapper.updateStatusByPath(path, SignSequenceStatus.HALF.getKey(), SignSequenceStatus.COMPLETE.getKey());
    	   pathSign = baseMapper.getByStatusAndPath(SignSequenceStatus.WAITINGFORGENERATIONSIGNATURETASK.getKey(),path);
       }
		
		if(BeanUtils.isEmpty(pathSign)){
			return null;
		}
		BpmTaskSignSequence nextBpmTaskSignSequence = pathSign.get(0);
		String executor = nextBpmTaskSignSequence.getExecutor();
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			resultMap = JsonUtil.toMap(executor);
		} catch (IOException e) {
			throw new BusinessException("签署失败");
		}
		
		nextBpmTaskSignSequence.setTaskId(nextTaskId);
		nextBpmTaskSignSequence.setStatus(SignSequenceStatus.INAPPROVAL.getKey());
		this.update(nextBpmTaskSignSequence);
		resultMap.put("taskId", nextBpmTaskSignSequence.getTaskId());
		return resultMap;
	}

	@Override
	public BpmTaskSignSequence getInApprovalByInstNodeId(String instId, String rootTaskId,String nodeId) {
		return baseMapper.getInApprovalByInstNodeId(instId,rootTaskId,nodeId);
	}

	@Override
	@Transactional
	public void removeByPath(String path) {
		baseMapper.removeByPath(path);
	}

	@Override
	@Transactional
	public void removeByInstNodeId(String instanceId, String rootTaskId,String nodeId) {
		baseMapper.removeByInstNodeId(instanceId,rootTaskId,nodeId);
	}

	@Override
	public Map<String, String> demoNextExecutor(String taskId) {
		BpmTaskSignSequence curSignSequence = this.getByTaskId(taskId);
		String path = curSignSequence.getPath();
		List<BpmTaskSignSequence> pathSign = baseMapper.getByStatusAndPath(SignSequenceStatus.WAITINGFORGENERATIONSIGNATURETASK.getKey(),path);
		while(BeanUtils.isEmpty(pathSign) && path.length()>0 && path.contains(".") ){
		   path = path.substring(0,path.lastIndexOf("."));
    	   pathSign = baseMapper.getByStatusAndPath(SignSequenceStatus.WAITINGFORGENERATIONSIGNATURETASK.getKey(),path);
       }
		
		if(BeanUtils.isEmpty(pathSign)){
			return null;
		}
		BpmTaskSignSequence nextBpmTaskSignSequence = pathSign.get(0);
		String executor = nextBpmTaskSignSequence.getExecutor();
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			resultMap = JsonUtil.toMap(executor);
		} catch (IOException e) {
			throw new BusinessException("签署失败");
		}
		return resultMap;
	}
}