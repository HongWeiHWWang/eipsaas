package com.hotent.bpm.persistence.model;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.DesignerType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefinition;



/**
 * 对象功能:@名称：BPM_DEFINITION 【流程定义】
 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2013-12-12 18:02:04
 */

@XmlRootElement(name = "BpmDef")
@XmlAccessorType(XmlAccessType.NONE)
@TableName("bpm_definition")
public class DefaultBpmDefinition extends AutoFillModel<DefaultBpmDefinition> implements BpmDefinition,Cloneable{
	private static final long serialVersionUID = 3755643685770853878L;
	
	@TableId("def_id_")
	protected String  defId; /*流程定义ID*/
	
	@TableField("name_")
	@XmlAttribute(name="name")
	protected String  name; /*流程名称*/
	
	@TableField("def_key_")
	@XmlAttribute(name="defKey")
	protected String  defKey; /*流程定义key*/
	
	@TableField("desc_")
	@XmlAttribute(name="desc")
	protected String  desc; /*流程描述*/
	
	@TableField("status_")
	@XmlAttribute(name="status")
	protected String  status; /*流程状态。草稿、发布、禁用*/
	
	@TableField("test_status_")
	/*测试状态,是和否*/
	@XmlAttribute(name="testStatus")
	protected String  testStatus=BpmDefinition.TEST_STATUS.TEST; 
	
	@TableField("bpmn_def_id_")
	protected String  bpmnDefId; /*BPMN - 流程定义ID*/
	
	@TableField("bpmn_deploy_id_")
	protected String  bpmnDeployId; /*BPMN - 流程发布ID*/
	
	@TableField("version_")
	protected Integer  version;			/*版本 - 当前版本号*/
	
	@TableField("main_def_id_")
	protected String  mainDefId; /*版本 - 主版本流程ID*/
	
	@TableField("is_main_")
	protected String  isMain; /*版本 - 是否主版本*/
	
	@TableField("reason_")
	protected String  reason; /*版本 - 变更理由*/
	
	@TableField("designer_")
	@XmlAttribute(name="designer")
	protected String  designer=DesignerType.FLASH.name(); /*更新人ID*/
	
	@TableField("support_mobile_")
	@XmlAttribute(name="supportMobile")
	protected int supportMobile=0;
	
	@TableField("rev_")
    @XmlAttribute(name="rev")
    protected Integer rev=0; /*关联锁*/
	
	@TableField("show_urgent_state_")
    @XmlAttribute(name="showUrgentState")
    protected int showUrgentState=0; /*显示紧急状态*/
	
	@TableField("show_modify_record_")
    @XmlAttribute(name="showModifyRecord")
	protected int showModifyRecord = 0; /*显示表单留痕*/
	
	@TableField("is_read_revoke")
    @XmlAttribute(name="isReadRevoke")
    protected String isReadRevoke = "false"; /*传阅已阅是否允许撤回*/
	
	@TableField("urgent_mail_tel")
    @XmlAttribute(name="urgentMailTel")
    protected String urgentMailTel; /*人工催办邮件模板*/
	
	@TableField("urgent_sms_tel")
    @XmlAttribute(name="urgentSmsTel")
    protected String urgentSmsTel; /*人工催办短信模板*/
    
	@TableField("type_id_")
    @XmlAttribute(name="typeId")
    protected String typeId;
	
	@TableField("type_name_")
    @XmlAttribute(name="type_name_")
    protected String typeName;
	
	@TableField(exist=false)
    protected List<BpmIdentity> leaders; /*领导列表*/
	
	//流程分管授权权限对象
	@TableField(exist=false)
  	protected ObjectNode authorizeRight;
	
  	@XmlElement(name="bpmDefData")
  	@TableField(exist=false)
	protected BpmDefData bpmDefData = new BpmDefData();
	
  	
    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }
	
	public void setId(String id) {
		setDefId(id);
	}
	
	@XmlTransient
	public String getId() {
		return defId;
	}	
	public void setDefId(String defId) 
	{
		this.defId = defId;
	}
	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public String getDefId() 
	{
		return this.defId;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	/**
	 * 返回 流程名称
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	
	public void setDefKey(String defKey) 
	{
		this.defKey = defKey;
	}
	/**
	 * 返回 流程定义Key
	 * @return
	 */
	public String getDefKey() 
	{
		return this.defKey;
	}
	public void setDesc(String desc) 
	{
		this.desc = desc;
	}
	/**
	 * 返回 流程描述
	 * @return
	 */
	public String getDesc() 
	{
		return this.desc;
	}
	public void setTypeId(String typeId) 
	{
		this.typeId = typeId;
	}
	/**
	 * 返回 所属分类ID
	 * @return
	 */
	public String getTypeId() 
	{
		return this.typeId;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	/**
	 * 返回 流程状态。草稿、发布、禁用
	 * @return
	 */
	public String getStatus() 
	{
		return this.status;
	}
	public void setTestStatus(String testStatus) 
	{
		this.testStatus = testStatus;
	}
	/**
	 * 返回 测试状态
	 * @return
	 */
	public String getTestStatus() 
	{
		return this.testStatus;
	}
	
	public void setBpmnDefId(String bpmnDefId) 
	{
		this.bpmnDefId = bpmnDefId;
	}
	/**
	 * 返回 BPMN - 流程定义ID
	 * @return
	 */
	public String getBpmnDefId() 
	{
		return this.bpmnDefId;
	}
	public void setBpmnDeployId(String bpmnDeployId) 
	{
		this.bpmnDeployId = bpmnDeployId;
	}
	/**
	 * 返回 BPMN - 流程发布ID
	 * @return
	 */
	public String getBpmnDeployId() 
	{
		return this.bpmnDeployId;
	}
	public void setVersion(Integer version) 
	{
		this.version = version;
	}
	/**
	 * 返回 版本 - 当前版本号
	 * @return
	 */
	public Integer getVersion() 
	{
		return this.version;
	}
	public void setMainDefId(String mainDefId) 
	{
		this.mainDefId = mainDefId;
	}
	/**
	 * 返回 版本 - 主版本流程ID
	 * @return
	 */
	public String getMainDefId() 
	{
		return this.mainDefId;
	}
	public void setIsMain(String isMain) 
	{
		this.isMain = isMain;
	}
	/**
	 * 返回 版本 - 是否主版本
	 * @return
	 */
	public String getIsMain() 
	{
		return this.isMain;
	}
	public void setReason(String reason) 
	{
		this.reason = reason;
	}
	/**
	 * 返回 版本 - 变更理由
	 * @return
	 */
	public String getReason() 
	{
		return this.reason;
	}
	/**
	 * 设计器。
	 * @param designer 
	 * void
	 */
	public void setDesigner(String designer){
		this.designer=designer;
	}
	
	/**
	 * 设计器。
	 * @param designer
	 * @return  String
	 */
	public String getDesigner(){
		return this.designer;
	}
	
	public String getDefJson() {
		return bpmDefData.defJson;
	}
	
	public void setDefJson(String defJson){
		bpmDefData.defJson = defJson;
	}
	
	@Override
	public String getDefXml() {
		return bpmDefData.defXml;
	}
	public void setDefXml(String defXml){
		// 处理泳道池的泳道Lane 的ID重复的问题
		defXml=dealPool(defXml);
		
		bpmDefData.defXml = defXml;
	}
	@Override
	public String getBpmnXml() {
		return bpmDefData.bpmnXml;
	}
	public void setBpmnXml(String bpmnXml){
		bpmDefData.bpmnXml = bpmnXml;
	}
	@Override
	public boolean isMain() {		
		return "Y".equals(isMain)?true:false;
	}
	
	public BpmDefData getBpmDefData() {
		return bpmDefData;
	}
	public void setBpmDefData(BpmDefData bpmDefData) {
		if(StringUtil.isNotEmpty(this.getDefId())){
			bpmDefData.setId(this.getDefId());
		}
		this.bpmDefData = bpmDefData;
	}
	
	public ObjectNode getAuthorizeRight()
	{
		return authorizeRight;
	}
	public void setAuthorizeRight(ObjectNode authorizeRight2)
	{
		this.authorizeRight = authorizeRight2;
	}
	
	
	/**
	 * 处理泳道池的泳道Lane 的ID重复的问题
	 * 
	 * @param defXml
	 */
	private String dealPool(String defXml) {
		// 控制ID不重复
		int v = 1;
		int h = 1;
		// 处理垂直泳道池
		Pattern vRegex = Pattern.compile("<bg:VLane\\s*(id=\"\\w+\")\\s*");
		Matcher vRegexMatcher = vRegex.matcher(defXml);
		while (vRegexMatcher.find()) {
			String vLane = "id=\"vLane" + v + "\"";
			defXml = defXml.replaceFirst(vRegexMatcher.group(1), vLane);
			v++;
		}
		// 处理水平泳道池
		Pattern hRegex = Pattern.compile("<bg:HLane\\s*(id=\"\\w+\")\\s*");
		Matcher hRegexMatcher = hRegex.matcher(defXml);
		while (hRegexMatcher.find()) {
			String hLane = "id=\"hLane" + h + "\"";
			defXml = defXml.replaceFirst(hRegexMatcher.group(1), hLane);
			h++;
		}
		return defXml;
	}
	
	public int getSupportMobile() {
		return supportMobile;
	}
	public void setSupportMobile(int supportMobile) {
		this.supportMobile = supportMobile;
	}
	
	public int getShowUrgentState() {
		return showUrgentState;
	}

	public void setShowUrgentState(int showUrgentState) {
		this.showUrgentState = showUrgentState;
	}

    public int getShowModifyRecord() {
		return showModifyRecord;
	}

	public void setShowModifyRecord(int showModifyRecord) {
		this.showModifyRecord = showModifyRecord;
	}

	public String getIsReadRevoke() {
        return isReadRevoke;
    }

    public void setIsReadRevoke(String isReadRevoke) {
        this.isReadRevoke = isReadRevoke;
    }

    public String getUrgentMailTel() {
        return urgentMailTel;
    }

    public void setUrgentMailTel(String urgentMailTel) {
        this.urgentMailTel = urgentMailTel;
    }

    public String getUrgentSmsTel() {
        return urgentSmsTel;
    }

    public void setUrgentSmsTel(String urgentSmsTel) {
        this.urgentSmsTel = urgentSmsTel;
    }
    
    

    public List<BpmIdentity> getLeaders() {
		return leaders;
	}

	public void setLeaders(List<BpmIdentity> leaders) {
		this.leaders = leaders;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("defId", this.defId) 
		.append("name", this.name)
        .append("rev", this.rev)
        .append("isReadRevoke", this.isReadRevoke)
        .append("showUrgentState", this.showUrgentState)
        .append("defKey", this.defKey)
		.append("desc", this.desc) 
		.append("typeId", this.typeId) 
		.append("status", this.status) 
		.append("testStatus", this.testStatus) 
		.append("bpmnDefId", this.bpmnDefId) 
		.append("bpmnDeployId", this.bpmnDeployId) 
		.append("version", this.version) 
		.append("mainDefId", this.mainDefId) 
		.append("isMain", this.isMain) 
		.append("reason", this.reason) 
		.toString();
	}

	public Object clone() {
		DefaultBpmDefinition obj=null;
		try{
			obj=(DefaultBpmDefinition)super.clone();
			obj.setBpmDefData((BpmDefData)bpmDefData.clone());
		}catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}