package com.hotent.uc.dao;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.Demension;

/**
 * 
 * <pre> 
 * 描述：维度管理 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-19 15:30:09
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface DemensionDao extends BaseMapper<Demension>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 根据Code取定义对象。
	 * @param code
	 * @return
	 */
	Demension getByCode(@Param("code") String code);

	/**
	 * 获取默认维度
	 * @return
	 */
	Demension getDefaultDemension();

	/**
	 * 设置所有维度为非默认
	 * @param id
	 */
	void setNotDefaultDemension(@Param("updateTime") LocalDateTime updateTime);

	/**
	 * 根据code查询记录数
	 * @param code
	 * @return
	 */
	Integer getCountByCode(@Param("code") String code);
	
}

