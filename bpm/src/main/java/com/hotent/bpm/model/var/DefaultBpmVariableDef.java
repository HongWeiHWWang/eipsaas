package com.hotent.bpm.model.var;

import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;

/**
 * 
 * <pre>
 * 描述：流程定义中的变量定义
 * 构建组：x5-bpmx-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014年5月16日-下午5:14:57
 * 版权：广州宏天软件有限公司版权所有
 * 网址：http://www.jee-soft.cn
 * </pre>
 */
public class DefaultBpmVariableDef implements BpmVariableDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 278555366495215660L;
	private String nodeId="";
	//变量名
	private String name="";
	//变量Key
	private String varKey="";
	//数据类型
	private String dataType="";
	//缺省值
	private Object defaultVal="";
	//是否必需
	private boolean isRequired=false;
	//变量描述
	private String description="";

	public DefaultBpmVariableDef() {
		
	}
	
	
	/**
	 * 将传入数据进行数据转型。
	 * @param dataType	数据类型：double,string,date,int
	 * @param value		需要转换的值。
	 * @return
	 */
	public static Object getValue(String dataType,String value){
		
		if(BpmVariableDef.VAR_TYPE_DOUBLE.equals(dataType)){
			if(value==null || StringUtil.isEmpty(value)){
				return  new Double(0);
			}
			return new Double(value);
		}else if(BpmVariableDef.VAR_TYPE_FLOAT.equals(dataType)){
			if(value==null || StringUtil.isEmpty(value)){
				return  new Float(0);
			}
			return new Float(value);
		}else if(BpmVariableDef.VAR_TYPE_INT.equals(dataType)){
			if(value==null || StringUtil.isEmpty(value)){
				return 0;
			}
			return new Integer(value);
		}else if(BpmVariableDef.VAR_TYPE_DATE.equals(dataType)){
			return DateFormatUtil.parse(value, new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"});
		}
		
		return value;
	}
	
	public DefaultBpmVariableDef(String name,String varKey,String dataType,
			String defaultVal,boolean isRequired,String description){
		this.name=name;
		this.varKey=varKey;
		this.dataType=dataType;
		this.defaultVal=getValue(dataType,defaultVal);
		this.isRequired=isRequired;
		this.description=description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVarKey() {
		return varKey;
	}

	public void setVarKey(String varKey) {
		this.varKey = varKey;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Object getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(Object defaultVal) {
		this.defaultVal = defaultVal;
	}
	
	public void setDefaultVal(String defaulVal2){
		this.defaultVal=getValue(dataType,defaulVal2);
	}
	
	public boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public String getDescription() {
		return description==null?"" :description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getNodeId() {
		return this.nodeId;
	}

	@Override
	public void setNodeId(String nodeId) {
		this.nodeId=nodeId;
	}
}
