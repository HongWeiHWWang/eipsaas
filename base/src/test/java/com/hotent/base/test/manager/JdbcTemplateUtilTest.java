package com.hotent.base.test.manager;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.jdbc.core.RowMapper;

import com.hotent.base.BaseTestCase;
import com.hotent.base.dao.CommonDao;
import com.hotent.base.example.model.Student;
import com.hotent.base.exception.SystemException;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.util.JdbcTemplateUtil;
import com.hotent.base.util.SQLUtil;

/**
 * 通用查询测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class JdbcTemplateUtilTest extends BaseTestCase {
	@Resource
	CommonManager commonManager;
	
	@Resource
	CommonManager studentManager;
	@Resource
	CommonDao commonDao;

	@Test
	public void testInsert() throws SystemException{
		String dbType = SQLUtil.getDbType();
		assertEquals("h2", dbType);
		for(int i=10;i<210;i++){
			String sql= String.format("insert into ex_student (id_,name_,birthday_,sex_,desc_) values ('%s', '%s', '%s', %s, '%s')", i, "同学" + i, LocalDateTime.now(), i%2, "我是小学一年级学生");
			commonManager.execute(sql);
		}
		String sql = String.format("select id_,name_,birthday_,sex_,desc_ from ex_student");
		PageList<Map<String, Object>> queryForListWithPage2 = JdbcTemplateUtil.queryForListWithPage(sql,  new PageBean(1, 21, true));
		assertEquals(21, queryForListWithPage2.getRows().size());
		PageList<Student> queryForListWithPage = JdbcTemplateUtil.query(sql, new PageBean(2, 5, true), 
				new RowMapper<Student>() {
					@Override
					public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
						Student student = new Student();
						student.setId(rs.getString("id_"));
						student.setName(rs.getString("name_"));
						student.setDesc(rs.getString("desc_"));
						return student;
					}
			
		});
		assertEquals(5, queryForListWithPage.getRows().size());
		
		String whereSql = String.format("select id_,name_,birthday_,sex_,desc_ from ex_student where id_ > ?");
		Object[] args = new Object[1];
		args[0] = 180;
		// 查询id 大于180的记录
		queryForListWithPage = JdbcTemplateUtil.query(whereSql,args, new PageBean(1, 8, true), 
				new RowMapper<Student>() {
					@Override
					public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
						Student student = new Student();
						student.setId(rs.getString("id_"));
						student.setName(rs.getString("name_"));
						student.setDesc(rs.getString("desc_"));
						return student;
					}
			
		});
		assertEquals(8, queryForListWithPage.getRows().size());
		
		
		for (int i = 0; i < 20000; i++) {
			System.out.println(SQLUtil.getDbType() + i );
		}
		
		
	}
}
