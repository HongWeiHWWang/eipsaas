package com.hotent.uc.ws;

import java.io.Serializable;

import org.springframework.util.Assert;

import com.hotent.base.util.StringUtil;

/**
 * Webservice外观的用户对象
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2019年4月29日
 */
public class WsFacadeUser implements Serializable{
	private static final long serialVersionUID = 7746293507656779309L;
	
	public static final String OPERATE_TYPE_ADD = "1";			/*新增*/
	public static final String OPERATE_TYPE_UPD = "2";			/*修改*/
	public static final String OPERATE_TYPE_DEL = "3";			/*删除*/
	public static final String OPERATE_TYPE_UPD_TIME = "4";		/*修改信息并且更新密码修改时间*/
	public static final String OPERATE_TYPE_ADD_ORDER = "5";	/*新增并且自动提vpn申请工单*/
	
	public static final String defaulPassword = "123456";		/*默认密码*/
	
	private String account;			/*用户账号*/
	private String mobile;			/*用户手机号码*/
	private String fullname;		/*用户姓名*/
	private String email;			/*用户email*/
	private String operatetype;		/*要执行的操作类型*/
	
	public WsFacadeUser(String account, String mobile, String fullname, String email, String operatetype) {
		Assert.isTrue(StringUtil.isNotEmpty(account), "account不能为空");
		Assert.isTrue(StringUtil.isNotEmpty(fullname), "fullname不能为空");
		Assert.isTrue(StringUtil.isNotEmpty(operatetype), "operatetype不能为空");
		this.account = account;
		this.mobile = mobile;
		this.fullname = fullname;
		this.email = email;
		this.operatetype = operatetype;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOperatetype() {
		return operatetype;
	}

	public void setOperatetype(String operatetype) {
		this.operatetype = operatetype;
	}
}
