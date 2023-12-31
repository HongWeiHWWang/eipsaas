package com.hotent.base.util;

import com.hotent.base.id.IdGenerator;

/**
 * 唯一ID获取类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月11日
 */
public class UniqueIdUtil{
	private static IdGenerator idGenerator;
	
	static{
		initIdGenerator();
	}
	
	/**
	 * 初始化ID生成器
	 */
	private static void initIdGenerator(){
		idGenerator = AppUtil.getBean(IdGenerator.class);
		System.out.println(idGenerator);
	}
	
	/**
	 * 
	 * 获取long型的ID.
	 * @return 
	 * Long
	 * @exception 
	 * @since  1.0.0
	 */
	public static Long getUId(){
		return idGenerator.nextId();
	}
	
    /**
     * 获取字符型的ID
     * @return 
     */
    public static String getSuid(){
    	return getUId().toString();
    }
}
