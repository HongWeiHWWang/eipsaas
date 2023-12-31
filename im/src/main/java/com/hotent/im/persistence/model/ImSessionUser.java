package com.hotent.im.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.model.BaseModel;


 /**
 * 
 * <pre> 
 * 描述：会话用户 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:19
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public class ImSessionUser extends BaseModel<String>{
	
	/**
	* ID
	*/
	protected String id; 
	
	/**
	* 用户别名
	*/
	protected String userAccount; 
	
	/**
	* 用户别名
	*/
	protected String userAlias; 
	
	/**
	* 是否显示
	*/
	protected Short isShow; 
	
	/**
	* 最后阅读时间
	*/
	protected java.util.Date lastReadTime; 
	
	/**
	* 加入时间
	*/
	protected java.util.Date joinTime; 
	
	/**
	* 来源(创建、邀请人）
	*/
	
	protected String from; 
	
	protected String photo;
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}
	
	/**
	 * 返回 用户别名
	 * @return
	 */
	public String getUserAlias() {
		return this.userAlias;
	}
	
	public void setIsShow(Short isShow) {
		this.isShow = isShow;
	}
	
	/**
	 * 返回 是否显示
	 * @return
	 */
	public Short getIsShow() {
		return this.isShow;
	}
	
	public void setLastReadTime(java.util.Date lastReadTime) {
		this.lastReadTime = lastReadTime;
	}
	
	/**
	 * 返回 最后阅读时间
	 * @return
	 */
	public java.util.Date getLastReadTime() {
		return this.lastReadTime;
	}
	
	public void setJoinTime(java.util.Date joinTime) {
		this.joinTime = joinTime;
	}
	
	/**
	 * 返回 加入时间
	 * @return
	 */
	public java.util.Date getJoinTime() {
		return this.joinTime;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * 返回 来源(创建、邀请人）
	 * @return
	 */
	public String getFrom() {
		return this.from;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("userAccount", this.userAccount) 
		.append("userAlias", this.userAlias) 
		.append("isShow", this.isShow) 
		.append("lastReadTime", this.lastReadTime) 
		.append("joinTime", this.joinTime) 
		.append("from", this.from) 
		.toString();
	}
	
	
	//会话编号
	protected String sessionCode;
	//会话标题
	protected String sessionTitle;
	//会话类型
	protected String sessionScene;
	//会话图标
	protected String sessionIcon;
	//会话最后一条消息
	protected String sessionLastText;
	//会话未读数
	protected short sessionUnRead;
	//会话创建人
	protected String sessionOwner;
	//会话的人
	protected String targetUser;

	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public String getSessionOwner() {
		return sessionOwner;
	}

	public void setSessionOwner(String sessionOwner) {
		this.sessionOwner = sessionOwner;
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}

	public String getSessionScene() {
		return sessionScene;
	}

	public void setSessionScene(String sessionScene) {
		this.sessionScene = sessionScene;
	}

	public String getSessionIcon() {
		return sessionIcon;
	}

	public void setSessionIcon(String sessionIcon) {
		this.sessionIcon = sessionIcon;
	}

	public String getSessionLastText() {
		return sessionLastText;
	}

	public void setSessionLastText(String sessionLastText) {
		this.sessionLastText = sessionLastText;
	}

	public short getSessionUnRead() {
		return sessionUnRead;
	}

	public void setSessionUnRead(short sessionUnRead) {
		this.sessionUnRead = sessionUnRead;
	}
	
}