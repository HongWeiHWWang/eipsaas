package com.hotent.runtime.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.runtime.model.BpmTaskSignSequence;

/**
 * 
 * <pre> 
 * 描述：顺序签署人员 DAO接口
 * 构建组：x7
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-09 10:40:32
 * 版权：广州宏天软件股份有限公司
 * 修改： 2020-02-27 修改 支持会签任务发起顺签
 * 
 * </pre>
 */
public interface BpmTaskSignSequenceDao extends BaseMapper<BpmTaskSignSequence> {
	
	/**
	 * 
	 * @param procInstId 流程实例id
	 * @param rootTaskId 流程根任务id
	 * @param statusa 状态A
	 * @param statusb 状态B
	 */
	void updateStatus(@Param("procInstId")String procInstId,@Param("rootTaskId")String rootTaskId, @Param("statusa")String statusa, @Param("statusb")String statusb);

	BpmTaskSignSequence getByTaskId(String id);

	List<BpmTaskSignSequence> getByStatusAndPath(@Param("status")String status, @Param("path")String path);

	void updateStatusByPath(@Param("path")String path, @Param("statusa")String statusa, @Param("statusb")String statusb);
	
	/**
	 * 
	 * @param instanceId
	 * @param rootTaskId
	 * @param nodeId
	 * @return
	 */
	BpmTaskSignSequence getInApprovalByInstNodeId(@Param("instanceId")String instanceId,@Param("rootTaskId")String rootTaskId,@Param("nodeId")String nodeId);

	void removeByPath(@Param("path")String path);
	
	/**
	 * 
	 * @param instanceId 流程实例id
	 * @param rootTaskId 根任务id
	 * @param nodeId 节点id
	 */
	void removeByInstNodeId(@Param("instanceId")String instanceId,@Param("rootTaskId")String rootTaskId,@Param("nodeId")String nodeId);
}
