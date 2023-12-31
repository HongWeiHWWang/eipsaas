/**
 * 
 */
package form.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.Form;
import com.hotent.form.persistence.manager.FormManager;

import form.core.FormTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月7日
 */
public class BpmFormManagerTest extends FormTestCase{
	@Resource 
	FormManager bpmFormManager;
	@SuppressWarnings("rawtypes")
	@Test
	public void testCurd() throws Exception{
		int countSize=bpmFormManager.list().size();
		Form bf=new Form();
		String id=UniqueIdUtil.getSuid();
		bf.setId(id);
		bf.setDefId("10000000020021");
		bf.setName("测试");
		bf.setFormKey("cs");
		bf.setFormHtml("validate=\"{&quot;required&quot;:false,&quot;maxLength&quot;:50}\"/></td></tr></tbody></table>");
		bf.setStatus("draft");
		bf.setFormType("pc");
		bf.setTypeId("10000003330041");
		bf.setTypeName("默认分类");
		bf.setIsMain('N');
		bf.setVersion(1);
		bf.setCreateBy("1");
		//bf.setCreateTime(LocalDateTime.now());
		bf.setFormTabTitle("主页面");
		bpmFormManager.create(bf);
		assertNotEquals(null, bpmFormManager.get(id));
		String getHtml=bpmFormManager.getHtml("10000000020021", "mainFieldTemplate", "subFieldListTemplate");
		assertEquals("", getHtml);
		Form b=bpmFormManager.getMainByFormKey("cs");
		assertEquals(null, b);
		List<Form> listBpmForm=bpmFormManager.getByFormKey("cs");
		assertEquals(1, listBpmForm.size());
		List list= bpmFormManager.getByBoCodes(null, "pc", QueryFilter.build());
		assertEquals(0, list.size());
		int count=bpmFormManager.getBpmFormCountsByFormKey("cs");
		assertEquals(1,count);
		
		bpmFormManager.newVersion(id);
		assertEquals(countSize+2,bpmFormManager.list().size());
		bpmFormManager.setDefaultVersion(id, "cs");
		assertNotEquals(bf.getIsMain(),bpmFormManager.get(id).getIsMain());
		bpmFormManager.publish(id);
		assertEquals("deploy",bpmFormManager.get(id).getStatus());
		List<Form> listbm=bpmFormManager.getByDefId("10000000020021");
		assertEquals(2,listbm.size());
		String html=bpmFormManager.genByField("10000000020021", "attrId", "pc");
		assertEquals("",html);
		bpmFormManager.remove(id);
		assertEquals(null,bpmFormManager.get(id));
	}
}
