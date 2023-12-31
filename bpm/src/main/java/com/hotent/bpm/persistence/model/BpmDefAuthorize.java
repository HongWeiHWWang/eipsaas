package com.hotent.bpm.persistence.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;


/**
 * 对象功能:流程授权主表明细 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 09:00:53
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("bpm_def_authorize")
public class BpmDefAuthorize extends  BaseModel<BpmDefAuthorize>{
	
	private static final long serialVersionUID = 7260326446957952925L;

	// id
	@TableId("id_")
	protected String id="";
	
	//流程授权说明
	@TableField("authorize_desc_")
	protected String authorizeDesc="";
	
	//创建人姓名
	@TableField("creator_")
    protected String creator="";
	
	//创建人姓名
	@TableField("create_by_")
    protected String createBy="";
    
    //授权流程类型(流程/流程分类)
	@TableField("multi_ple")
    protected String multiple="";
	
	@TableField("create_time_")
	protected LocalDateTime createTime = LocalDateTime.now();
	
	//流程授权类型  : start,management,task,instance
	@TableField(exist=false)
	protected String authorizeTypes="";
	//授权类型列表
	@TableField(exist=false)
	protected List<BpmDefAuthorizeType> bpmDefAuthorizeTypeList = new ArrayList<BpmDefAuthorizeType>();

	//授权对象列表
	@TableField(exist=false)
	protected List<BpmDefUser> bpmDefUserList = new ArrayList<BpmDefUser>();
	
	//授权流程列表
	@TableField(exist=false)
	protected List<BpmDefAct> bpmDefActList = new ArrayList<BpmDefAct>();
	
	//授权对象名称(仅用于查询)
	@TableField(exist=false)
	protected String ownerName;
	
	//授权流程名称(仅用于查询)
	@TableField(exist=false)
	protected String defName;
	
	
	/*
	 * [{type:"everyone"},{"type":"user",id:"",name:""}]
	 * 
  	*/
	@TableField(exist=false)
	protected String ownerNameJson="[]";
	
	//授权流程名称(仅用于存放授权流程的JSON数据)
	
  /* 
   * 	  [{ "defId":"10000018130014",
   *         "defKey":"zchz",
   *         "defName":"周程汇总",
   *         "right":{"edit":"N","del":"N","start":"N","set":"N"}
   *       },
   *       {"defId":"10000017980009",
   *        "defKey":"csjdsz",
   *        "defName":"测试节点设置",
   *        "right":{"edit":"N","del":"N","start":"N","set":"N"}
   *       },
   *       {"defId":"10000017860008",
   *        "defKey":"gxzlc",
   *        "defName":"共享子流程",
   *        "right":{"edit":"N","del":"N","start":"N","set":"N"}}]
  */	
	@TableField(exist=false)
	protected String defNameJson="[]";
	
	//授权流程分类名称(仅用于存放授权流程分类的JSON数据)
	 /* 
	   * 	  [{ "id":"11701",
	   *         "name":"测试",
	   *         "right":{"edit":"N","del":"N","start":"N","set":"N"}
	   *       },
	   *       {"id":"1212603",
	   *        "name":"海外订单",
	   *        "right":{"edit":"N","del":"N","start":"N","set":"N"}
	   *       },
	   *       {"id":"1212611",
	   *        "name":"启动测试",
	   *        "right":{"edit":"N","del":"N","start":"N","set":"N"}}]
	  */
	@TableField(exist=false)
	protected String defAllNameJson="[]";

//	@JSONField(serialize=true)
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

//	@JSONField(serialize=true)
	public String getAuthorizeTypes()
	{
		return authorizeTypes;
	}

	public void setAuthorizeTypes(String authorizeTypes)
	{              
		this.authorizeTypes = authorizeTypes;
	}

//	@JSONField(serialize=true)
	public String getAuthorizeDesc()
	{
		return authorizeDesc;
	}

	public void setAuthorizeDesc(String authorizeDesc)
	{
		this.authorizeDesc = authorizeDesc;
	}

	public List<BpmDefAuthorizeType> getBpmDefAuthorizeTypeList()
	{
		return bpmDefAuthorizeTypeList;
	}

	public void setBpmDefAuthorizeTypeList(List<BpmDefAuthorizeType> bpmDefAuthorizeTypeList){
		this.bpmDefAuthorizeTypeList = bpmDefAuthorizeTypeList;
		if(StringUtil.isEmpty(this.authorizeTypes)){
			this.authorizeTypes = "";
			if(this.bpmDefAuthorizeTypeList.size()>0){
				ObjectNode json = JsonUtil.getMapper().createObjectNode();
				for (BpmDefAuthorizeType bpmDefAuthorizeType : bpmDefAuthorizeTypeList){
					json.put(bpmDefAuthorizeType.getAuthorizeType(),true);
				}
				this.authorizeTypes =json.toString();
			}
		}
		this.authorizeTypes=this.authorizeTypes;
	}

	public List<BpmDefUser> getBpmDefUserList()
	{
		return bpmDefUserList;
	}

	public void setBpmDefUserList(List<BpmDefUser> bpmDefUserList)
	{
		this.bpmDefUserList = bpmDefUserList;
	}

	public List<BpmDefAct> getBpmDefActList()
	{
		return bpmDefActList;
	}

	public void setBpmDefActList(List<BpmDefAct> bpmDefActList)
	{
		this.bpmDefActList = bpmDefActList;
	}

	public String getOwnerName()
	{
		return ownerName;
	}

	public void setOwnerName(String ownerName)
	{
		this.ownerName = ownerName;
	}

	public String getDefName()
	{
		return defName;
	}

	public void setDefName(String defName)
	{
		this.defName = defName;
	}

	public String getOwnerNameJson()
	{
		return ownerNameJson;
	}

	public void setOwnerNameJson(String ownerNameJson)
	{
		this.ownerNameJson = ownerNameJson;
	}

	public String getDefNameJson()
	{
		return defNameJson;
	}

	public void setDefNameJson(String defNameJson)
	{
		this.defNameJson = defNameJson;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getDefAllNameJson() {
		return defAllNameJson;
	}

	public void setDefAllNameJson(String defAllNameJson) {
		this.defAllNameJson = defAllNameJson;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
}