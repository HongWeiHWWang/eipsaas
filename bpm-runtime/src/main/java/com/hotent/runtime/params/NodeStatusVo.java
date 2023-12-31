package com.hotent.runtime.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.uc.api.model.IUser;

/**
 * 节点审批状态 vo
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(value="节点审批状态")
public class NodeStatusVo {

	@ApiModelProperty(name="userList",notes="人员信息")
	private List<IUser> userList;
	
	@ApiModelProperty(name="bpmTaskOpinions",notes="流程审批意见")
	private List<BpmTaskOpinion> bpmTaskOpinions;
	
	public NodeStatusVo(){}
	
	public NodeStatusVo(List<IUser> userList, List<BpmTaskOpinion> bpmTaskOpinions){
		this.userList = userList;
		this.bpmTaskOpinions = bpmTaskOpinions;
	}

	public List<IUser> getUserList() {
		return userList;
	}

	public void setUserList(List<IUser> userList) {
		this.userList = userList;
	}

	public List<BpmTaskOpinion> getBpmTaskOpinions() {
		return bpmTaskOpinions;
	}

	public void setBpmTaskOpinions(List<BpmTaskOpinion> bpmTaskOpinions) {
		this.bpmTaskOpinions = bpmTaskOpinions;
	}

}
