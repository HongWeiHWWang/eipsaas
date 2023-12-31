package com.hotent.bo.persistence.manager.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.bodef.IBoEntHandler;
import com.hotent.bo.constant.BoConstants;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoEntRel;
import com.hotent.bo.persistence.dao.BoEntDao;
import com.hotent.bo.persistence.manager.BoAttributeManager;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.bo.persistence.manager.BoEntManager;
import com.hotent.bo.persistence.manager.BoEntRelManager;
import com.hotent.table.datasource.DataSourceUtil;
import com.hotent.table.meta.impl.BaseTableMeta;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.operator.ITableOperator;
import com.hotent.table.util.MetaDataUtil;

/**
 * 业务对象定义 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
@Service("boEntManager")
public class BoEntManagerImpl extends BaseManagerImpl<BoEntDao, BoEnt> implements BoEntManager {
	@Resource
	ITableOperator tableOperator;
	@Resource
	BoAttributeManager boAttributeManager;
	@Resource
	BoEntRelManager boEntRelManager;
	@Resource
	BoDefManager boDefManager;
	@Resource
	DatabaseContext databaseContext;

	@Override
	@Transactional
	public void createTable(BoEnt boEnt) throws SQLException {
		BoAttribute colPk = new BoAttribute();
		colPk.setName(BoEnt.PK_NAME);
		colPk.setFieldName(BoEnt.PK_NAME);
		colPk.setIsPk(true);
		colPk.setAttrLength(40);
		colPk.setCharLen(40);
		colPk.setColumnType(Column.COLUMN_TYPE_VARCHAR);
		colPk.setDataType(Column.COLUMN_TYPE_VARCHAR);
		colPk.setComment("主键");

		BoAttribute colFk = new BoAttribute();
		colFk.setName(BoEnt.FK_NAME);
		colFk.setFieldName(BoEnt.FK_NAME);
		colFk.setIsPk(false);
		colFk.setCharLen(40);
		colFk.setAttrLength(40);
		colFk.setDataType(Column.COLUMN_TYPE_VARCHAR);
		colFk.setColumnType(Column.COLUMN_TYPE_VARCHAR);
		colFk.setComment("外键");
		
		List<BoAttribute> colList = boEnt.getBoAttrList();
		boolean hasDataRev = false;
		for (BoAttribute col : colList) {
			if ("form_data_rev_".equals(col.getName())) {
				// 如果是其余数据库导出的建模导入，长度为0时设置为37
				if (col.getAttrLength()==0){
					col.setAttrLength(37);
				}
				hasDataRev = true;
			}
			col.setFieldName(col.getFieldName());
		}
        if (!hasDataRev) {
        	BoAttribute dateRevAttr = new BoAttribute();
        	dateRevAttr.setName("form_data_rev_");
        	dateRevAttr.setDesc("表单数据版本");
        	dateRevAttr.setDataType(Column.COLUMN_TYPE_INT);
        	dateRevAttr.setDefaultValue("0");
        	dateRevAttr.setFieldName(dateRevAttr.getFieldName());
			dateRevAttr.setAttrLength(37);
        	colList.add(dateRevAttr);
		}
		boEnt.addAttrFirst((BoAttribute) colFk);
		boEnt.addAttrFirst((BoAttribute) colPk);

		tableOperator.createTable(boEnt);

		boEnt.setIsCreatedTable(true);// 更新生成表的状态
		update(boEnt);
	}

	@Override
	@Transactional
	public String saveEnt(BoEnt boEnt) throws Exception {
		String entId;
		// 开始处理boEnt对象
		BoEnt dbBoEnt =null;//数据库的旧对象，更新时才有值
		BoEnt oldBoEnt = null;//更新时保存更新前的实体，方便创建表失败后还原
        List<BoAttribute> oldAttrList = new ArrayList<BoAttribute>();//更新前实体属性，方便创建表失败后还原
		if (StringUtil.isEmpty(boEnt.getId())) {
			if(baseMapper.getByName(boEnt.getName())!=null){
				throw new BaseException(String.format("已存在别名为%s的bo实体.", boEnt.getName()));
			}
			entId = UniqueIdUtil.getSuid();
			boEnt.setId(entId);
			boEnt.setStatus(BoEnt.STATUS_ACTIVED);
			this.create(boEnt);
		} else {
			entId = boEnt.getId();
			BoEnt temp=baseMapper.getByName(boEnt.getName());//数据库用这个名字的对象
			if(temp!=null&&!temp.getId().equals(boEnt.getId())){
				throw new BaseException(String.format("已存在别名为%s的bo实体.", boEnt.getName()));
			}
			
			dbBoEnt = getById(boEnt.getId());
			oldBoEnt = this.get(boEnt.getId());
			oldAttrList = boAttributeManager.getByBoEnt(boEnt);
			this.update(boEnt);
			boAttributeManager.removeByEntId(boEnt.getId());// 先删除旧的属性列表
		}

		// 开始处理字段attr对象
		List<BoAttribute> boAttrList = boEnt.getBoAttrList();
		List<BoAttribute> newAttrList = new ArrayList<BoAttribute>();// 新字段
		for (BoAttribute boAttribute : boAttrList) {
			boAttribute.setBoEnt(boEnt);
			if (StringUtil.isEmpty(boAttribute.getId())) {// 新字段
				newAttrList.add(boAttribute);
				boAttribute.setId(UniqueIdUtil.getSuid());
				boAttribute.setEntId(boEnt.getId());
				boAttribute.setBoEnt(boEnt);
			}
			boAttribute.setTableName(boEnt.getTableName());
			boAttributeManager.create(boAttribute);
		}

		// 开始处理物理表
		if (!boEnt.isCreatedTable() || boEnt.isExternal()) {// 没生成表或外部表就不处理
			return entId;
		}
		if (getCanEditByName(boEnt.getName()) == 0) {// 处于任意修改的状态
			if(dbBoEnt!=null){
				tableOperator.dropTable(dbBoEnt.getTableName());// 删除旧表
			}
			boEnt = getById(boEnt.getId());
			try {
                createTable(boEnt);// 新增表
            } catch (Exception e) {
                if(BeanUtils.isNotEmpty(oldBoEnt)){
                    boAttributeManager.removeByEntId(oldBoEnt.getId());// 先删除旧的属性列表
                    for (BoAttribute boAttribute : oldAttrList) {//还原之前的属性
                         boAttributeManager.create(boAttribute);
                    }
                    this.update(oldBoEnt);//还原之前的实体
                    createTable(oldBoEnt);// 还原之前的表结构
                    // 事务不会回滚
                    throw new Exception("生成表失败："+ExceptionUtils.getRootCause(e).getMessage());
                }
                // 事务回滚
                throw new BaseException("生成表失败："+ExceptionUtils.getRootCause(e).getMessage());
            }
		} else {// 已生成表，又不是任意修改，那么就应该在表结构插入新字段
			for (BoAttribute attr : newAttrList) {
				tableOperator.addColumn(boEnt.getTableName(), attr);
			}
		}
		return entId;
	}

	@Override
	@Transactional
	public void remove(String entityId) {
		List<BoEnt> byDefId = getByDefId(entityId);
		for(BoEnt b : byDefId) {
			BoEnt boEnt = get(b.getId());
			if (BeanUtils.isEmpty(boEnt)) {
				return;
			}
			// 验证实例是否已绑定定义
			String str = "";
			for (BoEntRel rel : boEntRelManager.getByEntId(entityId)) {
				BoDef boDef = boDefManager.get(rel.getBoDefid());
				if (StringUtil.isNotEmpty(str)) {
					str += ",";
				}
				str += boDef.getDescription();
			}
			if (StringUtil.isNotEmpty(str)) {
				throw new BaseException("实体“" + boEnt.getDesc() + "”已绑定业务对象定义“" + str + "”，不能被删除！");
			}

			// 先处理字段attr
			boAttributeManager.removeByEntId(entityId);
			super.remove(entityId);

			boAttributeManager.removeByEntId(entityId);// 删除对应字段信息
			if (boEnt.isCreatedTable() && !boEnt.isExternal()) {// 已生成物理表
				try {
					tableOperator.dropTable(boEnt.getTableName());
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public BoEnt getById(String entId) {
		BoEnt boEnt = get(entId);
		List<BoAttribute> list = boAttributeManager.getByBoEnt(boEnt);
		boEnt.setBoAttrList(list);
		return boEnt;
	}

	@Override
	public BoEnt getByName(String name) {
		BoEnt boEnt = baseMapper.getByName(name);
		if(BeanUtils.isNotEmpty(boEnt)){
			List<BoAttribute> list = boAttributeManager.getByBoEnt(boEnt);
			boEnt.setBoAttrList(list);
		}
		return boEnt;
	}

	@Override
	public int getCanEditByName(String name) {
		return 1;//目前编辑的时候只允许加字段，所以固定返回１
	}

	void handleAttr(List<BoAttribute> removeList,List<BoAttribute> addList,List<BoAttribute> updateList){
		for(BoAttribute attr:removeList){
			boAttributeManager.remove(attr.getId());
		}
		for(BoAttribute attr:addList){
			boAttributeManager.create(attr);
		}
		for(BoAttribute attr:updateList){
			boAttributeManager.update(attr);
		}
		
	}
	
	private List<BoAttribute> getRemoveList(List<BoAttribute>  attrList,List<Column> columnList){
		Set<String> attrSet=new HashSet<String>();
		for(Column col:columnList){
			attrSet.add(col.getFieldName());
		}
		List<BoAttribute> list=new ArrayList<BoAttribute>();
		for(BoAttribute attr:attrList){
			if(!attrSet.contains( attr.getFieldName())){
				list.add(attr);
			}
		}
		return list;
	}
	
	private List<BoAttribute> getAddList(List<BoAttribute>  attrList,List<Column> columnList,BoEnt boEnt){
		Map<String,BoAttribute> attrMap=new HashMap<String,BoAttribute>();
		for(BoAttribute attr:attrList){
			attrMap .put(attr.getFieldName(), attr);
		}
		
		List<BoAttribute> list=new ArrayList<BoAttribute>();
		for(Column column:columnList){
			
			String fieldName=column.getFieldName();
			if(fieldName.equalsIgnoreCase(boEnt.getPk())) continue;
			
			if(StringUtil.isNotEmpty(boEnt.getFk()) 
					&& 	 fieldName.equalsIgnoreCase(boEnt.getFk())) continue;
			
			if(attrMap.containsKey(column.getFieldName())) continue;
			BoAttribute attr=getByColumn(column);
			
			attr.setBoEnt(boEnt);
			
			attr.setEntId(boEnt.getId());
			list.add(attr);
		}
		return list;
	}
	
	private List<BoAttribute> getUpdateList(List<BoAttribute>  attrList,List<Column> columnList,BoEnt boEnt){
		Set<String> attrSet=new HashSet<String>();
		Map<String,String> attrMap = new HashMap<String, String>();
		for(BoAttribute attr:attrList){
			attrSet.add(attr.getFieldName());
			attrMap.put(attr.getFieldName(), attr.getId());
		}
		List<BoAttribute> list=new ArrayList<BoAttribute>();
		for(Column column:columnList){
			if(attrSet.contains(column.getFieldName())){
				BoAttribute attrTmp = getByColumn(column,attrMap.get(column.getFieldName()));
				attrTmp.setBoEnt(boEnt);
				attrTmp.setEntId(boEnt.getId());
				list.add(attrTmp);
			}
		}
		return list;
	}
	
	private BoAttribute getByColumn(Column column){
		return getByColumn(column,null);
	}
	
	private BoAttribute getByColumn(Column column,String id){
		BoAttribute attr = new BoAttribute();
		attr.setId( id==null? UniqueIdUtil.getSuid():id);
		attr.setName(column.getFieldName());
		attr.setFieldName(column.getFieldName());
		attr.setIsPk(false);
		attr.setIsRequired(column.getIsNull()?BoConstants.REQUIRED_NO:BoConstants.REQUIRED_YES);
		String columnType=column.getColumnType();
		if(columnType.equals(Column.COLUMN_TYPE_VARCHAR)){
			attr.setAttrLength(column.getCharLen());
		}
		else if(columnType.equals(Column.COLUMN_TYPE_NUMBER)){
			attr.setAttrLength(column.getIntLen());
			attr.setDecimalLen(column.getDecimalLen());
		}
		
		attr.setFormat("");
		updateDateFormat(column.getFcolumnType(),attr);
		
		attr.setDataType(columnType);
		attr.setDesc(column.getComment());
		return attr;
	
	}

	private void updateDateFormat(String fcolumnType, BoAttribute attr) {
		if("datetime".equals(fcolumnType) || "timestamp".equals(fcolumnType) ){
			attr.setFormat("yyyy-MM-dd HH:mm:ss");
		}
		if("time".equals(fcolumnType)){
			attr.setFormat("HH:mm:ss");
		}
		if("date".equals(fcolumnType)){
			attr.setFormat("yyyy-MM-dd");
		}
	}

	@Override
	public List<BoEnt> getByDefId(String defId) {
		List<BoEnt> list = baseMapper.getByDefId(defId);
		for (BoEnt boEnt : list) {
			List<BoAttribute> attributes = boAttributeManager.getByBoEnt(boEnt);
			boEnt.setBoAttrList(attributes);
		}
		return list;
	}

	@Override
	public List<BoEnt> getByTableName(String tableName) {
		return baseMapper.getByTableName(tableName);
	}

	@Override
	public List<BoEnt> getBySubEntId(String entId) {
		List<BoEnt> list = baseMapper.getBySubEntId(entId);
		if(BeanUtils.isNotEmpty(list)){
			for (BoEnt boEnt : list) {
				List<BoAttribute> attributes = boAttributeManager.getByBoEnt(boEnt);
				boEnt.setBoAttrList(attributes);
			}
		}
		return list;
	}

	@Override
	public void deleteByDefId(String defId) {
		baseMapper.deleteByDefId(defId);
	}
}
