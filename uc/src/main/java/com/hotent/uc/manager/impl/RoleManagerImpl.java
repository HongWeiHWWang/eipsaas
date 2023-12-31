package com.hotent.uc.manager.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.RoleDao;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.RoleManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserRoleManager;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRole;
import com.hotent.uc.params.role.RoleVo;
import com.hotent.uc.params.user.UserVo;
import com.hotent.uc.util.OrgUtil;

/**
 * 
 * <pre>
 * 描述：角色管理 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:28:04
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class RoleManagerImpl extends BaseManagerImpl <RoleDao, Role> implements RoleManager {
	@Autowired
	UserRoleManager userRoleManager;
	@Autowired
	UserManager userService;
	@Autowired
	OrgManager orgService;
	@Autowired
	UserRoleManager userRoleService;

	@Override
	public Role getByAlias(String code) {
		return baseMapper.getByCode(code);
	}

	@Override
	public List<Role> getListByUserId(String userId) {
		return baseMapper.getListByUserId(userId);
	}

	@Override
	public List<Role> getListByAccount(String account) {
		return baseMapper.getListByAccount(account);
	}

    @Transactional
	public void remove(String roleId) {
		super.remove(roleId);
		// 删除角色跟资源的关系
		//resRoleService.removeByRoleAndSystem(roleId, null);
		// 删除角色跟用户的关系
		userRoleManager.removeByRoleId(roleId,LocalDateTime.now());
		delUserMenuCache();
	}
    
    private void delUserMenuCache() {
    	RoleManagerImpl bean = AppUtil.getBean(getClass());
    	bean.removeUserMenuCache();
    }
    
    @CacheEvict(value = CacheKeyConst.USER_MENU_CACHENAME, allEntries = true)
    protected void removeUserMenuCache() {}
    
    private void delUserMenuCacheByUserId(String userId) {
    	RoleManagerImpl bean = AppUtil.getBean(getClass());
    	bean.removeUserMenuCacheByUserIde(userId);
    }
	
    @CacheEvict(value = CacheKeyConst.USER_MENU_CACHENAME, key = "#userId")
    protected void removeUserMenuCacheByUserIde(String userId) {}
    
	@Override
    @Transactional
	public CommonResult<String> addRole(RoleVo roleVo)
			throws Exception {
		if(StringUtil.isEmpty(roleVo.getName())){
			throw new RequiredException("添加角色失败，角色名称【name】必填！");
		}
		if(StringUtil.isEmpty(roleVo.getCode())){
			throw new RequiredException("添加角色失败，角色编码【code】必填！");
		}
		if(baseMapper.getCountByCode(roleVo.getCode())>0){
			return new CommonResult<String>(false, "添加角色失败，角色编码【"+roleVo.getCode()+"】在系统中已存在！", "");
		}
		Role role = RoleVo.parse(roleVo);
		role.setUpdateTime(LocalDateTime.now());
		role.setId(UniqueIdUtil.getSuid());
		create(role);
		return new CommonResult<String>(true, "添加角色成功！", "");
	}

	@Override
    @Transactional
	public CommonResult<String> deleteRole(String codes) throws Exception {
		String[] codeArray = codes.split(",");
		StringBuilder str = new StringBuilder();
		boolean isTrue = false;
		for (String code : codeArray) {
			Role role = getByAlias(code);
			if(BeanUtils.isNotEmpty(role)){
				if (OrgUtil.checkUserGruopIsUserRel("role", role.getId())) {
					str.append("编码为【"+code+"】的角色为汇报节点不能删除，");
					continue;
				}else{
					remove(role.getId());
					isTrue = true;
				}	
			}else{
				str.append(code);
				str.append("，");
			}
		}
		String msg = StringUtil.isEmpty(str.toString())?"删除角色成功！":"部分删除失败，角色编码："+str.toString()+"不存在！";
		delUserMenuCache();
		return new CommonResult<String>(isTrue, msg, str.toString());
	}
	
	@Override
    @Transactional
	public CommonResult<String> deleteRoleByIds(String ids) throws Exception {
		String[] idArray = ids.split(",");
		StringBuilder str = new StringBuilder();
		boolean isTrue = false;
		for (String id : idArray) {
			Role role = get(id);
			if(BeanUtils.isNotEmpty(role)){
				remove(role.getId());
				clearRoleCahceMenu(role.getCode());
				isTrue = true;	
			}else{
				str.append(id);
				str.append("，");
			}
		}
		String msg = StringUtil.isEmpty(str.toString())?"删除角色成功！":"部分删除失败，角色编码："+str.toString()+"不存在！";
		return new CommonResult<String>(isTrue, msg, str.toString());
	}


	@Override
    @Transactional
	public CommonResult<String> updateRole(RoleVo roleVo)
			throws Exception {
		if(StringUtil.isEmpty(roleVo.getCode())){
			throw new RequiredException("更新角色失败，角色编码【code】必填！");
		}
		Role role = baseMapper.getByCode(roleVo.getCode());
		if(BeanUtils.isEmpty(role)){
			return new CommonResult<String>(false, "更新角色失败，角色编码【"+roleVo.getCode()+"】不存在！", "");
		}
		if(StringUtil.isNotEmpty(roleVo.getName())){
			role.setName(roleVo.getName());
		}
		if(roleVo.getDescription()!=null){
			role.setDescription(roleVo.getDescription());
		}
		if(roleVo.getEnabled()!=null){
			//如果角色的禁用状态发生了变化，则删除该角色下所有用户的菜单缓存
			if (roleVo.getEnabled()!=role.getEnabled()) {
				clearRoleCahceMenu(role.getCode());
			}
			role.setEnabled(roleVo.getEnabled());
		}
		this.update(role);
		return new CommonResult<String>(true, "更新角色成功！", "");
	}
    
	/**
	 * 根据角色编码查找角色里面的用户，清除其缓存的菜单资源
	 * @param roleCode
	 * @throws Exception
	 */
    @Transactional
	private void clearRoleCahceMenu(String roleCode) throws Exception{
		QueryFilter filter = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
		filter.addFilter("r.CODE_",roleCode, QueryOP.EQUAL, FieldRelation.AND, "group_code");
		Page<User> list = (Page<User>)userService.getRoleUserQuery(filter);
		for (User userRole : list.getRecords()) {
			delUserMenuCacheByUserId(userRole.getUserId());
		}
	}
    
	@Override
	public Role getRole(String code) throws Exception {
		Role role = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(role)){
			throw new RequiredException("角色编码【"+code+"】不存在！");
		}
		return role;
	}

	@Override
    @Transactional
	public CommonResult<String> saveUserRole(String code, String accounts)
			throws Exception {
		Role role = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(role)){
			throw new RequiredException("角色编码【"+code+"】不存在！");
		}
		String[] accountArray = accounts.split(",");
		StringBuilder values = new StringBuilder();
		boolean isFirst = true;
		int size = 0;
		String msg = "分配用户成功！";
		for (String account : accountArray) {
			User user = userService.getByAccount(account);
			if(BeanUtils.isNotEmpty(user)){
				addUserRole(user.getUserId(), role.getId());
				size++;
			}else{
				if(isFirst){
					isFirst = false;
				}else{
					values.append("，");
				}
				values.append(account);
			}
		}
		if(size==0){
			msg = "未分配任何用户！";
		}else if(size>0&&size<accountArray.length){
			msg = "部分分配成功！";
		}
		return new CommonResult<String>(true, msg, StringUtil.isNotEmpty(values.toString())?"账号：【"+values.toString()+"】不存在！":"");
	}

	@Override
    @Transactional
	public CommonResult<String> addUserRoleByOrg(String code,
			String orgCodes) throws Exception {
		Role role = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(role)){
			throw new RequiredException("角色编码【"+code+"】不存在！");
		}
		String[] orgCodesArray = orgCodes.split(",");
		StringBuilder values = new StringBuilder();
		boolean isFirst = true;
		int size = 0;
		String msg = "分配用户成功！";
		for (String orgCode : orgCodesArray) {
			Org org = orgService.getByCode(orgCode);
			if(BeanUtils.isNotEmpty(org)){
				List<User> users = userService.getUserListByOrgId(org.getId());
				for (User user : users) {
					addUserRole(user.getUserId(), role.getId());
				}
				size++;
			}else{
				if(isFirst){
					isFirst = false;
				}else{
					values.append("，");
				}
				values.append(orgCode);
			}
			
		}
		if(size==0){
			msg = "未分配任何用户！";
		}else if(size>0&&size<orgCodesArray.length){
			msg = "部分分配成功！";
		}
		return new CommonResult<String>(true, msg, StringUtil.isNotEmpty(values.toString())?"组织编码：【"+values.toString()+"】不存在！":"");
	}

	@Override
    @Transactional
	public CommonResult<String> removeUserRole(String code,
			String accounts) throws Exception {
		
		Role role = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(role)){
			throw new RequiredException("角色编码【"+code+"】不存在！");
		}
		String[] accountArray = accounts.split(",");
		int size = 0;
		String msg = "移除用户成功！";
		for (String account : accountArray) {
			User user = userService.getByAccount(account);
			if(BeanUtils.isNotEmpty(user)){
				UserRole userRole = userRoleManager.getByRoleIdUserId(role.getId(), user.getId());
				if(BeanUtils.isNotEmpty(userRole)){
					userRoleManager.remove(userRole.getId());
					size++;
					delUserMenuCacheByUserId(userRole.getUserId());
				}
			}
		}
		if(size==0){
			msg = "未移除任何用户！";
		}else if(size>0&&size<accountArray.length){
			msg = "部分移除成功！";
		}
		return new CommonResult<String>(true, msg, "");
	}

	@Override
	public List<Role> getRolesByUser(String account) throws Exception {
		return baseMapper.getListByAccount(account);
	}

	@Override
	public List<UserVo> getUsersByRoleCode(String codes) throws Exception {
		if(StringUtil.isEmpty(codes)){
			throw new RequiredException("角色编码不能为空！");
		}
		List<User> list = new ArrayList<User>();
		String[] codeArray = codes.split(",");
		StringBuilder msg = new StringBuilder();
		boolean isTrue = false;
		boolean isFirst = true;
		for (String code : codeArray) {
			List<User> users =userService.getUserListByRoleCode(code);
			if(BeanUtils.isNotEmpty(users)){
				list.addAll(users);
				isTrue = true;
			}else{
				if(!isFirst){
					msg.append(",");
				}else{
					isFirst = false;
				}
				msg.append(code);
			}
		}
		if(!isTrue){
			throw new RequiredException("根据角色编码【"+msg+"】找不到对应的角色信息！");
		}
		OrgUtil.removeDuplicate(list);
		return OrgUtil.convertToUserVoList(list);
	}

    @Transactional
	private void addUserRole(String userId,String roleId){
		if (userRoleService.getByRoleIdUserId(roleId, userId) != null) return;
		
		UserRole userRole = new UserRole();
		userRole.setId(UniqueIdUtil.getSuid());
		userRole.setUserId(userId);
		userRole.setRoleId(roleId);
		userRoleService.create(userRole);
		delUserMenuCacheByUserId(userRole.getUserId());
	}

	@Override
	public List<Role> getOrgRoleList(Map<String, Object> params) {
		return baseMapper.getOrgRoleList(params);
	}

	@Override
    @Transactional
	public CommonResult<String> forbiddenRoles(String codes) throws Exception {
		String str = forbOrActiveRole(codes, 0);
		String msg = StringUtil.isEmpty(str)?"禁用角色成功！":"部分禁用失败，角色编码："+str+"不存在！";
		return new CommonResult<String>(true, msg, str);
	}

	@Override
    @Transactional
	public CommonResult<String> activateRoles(String codes) throws Exception {
		String str = forbOrActiveRole(codes, 1);
		String msg = StringUtil.isEmpty(str)?"激活角色成功！":"部分激活失败，角色编码："+str+"不存在！";
		return new CommonResult<String>(true, msg, str);
	}

    @Transactional
	private String forbOrActiveRole(String codes,Integer enabled) throws SQLException{
		String[] codeArray = codes.split(",");
		StringBuilder str = new StringBuilder();
		for (String code : codeArray) {
			Role role = getByAlias(code);
			if(BeanUtils.isNotEmpty(role)){
				role.setEnabled(enabled);
				this.update(role);
			}else{
				str.append(code);
				str.append("，");
			}
		}
		return str.toString();
	}

	@Override
	public List<Role> getRoleByTime(String btime, String etime)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(btime, etime);
		return this.queryNoPage(queryFilter);
	}

	@Override
    @Transactional
	public Integer removeUserRolePhysical() throws Exception {
		return userRoleManager.removePhysical();
	}
	
	@Override
	public CommonResult<Boolean> isCodeExist(String code) throws Exception {
		Role role = baseMapper.getByCode(code);
		boolean isExist = BeanUtils.isNotEmpty(role);
		return new CommonResult<Boolean>(isExist, isExist?"该角色编码已存在！":"", isExist);
	}

	/**
	 * 根据角色别名获取除这个角色之外的所有角色
	 * @param code
	 * @return
	 */
	@Override
	public List<Role> getOrgRoleListNotCode(String code){
		return baseMapper.getOrgRoleListNotCode(code);
	}

	@Override
    @Transactional
	public CommonResult<String> saveUserRoles(String codes, String account) {
		if(StringUtil.isEmpty(codes)){
			throw new RequiredException("角色编码【"+codes+"】必填！");
		}
		String[] codeArr = codes.split(",");
		for (String code : codeArr) {
			Role role = baseMapper.getByCode(code);
			if(BeanUtils.isEmpty(role)){
				throw new RequiredException("角色编码【"+code+"】不存在！");
			}
			StringBuilder values = new StringBuilder();
			boolean isFirst = true;
			User user = null;
			try {
				user = userService.getByAccount(account);
			} catch (Exception e) {
				throw new RequiredException("账号【"+account+"】无效！");
			}
			if(BeanUtils.isNotEmpty(user)){
				addUserRole(user.getUserId(), role.getId());
			}else{
				if(isFirst){
					isFirst = false;
				}else{
					values.append("，");
				}
				values.append(account);
			}
		}
		
		return new CommonResult<String>(true, "分配角色成功");
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
}
