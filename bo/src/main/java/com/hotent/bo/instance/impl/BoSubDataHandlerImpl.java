package com.hotent.bo.instance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hotent.base.util.StringUtil;
import com.hotent.bo.constant.BoConstants;
import com.hotent.bo.instance.BoSubDataHandler;
import com.hotent.bo.model.BoEnt;
import com.hotent.table.datasource.DataSourceUtil;

/**
 * 获取子表数据接口的默认实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月13日
 */
@Service
public class BoSubDataHandlerImpl implements BoSubDataHandler{
	@Resource
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> getSubDataByFk(BoEnt boEnt, Object fkValue) {
		// 拼装sql
		String sql = "";
		if (boEnt.getType().equals(BoConstants.RELATION_MANY_TO_MANY)) {
			sql = "select A.* from " + boEnt.getTableName() + " A , form_bo_data_relation B where " + " B.SUB_BO_NAME = '" + boEnt.getName() + "' AND A." + boEnt.getPkKey() + "=B.FK_  AND B.PK_=?";
		} else {
			String fk = boEnt.getFk();
			if(StringUtil.isEmpty(fk)){
				throw new RuntimeException("通过添加外部表构建业务对象时必须指定外键");
			}
			sql = "select * from " + boEnt.getTableName() + " A  where A." + fk + "=?";
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if( boEnt.isExternal() ){
			//外部表数据
			try {
				list = DataSourceUtil.getJdbcTempByDsAlias(boEnt.getDsName()).queryForList(sql, fkValue);
			} catch (Exception e) {
				throw new RuntimeException("操作外部表：" + boEnt.getDsName() + " 中的 " + boEnt.getDesc() + " 出错："+e.getMessage(), e);
			}

		}else{
			list = jdbcTemplate.queryForList(sql, fkValue);
		}

		return list;
	}
}
