package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmInstForm;

/**
 * 
 * <pre> 
 * 描述：bpm_inst_form 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2017-07-04 15:19:05
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmInstFormManager extends BaseManager<BpmInstForm>{

	BpmInstForm getNodeForm(String instId, String defId, String nodeId, String type);

	BpmInstForm getGlobalForm(String instId, String type);
	
	BpmInstForm getInstForm(String instId, String type);
	
	BpmInstForm getSubInstanFrom(String instId, String type);

	void removeDataByDefId(String defId);
	
	void removeDataByInstId(String instId);
	
}
