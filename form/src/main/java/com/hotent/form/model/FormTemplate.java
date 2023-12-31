package com.hotent.form.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 表单模版
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
@ApiModel("表单模版 ")
@TableName("form_template")
public class FormTemplate extends BaseModel<FormTemplate>{
	private static final long serialVersionUID = 1L;
	//模版类型
	//主表模版
	public static final String MAIN_TABLE = "main";
	public static final String MOBILE_MAIN = "mobileMain";
	//子表模版
	public static final String SUB_TABLE = "subTable";
	public static final String Mobile_SUB = "mobileSub";
	//宏模版
	public static final String MACRO = "macro";
	//列表模版
	public static final String LIST = "list";
	//明细模版
	public static final String DETAIL = "detail";
	// vue 表单设计模板
	public static final String FORM_DESIGN = "formDesign";
	/**
	 *	表管理模板 
	 */
	public static final String TABLE_MANAGE="tableManage";
	/**
	 *	表管理模板 
	 */
	public static final String DATA_TEMPLATE="dataTemplate";
	/**
	 *	div容器模板 
	 */
	public static final String DIV_CONTAINER="divContainer";
	/**
	 * 查询数据模块
	 */
	public static final String QUERY_DATA_TEMPLATE="queryDataTemplate";
	
	@ApiModelProperty(name="templateId", notes="模板id")
	@TableId("template_id_")
	protected String templateId; /*模板id*/

	@ApiModelProperty(name="templateName", notes="模板名称")
	@TableField("template_name_")
	protected String templateName; /*模板名称*/

	@ApiModelProperty(name="templateType", notes="模板类型")
	@TableField("template_type_")
	protected String templateType; /*模板类型*/

	@ApiModelProperty(name="macrotemplateAlias", notes="模板所向")
	@TableField("macrotemplate_alias_")
	protected String macrotemplateAlias; /*模板所向*/

	@ApiModelProperty(name="html", notes="模板内容")
	@TableField("html_")
	protected String html; /*模板内容*/

	@ApiModelProperty(name="templateDesc", notes="模板描述")
	@TableField("template_desc_")
	protected String templateDesc; /*模板描述*/

	@ApiModelProperty(name="canedit", notes="是否可以编辑")
	@TableField("canedit_")
	protected Integer canedit; /*是否可以编辑*/

	@ApiModelProperty(name="alias", notes="别名")
	@TableField("alias_")
	protected String alias; /*别名*/

	@ApiModelProperty(name="source", notes="模板来源")
	@TableField("source_")
	protected String source; /*模板来源*/

    @ApiModelProperty(name="rev", notes="关联锁")
	@TableField("rev_")
    protected Integer rev=1; /*关联锁*/

	@ApiModelProperty(name = "isDefault", notes = "0：非默认模板 1：默认模板")
	@TableField("is_default_")
	protected Integer isDefault;

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public void setId(String templateId) {
		this.templateId = templateId.toString();
	}
	
	public String getId() {
		return templateId.toString();
	}
	
	public void setTemplateId(String templateId) 
	{
		this.templateId = templateId;
	}
	/**
	 * 返回 模板id
	 * @return
	 */
	public String getTemplateId() 
	{
		return this.templateId;
	}
	
	public void setTemplateName(String templateName) 
	{
		this.templateName = templateName;
	}
	/**
	 * 返回 模板名称
	 * @return
	 */
	public String getTemplateName() 
	{
		return this.templateName;
	}
	public void setTemplateType(String templateType) 
	{
		this.templateType = templateType;
	}
	/**
	 * 返回 模板类型
	 * @return
	 */
	public String getTemplateType() 
	{
		return this.templateType;
	}
	public void setMacrotemplateAlias(String macrotemplateAlias) 
	{
		this.macrotemplateAlias = macrotemplateAlias;
	}
	/**
	 * 返回 模板所向
	 * @return
	 */
	public String getMacrotemplateAlias() 
	{
		return this.macrotemplateAlias;
	}
	public void setHtml(String html) 
	{
		this.html = html;
	}
	/**
	 * 返回 模板内容
	 * @return
	 */
	public String getHtml() 
	{
		return this.html;
	}
	public void setTemplateDesc(String templateDesc) 
	{
		this.templateDesc = templateDesc;
	}
	/**
	 * 返回 模板描述
	 * @return
	 */
	public String getTemplateDesc() 
	{
		return this.templateDesc;
	}
	public void setCanedit(Integer canedit) 
	{
		this.canedit = canedit;
	}
	/**
	 * 返回 是否可以编辑
	 * @return
	 */
	public Integer getCanedit() 
	{
		return this.canedit;
	}
	public void setAlias(String alias) 
	{
		this.alias = alias;
	}
	/**
	 * 返回 别名
	 * @return
	 */
	public String getAlias() 
	{
		return this.alias;
	}
	/**
	 * 返回 模板来源
	 * @return
	 */
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }
}