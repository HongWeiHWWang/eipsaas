package com.hotent.bpm.model.form;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.util.StringUtil;



/**
 * 对象功能:流程任务表单 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:何一帆
 * 创建时间:2014-06-18 11:57:34
 */
public class BpmForm extends DefaultForm implements FormModel , Cloneable{
	private static final long serialVersionUID = 1L;
	/**  * 草稿=draft  */
	public static final String STATUS_DRAFT="draft"; 
	/**  * 发布=deploy  */
	public static final String STATUS_DEPLOY="deploy";
	@XmlAttribute(name = "id")
	protected String id; /*表单ID*/
	
	@XmlAttribute(name = "defId")
	protected String defId; /*表单定义ID*/
	
	@XmlAttribute(name = "formKey")
	protected String formKey; /*表单业务主键*/
	
	@XmlAttribute(name = "name")
	protected String name; /*表单描述*/
	
	@XmlElement(name = "desc")
	protected String desc; /*表单描述*/
	
	@XmlElement(name = "formHtml")
	protected String formHtml; /*表单设计（HTML代码）*/
	
	@XmlAttribute(name = "status")
	protected String status = BpmForm.STATUS_DRAFT; /*表单状态。draft=草稿；deploy=发布*/
	
	@XmlAttribute(name = "isMain")
	protected char isMain = 'Y'; /*是否主版本*/
	
	@XmlAttribute(name = "version")
	protected Integer version = 1; /*表单版本号*/

	@XmlAttribute(name = "typeId")
	protected String typeId;/**分类iD*/
	@XmlAttribute(name = "typeName")
	protected String typeName;/***分类Name*/
	
	@XmlElement(name = "formTabTitle")
	protected String formTabTitle;//tab 的标题
	
	protected short busDataTemplateCount = 0; /** 是否已经生成业务数据模板  0 没有生成 1已经生成 */
	
	protected Integer versionCount = 0; /*表单版本数*/
	
	protected String formExtraConf;
	
	protected String helpFile;
	
	public BpmForm(){
		
	}
	
	public BpmForm(Form frm){
		setFormValue(frm.getFormValue());
		setName(frm.getName());
		setNodeId(frm.getNodeId());
		setParentFlowKey(frm.getParentFlowKey());
		setType(frm.getType());
		setFormExtraConf(frm.getFormExtraConf());
		setHelpFile(frm.getHelpFile());
	}
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 表单ID
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setFormKey(String formKey) 
	{
		this.formKey = formKey;
	}
	/**
	 * 返回 表单业务主键
	 * @return
	 */
	public String getFormKey() 
	{
		return this.formKey;
	}
	public void setDesc(String desc) 
	{
		this.desc = desc;
	}
	/**
	 * 返回 表单描述
	 * @return
	 */
	public String getDesc() 
	{
		return this.desc;
	}
	
	/**
	 * 设置表单html。
	 * 如果设置tabHtml
	 * @deprecated
	 */
	public void setFormHtml(String formHtml) 
	{
		this.formHtml = formHtml;
	}
	/**
	 * 返回 表单源码，未进行tab处理
	 * @return
	 */
	/*public String getFormSourceHtml()
	{
		return formHtml;
	}
	
	public void setFormSourceHtml(String formHtml)
	{
		this.formHtml = formHtml;
	}*/
	
	/**
	 * 返回 表单设计（HTML代码）
	 * 有#page#标记的则改为TAb页面
	 * @return
	 */
	public String getFormHtml()
	{
		if(StringUtil.isEmpty(formHtml) || StringUtil.isEmpty(formTabTitle))return formHtml;
		if(formHtml.indexOf("#tab#")==-1)return formHtml;
		
		
		String [] pageArr = formHtml.split("#tab#");
		String [] titleArr = formTabTitle.split("#tab#");
		
		//将以#page#分割的添加上tab分页
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='formTab' class='easyui-tabs' style=''>");
		for (int i = 0; i < pageArr.length; i++) {
			sb.append("<div title='"+titleArr[i]+"' style='padding:20px;'>");
			sb.append(pageArr[i]);
			sb.append("</div>");
		}
		sb.append("</div>");
		
		return sb.toString();
	}

	
	public void setStatus(String status) 
	{
		this.status = status;
	}
	/**
	 * 返回 表单状态。draft=草稿；deploy=发布
	 * @return
	 */
	public String getStatus() 
	{
		return this.status;
	}
	
	
	
	public void setIsMain(char isMain) 
	{
		this.isMain = isMain;
	}


	public String getDefId() {
		return defId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getFormTabTitle() {
		return formTabTitle;
	}

	public void setFormTabTitle(String formTabTitle) {
		this.formTabTitle = formTabTitle;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	/**
	 * 返回 是否主版本
	 * @return
	 */
	public char getIsMain() 
	{
		return this.isMain;
	}
	public void setVersion(Integer version) 
	{
		this.version = version;
	}

	public Integer getVersionCount() {
		return versionCount;
	}

	public void setVersionCount(Integer versionCount) {
		this.versionCount = versionCount;
	}

	/**
	 * 返回 表单版本号
	 * @return
	 */
	public Integer getVersion() 
	{
		return this.version;
	}
	
	
	
	public String getFormId() {
		return this.id;
	}
	public void setFormId(String formId) {
		this.id=formId;
	}
	

	public short getBusDataTemplateCount() {
		return busDataTemplateCount;
	}

	public void setBusDataTemplateCount(short busDataTemplateCount) {
		this.busDataTemplateCount = busDataTemplateCount;
	}

	public Object clone()
	{
		BpmForm obj=null;
		try{
			obj=(BpmForm)super.clone();
		}catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("formKey", this.formKey) 
		.append("desc", this.desc) 
		.append("formHtml", this.formHtml) 
		.append("status", this.status) 
		.append("isMain", this.isMain) 
		.append("version", this.version) 
		.toString();
	}

	public String getFormExtraConf() {
		return formExtraConf;
	}

	public void setFormExtraConf(String formExtraConf) {
		this.formExtraConf = formExtraConf;
	}

	public String getHelpFile() {
		return helpFile;
	}

	public void setHelpFile(String helpFile) {
		this.helpFile = helpFile;
	}
	
}