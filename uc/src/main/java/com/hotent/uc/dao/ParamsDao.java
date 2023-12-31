package com.hotent.uc.dao;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.Params;
import org.springframework.web.bind.annotation.PatchMapping;

/**
 * 
 * <pre> 
 * 描述：组织参数 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2016-10-31 14:29:12
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface ParamsDao extends BaseMapper<Params>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 根据别名获取参数
	 * @param alias
	 * @return
	 */
	Params getByCode(@Param("code") String code);

	/**
	 * 根据类型获取参数
	 * @param type
	 * @return
	 */
	List<Params> getByType(@Param("type") String type);
	
	/**
	 * 根据类型获取参数
	 * @param type
	 * @return
	 */
	List<Params> getByTenantTypeId(@Param("tenantTypeId") String tenantTypeId);

	/**
	 * 查询参数列表（包含租户类型名称）
	 * @param page
	 * @param wrapper
	 * @return
	 */
	IPage queryWithType(IPage<Params> page,@Param(Constants.WRAPPER) Wrapper<Params> wrapper);

	Integer getCountByCode(String code);
}
