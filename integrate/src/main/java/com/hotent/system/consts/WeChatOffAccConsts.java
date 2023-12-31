package com.hotent.system.consts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.hotent.base.util.AppUtil;
import com.hotent.system.model.SysExternalUnite;
import com.hotent.system.persistence.manager.SysExternalUniteManager;

/**
 * 微信公众号常量
 * 
 * @author pangquan
 *
 */
public class WeChatOffAccConsts {

	public final static String METHOD_GET = "GET";

	public final static String METHOD_POST = "POST";

	private final static String OPEN_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
	private final static String WX_URL = "https://api.weixin.qq.com/sns/oauth2";
	private final static String WX_Base_Url = "https://api.weixin.qq.com";

	public static SysExternalUnite getUnite() {
		SysExternalUniteManager manager = AppUtil.getBean(SysExternalUniteManager.class);
		return manager.getWeChatOfficialAccounts();
	}

	public static String generateMenuUrl(String baseUrl, String corpId) throws UnsupportedEncodingException {
		String redirectUri = baseUrl + "/weChatOffAcc?redirect=" + baseUrl + "/home";
		return OPEN_AUTHORIZE_URL + "?appid=" + corpId + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
				+ "&response_type=code&scope=snsapi_base&state=hotent#wechat_redirect";
	}

	/**
	 * 通过code appid secret 获取access_token
	 * 
	 * @param code
	 * @return
	 */
	public static String getWxAccessToken(String code) {
		SysExternalUnite unite = getUnite();
		String url = WX_URL + "/access_token?appid=" + unite.getCorpId() + "&secret=" + unite.getCorpSecret() + "&code="
				+ code + "&grant_type=authorization_code";
		return url;
	}

	/**
	 * 防止token超时refresh刷新 access_token
	 *
	 * @param appid
	 *            refreshToken
	 * @return
	 */
	public static String refreshToken(String refreshToken) {
		SysExternalUnite unite = getUnite();
		String url = WX_URL + "/refresh_token?appid=" + unite.getCorpId() + "&grant_type=refresh_token"
				+ "&refresh_token=" + refreshToken;
		return url;
	}

	/**
	 * 通过access_token openid获取用户基本信息
	 * 
	 * @param access_token
	 *            openid
	 *
	 * @return
	 */
	public static String getWxUserInfo(String access_token, String openid) {

		String url = WX_Base_Url + "/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
		return url;
	}

	/**
	 * 微信公众号发送模板消息
	 * 
	 * @return
	 */
	public static String send(String access_token) {
		String url = WX_Base_Url + "/cgi-bin/message/template/send?access_token=" + access_token;
		return url;
	}

	public static String getTokenUrl() {
		SysExternalUnite unite = getUnite();
		String url = WX_Base_Url + "/token?grant_type=client_credential&appid=" + unite.getCorpId() + "&secret="
				+ unite.getCorpSecret();
		return url;
	}

	public static String getWxAuthorize(String paramStr) throws UnsupportedEncodingException {
		SysExternalUnite unite = getUnite();
		String corpId = unite.getCorpId();
		String baseUrl = unite.getBaseUrl() + "/weChatOffAcc?params=" + paramStr;
		String redirect = URLEncoder.encode(baseUrl, "utf-8");
		String url = OPEN_AUTHORIZE_URL + "?appid=" + corpId + "&redirect_uri=" + redirect
				+ "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";

		return url;
	}
}
