package com.hotent.bpm.persistence.model;


public class BpmReportActVo extends BpmReportAct{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3177931239826935082L;

	//统计报表名称
	private String name;
	//统计维度：org（部门维度），flow（流程或流程分类维度）
	private String dimension;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	
}
