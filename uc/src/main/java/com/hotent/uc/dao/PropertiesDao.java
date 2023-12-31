package com.hotent.uc.dao;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.Properties;

/**
 * 
 * <pre> 
 * 描述：portal_sys_properties DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-07-28 09:19:53
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface PropertiesDao extends BaseMapper<Properties> {

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 分组列表。
	 * @return
	 */
	List<String> getGroups();
	
	
	/**
	 * 判断别名是否存在。
	 * @param sysProperties
	 * @return
	 */
	Integer isExist(@Param("code") String code,@Param("id") String id);

	/**
	 * 根据编码删除
	 * @param code
	 */
	void removeByCode(@Param("code") String code,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 根据编码获取
	 * @param code
	 * @return
	 */
	Properties getByCode(@Param("code") String code);
}
