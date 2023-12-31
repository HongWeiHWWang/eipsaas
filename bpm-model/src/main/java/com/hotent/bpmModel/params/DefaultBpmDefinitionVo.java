package com.hotent.bpmModel.params;

import org.springframework.web.multipart.MultipartFile;

import com.hotent.bpm.persistence.model.DefaultBpmDefinition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 流程定义保存对象
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description="流程定义保存对象")
public class DefaultBpmDefinitionVo {
	
	@ApiModelProperty(name="defaultBpmDefinition",notes="流程定义ID", required=true)
	protected DefaultBpmDefinition   defaultBpmDefinition; 
	
	@ApiModelProperty(name="isdeploy",notes="是否发布", required=true)
	protected Boolean  isdeploy; 
	
	@ApiModelProperty(name="isSave",notes="是否保存", required=true)
	protected Boolean  isSave; 
	
	@ApiModelProperty(name="defXml",notes="定义xml", required=true)
	protected String  defXml; 
	
	@ApiModelProperty(name="file",notes="流程定义文件路径")
	protected MultipartFile file;


	public Boolean getIsSave() {
		return isSave;
	}


	public void setIsSave(Boolean isSave) {
		this.isSave = isSave;
	}


	public String getDefXml() {
		return defXml;
	}


	public void setDefXml(String defXml) {
		this.defXml = defXml;
	}


	public MultipartFile getFile() {
		return file;
	}


	public void setFile(MultipartFile file) {
		this.file = file;
	}


	public DefaultBpmDefinition getDefaultBpmDefinition() {
		return defaultBpmDefinition;
	}


	public void setDefaultBpmDefinition(DefaultBpmDefinition defaultBpmDefinition) {
		this.defaultBpmDefinition = defaultBpmDefinition;
	}


	public Boolean getIsdeploy() {
		return isdeploy;
	}


	public void setIsdeploy(Boolean isdeploy) {
		this.isdeploy = isdeploy;
	}
	
	
}