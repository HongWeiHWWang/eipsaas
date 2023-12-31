package com.hotent.uc.api.impl.model;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.entity.BaseModel;
import com.hotent.uc.api.constant.GroupStructEnum;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IdentityType;

import io.swagger.annotations.ApiModelProperty;


/**
* 类 {@code OrgJob} 职务 实体对象
* @company 广州宏天软件股份有限公司
* @author heyifan
* @email heyf@jee-soft.cn
* @date 2018年7月5日
*/
public class OrgJob extends BaseModel<OrgJob> implements IGroup{

	private static final long serialVersionUID = -6236742378037779413L;
	
	/**
	* 职务id
	*/
	@ApiModelProperty(name="id",notes="职务id")
	protected String id; 
	
	/**
	* 职务名称
	*/
	@ApiModelProperty(name="name",notes="职务名称")
	protected String name; 
	
	/**
	* 职务编码
	*/
	@ApiModelProperty(name="code",notes="职务编码")
	protected String code; 
	
	/**
	* 职务级别
	*/
	@ApiModelProperty(name="postLevel",notes="职务级别")
	protected String postLevel; 
	
	/**
	* 描述
	*/
	@ApiModelProperty(name="description",notes="描述")
	protected String description;

    /**
     * 岗位定义状态：0：有效1：无效
     */
	@ApiModelProperty(name="jobDefinitionStatus",notes="岗位定义状态：0：有效1：无效")
	protected Integer jobDefinitionStatus;

    /**
     * 岗位定义类型：1-行政岗2-业务岗
     */
	@ApiModelProperty(name="jobDefinitionType",notes="岗位定义类型：1-行政岗2-业务岗")
	protected Integer jobDefinitionType;

    /**
     * 层级分类编码
     */
	@ApiModelProperty(name="z9121cj",notes="层级分类编码")
	protected String z9121cj;

    /**
     * 层级分类
     */
	@ApiModelProperty(name="z9121cjt",notes="层级分类")
	protected String z9121cjt;

    /**
     * 职能编码
     */
	@ApiModelProperty(name="z9121zn",notes="职能编码")
	protected String z9121zn;

    /**
     * 职能
     */
	@ApiModelProperty(name="z9121znt",notes="职能")
	protected String z9121znt;

    /**
     * SAP岗位定义编码
     */
	@ApiModelProperty(name="sapjobdefId",notes="SAP岗位定义编码")
	protected String sapjobdefId;

    /**
     * 序列编码
     */
	@ApiModelProperty(name="z9121xl",notes="序列编码")
	protected String z9121xl;

    /**
     * 序列
     */
	@ApiModelProperty(name="z9121xlt",notes="序列")
	protected String z9121xlt; 
	
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
	
	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}

	public String getPostLevel() {
		return this.postLevel;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("code", this.code) 
		.append("postLevel", this.postLevel) 
		.append("description", this.description)
		.toString();
	}

	@Override
	public String getIdentityType() {
		return IdentityType.GROUP;
	}

	@Override
	public String getGroupId() {
		return this.id;
	}

	@Override
	public String getGroupCode() {
		return this.code;
	}

	@Override
	public Long getOrderNo() {
		return null;
	}

	@Override
	public String getGroupType() {
		return GroupTypeConstant.JOB.key();
	}

	@Override
	public GroupStructEnum getStruct() {
		return GroupStructEnum.PLAIN;
	}

	@Override
	public String getParentId() {
		return null;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public Map<String, Object> getParams() {
		return null;
	}

	public Integer getJobDefinitionStatus() {
		return jobDefinitionStatus;
	}

	public void setJobDefinitionStatus(Integer jobDefinitionStatus) {
		this.jobDefinitionStatus = jobDefinitionStatus;
	}

	public Integer getJobDefinitionType() {
		return jobDefinitionType;
	}

	public void setJobDefinitionType(Integer jobDefinitionType) {
		this.jobDefinitionType = jobDefinitionType;
	}

	public String getZ9121cj() {
		return z9121cj;
	}

	public void setZ9121cj(String z9121cj) {
		this.z9121cj = z9121cj;
	}

	public String getZ9121cjt() {
		return z9121cjt;
	}

	public void setZ9121cjt(String z9121cjt) {
		this.z9121cjt = z9121cjt;
	}

	public String getZ9121zn() {
		return z9121zn;
	}

	public void setZ9121zn(String z9121zn) {
		this.z9121zn = z9121zn;
	}

	public String getZ9121znt() {
		return z9121znt;
	}

	public void setZ9121znt(String z9121znt) {
		this.z9121znt = z9121znt;
	}

	public String getSapjobdefId() {
		return sapjobdefId;
	}

	public void setSapjobdefId(String sapjobdefId) {
		this.sapjobdefId = sapjobdefId;
	}

	public String getZ9121xl() {
		return z9121xl;
	}

	public void setZ9121xl(String z9121xl) {
		this.z9121xl = z9121xl;
	}

	public String getZ9121xlt() {
		return z9121xlt;
	}

	public void setZ9121xlt(String z9121xlt) {
		this.z9121xlt = z9121xlt;
	}

}
