package com.hotent.uc.model;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.util.StringUtil;

import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;


/**
 * UC_TENANT_MANAGE
 * <pre> 
 * 描述：租户管理实体对象
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:56:07
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("UC_TENANT_MANAGE")
@ApiModel(value = "TenantManage",description = "租户管理") 
public class TenantManage extends AutoFillModel<TenantManage>{

	private static final long serialVersionUID = 1L;
	public final static String STATUS_ENABLE = "enable";
	public final static String STATUS_DISABLED = "disabled";
	public final static String STATUS_DRAFT = "draft";

	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="主键")
	protected String id; 

	@XmlAttribute(name = "typeId")
	@TableField("TYPE_ID_")
	@ApiModelProperty(value="租户类型id")
	protected String typeId; 

	@XmlAttribute(name = "name")
	@TableField("NAME_")
	@ApiModelProperty(value="租户名称")
	protected String name; 

	@XmlAttribute(name = "code")
	@TableField("CODE_")
	@ApiModelProperty(value="租户别名（编码）")
	protected String code; 

	@XmlAttribute(name = "status")
	@TableField("STATUS_")
	@ApiModelProperty(value="状态：启用（enable）、禁用（disabled）")
	protected String status; 

	@XmlAttribute(name = "shorthand")
	@TableField("SHORTHAND_")
	@ApiModelProperty(value="租户简称")
	protected String shorthand; 

	@XmlAttribute(name = "domain")
	@TableField("DOMAIN_")
	@ApiModelProperty(value="域名地址")
	protected String domain; 

	@XmlAttribute(name = "manageLogo")
	@TableField("MANAGE_LOGO_")
	@ApiModelProperty(value="管理端logo附件id")
	protected String manageLogo; 

	@XmlAttribute(name = "frontLogo")
	@TableField("FRONT_LOGO_")
	@ApiModelProperty(value="用户端logo附件id")
	protected String frontLogo; 

	@XmlAttribute(name = "ico")
	@TableField("ICO_")
	@ApiModelProperty(value="地址栏ICO图标附件id")
	protected String ico; 

	@XmlAttribute(name = "desc")
	@TableField("DESC_")
	@ApiModelProperty(value="描述")
	protected String desc; 

	@TableField(exist=false)
	@ApiModelProperty(name="typeName",notes="租户类型名称")
	protected String typeName="";
	
	@ApiModelProperty(value = "创建人ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_by_", fill=FieldFill.INSERT)
	private String createBy;

	@ApiModelProperty(value = "创建人组织ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_org_id_", fill=FieldFill.INSERT)
	private String createOrgId;
	
	@ApiModelProperty(value = "创建时间", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_time_", fill=FieldFill.INSERT)
	private LocalDateTime createTime;
	
	@ApiModelProperty(value = "更新人ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="update_by_", fill=FieldFill.UPDATE)
	private String updateBy;

	@ApiModelProperty(value = "更新时间", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="update_time_", fill=FieldFill.UPDATE)
	private LocalDateTime updateTime;
	
	@TableLogic
	@TableField("IS_DELE_")
	@ApiModelProperty(name="isDelete",notes="是否已删除 0：未删除 1：已删除（新增、更新数据时不需要传入）")
	protected String isDelete = "0";


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

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * 返回 租户类型id
	 * @return
	 */
	public String getTypeId() {
		return this.typeId;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 租户名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setCode(String code) {
		Assert.isTrue(StringUtil.isNotEmpty(code), "租户别名不能为空");
		try {
			Pattern regex = Pattern.compile("^[a-z]{0,10}\\d{0,3}$");
			Matcher regexMatcher = regex.matcher(code);
			if(!regexMatcher.find()) {
				throw new BaseException();
			}
			this.code = code;
		} catch (Exception ex) {
			throw new BaseException(ResponseErrorEnums.ILLEGAL_ARGUMENT, "租户别名只能包含小写字母和数字，且必须以小写字母开头，小写字母最多10个，数字最多3个.");
		}
	}

	/**
	 * 返回 租户别名（编码）
	 * @return
	 */
	public String getCode() {
		return this.code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setShorthand(String shorthand) {
		this.shorthand = shorthand;
	}

	/**
	 * 返回 租户简称
	 * @return
	 */
	public String getShorthand() {
		return this.shorthand;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * 返回 域名地址
	 * @return
	 */
	public String getDomain() {
		return this.domain;
	}

	public void setManageLogo(String manageLogo) {
		this.manageLogo = manageLogo;
	}

	/**
	 * 返回 管理端logo附件id
	 * @return
	 */
	public String getManageLogo() {
		return this.manageLogo;
	}

	public void setFrontLogo(String frontLogo) {
		this.frontLogo = frontLogo;
	}

	/**
	 * 返回 用户端logo附件id
	 * @return
	 */
	public String getFrontLogo() {
		return this.frontLogo;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	/**
	 * 返回 地址栏ICO图标附件id
	 * @return
	 */
	public String getIco() {
		return this.ico;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 返回 描述
	 * @return
	 */
	public String getDesc() {
		return this.desc;
	}



	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.id) 
				.append("typeId", this.typeId) 
				.append("name", this.name) 
				.append("code", this.code) 
				.append("shorthand", this.shorthand) 
				.append("domain", this.domain) 
				.append("manageLogo", this.manageLogo) 
				.append("frontLogo", this.frontLogo) 
				.append("ico", this.ico) 
				.append("desc", this.desc) 
				.append("createTime", this.createTime) 
				.append("createBy", this.createBy) 
				.append("updateBy", this.updateBy) 
				.append("updateTime", this.updateTime) 
				.append("createOrgId", this.createOrgId) 
				.toString();
	}
}