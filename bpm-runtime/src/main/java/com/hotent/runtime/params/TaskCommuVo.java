package com.hotent.runtime.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.bpm.persistence.model.BpmCommuReceiver;
import com.hotent.bpm.persistence.model.BpmTaskCommu;

/**
 * 沟通反馈vo
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="表单数据")
public class TaskCommuVo {

	@ApiModelProperty(name="taskCommu",notes="沟通任务实体")
	private BpmTaskCommu taskCommu;
	
	@ApiModelProperty(name="commuReceivers",notes="消息回复")
	private List<BpmCommuReceiver> commuReceivers;
	
	public TaskCommuVo(){}
	
	public TaskCommuVo(BpmTaskCommu taskCommu,List<BpmCommuReceiver> commuReceivers){
		this.taskCommu = taskCommu;
		this.commuReceivers = commuReceivers;
	}

	public BpmTaskCommu getTaskCommu() {
		return taskCommu;
	}

	public void setTaskCommu(BpmTaskCommu taskCommu) {
		this.taskCommu = taskCommu;
	}

	public List<BpmCommuReceiver> getCommuReceivers() {
		return commuReceivers;
	}

	public void setCommuReceivers(List<BpmCommuReceiver> commuReceivers) {
		this.commuReceivers = commuReceivers;
	}
}
