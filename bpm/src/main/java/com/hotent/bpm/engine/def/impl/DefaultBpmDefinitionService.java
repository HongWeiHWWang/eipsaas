package com.hotent.bpm.engine.def.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.persistence.dao.BpmProcessInstanceDao;
import com.hotent.bpm.persistence.manager.*;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.exception.ProcessDefException;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.engine.def.DefXmlTransForm;
import com.hotent.bpm.natapi.def.NatProDefinitionService;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.query.BpmDefFieldSorts;
import com.hotent.bpm.persistence.model.query.BpmDefQueryFields;
@Service
public class DefaultBpmDefinitionService implements BpmDefinitionService {
	// 注入Actviti原生的流程定义服务
	@Resource(name = "proDefinitionServiceImpl")
	NatProDefinitionService natProDefinitionService;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	DefXmlTransForm defXmlTransForm;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmBusLinkManager bpmBusLinkManager;
//	@Resource
//	BoDataHandler boDataHandler; 
	@Resource
	JdbcTemplate jdbcTemplate;
	@Resource
	BpmInstFormManager bpmInstFromManager;
    @Resource
    BpmTaskNoticeManager bpmTaskNoticeManager;
    @Resource
    BpmTaskNoticeDoneManager bpmTaskNoticeDoneManager;
    @Resource
    BpmTaskTurnManager bpmTaskTurnManager;
    @Resource
    BpmProcessInstanceDao bpmProcessInstanceDao;
    @Resource
    BpmTaskManager bpmTaskManager;
	@Override
	public boolean deploy(BpmDefinition bpmDefinition) throws Exception {
		return bpmDefinitionManager.deploy(bpmDefinition);
	}

	@Override
	public boolean saveDraft(BpmDefinition bpmDefinition) throws Exception {
		return bpmDefinitionManager.saveDraft(bpmDefinition);
	}

	@Override
	public BpmNodeDef getBpmNodeDef(String bpmnDefId, String nodeId) throws Exception {
		String defId = bpmDefinitionManager.getDefIdByBpmnDefId(bpmnDefId);
		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		return bpmNodeDef;
	}

	/**
	 * 取得流程定义中的某个节点定义
	 * 
	 * @param procDefId
	 *            X5中的流程定义ID
	 * @param nodeId
	 * @return BpmNodeDef
	 * @throws Exception 
	 */
	public BpmNodeDef getBpmNodeDefByDefIdNodeId(String procDefId, String nodeId) throws Exception {
		return bpmDefinitionAccessor.getBpmNodeDef(procDefId, nodeId);
	}

	@Override
	public BpmNodeDef getStartBpmNodeDef(String defId) throws Exception {
		List<BpmNodeDef> bpmNodeDefs = getAllBpmNodeDefs(defId);
		return bpmNodeDefs.size() > 0 ? bpmNodeDefs.get(0) : null;
	}

	@Override
	public List<BpmNodeDef> getAllBpmNodeDefs(String defId) throws Exception {
		return bpmDefinitionAccessor.getNodeDefs(defId);
	}

	@Override
	public List<BpmNodeDef> getEndNode(String defId) throws Exception {
		List<BpmNodeDef> endNodeDefs = new ArrayList<BpmNodeDef>();
		List<BpmNodeDef> bpmNodeDefs = getAllBpmNodeDefs(defId);
		for (BpmNodeDef bpmNodeDef : bpmNodeDefs) {
			if (bpmNodeDef.getOutcomeNodes().size() == 0) {
				endNodeDefs.add(bpmNodeDef);
			}
		}
		return endNodeDefs;
	}

	@Override
	public boolean removeBpmDefinition(String defId) throws Exception {
		bpmDefinitionManager.removeCascade(defId);
		return true;
	}

	@Override
	public boolean disabledBpmDefinition(String defId) {
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		defaultBpmDefinition.setStatus(BpmDefinition.STATUS.FORBIDDEN);
		bpmDefinitionManager.update(defaultBpmDefinition);

		return true;
	}

	@Override
	public boolean enabledBpmDefinition(String defId) {
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		defaultBpmDefinition.setStatus(BpmDefinition.STATUS.DEPLOY);
		bpmDefinitionManager.update(defaultBpmDefinition);
		// 恢复流程实例。
		bpmProcessInstanceManager.updForbiddenByDefKey(defaultBpmDefinition.getDefKey(), BpmProcessInstance.FORBIDDEN_NO);
		return true;
	}

	@Override
	public boolean updateBpmDefinition(BpmDefinition bpmDefinition) throws Exception {
		return bpmDefinitionManager.updateBpmDefinition(bpmDefinition);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BpmDefinition> getAllVersions(String defId) {
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		List<DefaultBpmDefinition> defaultBpmDefinitions = bpmDefinitionManager.queryByDefKey(defaultBpmDefinition.getDefKey());
		return (List)defaultBpmDefinitions;
	}

	@Override
	public List<BpmDefinition> getAllHistoryVersions(String defId) {
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		List<DefaultBpmDefinition> defaultBpmDefinitions = bpmDefinitionManager.queryHistorys(defaultBpmDefinition.getDefKey());
		return convertBpmDefinitions(defaultBpmDefinitions);
	}

	@Override
	public List<BpmDefinition> getAll(QueryFilter queryFilter) {
		List<DefaultBpmDefinition> defaultBpmDefinitions = bpmDefinitionManager.query(queryFilter).getRows();
		return convertBpmDefinitions(defaultBpmDefinitions);
	}

	@Override
	public boolean hasExternalSubprocess(String defId) throws Exception {
		List<BpmNodeDef> list = bpmDefinitionAccessor.getNodeDefs(defId);
		for (BpmNodeDef nodeDef : list) {
			if (NodeType.CALLACTIVITY.equals(nodeDef.getType())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDesignFile(String defId) {
		if (StringUtils.isEmpty(defId))
			return "";

		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		if (defaultBpmDefinition != null) {
			return defaultBpmDefinition.getDefXml();
		}
		return "";
	}

	@Override
	public String getBpmnFile(String defId) {
		if (StringUtils.isEmpty(defId)) {
			return "";
		}
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		if (defaultBpmDefinition != null) {
			return defaultBpmDefinition.getDefXml();
		}
		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmDefinition> queryList(QueryFilter query) throws IOException {
		List<DefaultBpmDefinition> defaultBpmDefinitions = bpmDefinitionManager.queryList(query).getRows();
		return (List)defaultBpmDefinitions;
	}


	@Override
	public boolean isDefCodeExist(String defCode) {
		List<DefaultBpmDefinition> defaultBpmDefinitions = bpmDefinitionManager.queryByDefKey(defCode);
		if (defaultBpmDefinitions.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateTreeType(String defId, String typeId) {
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		if (defaultBpmDefinition != null) {
			defaultBpmDefinition.setTypeId(typeId);
			bpmDefinitionManager.update(defaultBpmDefinition);
			return true;
		}
		return false;
	}

	@Override
	public List<BpmDefinition> getProcessDefinitionByUserId(String userId) {
		BpmDefQueryFields bpmDefQueryFields = new BpmDefQueryFields();
		bpmDefQueryFields.addCreateBy(userId);
		BpmDefFieldSorts bpmDefFieldSorts = new BpmDefFieldSorts();
		bpmDefFieldSorts.addDefId();
		List<DefaultBpmDefinition> defaultBpmDefinitions = bpmDefinitionManager.query(bpmDefQueryFields, FieldRelation.AND, bpmDefFieldSorts);
		return convertBpmDefinitions(defaultBpmDefinitions);
	}

	@Override
	public List<BpmDefinition> getProcessDefinitionByUserId(String userId, QueryFilter queryFilter) {
		if (StringUtils.isEmpty(userId) || queryFilter == null) {
			return new ArrayList<BpmDefinition>();
		}
		queryFilter.addFilter("CREATE_BY_", userId, QueryOP.EQUAL, FieldRelation.AND);
		PageList<DefaultBpmDefinition> pageList =  bpmDefinitionManager.query(queryFilter);
		return convertBpmDefinitions(pageList.getRows());
	}

	@Override
	public BpmDefinition getBpmDefinitionByDefId(String defId) {
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		return (BpmDefinition) defaultBpmDefinition;
	}

	@Override
	public BpmDefinition getBpmDefinitionByDefKey(String defKey, boolean needData) {
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getMainByDefKey(defKey, needData);
		return (BpmDefinition) defaultBpmDefinition;
	}

	private List<BpmDefinition> convertBpmDefinitions(List<DefaultBpmDefinition> defaultBpmDefinitions) {
		List<BpmDefinition> bpmDefinitions = new ArrayList<BpmDefinition>();
		for (DefaultBpmDefinition _defaultBpmDefinition : defaultBpmDefinitions) {
			bpmDefinitions.add((BpmDefinition) _defaultBpmDefinition);
		}
		return bpmDefinitions;
	}

	@Override
	public String getDefIdByBpmnDefId(String bpmnDefId) {
		return bpmDefinitionManager.getDefIdByBpmnDefId(bpmnDefId);
	}

	@Override
	public BpmProcessDef<BpmProcessDefExt> getBpmProcessDef(String bpmnDefId) throws Exception {

		String defId = bpmDefinitionManager.getDefIdByBpmnDefId(bpmnDefId);
		BpmProcessDef<BpmProcessDefExt> processDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		return processDef;

	}

	@Override
	public String getBpmnXmlByBpmnDefId(String bpmnDefId) {
		DefaultBpmDefinition bpmDefinition = bpmDefinitionManager.getByBpmnDefId(bpmnDefId);
		return natProDefinitionService.getDefXmlByDeployId(bpmDefinition.getBpmnDeployId());
	}

	@Override
	public String getBpmnXmlByDeployId(String deployId) {
		DefaultBpmDefinition bpmDefinition = bpmDefinitionManager.getByBpmnDeployId(deployId);
		return natProDefinitionService.getDefXmlByDeployId(bpmDefinition.getBpmnDeployId());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmVariableDef> getVariableDefs(String defId) throws Exception {
		BpmProcessDef<DefaultBpmProcessDefExt> bpmProcessDef = (BpmProcessDef) bpmDefinitionAccessor.getBpmProcessDef(defId);
		return bpmProcessDef.getProcessDefExt().getVariableList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmVariableDef> getVariableDefs(String defId, String nodeId) throws Exception {
		BpmProcessDef<DefaultBpmProcessDefExt> bpmProcessDef = (BpmProcessDef) bpmDefinitionAccessor.getBpmProcessDef(defId);
		return bpmProcessDef.getProcessDefExt().getVariableList(nodeId);
	}

	@Override
	public void switchMainVersion(String defId) {
		bpmDefinitionManager.updMainVersion(defId);

	}

	@Override
	public boolean disabledBpmDefinitionInst(String defId) {
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		defaultBpmDefinition.setStatus(BpmDefinition.STATUS.FORBIDDEN_INSTANCE);
		bpmDefinitionManager.update(defaultBpmDefinition);
		// 更新流程实例为禁止。
		String defKey = defaultBpmDefinition.getDefKey();
		bpmProcessInstanceManager.updForbiddenByDefKey(defKey, BpmProcessInstance.FORBIDDEN_YES);

		return false;
	}

	@Override
    public void cleanData(String defId) throws Exception {
        DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
        if (!BpmDefinition.TEST_STATUS.TEST.equals(defaultBpmDefinition.getTestStatus())) {
            throw new ProcessDefException("非测试状态的流程不能清除数据");
        }
        //todo 子流程删除
        //根据流程定义Key获取流程实例列表 (测试数据)
        List<DefaultBpmProcessInstance> instances = bpmProcessInstanceDao.getTestListByBpmnDefKey(defaultBpmDefinition.getDefKey());
        if(BeanUtils.isNotEmpty(instances)){
            for (DefaultBpmProcessInstance entity : instances){
                //根据父实例ID获取子流程实例列表
                List<DefaultBpmProcessInstance> instancesSub = bpmProcessInstanceDao.getByParentId(entity.getId());
                if(BeanUtils.isNotEmpty(instancesSub)){
                    List<String> listStr = new ArrayList<>();

                    for (DefaultBpmProcessInstance instance : instancesSub){
                        String instId=instance.getId();
                        //清除子流程实例
                        bpmProcessInstanceManager.remove(instId);
                        //根据子流程实例ID 删除当前流程所使用的表单关系
                        bpmInstFromManager.removeDataByInstId(instId);
                        //根据子流程实例ID 删除知会待办任务
                        bpmTaskNoticeManager.delBpmTaskNoticeByInstId(instId);
                        //根据子流程实例ID 删除知会已办任务
                        bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneByInstId(instId);
                        //根据子流程实例ID 删除我转办的任务
                        listStr.add(instId);
                    }
                    if (BeanUtils.isNotEmpty(listStr)) {
                        bpmTaskTurnManager.delByInstList(listStr);//根据流程实例列表删除任务
                        bpmTaskManager.delByInstList(listStr);
                    }
                }
            }
        }
        // todo 主流程删除
        //根据流程KEY查询所有流程定义ID
        List<DefaultBpmDefinition> listDefaultBpmDefinition = bpmDefinitionManager.queryByDefKey(defaultBpmDefinition.getDefKey());

        for(DefaultBpmDefinition entity : listDefaultBpmDefinition) {
            //根据流程定义Key获取流程实例列表 (测试数据)
            List<DefaultBpmProcessInstance> list = bpmProcessInstanceDao.getTestListByBpmnDefKey(entity.getDefKey());
            List<String> listStr = new ArrayList<>();
            if (BeanUtils.isNotEmpty(list)) {
                for (DefaultBpmProcessInstance defaultBpmProcessInstance : list) {
                    String instId = defaultBpmProcessInstance.getId();
                    listStr.add(instId);
                    //根据流程实例ID 删除知会已办任务
                    bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneByInstId(instId);
                    //根据流程实例ID 删除知会待办任务
                    bpmTaskNoticeManager.delBpmTaskNoticeByInstId(instId);
                    //根据流程实例ID 删除当前流程所使用的表单关系
                    bpmInstFromManager.removeDataByInstId(instId);
                    //根据流程定义ID 删除相关的关联表数据和对应的数据
                    bpmBusLinkManager.removeDataByInstId(instId);
                }
            }
            //根据流程实例ID 删除我转办的任务
            if (BeanUtils.isNotEmpty(listStr)) {
                bpmTaskTurnManager.delByInstList(listStr);//根据流程实例列表删除任务
                bpmTaskManager.delByInstList(listStr);
            }
            //根据流程KEY清除流程实例
            bpmProcessInstanceManager.removeTestInstByDefKey(entity.getDefKey(),true);
        }
    }

	@Override
	public BpmDefinition getByBpmnDefId(String bpmnDefId) {
		return bpmDefinitionManager.getByBpmnDefId(bpmnDefId);
	}
}
