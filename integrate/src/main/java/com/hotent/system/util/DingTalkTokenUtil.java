package com.hotent.system.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.system.consts.DingTalkConsts;
import com.hotent.system.model.TokenModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * <pre>
 * 描述：阿里钉钉accessToken工具类
 * 构建组：x5-bpmx-platform
 * 作者:PangQuan
 * 邮箱:PangQuan@jee-soft.cn
 * 日期:2019-12-03 10:30
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class DingTalkTokenUtil {
	protected static Logger log = LoggerFactory.getLogger(DingTalkTokenUtil.class);
	/**
	 * H5微应用的token
	 */
	private static TokenModel model=new TokenModel();

	/**
	 * 获取钉钉accessToken。
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
	 * https请求钉钉api获取token。
	 * @return
	 */
	private static String requestToken() throws IOException {
		String url= DingTalkConsts.getTokenUrl();
		String rtn= HttpUtil.sendHttpsRequest(url, "", "GET");
		JsonNode jsonObj = JsonUtil.toJsonNode(rtn);
		//取到了
		if(jsonObj.get("errcode").asInt()==0){
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
