package com.hotent.uc.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.dao.TenantIgnoreMenuDao;
import com.hotent.uc.manager.TenantIgnoreMenuManager;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.model.TenantIgnoreMenu;
import com.hotent.uc.model.TenantManage;

/**
 * 
 * <pre> 
 * 描述：租户禁用菜单 处理实现类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-20 17:00:53
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("tenantIgnoreMenuManager")
public class TenantIgnoreMenuManagerImpl extends BaseManagerImpl<TenantIgnoreMenuDao, TenantIgnoreMenu> implements TenantIgnoreMenuManager{
	@Resource
	TenantManageManager tenantManageManager;
	
	@Override
	public List<TenantIgnoreMenu> getByTenantId(String tenantId) {
		return baseMapper.getByTenantId(tenantId);
	}

	@Override
	@Transactional
	public void deleteByTenantId(String tenantId) {
		baseMapper.deleteByTenantId(tenantId);
	}

	@Override
    @Transactional
    @CacheEvict(value = CacheKeyConst.USER_MENU_CACHENAME, allEntries = true)
	public CommonResult<String> saveByTenantId(String tenantId,
			List<String> ignoreMenus) {
		if(StringUtil.isEmpty(tenantId)){
			return new CommonResult<String>(false, "租户禁用菜单信息保存失败：租户ID不能为空。");
		}
		TenantManage tenant = tenantManageManager.get(tenantId);
		if(BeanUtils.isEmpty(tenant)){
			return new CommonResult<String>(false, "租户禁用菜单信息保存失败：租户ID【"+tenantId+"】不存在。");
		}
		baseMapper.deleteByTenantId(tenantId);
		if(BeanUtils.isNotEmpty(ignoreMenus)){
			BeanUtils.removeDuplicate(ignoreMenus);
			List<TenantIgnoreMenu> nmenus = new ArrayList<TenantIgnoreMenu>();
			for (String alias : ignoreMenus) {
				TenantIgnoreMenu menu = new TenantIgnoreMenu();
				menu.setMenuCode(alias);
				menu.setTenantId(tenantId);
				nmenus.add(menu);
			}
			this.saveBatch(nmenus);
		}
		return new CommonResult<String>(true, "租户禁用菜单信息保存成功。");
	}
}
