package com.hotent.bpm.engine.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.process.def.BpmSubTableRight;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.persistence.manager.BpmBusLinkManager;
import com.hotent.table.datasource.DataSourceUtil;
import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * 子表数据读取。
 * 
 * @author ray
 *
 */
public class BoSubDataHandlerImpl  {

	@Resource
	JdbcTemplate jdbcTemplate;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	BpmBusLinkManager bpmBusLinkManager;

	public List<Map<String, Object>> getSubDataByFk(ObjectNode boEnt, Object fkValue) throws Exception {
		// 获取子表权限
		String defId = (String) ContextThreadUtil.getCommuVar("defId","");
		String nodeId = (String) ContextThreadUtil.getCommuVar("nodeId","");
		String parentDefKey = (String) ContextThreadUtil.getCommuVar("parentDefKey", BpmConstants.LOCAL);

		BpmSubTableRight bpmSubTableRight = getSubTableRight(defId, nodeId, parentDefKey, boEnt);

		// 拼装sql
		String sql = "";
		if (boEnt.get("type").asText().equals("manytomany")) {
			sql = "select A.* from " + boEnt.get("tableName").asText() + " A , form_bo_data_relation B where " + " B.SUB_BO_NAME = '" + boEnt.get("name").asText() + "' AND A." + boEnt.get("pkKey").asText() + "=B.FK_  AND B.PK_=?";
		} else {
			String fk = boEnt.get("fk").asText();
			if(StringUtil.isEmpty(fk)){
				throw new RuntimeException("通过添加外部表构建业务对象时必须指定外键");
			}
			sql = "select * from " + boEnt.get("tableName").asText() + " A  where A." + fk + "=?";
		}
		sql = handleRight(bpmSubTableRight, fkValue, sql);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if( boEnt.get("isExternal").asInt()==1 ){
			//外部表数据
			try {
				list = DataSourceUtil.getJdbcTempByDsAlias(boEnt.get("dsName").asText()).queryForList(sql, fkValue);
			} catch (Exception e) {
				throw new RuntimeException("操作外部表：" + boEnt.get("dsName").asText() + " 中的 " + boEnt.get("desc").asText() + " 出错："+e.getMessage(), e);
			}
		
		}else{
			list = jdbcTemplate.queryForList(sql, fkValue);
		}
		
		return list;
	}

	/**
	 * 获取权限。
	 * 
	 * @param defId
	 * @param nodeId
	 * @param parentDefKey
	 * @param boEnt
	 * @return
	 * @throws Exception 
	 */
	private BpmSubTableRight getSubTableRight(String defId, String nodeId, String parentDefKey, ObjectNode boEnt) throws Exception {
		if(StringUtil.isEmpty(nodeId)) return null;
		
   		BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		UserTaskNodeDef utnd = (UserTaskNodeDef) nodeDef;
		BpmSubTableRight bpmSubTableRight = null;
		List<BpmSubTableRight> list=utnd.getBpmSubTableRightByParentDefKey(parentDefKey);
		for (BpmSubTableRight bsr : list) {
			if (bsr.getTableName().equals(boEnt.get("name").asText())) {
				bpmSubTableRight = bsr;
				break;
			}
		}
		return bpmSubTableRight;
	}

	private String handleRight(BpmSubTableRight right, Object fkValue, String sql) throws Exception {
		if (right == null)
			return sql;
		if (right.getRightType().equals("script")) {
			String str = groovyScriptEngine.executeString(right.getScript(), new HashMap<String, Object>());
			sql += " and " + str;
		} else if (right.getRightType().equals("curUser")) {
			sql = "select a.* from ("+sql+") a , bpm_bus_link b where a.ID_ = B.businesskey_str_ and  B.start_id_="+ContextUtil.getCurrentUserId();
		}else if(right.getRightType().equals("curOrg")){
			sql = "select a.* from ("+sql+") a , bpm_bus_link b where a.ID_ = B.businesskey_str_ and  B.start_group_id_="+ContextUtil.getCurrentGroupId();
		}
		return sql;
	}
}
