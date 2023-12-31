package com.hotent.form.persistence.manager.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.util.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.form.enums.FieldControlType;
import com.hotent.form.model.QueryMetafield;
import com.hotent.form.model.QuerySqldef;
import com.hotent.form.model.QuerySqldefXml;
import com.hotent.form.model.QuerySqldefXmlList;
import com.hotent.form.model.QueryView;
import com.hotent.form.persistence.dao.QuerySqldefDao;
import com.hotent.form.persistence.manager.QueryMetafieldManager;
import com.hotent.form.persistence.manager.QuerySqldefManager;
import com.hotent.form.persistence.manager.QueryViewManager;
import com.hotent.table.datasource.DataSourceUtil;
import com.hotent.table.model.Column;

@Service("querySqldefManager")
public class QuerySqldefManagerImpl extends BaseManagerImpl<QuerySqldefDao, QuerySqldef> implements QuerySqldefManager {
	@Resource
	QueryMetafieldManager queryMetafieldManager;
	@Resource
	QueryViewManager queryViewManager;
	@Resource
	PortalFeignService portalFeignService;
	@Resource
	DatabaseContext databaseContext;

	@Override
	@Transactional
	public void remove(Serializable entityId) {
		QuerySqldef querySqldef=this.get(entityId);
		//删除元数据
		queryMetafieldManager.removeBySqlId((String)entityId);
		//删除视图
		queryViewManager.removeBySqlAlias(querySqldef.getAlias());
		//删除本身
		super.remove(entityId);
	}

	@Override
	public ObjectNode checkSql(String dsName, String sql) {
		ObjectNode data = JsonUtil.getMapper().createObjectNode();
		JdbcTemplate jdbcTemplate = null;
		try {
			jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
			jdbcTemplate.execute(sql);
			data.put("result", true);
			data.put("message", "验证通过");
		} catch (Exception e) {
			data.put("result", false);
			data.put("message", e.getMessage());
		}
		throw new RuntimeException(data.toString());
	}

	@Override
	@Transactional
	public boolean save(QuerySqldef querySqldef) {
		boolean result = false;
		checkBeforeSave(querySqldef);
		try(DatabaseSwitchResult dResult = databaseContext.setDataSource(querySqldef.getDsName())){
			checkSql(querySqldef.getSql());
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
		QuerySqldef byAlias = getByAlias(querySqldef.getAlias());
		QuerySqldef sqldef = get(querySqldef.getId());
		if (StringUtil.isEmpty(querySqldef.getId())) {// 新增的
			if(byAlias != null){
				throw new BaseException(querySqldef.getAlias()+"该别名已存在，请更换！");
			}
			querySqldef.setId(UniqueIdUtil.getSuid());
			result = super.save(querySqldef);
			initMetafield(querySqldef);
		} else if(BeanUtils.isEmpty(sqldef) && StringUtil.isNotEmpty(querySqldef.getId())){
			result = super.save(querySqldef);
			initMetafield(querySqldef);
		} else {
			if(byAlias != null && !byAlias.getId().equals(querySqldef.getId())){
				throw new BaseException(querySqldef.getAlias()+"该别名已存在，请更换！");
			}
			result = super.updateById(querySqldef);
		}

		// 开始处理queryMetafield字段
		queryMetafieldManager.removeBySqlId(querySqldef.getId());// 删除旧的
		for (QueryMetafield field : querySqldef.getMetafields()) {
			field.setId(UniqueIdUtil.getSuid());
			queryMetafieldManager.create(field);
		}
		return result;
	}

	private void checkSql(String sql){
		if(SQLUtil.containsSqlInjection(sql)){
			throw new BaseException("sql语句含有非法注入！");
		}
		JdbcTemplate jdbcTemplate = null;
		jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
		try {
			jdbcTemplate.execute(sql);
		}catch (Exception e) {
			throw new BaseException("SQL验证失败:"+e.getMessage());
		}
	}

	/**
	 * 保存前的验证 有问题直接throw异常
	 * 
	 * @param querySqldef
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	private void checkBeforeSave(QuerySqldef querySqldef) {
		if (!StringUtil.isEmpty(querySqldef.getId())) {
			return;
		}
		if (getByAlias(querySqldef.getAlias()) != null) {
			throw new RuntimeException("别名:" + querySqldef.getAlias() + ",已被使用");
		}
	}

	@Override
	public QuerySqldef getByAlias(String alias) {
		QueryFilter<QuerySqldef> queryFilter = QueryFilter.<QuerySqldef>build().withDefaultPage();
		queryFilter.addFilter("alias_", alias, QueryOP.EQUAL);
		PageList<QuerySqldef> pageList = query(queryFilter);
		if (pageList != null && !pageList.getRows().isEmpty()) {
			return pageList.getRows().get(0);
		}
		return null;
	}

	/**
	 * 新增时获取初始化字段配置
	 * 
	 * @param querySqldef
	 * @return List<QueryMetafield>
	 * @exception
	 * @since 1.0.0
	 */
	@Transactional
    private void initMetafield(QuerySqldef querySqldef) {
		List<QueryMetafield> list = new ArrayList<QueryMetafield>();
		JdbcTemplate jdbcTemplate = null;
		try {
			jdbcTemplate =DataSourceUtil.getJdbcTempByDsAlias(querySqldef.getDsName());
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
		SqlRowSet srs = jdbcTemplate.queryForRowSet(querySqldef.getSql());
		SqlRowSetMetaData srsmd = srs.getMetaData();
		// 列从1开始算
		for (int i = 1; i < srsmd.getColumnCount() + 1; i++) {
			String cn ="";
			String fn ="";
			if(StringUtil.isEmpty(srsmd.getTableName(i))) {
				//获取字段别名 
				cn = srsmd.getColumnName(i).toUpperCase();
	            //获取实际字段别名 
	            fn = srsmd.getColumnName(i).toUpperCase();
			}else if(StringUtil.isNotEmpty(srsmd.getTableName(i))){
				//获取字段别名  表名点字段名
				cn = srsmd.getTableName(i)+"."+srsmd.getColumnName(i).toUpperCase();
	            //获取实际字段别名  表名+"_"+字段名
	            fn = srsmd.getTableName(i)+"_"+srsmd.getColumnName(i).toUpperCase();
			}
			String ctn = srsmd.getColumnTypeName(i);
			QueryMetafield field = new QueryMetafield();
			field.setSqlId(querySqldef.getId());
			field.setName(cn);
			field.setFieldName(fn);
			field.setFieldDesc(fn);

			field.setDataType(simplifyDataType(ctn));
			field.setIsShow(QueryMetafield.TRUE);
			field.setIsSearch(QueryMetafield.FALSE);
			field.setControlType(FieldControlType.ONETEXT.key);
			field.setIsVirtual(QueryMetafield.FALSE);

			field.setSn((short) i);
			list.add(field);
		}
		querySqldef.setMetafields(list);
	}

	/**
	 * 把数据库对应的字段类型 简化成 四种基本的数据库字段类型（varchar,number,date,text）
	 * 
	 * @param type
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	private String simplifyDataType(String type) {
		type = type.toLowerCase();
		
		String number = portalFeignService.getPropertyByAlias("datatype.number");
		String date = portalFeignService.getPropertyByAlias("datatype.date");
		String text = portalFeignService.getPropertyByAlias("datatype.text");
		String varchar = portalFeignService.getPropertyByAlias("datatype.varchar");

		if (varchar.contains(type)) {
			return Column.COLUMN_TYPE_VARCHAR;
		}
		if (text.contains(type)) {
			return Column.COLUMN_TYPE_VARCHAR;
		}
		if (date.contains(type)) {
			return Column.COLUMN_TYPE_DATE;
		}
		if (number.contains(type)) {
			return Column.COLUMN_TYPE_NUMBER;
		}

		return type;
	}

	@Override
	public String export(String[] idList) throws Exception {
		if(BeanUtils.isEmpty(idList))
			return "";
		
		QuerySqldefXmlList list=new QuerySqldefXmlList();
		
		for(String id:idList){
			QuerySqldef def= this.get(id);
			List<QueryMetafield> metaFieldList= queryMetafieldManager.getBySqlId(id);
			List<QueryView> viewList=queryViewManager.getBySqlAlias(def.getAlias());
			
			QuerySqldefXml defXml=new QuerySqldefXml();
			
			defXml.setQuerySqldef(def);
			defXml.setMetafieldList(metaFieldList);
			defXml.setQueryViewList(viewList);
			list.addQuerySqlDef(defXml);
		}
		
		String xml=JAXBUtil.marshall(list, QuerySqldefXmlList.class);
		
		return xml;
	}

	@Override
	@Transactional
	public void importDef(String path) {
		try {
			String xml = FileUtil.readFile(path + File.separator + "sqldef.xml");
			if(StringUtil.isEmpty(xml)) return ;
			
			QuerySqldefXmlList list=(QuerySqldefXmlList) JAXBUtil.unmarshall(xml,QuerySqldefXmlList.class);
			List<QuerySqldefXml> sqlDefList=list.getQuerySqlDefList();
			for(QuerySqldefXml def:sqlDefList){
				importDef(def);
			}
		} catch (Exception e) {
			throw new BaseException(e.getMessage(),e);
		}
	}
	
	private void importDef(QuerySqldefXml def){
		QuerySqldef sqlDef= def.getQuerySqldef();
		QuerySqldef tmp= getByAlias(sqlDef.getAlias());
		if(tmp!=null){
			ThreadMsgUtil.addMsg("定义："+tmp.getName()+"，已存在故跳过");
			return ;
		}
		
		String sqlId=UniqueIdUtil.getSuid();
		sqlDef.setId(sqlId);
		this.create(sqlDef);
		//字段
		List<QueryMetafield> fieldList=def.getMetafieldList();
		for(QueryMetafield field:fieldList){
			field.setId(UniqueIdUtil.getSuid());
			field.setSqlId(sqlId);
			
			queryMetafieldManager.create(field);
		}
		//导入视图
		List<QueryView> viewList = def.getQueryViewList();
		for(QueryView view:viewList){
			view.setId(UniqueIdUtil.getSuid());
			queryViewManager.create(view);
		}
		
		ThreadMsgUtil.addMsg("定义："+sqlDef.getName()+"，成功导入!");
	}
	
	@Override
	@Transactional
	public List<QueryMetafield> refreshFields(String id) {
		//得到原有的字段集合
		QuerySqldef querySqldef = this.get(id);
		List<QueryMetafield> oldMetafields = queryMetafieldManager.getBySqlId(id);
		//查询出现有的字段集合
		initMetafield(querySqldef);
		List<QueryMetafield> newMetafields = querySqldef.getMetafields();
		//存放更新后的字段集合
		List<QueryMetafield> returnMetafields=new ArrayList<>();
		//根据字段名一个个进行对比
		for(int x=0;x<oldMetafields.size();x++){
			for(int i=0;i<newMetafields.size();i++){
				if(newMetafields.get(i).getName().equals(oldMetafields.get(x).getName())){
					returnMetafields.add(oldMetafields.get(x));
					newMetafields.remove(i);
					continue;
				}
			}
		}
		//对比完后,还剩余的就是新增字段
		returnMetafields.addAll(newMetafields);
		//返回最新的字段集合
		return returnMetafields;
	}

}
