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
 * 我的布局 Model对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(description="我的布局 Model对象")
@TableName("portal_sys_my_layout")
public class SysIndexMyLayout extends BaseModel<SysIndexMyLayout>{
	
	/**
	 *  用于去设置到布局管理实体的memo属性中，目的是在切换布局的时候区分是否为我的布局
	 */
	public static final String MY_LAYOUT="MY_LAYOUT";
	public static final String MY_LAYOUT_NAME="我的首页布局";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID")
	protected String  id;
	
	@ApiModelProperty(name="userId", notes="用户ID")
	@TableField("USER_ID")
	protected String  userId;
	
	@ApiModelProperty(name="templateHtml", notes="模版内容")
	@TableField("TEMPLATE_HTML")
	protected String  templateHtml;
	
	@ApiModelProperty(name="designHtml", notes="设计模版")
	@TableField("DESIGN_HTML")
	protected String  designHtml;
	
	@ApiModelProperty(name="name", notes="布局名称")
	@TableField("NAME_")
	protected String  name;
	
	@ApiModelProperty(name="valid", notes="是否有效")
	@TableField("VALID_")
	protected Integer  valid;
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id){
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	/**
	 * 设置用户ID
	 * @param userId 用户ID
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	/**
	 * 返回 用户ID
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	/**
	 * 设置模版内容
	 * @param templateHtml 模版内容
	 */
	public void setTemplateHtml(String templateHtml){
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
	public void setDesignHtml(String designHtml){
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
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof SysIndexMyLayout)) 
		{
			return false;
		}
		SysIndexMyLayout rhs = (SysIndexMyLayout) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.userId, rhs.userId)
		.append(this.templateHtml, rhs.templateHtml)
		.append(this.designHtml, rhs.designHtml)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.userId) 
		.append(this.templateHtml) 
		.append(this.designHtml) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("userId", this.userId) 
		.append("templateHtml", this.templateHtml) 
		.append("designHtml", this.designHtml) 
		.toString();
	}
   
  

}