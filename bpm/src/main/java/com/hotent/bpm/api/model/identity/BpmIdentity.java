package com.hotent.bpm.api.model.identity;

import com.hotent.bpm.api.constant.ExtractType;


/**
 * 
 * 描述：流程与组织挂接实体接口
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-14-下午3:01:09
 * 版权：广州宏天软件有限公司版权所有
 */
 public interface BpmIdentity {
	 
	 /**
	  * 用户。
	  */
	 public final static String TYPE_USER="user";
	 /**
	  * 组用户。
	  */
	 public final static String TYPE_GROUP_USER="groupUser";
	 
	 /**
	  * 用户组。
	  */
	 public final static String TYPE_GROUP="group";
	
	 public final static String GROUP_TYPE_ORG="org";
	 public final static String GROUP_TYPE_JOB="job";
	 public final static String GROUP_TYPE_POSITION="position";
	 public final static String GROUP_TYPE_ROLE="role";
	 
	 /**
	  * 实体ID
	  * @return  String
	  */
	 String getId();
	 
	 /**
	  * 设置实体ID 
	  * @param id 
	  * void
	  */
	 void setId(String id);
	 
	 /**
	  * 实体名称
	  * @return  String
	  */
	 String getName();
	 
	 /**
	  * 设置实体名称。
	  * @param name 
	  * void
	  */
	 void setName(String name);
	 
	 /**
	  * 实体分类,分为一下三种类型。
	  * <pre>
	  * user
	  * group
	  * groupUser
	  * </pre>
	  * @return  String
	  */
	 String getType();
	 
	 /**
	  * 实体分类。
	  * @param type 
	  * void
	  */
	 void setType(String type);
	 
	 /**
	  * 用户组类型 - 当实体分类是Group时，可以通过这方法扩展用户组类型。
	  * 如:
	  * org:组织
	  * role:角色
	  * pos:岗位
	  * @return  String
	  */
	 String getGroupType();
	 
	 /**
	  * 设置组类型。 
	  * @param groupType 
	  * void
	  */
	 void setGroupType(String groupType);
	 
	 /**
	  * 抽取类型
	  * @return  ExtractType
	  */
	 ExtractType getExtractType();
	 
	 /**
	  * 设置抽取类型。
	  * @param extractType 
	  * void
	  */
	 void setExtractType(ExtractType extractType);

	 void setCode(String code);

	 String getCode();
	 

}
