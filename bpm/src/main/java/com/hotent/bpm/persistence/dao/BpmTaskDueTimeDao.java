package com.hotent.bpm.persistence.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmTaskDueTime;

/**
 * 
 * <pre> 
 * 描述：任务期限统计 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-05-16 16:25:22
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmTaskDueTimeDao extends BaseMapper<BpmTaskDueTime> {

	BpmTaskDueTime getByTaskId(@Param("taskId") String taskId);
}
