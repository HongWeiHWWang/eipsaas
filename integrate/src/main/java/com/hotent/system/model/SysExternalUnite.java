package com.hotent.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@TableName("portal_sys_external_unite")
@ApiModel(description = "附件分类信息")
public class SysExternalUnite extends AutoFillModel<SysExternalUnite> {


    @ApiModelProperty(name="id", notes="主键")
    @TableId("id_")
    protected String id;

    @ApiModelProperty("企业名称")
    @TableField("CORP_NAME_")
    protected String corpName;

    @ApiModelProperty("企业id")
    @TableField("CORP_ID_")
    protected String corpId;

    @ApiModelProperty("企业secret")
    @TableField("CORP_SECRET_")
    protected String corpSecret;

    @ApiModelProperty("集成类型")
    @TableField("TYPE_")
    protected String type;



    @ApiModelProperty("本系统地址")
    @TableField("BASE_URL_")
    protected String baseUrl;

    @ApiModelProperty("应用id")
    @TableField("AGENT_ID_")
    protected String agentId;

    @ApiModelProperty("应用key")
    @TableField("AGENT_KEY_")
    protected String agentKey;

    @ApiModelProperty("菜单名称")
    @TableField("MENU_NAME_")
    protected String menuName;

    @ApiModelProperty("应用secret")
    @TableField("AGENT_SECRET_")
    protected String agentSecret;


    @ApiModelProperty("菜单url")
    @TableField("MENU_URL_")
    protected String menuUrl;
    
    @ApiModelProperty("公众号模板消息id")
    @TableField("TEMP_MSG_ID_")
    protected String tempMsgId;

    @ApiModelProperty("是否发布")
    @TableField(exist=false)
    protected String isPublish;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentKey() {
        return agentKey;
    }

    public void setAgentKey(String agentKey) {
        this.agentKey = agentKey;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getAgentSecret() {
        return agentSecret;
    }

    public void setAgentSecret(String agentSecret) {
        this.agentSecret = agentSecret;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getTempMsgId() {
		return tempMsgId;
	}

	public void setTempMsgId(String tempMsgId) {
		this.tempMsgId = tempMsgId;
	}

	public String getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(String isPublish) {
        this.isPublish = isPublish;
    }
}
