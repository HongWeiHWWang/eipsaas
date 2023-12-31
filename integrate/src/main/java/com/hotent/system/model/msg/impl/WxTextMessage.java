package com.hotent.system.model.msg.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hotent.base.util.JsonUtil;
import com.hotent.system.model.msg.WxBaseMessage;

/**
 * 文本消息对象。
 * <pre>
 * {
   "touser": "UserID1|UserID2|UserID3",
   "toparty": " PartyID1 | PartyID2 ",
   "totag": " TagID1 | TagID2 ",
   "msgtype": "text",
   "agentid": "1",
   "text": {
       "content": "Holiday Request For Pony(http://xxxxx)"
   },
   "safe":"0"
}</pre>
 * @author ray
 *
 */
public class WxTextMessage extends WxBaseMessage {
    private static final Log logger= LogFactory.getLog(WxTextMessage.class);
	public WxTextMessage(){}
	
	public WxTextMessage(String toUser,String toParty,String msg,String agentid){
		super.setTouser(toUser);
		super.setToparty(toParty);
		this.setText(msg);
		super.setAgentid(agentid);
	}
	
	public String getMsgtype() {
		return "text";
	}
	
	private MsgContent text;
	
	public MsgContent getText() {
		return text;
	}

	public void setText(String text) {
		this.text = new MsgContent(text);
	}



	class MsgContent {
		private String content;

		public MsgContent(String content) {
			this.content = content;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}



	@Override
	public String toString() {
        String msgStr = "";
        try {
            msgStr = JsonUtil.toJson(this);
        }catch (Exception e){
            logger.error(e);
        }

		return msgStr;
	}
	
	
	public static void main(String[] args) {
		WxTextMessage message=new WxTextMessage("zyg","","hello zyg","1");
		message.setAgentid("1");
		System.out.println(message);
	}
	

}
