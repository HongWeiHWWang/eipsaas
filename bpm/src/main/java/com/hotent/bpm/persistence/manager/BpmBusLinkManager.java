package com.hotent.bpm.persistence.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmBusLink;

public interface BpmBusLinkManager extends BaseManager<BpmBusLink>{

    /**
     * 根据流程实例ID 删除相关的关联表数据和对应的数据
     * @param instId
     * void
     * @throws Exception
     * @exception
     * @since  1.0.0
     */
    void removeDataByInstId(String instId) throws Exception;

	BpmBusLink getByBusinesKey(String businessKey,String formIdentity,boolean isNumber);
	
	BpmBusLink getByBusinesKey(String businessKey,boolean isNumber);
	/**
	 * 根据业务主键删除关联数据。
	 * @param businessKey
	 * @param formIdentity
	 * @param isNumber 
	 * void
	 */
	void delByBusinesKey(String businessKey,String formIdentity,boolean isNumber);
	
	
	/**
	 * 根据流程实例获取关联数据。
	 * @param instId
	 * @return 
	 * List&lt;BpmBusLink>
	 */
	List<BpmBusLink> getByInstId(String instId);
	
	
	/**
	 * 根据实例ID获取BO和关联数据的映射。
	 * @param instId
	 * @return
	 */
	Map<String, BpmBusLink> getMapByInstId(String instId);
	
	
	/**
	 * 根据流程ID 删除相关的关联表数据和对应的数据
	 * @param defId 
	 * void
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	void removeDataByDefId(String defId) throws Exception;
	
	/**
	 * 根据流程ID获取关联数据。
	 * @param instId
	 * @return 
	 * List&lt;BpmBusLink>
	 */
	List<BpmBusLink> getByDefId(String defId);
}
