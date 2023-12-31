package com.hotent.uc.model;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.util.EncryptUtil;

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
public class Properties extends UcBaseModel{
	
	private static final long serialVersionUID = -7938018912020183171L;

	/**
	* 主键
	*/
	protected String id; 
	
	/**
	* 参数名
	*/
	protected String name; 
	
	/**
	* 别名
	*/
	protected String code; 
	
	/**
	* 分组
	*/
	protected String group; 
	
	/**
	* 参数值
	*/
	protected String value; 
	
	protected LocalDateTime createTime;
	
	/**
	 * 分类使用逗号进行分割。
	 */
	protected List<String> categorys=new ArrayList<String>();
	
	
	/**
	 * 值是否加密存储。
	 * 在编辑的时候不显示具体的值。
	 */
	protected int encrypt=0;
	
	/**
	 * 描述。
	 */
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
		.append("createTime", this.createTime) 
		.append("updateTime",this.updateTime)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}
}