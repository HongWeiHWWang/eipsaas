package com.hotent.sys.persistence.manager.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.sys.persistence.dao.SysMethodDao;
import com.hotent.sys.persistence.dao.SysRoleAuthDao;
import com.hotent.sys.persistence.manager.SysMethodManager;
import com.hotent.sys.persistence.model.SysMethod;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre> 
 * 描述：系统请求方法的配置 （用于角色权限配置） 处理实现类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 14:23:28
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("sysMethodManager")
public class SysMethodManagerImpl extends BaseManagerImpl<SysMethodDao, SysMethod> implements SysMethodManager{
	@Resource
	SysRoleAuthDao sysRoleAuthDao;

	@Override
	public boolean isExistByAlias(String alias) {
		return baseMapper.isExistByAlias(alias)>0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCurrentUserMethodAuth() {
		List<String> userMethod = new ArrayList<String>();
		IUser currentUser = ContextUtil.getCurrentUser();
		
		Collection<SimpleGrantedAuthority> authorities =  (Collection<SimpleGrantedAuthority>) currentUser.getAuthorities();
		List<String> roles = new ArrayList<String>();
		if(BeanUtils.isNotEmpty(authorities)) {
			for (SimpleGrantedAuthority simpleGrantedAuthority : authorities) {
				roles.add(simpleGrantedAuthority.getAuthority());
			}
		}
		
		if(BeanUtils.isEmpty(roles)){
			return new ArrayList<String>();
		}
		userMethod = sysRoleAuthDao.getMethodByRoleAlias(roles);
		
		return userMethod;
	}
	@Override
	public List<Map<String, Object>> getAllMethodByRoleAlias(String roleAlias) {
		return baseMapper.getAllMethodByRoleAlias(roleAlias);
	}
	
	@Override
	public PageList<SysMethod> getRoleMethods(String roleAlias,QueryFilter queryFilter) {
    	PageBean pageBean = queryFilter.getPageBean();
    	IPage<SysMethod> page = new Page<SysMethod>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
    	Class<SysMethod> currentModelClass = currentModelClass();
    	Wrapper<SysMethod> convert2Wrapper = convert2Wrapper(queryFilter, currentModelClass);
    	List<SysMethod> roleMethods = baseMapper.getRoleMethods(page,convert2Wrapper);
		PageList<SysMethod> sysMethodPageList = new PageList<>(roleMethods);
		sysMethodPageList.setPage(page.getCurrent());
		sysMethodPageList.setPageSize(page.getSize());
		return sysMethodPageList;
	}
}
