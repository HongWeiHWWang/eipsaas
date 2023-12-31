package com.hotent.base.query;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 构建查询条件
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月4日
 */
@ApiModel(description="查询条件")
public class QueryField implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="property", notes="实体类属性")
	private String property;
	@ApiModelProperty(name="operation", notes="比较符", example="EQUAL")
	private QueryOP operation = QueryOP.EQUAL;
	@ApiModelProperty(name="value", notes="比较值")
	private Object value;
	@ApiModelProperty(name="relation", notes="同一个分组内的多个条件之间的组合关系，默认为and", example="AND")
	private FieldRelation relation = FieldRelation.AND;
	@ApiModelProperty(name="group", notes="查询条件分组，默认分组为main，多个分组默认按照and的关系组合在一起", example="main")
	private String group = "main";

	public QueryField(){}

	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 */
	public QueryField(String property, Object value){
		this(property, value, QueryOP.EQUAL, FieldRelation.AND);
	}

	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 * @param operation 查询符号
	 */
	public QueryField(String property, Object value, QueryOP operation){
		this(property, value, operation, FieldRelation.AND);
	}

	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 * @param relation 与其他查询条件的组合关系
	 */
	public QueryField(String property, Object value, FieldRelation relation){
		this(property, value, QueryOP.EQUAL, relation);
	}

	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 * @param relation 与其他查询条件的组合关系
	 */
	public QueryField(String property, Object value, QueryOP operation, FieldRelation relation){
		this(property, value, operation, relation, null);
	}

	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 * @param operation 查询符号
	 * @param relation 与其他查询条件的组合关系
	 */
	public QueryField(String property, Object value, QueryOP operation, FieldRelation relation, String group){
		this.property = property;
		this.value = value;
		this.operation = operation;
		this.relation = relation;
		if(!StringUtils.isEmpty(group)) {
			this.group = group;
		}
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public QueryOP getOperation() {
		return operation;
	}
	public void setOperation(QueryOP operation) {
		this.operation = operation;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public Boolean isGroup() {
		return false;
	}
	public FieldRelation getRelation() {
		return relation;
	}
	public void setRelation(FieldRelation relation) {
		this.relation = relation;
	}
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
