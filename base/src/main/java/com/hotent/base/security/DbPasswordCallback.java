package com.hotent.base.security;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;

public class DbPasswordCallback extends DruidPasswordCallback {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DbPasswordCallback.class);

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		String password = (String) properties.get("password");
		String publickey = (String) properties.get("publickey");
		try {
			String dbpassword = ConfigTools.decrypt(publickey, password);
			setPassword(dbpassword.toCharArray());
		} catch (Exception e) {
			LOGGER.error("Druid ConfigTools.decrypt", e);
		}
	}

}
