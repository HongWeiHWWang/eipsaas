package com.hotent.uc.params.echarts;

import io.swagger.annotations.ApiModelProperty;

 /**
 * 
 * <pre> 
 * 描述：echarts 简单关系网络 节点类
 * 构建组：htuc
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2018-01-19 08:51:05
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class ChartNode{
	
	/**
	* 节点id
	*/
	@ApiModelProperty(name="id",notes="节点id")
	protected String id; 
	
	/**
	* 等级
	*/
	@ApiModelProperty(name="category",notes="等级")
	protected int category; 
	
	/**
	 * 名称
	 */
	@ApiModelProperty(name="name",notes="名称")
	protected String name;
	
	/**
	* 值
	*/
	@ApiModelProperty(name="value",notes="值")
	protected String value; 
	
	/**
	* 标签
	*/
	@ApiModelProperty(name="label",notes="标签")
	protected String label;
	
	/**
	* 图形大小
	*/
	@ApiModelProperty(name="symbolSize",notes="图形大小")
	protected int symbolSize; 
	
	public ChartNode(){
		
	}
	
	public ChartNode(String id,int category,String name,String value,String label,int symbolSize){
		this.id = id;
		this.category = category;
		this.name = name;
		this.value = value;
		this.label = label;
		this.symbolSize = symbolSize;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(int symbolSize) {
		this.symbolSize = symbolSize;
	}

	@Override
	public String toString() {
		return "ChartNode [id=" + id + ", category=" + category
				+ ", name=" + name + ", value=" + value + ", label=" + label
				+ ", symbolSize=" + symbolSize + "]";
	}
	
}