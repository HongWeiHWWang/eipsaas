package com.hotent.bpm.api.model.process.nodedef;

import java.io.Serializable;

/**
 * 跳转规则。
 * <pre> 
 * 描述：跳转规则。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-27-上午8:53:15
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface JumpRule extends Serializable{

	/**
	 * 规则名称。
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	 String getRuleName() ;

	 /**
	  * 跳转到的节点。
	  * @return 
	  * String
	  * @exception 
	  * @since  1.0.0
	  */
	 String getTargetNode() ;

	
	 /**
	  * 规则的条件。
	  * @return 
	  * String
	  * @exception 
	  * @since  1.0.0
	  */
	 String getCondition();

	
}
