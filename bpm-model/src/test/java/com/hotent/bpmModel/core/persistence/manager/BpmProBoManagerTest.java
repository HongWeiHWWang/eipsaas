package com.hotent.bpmModel.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.manager.BpmProBoManager;
import com.hotent.bpm.persistence.model.BpmProBo;
import com.hotent.bpmModel.core.BpmModelTestCase;

/**
 * 流程跟业务定义之间的关系manager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月25日
 */
public class BpmProBoManagerTest extends BpmModelTestCase{
	@Resource
	BpmProBoManager bpmProBoManager;
	
	@Test
	public void testCurd() throws Exception{
		String suid1 = UniqueIdUtil.getSuid();
		BpmProBo bpmProBo = new BpmProBo();
		bpmProBo.setId(suid1);
		bpmProBo.setProcessId("9101001");;
		bpmProBo.setProcessKey("cslc");
		bpmProBo.setBoCode("cleaner9101001");
		bpmProBo.setBoName("保洁员信息9101001");
		
		//添加流程跟业务定义之间的关系
		bpmProBoManager.create(bpmProBo);
		
		//通过id获取流程跟业务定义之间的关系
		BpmProBo bpmProBo01 = bpmProBoManager.get(suid1);
		assertEquals(suid1, bpmProBo01.getId());
		
		//根据业务对象的标识（code）获得流程和业务对象的绑定信息
		List<BpmProBo> bpmProBoList = bpmProBoManager.getByBoCode("cleaner9101001");
		assertEquals(1, bpmProBoList.size());
		
		//根据流程信息（ID）获得流程和业务对象的绑定信息
		bpmProBoList = bpmProBoManager.getByProcessId("9101001");
		assertEquals(1, bpmProBoList.size());
		
		//根据流程信息（ID或者KEY）获得流程和业务对象的绑定信息
		bpmProBoList = bpmProBoManager.getByProcessKey("cslc");
		assertEquals(1, bpmProBoList.size());
		
		bpmProBoManager.removeByBoCode("cleaner9101001");
		
		bpmProBoManager.removeByProcessId("9101001");
		
		bpmProBoManager.removeByProcessKey("cslc");
		
		
		List<BpmProBo> all = bpmProBoManager.getAll();
		assertEquals(0, all.size());
		
		String suid2 = UniqueIdUtil.getSuid();
		BpmProBo bpmProBo2 = bpmProBo;
		bpmProBo2.setId(suid2);
		bpmProBoManager.create(bpmProBo2);
		String suid3 = UniqueIdUtil.getSuid();
		BpmProBo bpmProBo3 = bpmProBo;
		bpmProBo3.setId(suid3);
		bpmProBoManager.create(bpmProBo3);
		// 查询所有数据(分页)
		PageList<BpmProBo> pageList = bpmProBoManager.page(new PageBean(1, 10));
		assertEquals(2, pageList.getTotal());
		assertEquals(2, pageList.getRows().size());
		//根据id删除
		bpmProBoManager.remove(suid1);
		//根据id 批量删除
		bpmProBoManager.removeByIds(new String[]{suid2,suid3});
	}
	
}
