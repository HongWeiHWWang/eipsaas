package com.hotent.table.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Set;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.junit.Test;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.util.AppUtil;
import com.hotent.table.TableBaseTest;
import com.hotent.table.operator.ITableOperator;

public class TableOprationTest extends TableBaseTest{
	@Resource
	ITableOperator tableOperator;
	@Resource
	DatabaseContext databaseContext;
	
	//@Test	
	public void testCreateTable(){
		boolean tableExist = tableOperator.isTableExist("ex_student");
		assertTrue(tableExist);
		System.out.println(tableOperator);
		DynamicRoutingDataSource ds = AppUtil.getBean(DynamicRoutingDataSource.class);
	    Set<String> keySet = ds.getCurrentDataSources().keySet();
	    keySet.forEach(i -> {
	    	System.out.println(i);
	    	DataSource dataSource = ds.getDataSource(i);
	    	int hashCode = dataSource.hashCode();
	    	System.out.println(hashCode);
	    });
	}
	
	@Test
	public void testSwitchDB() {
		String dbType = databaseContext.getDbType();
		assertTrue("mysql".equals(dbType));
		boolean tableExist = tableOperator.isTableExist("ex_student");
		assertTrue(tableExist);
		try(DatabaseSwitchResult dResult = databaseContext.setDataSource("local")){
			String dbType2 = dResult.getDbType();
			assertTrue("mysql".equals(dbType2));
			boolean tableStillExist = tableOperator.isTableExist("ex_student");
			assertFalse(tableStillExist);
		}
		catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
		tableExist = tableOperator.isTableExist("ex_student");
		assertTrue(tableExist);
	}
}
