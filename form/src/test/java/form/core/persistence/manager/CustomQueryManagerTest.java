/**
 * 
 */
package form.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageList;
import com.hotent.form.model.CustomQuery;
import com.hotent.form.persistence.manager.CustomQueryManager;

import form.core.FormTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月12日
 */
@SuppressWarnings("rawtypes")
public class CustomQueryManagerTest extends FormTestCase{
	
	@Resource
	CustomQueryManager customQueryManager;
	@Test
	public void testCurd() throws Exception{
		CustomQuery cq=customQueryManager.get("20000000500041");
		CustomQuery c=customQueryManager.getByAlias(cq.getAlias());
		assertEquals(c.getAlias(),cq.getAlias());
		String queryData="[]";
		String dbType="mysql";
		PageList list= customQueryManager.getData(cq, queryData, dbType, 1, 10);
		assertEquals(0,list.getRows().size());
	}
}
