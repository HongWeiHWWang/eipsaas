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
import com.hotent.portal.PortalTestCase;
import com.hotent.portal.model.MessageReceiver;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class MessageReceiverManagerTest extends PortalTestCase {
	@Resource
	MessageReceiverManager msr;

	@Test
	public void testCRUD() {
		String suid = "1314";
		MessageReceiver sc = new MessageReceiver();
		String alias = "22233";
		sc.setId(suid);
		sc.setMsgId(alias);

		// 添加
		msr.create(sc);

		// 查询
		MessageReceiver column = msr.get(suid);
		assertEquals(alias, column.getMsgId());

		String newDesc = "新的学生信息";
		sc.setMsgId(newDesc);
		;
		// 更新
		msr.update(sc);

		MessageReceiver messageReceiver = msr.get(suid);
		assertEquals(newDesc, messageReceiver.getMsgId());

		MessageReceiver s = new MessageReceiver();
		String suid2 = "1312";
		String name = "首页栏目";
		s.setId(suid2);
		s.setMsgId(name);

		MessageReceiver sys = new MessageReceiver();
		String suid3 = "1315";
		String newAlias = "门户栏目";
		sys.setId(suid3);
		sys.setMsgId(newAlias);

		msr.create(s);
		msr.create(sys);

		// 通过ID列表批量查询
		List<MessageReceiver> byIds = msr.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("msgId", newDesc));
		// 通过通用查询条件查询
		PageList<MessageReceiver> query = msr.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		MessageReceiver bab = msr.getOne(Wrappers.<MessageReceiver>lambdaQuery().eq(MessageReceiver::getMsgId, newDesc));
		assertEquals(newDesc, bab.getMsgId());

		// 查询所有数据
		 List<MessageReceiver> all = msr.list();
		 assertEquals(3, all.size());

		// 查询所有数据(分页)
		 PageList<MessageReceiver> allByPage = msr.page(new PageBean(1, 2));
		 assertEquals(3, allByPage.getTotal());
		 assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		msr.remove(suid);
		MessageReceiver sstd = msr.getOne(Wrappers.<MessageReceiver>lambdaQuery().eq(MessageReceiver::getMsgId, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		msr.removeByIds(new String[] { suid, suid2, suid3 });
		List<MessageReceiver> nowAll = msr.list();
		assertEquals(0, nowAll.size());
	}
}
