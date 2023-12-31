package com.hotent.form.model;
import org.apache.commons.lang.builder.ToStringBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import java.time.LocalDateTime;


 /**
 * 数据报表草稿数据
 * <pre> 
 * 描述：数据报表草稿数据 实体对象
 * 构建组：x7
 * 作者:pangq
 * 邮箱:pangq@jee-soft.cn
 * 日期:2020-06-13 13:45:06
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
 @TableName("form_data_template_draft")
 @ApiModel(value = "FormDataTemplateDraft",description = "数据报表草稿数据") 
public class FormDataTemplateDraft extends BaseModel<FormDataTemplateDraft>{

	private static final long serialVersionUID = 1L;
	@XmlTransient
	@TableId("ID_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "title")
	@TableField("TITLE_")
	@ApiModelProperty(value="标题")
	protected String title; 
	
	@XmlAttribute(name = "tempAlias")
	@TableField("TEMP_ALIAS_")
	@ApiModelProperty(value="数据报表别名")
	protected String tempAlias; 
	
	@XmlAttribute(name = "dataJson")
	@TableField("DATA_JSON_")
	@ApiModelProperty(value="报表数据json")
	protected String dataJson; 
	
	@XmlAttribute(name = "createBy")
	@TableField("CREATE_BY_")
	@ApiModelProperty(value="创建人")
	protected String createBy; 
	
	@XmlAttribute(name = "createTime")
	@TableField("CREATE_TIME_")
	@ApiModelProperty(value="创建时间")
	protected LocalDateTime createTime; 
	
	@XmlAttribute(name = "tenantId")
	@TableField("TENANT_ID_")
	@ApiModelProperty(value = "租户id")
	protected String tenantId;
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 返回 标题
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}
	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}
	
	/**
	 * 返回 报表数据json
	 * @return
	 */
	public String getDataJson() {
		return this.dataJson;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	/**
	 * 返回 创建人
	 * @return
	 */
	public String getCreateBy() {
		return this.createBy;
	}
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 返回 创建时间
	 * @return
	 */
	public LocalDateTime getCreateTime() {
		return this.createTime;
	}
	public String getTempAlias() {
		return tempAlias;
	}

	public void setTempAlias(String tempAlias) {
		this.tempAlias = tempAlias;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("title", this.title) 
		.append("tempAlias", this.tempAlias) 
		.append("dataJson", this.dataJson) 
		.append("createBy", this.createBy) 
		.append("createTime", this.createTime) 
		.toString();
	}
}