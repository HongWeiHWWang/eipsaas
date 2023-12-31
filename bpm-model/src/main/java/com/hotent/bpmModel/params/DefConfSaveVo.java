package com.hotent.bpmModel.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程配置保存对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "流程配置保存对象")
public class DefConfSaveVo {

	@ApiModelProperty(name = "defId", notes = "流程定义id")
	protected String defId;

	@ApiModelProperty(name = "topDefKey", notes = "父流程定义id")
	protected String topDefKey;

	@ApiModelProperty(name = "defSettingJson", notes = "流程设置json")
	protected String defSettingJson;

	@ApiModelProperty(name = "userJson", notes = "审批人员设置json")
	protected String userJson;

    @ApiModelProperty(name = "userReadJson", notes = "传阅人员设置json")
    protected String userReadJson;

	@ApiModelProperty(name = "restfulJson", notes = "restful设置json")
	protected String restfulJson;

    @ApiModelProperty(name = "rev", notes = "流程定义关联锁")
    protected Integer rev;


    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getTopDefKey() {
		return topDefKey;
	}

	public void setTopDefKey(String topDefKey) {
		this.topDefKey = topDefKey;
	}

	public String getDefSettingJson() {
		return defSettingJson;
	}

	public void setDefSettingJson(String defSettingJson) {
		this.defSettingJson = defSettingJson;
	}

	public String getUserJson() {
		return userJson;
	}

	public void setUserJson(String userJson) {
		this.userJson = userJson;
	}

	public String getRestfulJson() {
		return restfulJson;
	}

	public void setRestfulJson(String restfulJson) {
		this.restfulJson = restfulJson;
	}

    public String getUserReadJson() {
        return userReadJson;
    }

    public void setUserReadJson(String userReadJson) {
        this.userReadJson = userReadJson;
    }
}