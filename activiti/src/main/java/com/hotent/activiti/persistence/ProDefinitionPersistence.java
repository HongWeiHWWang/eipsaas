package com.hotent.activiti.persistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

@Repository
public class ProDefinitionPersistence {
	private Log logger = LogFactory.getLog(ProDefinitionPersistence.class);
	@Resource
	JdbcTemplate jdbcTemplate;

	/**
	 * 取得流程定义的XML
	 * 
	 * @param deployId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getDefXmlByDeployId(String deployId) {
		String sql = "select a.* from ACT_GE_BYTEARRAY a where NAME_ LIKE '%bpmn20.xml' and DEPLOYMENT_ID_= ? ";
		final LobHandler lobHandler = new DefaultLobHandler();
		final ByteArrayOutputStream contentOs = new ByteArrayOutputStream();
		String defXml = null;
		try {
			jdbcTemplate.query(sql, new Object[] { deployId }, new AbstractLobStreamingResultSetExtractor() {
				public void streamData(ResultSet rs) throws SQLException, IOException {
					FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs, "BYTES_"), contentOs);
				}
			});
			defXml = new String(contentOs.toByteArray(), "UTF-8");
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		}
		return defXml;
	}

	/**
	 * 把修改过的xml更新至回流程定义中
	 * 
	 * @param deployId
	 * @param defXml
	 */
	public void writeDefXml(final String deployId, String defXml) {
		try {
			LobHandler lobHandler = new DefaultLobHandler();
			final byte[] btyesXml = defXml.getBytes("UTF-8");
			String sql = "update ACT_GE_BYTEARRAY set BYTES_=? where NAME_ LIKE '%bpmn20.xml' and DEPLOYMENT_ID_= ? ";
			jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				@Override
				protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
					lobCreator.setBlobAsBytes(ps, 1, btyesXml);
					ps.setString(2, deployId);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
