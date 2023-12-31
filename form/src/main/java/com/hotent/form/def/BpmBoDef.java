package com.hotent.form.def;


import java.util.ArrayList;
import java.util.List;

import com.hotent.form.extmodel.ProcBoDef;


/**
 * 流程的BO定义。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014年8月21日-下午2:44:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmBoDef {
	
	private String boSaveMode="";
	
	private List<ProcBoDef> boDefs=new ArrayList<ProcBoDef>();

	public String getBoSaveMode() {
		return boSaveMode;
	}

	public void setBoSaveMode(String boSaveMode) {
		this.boSaveMode = boSaveMode;
	}

	public List<ProcBoDef> getBoDefs() {
		for(ProcBoDef def:boDefs){
			def.setSaveMode(this.boSaveMode);
		}
		return boDefs;
	}

	public void setBoDefs(List<ProcBoDef> boDefs) {
		this.boDefs = boDefs;
	}
	
	public void addBoDefs(ProcBoDef bodef) {
		this.boDefs.add(bodef);
	}
	
	
	
	

}
