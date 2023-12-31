package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.persistence.model.BpmSignData;

/**
 * 会签数据访问。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-30-下午11:16:48
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmSignDataDao extends BaseMapper<BpmSignData> {
	
	
	/**
	 * 根据实例ID获取会签结果。
	 * @param executeId
	 * @param nodeId
	 * @param isActive
	 * @return
	 */
	List<BpmSignData> getVoteByExecuteNode(@Param("executeId") String executeId,@Param("nodeId") String nodeId,@Param("isActive") Integer isActive);
	
	
	
	
	/**
	* 根据流程实例列表删除任务。
	* @param instList 
	* void
	*/
	void delByInstList(@Param("list") List<String> instList) ;
	
	
	/**
	 * 根据运行实例ID,节点ID和节点索引。
	 * @param executeId
	 * @param nodeId
	 * @param index
	 * @return  BpmSignData
	 */
	BpmSignData getByExeNodeIndex(@Param("executeId") String executeId,@Param("nodeId") String nodeId,@Param("index") Short index);
	
	/**
	 * 会签完成更新会签数据状态为不活动。
	 * @param executeId
	 * @param nodeId
	 */
	void updByNotActive(@Param("executeId") String executeId,@Param("nodeId") String nodeId);
	
	/**
	 * 删除非活动的会签数据。
	 * @param executeId
	 * @param nodeId
	 */
	void removeByNotActive(@Param("executeId") String executeId,@Param("nodeId") String nodeId);
	
	/**
	 * 根据流程实例id和用户id获取会签数据
	 * @param instancId
	 * @param userId
	 * @return
	 */
	BpmSignData getByInstanIdAndUserId(@Param("instancId") String instancId,@Param("userId") String userId,@Param("taskId") String taskId);



	/**
	 * 获取未投票的用户
	 * @param instId
	 * @param nodeId
	 * @return
	 */
	List<BpmSignData> getByInstanIdAndNodeIdAndNo(@Param("instId") String instId, @Param("nodeId")String nodeId);


	/*
	 * 删除未投票的用户 等待重新生成
	 */

	void deleteByInstanIdAndNodeIdAndNo(@Param("instId") String instId, @Param("nodeId")String nodeId);


	/**
	 * 更新为活动状态
	 * @param instId
	 * @param nodeId
	 */
	void updByActive(@Param("instId") String instId, @Param("nodeId")String nodeId);
	
}
