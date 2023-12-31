package com.hotent.sys.persistence.dao;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.sys.persistence.model.SysMethod;

/**
 * 
 * <pre> 
 * 描述：系统请求方法的配置 （用于角色权限配置） DAO接口
 * 构建组：x6
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 14:23:28
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysMethodDao extends BaseMapper<SysMethod> {
	
	void removeByMenuId(Serializable menuId);
	
	List<SysMethod> getByMenuAlias(String menuAlias);
	
	int  isExistByAlias(String alias);

	List<Map<String, Object>> getAllMethodByRoleAlias(String roleAlias);

	List<SysMethod> getRoleMethods(IPage<SysMethod> page, @Param(Constants.WRAPPER) Wrapper<SysMethod> convert2Wrapper);
}
