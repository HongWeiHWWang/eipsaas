package com.hotent.system.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.system.consts.WeChatWorkConsts;
import com.hotent.system.model.TokenModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * <pre>
 * 描述：企业微信accessToken工具类
 * 构建组：x5-bpmx-platform
 * 作者:PangQuan
 * 邮箱:PangQuan@jee-soft.cn
 * 日期:2019-11-28 16:07:01
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class WechatWorkTokenUtil {
	protected static Logger log = LoggerFactory.getLogger(WechatWorkTokenUtil.class);
	/**
	 * 同步通讯录的token
	 */
	private static TokenModel model=new TokenModel();
	/**
	 * 应用的token
	 */
	private static TokenModel agentModel=new TokenModel();

	/**
	 * 获取企业微信accessToken。
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
	 * https请求企业微信api获取token。
	 * @return
	 */
	private static String requestToken() throws IOException {
		String url= WeChatWorkConsts.getTokenUrl();
		String rtn= HttpUtil.sendHttpsRequest(url, "", "GET");
		JsonNode jsonObj = JsonUtil.toJsonNode(rtn);
		System.out.println(rtn);
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
	/**
	 * 获取应用accessToken。
	 * <pre>
	 * 1.如果没有初始化则远程请求获取。
	 * 2.如果已经初始化并且未过期，直接获取。
	 * 1.如果已经过期则重新获取。
	 * </pre>
	 * @return
	 */
	public static synchronized String getAgentToken() throws IOException {
		//没有初始化直接获取。
		if(!agentModel.isInit()){
			String token=requestAgentToken();
			return token;
		}
		else{
			//如果token要过期则重新获取。
			if(agentModel.isExpire(agentModel.getLastUpdTime(),agentModel.getExprieIn())){
				String token=requestAgentToken();
				return token;
			}
			else{
				//从缓存中获取。
				return agentModel.getToken();
			}
		}
	}
	/**
	 * https请求获取应用token。
	 * @return
	 */
	private static String requestAgentToken() throws IOException {
		String url=WeChatWorkConsts.getAgentToKenUrl();
		String rtn=HttpUtil.sendHttpsRequest(url, "", "GET");
		JsonNode jsonObj = JsonUtil.toJsonNode(rtn);
		System.out.println(rtn);
		//取到了
		if(jsonObj.get("errcode").asInt()==0){
			String token=jsonObj.get("access_token").asText();
			int expireIn=jsonObj.get("expires_in").asInt();
			agentModel.setCorpToken(token, expireIn);
			return token;
		}
		//获取失败
		else{
			agentModel.setInit(false);
			String errMsg=jsonObj.get("errmsg").asText();
			log.error(errMsg);
			throw new RuntimeException("获取accessToken失败:<br>"+errMsg);
		}
	}

}
