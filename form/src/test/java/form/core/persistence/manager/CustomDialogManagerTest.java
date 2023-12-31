/**
 * 
 */
package form.core.persistence.manager;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageList;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.CustomDialog;
import com.hotent.form.persistence.manager.CustomDialogManager;

import form.core.FormTestCase;

/**
 * 自定义对话框对象Manager单元测试
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月12日
 */
public class CustomDialogManagerTest extends FormTestCase{
	@Resource
	CustomDialogManager customDialogManager;
	@SuppressWarnings("rawtypes")
	@Test
	public void testCurd() throws Exception{
		CustomDialog c=customDialogManager.get("15");
		String id=UniqueIdUtil.getSuid();
		c.setId(id);
		customDialogManager.create(c);
		assertEquals(id,customDialogManager.get(id).getId());
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("_ctx", "");
		map.put("dialog_alias", "FlowTypeor,FlowTypeor");
		map.put("setting[view][selectedMulti]", "false");
		List list=customDialogManager.geTreetData(c, map, "mysql");
		assertEquals(0,list.size());
		PageList listData = customDialogManager.getListData(c, map,null);
		assertEquals(0,listData.getRows().size());
	}
}
