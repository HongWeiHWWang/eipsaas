package com.hotent.uc.params.demension;

import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.util.OperateLogUtil;
import com.hotent.uc.util.UpdateCompare;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 维度VO类
 * @author zhangxw
 *
 */
@ApiModel
public class DemensionVo implements UpdateCompare {

	@ApiModelProperty(name="name",notes="维度名称",required=true)
	private String name;
	
	@ApiModelProperty(name="code",notes="维度编码",required=true)
	private String code;
	
	
	@ApiModelProperty(name="description",notes="维度描述")
	private String description;
	
	@ApiModelProperty(name="isDefault",notes="是否默认 0：否；1：是（默认为否）")
	private Integer isDefault;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
	@Override
	public String compare() throws Exception {
	    DemensionManager service =	AppUtil.getBean(DemensionManager.class);
	    Demension oldVo=service.getByCode(this.code);
		return OperateLogUtil.compare(this,changeVo(oldVo));
	}


	public DemensionVo changeVo(Demension oldVo) {
		DemensionVo newVo=new DemensionVo();
		if (BeanUtils.isEmpty(newVo)) return newVo;
		newVo.setCode(oldVo.getDemCode());;
		newVo.setDescription(oldVo.getDemDesc());
		newVo.setIsDefault(oldVo.getIsDefault());
		newVo.setName(oldVo.getDemName());
		return newVo;
	}
	
}
