package com.hotent.runtime.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.runtime.model.BpmTaskTrans;

/**
 * 任务流转处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface BpmTaskTransManager extends BaseManager<BpmTaskTrans>{
	
	
	/**
	 * 根据任务获取会签情况的配置。
	 * @param taskId
	 * @return BpmTaskTrans
	 */
	BpmTaskTrans getByTaskId(String taskId);

    /**
     * 征询回复
     * @param dbo
     */
    void taskToInquReply(DefaultBpmCheckOpinion dbo)throws Exception;
    
    void removeByInstId(String instanceId);
	
}
