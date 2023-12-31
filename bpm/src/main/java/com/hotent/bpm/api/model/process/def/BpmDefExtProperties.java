package com.hotent.bpm.api.model.process.def;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 流程定义扩展属性。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-21-下午2:10:55
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmDefExtProperties implements Serializable{
	private static final long serialVersionUID = -190683658353381346L;
	/**
	 * 流程标题规则。
	 * map.put("title", bpmDefinition.getName());
	 * map.put("startorName", ContextUtil.getCurrentUser().getFullname() );
	 * map.put("startDate", DateUtil.getCurrentDate());
	 */
    protected String subjectRule="{发起人:startorName}在{发起时间:startDate}发起{流程标题:title}";
    /**
     * 流程描述
     */
    protected String description="";
    /**
     * 发起时通知相关人员类型。
     */
    protected String startNotifyType="mail";
    /**
     * 归档时通知类型。
     */
    protected String archiveNotifyType="mail";
    /**
     * 通知类型。
     */
    protected String notifyType="mail";
    
    /**
     * 跳过第一个节点。
     */
    protected boolean skipFirstNode=true;
    
    /**
     * 第一个节点用户可以选择执行人。
     */
    protected boolean firstNodeUserAssign=false;
    /**
     * 两个节点相同节点执行人跳过。
     */
    protected boolean skipSameUser=true;
    
    /**
     * 允许抄送。
     */
    protected boolean allowCopyTo=false;
    
    /**
     * 允许转办。
     */
    protected boolean allowTransTo=false;
    /**
     * 使用表单主版本。
     */
    protected String useMainForm= "";
    
    /**
     * 允许参考。
     */
    protected boolean allowReference=false;
    
    /**
     * 允许参考的数量。
     */
    protected int allowRefCounts=5;
    
    /**
     * 是否允许执行人为空。
     */
    protected boolean allowExecutorEmpty=false;
    
    /**
     * 执行人为空时跳过此任务。
     */
    protected boolean skipExecutorEmpty=false;
    
    /**
     * 测试状态通知类型。
     */
    protected String testNotifyType="";
    
    /**
     * 测试状态。
     */
    protected String testStatus=BpmDefinition.TEST_STATUS.TEST;
    
    /**
     * 流程状态。
     */
    protected String status=BpmDefinition.STATUS.DEPLOY;
    
    /**
     * 扩展的参数。
     */
    protected List<ExtProperty> extProperty=new ArrayList<ExtProperty>();
    
    /**
     * 跳转规则。
     */
    protected String skipRules="";
    
    /**
     * 计算审批期限的类型   worktime, caltime
     */
 	private String dateType = "caltime";
 	
 	/**
 	 *  节点审批期限 (分钟)
 	 */
 	private int dueTime = 0;
    
 	/**
 	 * 是否显示紧急状态
 	 */
 	private boolean showUrgentState =false;

    /**
     * 传阅已阅是否允许撤回
     */
    private boolean isReadRevoke = false;
    
    /**
     * 是否显示表单留痕
     */
    private boolean showModifyRecord = false;

    private String urgentMailTel; /*人工催办邮件模板*/
    private String urgentSmsTel; /*人工催办短信模板*/
    
    /**
     * 查看已办数据版本
     */
    protected String doneDataVersion= "history";

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

    public String getSubjectRule() {
		return subjectRule;
	}
	public void setSubjectRule(String subjectRule) {
		this.subjectRule = subjectRule;
	}
	public String getDescription() {
		if(description==null) return "";
		return description;
	}
	public void setDescription(String description) {
		
		this.description = description;
	}
	public String getStartNotifyType() {
		if(startNotifyType==null) return "";
		return startNotifyType;
	}
	public void setStartNotifyType(String startNotifyType) {
		
		this.startNotifyType = startNotifyType;
	}
	public String getArchiveNotifyType() {
		if(archiveNotifyType==null) return "";
		return archiveNotifyType;
	}
	public void setArchiveNotifyType(String archiveNotifyType) {
		this.archiveNotifyType = archiveNotifyType;
	}
	
	public String getNotifyType() {
		if(notifyType==null) return "mail,inner";
		return  this.notifyType;
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
	public boolean isAllowCopyTo() {
		return allowCopyTo;
	}
	public void setAllowCopyTo(boolean allowCopyTo) {
		this.allowCopyTo = allowCopyTo;
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
	public List<ExtProperty> getExtProperty() {
		return extProperty;
	}
	public void setExtProperty(List<ExtProperty> extProperty) {
		this.extProperty = extProperty;
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
	
	
	public void addExtProperty(String name,String value){
		this.extProperty.add(new ExtProperty(name, value));
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
	public boolean isShowUrgentState() {
		return showUrgentState;
	}
	public void setShowUrgentState(boolean showUrgentState) {
		this.showUrgentState = showUrgentState;
	}

    public boolean isReadRevoke() {
        return isReadRevoke;
    }

    public void setReadRevoke(boolean readRevoke) {
        isReadRevoke = readRevoke;
    }

	public boolean isShowModifyRecord() {
		return showModifyRecord;
	}

	public void setShowModifyRecord(boolean showModifyRecord) {
		this.showModifyRecord = showModifyRecord;
	}

	public String getDoneDataVersion() {
		return doneDataVersion;
	}

	public void setDoneDataVersion(String doneDataVersion) {
		this.doneDataVersion = doneDataVersion;
	}
	
}
