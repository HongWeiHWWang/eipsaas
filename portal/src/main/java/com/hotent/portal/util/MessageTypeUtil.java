package com.hotent.portal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.util.ResourceUtils;

import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;


public class MessageTypeUtil {
	private Document doc=null;
	private static MessageTypeUtil config=null;
	
	private static Lock lock = new ReentrantLock();

	private MessageTypeUtil() throws FileNotFoundException
	{
		File file = ResourceUtils.getFile("classpath:conf/messageType.xml");
		InputStream	is = new FileInputStream(file);
		doc=Dom4jUtil.loadXml(is);
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 单例模式，获取类的示例。
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static MessageTypeUtil getInstance() throws FileNotFoundException
	{
		if(config==null)
		{
			lock.lock();
			try{
				if(config==null)
					config=new MessageTypeUtil();
			}
			finally{
				lock.unlock();
			}
		}
		return config;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getMap(String type){
		Map<String, String> msgMap=new HashMap<String, String>(); 
		Element root = doc.getRootElement();
		Element messageEl = (Element) root.selectSingleNode("message");
		List<Element> elements = messageEl.elements();
		for (int i = 0; i < elements.size(); i++) {
			Element message =  elements.get(i);
			if(StringUtil.isNotEmpty(type)){
				String typeValue = message.attributeValue(type);
				if ("1".equals(typeValue)) {
					String name = message.attributeValue("name");
					String key = message.attributeValue("key");
					msgMap.put(key, name);
				}
			}else {
				String name = message.attributeValue("name");
				String key = message.attributeValue("key");
				msgMap.put(key, name);
			}
			
		}
		return msgMap;
	}
	
	private  String getVal(String key){
		String template="message/msgtype[@key='%s']";
		String filter=String.format(template,key);
		Element root= doc.getRootElement();
		Element el=(Element)root.selectSingleNode(filter);
		if(el!=null)
			return el.attributeValue("name");
		return "系统消息";
	}
	
	public static Map<String, String> getDisPlayMsgType() throws FileNotFoundException{
		return MessageTypeUtil.getInstance().getMap("display");
	}
	
	
	public static Map<String, String> getReplyMsgType() throws FileNotFoundException{
		return MessageTypeUtil.getInstance().getMap("reply");
	}
	
	public static Map<String, String> getAllMsgType() throws FileNotFoundException{
		return MessageTypeUtil.getInstance().getMap("");
	}
	
	public  static String  getValue(String key) throws FileNotFoundException {
		return MessageTypeUtil.getInstance().getVal(key);
	}
	
	
	
	
	
	

}
