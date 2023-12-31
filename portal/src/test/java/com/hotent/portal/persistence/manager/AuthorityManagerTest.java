/**
 * 
 */
package com.hotent.portal.persistence.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.portal.PortalTestCase;
import com.hotent.uc.api.model.IPermission;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class AuthorityManagerTest extends PortalTestCase{
	@Resource
	AuthorityManager am;
	
	@Test
	public void testSysTypeManager(){
		
		String beanId = null;
		//获取授权的实现方法，这里返回对应实现类列表。
		List<IPermission> serviceList = am.getCurUserServiceList(beanId );
		
		Map<String, Set<String>> listMap = new HashMap<>();
		Map<String, String> list = am.getMapStringByMayList(listMap);
	}
}
