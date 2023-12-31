package com.hotent.bpm.api.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.DataType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.NodeDefTransient;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 实现ActionCmd基础接口。
 * <pre> 
 * 描述：其他的流程和任务命令可继承自该类。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-27-下午11:30:06
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BaseActionCmd implements ActionCmd {
	private Map<String, Object> vars_ =new HashMap<String, Object>();
	
	private Map<String, Object> transitVars_ =new HashMap<String, Object>();
	
	private Map<String, List<BpmIdentity>> identityMap_ =new HashMap<String, List<BpmIdentity>>();
	
	private String destinition="";
	private String  busData="";
	
	/**
	 * 动作名称。
	 */
	private String actionName="";
	/**
	 * 需要dataMode 为pair 。
	 * 业务数据对。
	 */
	private Map<String,String> dataPair=new HashMap<String, String>();

	private List<NodeDefTransient>  gatewayNodes=new ArrayList<NodeDefTransient>();
 
	/**
	 * 流程实例ID
	 */
	private String instId="";
	/**
	 * 通知类型
	 */
	private String notifyType="";
	/**
	 * 主键类型
	 */
	private DataType pkDataType=DataType.STRING;
	
	/**
	 * 业务主键。
	 */
	private String businessKey="";
	
	/**
	 * 业务系统编码。
	 */
	private String sysCode="";
	
	/**
	 * 数据模式
	 * <pre>
	 * bo:bo业务数据。
	 * pair:键值对模式(业务表，主键值)
	 * pk:业务主键
	 * </pre>
	 */
	private String dataMode="";
	
	
    /**
     * 被代理的领导id
     */
    private String agentLeaderId;
	
	/**
	 * 设置当前帐号。
	 */
	private String curAccount="";

	@Override
	public Map<String, Object> getVariables() {
		return vars_;
	}

	@Override
	public void addVariable(String name, String value) {
		this.vars_.put(name, value);

	}
	
	public void setVariables(Map<String, Object> vars){
		this.vars_=vars;
	}
	
	public void cleanVariables(){
		this.vars_.clear();
	}
	
	public void setBpmIdentities(Map<String, List<BpmIdentity>> map){
		this.identityMap_=map;
	}
	
	public void clearBpmIdentities(){
		this.identityMap_.clear();
	}
	
	public void addBpmIdentity(String key,BpmIdentity bpmIdentity){
		List<BpmIdentity> list= this.identityMap_.get(key);
		if(BeanUtils.isEmpty(list)){
			list=new ArrayList<BpmIdentity>();
			if(!list.contains(bpmIdentity)) {				
				list.add(bpmIdentity);
			}
			this.identityMap_.put(key, list);
		}
		else{
			if(!list.contains(bpmIdentity)) {				
				list.add(bpmIdentity);
			}
		}
	}
	
	/**
	 * 向某个节点添加用户。
	 * @param key
	 * @param bpmIdentityList 
	 * void
	 */
	public void addBpmIdentity(String key,List< BpmIdentity> bpmIdentityList){
		List<BpmIdentity> list= this.identityMap_.get(key);
		if(BeanUtils.isEmpty(list)){
			list=new ArrayList<BpmIdentity>();
			for (BpmIdentity bpmIdentity : bpmIdentityList) {
				if(!list.contains(bpmIdentity)) {				
					list.add(bpmIdentity);
				}
			}
			this.identityMap_.put(key, list);
		}
		else{
			for (BpmIdentity bpmIdentity : bpmIdentityList) {
				if(!list.contains(bpmIdentity)) {				
					list.add(bpmIdentity);
				}
			}
		}
	}
	
	/**
	 * 设置节点的用户数据。
	 * @param key
	 * @param bpmIdentityList 
	 * void
	 */
	public void setBpmIdentity(String key,List< BpmIdentity> bpmIdentityList){
		List<BpmIdentity> list= this.identityMap_.get(key);
		if(BeanUtils.isEmpty(list)){
			list=new ArrayList<BpmIdentity>();
			for (BpmIdentity bpmIdentity : bpmIdentityList) {
				if(!list.contains(bpmIdentity)) {				
					list.add(bpmIdentity);
				}
			}
			this.identityMap_.put(key, list);
		}
		else{
			list.clear();
			for (BpmIdentity bpmIdentity : bpmIdentityList) {
				if(!list.contains(bpmIdentity)) {				
					list.add(bpmIdentity);
				}
			}
		}
	}

	@Override
	public Map<String, List<BpmIdentity>> getBpmIdentities() {
		
		return identityMap_;
	}

	@Override
	public String getDestination() {
		return this.destinition;
	}

	
	public void setDestination(String dest){
		this.destinition=dest;
	}

	public void setDestinationToken(String destToken){
	}


	@Override
	public String getInstId() {
		return this.instId;
	}
	
	/**
	 * 设置流程实例Id
	 * @param instId_ 
	 */
	public void setInstId(String instId_){
		this.instId=instId_;
	}

	@Override
	public Map<String, Object> getTransitVars() {
		return transitVars_;
	}

	@Override
	public void addTransitVars(String name, Object value) {
		transitVars_.put(name, value);
	}

	@Override
	public void cleanTransitVars() {
		transitVars_.clear();
	}

	@Override
	public void setNotifyType(String notifyType) {
		this.notifyType=notifyType;
	}

	@Override
	public String getNotifyType() {
		return this.notifyType;
	}

	@Override
	public String getBusData() {
		return this.busData;
	}

	@Override
	public void setBusData(String json) {
		this.busData=json;
	}

	@Override
	public Object getTransitVars(String name) {
		return transitVars_.get(name);
	}

	@Override
	public String getDataMode() {
		return this.dataMode;
	}

	@Override
	public void setDataMode(String mode) {
		this.dataMode=mode;
	}

	@Override
	public String getBusinessKey() {
		return this.businessKey;
	}
	
	@Override
	public String getSysCode() {
		return sysCode;
	}

	@Override
	public DataType getPkDataType() {
		return this.pkDataType;
	}

	@Override
	public void setPkDataType(DataType dataType) {
		this.pkDataType=dataType;
		
	}

	@Override
	public void setBusinessKey(String businessKey) {
		this.businessKey=businessKey;
	}

	@Override
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getCurAccount() {
		return curAccount;
	}

	/**
	 * 这个一般用于在web service远程调用时设置当前用户。
	 * @param curAccount 当前用户帐号。
	 * void
	 */
	public void setCurAccount(String curAccount) {
		IUserService userService=AppUtil.getBean(IUserService.class);
		IUser user= userService.getUserByAccount(curAccount);
		ContextUtil.setCurrentUser(user);
		this.curAccount = curAccount;
	}

	@Override
	public void putTransitVars(Map<String, Object> transitVars) {
		if(BeanUtils.isEmpty(transitVars)) return;
		this.transitVars_.putAll(transitVars);
		
	}
	
	@Override
	public List<NodeDefTransient> getGateways() {
		 return this.gatewayNodes;
		
	}
	
	@Override
	public void addGateway(NodeDefTransient gatewayNode) {
		this.gatewayNodes.add(gatewayNode);
		
	}

	@Override
	public void setDataPair(Map<String, String> pair) {
		this.dataPair=pair;
	}

	@Override
	public Map<String, String> getDataPair() {
		return this.dataPair;
	}

	@Override
	public Object getTransitVars(String name, Object defaultValue) {
		if(transitVars_.containsKey(name)){
			return transitVars_.get(name);
		}
		return defaultValue;
	}

	@Override
	public String getActionName() {
		return actionName;
	}

	@Override
	public void setActionName(String actionName) {
		this.actionName=actionName;
	}

	public String getAgentLeaderId() {
		return agentLeaderId;
	}

	public void setAgentLeaderId(String agentLeaderId) {
		this.agentLeaderId = agentLeaderId;
	}
	
	
}
