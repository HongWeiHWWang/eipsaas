package com.hotent.service.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Test;
import com.hotent.base.service.InvokeResult;
import com.hotent.base.service.ServiceClient;
import com.hotent.base.util.FileUtil;
import com.hotent.service.ServiceBaseTest;
import com.hotent.service.constant.ServiceParamType;
import com.hotent.service.model.ServiceParam;
import com.hotent.service.model.ServiceSet;

/**
 * 测试服务调用设置的增删改查
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月4日
 */
public class ServiceSetManagerTest extends ServiceBaseTest{
	@Resource
	ServiceSetManager serviceSetManager;
	@Resource
	ServiceClient serviceClient;
	
	private ServiceParam buildParam(String name, String desc) {
		ServiceParam param = new ServiceParam();
		param.setName(name);
		param.setType(ServiceParamType.String);
		param.setDesc(desc);
		return param;
	}
	
	@Test
	public void testCrud() {
		String alias = "getDomesticAirlinesTime";
		String name = "查询两个城市的航班信息";
		ServiceSet serviceSet = new ServiceSet();
		serviceSet.setAlias(alias);
		serviceSet.setAddress("http://www.webxml.com.cn/webservices/DomesticAirline.asmx");
		serviceSet.setMethodName("getDomesticAirlinesTime");
		serviceSet.setName("getDomesticAirlinesTime");
		serviceSet.setNamespace("http://WebXml.com.cn/");
		serviceSet.setSoapAction('Y');
		String inputSet = FileUtil.readByClassPath("json/inputSet.json", this.getClass().getClassLoader());
		serviceSet.setInputSet(inputSet);
		
		List<ServiceParam> params = new ArrayList<>();
		params.add(buildParam("sc", "出发城市"));
		params.add(buildParam("lc", "抵达城市"));
		params.add(buildParam("td", "出发日期"));
		params.add(buildParam("uid", "用户ID"));
		serviceSet.setServiceParamList(params);
		// 保存
		serviceSetManager.saveData(serviceSet);
		// 查询
		ServiceSet set = serviceSetManager.getByAlias(alias);
		assertEquals(alias, set.getAlias());
		assertFalse(name.equals(set.getName()));
		
		List<ServiceParam> serviceParamList = set.getServiceParamList();
		int size = serviceParamList.size();
		assertEquals(4, size);
		
		serviceParamList.remove(3);
		set.setName(name);
		// 更新
		serviceSetManager.saveData(set);
		
		ServiceSet set2 = serviceSetManager.getByAlias(alias);
		
		assertEquals(name, set2.getName());
		List<ServiceParam> list = set2.getServiceParamList();
		assertEquals(3, list.size());
		
		Map<String, Object> map = new HashMap<>();
		map.put("sc", "广州");
		map.put("lc", "北京");
		map.put("td", "2018-7-4");
		// 调用服务
		InvokeResult invoke = serviceClient.invoke(alias, map);
		assertFalse(invoke.isFault());
	}
}
