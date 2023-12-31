package com.hotent.runtime.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.runtime.model.BpmTaskTransRecord;

/**
 * 任务流转记录 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface BpmTaskTransRecordDao extends BaseMapper<BpmTaskTransRecord> {
	
	/**
	 * 根据任务id获取流转任务记录
	 * @param taskId
	 * @return
	 */
	BpmTaskTransRecord getByTaskId(@Param("taskId") String taskId);
	
	/**
	 * 获取用户的流转记录列表
	 * @param queryFilter
	 * @return
	 */
	List<BpmTaskTransRecord> getTransRecord(IPage<BpmTaskTransRecord> page, @Param(Constants.WRAPPER) Wrapper<BpmTaskTransRecord> convert2Wrapper);
}
