package com.hotent.system.model.msg.impl;

import com.hotent.system.model.msg.WxBaseMessage;

/**
 * 企业微信的文本卡片消息
 * 
 * @author pangq
 *
 */
public class WxTextCardMessage extends WxBaseMessage {
	
	public WxTextCardMessage() {
	}
	public WxTextCardMessage(String toUser,String toParty,String agentid,String title,
			String description1,String description2,String description3,String url,String btntxt){
		super.setTouser(toUser);
		super.setToparty(toParty);
		super.setAgentid(agentid);
		
		//文本卡片内容体：分三段
		//<div class=\"gray\">2016年9月26日</div> <div class=\"normal\">恭喜你抽中iPhone 7一台，领奖码：xxxx</div><div class=\"highlight\">请于2016年10月10日前联系行政同事领取</div>
		description1 = "<div class=\"gray\">"+description1+"</div>";
		description2 = "<div class=\"normal\">"+description2+"</div>";
		description3 = "<div class=\"highlight\">"+description3+"</div>";
		Textcard textcard = new Textcard(title, description1+description2+description3, url, btntxt);
		this.setTextcard(textcard);
	}

	
	public String getMsgtype() {
		return "textcard";
	}

	public Textcard textcard;
	

	public Textcard getTextcard() {
		return textcard;
	}

	public void setTextcard(Textcard textcard) {
		this.textcard = textcard;
	}
	public void setTextcard(String title,String description,String url,String btntxt) {
		this.textcard = new Textcard(title, description, url, btntxt);
	}


	//内部类
	class Textcard {
		//标题，不超过128个字节，超过会自动截断（支持id转译）
		private String title;
		//描述，不超过512个字节，超过会自动截断（支持id转译）
		private String description;
		//点击后跳转的链接
		private String url;
		//按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断
		private String btntxt;
		
		public Textcard(String title, String description, String url, String btntxt) {
			super();
			this.title = title;
			this.description = description;
			this.url = url;
			this.btntxt = btntxt;
		}

		public Textcard() {
			super();
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getBtntxt() {
			return btntxt;
		}

		public void setBtntxt(String btntxt) {
			this.btntxt = btntxt;
		}
	}

}
