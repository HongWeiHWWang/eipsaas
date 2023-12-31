package com.hotent.bpm.persistence.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmCptoReceiver;


public interface BpmCptoReceiverDao extends BaseMapper<BpmCptoReceiver> {

	BpmCptoReceiver getByCopyToId(@Param("copToId") String copToId,@Param("receiverId") String userId);
}
