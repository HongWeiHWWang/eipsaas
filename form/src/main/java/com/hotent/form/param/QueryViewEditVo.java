package com.hotent.form.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.form.model.QuerySqldef;
import com.hotent.uc.api.impl.var.IContextVar;

/**
 * 
 * 
 * <pre>
 * 描述：自定义sql视图编辑vo
 * 作者：zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2019年7月31日 上午10:31:55 
 * 版权：广州宏天软件股份有限公司版权所有
 * </pre>
 */
@ApiModel(description="自定义sql视图编辑vo")
public class QueryViewEditVo {
	
	@ApiModelProperty(name="viewName", notes="视图名称")
	private String viewName;
	
	@ApiModelProperty(name="sqldef", notes="自定义sql定义")
	private QuerySqldef sqldef;

	@ApiModelProperty(name="comVarList", notes="当前用户相关变量")
	private List<IContextVar> comVarList;
	
	public QueryViewEditVo(){}
	
	public QueryViewEditVo(String viewName,QuerySqldef sqldef,List<IContextVar> comVarList){
		this.viewName = viewName;
		this.sqldef = sqldef;
		this.comVarList = comVarList;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public QuerySqldef getSqldef() {
		return sqldef;
	}

	public void setSqldef(QuerySqldef sqldef) {
		this.sqldef = sqldef;
	}

	public List<IContextVar> getComVarList() {
		return comVarList;
	}

	public void setComVarList(List<IContextVar> comVarList) {
		this.comVarList = comVarList;
	}
	
}
