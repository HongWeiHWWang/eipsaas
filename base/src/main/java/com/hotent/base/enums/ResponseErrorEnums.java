package com.hotent.base.enums;

/**
 * 请求错误枚举
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月4日
 */
public enum ResponseErrorEnums {
	SYSTEM_ERROR("-001","系统异常"),
	BAD_REQUEST("-002","错误的请求参数"),
	NOT_FOUND("-003","找不到请求路径"),
	CONNECTION_ERROR("-004","网络连接请求失败"),
	METHOD_NOT_ALLOWED("-005","不合法的请求方式"),
	DATABASE_ERROR("-004","数据库异常"),
	BOUND_STATEMENT_NOT_FOUNT("-006","找不到方法"),
	REPEAT_REGISTER("001","重复注册"),
	NO_USER_EXIST("002","用户不存在"),
	INVALID_PASSWORD("003","密码错误"),
	NO_PERMISSION("004","非法请求"),
	SUCCESS_OPTION("005","操作成功"),
	NOT_MATCH("-007","用户名和密码不匹配"),
	FAIL_GETDATA("-008","获取信息失败"),
	BAD_REQUEST_TYPE("-009","错误的请求类型"),
	REQUIRED_ERROR("-010","有必填的参数未传入"),
	FAIL_OPTION("-011","操作失败"),
	REPEAT_MOBILE("014","已存在此手机号"),
	REPEAT_EMAIL("015","已存在此邮箱地址"),
	NO_RECORD("016","没有查到相关记录"),
	LOGIN_SUCCESS("017","登陆成功"),
	LOGOUT_SUCCESS("018","已退出登录"),
	SENDEMAIL_SUCCESS("019","邮件已发送，请注意查收"),
	EDITPWD_SUCCESS("020","修改密码成功"),
	No_FileSELECT("021","未选择文件"),
	FILEUPLOAD_SUCCESS("022","上传成功"),
	NOLOGIN("023","未登陆"),
	ILLEGAL_ARGUMENT("024","参数不合法"),
	ERROR_IDCODE("025","验证码不正确"),
	CERT_ERROR("026","系统授权异常"),
	WORKFLOW_ERROR("027", "流程异常"),
	LOG_SAVE_ERROR("028","日志记录错误"),
	FEIGN_EMPTY_ERROR("029","Feign未找到对应微服务"),
	DATASOURCE_ERROR("030","数据源异常"),
	SERVICE_INVOKE_ERROR("031","服务接口调用异常"),
	WEBSERVICE_PARSE_ERROR("032","Webservice接口解析异常"),
	BPM_PROCESS("040","流程定义异常，不允许有多个process");

	private String code;
	private String message;
	
	private ResponseErrorEnums(String code, String message) {
		this.code = code;
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
