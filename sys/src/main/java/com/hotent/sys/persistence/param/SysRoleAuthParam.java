package com.hotent.sys.persistence.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;


/**
* 角色权限配置
* <pre> 
* 描述：角色权限配置 实体对象
* 构建组：x7
* 作者:liyg
* 邮箱:liygui@jee-soft.cn
* 日期:2018-06-29 14:27:46
* 版权：广州宏天软件有限公司
* </pre>
*/
@ApiModel(value = "SysRoleAuthParam",description = "角色授权请求参数") 
public class SysRoleAuthParam {
	
	@ApiModelProperty(name="roleAlias", notes="角色别名",required=true)
	private String roleAlias;
	
	@ApiModelProperty(name="arrMenuAlias", notes="菜单资源别名")
	private List<String> arrMenuAlias;
	
	@ApiModelProperty(name="arrMethodAlias", notes="请求方法别名")
	private List<String> arrMethodAlias;
	
	@ApiModelProperty(name="dataPermission", notes="数据权限设置")
	private Map<String,String> dataPermission;

	public String getRoleAlias() {
		return roleAlias;
	}

	public void setRoleAlias(String roleAlias) {
		this.roleAlias = roleAlias;
	}

	public List<String> getArrMenuAlias() {
		return arrMenuAlias;
	}

	public void setArrMenuAlias(List<String> arrMenuAlias) {
		this.arrMenuAlias = arrMenuAlias;
	}

	public List<String> getArrMethodAlias() {
		return arrMethodAlias;
	}

	public void setArrMethodAlias(List<String> arrMethodAlias) {
		this.arrMethodAlias = arrMethodAlias;
	}

	public Map<String, String> getDataPermission() {
		return dataPermission;
	}

	public void setDataPermission(Map<String, String> dataPermission) {
		this.dataPermission = dataPermission;
	}

	
}
