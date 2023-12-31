package com.hotent.form.vo;

public class CustomQueryControllerVo {
	protected String dsalias;//数据源别名
	protected String isTable;//1:表 2：视图
	protected String objName;//表名
	protected String id;
	
	
	public String getDsalias() {
		return dsalias;
	}
	public void setDsalias(String dsalias) {
		this.dsalias = dsalias;
	}
	public String getIsTable() {
		return isTable;
	}
	public void setIsTable(String isTable) {
		this.isTable = isTable;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	
}
