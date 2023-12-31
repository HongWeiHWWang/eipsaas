package com.hotent.uc.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.uc.api.constant.GroupStructEnum;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IdentityType;


/**
 * 
 * <pre> 
 * 描述：组织岗位  实体对象
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class OrgPost extends UcBaseModel implements IGroup {

	private static final long serialVersionUID = 1233764588726416781L;
	/**
	* id_
	*/
	@ApiModelProperty(name="id",notes="岗位id")
	protected String id; 
	
	/**
	* org_id_
	*/
	@ApiModelProperty(name="orgId",notes="所属组织id")
	protected String orgId; 
	
	/**
	* job_id_
	*/
	@ApiModelProperty(name="relDefId",notes="所属职务id")
	protected String relDefId; 
	
	/**
	* name
	*/
	@ApiModelProperty(name="name",notes="岗位名称")
	protected String name; 
	
	/**
	* code_
	*/
	@ApiModelProperty(name="code",notes="岗位编码")
	protected String code; 
	
	@ApiModelProperty(name="orgName",notes="所属组织名称")
	protected String orgName; 
	@ApiModelProperty(name="jobName",notes="所属职务名称")
	protected String jobName; 
	@ApiModelProperty(name="demName",notes="所属维度名称")
	protected String demName; 
	
	/**
	 * 主岗位
	 */
	@ApiModelProperty(name="isCharge",notes="是否主岗位")
	protected Integer isCharge=0;

    protected String pathName;//岗位组织全路径

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	
	/**
	 * 返回 id_
	 * @return
	 */
	public String getOrgName() {
		return this.orgName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	/**
	 * 返回 id_
	 * @return
	 */
	public String getJobName() {
		return this.jobName;
	}
	
	public void setId(String id) {
		this.id = id;
	}


	public String getDemName() {
		return demName;
	}



	public void setDemName(String demName) {
		this.demName = demName;
	}



	/**
	 * 返回 id_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * 返回 org_id_
	 * @return
	 */
	public String getOrgId() {
		return this.orgId;
	}
	
	public void setRelDefId(String relDefId) {
		this.relDefId = relDefId;
	}
	
	/**
	 * 返回 rel_def_id_
	 * @return
	 */
	public String getRelDefId() {
		return this.relDefId;
	}
	
	
	
	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getName() {
		return name;
	}



	/**
	 * 返回  是否主岗位
	 * @return
	 */
	public Integer getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(Integer isCharge) {
		this.isCharge = isCharge;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("orgId", this.orgId) 
		.append("relDefId", this.relDefId) 
		.append("name", this.name) 
		.append("code", this.code) 
		.append("isCharge", this.isCharge)
		.append("updateTime",this.updateTime)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

	public String getGroupId() {
		return this.id;
	}


	public Long getOrderNo() {
		return Long.valueOf(0);
	}



	@Override
	public String getIdentityType() {
		return IdentityType.GROUP;
	}



	@Override
	public String getGroupCode() {
		return this.code;
	}



	@Override
	public String getGroupType() {
		return GroupTypeConstant.POSITION.key();
	}



	@Override
	public GroupStructEnum getStruct() {
		return null;
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

}
