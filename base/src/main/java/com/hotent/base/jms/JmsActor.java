package com.hotent.base.jms;

import java.io.Serializable;

/**
 * jms消息的参与者
 *
 * <pre>
 * 参与者主要指jms消息的发送人、收信人、抄送人、密送人等
 * </pre>
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月10日
 */
public class JmsActor implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;			/*用户ID*/
    private String account;		/*用户账号*/
    private String name;		/*用户名称*/
    private String email;		/*用户邮箱*/
    private String mobile;		/*用户手机*/
    private String weixin;		/*用户openid*/

    public JmsActor() {}

    public JmsActor(String id, String account, String name, String email, String mobile, String weixin) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.weixin = weixin;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
