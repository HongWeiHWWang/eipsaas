package com.hotent.bo.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * bo定义xml包装类
 * <pre>
 * 此类用于bo的导入导出
 * </pre>
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
@XmlRootElement(name = "bodefs")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoDefXml {
	@XmlElement(name = "bodef", type = BoDef.class)
	private List<BoDef> defList=new ArrayList<BoDef>();

	public List<BoDef> getDefList() {
		return defList;
	}

	public void setDefList(List<BoDef> defList) {
		this.defList = defList;
	}
	
	/**
	 * 添加bo定义列表。
	 * @param def
	 */
	public void addBodef(BoDef def){
		this.defList.add(def);
	}
}
