package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmProBo;


public interface BpmProBoDao extends BaseMapper<BpmProBo> {
	
	/**
	 * 根据流程信息（ID或者KEY）删除流程和业务对象的绑定信息
	 * @param map 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void removeByProcess(Map<String,Object> params);
	
	/**
	 * 根据业务对象的标识（code）删除流程和业务对象的绑定信息
	 * @param boCode 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void removeByBoCode(@Param("boCode") String boCode);
	
	/**
	 * 根据流程信息（ID或者KEY）获得流程和业务对象的绑定信息
	 * @param map
	 * @return 
	 * List<BpmProBo>
	 * @exception 
	 * @since  1.0.0
	 */
	List<BpmProBo> getByProcess(Map<String,Object> params);
	
	/**
	 * 根据业务对象的标识（code）获得流程和业务对象的绑定信息
	 * @param boCode
	 * @return 
	 * List<BpmProBo>
	 * @exception 
	 * @since  1.0.0
	 */
	List<BpmProBo> getByBoCode(@Param("boCode") String boCode);
	
}
