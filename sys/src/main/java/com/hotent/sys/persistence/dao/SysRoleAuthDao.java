package com.hotent.sys.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.SysRoleAuth;

/**
 * 
 * <pre> 
 * 描述：角色权限配置 DAO接口
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 14:27:46
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysRoleAuthDao extends BaseMapper<SysRoleAuth> {

	List<SysRoleAuth> getSysRoleAuthByRoleAlias(String roleAlias);

	List<String> getMenuAliasByRoleAlias(String roleAlias);

	List<String> getMethodAliasByRoleAlias(String roleAlias);

	void removeByRoleAlias(String roleAlias);

	List<SysRoleAuth> getSysRoleAuthAll();

	List<String> getMethodByRoleAlias(@Param("roles") List<String> roles);

	void removeRoleMethods(@Param("roleAlias")  String roleAlias, @Param("methodAlias")  String[] methodAlias);
}
