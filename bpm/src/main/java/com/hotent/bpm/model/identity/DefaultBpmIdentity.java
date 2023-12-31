package com.hotent.bpm.model.identity;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import com.hotent.base.exception.BaseException;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.uc.api.model.IUser;

/**
 * 任务执行人。
 * <pre> 
 * 这个类有三个属性。
 * type：user，用户组。
 * id:人员的ID
 * name：人员名称
 * 构建组：x5-bpmx-root
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-10-下午11:06:33
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefaultBpmIdentity implements BpmIdentity,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4416404339210896051L;
	
	private String id;
	private String code;
	private String name;
	private String type;
	private String groupType;	
	private ExtractType extractType;
	
	public DefaultBpmIdentity(){
		
	}
	/**
	 * 
	 * 创建一个新的实例 DefaultBpmIdentity.
	 * @param id 
	 * @param name
	 * @param type
	 */
	public DefaultBpmIdentity(String id,String name,String type){
		this.id=id;
		this.name=name;
		this.type=type;
	}
	
	public static BpmIdentity  getIdentityByUserId(String userId,String userName){
		DefaultBpmIdentity identity=new DefaultBpmIdentity();
		identity.setId(userId);
		identity.setName(userName);
		identity.setType(TYPE_USER);
		return identity;
	}
	
	public DefaultBpmIdentity(IUser user){
		this.id=user.getUserId();
		this.name=user.getFullname();
		this.type=TYPE_USER;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getType() {
		return this.type;
	}
	
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public ExtractType getExtractType() {
		return extractType;
	}

	public void setExtractType(ExtractType extractType) {
		this.extractType = extractType;
	}
	
	@Override
	public String getCode() {
		return code;
	}
	@Override
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public int hashCode(){
		if(TYPE_USER.equals(this.type) || TYPE_GROUP_USER.endsWith(this.type)){
			if (StringUtil.isNotEmpty(this.id)) {
				return this.id.hashCode() + this.type.hashCode();
			}else {
				return this.code.hashCode() + this.type.hashCode();
			}
			
		}
		else if(TYPE_GROUP.equals(this.type)){
			if(StringUtil.isNotEmpty(this.id)) {
				return this.id.hashCode() + this.groupType.hashCode();
			}
			else if(StringUtil.isNotEmpty(this.code)) {
				return this.code.hashCode() + this.groupType.hashCode();
			}
			else {
				throw new BaseException("BpmIdentity的id或code中至少需要一个不为空值");
			}
		}
		return this.id.hashCode() + this.type.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		if(!( obj instanceof DefaultBpmIdentity)){
			return false;
		}
		DefaultBpmIdentity identity=(DefaultBpmIdentity)obj;
		if (BeanUtils.isEmpty(identity)) {
			return false;
		}
		//用户和组用户
		if(TYPE_USER.equals(this.type) || TYPE_GROUP_USER.endsWith(this.type)){
			
			if (StringUtil.isNotEmpty(identity.getId())) {
				return identity.getId().equals(this.id);
			}else {
				return identity.getCode().equals(this.code);
			}
		}
		//用户组。
		else if(TYPE_GROUP.equals(this.type)){
			boolean tmp= false ;
			if (StringUtil.isNotEmpty(identity.getId())) {
				tmp= identity.getId().equals(this.id) ;
			}else {
				tmp=  identity.getCode().equals(this.code);
			}
			return tmp && identity.getGroupType().equals(this.getGroupType());
		}
		
		return false;
	}

	
	public static void main(String[] args) {
		DefaultBpmIdentity id1=new DefaultBpmIdentity();
		id1.setId("1");
		id1.setType("user");
		
		DefaultBpmIdentity id2=new DefaultBpmIdentity();
		id2.setId("1");
		id2.setType("user");
		
		Set<DefaultBpmIdentity> list=new LinkedHashSet<DefaultBpmIdentity>();
		list.add(id1);
		list.add(id2);
		
		//list.remove(id2);
		
		System.out.println(list.size());
	}
}
