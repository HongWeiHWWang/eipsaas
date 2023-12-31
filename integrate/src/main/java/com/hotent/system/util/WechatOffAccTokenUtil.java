package com.hotent.system.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.system.consts.WeChatOffAccConsts;
import com.hotent.system.consts.WeChatWorkConsts;
import com.hotent.system.model.TokenModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * <pre>
 * 描述：微信公众号accessToken工具类
 * 构建组：x7
 * 作者:pangq
 * 邮箱:pangq@jee-soft.cn
 * 日期:2020-05-07 14:25:01
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class WechatOffAccTokenUtil {
	protected static Logger log = LoggerFactory.getLogger(WechatOffAccTokenUtil.class);
	
	private static TokenModel model=new TokenModel();

	/**
	 * 获取微信公众号accessToken。
	 * <pre>
	 * 1.如果没有初始化则远程请求获取。
	 * 2.如果已经初始化并且未过期，直接获取。
	 * 1.如果已经过期则重新获取。
	 * </pre>
	 * @return
	 */
	public static synchronized String getToken() throws IOException {
		//没有初始化直接获取。
		if(!model.isInit()){
			String token=requestToken();
			return token;
		}
		else{
			//如果token要过期则重新获取。
			if(model.isExpire(model.getLastUpdTime(),model.getExprieIn())){
				String token=requestToken();
				return token;
			}
			else{
				//从缓存中获取。
				return model.getToken();
			}
		}
	}
	/**
	 * https请求微信公众号api获取token。
	 * @return
	 */
	private static String requestToken() throws IOException {
		String url= WeChatOffAccConsts.getTokenUrl();
		String rtn= HttpUtil.sendHttpsRequest(url, "", "GET");
		JsonNode jsonObj = JsonUtil.toJsonNode(rtn);
		System.out.println(rtn);
		//取到了
		if(BeanUtils.isNotEmpty(jsonObj.get("access_token"))){
			String token=jsonObj.get("access_token").asText();
			int expireIn=jsonObj.get("expires_in").asInt();
			model.setCorpToken(token, expireIn);
			return token;
		}
		//获取失败
		else{
			model.setInit(false);
			String errMsg=jsonObj.get("errmsg").asText();
			log.error(errMsg);
			throw new RuntimeException("获取accessToken失败:<br>"+errMsg);
		}
	}

}
