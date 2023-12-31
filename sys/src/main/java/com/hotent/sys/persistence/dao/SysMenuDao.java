package com.hotent.sys.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.sys.persistence.model.SysMenu;

/**
 * 
 * <pre> 
 * 描述：系统菜单 DAO接口
 * 构建组：eip
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 09:34:15
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysMenuDao extends BaseMapper<SysMenu> {
	
	SysMenu getByAlias(String alias);
	
	int isExistByAlias(String alias);
	
	List<SysMenu> getByParentId(String parentId);
	
	/**
	 * 根据父菜单的路径获取子菜单（包含父菜单）
	 * @param path
	 * @return
	 */
	List<SysMenu> getByChidrensParentPath(@Param("path")String path);

	/**
	 * 获取角色对应的菜单
	 * @param roles
	 * @return
	 */
	List<SysMenu> getMenuByRoleAlias(@Param("roles") List<String> roles,@Param("ignoreMenus") List<String> ignoreMenus);

	List<Map<String, Object>> getAllMenuRoleAlias(@Param("roleAlias") String roleAlias,@Param(Constants.WRAPPER) QueryWrapper<SysMenu> queryWrapper);
	
}
