package com.hotent.sys.persistence.manager.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.conf.SaaSConfig;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.ServerRejectException;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.persistence.dao.SysMenuDao;
import com.hotent.sys.persistence.dao.SysMethodDao;
import com.hotent.sys.persistence.manager.SysMenuManager;
import com.hotent.sys.persistence.model.SysMenu;
import com.hotent.sys.persistence.model.SysMethod;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre>
 *  
 * 描述：系统菜单 处理实现类
 * 构建组：x6
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 09:34:15
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("sysMenuManager")
public class SysMenuManagerImpl extends BaseManagerImpl<SysMenuDao, SysMenu> implements SysMenuManager {
	@Resource
	SysMethodDao sysMethodDao;
	@Resource
	UCFeignService uCFeignService;
	@Value("${system.mode.demo:false}")
	protected boolean demoMode;
	@Autowired
	SaaSConfig saaSConfig;
	@Resource
	MultiTenantHandler multiTenantHandler;

	/**
	 * 根据别名获取菜单详情 包含请求的方法
	 */
	@Override
	public SysMenu getByAlias(String alias) {
        SysMenu sysMenu = null;
        try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
            sysMenu = baseMapper.getByAlias(alias);
            if (BeanUtils.isNotEmpty(sysMenu)) {
                List<SysMethod> sysMethods = sysMethodDao.getByMenuAlias(sysMenu.getAlias());
                if (BeanUtils.isNotEmpty(sysMethods)) {
                    sysMenu.setSysMethods(sysMethods);
                }
            }
            return sysMenu;
        }catch (Exception el){
            el.printStackTrace();
        }
        return sysMenu;
	}

	/**
	 * 获取菜单详情 包含请求的方法
	 */
	@Override
	public SysMenu get(Serializable entityId) {
		SysMenu sysMenu = super.get(entityId);
		if (BeanUtils.isNotEmpty(sysMenu)) {
			List<SysMethod> sysMethods = sysMethodDao.getByMenuAlias(sysMenu.getAlias());
			if (BeanUtils.isNotEmpty(sysMethods)) {
				sysMenu.setSysMethods(sysMethods);
			}
		}
		return sysMenu;
	}

	/**
	 * 添加菜单时 同时添加权限方法方法
	 */
	@Override
	public void create(SysMenu entity) {
		super.create(entity);

		sysMethodDao.removeByMenuId(entity.getId());
		List<SysMethod> sysMethods = entity.getSysMethods();
		if (BeanUtils.isNotEmpty(sysMethods)) {
			for (SysMethod sysMethod : sysMethods) {
				sysMethod.setMenuAlias(entity.getAlias());
				sysMethod.setPath(entity.getPath());
				sysMethod.setId(UniqueIdUtil.getSuid());
				sysMethodDao.insert(sysMethod);
			}
		}
		removeUserMenuCache();
	}

	@Override
	public void update(SysMenu entity) {
		if (demoMode) {
			throw new ServerRejectException("演示模式下无法执行该操作");
		}
		super.update(entity);
		sysMethodDao.removeByMenuId(entity.getId());
		List<SysMethod> sysMethods = entity.getSysMethods();
		if (BeanUtils.isNotEmpty(sysMethods)) {
			for (SysMethod sysMethod : sysMethods) {
				sysMethod.setMenuAlias(entity.getAlias());
				sysMethod.setPath(entity.getPath());
				sysMethod.setId(UniqueIdUtil.getSuid());
				sysMethodDao.insert(sysMethod);
			}

		}
		removeUserMenuCache();
	}

	/**
	 * 删除菜单时 同时删除方法请求 1. 先删除从表（sys_method）的数据 从表的数据是根据主表的数据获取别名删除的 2.
	 * 再删除portal_sys_menu数据
	 */
	@Override
	public void remove(Serializable entityId) {
		if (demoMode) {
			throw new ServerRejectException("演示模式下无法执行该操作");
		}
		sysMethodDao.removeByMenuId(entityId);
		super.remove(entityId);
	}

	@Override
	public boolean isExistByAlias(String alias) {
		return baseMapper.isExistByAlias(alias) > 0;
	}

	@Override
	public void removeByResId(String resId) {
		if (demoMode) {
			throw new ServerRejectException("演示模式下无法执行该操作");
		}
		List<SysMenu> list = getRecursionById(resId);
		for (SysMenu resource : list) {
			this.remove(resource.getId());
		}
		removeUserMenuCache();
	}

	private List<SysMenu> getRecursionById(String resId) {
		List<SysMenu> list = new ArrayList<SysMenu>();

		SysMenu resource = this.get(resId);
		list.add(resource);

		List<SysMenu> tmpList = baseMapper.getByParentId(resId);
		if (BeanUtils.isEmpty(tmpList))
			return list;

		for (SysMenu sysMenu : tmpList) {
			recursion(sysMenu, list);
		}
		return list;
	}

	private void recursion(SysMenu sysMenu, List<SysMenu> list) {
		list.add(sysMenu);
		List<SysMenu> tmpList = baseMapper.getByParentId(sysMenu.getId());
		if (BeanUtils.isEmpty(tmpList))
			return;

		for (SysMenu resource : tmpList) {
			recursion(resource, list);
		}
	}
	
	@Cacheable(value = CacheKeyConst.USER_MENU_CACHENAME, key="#currentUser.userId")
	protected String getMenuByUserId(IUser currentUser) throws IOException{
        List<SysMenu> dbMenus = null;

        if(currentUser.isAdmin()){
            dbMenus = this.getAllByTenant(null);
        }

        if( BeanUtils.isEmpty(dbMenus) ){
            List<ObjectNode>  roles =  uCFeignService.getRoleListByAccount(currentUser.getAccount());
            List<String> roleCodes = new ArrayList<String>();
            if(BeanUtils.isNotEmpty(roles)){
                for (ObjectNode role : roles) {
                    if (1 == role.get("enabled").asInt()) {
                        roleCodes.add(role.get("code").asText());
                    }
                }
                if (BeanUtils.isNotEmpty(roleCodes)) {
                	try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
                		List<String> ignoreMenus = null;
                		if(!TenantConstant.PLATFORM_TENANT_ID.equals(currentUser.getTenantId())){
                			ignoreMenus = uCFeignService.getIgnoreMenuCodes(currentUser.getTenantId());
                		}
                		dbMenus = baseMapper.getMenuByRoleAlias(roleCodes,BeanUtils.isEmpty(ignoreMenus)?null:ignoreMenus);
                	}
                	catch(Exception e) {
                		throw new BaseException(e.getMessage());
                	}
                }
            }
        }
        if(BeanUtils.isEmpty(dbMenus)) {
            dbMenus= new ArrayList<SysMenu>();
        }
        unique(dbMenus);
        String json = JsonUtil.toJson(dbMenus);
        return json;
	}

	@Override
	public List<SysMenu> getCurrentUserMenu() throws Exception {
		IUser currentUser = ContextUtil.getCurrentUser();
		if(BeanUtils.isEmpty(currentUser)) {
			throw new BaseException("未获取到当前登录用户");
		}
		SysMenuManagerImpl bean = AppUtil.getBean(getClass());
		String menuByUserId = bean.getMenuByUserId(currentUser);
		List<SysMenu> list = new ArrayList<>();
		if(menuByUserId!=null) {
			list = JsonUtil.toBean(menuByUserId, new TypeReference<List<SysMenu>>(){});
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getAllMenuRoleAlias(String roleAlias) {
		QueryWrapper<SysMenu> queryWrapper = getTenantQueryWrapper();
		List<Map<String, Object>> result = null;
		try(MultiTenantIgnoreResult multiTenantIgnoreResult = MultiTenantHandler.setThreadLocalIgnore()){
			result = baseMapper.getAllMenuRoleAlias(roleAlias, queryWrapper);
		}
		catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
		return result;
	}

	// 去除列表中的重复项
	private void unique(List<SysMenu> objects) {
		for (int i = 0; i < objects.size() - 1; i++) {
			for (int j = objects.size() - 1; j > i; j--) {
				if (objects.get(j).getId().equals(objects.get(i).getId())) {
					objects.remove(j);
				}
			}
		}
	}

	@Override
	public List<SysMenu> filterByMenuAlias(String menuAlias, List<SysMenu> lists) {
        List<SysMenu> result = new ArrayList<SysMenu>();
        if(BeanUtils.isEmpty(lists) || StringUtil.isEmpty(menuAlias)) {
            return result;
        }
        String pId = null;
        // 获取菜单别名对应id
        for(SysMenu sysMenu : lists) {
            if(menuAlias.equals(sysMenu.getAlias())) {
                pId = sysMenu.getId();
                break;
            }
        }
        if(StringUtil.isEmpty(pId)) {
            return result;
        }
        // 通过parentId来过滤菜单数组
        BeanUtils.listByPid(lists, pId, result);
        // 将返回的数组由平铺结果转换为nest结构
        return BeanUtils.listToTree(result);
	}

	@Override
	public List<SysMenu> getMenuByRoleAlias(String roleAlias) {
		return baseMapper.getMenuByRoleAlias(Arrays.asList(roleAlias.split(",")),null);
	}

	@Override
	public List<SysMenu> getAllByTenant(String ignoreAlias) {
		List<SysMenu> dbMenus = new ArrayList<SysMenu>();
		QueryWrapper<SysMenu>  queryWrapper = getTenantQueryWrapper();
		//处理忽略菜单
		dealWithIgnoreQuery(queryWrapper, ignoreAlias);
        try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
        	dbMenus = this.list(queryWrapper);
        }
        catch(Exception e) {
        	throw new BaseException(e.getMessage());
        }
		return dbMenus;
	}
	
	/**
	 * 处理忽略菜单以及下级菜单
	 * @param queryWrapper
	 * @param ignoreAlias
	 */
	private void dealWithIgnoreQuery(QueryWrapper<SysMenu>  queryWrapper,String ignoreAlias){
		if(StringUtil.isNotEmpty(ignoreAlias)){
			String[] ignores = ignoreAlias.split(",");
			List<String> allIgnores = new ArrayList<>();
			try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
				for (String alias : ignores) {
					SysMenu menu = baseMapper.getByAlias(alias);
					if(BeanUtils.isNotEmpty(menu)){
						allIgnores.add(alias);
						List<SysMenu> childrens = baseMapper.getByChidrensParentPath(menu.getPath());
						if(BeanUtils.isNotEmpty(childrens)){
							for (SysMenu sysMenu : childrens) {
								allIgnores.add(sysMenu.getAlias());
							}
						}
					}
					
				}
			}
			catch(Exception e) {
				throw new BaseException(e.getMessage());
			}
			if(BeanUtils.isNotEmpty(allIgnores)){
				BeanUtils.removeDuplicate(allIgnores);
				allIgnores.addAll(TenantConstant.IGNORE_MENU);
				queryWrapper.and(consumer -> {
					consumer.notIn("alias_", allIgnores);
				});
			}
		}
	}

	private QueryWrapper<SysMenu> getTenantQueryWrapper() {
		IUser currentUser = ContextUtil.getCurrentUser();
		QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>();
		if (saaSConfig.isEnable()) {
			queryWrapper.or().in(multiTenantHandler.getTenantIdColumn(),
					Arrays.asList(TenantConstant.PLATFORM_TENANT_ID, currentUser.getTenantId()));
			if (!TenantConstant.PLATFORM_TENANT_ID.equals(currentUser.getTenantId())) {
				queryWrapper.and(consumer -> {
					List<String> allIgnoreMenus = new ArrayList<String>();
					allIgnoreMenus.addAll(TenantConstant.IGNORE_MENU);
					try {
						List<String> ignoreMenus = uCFeignService.getIgnoreMenuCodes(currentUser.getTenantId());
						if (BeanUtils.isNotEmpty(ignoreMenus)) {
							allIgnoreMenus.addAll(ignoreMenus);
						}
					} catch (Exception e) {
						throw new BaseException("获取当前租户禁用菜单出错：" + e.getMessage());
					}
					consumer.notIn("alias_", allIgnoreMenus);
				});

			}
		}
		queryWrapper.orderByAsc("sn_");
		return queryWrapper;
	}
	
	private void removeUserMenuCache() {
		SysMenuManagerImpl bean = AppUtil.getBean(getClass());
		bean.delUserMenuCache();
	}

	@Override
	@CacheEvict(value = CacheKeyConst.USER_MENU_CACHENAME, allEntries = true)
	public void delUserMenuCache() {}
}
