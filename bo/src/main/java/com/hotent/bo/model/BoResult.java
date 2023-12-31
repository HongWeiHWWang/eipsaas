package com.hotent.bo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 数据处理结果
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public class BoResult{
	/**
	 * 主键
	 */
	private String pk="";

	private String parentId="0";
	/**
	 * 增加，修改，删除
	 * add,upd,del
	 */
	private String action="";
	/**
	 * BO定义别名
	 */
	private String boAlias="";
	/**
	 * 对应的bo实体。
	 */
	private BoEnt boEnt=null;

	/**
	 * 数据修改明细
	 */
	private String modifyDetail = "";
	
	/**
	 * 表单数据
	 */
	private String data;

	public String getAction() {
		return action;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}

	public void setAction(String action) {
		this.action = action;
	}
   
	 @JsonIgnore
	public String getEntName() {
		return  boEnt.getName();
	}
    @JsonIgnore
	public String getTableName() {
		return  boEnt.getTableName();
	}

	public BoEnt getBoEnt() {
		return boEnt;
	}
	public void setBoEnt(BoEnt boEnt) {
		this.boEnt = boEnt;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getBoAlias() {
		return boAlias;
	}
	public void setBoAlias(String boAlias) {
		this.boAlias = boAlias;
	}
	public String getModifyDetail() {
		return modifyDetail;
	}
	public void setModifyDetail(String modifyDetail) {
		this.modifyDetail = modifyDetail;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
