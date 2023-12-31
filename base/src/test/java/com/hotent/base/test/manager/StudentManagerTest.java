package com.hotent.base.test.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.BaseTestCase;
import com.hotent.base.example.manager.StudentManager;
import com.hotent.base.example.model.Student;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;

/**
 * student manager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
public class StudentManagerTest extends BaseTestCase{
	@Resource
	StudentManager studentManager;

	@Test
	public void testCrud(){
		String name = "小明";
		String suid = UniqueIdUtil.getSuid();
		Student student = new Student(suid, name, LocalDate.now(), new Short("1"), String.format("我叫%s，小学一年级学生", name));
		// 添加数据
		studentManager.save(student);
		// 通过主键查询数据
		Student st = studentManager.getById(suid);
		assertEquals(name, st.getName());

		String newDesc = String.format("我叫%s，现在我读小学二年级了", name);
		st.setDesc(newDesc);
		// 更新数据
		studentManager.updateById(st);
		st = studentManager.getById(suid);
		assertEquals(newDesc, st.getDesc());

		String suid2 = UniqueIdUtil.getSuid();
		String name2 = "小红";
		studentManager.save(new Student(suid2, name2, LocalDate.now(), new Short("2"), String.format("我叫%s，小学一年级学生", name2)));
		String suid3 = UniqueIdUtil.getSuid();
		String name3 = "小强";
		studentManager.save(new Student(suid3, name3, LocalDate.now(), new Short("1"), String.format("我叫%s，小学三年级学生", name3)));

		QueryFilter<Student> queryFilter = QueryFilter.<Student>build()
				.withDefaultPage()
				.withParam("dbType", "mysql")
				.withQuery(new QueryField("sex", "1"));
		// 通过通用查询条件查询
		PageList<Student> query = studentManager.query(queryFilter);
		assertEquals(2, query.getTotal());
		
		assertTrue(StringUtil.isNotEmpty(query.getRows().get(0).getTmp()));

		// 通过指定列查询唯一记录
		List<Student> stds = studentManager.getByName(name);
		assertEquals(name, stds.get(0).getName());

		// 查询所有数据
		List<Student> all = studentManager.list();
		assertEquals(3, all.size());
		
		QueryFilter<Student> pageBeanFilter = QueryFilter.<Student>build().withPage(new PageBean());
		
		// 查询所有数据(分页)
		PageList<Student> allByPage = studentManager.query(pageBeanFilter);
		assertEquals(3, allByPage.getRows().size());

		// 通过ID删除数据
		studentManager.removeById(suid);
		List<Student> sstd = studentManager.getByName(name);
		assertTrue(sstd.size()==0);

		List<Student> queryByName = studentManager.getByName(name3);
		assertEquals(1, queryByName.size());
		
		// 通过ID集合批量删除数据
		studentManager.removeByIds(Arrays.asList(new String[]{suid, suid2, suid3}));
		List<Student> nowAll = studentManager.list();
		assertEquals(0, nowAll.size());
	}
}
