package com.hotent.uc.params.properties;

import com.hotent.uc.manager.PropertiesService;
import com.hotent.uc.model.Properties;
import com.hotent.uc.util.OperateLogUtil;
import com.hotent.uc.util.UpdateCompare;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统参数vo类
 * @author zhangxw
 *
 */
@ApiModel
public class PropertiesVo implements UpdateCompare {

	
	@ApiModelProperty(name="name",notes="参数名称",required=true)
	protected String name; 
	
	@ApiModelProperty(name="code",notes="参数编码",required=true)
	protected String code; 
	
	@ApiModelProperty(name="group",notes="参数分组",required=true)
	protected String group; 
	
	@ApiModelProperty(name="value",notes="参数值",required=true)
	protected String value; 
	
	@ApiModelProperty(name="description",notes="参数描述")
	private String description;
	
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

	
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static Properties parse(PropertiesVo propertiesVo) throws Exception{
		Properties properties = new Properties();
		properties.setName(propertiesVo.getName());
		properties.setCode(propertiesVo.getCode());
		properties.setGroup(propertiesVo.getGroup());
		properties.setValue(propertiesVo.getValue());
		properties.setDescription(propertiesVo.getDescription());
		return properties;
	}
	@Override
	public String compare() throws Exception {
		PropertiesService service =	AppUtil.getBean(PropertiesService.class);
		Properties oldVo= service.getPropertiesByCode(this.code);
		return OperateLogUtil.compare(this,changeVo(oldVo));
	}


	public PropertiesVo changeVo(Properties properties) {
		PropertiesVo newVo=new PropertiesVo();
		if (BeanUtils.isEmpty(newVo)) return newVo;
		newVo.setCode(properties.getCode());
		newVo.setName(properties.getName());
		newVo.setGroup(properties.getGroup());
		newVo.setValue(properties.getValue());
		newVo.setDescription(properties.getDescription());
		return newVo;
	}
}
