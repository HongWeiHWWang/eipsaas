package com.hotent.portal.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.MessageReceiver;
import com.hotent.portal.model.SysExecutor;
import com.hotent.portal.model.SysMessage;
import com.hotent.portal.persistence.dao.SysMessageDao;
import com.hotent.portal.persistence.manager.MessageReceiverManager;
import com.hotent.portal.persistence.manager.SysMessageManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 系统信息处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author hugh
 * @email zxh@jee-soft.cn
 * @date 2018年6月21日
 */
@Service("sysMessageManager")
public class SysMessageManagerImpl extends BaseManagerImpl<SysMessageDao, SysMessage> implements SysMessageManager{

	@Resource
	MessageReceiverManager messageReceiverManager;
	
	@Override
	public PageList<SysMessage> getMsgByUserId(QueryFilter queryFilter) {
		page(queryFilter.getPageBean());
		Map<String,Object> map = new HashMap<>();
		map = queryFilter.getParams();

		List createTimeList=(List)queryFilter.getParams().get("create_time_");
		if(createTimeList!=null){
			map.put("beginreceiveTime",createTimeList.get(0));
			map.put("endreceiveTime",createTimeList.get(1));
		}
		List<SysMessage> list = baseMapper.getMsgByUserId(map);
		return new PageList<SysMessage>(list);
	}

	
	@Override
	public CommonResult<String> sendMsg(String subject, String content,String messageType, String senderId, String senderName, List<SysExecutor> receivers) {
		try {
			String id = UniqueIdUtil.getSuid();
			SysMessage sysMessage = new SysMessage();
			sysMessage.setId(id);
			sysMessage.setSubject(subject);
			sysMessage.setContent(content);
			sysMessage.setOwnerId(StringUtil.isEmpty(senderId)?"-1":senderId);
			sysMessage.setOwner(StringUtil.isEmpty(senderName)?"x7系统":senderName);
			sysMessage.setMessageType(messageType);
			sysMessage.setCanReply((short)0);
			sysMessage.setIsPublic((short)0);
			sysMessage.setCreateTime(LocalDateTime.now());
			this.create(sysMessage);
			
			for (SysExecutor sysExecutor : receivers) {
				MessageReceiver receiver = new MessageReceiver();
				receiver.setId(UniqueIdUtil.getSuid());
				receiver.setMsgId(id);
				receiver.setReceiverId(sysExecutor.getId());
				receiver.setReceiver(sysExecutor.getName());
				receiver.setReceiverType(sysExecutor.getType());
				messageReceiverManager.create(receiver);
			}
			return new CommonResult<>(true, "发送消息成功!");
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	public void addMessageSend(SysMessage sysMessage) {
		String messageId=sysMessage.getId();
		String receiverIds = sysMessage.getReceiverId();
		String receiverNames = sysMessage.getReceiverName();
		String receiverOrgIds = sysMessage.getReceiverOrgId();
		String receiverOrgNames = sysMessage.getReceiverOrgName();
		IUser sender = ContextUtil.getCurrentUser();
		messageId = UniqueIdUtil.getSuid();
		sysMessage.setId(messageId);
		sysMessage.setOwnerId(sender.getUserId());
		sysMessage.setOwner(sender.getFullname());
		sysMessage.setCreateTime(LocalDateTime.now());
		
		//如果是公告，则不存在接收人，不在接收人表中添加信息，不能回复，
		if (sysMessage.getIsPublic()==SysMessage.iS_PUBLIC_YES) {
			this.create(sysMessage);
			return;//直接返回，不做以后操作
		}
		//把发送给组织的也设置给人员字段，保存到数据库
		String receriver = "";
		if (StringUtil.isNotEmpty(receiverNames)&&StringUtil.isNotEmpty(receiverOrgNames)) {
			receriver = receiverNames+","+receiverOrgNames;
		}else if (StringUtil.isNotEmpty(receiverNames)&&StringUtil.isEmpty(receiverOrgNames)) {
			receriver=receiverNames;
		}else if(StringUtil.isEmpty(receiverNames)&&StringUtil.isNotEmpty(receiverOrgNames)){
			receriver=receiverOrgNames;
		}
		sysMessage.setReceiverName(receriver);
		this.create(sysMessage);
		
		
		//转换人员
		if (StringUtil.isNotEmpty(receiverIds)) {
			String[] idArr = receiverIds.split(",");
			String[] nameArr = receiverNames.split(",");
			for (int i = 0; i < idArr.length; i++) {
				String id=idArr[i];
				if (StringUtil.isEmpty(id)) continue;
				String name="";
				if (nameArr.length>i)
					name=nameArr[i];
				MessageReceiver receiver = new MessageReceiver();
				receiver.setId(UniqueIdUtil.getSuid());
				receiver.setMsgId(messageId);
				receiver.setReceiverType(MessageReceiver.TYPE_USER);
				receiver.setReceiverId(id);
				receiver.setReceiver(name);
				messageReceiverManager.create(receiver);
			}
		}
		//转换组织
		if (StringUtil.isNotEmpty(receiverOrgIds)) {
			String[] orgIdArr = receiverOrgIds.split(",");
			String[] orgNameArr = receiverOrgNames.split(",");
			for (int i = 0; i < orgIdArr.length; i++) {
				String id=orgIdArr[i];
				if (StringUtil.isEmpty(id)) continue;
				String name="";
				if (orgNameArr.length>i)
					name=orgNameArr[i];
				MessageReceiver receiver = new MessageReceiver();
				receiver.setId(UniqueIdUtil.getSuid());
				receiver.setMsgId(messageId);
				receiver.setReceiverType(MessageReceiver.TYPE_GROUP);
				receiver.setReceiverId(id);
				receiver.setReceiver(name);
				messageReceiverManager.create(receiver);
			}
		}
	}
	/**
	 * 获取最新一条的未读的消息
	 */
	@Override
	public SysMessage getNotReadMsg(String userId) {
		String dbtype = SQLUtil.getDbType();
		if(dbtype.equals("db2")){
			return baseMapper.getOneNotReadMsgByUserIdDb2(userId);
			}else if(dbtype.equals("oracle")){
			return baseMapper.getOneNotReadMsgByUserIdOracl(userId);
			}else if(dbtype.equals("mssql")){
			return baseMapper.getOneNotReadMsgByUserIdMssql(userId);
			}else{
			return baseMapper.getOneNotReadMsgByUserIdMysql(userId);	
			}
	}
	
	/**
	 * 获取未读消息的条数
	 */
	@Override
	public int getNotReadMsgNum(String userId) {
		return baseMapper.getNotReadMsgNum(userId).size();
	}
	@Override
	public int getMsgSize(String receiverId) {
		return baseMapper.getMsgSize(receiverId).size();
	}
	
	
}
