package com.hotent.portal.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.portal.model.SysIndexColumn;


/**
 * 首页栏目 Dao类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月7日
 */
public interface SysIndexColumnDao extends BaseMapper<SysIndexColumn> {
	
	/**
	 * 是否已经存在别名
	 * @param params 参数
	 * @return		返回判断结果
	 */
	@SuppressWarnings("rawtypes")
	Integer isExistAlias(Map params);
	
	/**
	 * 通过别名获取栏目
	 * 
	 * @param alias 别名
	 * @return		返回首页栏目对象
	 */
	public SysIndexColumn getByColumnAlias(String alias);
	
	/**
	 * 通过别名数组批量获取栏目
	 * @param aliases
	 * @param hasRightsColIds 有展示权限的栏目ids
	 * @return
	 */
	public List<SysIndexColumn> batchGetByColumnAliases(@Param("aliases") List<String> aliases, @Param("hasRightsColIds") String hasRightsColIds);
 
	/**
	 * 获取当前用户有权限的栏目
	 * 
	 * @param params Map参数
	 * @return		 返回首页栏目集合
	 */
	public List<SysIndexColumn> getByUserIdFilter(Map<String, Object> params);
	
	/**
	 * 返回统计数量
	 * @return 
	 */
	public Integer getCounts();

	public List<SysIndexColumn> getAllByLayoutType(@Param("isPublic") Short isPublic);
}