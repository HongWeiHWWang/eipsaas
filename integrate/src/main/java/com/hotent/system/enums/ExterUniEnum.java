package com.hotent.system.enums;
/**
*
* <pre>
* 描述：系统第三方集成 类型枚举
* 构建组：x5-bpmx-platform
* 作者:PangQuan
* 邮箱:PangQuan@jee-soft.cn
* 日期:2019-11-27 10:09:07
* 版权：广州宏天软件有限公司
* </pre>
*/
public enum ExterUniEnum {
	/**
	 * 企业微信
	 */
	WeChatWork("weChatWork","企业微信"),
	/**
	 * 阿里钉钉
	 */
	Dingtalk("dingtalk","阿里钉钉"),
	/**
	 * 微信公众号
	 */
	WeChatOfficialAccounts("weChatOffAcc","微信公众号");



	private String key;
	private String label;

	ExterUniEnum(String key, String label){
		this.setKey(key);
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public static String getLabelByKey(String key){
		for(ExterUniEnum en : ExterUniEnum.values()){
			if(en.getKey().equals(key)){
				return en.getLabel();
			}
		}
		return "";
	}
}
