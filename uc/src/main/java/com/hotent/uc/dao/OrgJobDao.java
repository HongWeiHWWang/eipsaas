package com.hotent.uc.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.OrgJob;
import org.springframework.security.core.parameters.P;

/**
 * 
 * <pre> 
 * 描述：组织关系定义 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-29 18:00:43
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgJobDao extends BaseMapper<OrgJob>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 根据编码获取职务
	 * @param code
	 * @return
	 */
	OrgJob getByCode(@Param("code") String code);
	
	/**
	 * 根据名称获取职务
	 * @param name
	 * @return
	 */
	List<OrgJob> getByName(@Param("name") String name);

	/**
	 * 根据code查询记录数
	 * @param code
	 * @return
	 */
	Integer getCountByCode(@Param("code") String code);

}
