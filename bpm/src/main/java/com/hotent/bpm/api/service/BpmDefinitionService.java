package com.hotent.bpm.api.service;

import java.io.IOException;
import java.util.List;

import com.hotent.base.groovy.IScript;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
/**
 * 
 * 描述：流程定义服务接口
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-14-下午5:41:22
 * 版权：广州宏天软件有限公司版权所有
 */
public interface BpmDefinitionService extends IScript {
	/**
	 * 通过ID取得流程定义
	 * @param defId
	 * @return
	 */
	BpmDefinition getBpmDefinitionByDefId(String defId);
	
	/**
	 * 根据流程定义Key获取流程主板本。
	 * @param defKey
	 * @return BpmDefinition
	 */
	BpmDefinition getBpmDefinitionByDefKey(String defKey,boolean needData);
	/**
	 * 发布并生成新版本的流程定义
	 * <pre>
	 * 1.新发布流程。
	 * 	如果key在数据中已经存在，那么
	 * </pre>
	 * @param bpmDefinition
	 * @return
	 * @throws Exception 
	 */
	boolean deploy(BpmDefinition bpmDefinition) throws Exception;
	/**
	 * 保存流程设计的草稿
	 * @param bpmDefinition
	 * @return
	 * @throws Exception 
	 */
	boolean saveDraft(BpmDefinition bpmDefinition) throws Exception;
	
	/**
	 * 取得流程定义中的某个节点的定义
	 * @param bpmnDefId
	 * @param nodeId
	 * @return
	 * @throws Exception 
	 */
	BpmNodeDef getBpmNodeDef(String bpmnDefId,String nodeId) throws Exception;
	/**
	 * 取得流程定义中的某个节点定义
	 * @param procDefId X5中的流程定义ID
	 * @param nodeId
	 * @return 
	 * BpmNodeDef
	 * @throws Exception 
	 */
	BpmNodeDef getBpmNodeDefByDefIdNodeId(String procDefId,String nodeId) throws Exception;
	
	/**
	 * 取得某个流程定义的开始节点的定义
	 * @param defId
	 * @return
	 * @throws Exception 
	 */
	BpmNodeDef getStartBpmNodeDef(String defId) throws Exception;
	
	/**
	 * 取得流程定义的结束节点
	 * @param defId
	 * @return
	 * @throws Exception 
	 */
	List<BpmNodeDef> getEndNode(String defId) throws Exception;
	
	/**
	 * 取得所有的流程定义节点列表
	 * @param defId
	 * @return
	 * @throws Exception 
	 */
	List<BpmNodeDef> getAllBpmNodeDefs(String defId) throws Exception;
	
	
	/**
	 * 根据流程定义ID获取流程定义数据。
	 * @param bpmnDefId
	 * @return 
	 * BpmProcessDef&lt;BpmProcessDefExt>
	 * @throws Exception 
	 */
	BpmProcessDef<BpmProcessDefExt> getBpmProcessDef(String bpmnDefId) throws Exception;
	
	/**
	 * 根据流程定义ID判断是否存在外部子流程
	 * @param defId
	 * @return
	 * @throws Exception 
	 */
	boolean hasExternalSubprocess(String defId) throws Exception;
	
	/**
	 * 通过流程定义ID删除流程定义
	 * @param defId
	 * @return
	 * @throws Exception 
	 */
	boolean removeBpmDefinition(String defId) throws Exception;
	
	
	
	/**
	 * 禁用流程定义
	 * @param defId
	 * @return
	 */
	boolean disabledBpmDefinition(String defId);
	
	
	/**
	 * 禁止流程定义并禁止流程实例。
	 * @param defId
	 * @return  boolean
	 */
	boolean disabledBpmDefinitionInst(String defId);
	
	/**
	 * 启用流程定义
	 * @param defId
	 * @return
	 */
	boolean enabledBpmDefinition(String defId);
	/**
	 * 更新流程定义，不进行版本升级
	 * @param bpmDefinition
	 * @return
	 * @throws Exception 
	 */
	boolean updateBpmDefinition(BpmDefinition bpmDefinition) throws Exception;
	/**
	 * 更新流程定义的所属分类
	 * @param defId
	 * @param typeId 分类ID
	 * @return
	 */
	boolean updateTreeType(String defId,String typeId);
	
	/**
	 * 通过流程定义取得该流程的所有版本（包括当前版本）
	 * @param defId
	 * @return
	 */
	List<BpmDefinition> getAllVersions(String defId);
	
	/**
	 * 取得流程定义的所有历史版本
	 * @param defId
	 * @return
	 */
	List<BpmDefinition> getAllHistoryVersions(String defId);
	
	/**
	 * 按条件取得符合条件的流程定义列表
	 * @param queryFilter
	 * @return
	 */
	List<BpmDefinition> getAll(QueryFilter queryFilter);
	
	/**
	 * 取得用户授权的流程定义
	 * @param userId
	 * @return
	 */
	List<BpmDefinition> getProcessDefinitionByUserId(String userId);
	
	/**
	 * 取得用户授权的流程定义,并按条件过滤
	 * @param userId
	 * @param queryFilter
	 * @return
	 */
	List<BpmDefinition> getProcessDefinitionByUserId(String userId,QueryFilter queryFilter);
	
	/**
	 * 返回流程定义中的设计生成的文件，并且以字符串格式返回
	 * @param defId
	 * @return
	 */
	String getDesignFile(String defId);
	/**
	 * 返回流程定义中的BPMN格式，并且以字符串格式返回
	 * @param defId
	 * @return
	 */
	String getBpmnFile(String defId);
	
	/**
	 * 取得分类下的流程定义列表,并且可进行其他字段的过滤查询
	 * @param filter
	 * 	
	 * @return
	 * @throws IOException 
	 */
	List<BpmDefinition> queryList(QueryFilter filter) throws IOException;
	
	/**
	 * 检查流程定义的defKey是否存在
	 * @param defKey
	 * @return
	 */
	boolean isDefCodeExist(String defKey);

	
	/**
	 * 根据BpmnDefId获取流程定义。
	 * @param bpmnDefId
	 * @return 
	 * BpmDefinition
	 */
	String getDefIdByBpmnDefId(String bpmnDefId);
	
	/**
	 * 按流程定义ID取得流程定义发布后的XML
	 * @param bpmnDefId
	 * @return
	 */
	String getBpmnXmlByBpmnDefId(String bpmnDefId);
	/**
	 * 按流程发布ID取得流程定义发布后的XML
	 * @param bpmnDefId
	 * @return
	 */
	String getBpmnXmlByDeployId(String deployId);
	
	
	
	/**
	 * 取到流程定义中的变量定义列表。
	 * @param defId
	 * @return 
	 * List&lt;BpmVariableDef>
	 * @throws Exception 
	 */
	List<BpmVariableDef> getVariableDefs(String defId) throws Exception;
	
	/**
	 * 取到流程定义中的某个节点上的变量定义列表
	 * @param defId
	 * @param nodeId
	 * @return  List&lt;BpmVariableDef>
	 * @throws Exception 
	 */
	List<BpmVariableDef> getVariableDefs(String defId,String nodeId) throws Exception;
	
	
	/**
	 * 切换到主板本。
	 * @param defId 
	 * void
	 */
	void switchMainVersion(String defId);
	
	/**
	 * 清除测试状态的测试数据
	 * @param defId
	 * @throws Exception 
	 */
	void cleanData(String defId) throws Exception;
	
	/**
	 * 根据流程定义ID获取流程定义对象。
	 * @param bpmnDefId
	 * @return BpmDefinition
	 */
	BpmDefinition getByBpmnDefId(String bpmnDefId);

}
