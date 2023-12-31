package com.hotent.runtime.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程定义其他参数对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程定义其他参数对象")
public class DefOtherParam {
	
	@ApiModelProperty(name="defId",notes="流程定义id",required=true)
	protected String defId;
	
	@ApiModelProperty(name="allowCopyTo",notes="是否允许抄送",allowableValues="false,true")
	private Boolean allowCopyTo;
	
	@ApiModelProperty(name="allowCopyTo",notes="可参考流程设置中的其他设置的标题规则")
	protected String subjectRule;
	
	@ApiModelProperty(name="description",notes="流程描述")
	protected String description;
	
	@ApiModelProperty(name="startNotifyType",notes="发起时通知相关人员类型，inner（内部消息），mail（邮件），sms（短信），voic（电话语音），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms,voic")
    protected String startNotifyType;
    
	@ApiModelProperty(name="archiveNotifyType",notes="归档时通知类型，inner（内部消息），mail（邮件），sms（短信），voic（电话语音），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms,voic")
    protected String archiveNotifyType;
    
	@ApiModelProperty(name="notifyType",notes="通知类型，inner（内部消息），mail（邮件），sms（短信），voic（电话语音），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms,voic")
    protected String notifyType;
    
	@ApiModelProperty(name="skipFirstNode",notes="跳过第一个节点",allowableValues="true,false")
    protected boolean skipFirstNode;
    
	@ApiModelProperty(name="firstNodeUserAssign",notes="第一个节点用户可以选择执行人",allowableValues="false,true")
    protected boolean firstNodeUserAssign;
    
	@ApiModelProperty(name="skipSameUser",notes="两个节点相同节点执行人跳过",allowableValues="true,false")
    protected boolean skipSameUser;
    
	@ApiModelProperty(name="allowTransTo",notes="允许转办",allowableValues="false,true")
    protected boolean allowTransTo;
    
	@ApiModelProperty(name="useMainForm",notes="使用表单版本，mainVersion（表单主版本），startVersion（启动时版本），不填则表示采用全局设置",
			allowableValues="mainVersion,startVersion")
    protected String useMainForm;
    
	@ApiModelProperty(name="allowReference",notes="允许参考",allowableValues="false,true")
    protected boolean allowReference=false;
    
    
	@ApiModelProperty(name="allowRefCounts",notes="允许参考的数量")
    protected int allowRefCounts=5;
    
	@ApiModelProperty(name="allowExecutorEmpty",notes="是否允许执行人为空",allowableValues="false,true")
    protected boolean allowExecutorEmpty;
    
	@ApiModelProperty(name="skipExecutorEmpty",notes="执行人为空时跳过此任务",allowableValues="false,true")
    protected boolean skipExecutorEmpty=false;
    
	@ApiModelProperty(name="testNotifyType",notes="测试状态通知类型",allowableValues="mail,inner")
    protected String testNotifyType;
    
	@ApiModelProperty(name="testStatus",notes="测试状态，test（测试状态），run（运营状态）",allowableValues="test,run")
    protected String testStatus;
    
	@ApiModelProperty(name="status",notes="流程状态，draft（草稿），deploy（已发布），forbidden（禁用），forbidden_instance（禁用实例），deleted（删除）",
			allowableValues="draft,deploy,forbidden,forbidden_instance,deleted")
    protected String status;
    
	@ApiModelProperty(name="skipRules",notes="跳转规则，可不填，all（无条件跳过），sameUser（相同执行人跳过），approver（审批跳过），emptyUser（执行人为空跳过）",
			allowableValues="all,sameUser,approver,emptyUser")
    protected String skipRules;
    
	@ApiModelProperty(name="dateType",notes="计算审批期限的类型   worktime（工作日）, caltime（日历日）",
			allowableValues="worktime, caltime")
 	private String dateType ;
 	
	@ApiModelProperty(name="dueTime",notes="节点审批期限 (分钟)")
 	private int dueTime;
	@ApiModelProperty(name="doneDataVersion",notes="查看已办数据版本，history（历史版本），latest（最新版本）",
			allowableValues="history,latest")
    protected String doneDataVersion;

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public Boolean getAllowCopyTo() {
		return allowCopyTo;
	}

	public void setAllowCopyTo(Boolean allowCopyTo) {
		this.allowCopyTo = allowCopyTo;
	}

	public String getSubjectRule() {
		return subjectRule;
	}

	public void setSubjectRule(String subjectRule) {
		this.subjectRule = subjectRule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartNotifyType() {
		return startNotifyType;
	}

	public void setStartNotifyType(String startNotifyType) {
		this.startNotifyType = startNotifyType;
	}

	public String getArchiveNotifyType() {
		return archiveNotifyType;
	}

	public void setArchiveNotifyType(String archiveNotifyType) {
		this.archiveNotifyType = archiveNotifyType;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public boolean isSkipFirstNode() {
		return skipFirstNode;
	}

	public void setSkipFirstNode(boolean skipFirstNode) {
		this.skipFirstNode = skipFirstNode;
	}

	public boolean isFirstNodeUserAssign() {
		return firstNodeUserAssign;
	}

	public void setFirstNodeUserAssign(boolean firstNodeUserAssign) {
		this.firstNodeUserAssign = firstNodeUserAssign;
	}

	public boolean isSkipSameUser() {
		return skipSameUser;
	}

	public void setSkipSameUser(boolean skipSameUser) {
		this.skipSameUser = skipSameUser;
	}

	public boolean isAllowTransTo() {
		return allowTransTo;
	}

	public void setAllowTransTo(boolean allowTransTo) {
		this.allowTransTo = allowTransTo;
	}

	public String getUseMainForm() {
		return useMainForm;
	}

	public void setUseMainForm(String useMainForm) {
		this.useMainForm = useMainForm;
	}

	public boolean isAllowReference() {
		return allowReference;
	}

	public void setAllowReference(boolean allowReference) {
		this.allowReference = allowReference;
	}

	public int getAllowRefCounts() {
		return allowRefCounts;
	}

	public void setAllowRefCounts(int allowRefCounts) {
		this.allowRefCounts = allowRefCounts;
	}

	public boolean isAllowExecutorEmpty() {
		return allowExecutorEmpty;
	}

	public void setAllowExecutorEmpty(boolean allowExecutorEmpty) {
		this.allowExecutorEmpty = allowExecutorEmpty;
	}

	public boolean isSkipExecutorEmpty() {
		return skipExecutorEmpty;
	}

	public void setSkipExecutorEmpty(boolean skipExecutorEmpty) {
		this.skipExecutorEmpty = skipExecutorEmpty;
	}

	public String getTestNotifyType() {
		return testNotifyType;
	}

	public void setTestNotifyType(String testNotifyType) {
		this.testNotifyType = testNotifyType;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSkipRules() {
		return skipRules;
	}

	public void setSkipRules(String skipRules) {
		this.skipRules = skipRules;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public int getDueTime() {
		return dueTime;
	}

	public void setDueTime(int dueTime) {
		this.dueTime = dueTime;
	}
	
	public String getDoneDataVersion() {
		return doneDataVersion;
	}

	public void setDoneDataVersion(String doneDataVersion) {
		this.doneDataVersion = doneDataVersion;
	}

	public String toString() {
//		return new ToStringBuilder(this)
//		.append("defId", this.defId) 
//		.append("allowCopyTo", this.allowCopyTo) 
//		.append("subjectRule", this.subjectRule) 
//		.append("description", this.description) 
//		.append("startNotifyType", this.startNotifyType) 
//		.append("archiveNotifyType", this.archiveNotifyType) 
//		.append("notifyType", this.notifyType) 
//		.append("skipFirstNode", this.skipFirstNode) 
//		.append("firstNodeUserAssign", this.firstNodeUserAssign) 
//		.append("skipSameUser", this.skipSameUser) 
//		.append("allowTransTo", this.allowTransTo) 
//		.append("useMainForm", this.useMainForm) 
//		.append("allowReference", this.allowReference) 
//		.append("allowRefCounts", this.allowRefCounts) 
//		.append("allowExecutorEmpty", this.allowExecutorEmpty) 
//		.append("skipExecutorEmpty", this.skipExecutorEmpty) 
//		.append("testNotifyType", this.testNotifyType) 
//		.append("testStatus", this.testStatus) 
//		.append("status", this.status) 
//		.append("skipRules", this.skipRules) 
//		.append("dateType", this.dateType) 
//		.append("dueTime", this.dueTime)
//		.toString();
		return "{"
		+ "\""+"defId"+"\""+":"+"\""+this.defId+"\","
		+ "\""+"allowCopyTo"+"\""+":"+"\""+this.allowCopyTo+"\","
		+ "\""+"useMainForm"+"\""+":"+"\""+this.useMainForm+"\","
		+ "\""+"allowTransTo"+"\""+":"+"\""+this.allowTransTo+"\","
		+ "\""+"skipSameUser"+"\""+":"+"\""+this.skipSameUser+"\","
		+"\""+"subjectRule"+"\""+":"+"\""+this.subjectRule+"\","
		+"\""+"description"+"\""+":"+"\""+this.description+"\","
		+"\""+"startNotifyType"+"\""+":"+"\""+this.startNotifyType+"\","
		+"\""+"archiveNotifyType"+"\""+":"+"\""+this.archiveNotifyType+"\","
		+"\""+"notifyType"+"\""+":"+"\""+this.notifyType+"\","
		+"\""+"skipFirstNode"+"\""+":"+"\""+this.skipFirstNode+"\","
		+"\""+"firstNodeUserAssign"+"\""+":"+"\""+this.firstNodeUserAssign+"\","
		+"\""+"testStatus"+"\""+":"+"\""+this.testStatus+"\","
		+"\""+"testNotifyType"+"\""+":"+"\""+this.testNotifyType+"\","
		+"\""+"skipExecutorEmpty"+"\""+":"+"\""+this.skipExecutorEmpty+"\","
		+"\""+"allowExecutorEmpty"+"\""+":"+"\""+this.allowExecutorEmpty+"\","
		+"\""+"allowRefCounts"+"\""+":"+"\""+this.allowRefCounts+"\","
		+"\""+"allowReference"+"\""+":"+"\""+this.allowReference+"\","
		+"\""+"status"+"\""+":"+"\""+this.status+"\","
		+"\""+"skipRules"+"\""+":"+"\""+this.skipRules+"\","
		+"\""+"dateType"+"\""+":"+"\""+this.dateType+"\","
		+"\""+"doneDataVersion"+"\""+":"+"\""+this.doneDataVersion+"\","
		+"\""+"dueTime"+"\""+":"+"\""+this.dueTime+"\""
		+ "}";
	}
	
}
