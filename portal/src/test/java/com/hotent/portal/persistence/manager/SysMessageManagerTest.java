package com.hotent.portal.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.PortalTestCase;
import com.hotent.portal.model.SysExecutor;
import com.hotent.portal.model.SysMessage;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class SysMessageManagerTest extends PortalTestCase{
	@Resource
	SysMessageManager smm;
	
	@Test
	public void testCRUD(){
		String suid = UniqueIdUtil.getSuid();
		String subject = "系统提醒";
		SysMessage sdm = new SysMessage();
		sdm.setId(suid);
		sdm.setSubject(subject);

		// 添加
		smm.create(sdm);
		// 查询
		SysMessage bodef = smm.get(suid);
		assertEquals(subject, bodef.getSubject());

		String newDesc = "leo";
		bodef.setSubject(newDesc);
		// 更新
		smm.update(bodef);

		SysMessage bd = smm.get(suid);
		assertEquals(subject, bd.getSubject());

		String suid2 = UniqueIdUtil.getSuid();
		String name = "bang";
		SysMessage st = new SysMessage();
		st.setId(suid2);
		st.setSubject(name);

		String suid3 = UniqueIdUtil.getSuid();
		String newName = "thal";
		SysMessage stm = new SysMessage();
		stm.setId(suid3);
		stm.setSubject(newName);

		smm.create(st);
		smm.create(stm);

		// 通过ID列表批量查询
		List<SysMessage> byIds = smm.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("subject", newDesc));
		// 通过通用查询条件查询
		PageList<SysMessage> query = smm.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		SysMessage bab = smm.getOne(Wrappers.<SysMessage>lambdaQuery().eq(SysMessage::getSubject, newDesc));
		assertEquals(newDesc, bab.getSubject());

		// 查询所有数据
		List<SysMessage> all = smm.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<SysMessage> allByPage = smm.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		smm.remove(suid);
		SysMessage sstd = smm.getOne(Wrappers.<SysMessage>lambdaQuery().eq(SysMessage::getSubject, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		smm.removeByIds(new String[] { suid, suid2, suid3 });
		List<SysMessage> nowAll = smm.list();
		assertEquals(0, nowAll.size());	
	
	}
	
	@Test
	public void testSysTypeManager(){
		String suid = idGenerator.getSuid();
		String name = "系统";
		String type = "122";
		List<SysExecutor> receivers = new ArrayList<>();
		SysExecutor se = new SysExecutor();
		se.setId(suid);
		se.setName(name);
		se.setType(type);
		receivers.add(se);
		
		String uid = "233";
		SysMessage sysMessage = new SysMessage();
		String id = "22223";
		String userId = "2234";
		String subject = "w发飞机啊";
		String content = "系统测试";
		String messageType ="测算";
		String receiverId = "123";
		String receiverName = "王";
		short isPublic = 1;
		sysMessage.setId(id);
		sysMessage.setSubject(subject);
		sysMessage.setReceiverId(receiverId);
		sysMessage.setContent(content);
		sysMessage.setMessageType(messageType);
		sysMessage.setReceiverId(receiverId);
		sysMessage.setReceiverName(receiverName);
		sysMessage.setIsPublic(isPublic);
		smm.create(sysMessage);
		
		//处理消息发送
		smm.addMessageSend(sysMessage);
		
		QueryFilter queryFilter = QueryFilter.build();
		queryFilter.addFilter("ownerId", uid, QueryOP.EQUAL);
		//通过用户id获取信息
//		List<SysMessage> list = smm.getMsgByUserId(queryFilter);
//		assertEquals(1, list.size());
		
		//获取信息的个数
//		int msgSize = smm.getMsgSize(receiverId);
//		assertEquals(1, msgSize);
		
		//获取最新一条的未读的消息
		SysMessage notReadMsg = smm.getNotReadMsg(userId);
		assertEquals(subject, notReadMsg.getSubject());
		
		//获取未读信息数量
		int notReadMsgNum = smm.getNotReadMsgNum(userId);
		assertEquals(1, notReadMsgNum);
		
		//发送信息
		//CommonResult<String> sendMsg = smm.sendMsg(subject, content, messageType, uid, fullname, receivers);
		
	}
}
