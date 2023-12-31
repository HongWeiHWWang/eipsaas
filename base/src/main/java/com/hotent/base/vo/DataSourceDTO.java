package com.hotent.base.vo;

import javax.validation.constraints.NotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 动态数据源连接信息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月6日
 */
@ApiModel(value="DataSourceDTO", description="动态数据源连接信息")
public class DataSourceDTO {
	@NotBlank
	@ApiModelProperty(value = "连接池名称", example = "test")
	private String pollName;

	@NotBlank
	@ApiModelProperty(value = "JDBC driver", example = "org.h2.Driver")
	private String driverClassName;

	@NotBlank
	@ApiModelProperty(value = "JDBC url 地址", example = "jdbc:h2:mem:test10")
	private String url;

	@NotBlank
	@ApiModelProperty(value = "JDBC 用户名", example = "sa")
	private String username;

	@ApiModelProperty(value = "JDBC 密码")
	private String password;

	public String getPollName() {
		return pollName;
	}

	public void setPollName(String pollName) {
		this.pollName = pollName;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
