package com.hotent.bpm.engine.inst;

import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.cmd.ProcessInstCmd;
import com.hotent.bpm.api.constant.DataType;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * <pre> 
 * 描述：流程实例启动命令参数对象
 * 构建组：x5-bpmx-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-10-上午11:08:48
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefaultProcessInstCmd  extends BaseActionCmd implements ProcessInstCmd {
	/**
	 * 流程BPMN定义ID,bpmnDefId\ProcDefId\flowKey\三值只需要设置一值即可
	 */
	private String bpmnDefId;
	/**
	 * 流程定义ID，bpmnDefId\ProcDefId\flowKey\三值只需要设置一值即可
	 */
	private String procDefId;
	/**
	 * 流程定义Key，bpmnDefId\ProcDefId\flowKey\三值只需要设置一值即可
	 */
	private String flowKey;
	
	/**
	 * 流程实例从标题
	 */
	private String subject;
	/**
	 * 业务主键
	 */
	private String businessKey;
	/**
	 * 业务系统编码
	 */
	private String sysCode;
	
	
	private DataType pkDatatype=DataType.STRING;
	
	private  String urgentState;/*紧急状态*/
	
	private String instId="";

    private Boolean isApproval = false;//是否从待办审批页面点的保存

    /**
     * 支持手机表单。
     */
    @ApiModelProperty(name="supportMobile",notes="是否支持手机表单 0：否，1：是")
    protected int supportMobile=0;

    public int getSupportMobile() {
        return supportMobile;
    }

    public void setSupportMobile(int supportMobile) {
        this.supportMobile = supportMobile;
    }
    public Boolean getApproval() {
        return isApproval;
    }

    public void setApproval(Boolean approval) {
        isApproval = approval;
    }

    public String getBpmnDefId() {
		return bpmnDefId;
	}
	
	public void setBpmnDefId(String bpmnDefId) {
		this.bpmnDefId = bpmnDefId;
	}
	public String getProcDefId() {
		return procDefId;
	}
	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}
	public String getFlowKey() {
		return flowKey;
	}
	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public DataType getPkDatatype() {
		return pkDatatype;
	}

	public void setPkDatatype(DataType pkDatatype) {
		this.pkDatatype = pkDatatype;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	@Override
	public DataType getPkDataType() {
		
		return this.pkDatatype;
	}

	public String getUrgentState() {
		return urgentState;
	}

	public void setUrgentState(String urgentState) {
		this.urgentState = urgentState;
	}

	
}
