package com.hotent.bpm.persistence.model;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


/**
 * 对象功能:@名称：BPM_DEF_DATA 【流程定义大数据值】 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2013-12-13 16:17:48
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BpmDefData")
@TableName("bpm_def_data")
public class BpmDefData extends BaseModel<BpmDefData> implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7714997895441809318L;
	@TableId("id_")
	protected String  id; /*流程定义ID*/
	
	@TableField("def_xml_")
	@XmlElement(name="defXml")
	protected String  defXml; /*流程定义XML*/
	
	@TableField("bpmn_xml_")
	@XmlElement(name="bpmnXml")
	protected String  bpmnXml; /*流程定义BPMN格式XML*/
	
	@TableField("def_json_")
	@XmlElement(name="defJson")
	protected String  defJson; /*流程定义的json*/
	
	public BpmDefData(){
		defXml = "";
		bpmnXml = "";
	}
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	
	
	
	
	public void setDefXml(String defXml) 
	{
		this.defXml = defXml;
	}
	/**
	 * 返回 流程定义XML
	 * @return
	 */
	public String getDefXml() 
	{
		return this.defXml;
	}
	public void setBpmnXml(String bpmnXml) 
	{
		this.bpmnXml = bpmnXml;
	}
	/**
	 * 返回 流程定义BPMN格式XML
	 * @return
	 */
	public String getBpmnXml() 
	{
		return this.bpmnXml;
	}
	
	
	public String getDefJson() {
		return defJson;
	}
	public void setDefJson(String defJson) {
		this.defJson = defJson;
	}
	
	@Override
	public String toString() {
		return "BpmDefData [id=" + id + ", defXml=" + defXml + ", bpmnXml="
				+ bpmnXml + ", defJson=" + defJson + "]";
	}
	public Object clone() {
		BpmDefData obj=null;
		try{
			obj=(BpmDefData)super.clone();			
		}catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}			
}