package com.hotent.portal.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysIndexLayout;
import com.hotent.portal.model.SysIndexMyLayout;

/**
 * 我的首页布局
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年7月7日
 */
@ApiModel(value="我的首页布局")
public class MyLayoutVo {

	@ApiModelProperty(name="indexLayoutList",notes="系统首页布局实体列表")
	private List<SysIndexLayout> indexLayoutList;
	
	@ApiModelProperty(name="columnMap",notes="展示的布局")
	private Map<String,List<SysIndexColumn>> columnMap;
	
	@ApiModelProperty(name="sysIndexMyLayout",notes="当前的布局")
	private SysIndexMyLayout sysIndexMyLayout;

	public List<SysIndexLayout> getIndexLayoutList() {
		return indexLayoutList;
	}
	
	public MyLayoutVo(){}
	
	public MyLayoutVo(List<SysIndexLayout> indexLayoutList,Map<String,List<SysIndexColumn>> columnMap,  SysIndexMyLayout sysIndexMyLayout){
		this.indexLayoutList = indexLayoutList;
		this.columnMap = columnMap;
		this.sysIndexMyLayout = sysIndexMyLayout;
	}	

	public void setIndexLayoutList(List<SysIndexLayout> indexLayoutList) {
		this.indexLayoutList = indexLayoutList;
	}

	public Map<String, List<SysIndexColumn>> getColumnMap() {
		return columnMap;
	}

	public void setColumnMap(Map<String, List<SysIndexColumn>> columnMap) {
		this.columnMap = columnMap;
	}

	public SysIndexMyLayout getSysIndexMyLayout() {
		return sysIndexMyLayout;
	}

	public void setSysIndexMyLayout(SysIndexMyLayout sysIndexMyLayout) {
		this.sysIndexMyLayout = sysIndexMyLayout;
	}

}
