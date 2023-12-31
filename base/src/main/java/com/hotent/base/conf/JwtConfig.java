package com.hotent.base.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt
 * @author zhangxw
 * @Date 2020-06-08
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
	
	private String header;
	
	private String secret;
	
	// 是否单用户登录
	private boolean single;
	
	//有效期
	private int expiration;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

	public int getExpiration() {
		return expiration;
	}

	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}
	
	
	
}
