package com.hotent.bpm.persistence.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmDefData;


public interface BpmDefDataDao extends BaseMapper<BpmDefData> {
	
	/**
	 * 根据流程定义删除流程数据。
	 */
	void delByDefKey(@Param("defKey") String defKey);
}
