package form.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.FormMeta;
import com.hotent.form.persistence.manager.FormMetaManager;

import form.core.FormTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月7日
 */
public class BpmFormDefManagerTest extends FormTestCase{
	@Resource
	FormMetaManager bpmFormDefManager;
	
	@Test
	public void testCurd(){
		String suid = UniqueIdUtil.getSuid();
		String key = "test";
		String name = "测试表单";
		String type = "默认分类";
		String typeId = "1";
		FormMeta formMeta = new FormMeta();
		formMeta.setId(suid);
		formMeta.setKey(key);
		formMeta.setName(name);
		formMeta.setDesc("一个测试表单");
		formMeta.setType(type);
		formMeta.setTypeId(typeId);
		bpmFormDefManager.create(formMeta);
		//根据表单key获取表单元数据定义。
		FormMeta def=bpmFormDefManager.getByKey(key);
		assertEquals(key, def.getKey());
		//更新表单意见配置。
		String config="[{\"name\": \"jzyj\",\"desc\": \"局长意见\"},{\"name\": \"cwyj\",\"desc\": \"财务意见\" }]";
		bpmFormDefManager.updateOpinionConf(suid, config);
		assertEquals(config, bpmFormDefManager.get(suid).getOpinionConf());
		
		String str = bpmFormDefManager.getMetaKeyByFormKey("formKey");
		assertEquals(null, str);
		List<String> listStr=bpmFormDefManager.getBOCodeByFormId("formId");
		assertEquals(0, listStr.size());
	}
}
