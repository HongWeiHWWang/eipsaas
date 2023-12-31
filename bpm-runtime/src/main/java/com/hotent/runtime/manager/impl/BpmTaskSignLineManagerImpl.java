package com.hotent.runtime.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.runtime.dao.BpmTaskSignLineDao;
import com.hotent.runtime.manager.BpmTaskSignLineManager;
import com.hotent.runtime.model.BpmTaskSignLine;

/**
 * 
 * <pre> 
 * 描述：并行签署 处理实现类
 * 构建组：x7
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-14 10:34:11
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("bpmTaskSignLineManager")
public class BpmTaskSignLineManagerImpl extends BaseManagerImpl<BpmTaskSignLineDao, BpmTaskSignLine> implements BpmTaskSignLineManager{
	
	@Override
	public BpmTaskSignLine getByTaskId(String taskId) {
		return baseMapper.getByTaskId(taskId);
	}
	@Override
	public List<BpmTaskSignLine> getByInstNodeIdAndStatus(String instanceId, String rootTaskId, String nodeId,
			String status) {
		return baseMapper.getByInstNodeIdAndStatus(instanceId,rootTaskId,nodeId,status);
	}
	@Override
	public List<BpmTaskSignLine> getByPathChildAndStatus(String path,
			String status) {
		return baseMapper.getByPathChildAndStatus(path,status);
	}
	@Override
	@Transactional
	public void removeByTaskIds(String[] taskIds) {
		for (String taskId : taskIds) {
			removeByTaskId(taskId);
		}
	}
	@Override
	@Transactional
	public void removeByTaskId(String taskId) {
		baseMapper.removeByTaskId(taskId);
	}
	@Override
	@Transactional
	public void removeByInstIdNodeId(String instanceId, String rootTaskId, String nodeId) {
		baseMapper.removeByInstIdNodeId(instanceId,rootTaskId,nodeId);
	}
	@Override
	@Transactional
	public void updateStatusByTaskIds(String status, String[] taskIds) {
		baseMapper.updateStatusByTaskIds(status,taskIds);
	}
}
