package com.hotent.bo.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.TextNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.bo.exception.BoBaseException;
import com.hotent.bo.util.BoUtil;

/**
 * bo数据默认实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public class BoData{
	/**
	 * 主表数据。
	 */
	private Map<String, Object> row = new HashMap<String, Object>();

	/**
	 * 子表数据。
	 */
	private Map<String, List<BoData>> subMap = new HashMap<String, List<BoData>>();

	/**
	 * 子表初始化数据。
	 * <pre>
	 *  键为：子表名称
	 *  值为：子表初始化数据。
	 * </pre>
	 */
	private Map<String,Map<String, Object>> initDataMap=new HashMap<String, Map<String,Object>>();

	private BoEnt boEnt=null;

	/**
	 * 业务对象定义。
	 */
	private BoDef boDef = null;
	
	private String boDefAlias = null;


	public void setBoEnt(BoEnt ent){
		this.boEnt=ent;
	}

	public BoEnt getBoEnt(){
		return this.boEnt;
	}

	/**
	 * 设置主表值。
	 * @param key	键
	 * @param val	值
	 */
	public void set(String key,Object val){
		row.put(key, val);
	}

	/**
	 * 根据键获取主表字段值。
	 * @param key
	 * @return
	 */
	public Object getByKey(String key){
		return row.get(key);
	}

	/**
	 * 判断数据是否存在。
	 * @param key
	 * @return
	 */
	public boolean containKey(String key){
		return row.containsKey(key);
	}
	@JsonIgnore
	public boolean isAdd(){
		Map<String, Object> row = this.getData();
		String pk = boEnt.getPkKey().toLowerCase();

		if(row.containsKey(pk) && StringUtil.isNotEmpty(String.valueOf(row.get(pk)))){
			return false;
		}
		return true;
	}

	/**
	 * 使用BaseAttribute对数据进行处理。
	 * @return
	 * @throws ParseException
	 */
	public Object getValByKey(String key) throws ParseException {
		BoAttribute attr = boEnt.getAttribute(key);
		Object obj=row.get(key);
		if(obj==null) return obj;
		String dataType = "";
		if(BeanUtils.isNotEmpty(attr)){
			dataType= attr.getDataType();
		}else{//当获取的字段可能是主键时
			dataType= boEnt.getPkType();
		}
		if(BoAttribute.COLUMN_TYPE_VARCHAR.equals(dataType)  || BoAttribute.COLUMN_TYPE_CLOB.equals(dataType)){
			return obj.toString();
		}
		else if(BoAttribute.COLUMN_TYPE_NUMBER.equals(dataType)){
			String val=obj.toString();
			if(StringUtil.isEmpty(val)) return null;
			if(attr.getDecimalLen()==0){
				if(attr.getAttrLength()<=10){
					return Integer.parseInt(obj.toString());
				}
				else{
					return Long.parseLong(obj.toString());
				}
			}
			else{
				return Double.parseDouble(obj.toString());
			}
		}
		else if(BoAttribute.COLUMN_TYPE_DATE.equals(dataType)){
			String val=obj.toString();
			if(StringUtil.isEmpty(val)) return null;

			String format = attr.getFormat();
			if(StringUtil.isEmpty(format)){
				format=StringPool.DATE_FORMAT_DATETIME;
			}
			return DateFormatUtil.parseDate(obj.toString(), format);
		}
		return obj.toString();
	}


	/**
	 * 根据键值获取主对象的值。
	 * @param key
	 * @return
	 */
	public String getString(String key){
		Object obj= row.get(key);
		if(obj!=null){
			if(obj instanceof TextNode) {
				return ((TextNode)obj).asText();
			}
			if(obj instanceof String) {
				return obj.toString();
			}
			else {
				throw new BoBaseException(String.format("BoData中key为%s的属性不是字符串类型", key)); 
			}
		}
		return "";
	}


	/**
	 * 获取一行数据。
	 * @return
	 */
	public Map<String, Object> getData(){
		return row;
	}

	/**
	 * 设置一行数据。
	 * @param row
	 */
	public void setData(Map<String, Object> row){
		this. row=row;
	}


	/**
	 * 根据键删除主表数据。
	 * @param key
	 */
	public void removeByKey(String key){
		row.remove(key);
	}

	/**
	 * 添加子表行数据。
	 * @param key
	 * @param data
	 */
	public void addSubRow(String key,BoData data){
		if(subMap.containsKey(key)){
			List<BoData> list=subMap.get(key);
			list.add(data);
		}
		else{
			List<BoData> list=new ArrayList<BoData>();
			list.add(data);
			subMap.put(key, list);
		}

	}

	/**
	 * 设置子表数据。
	 * @param key
	 * @param list
	 */
	public void setSubList(String key,List<BoData> list){
		subMap.put(key, list);
	}

	/**
	 * 根据子表key值获取数据。
	 * @param subKey
	 * @return
	 */
	public  List<BoData> getSubByKey(String subKey){
		return subMap.get(subKey);
	}


	/**
	 * 根据子表key删除数据。
	 * @param key
	 */
	public void removeSub(String key){
		subMap.remove(key);
	}

	/**
	 * 获取子实例map数据。
	 * @return
	 */
	public Map<String, List<BoData>> getSubMap(){
		return subMap;
	}

	/**
	 * 获取初始化对象Map。
	 * @return
	 */
	public Map<String, Map<String, Object>> getInitDataMap() {
		return initDataMap;
	}

	/**
	 * 设置自对象初始化值。
	 * @param initDataMap
	 */
	public void setInitDataMap(Map<String, Map<String, Object>> initDataMap) {
		this.initDataMap = initDataMap;
	}

	/**
	 * 添加子表初始化行数据。
	 * @param key
	 * @param initRow
	 */
	public void addInitDataMap(String key,Map<String, Object> initRow){
		this.initDataMap.put(key, initRow);
	}

	/**
	 * 单个设置初始化数据。
	 * @param key			表名
	 * @param fieldName		字段名
	 * @param val			表单
	 */
	public void setInitData(String key,String fieldName,Object val){
		if(this.initDataMap.containsKey(key)){
			Map<String, Object> row=this.initDataMap.get(key);
			row.put(fieldName, val);
		}
		else{
			Map<String, Object> row=new HashMap<String, Object>();
			row.put(fieldName,val);
			this.initDataMap.put(key, row);
		}
	}

	/**
	 * 获取当前的BO对象。
	 * @return  
	 */
	@JsonIgnore
	public BoDef getBoDef() {
		return boDef;
	}

	public void setBoDef(BoDef boDef) {
		this.boDef = boDef;
	}

	public String getBoDefAlias() {
		return boDefAlias;
	}

	public void setBoDefAlias(String boDefAlias) {
		this.boDefAlias = boDefAlias;
	}

	@Override
	public String toString() {
		try {
			return BoUtil.toJSON(this,true).toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BoBaseException(e.getMessage());
		}
	}
}
