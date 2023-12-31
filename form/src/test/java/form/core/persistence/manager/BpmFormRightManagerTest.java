/**
 * 
 */
package form.core.persistence.manager;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.FormRight;
import com.hotent.form.persistence.manager.FormRightManager;

import form.core.FormTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月7日
 */
public class BpmFormRightManagerTest extends FormTestCase{
	
	@Resource 
	FormRightManager bpmFormRightManager;
	@Test
	public void testCurd() throws Exception{
		FormRight br=new FormRight();
		int countsize=bpmFormRightManager.list().size();
		String id=UniqueIdUtil.getSuid();
		br.setId(id);
		br.setFormKey("bdlc1");
		br.setFlowKey("sqlccs01");
		br.setNodeId("SignTask1");
		br.setPermission("[{\"type\":\"none\"}],\"fieldName\":\"ssbm\"}},\"tableName\":\"slsqlc\"}}}");
		br.setPermissionType(2);
		bpmFormRightManager.create(br);
		assertEquals(id,bpmFormRightManager.get(id).getId());
		bpmFormRightManager.removeInst("sqlccs01");
		assertEquals(null,bpmFormRightManager.get(id));
		bpmFormRightManager.create(br);
		bpmFormRightManager.remove("sqlccs01", "SignTask1", "");
		assertEquals(null,bpmFormRightManager.get(id));
		bpmFormRightManager.create(br);
		bpmFormRightManager.remove("bdlc1", "sqlccs01", "SignTask1", "");
		assertEquals(id,bpmFormRightManager.get(id).getId());
		bpmFormRightManager.remove("sqlccs01", "");
		assertEquals(id,bpmFormRightManager.get(id).getId());
		assertEquals(countsize+2,bpmFormRightManager.list().size());
		JsonNode jsonNode= bpmFormRightManager.getPermissionSetting("bdlc1", "sqlccs01", "", "UserTask3", 1,true);
		assertNotEquals(null,jsonNode);
		jsonNode=bpmFormRightManager.getPermission("bdlc1", "sqlccs01", "", "UserTask3", 1,true);
		assertEquals(null,jsonNode);
		jsonNode=bpmFormRightManager.getDefaultByFormDefKey("bdlc1", false);
		assertEquals(null,jsonNode);
		jsonNode=bpmFormRightManager.getByFormKey("bdlc1", false);
		assertEquals(null,jsonNode);
		JsonNode permissionConf=JsonUtil.getMapper().createObjectNode();
		jsonNode=bpmFormRightManager.calcFormPermission(permissionConf);
		assertEquals(null,jsonNode);
		jsonNode=bpmFormRightManager.getStartPermission("bdlc1", "sqlccs01", "UserTask3", "");
		assertEquals(null,jsonNode);
		List<FormRight> list=bpmFormRightManager.getByFlowKey("bdlc1");
		assertEquals(0,list.size());
		List<Map<String,String>> list2=bpmFormRightManager.getTableOrderBySn("bdlc1");
		assertEquals(0,list2.size());
		bpmFormRightManager.removeByFormKey("bdlc1");
		assertEquals(0,bpmFormRightManager.list().size());
		
	}
}
