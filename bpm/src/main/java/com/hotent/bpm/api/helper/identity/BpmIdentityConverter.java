package com.hotent.bpm.api.helper.identity;

import java.util.List;

import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;

/**
 * 用户对象转换。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-8-上午9:21:50
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmIdentityConverter {
	
	/**
	 * 将用户对象转成BpmIdentity对象。
	 * @param user
	 * @return BpmIdentity
	 */
	BpmIdentity convertUser(IUser user);
	
	/**
	 * 将用户列表转换成BpmIdentity列表。
	 * @param userList
	 * @return 
	 * List&lt;BpmIdentity>
	 */
	List<BpmIdentity> convertUserList(List<IUser> userList);

	/**
	 * 将用户组转换成BpmIdentity。
	 * @param group
	 * @return BpmIdentity
	 */
	BpmIdentity convertGroup(IGroup group);

	/**
	 * 将用户组列表转换成BpmIdentity列表。
	 * @param groupList
	 * @return 
	 * List&lt;BpmIdentity>
	 */
	List<BpmIdentity> convertGroupList(List<IGroup> groupList);
	
	/**
	 * 将类型和ID转换成BpmIdentity对象。
	 * @param type
	 * @param id
	 * @return BpmIdentity
	 */
	BpmIdentity convert(String type,String id);
	/**
	 * 将类型和ID转换成BpmIdentity对象。
	 * @param type
	 * @param id
	 * @return BpmIdentity
	 */
	BpmIdentity convertValue(String type, String id);	
}
