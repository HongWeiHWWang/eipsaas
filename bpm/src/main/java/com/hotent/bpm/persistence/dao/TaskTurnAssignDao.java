package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.TaskTurnAssign;


/**
 * 转办任务指派人。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-12-11-下午2:13:03
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface TaskTurnAssignDao extends BaseMapper<TaskTurnAssign> {

	/**
	 * 根据taskTurnId获取指派人员列表。
	 * @param id		指派ID
	 * @param isAsc		按时间排序
	 * @return List&lt;TaskTurnAssign>
	 */
	List<TaskTurnAssign> getByTaskTurnId(@Param("taskTurnId") String id,@Param("isAsc") boolean isAsc);
	
	List<TaskTurnAssign> getLastTaskAssign(@Param("taskTurnId") String id,@Param("isAsc") boolean isAsc);
	
}
