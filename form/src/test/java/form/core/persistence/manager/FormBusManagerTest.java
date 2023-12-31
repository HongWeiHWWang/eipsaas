package form.core.persistence.manager;

import com.hotent.bo.model.BoData;
import com.hotent.form.persistence.manager.FormBusManager;
import form.core.FormTestCase;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月12日
 */
public class FormBusManagerTest extends FormTestCase{
	@Resource
	FormBusManager formBusManager;
	@Test
	public void testCurd() throws Exception{
		BoData bodata=formBusManager.getBoData("cwdx", "");
		assertEquals(null, bodata);
		System.out.println(bodata);
		String formKey="cwdx";
		String json="{\"initData\":{\"grxx\":{\"zz\":\"\",\"sjh\":\"\",\"xm\":\"\",\"xb\":\"\"}},\"sqr\":\"\",\"bz\":\"1\",\"bm\":\"药学研究,口服制剂研究\",\"je\":\"1\",\"sub_grxx\":[],\"rq\":\"2018-06-19 14:41:17\"}";
		formBusManager.saveData(formKey, json);

	}
}
