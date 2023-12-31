package com.hotent.bpm.plugin.task.userassign.context;

import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.constant.LogicType;

/**
 * 根据传入的人员类型。获取该类型的meijv
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月25日
 */
public class EnumTypeProcessor <E>  {
	
    String TypeKey;
	
	E typeClass;  
	
	public String getTypeName() {
		return TypeKey;
	}

	public void setTypeName(String typeName) {
		TypeKey = typeName;
	}

	public E getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(E typeClass) {
		this.typeClass = typeClass;
	}

	public  EnumTypeProcessor(){
		if(typeClass instanceof ExtractType){
			this.TypeKey= ((ExtractType) typeClass).getKey();
		}
		else if(typeClass instanceof LogicType){
			this.TypeKey= ((LogicType) typeClass).getKey();
		}
		this.TypeKey= "";
	}
}
