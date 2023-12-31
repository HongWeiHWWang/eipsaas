package com.hotent.portal.model;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 系统首页布局实体对象 
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(description="系统首页布局实体对象 ")
@TableName("portal_sys_layout")
public class SysIndexLayout extends BaseModel<SysIndexLayout>{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID")
	protected Long id; 

	@ApiModelProperty(name="name", notes="名称")
	@TableField("NAME")
	protected String name; 

	@ApiModelProperty(name="memo", notes="描述")
	@TableField("MEMO")
	protected String memo; 
	
	@ApiModelProperty(name="templateHtml", notes="模板内容")
	@TableField("TEMPLATE_HTML")
	protected String templateHtml; 
	
	@ApiModelProperty(name="sn", notes="序号")
	@TableField("SN")
	protected Integer sn; 
	
	/**
	 * 返回主键
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回描述
	 * @return
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置描述
	 * @param memo 描述
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 返回模板内容
	 * @return
	 */
	public String getTemplateHtml() {
		return templateHtml;
	}

	/**
	 * 设置HTML模板内容
	 * @param templateHtml 模版内容
	 */
	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	/**
	 * 返回序号
	 * @return
	 */
	public Integer getSn() {
		return sn;
	}

	/**
	 * 设置序号 
	 * @param sn 序号
	 */
	public void setSn(Integer sn) {
		this.sn = sn;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("memo", this.memo) 
		.append("templateHtml", this.templateHtml) 
		.append("sn", this.sn) 
		.toString();
	}

}