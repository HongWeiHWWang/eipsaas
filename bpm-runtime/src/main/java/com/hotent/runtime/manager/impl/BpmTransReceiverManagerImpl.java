package com.hotent.runtime.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.runtime.dao.BpmTransReceiverDao;
import com.hotent.runtime.manager.BpmTransReceiverManager;
import com.hotent.runtime.model.BpmTransReceiver;

/**
 * 
 * <pre> 
 * 描述：流转任务接收人 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-06 10:51:37
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmTransReceiverManager")
public class BpmTransReceiverManagerImpl extends BaseManagerImpl<BpmTransReceiverDao, BpmTransReceiver> implements BpmTransReceiverManager{
	@Override
	public List<BpmTransReceiver> getByTransRecordid(String transRecordid) {
		return baseMapper.getByTransRecordid(transRecordid);
	}
	@Override
	public BpmTransReceiver getByTransRecordAndUserId(Map<String, String> params) {
		return baseMapper.getByTransRecordAndUserId(params);
	}
    @Override
    @Transactional
    public void updateReceiver(Map<String, Object> params) {
    	baseMapper.updateReceiver(params);
    }
	@Override
	public List<BpmTransReceiver> getByTaskId(String taskId) {
		return baseMapper.getByTaskId(taskId);
	}
}

