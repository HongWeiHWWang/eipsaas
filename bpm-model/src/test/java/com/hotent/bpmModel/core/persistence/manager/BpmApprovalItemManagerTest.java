package com.hotent.bpmModel.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpmModel.core.BpmModelTestCase;
import com.hotent.bpmModel.manager.BpmApprovalItemManager;
import com.hotent.bpmModel.model.BpmApprovalItem;

/**
 * 常用语manager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月25日
 */
public class BpmApprovalItemManagerTest extends BpmModelTestCase{
	@Resource
	BpmApprovalItemManager bpmApprovalItemManager;
	
	@Test
	public void testCurd() throws Exception{
		String suid1 = UniqueIdUtil.getSuid();
		BpmApprovalItem bpmApprovalItem = new BpmApprovalItem();
		bpmApprovalItem.setId(suid1);
		bpmApprovalItem.setUserId("1");
		bpmApprovalItem.setDefKey("cslc");
		bpmApprovalItem.setDefName("测试流程常用语");
		bpmApprovalItem.setType(Short.valueOf("2"));
		String flowTypeId = UniqueIdUtil.getSuid();
		bpmApprovalItem.setTypeId(flowTypeId);
		bpmApprovalItem.setExpression("测试添加常用语");
		
		//添加常用语
		bpmApprovalItemManager.addTaskApproval(bpmApprovalItem);
		
		//通过id获取常用语
		/*BpmApprovalItem bpmApprovalItem01 = bpmApprovalItemManager.get(suid1);
		assertEquals(suid1, bpmApprovalItem01.getId());*/
		
		//获取常用语
		List<String> approvalItems = bpmApprovalItemManager.getApprovalByDefKeyAndTypeId("cslc", flowTypeId,null);
		assertEquals(1, approvalItems.size());
		
		
		// 查询所有数据(分页)
		PageList<BpmApprovalItem> pageList = bpmApprovalItemManager.page(new PageBean(1, 10));
		assertEquals(1, pageList.getTotal());
		assertEquals(1, pageList.getRows().size());
		
		String suid2 = UniqueIdUtil.getSuid();
		BpmApprovalItem bpmApprovalItem2 = bpmApprovalItem;
		bpmApprovalItem2.setId(suid2);
		bpmApprovalItemManager.create(bpmApprovalItem2);
		String suid3 = UniqueIdUtil.getSuid();
		BpmApprovalItem bpmApprovalItem3 = bpmApprovalItem;
		bpmApprovalItem3.setId(suid2);
		bpmApprovalItemManager.create(bpmApprovalItem3);
		List<BpmApprovalItem> all = bpmApprovalItemManager.getAll();
		assertEquals(3, all.size());
		//根据id删除
		bpmApprovalItemManager.remove(suid1);
		List<BpmApprovalItem> all2 = bpmApprovalItemManager.getAll();
		assertEquals(2, all2.size());
		//根据id 批量删除
		bpmApprovalItemManager.removeByIds(new String[]{suid2,suid3});
		List<BpmApprovalItem> all0 = bpmApprovalItemManager.getAll();
		assertEquals(0, all0.size());
	}
	
}
