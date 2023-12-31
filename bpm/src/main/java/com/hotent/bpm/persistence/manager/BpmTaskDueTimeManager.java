package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmTaskDueTime;

/**
 * 
 * <pre> 
 * 描述：任务期限统计 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-05-16 16:25:22
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmTaskDueTimeManager extends BaseManager<BpmTaskDueTime>{

	BpmTaskDueTime getByTaskId(String taskId);
	
	void updateAndSave(BpmTaskDueTime bpmTaskDueTime);
	
}
