package com.hotent.bpm.persistence.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.task.NodeDefTransient;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.model.def.SubProcessStartOrEndEventModel;
import com.hotent.bpm.persistence.dao.BpmExeStackRelationDao;
import com.hotent.bpm.persistence.manager.ActExecutionManager;
import com.hotent.bpm.persistence.manager.ActTaskManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmExeStackRelationManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.ActExecution;
import com.hotent.bpm.persistence.model.ActTask;
import com.hotent.bpm.persistence.model.BpmExeStack;
import com.hotent.bpm.persistence.model.BpmExeStackRelation;

/*
 * 驳回辅助工具
 */
public class BpmStackRelationUtil {

	/**
	 * 从流程定义中获取指定节点来路网关定义
	 * 
	 * @param defId
	 *            流程定义ID
	 * @param nodeId
	 *            节点ID
	 * @param fromStack
	 *            节点的来路堆栈
	 * @return
	 * @throws Exception
	 */
	public static List<NodeDefTransient> getInComeGateway(String defId, String nodeId, BpmExeStack fromStack)
			throws Exception {
		List<NodeDefTransient> listResult = new ArrayList<NodeDefTransient>();
		if (fromStack == null)
			return null;

		BpmDefinitionAccessor bpmDefinitionAccessor = (BpmDefinitionAccessor) AppUtil.getBean("bpmDefinitionAccessor");

		// 判断当前是否存在子流程开始或结束结点的网关，优先判断子流程多实例网关
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		String currentEventType = cmd.getTransitVars("CurrentEventType") != null
				? cmd.getTransitVars("CurrentEventType").toString() : null;
		String isSubProcessMultiStartOrEndEvent = cmd.getTransitVars("SubProcessMultiStartOrEndEvent") != null
				? cmd.getTransitVars("SubProcessMultiStartOrEndEvent").toString() : null;

		if (isSubProcessMultiStartOrEndEvent != null) {
			SubProcessStartOrEndEventModel model = (SubProcessStartOrEndEventModel) cmd
					.getTransitVars("SubProcessMultiStartOrEndEventModel");

			String nodeType = model.getNoteType();
			BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(fromStack.getPrcoDefId(), model.getNodeId());

			NodeDefTransient nodeDef = new NodeDefTransient(bpmNodeDef);

			nodeDef.setType(NodeType.fromKey(nodeType));
			listResult.add(nodeDef);
			return listResult;
		} else if (currentEventType != null && currentEventType.equals("SubProcessStartOrEndEvent")) {
			SubProcessStartOrEndEventModel model = (SubProcessStartOrEndEventModel) cmd
					.getTransitVars("SubProcessStartOrEndEventModel");

			String nodeType = model.getNoteType();
			BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(fromStack.getPrcoDefId(), model.getNodeId());

			NodeDefTransient nodeDef = new NodeDefTransient(bpmNodeDef);

			nodeDef.setType(NodeType.fromKey(nodeType));
			listResult.add(nodeDef);
			return listResult;
		}

		NodeDefTransient nodeDef = null;
		List<NodeDefTransient> histSearchNodeList = new ArrayList<NodeDefTransient>();
		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<BpmNodeDef> inComeList = bpmNodeDef.getIncomeNodes();

		String parentNodeId = fromStack.getNodeId();
		// 找出实例来路的网关或者内嵌子流程开始节点或子流程结束节点
		for (BpmNodeDef node : inComeList) {
			if (nodeDef != null)
				break;
			NodeType noteType = node.getType();
			if (noteType.equals(NodeType.EXCLUSIVEGATEWAY) || noteType.equals(NodeType.PARALLELGATEWAY)
					|| noteType.equals(NodeType.INCLUSIVEGATEWAY)) {
				histSearchNodeList.add(new NodeDefTransient(node));
				nodeDef = getInComeDateWay(node, parentNodeId, histSearchNodeList);
			}
		}
		return histSearchNodeList;
	}

	// 递归找出由节点任务发送来时中间经过的来路网关
	// histSearchNodeList:当前历史的节点,注意列表中是反向的，索引值为0是接近目标节点的网关，列表索引值最大的接近来向节点
	private static NodeDefTransient getInComeDateWay(BpmNodeDef node, String parentNodeId,
			List<NodeDefTransient> histSearchNodeList) {

		NodeDefTransient resultNode = null;
		List<BpmNodeDef> inList = node.getIncomeNodes();
		for (BpmNodeDef theNode : inList) {
			if (theNode.getNodeId().equals(parentNodeId)) {
				resultNode = new NodeDefTransient(node);
				break;
			}
			NodeType noteType = theNode.getType();
			if (noteType.equals(NodeType.EXCLUSIVEGATEWAY) || noteType.equals(NodeType.PARALLELGATEWAY)
					|| noteType.equals(NodeType.INCLUSIVEGATEWAY)) {
				histSearchNodeList.add(new NodeDefTransient(theNode));
				resultNode = getInComeDateWay(theNode, parentNodeId, histSearchNodeList);
			}
		}
		if (BeanUtils.isNotEmpty(histSearchNodeList) && resultNode == null) {
			// 清除本次遍历的网关
			for (NodeDefTransient item : histSearchNodeList) {
				if (item.getNodeId().equals(node.getNodeId())) {
					histSearchNodeList.remove(item);
					break;

				}
			}
		}
		if (resultNode != null && histSearchNodeList != null && histSearchNodeList.size() > 0) {
			// 取回目标节点最近的那个网关
			resultNode = histSearchNodeList.get(0);
		}
		return resultNode;
	}

	/**
	 * 判断指定实例节点是否有走过前置网关（包括子流程开始节点和结束结点这两个特殊的网关）
	 * 
	 * @param bpmProcInstId
	 * @param ondeId
	 * @param direction
	 *            方向，前pre，后after
	 * @return
	 * @throws Exception
	 */
	public static boolean isHaveAndOrGateway(String bpmProcInstId, String ondeId, String direction) throws Exception {
		BpmExeStackRelationDao relationDao = AppUtil.getBean(BpmExeStackRelationDao.class);
		List<BpmExeStackRelation> list = relationDao.getListByProcInstId(bpmProcInstId);
		return isHaveAndOrGateway(bpmProcInstId, ondeId, direction, list);
	}

	public static boolean isHaveAndOrGateway(String bpmProcInstId, String nodeId, String direction,
			List<BpmExeStackRelation> list) throws Exception {
		BpmProcessInstanceManager instanceManager = AppUtil.getBean(BpmProcessInstanceManager.class);

		BpmExeStackRelation relation = null;
		for (BpmExeStackRelation bpmExeStackRelation : list) {
			String fromNodeId = bpmExeStackRelation.getFromNodeId();
			String toNodeId = bpmExeStackRelation.getToNodeId();
			// 在内部子流程中，开始节点后面的那个节点可能存在两条堆栈关系的记录，其中有一条是 fromNodeId 和 toNodeId
			// 相同的，这里忽略这条记录
			if (fromNodeId.equals(toNodeId))
				continue;
			if ("pre".equals(direction)) {
				// 向前：谁给我的，那么自己就是在To的位置
				if (toNodeId.equals(nodeId)) {
					relation = bpmExeStackRelation;
					break;
				}
			} else if ("after".equals(direction)) {
				// 向后：我给了谁，那么自己就是在From的位置
				if (fromNodeId.equals(nodeId)) {
					relation = bpmExeStackRelation;
					break;
				}
			}
		}
		if (relation == null)
			return false;
		// 向后找
		String rnodeType = relation.getToNodeType();
		String noteType = relation.getFromNodeType();
		if ("after".equals(direction)) {
			rnodeType = relation.getFromNodeType();
			noteType = relation.getToNodeType();
		}
		if (noteType.equals(NodeType.PARALLELGATEWAY.getKey()) || noteType.equals(NodeType.INCLUSIVEGATEWAY.getKey())
				|| noteType.equals(NodeType.SUBSTARTGATEWAY.getKey())
				|| noteType.equals(NodeType.SUBENDGATEWAY.getKey())
				|| (noteType.equals(NodeType.SUBMULTISTARTGATEWAY.getKey())
						&& !rnodeType.equals(NodeType.SIGNTASK.getKey()))) {
			return true;
		}
		boolean hasHistoryNode = false;
		for (BpmExeStackRelation bpmExeStackRelation : list) {
			if (bpmExeStackRelation.getToNodeType().equals(NodeType.PARALLELGATEWAY.getKey())
					|| noteType.equals(NodeType.INCLUSIVEGATEWAY.getKey())
					|| noteType.equals(NodeType.SUBSTARTGATEWAY.getKey())
					|| noteType.equals(NodeType.SUBENDGATEWAY.getKey())
					|| noteType.equals(NodeType.SUBMULTISTARTGATEWAY.getKey())) {
				hasHistoryNode = true;
				break;
			}
		}
		if (!hasHistoryNode) {
			return false;
		}
		BpmProcessInstance instance = instanceManager.get(bpmProcInstId);
		String defId = instance.getProcDefId();
		BpmNodeDef bpmNodeDef = direction.equals("pre") ? getPreParallelBpmNodeDef(list, relation, defId)
				: getAfterParallelBpmNodeDef(list, relation, defId);
		return bpmNodeDef != null;
	}

	/**
	 * 创建堆栈关系
	 * 
	 * @param procInstId
	 *            流程实例
	 * @param fromBpmExeStack
	 *            来向堆栈
	 * @param toBpmExeStack
	 *            到达堆栈
	 * @throws Exception
	 */
	public static void createBpmExeStackRelation(String procInstId, BpmExeStack fromBpmExeStack,
			BpmExeStack toBpmExeStack) {
		if (fromBpmExeStack == null)
			return;

		BpmExeStackRelationManager bpmExeStackRelationManager = AppUtil.getBean(BpmExeStackRelationManager.class);

		// 会签并行第二次撤回时 fromId和toId不能相等
		if (!fromBpmExeStack.getId().equals(toBpmExeStack.getId())) {
			BpmExeStackRelation bpmExeStackRelation = bpmExeStackRelationManager.getById(procInstId,
					fromBpmExeStack.getId(), toBpmExeStack.getId());
			if (BeanUtils.isEmpty(bpmExeStackRelation)) {// 判断是否已存在堆栈数据
				BpmExeStackRelation entity = new BpmExeStackRelation();
				entity.setRelationId(UniqueIdUtil.getSuid());
				entity.setFromStackId(fromBpmExeStack.getId());
				entity.setToStackId(toBpmExeStack.getId());
				entity.setToNodeId(toBpmExeStack.getNodeId());
				entity.setToNodeType(toBpmExeStack.getNodeType());
				entity.setFromNodeId(fromBpmExeStack.getNodeId());
				String fromNodeType = fromBpmExeStack.getNodeType();
				entity.setFromNodeType(fromNodeType);
				entity.setProcInstId(procInstId);
				bpmExeStackRelationManager.create(entity);
			}
		}
	}

	/**
	 * 获取指定节点在堆栈中的前继节点实例中的And OR网关 子流程开始网关，子流程结束网关
	 * 
	 * @param list
	 * @param relation
	 * @param defId
	 * @return
	 * @throws Exception
	 */
	private static BpmNodeDef getPreParallelBpmNodeDef(List<BpmExeStackRelation> list, BpmExeStackRelation relation,
			String defId) throws Exception {
		if (relation == null)
			return null;
		BpmNodeDef bpmNodeDef = null;
		BpmDefinitionAccessor bpmDefinitionAccessor = (BpmDefinitionAccessor) AppUtil.getBean("bpmDefinitionAccessor");
		String noteType = relation.getFromNodeType();

		if (noteType.equals(NodeType.PARALLELGATEWAY.getKey()) || noteType.equals(NodeType.INCLUSIVEGATEWAY.getKey())
				|| noteType.equals(NodeType.SUBSTARTGATEWAY.getKey())
				|| noteType.equals(NodeType.SUBENDGATEWAY.getKey())
				|| noteType.equals(NodeType.SUBMULTISTARTGATEWAY.getKey())) {
			bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, relation.getToNodeId());
			return bpmNodeDef;
		}

		List<BpmExeStackRelation> listNewBpmExeStacks = new ArrayList<BpmExeStackRelation>();
		String fromStackId = relation.getFromStackId();
		// 收集父迁移关系
		for (BpmExeStackRelation bpmExeStackRelation : list) {
			if (!bpmExeStackRelation.getFromNodeId().equals(bpmExeStackRelation.getToNodeId())
					&& bpmExeStackRelation.getToStackId().equals(fromStackId)
					&& !listNewBpmExeStacks.contains(bpmExeStackRelation)) {
				listNewBpmExeStacks.add(bpmExeStackRelation);
			}
		}
		for (BpmExeStackRelation bpmExeStackRelation : listNewBpmExeStacks) {
			bpmNodeDef = getPreParallelBpmNodeDef(list, bpmExeStackRelation, defId);
			if (bpmNodeDef != null)
				break;
		}
		return bpmNodeDef;
	}

	/**
	 * 获取指定节点在堆栈中的后续节点实例中的And OR网关
	 * 
	 * @param list
	 * @param relation
	 * @param defId
	 * @return
	 * @throws Exception
	 */
	private static BpmNodeDef getAfterParallelBpmNodeDef(List<BpmExeStackRelation> list, BpmExeStackRelation relation,
			String defId) throws Exception {
		if (relation == null)
			return null;
		BpmNodeDef bpmNodeDef = null;
		BpmDefinitionAccessor bpmDefinitionAccessor = (BpmDefinitionAccessor) AppUtil.getBean("bpmDefinitionAccessor");
		String noteType = relation.getToNodeType();

		if (noteType.equals(NodeType.PARALLELGATEWAY.getKey()) || noteType.equals(NodeType.INCLUSIVEGATEWAY.getKey())
				|| noteType.equals(NodeType.SUBSTARTGATEWAY.getKey())
				|| noteType.equals(NodeType.SUBENDGATEWAY.getKey())
				|| noteType.equals(NodeType.SUBMULTISTARTGATEWAY.getKey())) {
			bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, relation.getToNodeId());
			return bpmNodeDef;
		}

		List<BpmExeStackRelation> listNewBpmExeStacks = new ArrayList<BpmExeStackRelation>();
		String toStackId = relation.getToStackId();
		// 收集父迁移关系
		for (BpmExeStackRelation bpmExeStackRelation : list) {
			if (bpmExeStackRelation.getFromStackId().equals(toStackId)
					&& !listNewBpmExeStacks.contains(bpmExeStackRelation)) {
				listNewBpmExeStacks.add(bpmExeStackRelation);
			}
		}
		for (BpmExeStackRelation bpmExeStackRelation : listNewBpmExeStacks) {
			bpmNodeDef = getAfterParallelBpmNodeDef(list, bpmExeStackRelation, defId);
			// if (bpmNodeDef != null) break;
			break;

		}
		return bpmNodeDef;
	}

	public static List<BpmNodeDef> getHistoryListBpmNodeDef(String bpmProcInstId, String ondeId, String direction)
			throws Exception {
		List<BpmNodeDef> listResult = new ArrayList<BpmNodeDef>();
		BpmExeStackRelationDao relationDao = AppUtil.getBean(BpmExeStackRelationDao.class);
		BpmProcessInstanceManager instanceManager = AppUtil.getBean(BpmProcessInstanceManager.class);
		List<BpmExeStackRelation> list = relationDao.getListByProcInstId(bpmProcInstId);
		BpmExeStackRelation relation = null;
		for (BpmExeStackRelation bpmExeStackRelation : list) {
			if ("pre".equals(direction) && bpmExeStackRelation.getToNodeId().equals(ondeId)
					&& !bpmExeStackRelation.getFromNodeId().equals(bpmExeStackRelation.getToNodeId())) {
				relation = bpmExeStackRelation;
				break;
			}
			if ("after".equals(direction) && bpmExeStackRelation.getFromNodeId().equals(ondeId)
					&& !bpmExeStackRelation.getFromNodeId().equals(bpmExeStackRelation.getToNodeId())) {
				relation = bpmExeStackRelation;
				break;
			}
		}
		if (relation == null)
			return listResult;
		BpmProcessInstance instance = instanceManager.get(bpmProcInstId);
		String defId = instance.getProcDefId();

		BpmDefinitionAccessor bpmDefinitionAccessor = (BpmDefinitionAccessor) AppUtil.getBean("bpmDefinitionAccessor");
		List<BpmNodeDef> allNodeDef = bpmDefinitionAccessor.getAllNodeDef(defId);
		Map<String, BpmNodeDef> nodeMap = new HashMap<>();
		for (BpmNodeDef bpmNodeDef : allNodeDef) {
			nodeMap.put(bpmNodeDef.getNodeId(), bpmNodeDef);
		}
		Map<String, BpmNodeDef> resultMap = new HashMap<>();
		List<BpmExeStackRelation> resultRelation = new ArrayList<>();
		getHistoryListBpmNodeDef(list, relation, direction, nodeMap, resultMap, resultRelation);
		if (BeanUtils.isEmpty(resultRelation)) {
			return listResult;
		}
		Collections.sort(resultRelation, new Comparator<BpmExeStackRelation>() {
			@Override
			public int compare(BpmExeStackRelation opinion1, BpmExeStackRelation opinion2) {
				return opinion2.getCreatedTime().compareTo(opinion1.getCreatedTime());
			}
		});
		for (BpmExeStackRelation bpmExeStackRelation : resultRelation) {
			String nodeId = direction.equals("pre") ? bpmExeStackRelation.getFromNodeId()
					: bpmExeStackRelation.getToNodeId();
			if (resultMap.containsKey(nodeId)) {
				listResult.add(resultMap.get(nodeId));
			}
		}
		return listResult;
	}

	/**
	 * 收集指定节点的历史轨迹
	 * 
	 * @param list
	 *            实例对应的堆栈关系列表
	 * @param relation
	 *            关系对象
	 * @param direction
	 *            方向pre前继，after后续
	 * @param nodeMap
	 *            流程定义所有节点的map
	 * @param resultMap
	 *            结果集map
	 * @param resultRelation
	 *            已收集的Relation集合
	 * @throws Exception
	 */
	private static void getHistoryListBpmNodeDef(List<BpmExeStackRelation> list, BpmExeStackRelation relation,
			String direction, Map<String, BpmNodeDef> nodeMap, Map<String, BpmNodeDef> resultMap,
			List<BpmExeStackRelation> resultRelation) throws Exception {

		// 如果来源节点ID和去往节点ID相同，则直接返回，避免该递归方法无法跳出
		if (relation == null || relation.getToNodeId().equals(relation.getFromNodeId())) {
			return;
		}

		String nodeId = direction.equals("pre") ? relation.getFromNodeId() : relation.getToNodeId();
		//改节点已经在结果集里面了。就不再遍历收集，以免多实例内部子流程死循环，
        if (resultMap.containsKey(nodeId)) {
			return;
		}
		BpmNodeDef bpmNodeDef = nodeMap.get(nodeId);
		// 如果是用户任务或者外部子流程，则将其放入驳回节点集合中
		if (bpmNodeDef != null && (bpmNodeDef instanceof UserTaskNodeDef || bpmNodeDef instanceof SignNodeDef
				|| bpmNodeDef instanceof CallActivityNodeDef) ) {
			resultMap.put(bpmNodeDef.getNodeId(), bpmNodeDef);
			resultRelation.add(relation);
		}

		String fromStackId = relation.getFromStackId();
		String toStackId = relation.getToStackId();
		for (BpmExeStackRelation bpmExeStackRelation : list) {

			if (bpmExeStackRelation.getFromStackId().equals(bpmExeStackRelation.getToStackId())) {
				continue;
			}
			// 如果往前找，谁到当前节点来的
			if (direction.equals("pre") && bpmExeStackRelation.getToStackId().equals(fromStackId)) {
				getHistoryListBpmNodeDef(list, bpmExeStackRelation, direction, nodeMap, resultMap, resultRelation);
				// 如果往后找，当前节点到那里去了
			} else if (direction.equals("after") && bpmExeStackRelation.getFromStackId().equals(toStackId)) {
				getHistoryListBpmNodeDef(list, bpmExeStackRelation, direction, nodeMap, resultMap, resultRelation);
			}
		}
	}

	/**
	 * 判断两个节点之间是否有多实例子流程网关
	 * 
	 * @param bpmProcInstId
	 * @param startNodeId
	 * @param endNodeId
	 * @return
	 * @throws Exception
	 */
	public static boolean isHaveMultiGatewayByBetweenNode(String bpmProcInstId, String startNodeId, String endNodeId)
			throws Exception {
		// subMultiStartGateway
		List<BpmNodeDef> list = getHistoryListByBetweenNode(bpmProcInstId, startNodeId, endNodeId);
		for (BpmNodeDef bpmNodeDef : list) {
			if (SubProcessNodeDef.class.getName().equals(bpmNodeDef.getClass().getName())) {
				SubProcessNodeDef subNodeDef = (SubProcessNodeDef) bpmNodeDef;
				if (subNodeDef.isParallel())
					return true;
			}
			String noteType = bpmNodeDef.getType().getKey();
			if (noteType.equals(NodeType.SUBMULTISTARTGATEWAY.getKey())
					|| noteType.equals(NodeType.SUBENDGATEWAY.getKey())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回两个节点之间的历史轨迹 startNodeId:为剑头起始的节点，endNodeId为剑头指向的结点
	 * 
	 * @param startNodeId
	 * @param endNodeId
	 * @return
	 * @throws Exception
	 */
	public static List<BpmNodeDef> getHistoryListByBetweenNode(String bpmProcInstId, String startNodeId,
			String endNodeId) throws Exception {
		List<BpmNodeDef> list = new ArrayList<BpmNodeDef>();
		// 取交集startNodeId:为剑头开始的节点，endNodeId为剑头结束的结点
		List<BpmNodeDef> listStartNodeIdPre = getHistoryListBpmNodeDef(bpmProcInstId, startNodeId, "after");
		List<BpmNodeDef> listEndNodeIdafter = getHistoryListBpmNodeDef(bpmProcInstId, endNodeId, "pre");
		for (BpmNodeDef startNode : listStartNodeIdPre) {
			for (BpmNodeDef endNode : listEndNodeIdafter) {
				if (startNode.getNodeId().equals(endNode.getNodeId())) {
					list.add(startNode);
				}
			}
		}
		return list;
	}

	/**
	 * 直接返回执行的单实例同步网关驳回，对act_ru_execution进行调整
	 * 
	 * @param rejectSingleExecutionId
	 * @return
	 */
	public static boolean instancesRejectDirectAdjust(String rejectDirectExecutionId) {
		ActExecutionManager actExecutionManager = AppUtil.getBean(ActExecutionManager.class);
		ActExecution currentExecution = actExecutionManager.get(rejectDirectExecutionId);
		// 任务完成数据
		TaskFinishCmd cmd = (TaskFinishCmd) ContextThreadUtil.getActionCmd();
		if (currentExecution == null)
			return true;
		Object rejectDirectParentId = cmd.getTransitVars("rejectDirectParentId");
		// 如果同步网关中直来直往驳回时 ParentId有变化
		if (rejectDirectParentId != null && !rejectDirectParentId.toString().equals(currentExecution.getParentId())) {
			if (currentExecution.getParentId().equals(currentExecution.getProcInstId())) {
				List<String> parentsId = actExecutionManager.getByParentsId(currentExecution.getParentId());
				// 当出现多条记录的父ID为流程实例ID，修复流程轨迹
				if (BeanUtils.isNotEmpty(parentsId) && parentsId.size() > 1) {
					for (String parentId : parentsId) {
						if (!parentId.equals(currentExecution.getId())) {
							currentExecution.setParentId(parentId);
							actExecutionManager.update(currentExecution);
							break;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 直接返回执行的单实例同步网关退回，对act_ru_execution进行调整
	 * 
	 * @param rejectSingleExecutionId
	 * @return
	 */
	public static boolean parallelGatewayRejectDirectAdjust(String parentExecutionId) {
		ActExecutionManager actExecutionManager = AppUtil.getBean(ActExecutionManager.class);
		ActExecution parentExecution = actExecutionManager.get(parentExecutionId);
		ActTaskManager actTaskManager = AppUtil.getBean(ActTaskManager.class);
		if (parentExecution == null)
			return true;

		String actProcInstanceId = parentExecution.getProcInstId();
		// 任务完成数据
		TaskFinishCmd cmd = (TaskFinishCmd) ContextThreadUtil.getActionCmd();
		// 退回时的目标节点
		String rejectTargetNodeId = cmd.getDestination();
		String actionName = cmd.getActionName();
		// 如果是退回
		if (StringUtil.isNotEmpty(rejectTargetNodeId)
				&& !("reject".equals(actionName) || "backToStart".equals(actionName))) {
			// 取上级的执行线程
			List<String> parentsId = actExecutionManager.getByParentsId(parentExecution.getId());
			if (BeanUtils.isNotEmpty(parentsId)) {
				ActExecution currentExecution = actExecutionManager.get(parentsId.get(0));
				// 如果上一步执行存在且为并行且上一步执行的父ID不等于流程实例ID：则需修复
				if (parentExecution.getIsConcurrent().toString().equals("1")
						&& !parentExecutionId.equals(parentExecution.getProcInstId())) {
					parentExecution.setIsActive((short) 1);
					parentExecution.setActId(currentExecution.getActId());
					// 更新父执行记录为当前记录
					actExecutionManager.update(parentExecution);

					List<ActTask> actTasks = actTaskManager.getByInstId(actProcInstanceId);
					if (BeanUtils.isNotEmpty(actTasks)) {
						for (ActTask actTask : actTasks) {
							if (actTask.getTaskDefKey() != null
									&& actTask.getTaskDefKey().equals(parentExecution.getActId())) {
								actTask.setExecutionId(parentExecutionId);
								actTaskManager.update(actTask);
							}
						}
					}
					// 删除驳回错误产生的执行记录
					actExecutionManager.remove(currentExecution.getId());
				}
			}
		}

		return true;
	}

	/**
	 * 按流程图执行的单实例退回，对act_ru_execution及任务进行调整
	 * 
	 * @param rejectSingleExecutionId
	 * @return
	 */
	public static boolean singleInstancesRejectAdjust(String rejectSingleExecutionId) {
		ActExecutionManager actExecutionManager = AppUtil.getBean(ActExecutionManager.class);
		BpmExeStackManager bpmExeStackManager = AppUtil.getBean(BpmExeStackManager.class);
		ActTaskManager actTaskManager = AppUtil.getBean(ActTaskManager.class);

		// 1.修改act的任务表；2.修改act_ru_execution主实例线程
		ActExecution currentExecution = actExecutionManager.get(rejectSingleExecutionId);
		if (currentExecution == null)
			return true;
		String actProcInstanceId = currentExecution.getProcInstId();

		bpmExeStackManager.multipleInstancesRejectAdjustOnActTask(rejectSingleExecutionId);
		bpmExeStackManager.multipleInstancesRejectAdjustOnActExecution(actProcInstanceId);

		if (actExecutionManager.get(currentExecution.getId()) == null) {
			currentExecution.setParentId(actProcInstanceId);
			currentExecution.setIsScope((short) 1);
			currentExecution.setIsConcurrent((short) 0);
			actExecutionManager.create(currentExecution);

			List<ActTask> actTasks = actTaskManager.getByInstId(actProcInstanceId);
			if (BeanUtils.isNotEmpty(actTasks)) {
				for (ActTask actTask : actTasks) {
					actTask.setExecutionId(currentExecution.getId());
					actTaskManager.update(actTask);
				}
			}
		}

		return true;
	}

	/**
	 * 按流程图执行的多实例退回，对act_ru_execution及任务进行调整
	 * 
	 * @param rejectAfterExecutionId
	 * @return
	 */
	public static boolean multipleInstancesRejectAdjust(String rejectAfterExecutionId) {
		ActExecutionManager actExecutionManager = AppUtil.getBean(ActExecutionManager.class);
		BpmExeStackManager bpmExeStackManager = AppUtil.getBean(BpmExeStackManager.class);
		// 1.修改BPM的任务表；2.修改act的任务表；3.修改act_ru_execution主实例线程
		ActExecution currentExecution = actExecutionManager.get(rejectAfterExecutionId);
		if (currentExecution == null)
			return true;
		String nodeId = currentExecution.getActId();
		String actProcInstanceId = currentExecution.getProcInstId();

		bpmExeStackManager.multipleInstancesRejectAdjustOnBpmTask(rejectAfterExecutionId);
		bpmExeStackManager.multipleInstancesRejectAdjustOnActTask(rejectAfterExecutionId);
		bpmExeStackManager.multipleInstancesRejectAdjustOnActExecution(actProcInstanceId);

		currentExecution = actExecutionManager.get(actProcInstanceId);
		currentExecution.setActId(nodeId);
		actExecutionManager.update(currentExecution);
		return true;
	}

	/**
	 * 获取指定节点的后续节点（如果节点后面是网关自动获取网关下一级的节点）
	 * 
	 * @Title: getAfterListNode
	 * @Description: TODO
	 * @param defId
	 * @param nodeId
	 * @return
	 * @return: List<BpmNodeDef>
	 * @throws Exception
	 */
	public static List<BpmNodeDef> getAfterListNode(String defId, String nodeId) throws Exception {
		List<BpmNodeDef> listResult = new ArrayList<BpmNodeDef>();
		return getAfterListNode(defId, nodeId, listResult);
	}

	/**
	 * 获取指定节点的后续节点（如果节点后面是网关自动获取网关下一级的节点）
	 * 
	 * @Title: getAfterListNode
	 * @Description: TODO
	 * @param defId
	 * @param nodeId
	 * @param listResult
	 * @return
	 * @return: List<BpmNodeDef>
	 * @throws Exception
	 */
	private static List<BpmNodeDef> getAfterListNode(String defId, String nodeId, List<BpmNodeDef> listResult)
			throws Exception {
		BpmDefinitionAccessor bpmDefinitionAccessor = (BpmDefinitionAccessor) AppUtil.getBean("bpmDefinitionAccessor");
		BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<BpmNodeDef> listOut = nodeDef.getOutcomeNodes();
		for (BpmNodeDef outNode : listOut) {
			if (outNode.getType().getKey().equals(NodeType.USERTASK.getKey())) {
				listResult.add(outNode);
			} else {
				getAfterListNode(defId, outNode.getNodeId(), listResult);
			}
		}
		return listResult;
	}

}
