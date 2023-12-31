package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmBusLink;


public interface BpmBusLinkDao extends BaseMapper<BpmBusLink> {

	
	BpmBusLink getByBusinesKey(Map<String, Object> params);
	/**
	 * 根据业务主键删除数据。
	 * @param businessKey
	 * @param formIdentity
	 * @param isNumber 
	 * void
	 */
	void delByBusinesKey(Map<String, Object> params);
	
	/**
	 * 根据流程实例获取关联数据(主实体)。
	 * @param instId
	 * @return 
	 * List&lt;BpmBusLink>
	 */
	List<BpmBusLink> getByInstId(@Param("procInstId") String instId);
	
	/**
	 * 根据流程实例获取关联数据(主子实体等)。
	 * @param instId
	 * @return
	 */
	List<BpmBusLink> getAllByInstId(@Param("procInstId") String instId);


	String getMysqlVersion();
	
	/**
	 * 根据流程ID获取关联数据。
	 * @param instId
	 * @return 
	 * List&lt;BpmBusLink>
	 */
	List<BpmBusLink> getByDefId(@Param("defId") String defId);
	
}
