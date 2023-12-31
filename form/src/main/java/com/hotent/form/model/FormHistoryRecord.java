package com.hotent.form.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.entity.AutoFillModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.time.LocalDateTime;

/**
 * 表单form_html历史数据
 * <pre> 
 * 描述：表单form_html历史数据 实体对象
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-11-19 10:03:35
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@ApiModel("表单form_html历史数据")
@TableName("form_history_record")
public class FormHistoryRecord extends AutoFillModel<FormHistoryRecord> {

	private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId("id_")
	protected String id;

    @ApiModelProperty("表单ID")
    @TableField("form_id_")
	protected String formId;

    @ApiModelProperty("表单HTML内容")
    @TableField("form_html_")
	protected String formHtml;

    //@ApiModelProperty(value = "创建时间", hidden=true, accessMode= ApiModelProperty.AccessMode.READ_ONLY)
    //@TableField(value="create_time_", fill= FieldFill.INSERT, select=true)
    //private LocalDateTime createTime;


	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键ID
	 * @return
	 */
	public String getId() {
		return this.id;
	}

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void setFormHtml(String formHtml) {
		this.formHtml = formHtml;
	}
	
	/**
	 * 返回 表单定义HTML
	 * @return
	 */
	public String getFormHtml() {
		return this.formHtml;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("formId", this.formId)
		.append("formHtml", this.formHtml)
		.toString();
	}
}