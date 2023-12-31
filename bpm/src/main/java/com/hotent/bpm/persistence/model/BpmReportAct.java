package com.hotent.bpm.persistence.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

@TableName("bpm_report_act")
public class BpmReportAct extends BaseModel<BpmReportAct>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3177931239826935082L;

	//主键
	@TableId("id_")
	protected String id;
	
	//报表主表id
	@TableField("report_id_")
	protected String reportId;
	
	//流程id
	@TableField("bpm_def_id_")
	protected String bpmDefId;
	
	//报表名称(流程名称)
	@TableField("report_name_")
	protected String reportName;
	
	//组织维度
	@TableField("dimension_id_")
	protected String dimensionId;
	
	//统计级别
	@TableField("grade_")
	protected String grade;
	
	//统计参数
	@TableField("params_")
	protected String params;
	
	//报表x轴数据(存入数据库)
	@TableField("x_axis_")
	protected String xAxis;
	
	//报表y轴数据(存入数据库)
	@TableField("y_axis_")
	protected String yAxis;
	
	//授权视图内容
	@TableField("right_content_")
	protected String rightContent;
	
	//默认显示视图
	@TableField("is_default_")
	protected String isDefault;
	
	//设置参数
	@TableField("porp_")
	protected String porp;
	
	//传入前台x轴数据
	@TableField(exist=false)
	protected String[] xData;
	
	//传入前台y轴数据
	@TableField(exist=false)
	protected String[] yData;
	

	public BpmReportAct() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getPorp() {
		return porp;
	}

	public void setPorp(String porp) {
		this.porp = porp;
	}

	public String getBpmDefId() {
		return bpmDefId;
	}

	public void setBpmDefId(String bpmDefId) {
		this.bpmDefId = bpmDefId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getDimensionId() {
		return dimensionId;
	}

	public void setDimensionId(String dimensionId) {
		this.dimensionId = dimensionId;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getxAxis() {
		return xAxis;
	}

	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}

	public String getyAxis() {
		return yAxis;
	}

	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}

	public String getRightContent() {
		return rightContent;
	}

	public void setRightContent(String rightContent) {
		this.rightContent = rightContent;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String[] getxData() {
		return xData;
	}

	public void setxData(String[] xData) {
		this.xData = xData;
	}

	public String[] getyData() {
		return yData;
	}

	public void setyData(String[] yData) {
		this.yData = yData;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
}
