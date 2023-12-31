package com.hotent.bpm.persistence.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmProBo;

public interface BpmProBoManager extends BaseManager<BpmProBo>{
	
	/**
	 * 根据流程信息（ID）删除流程和业务对象的绑定信息
	 * @param processId 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void removeByProcessId(String processId);
	
	/**
	 * 根据流程信息（KEY）删除流程和业务对象的绑定信息
	 * @param processKey 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void removeByProcessKey(String processKey);
	
	/**
	 * 根据业务对象的标识（code）删除流程和业务对象的绑定信息
	 * @param boCode 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void removeByBoCode(String boCode);
	
	/**
	 * 根据流程信息（ID）获得流程和业务对象的绑定信息
	 * @param processId
	 * @return 
	 * List<BpmProBo>
	 * @exception 
	 * @since  1.0.0
	 */
	List<BpmProBo> getByProcessId(String processId);
	
	
	/**
	 * 根据流程信息（ID或者KEY）获得流程和业务对象的绑定信息
	 * @param processKey
	 * @return 
	 * List<BpmProBo>
	 * @exception 
	 * @since  1.0.0
	 */
	List<BpmProBo> getByProcessKey(String processKey);
	

	/**
	 * 根据业务对象的标识（code）获得流程和业务对象的绑定信息
	 * @param boCode
	 * @return 
	 * List<BpmProBo>
	 * @exception 
	 * @since  1.0.0
	 */
	List<BpmProBo> getByBoCode(String boCode);
	
	
	/**
	 * 根据流程信息（ID）删除流程和业务对象的绑定信息
	 * @param bpmProBoList 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void createByBpmProBoList(List<BpmProBo> bpmProBoList);

	List<BpmProBo> getByProcess(Map<String, Object> params);
	
}
