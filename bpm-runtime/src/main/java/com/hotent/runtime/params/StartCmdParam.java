package com.hotent.runtime.params;

import com.hotent.bpm.model.form.FormCategory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 流程启动时cmd
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(value="流程启动时cmd")
public class StartCmdParam {
	
	@ApiModelProperty(name="proInstId",notes="流程实例id")
	protected String proInstId;
	
	@ApiModelProperty(name="defId",notes="流程定义id ",required=true)
	protected String defId;
	
	@ApiModelProperty(name="isSendNodeUsers",notes="是否由选择人员做为下一节点处理人，默认为0",allowableValues="0,1",required=true)
	protected int isSendNodeUsers = 0;
	
	@ApiModelProperty(name="destination",notes="目标节点")
	protected String destination = "";
	
	@ApiModelProperty(name="nodeUsers",notes="节点执行人 [{nodeId:\"userTask1\",executors:[{id:\"\",type:\"org,user,pos\", name:\"\"},{id:\"\",type:\"org,user,pos\",name:\"\"}]}]",required=true)
	protected String nodeUsers;
	
	@ApiModelProperty(name="busData",notes="流程表单数据",required=true)
	protected String busData;
	
	@ApiModelProperty(name="formType",notes="表单类型")
	protected String formType = FormCategory.INNER.value();
	
	@ApiModelProperty(name="reqValue",notes="变量参数")
	protected String reqValue;

	public String getProInstId() {
		return proInstId;
	}

	public void setProInstId(String proInstId) {
		this.proInstId = proInstId;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public int getIsSendNodeUsers() {
		return isSendNodeUsers;
	}

	public void setIsSendNodeUsers(int isSendNodeUsers) {
		this.isSendNodeUsers = isSendNodeUsers;
	}
	public String getNodeUsers() {
		return nodeUsers;
	}

	public void setNodeUsers(String nodeUsers) {
		this.nodeUsers = nodeUsers;
	}

	public String getBusData() {
		return busData;
	}

	public void setBusData(String busData) {
		this.busData = busData;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getReqValue() {
		return reqValue;
	}

	public void setReqValue(String reqValue) {
		this.reqValue = reqValue;
	}
	
}
