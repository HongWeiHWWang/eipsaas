package com.hotent.form.model;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.bo.model.ValidateResult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



 /**
 * form_data_import_log
 * <pre> 
 * 描述：form_data_import_log 实体对象
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-06-18 14:10:21
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
 @TableName("form_data_import_log")
 @ApiModel(value = "FormDataImportLog",description = "form_data_import_log") 
public class FormDataImportLog extends BaseModel<FormDataImportLog>  	 {

	private static final long serialVersionUID = 1L;
	@XmlTransient
	@TableId("ID_")
	@ApiModelProperty(value="ID_")
	protected String id; 
	
	@XmlAttribute(name = "PId")
	@TableField("P_ID_")
	@ApiModelProperty(value="P_ID_")
	protected String PId; 
	
	@XmlAttribute(name = "rowNumber")
	@TableField("ROW_NUMBER_")
	@ApiModelProperty(value="错误行号")
	protected Integer rowNumber; 
	
	@XmlAttribute(name = "columnName")
	@TableField("COLUMN_NAME_")
	@ApiModelProperty(value="错误列名")
	protected String columnName; 
	
	@XmlAttribute(name = "errorMsg")
	@TableField("ERROR_MSG_")
	@ApiModelProperty(value="错误信息")
	protected String errorMsg; 
	
	
	
	public FormDataImportLog() {
		super();
	}

	public FormDataImportLog(ValidateResult result) {
		super();
		this.columnName = result.getColumnName();
		this.errorMsg = result.getErrorMsg();
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	public void setPId(String PId) {
		this.PId = PId;
	}
	
	/**
	 * 返回 P_ID_
	 * @return
	 */
	public String getPId() {
		return this.PId;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
	
	/**
	 * 返回 错误行号
	 * @return
	 */
	public Integer getRowNumber() {
		return this.rowNumber;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * 返回 错误列名
	 * @return
	 */
	public String getColumnName() {
		return this.columnName;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	/**
	 * 返回 错误信息
	 * @return
	 */
	public String getErrorMsg() {
		return this.errorMsg;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("PId", this.PId) 
		.append("rowNumber", this.rowNumber) 
		.append("columnName", this.columnName) 
		.append("errorMsg", this.errorMsg) 
		.toString();
	}
}