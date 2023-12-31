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
import com.hotent.portal.model.MessageRead;
import com.hotent.uc.api.impl.model.UserFacade;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class MessageReadManagerTest extends PortalTestCase{
	@Resource
	MessageReadManager mr;
	
	@Test
	public void testCRUD(){
		String suid = idGenerator.getSuid();
		MessageRead sc = new MessageRead();
		String alias = "22233";
		sc.setId(suid);
		sc.setMsgId(alias);

		// 添加
		mr.create(sc);

		// 查询
		MessageRead column = mr.get(suid);
		assertEquals(alias, column.getMsgId());

		String newDesc = "新的学生信息";
		sc.setMsgId(newDesc);
		;
		// 更新
		mr.update(sc);

		MessageRead messageRead = mr.get(suid);
		assertEquals(newDesc, messageRead.getMsgId());

		MessageRead s = new MessageRead();
		String suid2 = idGenerator.getSuid();
		String name = "首页栏目";
		s.setId(suid2);
		s.setMsgId(name);

		MessageRead sys = new MessageRead();
		String suid3 = idGenerator.getSuid();
		String newAlias = "门户栏目";
		sys.setId(suid3);
		sys.setMsgId(newAlias);

		mr.create(s);
		mr.create(sys);

		// 通过ID列表批量查询
		List<MessageRead> byIds = mr.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("msgId", newDesc));
		// 通过通用查询条件查询
		PageList<MessageRead> query = mr.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		MessageRead bab = mr.getOne(Wrappers.<MessageRead>lambdaQuery().eq(MessageRead::getMsgId, newDesc));
		assertEquals(newDesc, bab.getMsgId());

		// 查询所有数据
		 List<MessageRead> all = mr.list();
		 assertEquals(3, all.size());

		// 查询所有数据(分页)
		 PageList<MessageRead> allByPage = mr.page(new PageBean(1, 2));
		 assertEquals(3, allByPage.getTotal());
		 assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		mr.remove(suid);
		MessageRead sstd = mr.getOne(Wrappers.<MessageRead>lambdaQuery().eq(MessageRead::getMsgId, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		mr.removeByIds(new String[] { suid, suid2, suid3 });
		List<MessageRead> nowAll = mr.list();
		assertEquals(0, nowAll.size());	
	
	}
	
	@Test
	public void testSysTypeManager(){
		UserFacade u = new UserFacade();
		String userId = "12";
		String fullname = "dawang";
		u.setUserId(userId);
		u.setFullname(fullname);
		
		String id = idGenerator.getSuid();
		String msgId = "2233";
		MessageRead sc = new MessageRead();
		sc.setId(id);
		sc.setMsgId(msgId);
		sc.setReceiverId(userId);
		mr.create(sc);
		
		//添加已读信息
		mr.addMessageRead(msgId, u);
		
		//获取已读信息通过信息id
		List<MessageRead> list = mr.getByMessageId(msgId);
		assertEquals(1, list.size());
	}
}
