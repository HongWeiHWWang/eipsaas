package com.hotent.form.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 自定义查询字段元信息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@ApiModel("自定义查询字段元信息")
@TableName("form_query_metafield")
public class QueryMetafield extends BaseModel<QueryMetafield> {
	private static final long serialVersionUID = 1L;
	public static final short TRUE = 1;
	public static final short FALSE = 0;
	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id;

	/**
	 * SQL_ID
	 */
	@ApiModelProperty("SQL_ID")
	@TableField("sql_id_")
	protected String sqlId;

	/**
	 * 字段名
	 */
	@ApiModelProperty("字段名")
	@TableField("name_")
	protected String name;

	/**
	 * 实际字段名
	 */
	@ApiModelProperty("实际字段名")
	@TableField("field_name_")
	protected String fieldName;

	/**
	 * 字段备注
	 */
	@ApiModelProperty("字段备注")
	@TableField("field_desc_")
	protected String fieldDesc;

	/**
	 * 是否可见
	 */
	@ApiModelProperty("是否可见")
	@TableField("is_show_")
	protected Short isShow;

	/**
	 * 是否搜索
	 */
	@ApiModelProperty("是否搜索")
	@TableField("is_search_")
	protected Short isSearch;
	
	/**
	 * 是否合并查询
	 */
	@ApiModelProperty("是否合并查询")
	@TableField("is_combine_")
	protected Short isCombine;

	/**
	 * 控件类型
	 */
	@ApiModelProperty("控件类型")
	@TableField("control_type_")
	protected String controlType;

	/**
	 * 数据类型
	 */
	@ApiModelProperty("数据类型")
	@TableField("data_type_")
	protected String dataType;

	/**
	 * 是否衍生列
	 */
	@ApiModelProperty("是否衍生列")
	@TableField("is_virtual_")
	protected Short isVirtual;

	/**
	 * 衍生列来自列
	 */
	@ApiModelProperty("衍生列来自列")
	@TableField("virtual_from_")
	protected String virtualFrom;

	/**
	 * 来自类型
	 */
	@ApiModelProperty("来自类型")
	@TableField("result_from_type_")
	protected String resultFromType;

	/**
	 * 衍生列配置
	 */
	@ApiModelProperty("衍生列配置")
	@TableField("result_from_")
	protected String resultFrom;

	/**
	 * 报警设定
	 */
	@ApiModelProperty("报警设定")
	@TableField("alarm_setting_")
	protected String alarmSetting;

	/**
	 * 日期格式
	 */
	@ApiModelProperty("日期格式")
	@TableField("date_format_")
	protected String dateFormat;

	/**
	 * 连接地址
	 */
	@ApiModelProperty("连接地址")
	@TableField("url_")
	protected String url;

	/**
	 * 格式化函数
	 */
	@ApiModelProperty("格式化函数")
	@TableField("formater_")
	protected String formater;

	/**
	 * 控件内容
	 */
	@ApiModelProperty("控件内容")
	@TableField("control_content_")
	protected String controlContent;

	/**
	 * SN_
	 */
	@ApiModelProperty("排序号")
	@TableField("sn_")
	protected Short sn;

	/**
	 * 宽度
	 */
	@ApiModelProperty("宽度")
	@TableField("width_")
	protected Short width;

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	/**
	 * 返回 SQL_ID
	 * 
	 * @return
	 */
	public String getSqlId() {
		return this.sqlId;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 字段名
	 * 
	 * @return
	 */
	@XmlAttribute(name = "name")
	public String getName() {
		return this.name;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 返回 实际字段名
	 * 
	 * @return
	 */
	@XmlAttribute(name = "fieldName")
	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	/**
	 * 返回 字段备注
	 * 
	 * @return
	 */
	@XmlAttribute(name = "fieldDesc")
	public String getFieldDesc() {
		return this.fieldDesc;
	}

	public void setIsShow(Short isShow) {
		this.isShow = isShow;
	}

	/**
	 * 返回 是否可见
	 * 
	 * @return
	 */
	@XmlAttribute(name = "isShow")
	public Short getIsShow() {
		return this.isShow;
	}

	public void setIsSearch(Short isSearch) {
		this.isSearch = isSearch;
	}

	/**
	 * 返回 是否搜索
	 * 
	 * @return
	 */
	@XmlAttribute(name = "isSearch")
	public Short getIsSearch() {
		return this.isSearch;
	}

	@XmlAttribute(name = "controlType")
	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String getControlTypeDesc() {
		if (this.controlType.equals("onetext")) {
			return "单行文本框";
		}
		if (this.controlType.equals("select")) {
			return "下拉框";
		}
		if (this.controlType.equals("customdialog")) {
			return "自定义对话框";
		}
		if (this.controlType.equals("date")) {
			return "日期选择器";
		}
		return "";
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * 返回 数据类型
	 * 
	 * @return
	 */
	@XmlAttribute(name = "dataType")
	public String getDataType() {
		return this.dataType;
	}

	public void setIsVirtual(Short isVirtual) {
		this.isVirtual = isVirtual;
	}

	/**
	 * 返回 是否衍生列
	 * 
	 * @return
	 */
	@XmlAttribute(name = "isVirtual")
	public Short getIsVirtual() {
		return this.isVirtual;
	}

	public void setVirtualFrom(String virtualFrom) {
		this.virtualFrom = virtualFrom;
	}

	/**
	 * 返回 衍生列来自列
	 * 
	 * @return
	 */
	@XmlAttribute(name = "virtualFrom")
	public String getVirtualFrom() {
		return this.virtualFrom;
	}

	public void setResultFromType(String resultFromType) {
		this.resultFromType = resultFromType;
	}

	/**
	 * 返回 来自类型
	 * 
	 * @return
	 */
	@XmlAttribute(name = "resultFromType")
	public String getResultFromType() {
		return this.resultFromType;
	}

	public void setResultFrom(String resultFrom) {
		this.resultFrom = resultFrom;
	}

	/**
	 * 返回 衍生列配置
	 * 
	 * @return
	 */
	@XmlElement(name = "resultFrom")
	public String getResultFrom() {
		return this.resultFrom;
	}

	public void setAlarmSetting(String alarmSetting) {
		this.alarmSetting = alarmSetting;
	}

	/**
	 * 返回 报警设定
	 * 
	 * @return
	 */
	@XmlElement(name = "alarmSetting")
	public String getAlarmSetting() {
		return this.alarmSetting;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * 返回 日期格式
	 * 
	 * @return
	 */
	@XmlAttribute(name = "dateFormat")
	public String getDateFormat() {
		return this.dateFormat;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 返回 连接地址
	 * 
	 * @return
	 */
	@XmlAttribute(name = "url")
	public String getUrl() {
		return this.url;
	}

	public void setFormater(String formater) {
		this.formater = formater;
	}

	/**
	 * 返回 格式化函数
	 * 
	 * @return
	 */
	@XmlElement(name = "formater")
	public String getFormater() {
		return this.formater;
	}

	public void setControlContent(String controlContent) {
		this.controlContent = controlContent;
	}

	/**
	 * 返回 控件内容
	 * 
	 * @return
	 */
	@XmlElement(name = "controlContent")
	public String getControlContent() {
		return this.controlContent;
	}

	public void setSn(Short sn) {
		this.sn = sn;
	}

	/**
	 * 返回 SN_
	 * 
	 * @return
	 */
	@XmlAttribute(name = "sn")
	public Short getSn() {
		return this.sn;
	}

	public void setWidth(Short width) {
		this.width = width;
	}

	/**
	 * 返回 WIDTH_
	 * 
	 * @return
	 */
	@XmlAttribute(name = "width")
	public Short getWidth() {
		return this.width;
	}

	@XmlAttribute(name = "isCombine")
	public Short getIsCombine() {
		return isCombine;
	}

	public void setIsCombine(Short isCombine) {
		this.isCombine = isCombine;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("sqlId", this.sqlId).append("name", this.name).append("fieldName", this.fieldName).append("fieldDesc", this.fieldDesc).append("isShow", this.isShow).append("isSearch", this.isSearch).append("controlType", this.controlType).append("dataType", this.dataType).append("isVirtual", this.isVirtual).append("virtualFrom", this.virtualFrom).append("resultFromType", this.resultFromType).append("resultFrom", this.resultFrom).append("alarmSetting", this.alarmSetting).append("dateFormat", this.dateFormat).append("url", this.url).append("formater", this.formater).append("controlContent", this.controlContent).append("sn", this.sn).append("width", this.width).toString();
	}
}