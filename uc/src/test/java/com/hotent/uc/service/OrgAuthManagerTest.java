package com.hotent.uc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.UcTestCase;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgAuthManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.params.common.OrgExportObject;

/**
 * 维度测试用例
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月8日
 */

public class OrgAuthManagerTest extends UcTestCase {
	@Resource
	OrgAuthManager OrgAuthManager;

	@Before
	public void runBeforeTestMethod() {
		System.out.println("测试开始");
	}

	@After
	public void runAfterTestMethod() {
		System.out.println("测试完成");
	}

	@Test
	public void getAllOrgAuth() throws Exception {
		QueryFilter<OrgAuth> queryFilter =QueryFilter.build();
	//	List<OrgAuth> allOrgAuth = OrgAuthManager.getAllOrgAuth(queryFilter);
		//System.out.println(demByTime);
	}
}
