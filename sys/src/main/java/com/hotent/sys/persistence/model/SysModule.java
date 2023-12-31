package com.hotent.sys.persistence.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("portal_sys_module")
@ApiModel(description="系统模块")
public class SysModule  extends AutoFillModel<SysModule> {
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id",notes = "主键")
    @TableId("id_")
    protected String id;

    @ApiModelProperty(name = "code",notes = "模块编码")
    @TableField("code_")
    protected String code;

    @ApiModelProperty(name = "name",notes = "模块名称")
    @TableField("name_")
    protected String name;

    /**
     * 状态 draft=草稿；deploy=发布
     */
    @ApiModelProperty(name = "status",notes = "状态")
    @TableField("status_")
    protected String status;

    /**
     * 模块类型
     */
    @ApiModelProperty(name = "type",notes = "模块类型")
    @TableField("type_")
    protected String type;

    /**
     * 模块分类id
     */
    @ApiModelProperty(name = "classifyId",notes = "模块分类id")
    @TableField("classify_id_")
    protected String classifyId;

    @ApiModelProperty(name = "classifyName",notes = "模块分类名称")
    @TableField("classify_name_")
    protected String classifyName;

    @ApiModelProperty(name = "relCode",notes = "模块类型编码")
    @TableField("rel_code_")
    protected String relCode;

    @ApiModelProperty(name = "relName",notes = "模块类型名称")
    @TableField("rel_name_")
    protected String relName;

    @ApiModelProperty(name = "desc",notes = "模块描述")
    @TableField("desc_")
    protected String desc;

    @ApiModelProperty(name = "menu",notes = "菜单分配（workspace:工作空间，datalist:数据列表，chart:图表，other:其他）")
    @TableField("menu_")
    protected String menu;


    /**
     * 创建人岗位编码
     */
    @ApiModelProperty(name = "postCode",notes = "创建人岗位编码")
    @TableField("post_code_")
    protected String postCode;

    /**
     * 创建人岗位名称
     */
    @ApiModelProperty(name = "postName",notes = "创建人岗位名称")
    @TableField("post_name_")
    protected String postName;

    @ApiModelProperty(name = "creator",notes = "创建人")
    @TableField("creator_")
    protected String creator;

    @ApiModelProperty(name = "isDeploy",notes = "是否发布")
    @TableField(exist=false)
    protected boolean isDeploy;

    @ApiModelProperty(name = "reportName",notes = "报表名称")
    @TableField("report_name_")
    protected String reportName;

    @ApiModelProperty(name = "reportAlias",notes = "报表别名")
    @TableField("report_alias")
    protected String reportAlias;

    @TableField(exist=false)
    @ApiModelProperty(name = "moduleDetail",notes = "模块明细")
    protected List<SysModuleDetail> moduleDetail;


    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportAlias() {
        return reportAlias;
    }

    public void setReportAlias(String reportAlias) {
        this.reportAlias = reportAlias;
    }

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

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 返回 模块编码
     * @return
     */
    public String getCode() {
        return this.code;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 返回 模块名称
     * @return
     */
    public String getName() {
        return this.name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 返回 状态 draft=草稿；deploy=发布
     * @return
     */
    public String getStatus() {
        return this.status;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 返回 模块类型
     * @return
     */
    public String getType() {
        return this.type;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    /**
     * 返回 模块分类id
     * @return
     */
    public String getClassifyId() {
        return this.classifyId;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    /**
     * 返回 模块分类名称
     * @return
     */
    public String getClassifyName() {
        return this.classifyName;
    }

    public void setRelCode(String relCode) {
        this.relCode = relCode;
    }

    /**
     * 返回 模块类型编码
     * @return
     */
    public String getRelCode() {
        return this.relCode;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }

    /**
     * 返回 模块类型名称
     * @return
     */
    public String getRelName() {
        return this.relName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 返回 模块描述
     * @return
     */
    public String getDesc() {
        return this.desc;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    /**
     * 返回 菜单分配
     * @return
     */
    public String getMenu() {
        return this.menu;
    }


    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * 返回 创建人岗位编码
     * @return
     */
    public String getPostCode() {
        return this.postCode;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    /**
     * 返回 创建人岗位名称
     * @return
     */
    public String getPostName() {
        return this.postName;
    }

    public List<SysModuleDetail> getModuleDetail() {
        return moduleDetail;
    }

    public void setModuleDetail(List<SysModuleDetail> moduleDetail) {
        this.moduleDetail = moduleDetail;
    }

    public boolean isDeploy() {
        return isDeploy;
    }

    public void setDeploy(boolean deploy) {
        isDeploy = deploy;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("code", this.code)
                .append("name", this.name)
                .append("status", this.status)
                .append("type", this.type)
                .append("classifyId", this.classifyId)
                .append("classifyName", this.classifyName)
                .append("relCode", this.relCode)
                .append("relName", this.relName)
                .append("desc", this.desc)
                .append("menu", this.menu)
                .append("postCode", this.postCode)
                .append("postName", this.postName)
                .toString();
    }
}
