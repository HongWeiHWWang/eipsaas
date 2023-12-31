package com.hotent.uc.model;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.util.EncryptUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

 /**
 * 
 * <pre> 
 * 描述：portal_sys_properties 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-07-28 09:19:53
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("UC_PROPERTIES")
@ApiModel(description="参数表")
public class Properties extends UcBaseModel<Properties>{
	
	private static final long serialVersionUID = -7938018912020183171L;

	/**
	* 主键
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="id")
	protected String id; 
	
	/**
	* 参数名
	*/
	@TableField("NAME_")
	@ApiModelProperty(name="name",notes="参数名")
	protected String name; 
	
	/**
	* 别名
	*/
	@TableField("CODE_")
	@ApiModelProperty(name="code",notes="别名")
	protected String code; 
	
	/**
	* 分组
	*/
	@TableField("GROUP_")
	@ApiModelProperty(name="group",notes="分组")
	protected String group; 
	
	/**
	* 参数值
	*/
	@TableField("VALUE_")
	@ApiModelProperty(name="value",notes="参数值")
	protected String value; 
	
	
	/**
	 * 分类使用逗号进行分割。
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="categorys",notes="分类")
	protected List<String> categorys=new ArrayList<String>();
	
	
	/**
	 * 值是否加密存储。
	 * 在编辑的时候不显示具体的值。
	 */
	@TableField("ENCRYPT_")
	@ApiModelProperty(name="encrypt",notes="值是否加密存储")
	protected int encrypt=0;
	
	/**
	 * 描述。
	 */
	@TableField("DESCRIPTION_")
	@ApiModelProperty(name="description",notes="描述")
	protected String description="";
	
	
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
	

	/**
	 * 返回 别名
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
		.append("code", this.code) 
		.append("group", this.group) 
		.append("value", this.value) 
		.append("description", this.description) 
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}
}