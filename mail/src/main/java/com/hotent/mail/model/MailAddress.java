package com.hotent.mail.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 邮件自定义显示名及邮件地址辅助类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@ApiModel(description="邮件自定义显示名及邮件地址辅助类")
public class MailAddress {
	
	@ApiModelProperty(name="address", notes="地址")
	protected String address = "";
	
	@ApiModelProperty(name="name", notes="名称")
	protected String name = "";
	
	/**
	 * 获取地址
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * 设置地址
	 * @param address 地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * 获取名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 邮件自定义显示名及邮件地址辅助类无参构造方法
	 */
	public MailAddress() {

	}
	
	/**
	 * 邮件自定义显示名及邮件地址辅助类有参构造方法
	 * @param address 地址
	 * @param name    名称
	 */
	public MailAddress(String address, String name) {
		this.address = address;
		this.name = name;
	}
}
