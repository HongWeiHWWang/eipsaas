package com.hotent.uc.params.org;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapConfig {
	private String url;
	private String name;
	private String password;
	private String demCode;
	private String jobCode;
	public String getUrl() {
		return url;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getDemCode() {
		return demCode;
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDemCode(String demCode) {
		this.demCode = demCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	
}
