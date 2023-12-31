package com.hotent.bo.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

 /**
  * BO数据关联表
  * <pre>
  * 用于多对多的情况,实体对象
  * </pre>
  * 
  * @company 广州宏天软件股份有限公司
  * @author heyifan
  * @email heyf@jee-soft.cn
  * @date 2018年4月12日
  */
@TableName("form_bo_data_relation")
@ApiModel("bo数据关联关系")
public class BoDataRel extends BaseModel<BoDataRel>{
	private static final long serialVersionUID = 4633816793042530729L;
	/**
	* 主键
	*/
	@TableId("id_")
	@ApiModelProperty("主键")
	protected String id; 
	
	/**
	* 主表键数据
	*/
	@TableField("pk_")
	@ApiModelProperty("主表键数据")
	protected String pk; 
	
	/**
	* 外键数据
	*/
	@TableField("fk_")
	@ApiModelProperty("外键数据")
	protected String fk; 
	
	/**
	* 数字主键
	*/
	@TableField("pk_num_")
	@ApiModelProperty("数字主键")
	protected Long pkNum; 
	
	/**
	* 外键数字数据
	*/
	@TableField("fk_num_")
	@ApiModelProperty("外键数字数据")
	protected Long fkNum; 
	
	/**
	* 子实体名称
	*/
	@TableField("sub_bo_name")
	@ApiModelProperty("子实体名称")
	protected String subBoName; 
	
	
	public BoDataRel(){}
	
	public BoDataRel(String id,String pk,String fk,String subBoName){
		this.id=id;
		this.pk=pk;
		this.fk=fk;
		this.subBoName=subBoName;
	}
	
	public BoDataRel(String id,Long pk,Long fk,String subBoName){
		this.id=id;
		this.pkNum=pk;
		this.fkNum=fk;
		this.subBoName=subBoName;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setPk(String pk) {
		this.pk = pk;
	}
	
	/**
	 * 返回 主表键数据
	 * @return
	 */
	public String getPk() {
		return this.pk;
	}
	
	public void setFk(String fk) {
		this.fk = fk;
	}
	
	/**
	 * 返回 外键数据
	 * @return
	 */
	public String getFk() {
		return this.fk;
	}
	
	public void setPkNum(Long pkNum) {
		this.pkNum = pkNum;
	}
	
	/**
	 * 返回 数字主键
	 * @return
	 */
	public Long getPkNum() {
		return this.pkNum;
	}
	
	public void setFkNum(Long fkNum) {
		this.fkNum = fkNum;
	}
	
	/**
	 * 返回 外键数字数据
	 * @return
	 */
	public Long getFkNum() {
		return this.fkNum;
	}
	
	public void setSubBoName(String subBoName) {
		this.subBoName = subBoName;
	}
	
	/**
	 * 返回 子实体名称
	 * @return
	 */
	public String getSubBoName() {
		return this.subBoName;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("pk", this.pk) 
		.append("fk", this.fk) 
		.append("pkNum", this.pkNum) 
		.append("fkNum", this.fkNum) 
		.append("subBoName", this.subBoName) 
		.toString();
	}
}