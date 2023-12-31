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

/**
 * 视图管理
 * 
 * <pre>
 *  
 * 描述：视图管理 实体对象
 * 构建组：x7
 * 作者:pangq
 * 邮箱:pangq@jee-soft.cn
 * 日期:2020-04-30 17:01:49
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("form_view_manage")
@ApiModel(value = "ViewManage", description = "视图管理")
public class ViewManage extends BaseModel<ViewManage> {
	//0未生成视图
	public static final Integer NotGenerated = 0;
	//1已生成视图
	public static final Integer Generated = 1;

	private static final long serialVersionUID = 1L;
	@XmlTransient
	@TableId("ID_")
	@ApiModelProperty(value = "主键")
	protected String id;

	@XmlAttribute(name = "desc")
	@TableField("DESC_")
	@ApiModelProperty(value = "描述")
	protected String desc;

	@XmlAttribute(name = "viewName")
	@TableField("VIEW_NAME_")
	@ApiModelProperty(value = "视图名称")
	protected String viewName;

	@XmlAttribute(name = "sql")
	@TableField("SQL_")
	@ApiModelProperty(value = "视图sql")
	protected String sql;

	@XmlAttribute(name = "dsAlias")
	@TableField("DS_ALIAS_")
	@ApiModelProperty(value = "数据源别名")
	protected String dsAlias;

	@XmlAttribute(name = "status")
	@TableField("STATUS_")
	@ApiModelProperty(value = "状态：0未生成视图，1已生成视图")
	protected Integer status = NotGenerated;

	@XmlAttribute(name = "tenantId")
	@TableField("TENANT_ID_")
	@ApiModelProperty(value = "租户id")
	protected String tenantId;

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 返回 描述
	 * 
	 * @return
	 */
	public String getDesc() {
		return this.desc;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * 返回 视图名称
	 * 
	 * @return
	 */
	public String getViewName() {
		return this.viewName;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * 返回 视图sql
	 * 
	 * @return
	 */
	public String getSql() {
		return this.sql;
	}

	public void setDsAlias(String dsAlias) {
		this.dsAlias = dsAlias;
	}

	/**
	 * 返回 数据源别名
	 * 
	 * @return
	 */
	public String getDsAlias() {
		return this.dsAlias;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 返回 状态：0未生成视图，1已生成视图
	 * 
	 * @return
	 */
	public Integer getStatus() {
		return this.status;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * 返回 租户id
	 * 
	 * @return
	 */
	public String getTenantId() {
		return this.tenantId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("desc", this.desc)
				.append("viewName", this.viewName).append("sql", this.sql).append("dsAlias", this.dsAlias)
				.append("status", this.status).append("tenantId", this.tenantId).toString();
	}
}