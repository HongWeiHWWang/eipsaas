package com.hotent.bpmModel.params;

import com.hotent.bpm.api.model.process.def.BpmDefExtProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程其他属性保存对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "流程其他属性保存对象")
public class DefPropSaveVo {

	@ApiModelProperty(name = "bpmProp", notes = "流程的其他属性")
	protected BpmDefExtProperties bpmProp;

	@ApiModelProperty(name = "description", notes = "描述")
	protected String description;

	@ApiModelProperty(name = "defId", notes = "流程定义id")
	protected String defId;

    @ApiModelProperty(name = "rev", notes = "流程定义关联锁")
    protected Integer rev;


    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

	public BpmDefExtProperties getBpmProp() {
		return bpmProp;
	}

	public void setBpmProp(BpmDefExtProperties bpmProp) {
		this.bpmProp = bpmProp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

}