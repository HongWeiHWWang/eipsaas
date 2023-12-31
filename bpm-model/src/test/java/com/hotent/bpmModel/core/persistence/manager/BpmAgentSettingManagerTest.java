package com.hotent.bpmModel.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.bpm.persistence.manager.BpmAgentSettingManager;
import com.hotent.bpm.persistence.model.BpmAgentSetting;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.bpmModel.core.BpmModelTestCase;

/**
 * 流程代理manger测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月25日
 */
public class BpmAgentSettingManagerTest extends BpmModelTestCase{
	@Resource
	BpmAgentSettingManager bpmAgentSettingManager;
	
	@Test
	public void testCurd(){ 
		String suid1 = UniqueIdUtil.getSuid();
		BpmAgentSetting bpmAgentSetting = new BpmAgentSetting();
		bpmAgentSetting.setId(suid1);
		bpmAgentSetting.setSubject("测试流程代理设置");
		bpmAgentSetting.setAuthId("1");
		bpmAgentSetting.setAuthName("系统管理员");
		bpmAgentSetting.setStartDate(DateUtil.getCurrentDate());
		bpmAgentSetting.setEndDate(DateUtil.getDate(2028, 8, 25));
		bpmAgentSetting.setAgentId("1");
		bpmAgentSetting.setAgent("系统管理员");
		bpmAgentSetting.setFlowKey("cslc");
		bpmAgentSetting.setType(Short.valueOf("1"));
		
		//添加
		bpmAgentSettingManager.create(bpmAgentSetting);
		
		//通过id获取流程代理设定。（包含流程和 代理条件设定）
		BpmAgentSetting bpmAgentSetting01 = bpmAgentSettingManager.getById(suid1);
		assertEquals(suid1, bpmAgentSetting01.getId());
		
		//根据授权人和流程定义ID获取流程代理设定。
		BpmAgentSetting bpmAgentSetting02 = bpmAgentSettingManager.getSettingByFlowAndAuthidAndDate("1", "cslc");
		assertEquals("cslc", bpmAgentSetting02.getFlowKey());
		
		//检查代理是否和已设置的代理是否有冲突
		ResultMessage message = bpmAgentSettingManager.checkConflict(bpmAgentSetting);
		System.err.println(message.getResult());
		
		
		// 查询所有数据(分页)
		PageList<BpmAgentSetting> pageList = bpmAgentSettingManager.page(new PageBean(1, 10));
		assertEquals(1, pageList.getTotal());
		assertEquals(1, pageList.getRows().size());
		
		String suid2 = UniqueIdUtil.getSuid();
		BpmAgentSetting bpmAgentSetting2 = bpmAgentSetting;
		bpmAgentSetting2.setId(suid2);
		bpmAgentSettingManager.create(bpmAgentSetting2);
		String suid3 = UniqueIdUtil.getSuid();
		BpmAgentSetting bpmAgentSetting3 = bpmAgentSetting;
		bpmAgentSetting3.setId(suid3);
		bpmAgentSettingManager.create(bpmAgentSetting3);
		//查询所有不带分页
		List<BpmAgentSetting> all = bpmAgentSettingManager.getAll();
		assertEquals(3, all.size());
		//根据id删除
		bpmAgentSettingManager.remove(suid1);
		List<BpmAgentSetting> all2 = bpmAgentSettingManager.getAll();
		assertEquals(2, all2.size());
		//根据id 批量删除
		bpmAgentSettingManager.removeByIds(new String[]{suid2,suid3});
		List<BpmAgentSetting> all1 = bpmAgentSettingManager.getAll();
		assertEquals(0, all1.size());
	}
	
}
