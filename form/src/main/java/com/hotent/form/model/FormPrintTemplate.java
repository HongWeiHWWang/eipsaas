package com.hotent.form.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("表单套打模板")
@TableName("FORM_PRINT_TEMPLATE")
public class FormPrintTemplate extends AutoFillModel<FormPrintTemplate> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="主键")
    @TableId("ID_")
    protected String id;

    @ApiModelProperty(value="附件ID")
    @TableField("FILE_ID_")
    protected String fileId;

    @ApiModelProperty(value="模板名称")
    @TableField("FILE_NAME")
    protected String fileName;

    @ApiModelProperty(value="表单key")
    @TableField("FORM_KEY_")
    protected String formKey;
    
    @ApiModelProperty(value="表单元数据id")
    @TableField("DEF_ID_")
    protected String defId;
    
    @ApiModelProperty(value="表单id")
    @TableField("FORM_ID_")
    protected String formId;
    
    @ApiModelProperty(value="打印类型(word:word套打，form:表单模板)")
    @TableField("PRINT_TYPE_")
    protected String printType;

    @ApiModelProperty(value="是否主版本")
    @TableField("IS_MAIN")
    protected String isMain="N";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

	public String getPrintType() {
		return printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}
    
}
