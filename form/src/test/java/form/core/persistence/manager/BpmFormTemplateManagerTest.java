/**
 * 
 */
package form.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.FormTemplate;
import com.hotent.form.persistence.manager.FormTemplateManager;

import form.core.FormTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月7日
 */
@SuppressWarnings("rawtypes")
public class BpmFormTemplateManagerTest extends FormTestCase{

	@Resource
	FormTemplateManager bpmFormTemplateManager;
	@Test
	public void testCurd() throws Exception{
		bpmFormTemplateManager.initAllTemplate();
		bpmFormTemplateManager.init();
		int size=bpmFormTemplateManager.getTemplateType("macro").size();
		FormTemplate bt=bpmFormTemplateManager.get("10000000090566");
		String name="雷健测试";
		bt.setTemplateName(name);
		String id=UniqueIdUtil.getSuid();
		bt.setTemplateId(id);
		bpmFormTemplateManager.create(bt);
		assertEquals(name, bt.getTemplateName());
		bt.setAlias("ljcs"+id);
		bpmFormTemplateManager.update(bt);
		assertEquals("ljcs"+id, bt.getAlias());
		FormTemplate b=bpmFormTemplateManager.getByTemplateAlias("ljcs"+id);
		assertEquals("ljcs"+id, b.getAlias());
		boolean f=bpmFormTemplateManager.isExistAlias("ljcs"+id);
		assertTrue(f);
		bpmFormTemplateManager.backUpTemplate(id);
		
		List list=bpmFormTemplateManager.getTemplateType("macro");
		assertEquals(size+1, list.size());
		List<FormTemplate> allMainTableTemplate=bpmFormTemplateManager.getAllMainTableTemplate(true);
		assertEquals(4, allMainTableTemplate.size());
		List<FormTemplate> allSubTableTemplate=bpmFormTemplateManager.getAllSubTableTemplate(true);
		assertEquals(4, allSubTableTemplate.size());
		List<FormTemplate> allMacroTemplate=bpmFormTemplateManager.getAllMacroTemplate();
		assertEquals(size+1, allMacroTemplate.size());
		List<FormTemplate> allTableManageTemplate=bpmFormTemplateManager.getAllTableManageTemplate() ;
		assertEquals(0, allTableManageTemplate.size());
		List< FormTemplate> listTemplate=bpmFormTemplateManager.getListTemplate() ;
		assertEquals(0, listTemplate.size());
		List< FormTemplate> DetailTemplate=bpmFormTemplateManager.getDetailTemplate() ;
		assertEquals(0, DetailTemplate.size());
		List< FormTemplate> DataTemplate=bpmFormTemplateManager.getDataTemplate() ;
		assertEquals(1, DataTemplate.size());
		List< FormTemplate> QueryDataTemplate=bpmFormTemplateManager.getQueryDataTemplate() ;
		assertEquals(1, QueryDataTemplate.size());
	}
}
