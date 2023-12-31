package com.hotent.form.datatrans;


/**
 * <pre> 
 * 描述：数据对象转换器。
 * 用户可以自行设计转换器。
 * 构建组：x5-bpmx-platform
 * 作者：lyj
 * 邮箱:liyj@jee-soft.cn
 * 日期:2014-7-22-上午9:35:00
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface ITypeConvert {
	
	Object processValue(Object obj);

}
