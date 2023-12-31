package com.hotent.portal.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 首页工具 实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description="首页工具 实体对象")
@TableName("portal_sys_tools")
public class SysIndexTools extends AutoFillModel<SysIndexTools>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**首页工具权限*/
	public static final String INDEX_TOOLS="indexTools";
	public static final short NOT_STATISTICS = 0;
	public static final short SERVICE = 1;
	public static final short SLEF_QUERY = 2;
	
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID_")
	protected String id; 
	
	@ApiModelProperty(name="name", notes="名称")
	@TableField("NAME_")
	protected String name; 
	
	@ApiModelProperty(name="icon", notes="图标")
	@TableField("ICON_")
	protected String icon; 
	
	@ApiModelProperty(name="url", notes="链接")
	@TableField("URL_")
	protected String url; 
	
	@ApiModelProperty(name="type", notes="类型")
	@TableField("TYPE_")
	protected String type; 

	@ApiModelProperty(name="countMode", notes="统计模式(0:不统计  1:service  2:自定义查询)", allowableValues="0,1,2")
	@TableField("COUNT_MODE")
	protected Short countMode; 
	
	@ApiModelProperty(name="counting", notes="统计算法")
	@TableField("COUNTING")
	protected String counting; 
	
	@ApiModelProperty(name="countParam", notes="统计参数")
	@TableField("COUNT_PARAM")
	protected String countParam; 
	
	@ApiModelProperty(name="fontStyle", notes="字体样式")
	@TableField("FONT_STYLE")
	protected String fontStyle;

	@ApiModelProperty(name="numberStyle", notes="统计数字样式")
	@TableField("NUMBER_STYLE")
	protected String numberStyle;
	
	@ApiModelProperty(name="statistics", notes="统计")
	@TableField(exist = false)
	protected Long statistics;
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 设置名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 设置图标
	 * @param icon 图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * 返回 图标
	 * @return
	 */
	public String getIcon() {
		return this.icon;
	}
	
	/**
	 * 设置链接
	 * @param url 链接
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 返回 链接
	 * @return
	 */
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * 设置类型
	 * @param type 类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回 类型
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * 设置统计模式(0--不统计，1--service，2-自定义查询)
	 * @param countMode 统计模式(0--不统计，1--service，2-自定义查询)
	 */
	public void setCountMode(Short countMode) {
		this.countMode = countMode;
	}
	
	/**
	 * 返回 统计模式(0--不统计，1--service，2-自定义查询)
	 * @return
	 */
	public Short getCountMode() {
		return this.countMode;
	}
	
	/**
	 * 设置统计算法
	 * @param counting 统计算法
	 */
	public void setCounting(String counting) {
		this.counting = counting;
	}
	
	/**
	 * 返回 统计算法
	 * @return
	 */
	public String getCounting() {
		return this.counting;
	}

	/**
	 * 返回 统计参数
	 * @return
	 */
	public String getCountParam() {
		return countParam;
	}
	
	/**
	 * 设置统计参数
	 * @param countParam 统计参数
	 */
	public void setCountParam(String countParam) {
		this.countParam = countParam;
	}
	
	/**
	 * 返回统计
	 * @return
	 */
	public Long getStatistics() {
		return statistics;
	}
	
	/**
	 * 设置统计
	 * @param statistics 统计
	 */
	public void setStatistics(Long statistics) {
		this.statistics = statistics;
	}

	/**
	 * 返回字体样式
	 * @return
	 */
	public String getFontStyle() {
		return fontStyle;
	}
	
	/**
	 * 设置字体样式
	 * @param fontStyle 字体样式
	 */
	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
	
	/**
	 * 返回统计数字样式
	 * @return
	 */
	public String getNumberStyle() {
		return numberStyle;
	}
	
	/**
	 * 设置统计数字样式
	 * @param numberStyle 统计数字样式
	 */
	public void setNumberStyle(String numberStyle) {
		this.numberStyle = numberStyle;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("icon", this.icon) 
		.append("url", this.url) 
		.append("type", this.type) 
		.append("countMode", this.countMode) 
		.append("counting", this.counting) 
		.append("countParam", this.countParam) 
		.toString();
	}
}