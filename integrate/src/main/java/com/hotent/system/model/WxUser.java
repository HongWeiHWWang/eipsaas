package com.hotent.system.model;

import com.hotent.base.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WxUser {
	private static final Log logger = LogFactory.getLog(WxOrg.class);
	private String userid;
	private String name;
	/* 所属部门 */
	private String[] department;
	/* 岗位 */
	private String position;
	private String mobile;
	/* 性别 */
	private String gender;
	private String email;
	private String weixinid;
	private String enable;


	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getDepartment() {
		return department;
	}

	public void setDepartment(String[] department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeixinid() {
		return weixinid;
	}

	public void setWeixinid(String weixinid) {
		this.weixinid = weixinid;
	}

	@Override
	public String toString() {
		String json = "";
		try {
			json = JsonUtil.toJson(this);
		} catch (Exception e) {
			logger.error(e);
		}
		return json;
	}
}
