package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;

/**
 * 指定节点的审批意见参数类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(value="指定节点的审批意见参数")
public class NodeOpinionVo {
	
	@ApiModelProperty(name="hasOpinion",notes="是否有审批意见")
	private boolean hasOpinion;
	
	@ApiModelProperty(name="data",notes="审批意见列表")
	private List<BpmTaskOpinion> data;
	
	public NodeOpinionVo(){}
	
	public NodeOpinionVo(boolean hasOpinion, List<BpmTaskOpinion> data){
		this.hasOpinion = hasOpinion;
		this.data = data;
	}

	public boolean isHasOpinion() {
		return hasOpinion;
	}

	public void setHasOpinion(boolean hasOpinion) {
		this.hasOpinion = hasOpinion;
	}

	public List<BpmTaskOpinion> getData() {
		return data;
	}

	public void setData(List<BpmTaskOpinion> data) {
		this.data = data;
	}
	
}
