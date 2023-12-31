package com.hotent.form.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hotent.base.entity.AutoFillModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

/**
 * 代码生成日志
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @since 2020-05-09
 */
@ApiModel(value="FormCodegenLog对象", description="代码生成日志")
public class FormCodegenLog extends AutoFillModel<FormCodegenLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "操作类型")
    @TableField("type_")
    private String type;

    @ApiModelProperty(value = "表或表单名称")
    @TableField("table_or_form_")
    private String tableOrForm;

    @ApiModelProperty(value = "操作参数")
    @TableField("ope_content_")
    private String opeContent;

    @ApiModelProperty(value = "操作IP")
    @TableField("ip_")
    private String ip;

    @ApiModelProperty(value = "创建人ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_by_", fill=FieldFill.INSERT)
    private String createBy;

	@ApiModelProperty(value = "创建时间", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_time_", fill=FieldFill.INSERT)
    private LocalDateTime createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getTableOrForm() {
        return tableOrForm;
    }

    public void setTableOrForm(String tableOrForm) {
        this.tableOrForm = tableOrForm;
    }
    public String getOpeContent() {
        return opeContent;
    }

    public void setOpeContent(String opeContent) {
        this.opeContent = opeContent;
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "FormCodegenLog{" +
            "id=" + id +
            ", type=" + type +
            ", tableOrForm=" + tableOrForm +
            ", opeContent=" + opeContent +
            ", ip=" + ip +
            ", createBy=" + createBy +
            ", createTime=" + createTime +
        "}";
    }
}
