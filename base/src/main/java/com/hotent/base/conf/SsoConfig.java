package com.hotent.base.conf;

import java.io.UnsupportedEncodingException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.Base64;
import com.hotent.base.util.JsonUtil;

/**
 * 单点登录配置文件
 * @author liyg
 * @Date 2018-08-07
 */
@Component
@ConfigurationProperties(prefix = "sso")
public class SsoConfig {
	public final static String MODE_CAS = "cas";
	public final static String MODE_OAUTH = "oauth";
	//public final static String MODE_BASIC = "basic";
    public final static String MODE_JWT = "jwt";
	
	// 是否开启单点登录
	private boolean enable;
	// 单点登录模式
	private String mode;
	// cas配置
	private Cas cas;
	// oauth配置
	private Oauth oauth;
	
	static class Cas {
		// 基础地址
		private String url;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
	
	static class Oauth {
		// 基础地址
		private String url;
		// 登录路径
		private String loginPath;
		// 获取token的路径
		private String tokenPath;
		// 检查token的路径
		private String checkPath;
		// 客户端ID
		private String clientId;
		// 客户端秘钥
		private String secret;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getLoginPath() {
			return loginPath;
		}
		public void setLoginPath(String loginPath) {
			this.loginPath = loginPath;
		}
		public String getTokenPath() {
			return tokenPath;
		}
		public void setTokenPath(String tokenPath) {
			this.tokenPath = tokenPath;
		}
		public String getCheckPath() {
			return checkPath;
		}
		public void setCheckPath(String checkPath) {
			this.checkPath = checkPath;
		}
		public String getClientId() {
			return clientId;
		}
		public void setClientId(String clientId) {
			this.clientId = clientId;
		}
		public String getSecret() {
			return secret;
		}
		public void setSecret(String secret) {
			this.secret = secret;
		}
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Cas getCas() {
		return cas;
	}

	public void setCas(Cas cas) {
		this.cas = cas;
	}

	public Oauth getOauth() {
		return oauth;
	}

	public void setOauth(Oauth oauth) {
		this.oauth = oauth;
	}
	
	public String getCasUrl() {
		return cas.getUrl();
	}
	/**
	 * 获取单点登录地址
	 * @return
	 */
	public String getSsoUrl() {
		String ssoUrl = null;
		if(enable) {
			if(MODE_CAS.equals(mode)) {
				ssoUrl = cas.getUrl() + "?service=";
			}
			else if(MODE_OAUTH.equals(mode)) {
				String stufix = String.format("%s?response_type=code&client_id=%s&client_secret=%s&redirect_uri=", oauth.getLoginPath(), oauth.getClientId(), oauth.getSecret());
				ssoUrl = oauth.getUrl() + stufix;
			}
		}
		return ssoUrl;
	}
	/**
	 * 获取单点退出地址
	 * @return
	 */
	public String getSsoLogoutUrl() {
		String ssoLogoutUrl = null;
		if(enable) {
			if(MODE_CAS.equals(mode)) {
				ssoLogoutUrl = cas.getUrl() + "/logout?service=";
			}
			else if(MODE_OAUTH.equals(mode)) {
				ssoLogoutUrl = oauth.getUrl() + "/logout?redirect_uri=";
			}
		}
		return ssoLogoutUrl;
	}
	/**
	 * 获取oauth请求token的地址
	 * @return
	 */
	public String getOauthTokenUrl() {
		String url = null;
		if(enable && MODE_OAUTH.equals(mode)) {
			String stufix = String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s", oauth.getTokenPath(), oauth.getClientId(), oauth.getSecret());
			url = oauth.getUrl() + stufix;
		}
		return url;
	}
	/**
	 * 获取oauth验证token的地址
	 * @return
	 */
	public String getOauthCheckUrl() {
		String url = null;
		if(enable && MODE_OAUTH.equals(mode)) {
			String stufix = String.format("%s?token=", oauth.getCheckPath());
			url = oauth.getUrl() + stufix;
		}
		return url;
	}
	
	/**
	 * 获取oauth认证时的basic头部
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getOauthBasicHeader() throws UnsupportedEncodingException {
		String basicStr = oauth.getClientId() + ":" + oauth.getSecret();
		ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
		objectNode.put("Authorization", "Basic " + Base64.getBase64(basicStr));
		String json = objectNode.toString();
		return Base64.getBase64(json);
	}
}
