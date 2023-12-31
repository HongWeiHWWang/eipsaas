package com.hotent.bpm.persistence.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.persistence.dao.BpmInstFormDao;
import com.hotent.bpm.persistence.manager.BpmInstFormManager;
import com.hotent.bpm.persistence.model.BpmInstForm;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：bpm_inst_form 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2017-07-04 15:19:05
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmInstFormManager")
public class BpmInstFormManagerImpl extends BaseManagerImpl<BpmInstFormDao, BpmInstForm> implements BpmInstFormManager{

	
	@Override
	public BpmInstForm getNodeForm(String instId, String defId, String nodeId,
			String type) {
		List<BpmInstForm> list = baseMapper.getNodeForm(instId,defId,nodeId,type);
		if(BeanUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public BpmInstForm getGlobalForm(String instId,String type) {
		List<BpmInstForm> list = baseMapper.getGlobalForm(instId,type);
		if(BeanUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public BpmInstForm getInstForm(String instId, String type) {
		List<BpmInstForm> list =  baseMapper.getInstForm(instId,type);
		if(BeanUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
    @Transactional
	public void removeDataByDefId(String defId) {
		baseMapper.removeDataByDefId(defId);
	}

	@Override
    @Transactional
	public void removeDataByInstId(String instId) {
		baseMapper.removeDataByInstId(instId);
	}

	@Override
	public BpmInstForm getSubInstanFrom(String instId, String type) {
		return baseMapper.getSubInstanFrom(instId, type);
	}

	
}
