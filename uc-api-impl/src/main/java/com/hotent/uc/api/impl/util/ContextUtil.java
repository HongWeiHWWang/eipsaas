package com.hotent.uc.api.impl.util;

import java.util.Collection;
import java.util.Locale;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.IgnoreOnAssembly;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.context.ICurrentContext;
import com.hotent.uc.api.impl.model.Org;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 类 {@code ContextUtil} 获取上下文数据对象的工具类
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月9日
 */
@Component
@IgnoreOnAssembly
public class ContextUtil  {

	private static ContextUtil contextUtil;

	private ICurrentContext currentContext;

	public void setCurrentContext(ICurrentContext _currentContext){
		contextUtil=this;
		contextUtil.currentContext=_currentContext;
	}

	/**
	 * 获取当前执行人
	 * @return 用户详情
	 */
	public static IUser getCurrentUser(){
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Assert.notNull(authentication, "当前登录用户不能为空");
			Object principal = authentication.getPrincipal();
			if(principal instanceof UserFacade) {
				return (UserFacade)principal;
			}
			else if(principal instanceof UserDetails) {
				UserDetails ud = (UserDetails)principal;
				UserFacade user = JsonUtil.toBean(JsonUtil.toJson(ud), UserFacade.class);
				return user;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

    /**
     * 设置当前用户ID
     */
	public static String getCurrentUserId(){
		IUser user = getCurrentUser();
		return BeanUtils.isEmpty(user)?null:user.getUserId();
	}

	/**
     * 获取当前用户组
     * @return 用户组
     */
	public static IGroup getCurrentGroup(){
		try {
			UCFeignService service = AppUtil.getBean(UCFeignService.class);
			String userId = getCurrentUserId();
			if(StringUtil.isNotEmpty(userId)){
				ObjectNode orgObj = service.getMainGroup(userId);
				if(BeanUtils.isNotEmpty(orgObj)){
					boolean isParent = orgObj.get("isParent").asBoolean();
					orgObj.put("isIsParent", isParent?1:0);
					orgObj.remove("isParent");
					IGroup org = JsonUtil.toBean(orgObj, Org.class);
					return org;
				}

			}
		} catch (Exception e) {}
		return null;
	}

	/**
     * 获取当前用户组Id，用户组为空则返回空
     * @return 用户组Id
     */
	public static String getCurrentGroupId(){
		IGroup org =  getCurrentGroup();
		if(BeanUtils.isNotEmpty(org)){
			return org.getGroupId();
		}else{
			return "";
		}
	}

	/**
	 * 获取当前Locale
	 * @return 国际化信息
	 */
	public static Locale getLocale(){
		return contextUtil.currentContext.getLocale();
	}

	/**
	 * 清除当前执行人
	 */
	public static void clearCurrentUser(){
		if(contextUtil!=null){
			contextUtil.currentContext.clearCurrentUser();
		}
	}

	/**
	 * 设置当前执行人
	 * @param user 用户信息
	 */
	public static void setCurrentUser(IUser user){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (BeanUtils.isEmpty(authentication) || BeanUtils.isEmpty(authentication.getPrincipal())) {
			Assert.isTrue(BeanUtils.isNotEmpty(user), "传入的用户不能为空");
			UsernamePasswordAuthenticationToken usernamePwdAuth = new UsernamePasswordAuthenticationToken(user, null);
			SecurityContextHolder.getContext().setAuthentication(usernamePwdAuth);
		}
	}

	/**
	 * 根据用户账户获取用户信息
	 * @param account 用户账号
	 * @return 用户详情
	 */
	public static IUser getUserByAccount(String account)
	{
		Assert.isTrue(StringUtil.isNotEmpty(account), "必须传入用户账号");
		IUserService userServiceImpl=AppUtil.getBean(IUserService.class);
		IUser user = userServiceImpl.getUserByAccount(account);
		Assert.isTrue(BeanUtils.isNotEmpty(user), String.format("账号为：%s的用户不存在", account));
		return user;
	}

    /**
     * 设置当前用户账号
     * @param account 用户账号
     */
	public static void setCurrentUserByAccount(String account){
		setCurrentUser(getUserByAccount(account));
	}

	/**
	 * 设置当前组织(岗位)
	 * @param group 用户组
	 */
	public static void setCurrentOrg(IGroup group){
		contextUtil.currentContext.setCurrentGroup(group);
	}

	/**
	 * 设置Locale
	 * @param locale 国际化信息
	 */
	public static void setLocale(Locale locale){
		contextUtil.currentContext.setLocale(locale);
	}

	/**
	 * 清除Local
	 */
	public static void cleanLocale(){
		if(contextUtil!=null){
			contextUtil.currentContext.clearLocale();
		}
	}

    /**
     * 清除Local和当前用户信息
     */
	public static void clearAll() {
		cleanLocale();
		clearCurrentUser();  
	}
}
