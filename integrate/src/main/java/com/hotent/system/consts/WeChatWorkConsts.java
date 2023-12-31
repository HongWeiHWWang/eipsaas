package com.hotent.system.consts;
/**
 * 微信公众号常量
 */



import com.hotent.base.util.AppUtil;
import com.hotent.system.model.SysExternalUnite;
import com.hotent.system.persistence.manager.SysExternalUniteManager;
import com.hotent.system.util.WechatWorkTokenUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * <pre>
 * 描述：集成企业微信的api常量
 * 构建组：x5-bpmx-platform
 * 作者:PangQuan
 * 邮箱:PangQuan@jee-soft.cn
 * 日期:2019-11-26 16:07:01
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class WeChatWorkConsts {
	
	public final static String METHOD_GET="GET";
	
	public final static String METHOD_POST="POST";

	private final static String QIYE_URL = "https://qyapi.weixin.qq.com/cgi-bin";
	private final static String OPEN_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";


	public static SysExternalUnite getUnite(){
		SysExternalUniteManager manager= AppUtil.getBean(SysExternalUniteManager.class);
		return manager.getWechatWork();
	}
	/**
	 * 获取通讯录 api地址。
	 * @return
	 */
	public static String getTokenUrl() {
		SysExternalUnite unite = getUnite();
		return QIYE_URL + "/gettoken?corpid=" + (unite==null ? "":unite.getCorpId())
				+ "&corpsecret=" + (unite==null ? "":unite.getCorpSecret()) ;
	}
	/**
	 * 获取应用的凭证
	 * @return
	 */
	public static String getAgentToKenUrl() {
		SysExternalUnite unite = getUnite();
		return QIYE_URL + "/gettoken?corpid=" + (unite==null ? "":unite.getCorpId())
				+ "&corpsecret=" + (unite==null ? "":unite.getAgentSecret());
	}

	public static String generateMenuUrl(String baseUrl,String corpId) throws UnsupportedEncodingException{
		String redirectUri =baseUrl+"/weChat?redirect="+baseUrl+"/home";
		return OPEN_AUTHORIZE_URL+"?appid="+ corpId +
								   "&redirect_uri="+ URLEncoder.encode( redirectUri, "UTF-8" ) +
								   "&response_type=code&scope=snsapi_base&state=#wechat_redirect";
	}

	public static String getQyWxUserInfo(String code) throws IOException {
		String accessToken = WechatWorkTokenUtil.getAgentToken();
		String url = QIYE_URL + "/user/getuserinfo?access_token=" + accessToken + "&code=" + code;
		return url;
	}
	/**
	 * 根据用户帐号获取用户信息地址
	 *
	 * @return
	 */
	public static String getUserUrl() throws IOException {
		String url = QIYE_URL + "/user/get?access_token=" + WechatWorkTokenUtil.getToken() + "&userid=";
		return url;
	}
	public static String getDeleteUserUrl() throws IOException {
		String url = QIYE_URL + "/user/delete?access_token=" + WechatWorkTokenUtil.getToken() + "&userid=";
		return url;
	}
	public static String getCreateUserUrl() throws IOException {
		String url = QIYE_URL + "/user/create?access_token=" + WechatWorkTokenUtil.getToken();
		return url;
	}
	public static String getUpdateUserUrl() throws IOException {
		String url = QIYE_URL + "/user/update?access_token=" + WechatWorkTokenUtil.getToken();
		return url;
	}
	public static String getDeleteAllUserUrl() throws IOException {
		String url = QIYE_URL + "/user/batchdelete?access_token=" + WechatWorkTokenUtil.getToken();
		return url;
	}
	public static String getInviteUserUrl() throws IOException {
		String url = QIYE_URL + "/invite/send?access_token=" + WechatWorkTokenUtil.getToken();
		return url;
	}
	/**
	 * 获取创建应用菜单的url地址
	 * @return
	 */
	public static String getCreateAgentMenuUrl(String agentId) throws IOException {
		String url = QIYE_URL + "/menu/create?access_token=" + WechatWorkTokenUtil.getAgentToken()+"&agentid="+agentId;
		return url;
	}
	public static String getCreateOrgUrl() throws IOException {
		String url = QIYE_URL + "/department/create?access_token=" + WechatWorkTokenUtil.getToken();
		return url;
	}
	public static String getUpdateOrgUrl() throws IOException {
		String url = QIYE_URL + "/department/update?access_token=" + WechatWorkTokenUtil.getToken();
		return url;
	}
	public static String getDeleteOrgUrl() throws IOException {
		String url = QIYE_URL + "/department/delete?access_token=" + WechatWorkTokenUtil.getToken() + "&id=";
		return url;
	}
	/**
	 * 获取部门成员
	 *
	 * @return
	 */
	public static String getDepartmentUrl(String department_id) throws IOException {
		String url = QIYE_URL + "/user/simplelist?access_token=" + WechatWorkTokenUtil.getToken() + "&department_id="
				+ department_id;
		return url;
	}
	public static String getSendMsgUrl() throws IOException {
		String url = QIYE_URL + "/message/send?access_token=" + WechatWorkTokenUtil.getAgentToken();
		return url;
	}
	
	/**
	 * 获取部门列表
	 * @return
	 * @throws IOException 
	 */
	public static String getDepartmentListUrl(String department_id) throws IOException {
		String url =QIYE_URL +"/department/list?access_token="+WechatWorkTokenUtil.getToken()+"&id="+department_id;
		return url;
	}
	/**
	 * 获取部门成员详情
	 * @param department_id 获取的部门id
	 * @param fetch_child 是否递归获取子部门下面的成员：1-递归获取，0-只获取本部门
	 * @return
	 * @throws IOException
	 */
	public static String getUsersByDepartmentId(String department_id ,String fetch_child) throws IOException {
		String url =QIYE_URL +"/user/list?access_token="+WechatWorkTokenUtil.getToken()+"&department_id="+department_id+"&fetch_child="+fetch_child;
		return url;
	}
	
	/**
	 * 获取微信验证地址。
	 * @param redirectUrl
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getWxAuthorize(String paramStr) throws UnsupportedEncodingException{
        SysExternalUnite unite = getUnite();
		String corpId=unite.getCorpId();
		String agentid= unite.getAgentId();
		String baseUrl = unite.getBaseUrl() + "/weChat?params="+paramStr;
		String redirect=URLEncoder.encode(baseUrl, "utf-8");
		String url=OPEN_AUTHORIZE_URL+"?appid="+corpId+"&redirect_uri="+redirect+"&agentid="+agentid+"&response_type=code&scope=snsapi_base#wechat_redirect";
		return url;
		}

}
