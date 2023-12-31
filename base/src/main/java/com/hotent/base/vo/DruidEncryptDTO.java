package com.hotent.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 动态数据源连接信息
 *
 * @company 广州宏天软件股份有限公司
 * @author jason
 * @email liygui@jee-soft.cn
 * @date 2020年4月6日
 */
@ApiModel(value="DruidEncryptDTO", description="密码加密")
public class DruidEncryptDTO {

	@ApiModelProperty(value = "JDBC 密码")
	private String password;
	
	@ApiModelProperty(value = "公钥")
	private String publicKey;
	


	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
