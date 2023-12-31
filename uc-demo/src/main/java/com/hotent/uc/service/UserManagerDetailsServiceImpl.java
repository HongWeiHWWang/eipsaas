package com.hotent.uc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.PlatformConsts;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.mock.MockUCDataUtil;
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
	UserDetailsFacade userDetailsFacade;
	
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
//			User user = userManager.getByAccount(username);
			User user = MockUCDataUtil.getUserByAccount(username);
			if(BeanUtils.isEmpty(user)) {
				throw new UsernameNotFoundException("");
			}
			Collection<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
			// 获取用户的权限
			if(user.isAdmin()){
				authorities.add(PlatformConsts.ROLE_GRANT_SUPER);
			}
			//List<UserRole> userRoles = userRoleManager.getListByUserId(user.getId());
			List<UserRole> userRoles = MockUCDataUtil.getUserRoleListByUserId(user.getId());
			for (UserRole userRole : userRoles) {
				SimpleGrantedAuthority role=new SimpleGrantedAuthority(userRole.getAlias());
				authorities.add(role);
			}
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
			User user = new User(account, fullname, password, authorities);
			user.setUserId(userId);
			user.setEmail(email);
			user.setMobile(mobile);
			user.setStatus(status);
			//user.setWeixin(iuser.getWeixin());
			//user.setHasSyncToWx(iuser.getHasSyncToWx());
			return user;
		}
	}
}
