package com.hotent.base.example.manager;

import java.util.List;

import com.hotent.base.example.model.Student;
import com.hotent.base.manager.BaseManager;

/**
 * student Manager
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
public interface StudentManager extends BaseManager<Student>{
	/**
	 * 通过名称查询
	 * @param name	名称
	 * @return
	 */
	List<Student> getByName(String name);
	
	/**
	 * 通过姓名获取单条数据
	 * <p>该方法添加了缓存注解</p>
	 * @param name
	 * @return
	 */
	Student getSingleByName(String name);
	
	/**
	 * 将指定姓名的数据放入缓存
	 * 
	 * @param name
	 * @return
	 */
	Student putStudentInCache(String name);
	
	/**
	 * 从缓存中删除指定姓名的数据
	 * @param name
	 */
	void removeStudentFromCache(String name);
}
