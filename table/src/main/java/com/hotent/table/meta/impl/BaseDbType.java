package com.hotent.table.meta.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hotent.table.meta.IDbType;

public class BaseDbType implements IDbType {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected JdbcTemplate jdbcTemplate;

	@Override
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
