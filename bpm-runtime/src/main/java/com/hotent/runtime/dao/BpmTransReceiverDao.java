package com.hotent.runtime.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.runtime.model.BpmTransReceiver;

/**
 * 流转任务接收人 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface BpmTransReceiverDao extends BaseMapper<BpmTransReceiver> {
	
	/**
	 * 根据transRecordid获取接收人列表
	 * @param transRecordid
	 * @return
	 */
	List<BpmTransReceiver> getByTransRecordid(@Param("transRecordid") String transRecordid);
	
	/**
	 * 通过任务ID获取流转接收人列表
	 * @param taskId
	 * @return
	 */
	List<BpmTransReceiver> getByTaskId(@Param("taskId") String taskId);
	
	/**
	 * 根据transRecordid和人员id获取数据
	 * @param params
	 * @return
	 */
	BpmTransReceiver getByTransRecordAndUserId(Map<String,String> params);

    /**
     * 修改流转接收人信息
     * @param params
     */
    void updateReceiver(Map<String,Object> params);
}
