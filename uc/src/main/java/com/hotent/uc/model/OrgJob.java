package com.hotent.uc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.uc.api.constant.GroupStructEnum;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IdentityType;


/**
* 
* <pre> 
* 描述：职务  实体对象
* 构建组：x5-bpmx-platform
* 作者:ray
* 邮箱:zhangyg@jee-soft.cn
* 日期:2016-06-29 18:00:43
* 版权：广州宏天软件有限公司
* </pre>
*/
@TableName("UC_ORG_JOB")
@ApiModel(description="职务")
public class OrgJob extends UcBaseModel<OrgJob> implements IGroup {

	private static final long serialVersionUID = -6236742378037779413L;
	
	/**
	* id_
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="职务id")
	protected String id; 
	
	/**
	* 名称
	*/
	@TableField("NAME_")
	@ApiModelProperty(name="name",notes="职务名称")
	protected String name; 
	
	/**
	* 编码
	*/
	@TableField("CODE_")
	@ApiModelProperty(name="code",notes="职务编码")
	protected String code; 
	
	/**
	* 职务级别
	*/
	@TableField("JOB_LEVEL_")
	@ApiModelProperty(name="postLevel",notes="职务级别")
	protected String postLevel; 
	
	/**
	* 描述
	*/
	@TableField("DESCRIPTION_")
	@ApiModelProperty(name="description",notes="描述")
	protected String description; 
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 id_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
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
	
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 返回 编码
	 * @return
	 */
	public String getCode() {
		return this.code;
	}
	
	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}
	
	/**
	 * 返回 职务级别
	 * @return
	 */
	public String getPostLevel() {
		return this.postLevel;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 返回 描述
	 * @return
	 */
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
		.append("isDelete",this.isDelete)
		.append("version",this.version)
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
