package com.hotent.uc.util;

import java.util.Locale;

import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.context.ICurrentContext;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;
import com.hotent.uc.model.User;



/**
 * 获取上下文数据对象的工具类。
 * <pre> 
 * 
 * 构建组：x5-org-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2013-12-20-上午9:38:46
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
@Primary
public class ContextUtil  {

	private static ContextUtil contextUtil;

	private ICurrentContext currentContext;

	public void setCurrentContext(ICurrentContext _currentContext){
		contextUtil=this;
		contextUtil.currentContext=_currentContext;
	}

	/**
	 * 获取当前执行人
	 * @return 
	 * User
	 * @exception 
	 * @since  1.0.0
	 */
	public static User getCurrentUser(){
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Assert.notNull(authentication, "当前登录用户不能为空");
			Object principal = authentication.getPrincipal();
			if(principal instanceof User) {
				return (User)principal;
			}
			else if(principal instanceof UserDetails) {
				UserDetails ud = (UserDetails)principal;
				User user = JsonUtil.toBean(JsonUtil.toJson(ud), User.class);
				return user;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public static String getCurrentUserId(){
		User user = getCurrentUser();
		return BeanUtils.isEmpty(user)?null:user.getUserId();
	}
	/**获取当前组织*/
	public static IGroup getCurrentGroup(){
		return null;
		//return contextUtil.currentContext.getCurrentGroup();
	}

	/**获取当前组织Id。组织为空则返回空*/
	public static String getCurrentGroupId(){
		IGroup iGroup =  getCurrentGroup();
		if(BeanUtils.isNotEmpty(iGroup)){
			return iGroup.getGroupId();
		}else{
			return "";
		}
	}
	/**
	 * 获取当前Locale。
	 * @return 
	 * Locale
	 * @exception 
	 * @since  1.0.0
	 */
	public static Locale getLocale(){
		return contextUtil.currentContext.getLocale();
	}

	/**
	 * 清除当前执行人。
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void clearCurrentUser(){
		if(contextUtil!=null){
			contextUtil.currentContext.clearCurrentUser();
		}
	}

	/**
	 * 设置当前执行人。
	 * @param user 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void setCurrentUser(IUser user){
		Assert.isTrue(BeanUtils.isNotEmpty(user), "传入的用户不能为空");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Assert.isNull(authentication, "当前登录上下文中有登录用户时不能设置用户");
		UsernamePasswordAuthenticationToken usernamePwdAuth = new UsernamePasswordAuthenticationToken(user, null);
		SecurityContextHolder.getContext().setAuthentication(usernamePwdAuth);
	}

	/**
	 * 根据用户账户获取用户
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public static IUser getUserByAccount(String account)
	{
		Assert.isTrue(StringUtil.isNotEmpty(account), "必须传入用户账号");
		IUserService userServiceImpl=AppUtil.getBean(IUserService.class);
		IUser user = userServiceImpl.getUserByAccount(account);
		Assert.isTrue(BeanUtils.isNotEmpty(user), String.format("账号为：%s的用户不存在", account));
		return user;
	}

	public static void setCurrentUserByAccount(String account){
		setCurrentUser(getUserByAccount(account));
	}

	/**
	 * 设置当前组织(岗位)。
	 * @param user 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void setCurrentOrg(IGroup group){
		contextUtil.currentContext.setCurrentGroup(group);
	}

	/**
	 * 设置Locale。
	 * @param locale 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void setLocale(Locale locale){
		contextUtil.currentContext.setLocale(locale);
	}

	/**
	 * 清除Local。
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void cleanLocale(){
		if(contextUtil!=null){
			contextUtil.currentContext.clearLocale();
		}
	}

	public static void clearAll() {
		cleanLocale();
		clearCurrentUser();  
	}
}
