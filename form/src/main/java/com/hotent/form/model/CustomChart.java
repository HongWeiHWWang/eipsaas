package com.hotent.form.model;

import com.hotent.base.entity.AutoFillModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

 /**
 * 自定义对图表
 * <pre> 
 * 描述：自定义对图表 实体对象
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2019-07-24 10:46:14
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
 @ApiModel("自定义对图表")
 @TableName("form_custom_chart")
public class CustomChart extends AutoFillModel<CustomChart> {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty("名字")
	@TableField("name_")
	protected String name; 
	
	@ApiModelProperty("别名")
	@TableField("alias_")
	protected String alias; 
	
	@ApiModelProperty("图表类型：1-折线图，2-饼图，3-柱状图，4-雷达图，5-树形图")
	@TableField("style_")
	protected Short style; 
	
	@ApiModelProperty("表名称")
	@TableField("obj_name_")
	protected String objName; 
	
	@ApiModelProperty("显示字段")
	@TableField("displayfield_")
	protected String displayfield; 
	
	@ApiModelProperty("条件字段的json")
	@TableField("conditionfield_")
	protected String conditionfield; 
	
	@ApiModelProperty("返回字段json")
	@TableField("resultfield_")
	protected String resultfield; 
	
	@ApiModelProperty("排序字段")
	@TableField("sortfield_")
	protected String sortfield; 
	
	@ApiModelProperty("数据源的别名")
	@TableField("dsalias_")
	protected String dsalias; 
	
	@ApiModelProperty("是否数据库表1:表，2.自定义sql")
	@TableField("is_table_")
	protected Short isTable; 
	
	@ApiModelProperty("自定义SQL")
	@TableField("diy_sql_")
	protected String diySql; 
	
	@ApiModelProperty("图标宽度")
	@TableField("width_")
	protected Integer width; 
	
	@ApiModelProperty("图表宽度")
	@TableField("height_")
	protected Integer height; 
	
	@ApiModelProperty("x轴字段")
	@TableField("xaxis_field_")
	protected String xaxisField; 
	
	@ApiModelProperty("y轴单位")
	@TableField("yaxis_unit_")
	protected String yaxisUnit; 
	
	@ApiModelProperty("图表相关配置")
	@TableField("conf_")
	protected String conf;
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 名字
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * 返回 别名
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}
	
	public void setStyle(Short style) {
		this.style = style;
	}
	
	/**
	 * 返回 图表类型：1-折线图，2-饼图，3-柱状图，4-雷达图，5-树形图
	 * @return
	 */
	public Short getStyle() {
		return this.style;
	}
	
	public void setObjName(String objName) {
		this.objName = objName;
	}
	
	/**
	 * 返回 表名称
	 * @return
	 */
	public String getObjName() {
		return this.objName;
	}
	
	public void setDisplayfield(String displayfield) {
		this.displayfield = displayfield;
	}
	
	/**
	 * 返回 显示字段
	 * @return
	 */
	public String getDisplayfield() {
		return this.displayfield;
	}
	
	public void setConditionfield(String conditionfield) {
		this.conditionfield = conditionfield;
	}
	
	/**
	 * 返回 条件字段的json
	 * @return
	 */
	public String getConditionfield() {
		return this.conditionfield;
	}
	
	public void setResultfield(String resultfield) {
		this.resultfield = resultfield;
	}
	
	/**
	 * 返回 返回字段json
	 * @return
	 */
	public String getResultfield() {
		return this.resultfield;
	}
	
	public void setSortfield(String sortfield) {
		this.sortfield = sortfield;
	}
	
	/**
	 * 返回 排序字段
	 * @return
	 */
	public String getSortfield() {
		return this.sortfield;
	}
	
	public void setDsalias(String dsalias) {
		this.dsalias = dsalias;
	}
	
	/**
	 * 返回 数据源的别名
	 * @return
	 */
	public String getDsalias() {
		return this.dsalias;
	}
	
	public void setIsTable(Short isTable) {
		this.isTable = isTable;
	}
	
	/**
	 * 返回 是否数据库表1:表，2.自定义sql
	 * @return
	 */
	public Short getIsTable() {
		return this.isTable;
	}
	
	public void setDiySql(String diySql) {
		this.diySql = diySql;
	}
	
	/**
	 * 返回 自定义SQL
	 * @return
	 */
	public String getDiySql() {
		return this.diySql;
	}
	
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	/**
	 * 返回 图标宽度
	 * @return
	 */
	public Integer getWidth() {
		return this.width;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	/**
	 * 返回 图表宽度
	 * @return
	 */
	public Integer getHeight() {
		return this.height;
	}
	
	public void setXaxisField(String xaxisField) {
		this.xaxisField = xaxisField;
	}
	
	/**
	 * 返回 x轴字段
	 * @return
	 */
	public String getXaxisField() {
		return this.xaxisField;
	}
	
	public void setYaxisUnit(String yaxisUnit) {
		this.yaxisUnit = yaxisUnit;
	}
	
	/**
	 * 返回 y轴单位
	 * @return
	 */
	public String getYaxisUnit() {
		return this.yaxisUnit;
	}
	
	public void setConf(String conf) {
		this.conf = conf;
	}
	
	/**
	 * 返回 图表相关配置
	 * @return
	 */
	public String getConf() {
		return this.conf;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("alias", this.alias) 
		.append("style", this.style) 
		.append("objName", this.objName) 
		.append("displayfield", this.displayfield) 
		.append("conditionfield", this.conditionfield) 
		.append("resultfield", this.resultfield) 
		.append("sortfield", this.sortfield) 
		.append("dsalias", this.dsalias) 
		.append("isTable", this.isTable) 
		.append("diySql", this.diySql) 
		.append("width", this.width) 
		.append("height", this.height) 
		.append("xaxisField", this.xaxisField) 
		.append("yaxisUnit", this.yaxisUnit) 
		.append("conf", this.conf) 
		.toString();
	}
}