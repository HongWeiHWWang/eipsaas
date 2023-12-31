package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.TaskTurnAssign;

/**
 * 任务指派人业务管理对象。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-12-11-下午2:32:42
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface TaskTurnAssignManager extends BaseManager<TaskTurnAssign>{
	
	/**
	 * 根据taskTurnId升序获取指派人列表。
	 * @param taskTurnId
	 * @return 
	 * List&lt;TaskTurnAssign>
	 */
	List<TaskTurnAssign> getByTaskTurnId(String taskTurnId);
	
	
	/**
	 * 根据taskTurnId获取最新的指派人。
	 * <pre>
	 * 	在列表中获取第一行数据。
	 * </pre>
	 * @param taskTurnId
	 * @return TaskTurnAssign
	 */
	TaskTurnAssign getLastTaskTurn(String taskTurnId);


	List<TaskTurnAssign> getByTaskTurnId(String taskTurnId, boolean b);
	
}
