package com.hotent.sys.persistence.model;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("portal_sys_module_detail")
@ApiModel(description="bo属性")
public class SysModuleDetail extends BaseModel<SysModuleDetail> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name = "id",notes = "主键")
    @TableId("id_")
    protected String id;

    @ApiModelProperty(name = "moduleId",notes="模块id")
    @TableField("module_id_")
    protected String moduleId;

    @ApiModelProperty(name = "type",notes = "类型（工作空间、数据列表、图表等）")
    @TableField("type_")
    protected String type;

    @ApiModelProperty(name = "columnName",notes = "栏目名称")
    @TableField("column_name_")
    protected String columnName;

    @ApiModelProperty(name = "code",notes = "编码")
    @TableField("code_")
    protected String code;

    @ApiModelProperty(name = "name",notes = "名称")
    @TableField("name_")
    protected String name;

    @ApiModelProperty(name = "viewCode",notes = "视图编码")
    @TableField("view_code_")
    protected String viewCode;

    @ApiModelProperty(name = "viewName",notes = "视图名称")
    @TableField("view_name_")
    protected String viewName;

    @ApiModelProperty(name = "url",notes = "查看地址")
    @TableField("url_")
    protected String url;

    @ApiModelProperty(name = "chartType",notes = "图表类型")
    @TableField("chart_type_")
    protected String chartType;

    @ApiModelProperty(name = "showType",notes = "工作空间显示方式")
    @TableField("show_type_")
    protected String showType;

    @ApiModelProperty(name = "sn",notes = "排序")
    @TableField("sn_")
    protected Integer sn;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }
}
