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

public class DemensionServiceTest extends UcTestCase {
	@Resource
	DemensionManager demensionService;
	@Resource
	OrgAuthManager orgAuthManager;

	@Before
	public void runBeforeTestMethod() {
		System.out.println("测试开始");
	}

	@After
	public void runAfterTestMethod() {
		System.out.println("测试完成");
	}

	@Test
	public void getDemByTime() throws Exception {
		OrgExportObject exportObject = new OrgExportObject();
		exportObject.setBtime("2018-01-01 12:00:00");
		exportObject.setEtime("2018-02-01 12:00:00");
		List<Demension> demByTime = demensionService.getDemByTime(exportObject);
		System.out.println(demByTime);
	}
}
