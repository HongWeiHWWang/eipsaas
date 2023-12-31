package com.hotent.base.example.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.base.example.model.Student;

/**
 * student dao
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
public interface StudentDao extends BaseMapper<Student>{
	@Select("select * from ex_student ${ew.customSqlSegment}")
	List<Student> getAll(@Param(Constants.WRAPPER) Wrapper<Student> wrapper);
	
	/**
	 * 通过姓名获取单条记录
	 * @param name
	 * @return
	 */
	Student getSingleByName(@Param("name") String name);
}
