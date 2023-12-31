package com.hotent.runtime.manager;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.runtime.model.BpmTestCase;
import com.hotent.runtime.params.TestCaseBaseInfoVo;

/**
 * 流程的测试用例设置 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface BpmTestCaseManager extends BaseManager<BpmTestCase>{

	void startTest(String ids)  throws Exception ;

	TestCaseBaseInfoVo getBaseInfo(String defKey) throws Exception;

	void doNext(String id);

	ObjectNode getReportData(String ids) throws Exception;
	
}
