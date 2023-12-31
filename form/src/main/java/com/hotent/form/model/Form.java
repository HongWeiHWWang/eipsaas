package com.hotent.form.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

/**
 * 表单
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月13日
 */
@ApiModel("流程任务表单 entity对象")
@TableName("form_definition")
public class Form extends AutoFillModel<Form> {
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人ID", hidden=true, accessMode=ApiModelProperty.AccessMode.READ_ONLY)
    @TableField(value="create_by_", fill=FieldFill.INSERT, select=true)
    private String createBy;

    @ApiModelProperty(value = "更新人ID", hidden=true, accessMode= ApiModelProperty.AccessMode.READ_ONLY)
    @TableField(value="update_by_", fill= FieldFill.UPDATE, select=true)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden=true, accessMode= ApiModelProperty.AccessMode.READ_ONLY)
    @TableField(value="update_time_", fill=FieldFill.UPDATE, select=true)
    private LocalDateTime updateTime;
	
	/**
	 * 草稿状态
	 */
	public static final String STATUS_DRAFT="draft";
	/**
	 * 发布状态
	 */
	public static final String STATUS_DEPLOY="deploy";

	/**
	 * 栅格布局
	 */
	public static final String GRID_LAYOUT = "grid";

	/**
	 * tab布局
	 */
	public static final String TAB_LAYOUT = "tab";
	
	/**
	 * 手风琴布局
	 */
	public static final String ACCORDION_LAYOUT = "accordion";

	/**
	 * 子表布局
	 */
	public static final String SUBTABLE_LAYOUT = "subtable";
	
	/**
	 * 孙表Table布局
	 */
	public static final String SUNTABLE_LAYOUT = "suntable";
	
	/**
	 * 孙表Div布局
	 */
	public static final String SUNDIV_LAYOUT = "sunDiv";
	
	/**
	 * div布局
	 */
	public static final String SUBDIV_LAYOUT = "subDiv";

	/**
	 * handsontable布局
	 */
	public static final String HOT_TABLE = "hottable";


	/**
	 * 表单ID
	 */
	@ApiModelProperty(name="id", notes="表单id")
	@TableId("id_")
	protected String id;

	/**
	 * 表单元数据定义ID
	 */
	@ApiModelProperty(name="defId", notes="表单元数据定义ID")
	@TableField("def_id_")
	protected String defId;

	/**
	 * 表单key
	 */
	@ApiModelProperty(name="formKey", notes="表单key")
	@TableField("form_key_")
	protected String formKey;

	/**
	 * 表单名称
	 */
	@ApiModelProperty(name="name", notes="表单名称")
	@TableField("name_")
	protected String name;

	/**
	 * 表单描述
	 */
	@ApiModelProperty(name="desc", notes="表单描述")
	@TableField("desc_")
	protected String desc;

	/**
	 * 表单设计（HTML代码）
	 */
	@ApiModelProperty(name="formHtml", notes="表单设计（HTML代码）")
	@TableField("form_html_")
	protected String formHtml;

	/**
	 * 表单状态
	 * <pre>
	 * draft=草稿；deploy=发布
	 * </pre>
	 */
	@ApiModelProperty(name="status", notes="表单状态(draft:草稿  deploy:发布)", allowableValues="draft,deploy")
	@TableField("status_")
	protected String status = Form.STATUS_DRAFT;

	/**
	 * 是否主版本
	 */
	@ApiModelProperty(name="isMain", notes="是否主版本(Y:是  N:否)", allowableValues="Y,N")
	@TableField("is_main_")
	protected char isMain = 'Y';

	/**
	 * 表单版本号
	 */
	@ApiModelProperty(name="version", notes="表单版本号")
	@TableField("version_")
	protected Integer version = 1;

	/**
	 * 分类iD
	 */
	@ApiModelProperty(name="typeId", notes="分类iD")
	@TableField("type_id_")
	protected String typeId;

	/**
	 * 分类Name
	 */
	@ApiModelProperty(name="typeName", notes="分类名称")
	@TableField("type_name_")
	protected String typeName;

	/**
	 * tab 的标题
	 */
	@ApiModelProperty(name="formTabTitle", notes="表单标题")
	@TableField("form_tab_title_")
	protected String formTabTitle;//

	/**
	 * 表单类型
	 * <pre>
	 * PC和mobile
	 * </pre>
	 */
	@ApiModelProperty(name="formType", notes="表单类型 (PC:pc端  mobile:手机端)", allowableValues="pc,mobile")
	@TableField("form_type_")
	protected String formType;

    /**
     * 自定义JS脚本
     */
    @ApiModelProperty(name="diyJs", notes="自定义JS脚本")
    @TableField("diy_js_")
    protected String diyJs;
    
    /**
	 * 表单版本数
	 */
	@ApiModelProperty(name="versionCount", notes="表单版本数")
	@TableField(exist=false)
	protected Integer versionCount = 0;
    
    /**
	 * 是否已经生成业务数据模板
	 * <pre>
	 * 0=没有生成；1=已经生成
	 * </pre>
	 */
	@ApiModelProperty(name="busDataTemplateCount", notes="是否已经生成业务数据模板 (0:没有生成  1:已经生成)", allowableValues="0,1")
	@TableField(exist=false)
	protected short busDataTemplateCount = 0;

	@ApiModelProperty(name="expand", notes="扩展字段")
	@TableField("expand_")
	protected String expand;
	
	@ApiModelProperty(name="isPrint", notes="是否为打印模板（Y：是，N：否）")
	@TableField("IS_PRINT_")
	protected String isPrint = "N";

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getDiyJs() {
        return diyJs;
    }

    public void setDiyJs(String diyJs) {
        this.diyJs = diyJs;
    }
    
    /**
	 * @return the formType
	 */
	public String getFormType() {
		return formType;
	}

	/**
	 * @param formType the formType to set
	 */
	public void setFormType(String formType) {
		this.formType = formType;
	}

	public void setId(String id) {
		this.id = id;

	}

	public String getId() {
		return this.id;
	}

	/**
	 * @return the defId
	 */
	public String getDefId() {
		return defId;
	}

	/**
	 * @param defId the defId to set
	 */
	public void setDefId(String defId) {
		this.defId = defId;
	}

	/**
	 * @return the formKey
	 */
	public String getFormKey() {
		return formKey;
	}

	/**
	 * @param formKey the formKey to set
	 */
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}


	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}


	/**
	 * @return the formHtml
	 */
	public String getFormHtml() {
		return formHtml;
	}


	/**
	 * @param formHtml the formHtml to set
	 */
	public void setFormHtml(String formHtml) {
		this.formHtml = formHtml;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return the isMain
	 */
	public char getIsMain() {
		return isMain;
	}


	/**
	 * @param isMain the isMain to set
	 */
	public void setIsMain(char isMain) {
		this.isMain = isMain;
	}


	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}


	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}


	/**
	 * @return the typeId
	 */
	public String getTypeId() {
		return typeId;
	}


	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}


	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}


	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	/**
	 * @return the formTabTitle
	 */
	public String getFormTabTitle() {
		return formTabTitle;
	}


	/**
	 * @param formTabTitle the formTabTitle to set
	 */
	public void setFormTabTitle(String formTabTitle) {
		this.formTabTitle = formTabTitle;
	}


	/**
	 * @return the busDataTemplateCount
	 */
	public short getBusDataTemplateCount() {
		return busDataTemplateCount;
	}


	/**
	 * @param busDataTemplateCount the busDataTemplateCount to set
	 */
	public void setBusDataTemplateCount(short busDataTemplateCount) {
		this.busDataTemplateCount = busDataTemplateCount;
	}


	/**
	 * @return the versionCount
	 */
	public Integer getVersionCount() {
		return versionCount;
	}


	/**
	 * @param versionCount the versionCount to set
	 */
	public void setVersionCount(Integer versionCount) {
		this.versionCount = versionCount;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.id)
				.append("defId", this.defId)
				.append("formKey", this.formKey)
				.append("name", this.name)
				.append("desc", this.desc)
				.append("status", this.status)
				.append("isMain", this.isMain)
				.append("version", this.version)
				.append("typeName", this.typeName)
				.append("formType", this.formType)
				.toString();
	}
}
