package com.hotent.form.datatrans;


import java.util.HashMap;
import java.util.Map;

/**
 * <pre> 
 * 描述：转换器类型注册容器。
 * 构建组：x5-bpmx-platform
 * 作者：lyj
 * 邮箱:liyj@jee-soft.cn
 * 日期:2014-7-22-上午9:35:27
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@SuppressWarnings("rawtypes")
public class TypeConvertConfig {
	
	
	private Map<Class,ITypeConvert> map=new HashMap<Class, ITypeConvert>();
	
	public void regeisterConvert(Class cls,ITypeConvert convert){
		map.put(cls, convert);
	}
	
	public Map<Class,ITypeConvert> getConverts(){
		return map;
	}
	
}
