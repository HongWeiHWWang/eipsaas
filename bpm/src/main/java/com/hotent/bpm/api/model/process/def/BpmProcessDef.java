package com.hotent.bpm.api.model.process.def;

import java.io.Serializable;
import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;


/**
 * <pre>
 * 描述：BPMN流程定义接口
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-13-下午5:39:52
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmProcessDef<T extends BpmProcessDefExt> extends Serializable{
	
	/**
	 * 获取流程定义KEY。
	 * @return String
	 */
	String getDefKey();
	
	/**
	 * 流程名称
	 * @return  String
	 */
	String getName(); 
	/**
	 * 流程定义ID
	 * @return  String
	 */
	String getProcessDefinitionId() ;
	
	
	
	/**
	 * 流程节点定义
	 * @return 
	 * List&lt;BpmNodeDef>
	 */
	List<BpmNodeDef> getBpmnNodeDefs();
	

	
	T getProcessDefExt();
	
	
	/**
	 * 取得上级流程定义。
	 */
	BpmProcessDef<T> getParentProcessDef();		
	
	
	/**
	 * 获取发起事件。
	 * @return  BpmNodeDef
	 */
	BpmNodeDef getStartEvent();
	
	/**
	  * 获取第一个节点。 
	  * 第一个节点的定义是指，开始事件后的第一个节点。
	  * @return  List&lt;BpmNodeDef>
	  */
	 List<BpmNodeDef> getStartNodes();
	 
	 /**
	  * 获取流程的结束节点。
	  * @param processDefId
	  * @return  List&lt;BpmNodeDef>
	  */
	 List<BpmNodeDef> getEndEvents();
}
