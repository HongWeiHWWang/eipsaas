/**
 * 
 */
package form.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.form.model.CombinateDialog;
import com.hotent.form.persistence.manager.CombinateDialogManager;

import form.core.FormTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月12日
 */
public class CombinateDialogManagerTest extends FormTestCase{
	@Resource
	CombinateDialogManager combinateDialogManager;
	@Test
	public void testCurd() throws Exception{
		CombinateDialog c=new CombinateDialog();
		String str="[{\"tree\":{\"field\":\"ID_\",\"comment\":\"ID_\",\"idKey\":\"0\",\"AggFuncOp\":\"\"},\"list\":{\"field\":\"TYPE_ID_\",\"comment\":\"所属分类ID\",\"condition\":\"EQ\",\"dbType\":\"varchar\",\"defaultType\":\"4\",\"defaultValue\":\"\"}}]";
		c.setId("1");
		c.setName("流程组合对话框");
		c.setAlias("bpmDefinitionCombinateSelector");
		c.setWidth(800);
		c.setHeight(600);
		c.setTreeDialogId("15");
		c.setTreeDialogName("流程分类选择器");
		c.setListDialogId("14");
		c.setListDialogName("流程定义选择器");
		c.setField(str);
		combinateDialogManager.create(c);
		assertEquals("1", combinateDialogManager.get("1").getId());
		
		CombinateDialog cd=combinateDialogManager.getByAlias("bpmDefinitionCombinateSelector");
		System.out.println(cd);
		assertEquals("bpmDefinitionCombinateSelector",cd.getAlias());
	}
}
