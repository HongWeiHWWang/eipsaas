package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 信息盒
 * 
 * @author zxh
 * 
 */
@ApiModel(value="首页栏目消息盒子对象")
public class InfoboxVo {
	
	@ApiModelProperty(name="id",notes="id")
	public String id;
	
	@ApiModelProperty(name="fontStyle",notes="fontStyle")
	public String fontStyle;
	
	@ApiModelProperty(name="numberStyle",notes="numberStyle")
	public String numberStyle;
	
	public static final String COLOR_BLUE ="blue";
	public static final String COLOR_BLUE2 ="blue2";
	public static final String COLOR_BLUE3 ="blue3";
	public static final String COLOR_RED ="red";
	public static final String COLOR_BROWN ="brown";
	public static final String COLOR_LIGHT_BROWN ="light-brown";
	public static final String COLOR_WOOD ="wood";
	public static final String COLOR_ORANGE ="orange";
	public static final String COLOR_ORANGE2 ="orange2";
	public static final String COLOR_GREEN ="green";
	public static final String COLOR_GREEN2 ="green2";
	public static final String COLOR_BLACK ="black";
	public static final String COLOR_DARK ="dark";
	public static final String COLOR_PINK ="pink";
	
	
	//展示类型-图标
	public static final int TYPE_ICON = 0;
	//展示类型-进度
	public static final int TYPE_PROGRESS = 1;
	//展示类型-图表
	public static final int TYPE_CHART= 2;
	//stat 方式
	public static final int STAT_TYPE_STAT = 1;
	//徽章方式
	public static final int STAT_TYPE_BADGE= 2;
	
	//上升
	public static final int STAT_STATUS_UP = 1;
	//下降
	public static final int STAT_STATUS_DOWN = 2;
	
	
	@ApiModelProperty(name="icon",notes="图标")
	protected String icon;
	
	@ApiModelProperty(name="color",notes="信息盒颜色")
	protected String color;

	@ApiModelProperty(name="dataText",notes="数据文本（包含数字，文本）")
	protected String dataText;

	@ApiModelProperty(name="dataContent",notes="文本内容")
	protected String dataContent;

	@ApiModelProperty(name="type",notes="信息盒类型，普通类型、图表类型 easy-pie-chart")
	protected Integer type = TYPE_ICON;
	
	
	@ApiModelProperty(name="data",notes="信息盒数据")
	protected String data;

	@ApiModelProperty(name="statType",notes="状态信息的")
	protected Integer statType; 
	
	@ApiModelProperty(name="statData",notes="状态信息数据")
	protected String statData;

	@ApiModelProperty(name="statStatus",notes="状态信息的状态（上升：1，下降：2）")
	protected Integer statStatus;
	
	@ApiModelProperty(name="url",notes="更多地址的url")
	protected String url;
	
	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getNumberStyle() {
		return numberStyle;
	}

	public void setNumberStyle(String numberStyle) {
		this.numberStyle = numberStyle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDataText() {
		return dataText;
	}

	public void setDataText(String dataText) {
		this.dataText = dataText;
	}

	public String getDataContent() {
		return dataContent;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getStatType() {
		return statType;
	}

	public void setStatType(Integer statType) {
		this.statType = statType;
	}

	public String getStatData() {
		return statData;
	}

	public void setStatData(String statData) {
		this.statData = statData;
	}

	public Integer getStatStatus() {
		return statStatus;
	}

	public void setStatStatus(Integer statStatus) {
		this.statStatus = statStatus;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}
