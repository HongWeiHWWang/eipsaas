package com.hotent.base.test.cache;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import javax.annotation.Resource;
import org.junit.Test;
import com.hotent.base.BaseTestCase;
import com.hotent.base.example.manager.StudentManager;
import com.hotent.base.example.model.Student;
import com.hotent.base.util.BeanUtils;

/**
 * 测试缓存的注解
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月17日
 */
public class CacheAspectTest extends BaseTestCase{
	@Resource
	StudentManager studentManager;
	
	@Test
	public void testCRUD() {
		String name = "张三";
		Student s1 = studentManager.getSingleByName(name);
		assertTrue(BeanUtils.isEmpty(s1));
		
		Student s2 = studentManager.putStudentInCache(name);
		
		Student s3 = studentManager.getSingleByName(name);
		assertEquals(s3.getId(), s2.getId());
		
		studentManager.removeStudentFromCache(name);
		
		assertTrue(BeanUtils.isEmpty(studentManager.getSingleByName(name)));
	}
}
