package com.hotent.bpm.api.model.process.def;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义的坐标。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-4-下午2:05:52
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmDefLayout {
	
	/**
	 * 流程定义布局常量，用于布局缓存。
	 */
	public final static String BpmDefLayout="BpmDefLayout_";

	/**
	 * 流程定义ID
	 */
	private String defId="";
	
	/**
	 * 流程名称。
	 */
	private String name="";
	
	/**
	 * 流程图宽度
	 */
	private float width=0;
	
	/**
	 * 流程图高度
	 */
	private float height=0;
	
	
	private List<BpmNodeLayout> listLayout=new ArrayList<BpmNodeLayout>();


	public String getDefId() {
		return defId;
	}


	public void setDefId(String defId) {
		this.defId = defId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public float getWidth() {
		return width;
	}


	public void setWidth(float width) {
		this.width = width;
	}


	public float getHeight() {
		return height;
	}


	public void setHeight(float height) {
		this.height = height;
	}


	public List<BpmNodeLayout> getListLayout() {
		return listLayout;
	}


	public void setListLayout(List<BpmNodeLayout> listLayout) {
		this.listLayout = listLayout;
	}
	

}
