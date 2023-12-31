package com.hotent.bo.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.hotent.bo.model.BoDataRel;
import com.hotent.bo.persistence.manager.BoDataRelManager;

/**
 * BoDataRelManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
public class BoDataRelManagerTest extends BoTestCase{
	@Resource
	BoDataRelManager boDataRelManager;

	@Test
	public void testCurd(){
		String suid = UniqueIdUtil.getSuid();
		String pk = "id";
		BoDataRel boDataRel = new BoDataRel();
		boDataRel.setId(suid);
		boDataRel.setPk(pk);
		boDataRel.setFk("ref_id");
		// 添加
		boDataRelManager.create(boDataRel);
		// 查询
		BoDataRel bdr = boDataRelManager.get(suid);
		assertEquals(pk, bdr.getPk());

		String newFk = "r_id";
		bdr.setFk(newFk);
		// 更新
		boDataRelManager.update(bdr);

		BoDataRel bd = boDataRelManager.get(suid);
		assertEquals(newFk, bd.getFk());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		BoDataRel bdr1 = new BoDataRel(suid2, "myId", "myRId", "test");
		BoDataRel bdr2 = new BoDataRel(suid3, "yourId", "youRId", "yourTest");
		boDataRelManager.create(bdr1);
		boDataRelManager.create(bdr2);

		// 通过ID列表批量查询
		List<BoDataRel> byIds = boDataRelManager.listByIds(Arrays.asList(new String[]{suid2,suid3}));
		assertEquals(2, byIds.size());

		QueryFilter<BoDataRel> queryFilter = QueryFilter.<BoDataRel>build()
				.withDefaultPage() /*构建默认分页条件，否则不会做分页查询，则结构中也不会有total返回*/
				.withQuery(new QueryField("pk", "myId"));
		// 通过通用查询条件查询
		PageList<BoDataRel> query = boDataRelManager.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		BoDataRel bab = boDataRelManager.getOne(Wrappers.<BoDataRel>lambdaQuery().eq(BoDataRel::getPk, pk));
		assertEquals(pk, bab.getPk());

		// 查询所有数据
		List<BoDataRel> all = boDataRelManager.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<BoDataRel> allByPage = boDataRelManager.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		boDataRelManager.remove(suid);
		BoDataRel sstd = boDataRelManager.getOne(Wrappers.<BoDataRel>lambdaQuery().eq(BoDataRel::getPk, pk));
		assertTrue(sstd==null);

		// 通过ID集合批量删除数据
		boDataRelManager.removeByIds(Arrays.asList(new String[]{suid, suid2, suid3}));
		List<BoDataRel> nowAll = boDataRelManager.list();
		assertEquals(0, nowAll.size());
	}
}
