package com.hotent.portal.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.exception.BaseException;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.i18n.util.I18nUtil;
import com.hotent.portal.params.TemplateToMenuVo;
import com.hotent.sys.persistence.manager.SysMenuManager;
import com.hotent.sys.persistence.manager.SysMethodManager;
import com.hotent.sys.persistence.manager.SysRoleAuthManager;
import com.hotent.sys.persistence.model.SysMenu;
import com.hotent.sys.persistence.model.SysMethod;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 系统菜单 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author liyg
 * @email liyg@jee-soft.cn
 * @date 2018-07-02 17:18:55
 */
@RestController
@RequestMapping("/sys/sysMenu/v1")
@Api(tags="系统菜单")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysMenuController extends BaseController<SysMenuManager, SysMenu>{
	@Resource
	SysMenuManager sysMenuManager;
	@Resource
	SysMethodManager sysMethodManager;
	@Resource
	SysRoleAuthManager sysRoleAuthManager;

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "菜单资源数据", httpMethod = "POST", notes = "菜单资源数据")
	public PageList<SysMenu> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysMenu> queryFilter) throws Exception {
		return sysMenuManager.query(queryFilter);
	}

	@RequestMapping(value="getTree", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获得树形菜单", httpMethod = "GET", notes = "获得树形菜单")
	public List<SysMenu> getTree(@ApiParam(name="ignoreAlias",value="忽略菜单（多个用逗号隔开）", required = true)@RequestParam Optional<String> ignoreAlias) throws Exception {
		List<SysMenu> list =sysMenuManager.getAllByTenant(ignoreAlias.orElse(""));
		list=BeanUtils.listToTree(list);
		return list;
	}
	
	@RequestMapping(value="saveMenus", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存资源菜单", httpMethod = "POST", notes = "保存资源菜单")
	public CommonResult<String> saveMenus(@ApiParam(name="list",value="菜单列表")@RequestBody List<SysMenu> list) throws Exception {
		List<SysMenu> oldList =sysMenuManager.list();
		Map<String, SysMenu> oldMap = new HashMap<>();
		for (SysMenu sysMenu : oldList) {
			oldMap.put(sysMenu.getId(), sysMenu);
		}
		List<SysMenu> changeMenus = new ArrayList<>();
		for (SysMenu menu : list) {
			if (!menu.equals(oldMap.get(menu.getId()))) {
				changeMenus.add(menu);
			}
		}
		for (SysMenu sysMenu : changeMenus) {
			sysMenuManager.update(sysMenu);
		}
		baseService.delUserMenuCache();
		return new CommonResult<>("保存成功");
	}
	
	/**
	 * 
	 * @param roleAlias
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getMenuByRoleAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获得角色树形菜单", httpMethod = "GET", notes = "获得角色树形菜单")
	public List<SysMenu> getMenuByRoleAlias(@ApiParam(name="roleAlias",value="角色别名", required = true)@RequestParam String roleAlias) throws Exception {
		return sysMenuManager.getMenuByRoleAlias(roleAlias);
	}
	
	/**
	 * 
	 * @param roleAlias
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getAllMenuRoleAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获得角色树形菜单", httpMethod = "GET", notes = "获得角色树形菜单")
	public ArrayList<Map<String,Object>> getAllMenuRoleAlias(@ApiParam(name="roleAlias",value="角色别名", required = true)@RequestParam String roleAlias) throws Exception {
		return (ArrayList<Map<String, Object>>) sysMenuManager.getAllMenuRoleAlias(roleAlias);
	}
	
	/**
	 * 获取授权的后台方法
	 * @param roleAlias
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getAllMethodByRoleAlias", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取授权的后台方法", httpMethod = "POST", notes = "获取授权的后台方法")
	public ArrayList<Map<String,Object>> getAllMethodByRoleAlias(@ApiParam(name="roleAlias",value="角色别名", required = true)@RequestParam String roleAlias) throws Exception {
		return (ArrayList<Map<String, Object>>) sysMethodManager.getAllMethodByRoleAlias(roleAlias);
	}
	
	

	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "菜单资源数据明细页面", httpMethod = "GET", notes = "菜单资源数据明细页面")
	public @ResponseBody SysMenu getJson(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		SysMenu sysMenu=new SysMenu();
		sysMenu.setOpen(true);
		if(!StringUtil.isEmpty(id)){
			try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
				sysMenu=sysMenuManager.get(id);
			}
		}
		return sysMenu;
	}

	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存菜单资源数据信息", httpMethod = "POST", notes = "保存菜单资源数据信息")
	public CommonResult<String> save(@ApiParam(name="sysMenu", value="菜单资源", required = true)@RequestBody SysMenu sysMenu) throws Exception {
		String resultMsg=null;
		String id=sysMenu.getId();
		if(StringUtil.isEmpty(id)){
			if(sysMenuManager.isExistByAlias(sysMenu.getAlias())){
				throw new BaseException("菜单别名已存在");
			}
			List<SysMethod> sysMethods = sysMenu.getSysMethods();
			for (int i = 0; i < sysMethods.size()-1; i++) {
				for (int j = i+1; j < sysMethods.size(); j++) {
					if (sysMethods.get(i).getAlias().equals(sysMethods.get(j).getAlias())){
						return new CommonResult<String>(false, "请求方法别名重复");
					}
				}
			}
			sysMenu.setId(UniqueIdUtil.getSuid());
			sysMenu.setPath(sysMenu.getPath()+sysMenu.getId()+".");
			if(BeanUtils.isEmpty(sysMenu.getSn())){
				sysMenu.setSn(99);
			}
			sysMenuManager.create(sysMenu);
			resultMsg="添加菜单信息成功";
		}else{
			List<SysMethod> sysMethods = sysMenu.getSysMethods();
			for (int i = 0; i < sysMethods.size()-1; i++) {
				for (int j = i+1; j < sysMethods.size(); j++) {
					if (sysMethods.get(i).getAlias().equals(sysMethods.get(j).getAlias())){
						return new CommonResult<String>(false, "请求方法别名重复");
					}
				}
			}
			sysMenuManager.update(sysMenu);
			resultMsg="更新菜单信息成功";
		}
		baseService.delUserMenuCache();
		return new CommonResult<String>(true, resultMsg);
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除菜单信息记录", httpMethod = "DELETE", notes = "批量删除菜单信息记录")
	public CommonResult<String> remove(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		sysMenuManager.removeByResId(id);
		return new CommonResult<String>(true, "删除菜单信息成功");
	}

	@RequestMapping(value="isMenuExistByAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "检测菜单资源别名是否已经存在", httpMethod = "GET", notes = "检测菜单资源别名是否已经存在")
	public @ResponseBody CommonResult<Boolean> isMenuExistByAlias(@ApiParam(name="alias", value="菜单别名", required = true)@RequestParam String alias) throws Exception {
        boolean existByAlias = sysMenuManager.isExistByAlias(alias);
        CommonResult<Boolean> commonResult = new CommonResult<Boolean>();
        commonResult.setValue(existByAlias);
        return commonResult;
	}

	@RequestMapping(value="isMethodExistByAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "检测请求方法别名是否已经存在", httpMethod = "GET", notes = "检测请求方法别名是否已经存在")
	public @ResponseBody CommonResult<Boolean> isMethodExistByAlias(@ApiParam(name="alias", value="菜单别名", required = true)@RequestParam String alias) throws Exception {
		boolean existByAlias = sysMethodManager.isExistByAlias(alias);
		CommonResult<Boolean> commonResult = new CommonResult<Boolean>();
		commonResult.setValue(existByAlias);
		return commonResult;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getCurrentUserMenu", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取当前用户的菜单", httpMethod = "GET", notes = "获取当前用户的菜单")
	public @ResponseBody CommonResult<List<SysMenu> > getCurrentUserMenu(@ApiParam(name="menuAlias", value="菜单类型", required = true)@RequestParam String menuAlias) throws Exception {
		CommonResult<List<SysMenu>> commonResult = new CommonResult<List<SysMenu>>(true,"获取当前用户菜单信息");
		List<SysMenu> lists = sysMenuManager.getCurrentUserMenu();
		lists = i18nSysMenu(lists);
		List<SysMenu> result = sysMenuManager.filterByMenuAlias(menuAlias, lists);
		commonResult.setValue(result);
		return commonResult;
	}
	
	//  菜单资源国际化
	private List<SysMenu> i18nSysMenu(List<SysMenu> lists) {
		List<String> i18nKey = new ArrayList<String>();
		for (SysMenu sysMenu : lists) {
			i18nKey.add(sysMenu.getAlias());
		}
		Map<String, String> messages = I18nUtil.getMessages(i18nKey, LocaleContextHolder.getLocale());
		for (SysMenu sysMenu : lists) {
			String key = sysMenu.getAlias();
			if(messages.containsKey(key) && StringUtil.isNotEmpty(messages.get(key)) && !key.equals(messages.get(key)) ){
				sysMenu.setName(messages.get(key));
			}
		}
		return lists;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getCurrentUserMethodAuth", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取当前用户的请求权限", httpMethod = "GET", notes = "获取当前用户的请求权限")
	public @ResponseBody Map<String,List<String>> getCurrentUserMethodAuth() throws Exception {
		List<SysMethod> all = sysMethodManager.list();
		List<String> sysMethodList = new ArrayList<String>();
		for (SysMethod sysMethod : all) {
			sysMethodList.add(sysMethod.getAlias());
		}
		
		IUser currentUser = ContextUtil.getCurrentUser();
		List<String> curUserMethod = sysMethodList;
		if(!currentUser.isAdmin()){
			curUserMethod = sysMethodManager.getCurrentUserMethodAuth();
		}
		
		Map<String, List<String>> rtnMap = new HashMap<String, List<String>>();
		rtnMap.put("allMethod", sysMethodList);
		rtnMap.put("curUserMethod", curUserMethod);
		
		return rtnMap;
	}

	@RequestMapping(value="addDataTemplateToMenu", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "添加业务数据模板到菜单", httpMethod = "POST", notes = "添加业务数据模板到菜单")
	public CommonResult<String> addDataTemplateToMenu(@ApiParam(name="templateToMenuVo", value="业务数据模板添加到菜单参数", required = true)@RequestBody TemplateToMenuVo templateToMenuVo) throws Exception {
		if(StringUtil.isEmpty(templateToMenuVo.getParentAlias())){
			return new CommonResult<String>(false, "父菜单别名不能为空");
		}
		if(StringUtil.isEmpty(templateToMenuVo.getAlias())){
			return new CommonResult<String>(false, "别名不能为空");
		}
//		if(StringUtil.isEmpty(templateToMenuVo.getPath())){
//			return new CommonResult<String>(false, "路径不能为空");
//		}
		SysMenu pMenu = sysMenuManager.getByAlias(templateToMenuVo.getParentAlias());
		if(BeanUtils.isEmpty(pMenu)){
			return new CommonResult<String>(false, "根据父菜单别名"+templateToMenuVo.getParentAlias()+"未找到对应菜单！");
		}
		boolean isExist = sysMenuManager.isExistByAlias(templateToMenuVo.getAlias());
		if(isExist){
			return new CommonResult<String>(false, "别名【"+templateToMenuVo.getAlias()+"】已存在！");
		}
//		SysMenu templateListMenu = sysMenuManager.getByAlias("form.bpmDataTemplatePreview");
//		if(BeanUtils.isEmpty(templateListMenu)){
//			return new CommonResult<String>(false, "业务数据模板中预览列表路由【form.bpmDataTemplatePreview】不存在或已改别名，无法添加到菜单！");
//		}
		SysMenu templateListMenu = new SysMenu();
		//将业务数据模板预览的列表页面和添加页面菜单复制一份到添加的菜单
		try {
			String id = UniqueIdUtil.getSuid();
			templateListMenu.setPath(pMenu.getPath()+id+".");
			templateListMenu.setSn(99);
			templateListMenu.setOpen(false);
            templateListMenu.setHref(templateToMenuVo.getHref());
			templateListMenu.setAlias(templateToMenuVo.getAlias());
			templateListMenu.setName(templateToMenuVo.getName());
			templateListMenu.setActiveTab(templateToMenuVo.getPath());
			templateListMenu.setId(id);
			templateListMenu.setParentId(pMenu.getId());
			sysMenuManager.create(templateListMenu);
		} catch (Exception e) {
			return new CommonResult<String>(false, "添加失败："+e.getMessage());
		}
		return new CommonResult<String>(true, "添加成功，重新登录后生效！");
	}

}
