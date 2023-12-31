/**
 * 
 */
package com.hotent.portal.persistence.manager;

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
import com.hotent.portal.PortalTestCase;
import com.hotent.portal.model.SysLayoutSetting;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class SysLayoutSettingManagerTest extends PortalTestCase{
	@Resource
	SysLayoutSettingManager ssm;
	
	@Test
	public void testCRUD(){
		String suid = UniqueIdUtil.getSuid();
		String alias = "faker";
		SysLayoutSetting sdm = new SysLayoutSetting();
		sdm.setId(suid);
		sdm.setLogo(alias);

		// 添加
		ssm.create(sdm);
		// 查询
		SysLayoutSetting bodef = ssm.get(suid);
		assertEquals(alias, bodef.getLogo());

		String newDesc = "leo";
		bodef.setLogo(newDesc);
		// 更新
		ssm.update(bodef);

		SysLayoutSetting bd = ssm.get(suid);
		assertEquals(newDesc, bd.getLogo());

		String suid2 = UniqueIdUtil.getSuid();
		String name = "bang";
		SysLayoutSetting st = new SysLayoutSetting();
		st.setId(suid2);
		st.setLogo(name);

		String suid3 = UniqueIdUtil.getSuid();
		String newName = "thal";
		SysLayoutSetting stm = new SysLayoutSetting();
		stm.setId(suid3);
		stm.setLogo(newName);

		ssm.create(st);
		ssm.create(stm);

		// 通过ID列表批量查询
		List<SysLayoutSetting> byIds = ssm.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("logo", newDesc));
		// 通过通用查询条件查询
		PageList<SysLayoutSetting> query = ssm.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		SysLayoutSetting bab = ssm.getOne(Wrappers.<SysLayoutSetting>lambdaQuery().eq(SysLayoutSetting::getLogo, newDesc));
		assertEquals(newDesc, bab.getLogo());

		// 查询所有数据
		List<SysLayoutSetting> all = ssm.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<SysLayoutSetting> allByPage = ssm.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		ssm.remove(suid);
		SysLayoutSetting sstd = ssm.getOne(Wrappers.<SysLayoutSetting>lambdaQuery().eq(SysLayoutSetting::getLogo, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		ssm.removeByIds(new String[] { suid, suid2, suid3 });
		List<SysLayoutSetting> nowAll = ssm.list();
		assertEquals(0, nowAll.size());	
	
	}
	
	@Test
	public void testSysTypeManager(){
		String id=idGenerator.getSuid();
		String layoutId = "23";
		String logo ="nihao";
		SysLayoutSetting ss = new SysLayoutSetting();
		ss.setId(id);
		ss.setLayoutId(layoutId);
		ss.setLogo(logo);
		ssm.create(ss);
		
		//通过布局Id获取布局设置
		SysLayoutSetting byLayoutId = ssm.getByLayoutId(layoutId);
		assertEquals(logo, byLayoutId.getLogo());
	}
}
