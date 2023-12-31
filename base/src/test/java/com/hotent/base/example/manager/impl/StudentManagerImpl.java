package com.hotent.base.example.manager.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.CachePut;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.cache.annotation.FirstCache;
import com.hotent.base.example.dao.StudentDao;
import com.hotent.base.example.manager.StudentManager;
import com.hotent.base.example.model.Student;
import com.hotent.base.manager.impl.BaseManagerImpl;

/**
 * student Manager
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
@Service
public class StudentManagerImpl extends BaseManagerImpl<StudentDao, Student> implements StudentManager{
	@Override
	public List<Student> getByName(String name) {
		List<Student> all = baseMapper.getAll(Wrappers.<Student>lambdaQuery()
				.eq(Student::getName, name));
		return all;
	}

	@Cacheable(value = "student:name", key = "#name", firstCache = @FirstCache(expireTime = 100))
	public Student getSingleByName(String name) {
		return baseMapper.getSingleByName(name);
	}

	@CachePut(value = "student:name", key = "#name", firstCache = @FirstCache(expireTime = 100))
	public Student putStudentInCache(String name) {
		return new Student("1", name, LocalDate.now(), new Short("1"), String.format("我叫%s，小学一年级学生", name));
	}
	
	@CacheEvict(value = "student:name", key = "#name")
	public void removeStudentFromCache(String name) {}
}
