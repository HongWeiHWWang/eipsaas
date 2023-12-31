package com.hotent.runtime.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.runtime.model.BpmTransReceiver;

/**
 * 流转任务接收人 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface BpmTransReceiverManager extends BaseManager<BpmTransReceiver>{
	
	/**
	 * 根据transRecordid获取接收人列表
	 * @param transRecordid
	 * @return
	 */
	List<BpmTransReceiver> getByTransRecordid(String transRecordid);
	
	/**
	 * 通过任务ID获取流转接收人列表
	 * @param taskId
	 * @return
	 */
	List<BpmTransReceiver> getByTaskId(String taskId);
	
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
