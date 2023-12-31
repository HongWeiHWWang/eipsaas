package com.hotent.activiti.def;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.springframework.transaction.TransactionSystemException;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.bpm.util.ClassLoadUtil;

public class BpmDefUtil {
	
	/**
	 * 将通过设计器设计的流程定义xml添加监听器设置。
	 * @param id		流程定义ID
	 * @param name		流程定义名称
	 * @param xml		流程定义xml。
	 * @return			转化过的xml。
	 * @throws Exception
	 */
	public static String transBpmDef(String id, String name, String xml) {
		try{
			ClassLoader  loader  =  Thread.currentThread().getContextClassLoader();
			
			InputStream  is=loader.getResourceAsStream("com/hotent/bpmx/activiti/xml/transformDef.xsl");
			
			if(is==null){
				is=BpmDefUtil.class.getResourceAsStream("com/hotent/bpmx/activiti/xml/transformDef.xsl");   
			}
			
			Map<String, String> map =new HashMap<String, String>();
			map.put("id", id);
			map.put("name", name);
			String result= Dom4jUtil.transXmlByXslt(xml, is, map);
			result = result.replace("&lt;", "<").replace("&gt;", ">")
					.replace("xmlns=\"\"", "").replace("&amp;", "&");
			return result;
		}
		catch(Exception ex){
			throw new TransactionSystemException("转换流程定义出错", ex);
		}
	}
	
	/**
	 * 将通过设计器设计的流程定义xml添加监听器设置。
	 * @param id		流程定义ID
	 * @param name		流程定义名称
	 * @param xml		流程定义xml。
	 * @return			转化过的xml。
	 * @throws Exception
	 */
	public static String transFlashBpmDef(String id, String name, String xml) throws Exception{
		return ClassLoadUtil.transform(id, name, xml);
	}
	
	/**
	 * 将节点之后的节点删除然后指向新的节点。 
	 * @param actDefId			流程定义ID
	 * @param nodeId			流程节点ID
	 * @param aryDestination	需要跳转的节点
	 * @return Map<String,Object> 返回节点和需要恢复节点的集合。
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object>  prepare(String actDefId,String nodeId,String[] aryDestination){
		Map<String,Object> map=new HashMap<String, Object>();
		
		RepositoryService repositoryService=AppUtil.getBean(RepositoryService.class);
		
		//修改流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(actDefId);
		
		ActivityImpl curAct= processDefinition.findActivity(nodeId);
		List<PvmTransition> outTrans= curAct.getOutgoingTransitions();
		try{
			List<PvmTransition> cloneOutTrans=(List<PvmTransition>) FileUtil.cloneObject(outTrans);
			map.put("outTrans", cloneOutTrans);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		/**
		 * 解决通过选择自由跳转指向同步节点导致的流程终止的问题。
		 * 在目标节点中删除指向自己的流转。
		 */
		for(Iterator<PvmTransition> it=outTrans.iterator();it.hasNext();){
			PvmTransition transition=it.next();
			PvmActivity activity= transition.getDestination();
			List<PvmTransition> inTrans= activity.getIncomingTransitions();
			for(Iterator<PvmTransition> itIn=inTrans.iterator();itIn.hasNext();){
				PvmTransition inTransition=itIn.next();
				if(inTransition.getSource().getId().equals(curAct.getId())){
					itIn.remove();
				}
			}
		}
		
		
		curAct.getOutgoingTransitions().clear();
		
		if(aryDestination!=null && aryDestination.length>0){
			for(String dest:aryDestination){
				//创建一个连接
				ActivityImpl destAct= processDefinition.findActivity(dest);
				TransitionImpl transitionImpl = curAct.createOutgoingTransition();
				transitionImpl.setDestination(destAct);
			}
		}
		
		map.put("activity", curAct);
		
		
		return map;
		
	}
	
	/**
	 * 将临时节点清除掉，加回原来的节点。
	 * @param map 
	 * void
	 */
	@SuppressWarnings("unchecked")
	public static void restore(Map<String,Object> map){
		ActivityImpl curAct=(ActivityImpl) map.get("activity");
		List<PvmTransition> outTrans=(List<PvmTransition>) map.get("outTrans");
		curAct.getOutgoingTransitions().clear();
		curAct.getOutgoingTransitions().addAll(outTrans);
	}

}
