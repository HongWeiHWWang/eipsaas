package com.hotent.uc.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
* uc_pwd_strategy
* <pre> 
* 描述：uc_pwd_strategy 实体对象
* 构建组：x7
* 作者:tangxin
* 邮箱:tangxin@jee-soft.cn
* 日期:2020-05-19 10:52:36
* 版权：广州宏天软件有限公司
* </pre>
*/
@ApiModel(value = "PwdStrategy",description = "密码策略")
@TableName("uc_pwd_strategy")
public class PwdStrategy extends BaseModel<PwdStrategy>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="ID_")
	@TableId("ID_")
	protected String id;
	
	@ApiModelProperty(value="初始化密码")
	@TableField("INIT_PWD_")
	protected String initPwd;
	
	@ApiModelProperty(value="密码策略")
	@TableField("PWD_RULE_")
	protected Short pwdRule;
	
	@ApiModelProperty(value="密码长度")
	@TableField("PWD_LENGTH_")
	protected Short pwdLength;
	
	@ApiModelProperty(value="密码可用时长")
	@TableField("DURATION_")
	protected Short duration;
	
	@ApiModelProperty(value="启用策略	0:停用，1:启用")
	@TableField("ENABLE_")
	protected Short enable = 1;

	public PwdStrategy() {
	}

	public PwdStrategy(String initPwd, Short pwdRule, Short pwdLength, Short duration, Short enable) {
		this.initPwd = initPwd;
		this.pwdRule = pwdRule;
		this.pwdLength = pwdLength;
		this.duration = duration;
		this.enable = enable;
	}

	public PwdStrategy(String id, String initPwd, Short pwdRule, Short pwdLength, Short duration, Short enable) {
		this.id = id;
		this.initPwd = initPwd;
		this.pwdRule = pwdRule;
		this.pwdLength = pwdLength;
		this.duration = duration;
		this.enable = enable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInitPwd() {
		return initPwd;
	}

	public void setInitPwd(String initPwd) {
		this.initPwd = initPwd;
	}

	public Short getPwdRule() {
		return pwdRule;
	}

	public void setPwdRule(Short pwdRule) {
		this.pwdRule = pwdRule;
	}

	public Short getPwdLength() {
		return pwdLength;
	}

	public void setPwdLength(Short pwdLength) {
		this.pwdLength = pwdLength;
	}

	public Short getDuration() {
		return duration;
	}

	public void setDuration(Short duration) {
		this.duration = duration;
	}

	public Short getEnable() {
		return enable;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}
	
}
