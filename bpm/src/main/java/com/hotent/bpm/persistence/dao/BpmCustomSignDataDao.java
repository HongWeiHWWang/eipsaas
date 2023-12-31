package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmCustomSignData;

/**
 * 
 * <pre> 
 * 描述：bpm_custom_signdata DAO接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-03 17:18:18
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BpmCustomSignDataDao extends BaseMapper<BpmCustomSignData> {
	
	/**
	 * 根据任务id 和状态获取数据
	 * @param taskId
	 * @param status
	 * @return
	 */
	BpmCustomSignData getByTaskIdAndStatus(@Param("taskId") String taskId,@Param("status") String status);
	
	/**
	 * 根据流程实例 和 状态获取
	 * @param instId
	 * @param status
	 * @return
	 */
	List<BpmCustomSignData> getByInstIdAndStatus(@Param("instId") String instId,@Param("status") String status);
	
	/**
	 * 更新状态
	 * @param taskId
	 * @param oldStatus
	 * @param newStatus
	 * @param newCreateTaskId 
	 */
	void updateStatusByTaskId(@Param("taskId")String taskId,@Param("oldStatus") List<String> oldStatus,@Param("newStatus") String newStatus,@Param("newCreateTaskId")  String newCreateTaskId);
	
	/**
	 * 通过流程实例ID和状态数组获取数据
	 * @param instId
	 * @param status
	 */
	 List<BpmCustomSignData> getByInstIdAndStatusList(@Param("instId") String instId,@Param("statusList")List<String> status);
	
	 /**
	  * 通过流程实例ID更新数据的状态
	  * @param instId
	  * @param oldStatusList
	  * @param newStatus
	  */
	void updateStatusByInstId(@Param("instId") String instId,@Param("statusList") List<String> oldStatusList,@Param("newStatus")  String newStatus);
	
	/**
	 * 通过任务ID获取顺签的直接后代(即parentId为任务ID所对应数据id的这条记录)
	 * @param taskId
	 * @return
	 */
	BpmCustomSignData getSequentialSonByTaskId(@Param("taskId")String taskId);
	
	/**
	 * 获取串并签前置任务对应的处于审批中和撤回审批中的数据
	 * @param instId
	 * @param taskId
	 * @return
	 */
	List<BpmCustomSignData> getSignDataByBeforeSignTaskId(@Param("instId")String instId, @Param("taskId")String taskId);
	
	/**
	 * 根据实例id清空数据
	 * @param instId
	 */
	void removeByInstId(@Param("instId")String instId);

	/**
	 * 获取并签下级审批中和撤回审批中的数据
	 * @param path
	 * @return
	 */
	List<BpmCustomSignData> getParallelSonByPath(@Param("path")String path);
	/**
	 * 获取串并签前置任务对应的处于审批中和撤回审批中的数据
	 * @param instId
	 * @param taskId
	 * @return
	 */
	List<BpmCustomSignData> getAllSignDataByBeforeSignTaskId(@Param("instId")String instId, @Param("taskId")String taskId);
	
	/**
	 * 获取并签下级审批中和撤回审批中的数据
	 * @param path
	 * @return
	 */
	List<BpmCustomSignData> getParallelAllSonByPath(@Param("path")String path);
    
	/**
	 * 获取指定状态的兄弟数据
	 * @param taskId
	 * @param status
	 * @return
	 */
	List<BpmCustomSignData> getBrotherByTaskId(@Param("taskId")String taskId, @Param("status")List<String> status);
}
