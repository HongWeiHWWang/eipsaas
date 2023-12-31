package com.hotent.im.util;

public class ImConstant {
	
	
	public static final short IM_SESSION_DELETE = 1;
	
	//默认讨论组的标题分隔符
	public static final String SESSION_TITLE_DELIMITER = "、";
	
	//系统会话的默认icon
	public static final String SYSTEM_SESSION_ICON = "/mobile/img/sys_message_icon.png";
	
	//系统消息用到的帐号
	public static final String SYSTEM_SESSION_ACCOUNT = "im_admin";
		
	//显示会话
	public static final short SESSION_SHOW = 1;
	
	//隐藏会话
	public static final short SESSION_HIDE = 0;

	//群聊
	public static final String SESSION_SCENE_TEAM = "team";
	
	//点对点聊天
	public static final String SESSION_SCENE_P2P = "p2p";
	
	//默认的下载路径
	public static final String DEFAULT_DOWNLOAD_URL = "/system/file/download?id=";
	
//	//默认帐号
//	public final static String userName = "admin";
//	//默认帐号密码
//	public final static String password = "123";
//	//后台接受消息默认的clientId
//	public final static String MQTT_RECEIVE_CLIENTID = "mqttReceiveClientId";
//	//后台发送消息默认的clientId
//	public final static String MQTT_SEND_CLIENTID = "mqttSendClientId";
//	//mqtt服务器地址_本地
//	//public final static String HOST = "tcp://192.168.1.126:1883";
//	//mqtt服务器地址_21
//	public final static String HOST = "tcp://192.168.1.21:1883";
//	
	//后台接收消息的Destination
	public final static String MQTT_RECEIVE_DESTINATION = "mqttReceiveDestination";
	//后台发送消息的Destination
	public final static String MQTT_SEND_DESTINATION = "mqttSendDestination";
	//会话编号公共密钥
	public final static String DEFUALT_SESSION_SECRET = "default_secret";
	
	//每次请求历史记录的数目
	public final static short MESSAGE_HISTORY_LIMIT = 100;
	
	//下线
	public final static short USER_STATUS_OFFLINE = 0;
	//上线
	public final static short USER_STATUS_ONLINE = 1;
	
	public final static String MESSAGE_TYPE_SYSTEM = "system";

}
