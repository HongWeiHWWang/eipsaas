package com.hotent.sys.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.util.EncryptUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 流程任务表单 entity对象
 * @company 广州宏天软件股份有限公司
 * @author:liyg
 * @date:2018年6月27日
 */
@ApiModel(description="系统属性实体对象")
@TableName("portal_sys_properties")
public class SysProperties extends AutoFillModel<SysProperties> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name="id", notes="主键")
	@TableId("id")
	protected String id; 
	
	@ApiModelProperty(name="name", notes="参数名")
	@TableField("name")
	@NotBlank
	protected String name; 
	
	@ApiModelProperty(name="alias", notes="别名")
	@TableField("alias")
	protected String alias; 
	
	@ApiModelProperty(name="group", notes="分组")
	@TableField("group_")
	protected String group; 
	
	@ApiModelProperty(name="value", notes="参数值")
	@TableField("value")
	protected String value; 
	
	/**
	 * 值是否加密存储。
	 * 在编辑的时候不显示具体的值。
	 */
	@ApiModelProperty(name="encrypt", notes="值是否加密存储。",allowableValues="1,0")
	@TableField("encrypt")
	protected int encrypt=0;
	
	/**
	 * 描述。
	 */
	@ApiModelProperty(name="description", notes="描述")
	@TableField("description")
	protected String description="";
	
	@TableField(exist=false)
	@ApiModelProperty(name="value", notes="分类使用逗号进行分割")
	protected List<String> categorys=new ArrayList<String>();
	
	
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 参数名
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * 返回 别名
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * 返回 分组
	 * @return
	 */
	public String getGroup() {
		return this.group;
	}
	
	public void setValue(String val) throws Exception {
		this.value = val;
	}
	
	/**
	 * 返回 参数值
	 * @return
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * 如果是加密的情况，将值进行加密。
	 * @throws Exception
	 */
	public void setValByEncrypt() throws Exception{
		if(this.encrypt==1){
			this.value=EncryptUtil.encrypt(this.value);
		}
	}
	
	/**
	 * 返回值时如果是加密情况，则将密码解密。
	 * @return
	 * @throws Exception
	 */
	@ApiIgnore
	@JsonIgnore
	public String getRealVal() {
		if(this.encrypt==1){
			try {
				return EncryptUtil.decrypt(this.value);
			} catch (Exception e) {
				return "";
			}
		}
		return this.value;
	}
	
	
	public List<String> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<String> categorys) {
		this.categorys = categorys;
	}
	
	public int getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(int encrypt) {
		this.encrypt = encrypt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("alias", this.alias) 
		.append("group", this.group) 
		.append("value", this.value) 
		.toString();
	}
}