package com.hotent.runtime.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.runtime.model.BpmTaskSignLine;

/**
 * 
 * <pre> 
 * 描述：并行签署 DAO接口
 * 构建组：x7
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-14 10:34:11
 * 版权：广州宏天软件股份有限公司
 * 
 * 修改： 支持会签任务发起并签  修改添加查询参数 root_task_id
 * </pre>
 */
public interface BpmTaskSignLineDao extends BaseMapper<BpmTaskSignLine> {

	BpmTaskSignLine getByTaskId(String taskId);
	
	/**
	 * 
	 * @param instanceId 流程实例id
	 * @param rootTaskId 根任务id 
	 * @param nodeId 节点id
	 * @param status 状态
	 * @return
	 */
	List<BpmTaskSignLine> getByInstNodeIdAndStatus(@Param("instanceId")String instanceId,@Param("rootTaskId")String rootTaskId,
			@Param("nodeId")String nodeId, @Param("status")String status);

	List<BpmTaskSignLine> getByPathChildAndStatus(@Param("path")String path, @Param("status")String status);

	void removeByTaskId(@Param("taskId")String taskId);
	
	/**
	 * 
	 * @param instanceId 流程实例id
	 * @param rootTaskId 根任务id
	 * @param nodeId 节点id
	 */
	void removeByInstIdNodeId(@Param("instanceId")String instanceId,@Param("rootTaskId")String rootTaskId, @Param("nodeId")String nodeId);

	void updateStatusByTaskIds(@Param("status")String status, @Param("taskIds")String[] taskIds);
}
