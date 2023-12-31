package com.hotent.bpm.api.helper.identity;

import java.util.List;

import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre> 
 * 描述：针对BpmIdentity进行的用户抽取处理服务
 * 构建组：x5-bpmx-api
 * 作者：winston yan
 * 邮箱:yancm@jee-soft.cn
 * 日期:2014-4-15-下午2:14:05
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmIdentityExtractService {
	/**
	 * 根据实体集合（实体都是用户类型的）构成组用户。
	 * @param bpmIdentities
	 * @return 
	 * List&lt;BpmIdentity>
	 */
	public List<BpmIdentity> extractUserGroup(List<BpmIdentity> bpmIdentities);
	
	/**
	 * 根据BpmIdentity列表抽取成用户类型(BpmIdentity)的列表数据。
	 * @param bpmIdentities 都是用户类型
	 * @return 
	 * List&lt;BpmIdentity>
	 */
	public List<BpmIdentity> extractBpmIdentity(List<BpmIdentity> bpmIdentities);
	
	
	/**
	 * 抽取执行人。
	 * @param bpmIdentities
	 * @return 
	 * List&lt;User>
	 */
	public List<IUser> extractUser(List<BpmIdentity> bpmIdentities);
	
	

	
	
}
