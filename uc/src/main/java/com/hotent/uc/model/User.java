package com.hotent.uc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.constants.SystemConstants;
import com.hotent.base.util.BeanUtils;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

 /**
 * 
 * <pre> 
 * 描述：用户表 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:50
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("uc_user")
@ApiModel(description="用户表")
public class User extends UcBaseModel<User>  implements IUser {
	private static final long serialVersionUID = -4165513977160986324L;

	public final static String FROM_RESTFUL = "restful";
	public final static String FROM_AD = "AD";
	public final static String FROM_EXCEL = "EXCEL";
	public final static String FROM_WEBSERVICE = "webservice";
	
	public final static int STATUS_NORMAL = 1;
	public final static int STATUS_DISABLED = 0;
	public final static int STATUS_NOT_ACTIVE = -1;
	public final static int STATUS_LEAVE = -2;
	
	//是否删除
	public final static String DELETE_YES = "1";
	public final static String DELETE_NO = "0";
	
	//是否已经同步微信
	public final static int HASSYNCTOWX_YEX = 1;
	public final static int HASSYNCTOWX_NO = 0;
	
	/**
	* id_
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="用户id")
	protected String id; 
	
	/**
	* 姓名
	*/
	@TableField("FULLNAME_")
	@ApiModelProperty(name="fullname",notes="姓名")
	protected String fullname; 
	
	/**
	* 账号
	*/
	@TableField("ACCOUNT_")
	@ApiModelProperty(name="account",notes="账号")
	protected String account; 
	
	/**
	* 密码
	*/
	@TableField("PASSWORD_")
	@ApiModelProperty(name="password",notes="密码")
	protected String password; 
	
	/**
	* 邮箱
	*/
	@TableField("EMAIL_")
	@ApiModelProperty(name="email",notes="邮箱")
	protected String email; 
	
	/**
	* 手机号码
	*/
	@TableField("MOBILE_")
	@ApiModelProperty(name="mobile",notes="手机号码")
	protected String mobile; 
	
	
	
	/**
	* 地址
	*/
	@TableField("ADDRESS_")
	@ApiModelProperty(name="address",notes="地址")
	protected String address; 
	
	/**
	* 头像
	*/
	@TableField("PHOTO_")
	@ApiModelProperty(name="photo",notes="头像")
	protected String photo; 
	
	/**
	* 性别：男，女，未知
	*/
	@TableField("SEX_")
	@ApiModelProperty(name="sex",notes="性别")
	protected String sex; 
	
	/**
	* 来源
	*/
	@TableField("FROM_")
	@ApiModelProperty(name="from",notes="来源")
	protected String from="system"; 
	
	/**
	* 0:禁用，1正常，-1未激活，-2离职
	*/
	@TableField("STATUS_")
	@ApiModelProperty(name="status",notes="0:禁用，1正常，-1未激活，-2离职")
	protected Integer status; 
	
	
	/**
	 * 组织ID，用于在组织下添加用户。
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="groupId",notes="组织ID，用于在组织下添加用户")
	protected String groupId="";
	
	/**
	 * 微信同步关注状态  0：未同步  1：已同步，尚未关注  2：已同步且已关注
	 */
	@TableField("HAS_SYNC_TO_WX_")
	@ApiModelProperty(name="hasSyncToWx",notes="微信同步关注状态")
	protected Integer hasSyncToWx=0;

	/**
     * 微信号
     */
	@TableField("WEIXIN_")
	@ApiModelProperty(name="weixin",notes="微信号")
    protected String weixin;

     /**
	* 消息通知类型
	*/
	@TableField("NOTIFY_TYPE_")
	@ApiModelProperty(name="notifyType",notes="消息通知类型")
	protected String notifyType; 
	
	/**
	* 工号
	*/
	@TableField("USER_NUMBER_")
	@ApiModelProperty(name="userNumber",notes="工号")
	protected String userNumber; 
	/**
	* 身份证号
	*/
	@TableField("ID_CARD_")
	@ApiModelProperty(name="idCard",notes="身份证号")
	protected String idCard; 
	/**
	* 办公电话
	*/
	@TableField("PHONE_")
	@ApiModelProperty(name="phone",notes="办公电话")
	protected String phone; 
	/**
	* 生日
	*/
	@TableField("BIRTHDAY_")
	@ApiModelProperty(name="birthday",notes="生日")
	protected LocalDate birthday;
	/**
	* 入职日期
	*/
	@TableField("ENTRY_DATE_")
	@ApiModelProperty(name="entryDate",notes="入职日期")
	protected LocalDate entryDate;
	/**
	* 离职日期
	*/
	@TableField("LEAVE_DATE_")
	@ApiModelProperty(name="leaveDate",notes="离职日期")
	protected LocalDate leaveDate;
	/**
	* 学历
	*/
	@TableField("EDUCATION_")
	@ApiModelProperty(name="education",notes="学历")
	protected String education; 
	
	@TableField("tenant_id_")
	@ApiModelProperty(name="tenantId",notes="租户id")
	protected String tenantId;
	
	@TableField("PWD_CREATE_TIME_")
	@ApiModelProperty(name="pwdCreateTime",notes="密码策略时间")
	protected LocalDateTime pwdCreateTime;
	
	/**
	 * 其他属性
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="attributes",notes="其他属性")
	protected Map<String,String> attributes = new HashMap<String, String>() ;  
	
	/**
	 *	用户授权信息
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="authorities",notes="用户授权信息")
	protected  Collection<GrantedAuthority> authorities;
	
	public User(){}
	
	public User(String account, String fullname, String password, Collection<GrantedAuthority> authorities){
		this.account = account;
		this.fullname = fullname;
		this.password = password;
		this.authorities = authorities;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 id_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	/**
	 * 返回 姓名
	 * @return
	 */
	public String getFullname() {
		return this.fullname;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	/**
	 * 返回 账号
	 * @return
	 */
	public String getAccount() {
		return this.account;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 返回 密码
	 * @return
	 */
	public String getPassword() {
		return this.password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 返回 邮箱
	 * @return
	 */
	public String getEmail() {
		return this.email;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * 返回 手机号码
	 * @return
	 */
	public String getMobile() {
		return this.mobile;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * 返回 地址
	 * @return
	 */
	public String getAddress() {
		return this.address;
	}
	
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	/**
	 * 返回 头像
	 * @return
	 */
	public String getPhoto() {
		return this.photo;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	/**
	 * 返回 性别：男，女，未知
	 * @return
	 */
	public String getSex() {
		return this.sex;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * 返回 来源
	 * @return
	 */
	public String getFrom() {
		return this.from;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 返回 0:禁用，1正常
	 * @return
	 */
	public Integer getStatus() {
		return this.status;
	}
	
	/**
	 * 返回 消息通知类型
	 * @return
	 */
	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	/**
	 * 返回 工号
	 * @return
	 */
	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	/**
	 * 返回 身份证号
	 * @return
	 */
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	/**
	 * 返回 办公电话
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 返回 生日
	 * @return
	 */
	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	/**
	 * 返回 入职日期
	 * @return
	 */
	public LocalDate getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDate entryDate2) {
		this.entryDate = entryDate2;
	}

	/**
	 * 返回 学历
	 * @return
	 */
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public LocalDate getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(LocalDate leaveDate) {
		this.leaveDate = leaveDate;
	}

     public String getWeixin() {
         return weixin;
     }

     public void setWeixin(String weixin) {
         this.weixin = weixin;
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
		.append("address", this.address) 
		.append("photo", this.photo)
        .append("hasSyncToWx", this.hasSyncToWx)
        .append("weixin", this.weixin)
		.append("sex", this.sex)
		.append("from", this.from) 
		.append("status", this.status) 
		.append("notifyType", this.notifyType) 
		.append("userNumber", this.userNumber) 
		.append("idCard", this.idCard) 
		.append("phone", this.phone) 
		.append("birthday", this.birthday) 
		.append("entryDate", this.entryDate) 
		.append("leaveDate", this.leaveDate) 
		.append("education", this.education) 
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
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
		String[] split = tmp.split(",");
		for (String _account : split) {
			if(_account.equals(this.account)){
				return true;
			}
		}
		
		return false;
	}

	public Map<String, String> getAttributes() {
		return this.attributes;
	}
	public String getAttrbuite(String key) {
		if(this.attributes.containsKey(key)){
			return this.attributes.get(key);
		}
		return "";
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

	public boolean isEnable() {
		return BeanUtils.isNotEmpty(this.status)?this.status==1:false;
	}

	@Override
	public String getIdentityType() {
		return IdentityType.USER;
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
	
	public String getTenantId() {
		return tenantId;
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

	/**
	 * 反序列化认证信息时需要使用
	 * @param arrayNode
	 */
	public void setAuthorities(ArrayNode arrayNode) {
		this.authorities = new ArrayList<>();
		if (BeanUtils.isNotEmpty(arrayNode)) {
			for (JsonNode jsonNode: arrayNode) {
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(jsonNode.get("authority").asText());
				this.authorities.add(grantedAuthority);
			}
		}
	}
}