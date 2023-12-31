/**
 * 
 */
package form.core.persistence.manager;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.form.model.FormBusSet;
import com.hotent.form.persistence.manager.FormBusSetManager;

import form.core.FormTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月12日
 */
public class FormBusSetManagerTest extends FormTestCase{
	@Resource
	FormBusSetManager  formBusSetManager;
	@Test
	public void testCurd() throws Exception{
		FormBusSet form=formBusSetManager.get("10000000090452");
		FormBusSet f1=formBusSetManager.getByFormKey(form.getFormKey());
		assertEquals(form.getFormKey(),f1.getFormKey());
		boolean f=formBusSetManager.isExist(form);
		assertTrue(f);
	}
}
