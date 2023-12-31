package com.hotent.bpm.persistence.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.DefaultJumpRule;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.defxml.DefXmlUtil;
import com.hotent.bpm.defxml.entity.Process;
import com.hotent.bpm.defxml.entity.RootElement;
import com.hotent.bpm.defxml.entity.ext.ExtDefinitions;
import com.hotent.bpm.persistence.model.BpmDefData;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDef;
import com.hotent.bpm.persistence.util.BpmProcessDefExtParse;
/**
 * 
 * <pre> 
 * 描述：流程定义描述访问器
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-11-下午8:43:14
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 *
 */
@Service
public class DefaultBpmDefinitionAccessor implements BpmDefinitionAccessor {
	@Resource
	private BpmDefinitionManager bpmDefinitionManager;


	@Override
	public List<BpmNodeDef> getNodeDefs(String processDefinitionId) throws Exception {
		List<BpmNodeDef> list = new ArrayList<BpmNodeDef>();
		DefaultBpmProcessDef processDef= getProcessDefByDefId(processDefinitionId);
		if(processDef != null){
			list = processDef.getBpmnNodeDefs();
		}
		return list;
	}

	@Override
	public BpmNodeDef getBpmNodeDef(String processDefinitionId, String nodeId) throws Exception {
		List<BpmNodeDef> list=  getNodeDefs(processDefinitionId);
		List<SubProcessNodeDef> listSub=new ArrayList<SubProcessNodeDef>();
		for(BpmNodeDef nodeDef:list){
			if(nodeDef.getNodeId().equals(nodeId)){
				if(nodeDef instanceof UserTaskNodeDef){//解决nullPoint exception的BUG
					UserTaskNodeDef utn = (UserTaskNodeDef)nodeDef;
					if(utn.getJumpRuleList()==null){
						utn.setJumpRuleList(new ArrayList<DefaultJumpRule>());
					}
				}
				return nodeDef;
			}
			if(nodeDef instanceof SubProcessNodeDef){
				listSub.add((SubProcessNodeDef)nodeDef);
			}
		}
		if(listSub.size()>0)
			return findSubProcessNodeDefByNodeId(listSub,nodeId);
		return null;
	}

	private BpmNodeDef findSubProcessNodeDefByNodeId(List<SubProcessNodeDef> subList,String nodeId){
		for(SubProcessNodeDef nodeDef:subList){
			List<BpmNodeDef> nodeList= nodeDef.getChildBpmProcessDef().getBpmnNodeDefs();
			List<SubProcessNodeDef> nestSub=new ArrayList<SubProcessNodeDef>();
			for(BpmNodeDef tmpDef:nodeList){
				if(tmpDef.getNodeId().equals(nodeId)){
					return tmpDef;
				}
				if(tmpDef instanceof SubProcessNodeDef){
					nestSub.add((SubProcessNodeDef)tmpDef);
				}
			}
			if(nestSub.size()>0) //如果多级，进行递归
				return findSubProcessNodeDefByNodeId(nestSub,nodeId);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BpmProcessDef getBpmProcessDef(String processDefId) throws Exception {
		return getProcessDefByDefId(processDefId);
	}



	@Override
	public BpmNodeDef getStartEvent(String processDefId) throws Exception {
		DefaultBpmProcessDef processDef = getProcessDefByDefId(processDefId);
		if(processDef != null){
			List<BpmNodeDef> list= processDef.getBpmnNodeDefs();
			for(BpmNodeDef nodeDef:list){
				if(nodeDef.getType().equals(NodeType.START))
					return nodeDef;
			}
		}
		return null;
	}

	@Override
	public List<BpmNodeDef> getEndEvents(String processDefId) throws Exception {
		List<BpmNodeDef> nodeList=new ArrayList<BpmNodeDef>();
		DefaultBpmProcessDef processDef=getProcessDefByDefId(processDefId);
		if(processDef != null){
			List<BpmNodeDef> list= processDef.getBpmnNodeDefs();
			for(BpmNodeDef nodeDef:list){
				if(nodeDef.getType().equals(NodeType.END)){
					nodeList.add(nodeDef);
				}
			}
		}
		return nodeList;
	}

	@Override
	@CacheEvict(value=BpmConstants.BPMPROCESSDEF_CACHENAME, key="#processDefId", ignoreException = false)
	public void clean(String processDefId) {}

	@Override
	public List<BpmNodeDef> getStartNodes(String processDefId) throws Exception {
		BpmNodeDef nodeDef=getStartEvent(processDefId);
		return nodeDef.getOutcomeNodes();

	}

	@Override
	public boolean isStartNode(String defId, String nodeId) throws Exception {
		List<BpmNodeDef> nodes= getStartNodes(defId);
		for(BpmNodeDef node:nodes){
			if(node.getNodeId().equals(nodeId)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validNodeDefType(String defId, String nodeId,
			NodeType nodeDefType) throws Exception {
		BpmNodeDef nodeDef= getBpmNodeDef(defId,nodeId);
		return nodeDef.getType().equals(nodeDefType);

	}

	@Override
	public boolean isContainCallActivity(String defId) throws Exception {
		DefaultBpmProcessDef processDef=getProcessDefByDefId(defId);
		if(processDef != null){
			List<BpmNodeDef> list= processDef.getBpmnNodeDefs();
			for(BpmNodeDef nodeDef:list){
				if(nodeDef.getType().equals(NodeType.CALLACTIVITY)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 根据流程定义ID取得流程定义数据。
	 * @param processDefinitionId
	 * @return	DefaultBpmProcessDef
	 * @throws Exception 
	 */
	private DefaultBpmProcessDef getProcessDefByDefId(String processDefinitionId) throws Exception {
		DefaultBpmDefinitionAccessor bean = AppUtil.getBean(getClass());
		// 获取缓存数据时，必须通过Spring容器中获取到的bean来调用（否则缓存方法上的注解不会生效），而且缓存方法只能是protected后public方法
		return bean.getProcessDefByDefIdFromCache(processDefinitionId);
	}
	
//	@Cacheable(value=BpmConstants.BPMPROCESSDEF_CACHENAME, key="#processDefinitionId", ignoreException = false)
	protected DefaultBpmProcessDef getProcessDefByDefIdFromCache(String processDefinitionId) throws Exception{
		DefaultBpmProcessDef bpmProcessDef = null;
		DefaultBpmDefinition bpmDef= bpmDefinitionManager.getById(processDefinitionId);
		if(BeanUtils.isNotEmpty(bpmDef)){
			BpmDefData data= bpmDef.getBpmDefData();
			try {
				bpmProcessDef = getByBpmnXml(processDefinitionId,data.getBpmnXml());
				// 暂时这样， 正常的做法要将testStatus写入到xml中
				bpmProcessDef.getProcessDefExt().getExtProperties().setTestStatus(bpmDef.getTestStatus());
			} catch (Exception e) {
				throw new WorkFlowException(String.format("获取流程定义时候出现异常：%s", ExceptionUtils.getRootCauseMessage(e)));
			}
			return bpmProcessDef;
		}
		else {
			throw new WorkFlowException(String.format("未获取到流程定义，processDefinitionId：%s", processDefinitionId));
		}
	}

	/**
	 * 根据流程的xml获取流程定义访问接口类。
	 * @param processDefinitionId	流程定义ID
	 * @param bpmnXml				流程定义文件。
	 * @return
	 * @throws Exception 
	 */
	public DefaultBpmProcessDef getByBpmnXml(String processDefinitionId,String bpmnXml) throws Exception{

		ExtDefinitions definition = DefXmlUtil.getDefinitionsByXml(bpmnXml);
		if(definition==null)
			return null;
		List<Process> list = getProcess(definition);
		if(list.size()!=1)
			return null;
		Process process=list.get(0);

		DefaultBpmProcessDef bpmProcessDef=new DefaultBpmProcessDef();
		bpmProcessDef.setProcessDefinitionId(processDefinitionId);
		bpmProcessDef.setName(process.getName());
		bpmProcessDef.setDefKey(process.getId());

		BpmProcessDefExtParse processDefExtParse = BpmProcessDefExtParse.getInstance();
		processDefExtParse.handProcessDef(bpmProcessDef,definition,process);
		return bpmProcessDef;
	}

	/**
	 * 根据 ExtDefinitions 获取Process。
	 * @param definitions
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 * List<Process>
	 */
	private  List<Process> getProcess(ExtDefinitions definitions) throws JAXBException, IOException{
		List<Process> processes = new ArrayList<Process>();
		List<JAXBElement<? extends RootElement>> bPMNElements =definitions.getRootElement();
		for(JAXBElement<? extends RootElement> jAXBe:bPMNElements){
			RootElement element =  jAXBe.getValue();
			if(element instanceof Process){
				processes.add((Process)element);
			}
		}
		return processes;
	}	


	@Override
	public List<BpmNodeDef> getNodesByType(String processDefinitionId,NodeType nodeType) throws Exception {
		List<BpmNodeDef> rtnList=new ArrayList<BpmNodeDef>();
		DefaultBpmProcessDef processDef=getProcessDefByDefId(processDefinitionId);
		if(processDef != null){
			List<BpmNodeDef> list= processDef.getBpmnNodeDefs();
			for(BpmNodeDef nodeDef:list){
				if(nodeDef.getType().equals(nodeType)){
					rtnList.add(nodeDef);
				}
			}
		}
		return rtnList;
	}



	@Override
	public List<BpmNodeDef> getAllNodeDef(String processDefinitionId) throws Exception {
		List<BpmNodeDef> bpmNodeDefs=this.getNodeDefs(processDefinitionId);
		List<BpmNodeDef> rtnList=new ArrayList<BpmNodeDef>();
		this.getBpmNodeDefs(bpmNodeDefs,rtnList);
		return rtnList;
	}



	private void getBpmNodeDefs(List<BpmNodeDef> bpmNodeDefs, List<BpmNodeDef> rtnList) {
		for(BpmNodeDef def:bpmNodeDefs){
			rtnList.add(def);
			if(!NodeType.SUBPROCESS.equals(def.getType())) continue;
			SubProcessNodeDef subProcessNodeDef=(SubProcessNodeDef)def;
			BpmProcessDef<? extends BpmProcessDefExt> processDef= subProcessNodeDef.getChildBpmProcessDef();
			if(processDef == null) continue; 
			List<BpmNodeDef> subBpmNodeDefs=processDef.getBpmnNodeDefs();
			this.getBpmNodeDefs(subBpmNodeDefs,rtnList);
		}
	}

	/**
	 * 仅仅获取开始、普通、会签的节点定义
	 * @param processDefinitionId
	 * @return 
	 * @throws Exception 
	 */
	public List<BpmNodeDef> getSignUserNode(String processDefinitionId) throws Exception{
		List<BpmNodeDef> bpmNodeDefs = this.getAllNodeDef(processDefinitionId);
		List<BpmNodeDef> rtnList = new ArrayList<BpmNodeDef>();
		for(BpmNodeDef bnd :bpmNodeDefs){
			if(bnd.getType().equals(NodeType.START) || bnd.getType().equals(NodeType.SIGNTASK) || bnd.getType().equals(NodeType.USERTASK)){
				rtnList.add(bnd);
			}
		}
		return rtnList;
	}
}
