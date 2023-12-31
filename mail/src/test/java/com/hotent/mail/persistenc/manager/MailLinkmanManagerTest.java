package com.hotent.mail.persistenc.manager;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.mail.MailTestCase;
import com.hotent.mail.model.MailLinkman;
import com.hotent.mail.persistence.manager.MailLinkmanManager;

/**
 * MailLinkmanManager测试类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
public class MailLinkmanManagerTest extends MailTestCase {
	@Resource
	MailLinkmanManager m;

	@Test
	public void testCRUD() {
		String suid = UniqueIdUtil.getSuid();
		String name = "csry";
		MailLinkman ml = new MailLinkman();
		ml.setId(suid);
		ml.setLinkName(name);
		// 添加
		m.create(ml);
		// 查询
		MailLinkman linkman = m.get(suid);
		assertEquals(name, linkman.getLinkName());

		String nowName = "测试人员";
		ml.setLinkName(nowName);
		m.update(ml);

		MailLinkman mailLinkman = m.get(suid);
		assertEquals(nowName, mailLinkman.getLinkName());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();

		MailLinkman ma = new MailLinkman();
		ma.setId(suid2);
		ma.setLinkName("csry2");

		MailLinkman mk = new MailLinkman();
		mk.setId(suid3);
		mk.setLinkName("csry3");

		m.create(ma);
		m.create(mk);

		// 通过ID列表批量查询
		List<String> ls = new ArrayList<>();
		ls.add(suid2);
		ls.add(suid3);
		List<MailLinkman> ids = m.listByIds(ls);
		assertEquals(2, ids.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("LINKNAME", nowName));

		// 通过通用查询条件查询
		PageList<MailLinkman> query = m.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列表查询唯一记录
		MailLinkman unique = m.getOne(Wrappers.<MailLinkman>lambdaQuery().eq(MailLinkman::getLinkName, nowName));
		
		assertEquals(nowName, unique.getLinkName());

		// 通过主键查询
		MailLinkman mm = m.get(suid);
		assertEquals(suid, mm.getId());

		// 查询所有数据
		List<MailLinkman> all = m.list();
		assertEquals(3, all.size());

		// 查询所有分页
		PageList<MailLinkman> allByPage = m.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过id删除数据
		m.remove(suid);
		MailLinkman ln = m.getOne(Wrappers.<MailLinkman>lambdaQuery().eq(MailLinkman::getLinkName, nowName));
		assertTrue(ln == null);

		// 通过id集合批量删除数据
		m.removeByIds(new String[] { suid2, suid3 });
		List<MailLinkman> list = m.list();
		assertEquals(0, list.size());
	}

	@Test
	public void testManage() throws Exception {
		String userId = "1";
		String address = "gzht@hotent.com"; 
		MailLinkman mn = new MailLinkman();
		mn.setId(UniqueIdUtil.getSuid());
		mn.setUserId(userId);
		mn.setMailId(address);
		m.create(mn);
		
		//根据邮箱地址找到联系人
		MailLinkman man = m.findLinkMan(address, userId);
		assertEquals(address, man.getMailId());
		
		//找到当前用户下的最近联系人
		String condition = "sortingByTimes";
		List<MailLinkman> list = m.getAllByUserId(userId, condition);
		assertEquals(1, list.size());
	}
}
