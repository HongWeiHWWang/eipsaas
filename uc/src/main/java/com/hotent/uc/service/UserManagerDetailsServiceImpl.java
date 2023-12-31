package com.hotent.uc.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hotent.base.exception.CertificateException;
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.PlatformConsts;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserRoleManager;
import com.hotent.uc.model.TenantManage;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRole;

/**
 * 查询用户表获取用户详情的实现
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月6日
 */
@Primary
@Service
public class UserManagerDetailsServiceImpl implements UserDetailsService{
	@Resource
	UserManager userManager;
	@Resource
	UserRoleManager userRoleManager;
	@Resource
	UserDetailsFacade userDetailsFacade;
	@Resource
	OrgManager orgManager;
	@Resource
	TenantManageManager tenantManageManager;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, CertificateException {
		UserDetails	user =  convertUserDetails2User(loadUserByUsernameFromDB(username));
		Assert.notNull(user, "UserManagerDetailsServiceImpl.loadUserByUsernameFromDB "
				+ " returned null for username " + username + ". "
				+ "This is an interface contract violation");
		return user;
	}
	
	/**
	 * 从数据库中获取用户的认证信息
	 * @param username
	 * @return
	 */
	private UserDetails loadUserByUsernameFromDB(String username){
		try {
			User user = userManager.getByAccount(username);
			if(BeanUtils.isEmpty(user)) {
				throw new UsernameNotFoundException("");
			}
			//判断租户状态，非正常状态租户不允许其下用户
			if(StringUtil.isNotEmpty(user.getTenantId()) && !"-1".equals(user.getTenantId())){
				TenantManage tenant = tenantManageManager.get(user.getTenantId());
				if(BeanUtils.isEmpty(tenant)){
					throw new CertificateException("未获取到用户租户信息。");
				}else if(!TenantManage.STATUS_ENABLE.equals(tenant.getStatus())){
					throw new CertificateException("用户所属租户未启用。");
				}
			}
			Collection<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
			// 获取用户的权限
			if(user.isAdmin()){
				authorities.add(PlatformConsts.ROLE_GRANT_SUPER);
			}
			List<UserRole> userRoles = userRoleManager.getListByUserId(user.getId());
			for (UserRole userRole : userRoles) {
				SimpleGrantedAuthority role=new SimpleGrantedAuthority(userRole.getAlias());
				authorities.add(role);
			}
			user.setAttributes(getOrgIdByUserId(user.getUserId()));
			return userDetailsFacade.loadUserDetails(authorities, user);
			
		} 
		catch (CertificateException e) {
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("", e);
		}
	}
	
	private Map<String,String> getOrgIdByUserId(String userId){
		Map<String, String> map = new HashMap<String, String>();
		// 查询用户所在组织及该组织是否为主组织
		List<Map<String, Object>> orgIdMapList = orgManager.getOrgIdMapByUserId(userId);
		
		Set<String> orgIds = new HashSet<String>();
		orgIdMapList.forEach(orgIdMap -> {
			String orgId = MapUtil.getString(orgIdMap, "orgId");
			String isMaster = MapUtil.getString(orgIdMap, "isMaster");
			if(StringUtil.isNotEmpty(orgId)){
				orgIds.add(orgId);
			}
			if("1".equals(isMaster)) {
				map.put(AuthenticationUtil.CURRENT_USER_MAIN_ORGID, orgId);
			}
		});
		
		if(!orgIds.isEmpty()){
			map.put(AuthenticationUtil.CURRENT_USER_ORGIDS,String.join(",", orgIds));
		}
		// 查询所在组织的下级组织
		List<String> subOrgByIds = orgManager.getSubOrgByIds(orgIds);
		if(BeanUtils.isNotEmpty(subOrgByIds)) {
			Set<String> orgIdsAndSub = new HashSet<>(subOrgByIds);
			orgIdsAndSub.addAll(orgIds);
			map.put(AuthenticationUtil.CURRENT_USER_SUB_ORGIDS, String.join(",", orgIdsAndSub));
		}
		return map;
	}
	
	private User convertUserDetails2User(UserDetails userDetails){
		if(BeanUtils.isEmpty(userDetails)) return null;
		if(userDetails instanceof User) {
			return (User)userDetails;
		}
		else {
			IUser iuser = (IUser)userDetails;
			String userId = iuser.getUserId();
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			Collection<? extends GrantedAuthority> extendsAuthorities = iuser.getAuthorities();
			if(BeanUtils.isNotEmpty(extendsAuthorities)) {
				authorities.addAll(extendsAuthorities);
			}
			String account = iuser.getAccount();
			String fullname = iuser.getFullname();
			String password = iuser.getPassword();
			Integer status = iuser.getStatus();
			String email = iuser.getEmail();
			String mobile = iuser.getMobile();
			LocalDateTime pwdCreateTime =iuser.getPwdCreateTime();
			User user = new User(account, fullname, password, authorities);
			user.setUserId(userId);
			user.setEmail(email);
			user.setMobile(mobile);
			user.setStatus(status);
			user.setPwdCreateTime(pwdCreateTime);
			//user.setWeixin(iuser.getWeixin());
			//user.setHasSyncToWx(iuser.getHasSyncToWx());
			return user;
		}
	}
}
