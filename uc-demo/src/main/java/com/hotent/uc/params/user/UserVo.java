package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.uc.model.User;
import com.hotent.uc.util.UpdateCompare;

/**
 * 用户参数对象
 * 
 * @author liangqf
 *
 */
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVo implements UpdateCompare {

	@ApiModelProperty(name = "id", notes = "用户id")
	private String id;

	@ApiModelProperty(name = "account", notes = "登录帐号（更新时不会更新）", required = true)
	private String account;

	@ApiModelProperty(name = "fullname", notes = "用户名", required = true)
	private String fullname;

	@ApiModelProperty(name = "userNumber", notes = "工号", required = true)
	private String userNumber;

	@ApiModelProperty(name = "password", notes = "登录密码（更新时不会更新）", required = true)
	private String password;

	@ApiModelProperty(name = "email", notes = "邮箱地址")
	private String email;

	@ApiModelProperty(name = "mobile", notes = "手机号码")
	private String mobile;

	@ApiModelProperty(name = "address", notes = "地址")
	private String address;

	@ApiModelProperty(name = "sex", notes = "性别", allowableValues = "男,女,未知")
	private String sex;

	@ApiModelProperty(name = "photo", notes = "头像（更新时不会更新）", allowableValues = "用户的头像")
	protected String photo;

	@ApiModelProperty(name = "status", notes = "状态 1：正常；0：禁用；-1：待激活；-2：离职（默认为正常）")
	private Integer status;

	@ApiModelProperty(name = "idCard", notes = "身份证号")
	private String idCard;

	@ApiModelProperty(name = "phone", notes = "办公电话")
	private String phone;

	@ApiModelProperty(name = "birthday", notes = "生日")
	private String birthday;

	@ApiModelProperty(name = "entryDate", notes = "入职日期")
	private String entryDate;
	
	@ApiModelProperty(name = "leaveDate", notes = "离职日期")
	private LocalDate leaveDate;

	@ApiModelProperty(name = "education", notes = "学历")
	private String education;

	@ApiModelProperty(name = "updateTime", notes = "更新时间")
	private LocalDateTime updateTime;

	@ApiModelProperty(name = "isDelete", notes = "是否已删除 1已删除 0未删除（更新时不会更新）")
	private String isDelete;

	@ApiModelProperty(name = "version", notes = "版本号（更新时不会更新）")
	private Integer version;

	@ApiModelProperty(name = "from", notes = "来源")
	private String from;

	@ApiModelProperty(name = "params", notes = "用户参数（获取单个用户时才会有值）")
	private Map<String, Object> params;

	public UserVo(User user) {
		this.id = user.getId();
		this.account = user.getAccount();
		this.fullname = user.getFullname();
		this.userNumber = user.getUserNumber();
		this.email = user.getEmail() == null ? "" : user.getEmail();
		this.mobile = user.getMobile() == null ? "" : user.getMobile();
		this.address = user.getAddress() == null ? "" : user.getAddress();
		this.sex = user.getSex() == null ? "" : user.getSex();
		this.photo = user.getPhoto() == null ? "" : user.getPhoto();
		this.status = user.getStatus();
		this.idCard = user.getIdCard() == null ? "" : user.getIdCard();
		this.phone = user.getPhone() == null ? "" : user.getPhone();
		this.birthday = BeanUtils.isNotEmpty(user.getBirthday())
				? DateFormatUtil.format(user.getBirthday().atStartOfDay(), StringPool.DATE_FORMAT_DATE) : "";
		this.entryDate = BeanUtils.isNotEmpty(user.getEntryDate())
				? DateFormatUtil.format(user.getEntryDate().atStartOfDay(), StringPool.DATE_FORMAT_DATE) : "";
		this.leaveDate = user.getLeaveDate();
		this.education = user.getEducation() == null ? "" : user.getEducation();
		this.updateTime = user.getUpdateTime();
		this.isDelete = user.getIsDelete();
		this.version = user.getVersion() == null ? 1 : user.getVersion();
		this.from = user.getFrom();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static User parser(UserVo userVo) throws ParseException {
		/*
		 * PasswordEncoder p =
		 * (PasswordEncoder)AppUtil.getBean(PasswordEncoder.class);
		 */
		User user = new User();
		user.setAccount(userVo.getAccount().toLowerCase());
		user.setFullname(userVo.getFullname());
		user.setUserNumber(userVo.getUserNumber());
		user.setEmail(userVo.getEmail());
		user.setMobile(userVo.getMobile());
		user.setAddress(userVo.getAddress());
		user.setSex(userVo.getSex());
		user.setPhoto(userVo.getPhoto());
		user.setStatus(userVo.getStatus());
		user.setIdCard(userVo.getIdCard());
		user.setPhone(userVo.getPhone());
		user.setBirthday(StringUtil.isNotEmpty(userVo.getBirthday())
				? DateFormatUtil.parse(userVo.getBirthday(), StringPool.DATE_FORMAT_DATE).toLocalDate() : null);
		user.setEntryDate(StringUtil.isNotEmpty(userVo.getEntryDate())
				? DateFormatUtil.parse(userVo.getEntryDate(), StringPool.DATE_FORMAT_DATE).toLocalDate() : null);
		user.setLeaveDate(userVo.getLeaveDate());
		user.setEducation(userVo.getEducation());
		/* user.setPassword(p.encode(userVo.getPassword())); */
		return user;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public LocalDate getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(LocalDate leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String toString() {
		return "{" + "\"" + "account" + "\"" + ":" + "\"" + this.account + "\"," + "\"" + "fullname" + "\"" + ":" + "\""
				+ this.fullname + "\"," + "\"" + "userNumber" + "\"" + ":" + "\"" + this.userNumber + "\"," + "\""
				+ "password" + "\"" + ":" + "\"" + this.password + "\"," + "\"" + "email" + "\"" + ":" + "\""
				+ this.email + "\"," + "\"" + "mobile" + "\"" + ":" + "\"" + this.mobile + "\"," + "\"" + "address"
				+ "\"" + ":" + "\"" + this.address + "\"," + "\"" + "sex" + "\"" + ":" + "\"" + this.sex + "\"," + "\""
				+ "status" + "\"" + ":" + "\"" + this.status + "\"," + "\"" + "idCard" + "\"" + ":" + "\"" + this.idCard
				+ "\"," + "\"" + "phone" + "\"" + ":" + "\"" + this.phone + "\"," + "\"" + "birthday" + "\"" + ":"
				+ "\"" + this.birthday + "\"," + "\"" + "entryDate" + "\"" + ":" + "\"" + this.entryDate + "\"," + "\"" + "leaveDate" + "\"" + ":" + "\"" + this.leaveDate + "\"," + "\""
				+ "education" + "\"" + ":" + "\"" + this.education + "\"" + "\"" + "updateTime" + "\"" + ":" + "\""
				+ this.updateTime + "\"" + "\"" + "isDelete" + "\"" + ":" + "\"" + this.isDelete + "\"" + "\""
				+ "version" + "\"" + ":" + "\"" + this.version + "\"" + "\"" + "from" + "\"" + ":" + "\"" + this.from
				+ "\"" + "}";
	}

	public UserVo() {

	}

	@Override
	public String compare() throws Exception {
		return "";
	}

	public static UserVo changeVo(User oldVo) {
		UserVo newVo = new UserVo();
		if (BeanUtils.isEmpty(oldVo))
			return newVo;
		newVo.setAccount(oldVo.getAccount());
		newVo.setAddress(oldVo.getAddress());
		newVo.setBirthday(DateFormatUtil.format(oldVo.getBirthday().atStartOfDay(), StringPool.DATE_FORMAT_DATE));
		newVo.setEducation(oldVo.getEducation());
		newVo.setEmail(oldVo.getEmail());
		newVo.setEntryDate(DateFormatUtil.format(oldVo.getEntryDate().atStartOfDay(), StringPool.DATE_FORMAT_DATE));
		newVo.setLeaveDate(oldVo.getLeaveDate());
		newVo.setFrom(oldVo.getFrom());
		newVo.setFullname(oldVo.getFullname());
		newVo.setIdCard(oldVo.getIdCard());
		newVo.setIsDelete(oldVo.getIsDelete());
		newVo.setMobile(oldVo.getMobile());
		newVo.setPhone(oldVo.getPhone());
		newVo.setPhoto(oldVo.getPhoto());
		newVo.setSex(oldVo.getSex());
		newVo.setStatus(oldVo.getStatus());
		newVo.setUpdateTime(oldVo.getUpdateTime());
		newVo.setUserNumber(oldVo.getUserNumber());
		return newVo;
	}
}
