package com.hotent.sys.persistence.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.exception.ServerRejectException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.persistence.dao.SysRoleAuthDao;
import com.hotent.sys.persistence.manager.SysRoleAuthManager;
import com.hotent.sys.persistence.model.SysRoleAuth;
import com.hotent.sys.persistence.param.SysRoleAuthParam;

/**
 * 
 * <pre> 
 * 描述：角色权限配置 处理实现类
 * 构建组：x6
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 14:27:46
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("sysRoleAuthManager")
public class SysRoleAuthManagerImpl extends BaseManagerImpl<SysRoleAuthDao, SysRoleAuth> implements SysRoleAuthManager{
	@Value("${system.mode.demo:false}")
    protected boolean demoMode;
	
	/**
	 * 删除方法与角色授权的缓存
	 */
	private void removeMethodRoleAuth() {
		SysRoleAuthManagerImpl bean = AppUtil.getBean(getClass());
		bean.delMethodRoleAuth();
	}
	
	@CacheEvict(value = CacheKeyConst.METHOD_AUTH_CACHENAME, allEntries = true)
	protected void delMethodRoleAuth() {}
	
	/**
	 * 删除用户资源授权的缓存
	 */
	private void removeUserMenu() {
		SysRoleAuthManagerImpl bean = AppUtil.getBean(getClass());
		bean.delUserMenu();
	}
	
	@CacheEvict(value = CacheKeyConst.USER_MENU_CACHENAME, allEntries = true)
	protected void delUserMenu() {}
	
	/**
	 * 删除数据授权缓存
	 */
	private void removeDataPermission() {
		SysRoleAuthManagerImpl bean = AppUtil.getBean(getClass());
		bean.delDataPermission();
	}
	
	@CacheEvict(value = CacheKeyConst.DATA_PERMISSION_CACHENAME, allEntries = true)
	protected void delDataPermission() {}
	
	@Override
	public List<SysRoleAuth> getSysRoleAuthByRoleAlias(String roleAlias) {
		return baseMapper.getSysRoleAuthByRoleAlias(roleAlias);
	}
	@Override
	public List<String> getMenuAliasByRoleAlias(String roleAlias) {
		return baseMapper.getMenuAliasByRoleAlias(roleAlias);
	}
	@Override
	public List<String> getMethodAliasByRoleAlias(String roleAlias) {
		return baseMapper.getMethodAliasByRoleAlias(roleAlias);
	}
	@Override
	public void removeByRoleAlias(String roleAlias) {
		baseMapper.removeByRoleAlias(roleAlias);
	}
	@Override
	public void removeByArrRoleAlias(String[] aryroleAlias) {
		for (String roleAlias : aryroleAlias) {
			baseMapper.removeByRoleAlias(roleAlias);
		}
		removeMethodRoleAuth();
		removeUserMenu();
		removeDataPermission();
	}
	
	@Override
	public void create(SysRoleAuthParam sysRoleAuthParam) {
		if(demoMode) {
    		throw new ServerRejectException("演示模式下无法执行该操作");
    	}
		baseMapper.removeByRoleAlias(sysRoleAuthParam.getRoleAlias());
		
		List<String> arrMenuAlias = sysRoleAuthParam.getArrMenuAlias();
		SysRoleAuth sysRoleAuth = new SysRoleAuth();
		sysRoleAuth.setRoleAlias(sysRoleAuthParam.getRoleAlias());
		for (String menuAlias : arrMenuAlias) {
			sysRoleAuth.setId(UniqueIdUtil.getSuid());
			sysRoleAuth.setMenuAlias(menuAlias);
			baseMapper.insert(sysRoleAuth);
		}
		
		sysRoleAuth.setMenuAlias(null);
		
		List<String> arrMethodAlias = sysRoleAuthParam.getArrMethodAlias();
		Map<String, String> dataPermission = sysRoleAuthParam.getDataPermission();
		for (String methodAlias : arrMethodAlias) {
			sysRoleAuth.setId(UniqueIdUtil.getSuid());
			sysRoleAuth.setMethodAlias(methodAlias);
			sysRoleAuth.setDataPermission(dataPermission.get(methodAlias));
			baseMapper.insert(sysRoleAuth);
		}
		removeMethodRoleAuth();
		removeUserMenu();
		removeDataPermission();
	}
	
	@Override
	public List<HashMap<String,String>> getSysRoleAuthAll() {
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		List<SysRoleAuth> sysRoleAuths = (ArrayList<SysRoleAuth>) baseMapper.getSysRoleAuthAll();
		for (SysRoleAuth sysRoleAuth : sysRoleAuths) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("roleAlias", sysRoleAuth.getRoleAlias());
			map.put("methodRequestUrl", sysRoleAuth.getMethodRequestUrl());
			map.put("dataPermission", sysRoleAuth.getDataPermission());
			result.add(map);
		}
		return result;
	}

	/**
	 * 权限复制（原角色权限复制给新的角色）
	 * @param oldCode 原角色别名
	 * @param newCodes 权限复制的角色别名
	 */
	@Override
	public void createCopy(String oldCode,String[] newCodes){
		//复制权限前先删除
		for(int i=0;i<newCodes.length;i++){
			baseMapper.removeByRoleAlias(newCodes[i]);
		}
		List<String> strList = getMenuAliasByRoleAlias(oldCode);//根据原角色查询原角色的菜单权限
		for(int i=0;i<newCodes.length;i++) {//循环赋值给新的角色
			for (String menuAlias : strList) {
				SysRoleAuth sysRoleAuth = new SysRoleAuth();
				sysRoleAuth.setId(UniqueIdUtil.getSuid());
				sysRoleAuth.setRoleAlias(newCodes[i]);
				sysRoleAuth.setMenuAlias(menuAlias);
				baseMapper.insert(sysRoleAuth);
			}
		}
		removeMethodRoleAuth();
		removeUserMenu();
	}
	
	@Override
	public void saveRoleMethods(SysRoleAuthParam sysRoleAuthParam) {
		if(demoMode) {
    		throw new ServerRejectException("演示模式下无法执行该操作");
    	}
		SysRoleAuth sysRoleAuth = new SysRoleAuth();
		sysRoleAuth.setRoleAlias(sysRoleAuthParam.getRoleAlias());
		List<String> methodAliasByRoleAlias = this.getMethodAliasByRoleAlias(sysRoleAuth.getRoleAlias());
		
		List<String> arrMethodAlias = sysRoleAuthParam.getArrMethodAlias();
		Map<String, String> dataPermission = sysRoleAuthParam.getDataPermission();
		for (String methodAlias : arrMethodAlias) {
			sysRoleAuth.setId(UniqueIdUtil.getSuid());
			sysRoleAuth.setMethodAlias(methodAlias);
			sysRoleAuth.setDataPermission(dataPermission.get(methodAlias));
			if(!methodAliasByRoleAlias.contains(methodAlias)) {
				this.create(sysRoleAuth);
			}
		}
		removeMethodRoleAuth();
		removeDataPermission();
	}
	
	@Override
	public void removeRoleMethods(String roleAlias, String[] methodAliasArr) {
		baseMapper.removeRoleMethods(roleAlias,methodAliasArr);
		removeMethodRoleAuth();
		removeDataPermission();
	}
}
