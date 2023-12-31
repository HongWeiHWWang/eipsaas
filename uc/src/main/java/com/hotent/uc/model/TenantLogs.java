package com.hotent.uc.model;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


 /**
 * UC_TENANT_LOGS
 * <pre> 
 * 描述：租户管理操作日志 实体对象
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:53:55
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("UC_TENANT_LOGS")
@ApiModel(value = "TenantLogs",description = "租户管理操作日志") 
public class TenantLogs extends BaseModel<TenantLogs>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "tenantId")
	@TableField("TENANT_ID_")
	@ApiModelProperty(value="租户id")
	protected String tenantId; 
	
	@XmlAttribute(name = "type")
	@TableField("TYPE_")
	@ApiModelProperty(value="操作类型")
	protected String type; 
	
	@XmlAttribute(name = "name")
	@TableField("NAME_")
	@ApiModelProperty(value="接口名称（中文）")
	protected String name; 
	
	@XmlAttribute(name = "reqUrl")
	@TableField("REQ_URL_")
	@ApiModelProperty(value="接口地址")
	protected String reqUrl; 
	
	@XmlAttribute(name = "ip")
	@TableField("IP_")
	@ApiModelProperty(value="操作客户端ip")
	protected String ip; 
	
	@XmlAttribute(name = "params")
	@TableField("PARAMS_")
	@ApiModelProperty(value="接口参数")
	protected String params; 
	
	@XmlAttribute(name = "opeContent")
	@TableField("OPE_CONTENT_")
	@ApiModelProperty(value="操作内容")
	protected String opeContent; 
	
	@XmlAttribute(name = "result")
	@TableField("RESULT_")
	@ApiModelProperty(value="操作结果（1:成功，0:失败）")
	protected Short result; 
	
	@XmlAttribute(name = "reason")
	@TableField("REASON_")
	@ApiModelProperty(value="失败原因：异常信息")
	protected String reason; 
	
	@XmlAttribute(name = "createTime")
	@TableField("CREATE_TIME_")
	@ApiModelProperty(value="创建时间")
	protected LocalDateTime createTime; 
	
	@XmlAttribute(name = "createBy")
	@TableField("CREATE_BY_")
	@ApiModelProperty(value="创建人")
	protected String createBy;
	
	@XmlAttribute(name = "createOrgId")
	@TableField("CREATE_ORG_ID_")
	@ApiModelProperty(value="创建人所属部门id")
	protected String createOrgId;
	
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
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	/**
	 * 返回 租户id
	 * @return
	 */
	public String getTenantId() {
		return this.tenantId;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回 操作类型
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 接口名称（中文）
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
	
	/**
	 * 返回 接口地址
	 * @return
	 */
	public String getReqUrl() {
		return this.reqUrl;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * 返回 操作客户端ip
	 * @return
	 */
	public String getIp() {
		return this.ip;
	}
	
	public void setParams(String params) {
		this.params = params;
	}
	
	/**
	 * 返回 接口参数
	 * @return
	 */
	public String getParams() {
		return this.params;
	}
	
	public void setOpeContent(String opeContent) {
		this.opeContent = opeContent;
	}
	
	/**
	 * 返回 操作内容
	 * @return
	 */
	public String getOpeContent() {
		return this.opeContent;
	}
	
	public void setResult(Short result) {
		this.result = result;
	}
	
	/**
	 * 返回 操作结果（1:成功，0:失败）
	 * @return
	 */
	public Short getResult() {
		return this.result;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/**
	 * 返回 失败原因：异常信息
	 * @return
	 */
	public String getReason() {
		return this.reason;
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

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("tenantId", this.tenantId) 
		.append("type", this.type) 
		.append("name", this.name) 
		.append("reqUrl", this.reqUrl) 
		.append("ip", this.ip) 
		.append("params", this.params) 
		.append("opeContent", this.opeContent) 
		.append("result", this.result) 
		.append("reason", this.reason) 
		.append("createTime", this.createTime) 
		.append("createBy", this.createBy) 
		.append("createOrgId", this.createOrgId) 
		.toString();
	}
}