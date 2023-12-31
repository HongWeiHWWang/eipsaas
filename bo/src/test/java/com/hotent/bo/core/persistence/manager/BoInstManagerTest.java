package com.hotent.bo.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.core.BoTestCase;
import com.hotent.bo.model.BoInst;
import com.hotent.bo.persistence.manager.BoInstManager;

/**
 * boInstManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
public class BoInstManagerTest extends BoTestCase{
	@Resource
	BoInstManager boInstManager;
	
	private BoInst build(String id, String defId, String instData){
		BoInst boInst = new BoInst();
		boInst.setId(id);
		boInst.setDefId(defId);
		boInst.setInstData(instData);
		boInst.setCreateTime(LocalDateTime.now());
		return boInst;
	}
	
	@Test
	public void testCrud(){
		String suid = UniqueIdUtil.getSuid();
		String boDefId = "1002";
		// 添加
		boInstManager.create(build(suid, boDefId, "test data 1"));
		// 查询
		BoInst boent = boInstManager.get(suid);
		assertEquals(boDefId, boent.getDefId());

		String newData = "new test data 1";
		boent.setInstData(newData);
		// 更新
		boInstManager.update(boent);

		BoInst bd = boInstManager.get(suid);
		assertEquals(newData, bd.getInstData());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		
		boInstManager.create(build(suid2, boDefId, "test data 2"));
		boInstManager.create(build(suid3, boDefId, "test data 3"));

		// 通过ID列表批量查询
		List<BoInst> byIds = boInstManager.listByIds(Arrays.asList(new String[]{suid2, suid3}));
		assertEquals(2, byIds.size());

		QueryFilter<BoInst> queryFilter = QueryFilter.<BoInst>build()
											 .withQuery(new QueryField("defId", boDefId));
		// 通过通用查询条件查询
		PageList<BoInst> query = boInstManager.query(queryFilter);
		assertEquals(3, query.getTotal());

		// 通过指定列查询唯一记录
		BoInst bab = boInstManager.getOne(Wrappers.<BoInst>lambdaQuery().eq(BoInst::getId, suid));
		assertEquals(newData, bab.getInstData());
		
		// 查询所有数据
		List<BoInst> all = boInstManager.list();
		assertEquals(3, all.size());
		
		// 查询所有数据(分页)
		PageList<BoInst> allByPage = boInstManager.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());
		
		// 通过ID删除数据
		boInstManager.remove(suid);
		BoInst sstd = boInstManager.getOne(Wrappers.<BoInst>lambdaQuery().eq(BoInst::getId, suid));
		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		boInstManager.removeByIds(Arrays.asList(new String[]{suid2, suid3}));
		List<BoInst> nowAll = boInstManager.list();
		assertEquals(0, nowAll.size());
	}
}
