package com.hotent.system.model.msg.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hotent.base.util.JsonUtil;
import com.hotent.system.model.WxArticle;
import com.hotent.system.model.msg.WxBaseMessage;

/**
 * news消息
 * <pre>
 * {
   "touser": "UserID1|UserID2|UserID3",
   "toparty": " PartyID1 | PartyID2 ",
   "totag": " TagID1 | TagID2 ",
   "msgtype": "news",
   "agentid": "1",
   "news": {
       "articles":[
           {
               "title": "Title",
               "description": "Description",
               "url": "URL",
               "picurl": "PIC_URL"
           },
           {
               "title": "Title",
               "description": "Description",
               "url": "URL",
               "picurl": "PIC_URL"
           }    
       ]
   }
}
 * </pre>
 * @author ray
 *
 */
public class WxNewsMessage extends WxBaseMessage {
    private static final Log logger= LogFactory.getLog(WxNewsMessage.class);
	private List<WxArticle> news=new ArrayList<WxArticle>();
	
	public String getMsgtype() {
		return "news";
	}

	public List<WxArticle> getNews() {
		return news;
	}

	public void setNews(List<WxArticle> news) {
		this.news = news;
	}
	
	public void addArticle(WxArticle article){
		this.news.add(article);
	}


	@Override
	public String toString(){
        String json = "{\"touser\": \"%s\",\"toparty\": \"%s\",\"msgtype\": \"news\",\"agentid\": \"%s\",\"news\": {\"articles\":%s}}";

        try {
            String newsJson=JsonUtil.toJson(this.news);
            json=String.format(json, this.getTouser(),this.getToparty(),this.getAgentid(),newsJson);
        }catch (Exception e){
            logger.error(e);
        }
        return json;
	}




	public static void main(String[] args) {
		WxNewsMessage message=new WxNewsMessage();
		message.setTouser("zyg");
		message.setAgentid("13");
		WxArticle a1=new WxArticle("通知","通知","http://www.ifeng.com","");
		WxArticle a2=new WxArticle("兄弟我要请假","兄弟我要请假","http://www.163.com","");
		message.addArticle(a1);
		message.addArticle(a2);
		System.out.println(message);
	}
}
