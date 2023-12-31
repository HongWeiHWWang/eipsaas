package com.hotent.runtime.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.runtime.model.BpmTaskTrans;

/**
 * 任务流转 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface BpmTaskTransDao extends BaseMapper<BpmTaskTrans> {
	
	/**
	 * 根据任务获取会签情况的配置。
	 * @param taskId
	 * @return BpmTaskTrans
	 */
	BpmTaskTrans getByTaskId(@Param("taskId") String taskId);
	
	/**
	 * 根据流程实例id 删除流转数据
	 * 撤回发起人时 需要用到
	 * @param instanceId
	 */
	void removeByInstId(@Param("instanceId") String instanceId);
	
}
