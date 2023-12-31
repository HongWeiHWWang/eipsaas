package com.hotent.portal.model;



import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 布局管理 Model对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(description="布局管理 Model对象")
@TableName("portal_sys_layout_manage")
public class SysIndexLayoutManage extends BaseModel<SysIndexLayoutManage> {
	private static final long serialVersionUID = 1L;
	/**
	 * 管理端系统默认布局id
	 */
	public static final String MNG_DEFAULT_ID= "0";
	/**
	 * 手机端系统默认布局id
	 */
	public static final String MOBILE_DEFAULT_ID = "1";
	/**
	 * 应用端系统默认布局id
	 */
	public static final String APPLICATION_DEFAULT_ID= "2";
	

	/**布局管理权限*/
	public static  String INDEX_MANAGE ="indexManage";
	
	// 管理端
	public static final short TYPE_MNG = 0;
	//手机端
	public static final short TYPE_MOBILE = 1;
	//应用端
	public static final short TYPE_APPLICATION = 2;
	

	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID")
	protected String id;
 
	@ApiModelProperty(name="name", notes="布局名称")
	@TableField("NAME")
	protected String name;
 
	@ApiModelProperty(name="memo", notes="布局描述")
	@TableField("MEMO")
	protected String memo;

	@ApiModelProperty(name="orgId", notes="组织ID")
	@TableField("ORG_ID")
	protected String orgId;
	
	@ApiModelProperty(name="templateHtml", notes="模版内容")
	@TableField("TEMPLATE_HTML")
	protected String templateHtml;

	@ApiModelProperty(name="designHtml", notes="设计模版")
	@TableField("DESIGN_HTML")
	protected String designHtml;

	// 系统默认
	public static final short SET_DEF = 1;
	// 非系统默认
	public static final short CANCEL_DEF = 0;
	
	@ApiModelProperty(name="isDef", notes="是否是系统默认")
	@TableField("IS_DEF")
	protected Short isDef=CANCEL_DEF;

	@ApiModelProperty(name="orgName", notes="组织名称")
	@TableField("org_name")
	protected String orgName;
	// 
	@ApiModelProperty(name="layoutType", notes="布局类型(0:管理端  1:手机端 2:应用端)", allowableValues="0,1")
	@TableField("LAYOUT_TYPE")
	protected Short layoutType;
	/**
	 * 是否启用  
	 * 1=启用
	 * 0=停用
	 */
	@ApiModelProperty(name="enable", notes="是否启用（1=启用，0=停用）", allowableValues="0,1")
	@TableField("ENABLE")
	protected Short enable=0;
	/**
	 * 是否共享给子部门
	 * 1=是
	 * 0=否
	 */
	@ApiModelProperty(name="enable", notes="是否共享给子部门（1=是，0=否）", allowableValues="0,1")
	@TableField("SHARE_TO_SUB")
	protected Short shareToSub=0;
	
	
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
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
	
	/**
	 * 设置布局名称
	 * @param name 布局名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 布局名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 设置布局描述
	 * @param memo
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 返回 布局描述
	 * @return
	 */
	public String getMemo() {
		return this.memo;
	}
	
	/**
	 * 设置组织ID
	 * @param orgId 组织ID
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 返回 组织ID
	 * @return
	 */
	public String getOrgId() {
		return this.orgId;
	}
	
	/**
	 * 模版内容
	 * @param templateHtml 模版内容
	 */
	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	/**
	 * 返回 模版内容
	 * @return
	 */
	public String getTemplateHtml() {
		return this.templateHtml;
	}
	
	/**
	 * 设置设计模版 
	 * @param designHtml 设计模版 
	 */
	public void setDesignHtml(String designHtml) {
		this.designHtml = designHtml;
	}

	/**
	 * 返回 设计模版 
	 * @return
	 */
	public String getDesignHtml() {
		return this.designHtml;
	}
	
	/**
	 * 设置 是否是默认
	 * @param isDef
	 */
	public void setIsDef(Short isDef) {
		this.isDef = isDef;
	}

	/**
	 * 返回 是否是默认
	 * @return
	 */
	public Short getIsDef() {
		return this.isDef;
	}
	
	/**
	 * 返回组织名称
	 * @return
	 */
	public String getOrgName() {
		return orgName;
	}
	
	/**
	 * 设置组织名称
	 * @param orgName 组织名称
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public Short getLayoutType() {
		return layoutType;
	}
	
	public void setLayoutType(Short layoutType) {
		this.layoutType = layoutType;
	}

	public Short getEnable() {
		return enable;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}

	public Short getShareToSub() {
		return shareToSub;
	}

	public void setShareToSub(Short shareToSub) {
		this.shareToSub = shareToSub;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof SysIndexLayoutManage)) {
			return false;
		}
		SysIndexLayoutManage rhs = (SysIndexLayoutManage) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.name, rhs.name).append(this.memo, rhs.memo)
				.append(this.orgId, rhs.orgId)
				.append(this.templateHtml, rhs.templateHtml)
				.append(this.designHtml, rhs.designHtml)
				.append(this.isDef, rhs.isDef)
				.append(this.layoutType, rhs.layoutType)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.name).append(this.memo).append(this.orgId)
				.append(this.templateHtml).append(this.designHtml)
				.append(this.isDef)
				.append(this.layoutType).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("name", this.name).append("memo", this.memo)
				.append("orgId", this.orgId)
				.append("templateHtml", this.templateHtml)
				.append("designHtml", this.designHtml)
				.append("isDef", this.isDef)
				.append("layoutType", this.layoutType).toString();
	}

}