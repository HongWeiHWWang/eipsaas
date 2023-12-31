package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmCommuReceiver;


public interface BpmCommuReceiverDao extends BaseMapper<BpmCommuReceiver> {
	
	/**
	 * 根据commuId和接收人ID取得接收人对象。 
	 * @param commuId
	 * @param receiverId
	 * @return 
	 * BpmCommuReceiver
	 */
	BpmCommuReceiver getByCommuUser(@Param("commuId") String commuId,@Param("receiverId") String receiverId);

    /**
     * 根据commuId取得接收人对象。
     * @param commuId
     * @param receiverId
     * @return
     * BpmCommuReceiver
     */
    BpmCommuReceiver getByCommuId(@Param("commuId") String commuId);
	/**
	 * 通过通知，状态获取所有的接收人消息
	 * @param commuId
	 * @param status
	 * @return
	 */
	List<BpmCommuReceiver> getByCommuStatus(@Param("commuId") String commuId,@Param("status") String status);
	
	Integer checkHasCommued(@Param("commuId")String commuId,@Param("receiverId") String receiverId);
}
