package com.hotent.uc.api.impl.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.constants.SystemConstants;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;

 /**
  * 类 {@code UserFacade} 用户的默认实现
  * @company 广州宏天软件股份有限公司
  * @author heyifan
  * @email heyf@jee-soft.cn
  * @date 2018年7月5日	
  */
public class UserFacade implements IUser {
	private static final long serialVersionUID = -47458854186013987L;

     /**
      * 用户来源(系统) {@value}
      */
	public final static String FROM_SYSTEM = "system";

     /**
      * 用户来源(接口) {@value}
      */
	public final static String FROM_RESTFUL = "restful";
	
	/**
	* 主键ID
	*/
	protected String id; 
	
	/**
	* 姓名
	*/
	protected String fullname; 
	
	/**
	* 账号
	*/
	protected String account; 
	
	/**
	* 密码
	*/
	protected String password; 
	
	/**
	* 邮箱
	*/
	protected String email; 
	
	/**
	* 手机号码
	*/
	protected String mobile; 
	
	/**
	* 微信号
	*/
	protected String weixin; 
	
	/**
	* 创建时间
	*/
	protected LocalDateTime createTime; 
	
	/**
	* 地址
	*/
	protected String address; 
	
	/**
	* 头像
	*/
	protected String photo; 
	
	/**
	* 性别：男，女，未知
	*/
	protected String sex; 
	
	/**
	* 来源
	*/
	protected String from="system"; 
	
	/**
	* 0:禁用，1正常
	*/
	protected Integer status; 
	
	
	/**
	 * 组织ID，用于在组织下添加用户。
	 */
	protected String groupId="";
	
	/**
	 * 微信同步关注状态  0：未同步  1：已同步，尚未关注  2：已同步且已关注
	 */
	protected Integer hasSyncToWx;
	
	/**
	* 微信用户唯一识别号
	*/
	protected String openId;
	
	/**
	 * 密码策略时间
	 */
	protected LocalDateTime pwdCreateTime;
	
	/**
	* 租户id
	*/
	protected String tenantId; 
	
	/**
	 *	用户授权信息
	 */
	protected  Collection<SimpleGrantedAuthority> authorities;
	
	/**
	 * 其他属性
	 */
	protected Map<String,String> attributes = new HashMap<String, String>() ;  

	/**
	 * 反序列化认证信息时需要使用
	 * @param arrayNode
	 */
	public void setAuthorities(ArrayNode arrayNode) {
		this.authorities = new ArrayList<>();
		for (JsonNode jsonNode: arrayNode) {
			SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(jsonNode.get("authority").asText());
			this.authorities.add(grantedAuthority);
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getFullname() {
		return this.fullname;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return this.account;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}
	
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getWeixin() {
		return this.weixin;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}
	
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhoto() {
		return this.photo;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom() {
		return this.from;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("fullname", this.fullname) 
		.append("account", this.account) 
		.append("password", this.password) 
		.append("email", this.email) 
		.append("mobile", this.mobile) 
		.append("weixin", this.weixin) 
		.append("createTime", this.createTime) 
		.append("address", this.address) 
		.append("photo", this.photo) 
		.append("sex", this.sex) 
		.append("from", this.from) 
		.append("status", this.status)
		.append("openId", this.openId)
		.toString();
	}

	public String getIdentityType() {
		return IdentityType.USER;
	}

	public String getUserId() {
		return this.id;
	}

	public void setUserId(String userId) {
		this.id=userId;
		
	}

	public void setAttributes(Map<String, String> map) {
		this.attributes = map;
	}

	public boolean isAdmin() {
		String tmp = SystemConstants.SYSTEM_ACCOUNT;
		return tmp.equals(this.account);
	}

	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	public String getAttrbuite(String key) {
		if(this.attributes.containsKey(key)){
			return this.attributes.get(key);
		}
		return null;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getHasSyncToWx() {
		return hasSyncToWx;
	}

	public void setHasSyncToWx(Integer hasSyncToWx) {
		this.hasSyncToWx = hasSyncToWx;
	}

	@Override
	public boolean isEnable() {
		int status=this.getStatus();
		if(status==1){
			return true;
		}
		return false;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getUsername() {
		return this.account;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isEnable();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isEnable();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isEnable();
	}

	@Override
	public boolean isEnabled() {
		return isEnable();
	}

	@Override
	public String getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public LocalDateTime getPwdCreateTime() {
		return pwdCreateTime;
	}

	public void setPwdCreateTime(LocalDateTime pwdCreateTime) {
		this.pwdCreateTime = pwdCreateTime;
	}
	
}