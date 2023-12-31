package com.hotent.runtime.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldSort;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CustomSignNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTaskTurn;
import com.hotent.bpm.api.model.process.task.SkipResult;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmReadRecordManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskTurnManager;
import com.hotent.bpm.persistence.model.BpmReadRecord;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.runtime.manager.BpmTransReceiverManager;
import com.hotent.runtime.model.BpmTransReceiver;
import com.hotent.runtime.params.CustomSignRevokeParam;

/**
 * 处理撤回的上下文类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年3月11日
 */
@Service
public class RevokeHandler {
    @Resource
    BpmCheckOpinionManager bpmCheckOpinionManager;
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Resource
    BpmTaskManager bpmTaskManager;
    @Resource
    BpmReadRecordManager bpmReadRecordManager;
    @Resource
    BpmExeStackManager bpmExeStackManager;
    @Resource
    BpmTaskTurnManager bpmTaskTurnManager;
    @Resource
    BpmTransReceiverManager bpmTransReceiverManager;
    
    /**
     * 判断每条已办是否可以进行撤回
     * @param pageList
     * @throws Exception 
     */
    public void checkRevoke(PageList<Map<String, Object>> pageList) throws Exception {
        if (BeanUtils.isEmpty(pageList) || pageList.getRows().size() == 0) {
            return;
        }
        List<Map<String, Object>> oldList = pageList.getRows();
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        QueryFilter<DefaultBpmCheckOpinion> queryFilter = QueryFilter.<DefaultBpmCheckOpinion>build();
        queryFilter.addFilter("PROC_INST_ID_", oldList.get(0).get("id").toString(), QueryOP.EQUAL);
        queryFilter.withSorter(new FieldSort("CREATE_TIME_", Direction.DESC));
        PageList<DefaultBpmCheckOpinion> opinionList = bpmCheckOpinionManager.query(queryFilter);
        //记录当前待审批的前置任务id
        Set<String> awaitingParentTaskIds = new HashSet<>();
        //记录已办的前置任务id
        Set<String> doneParentTaskIds = new HashSet<>();
        //记录已阅的任务id
        Set<String> readTaskIds = new HashSet<>();
        for (DefaultBpmCheckOpinion opinion : opinionList.getRows()) {
			if (OpinionStatus.AWAITING_CHECK.getKey().equals(opinion.getStatus()) && !doneParentTaskIds.contains(opinion.getParentTaskId())) {
				awaitingParentTaskIds.add(opinion.getParentTaskId());
				if (opinion.getIsRead() == 1) {
					readTaskIds.add(opinion.getParentTaskId());
				}
			}else if (!OpinionStatus.AWAITING_CHECK.getKey().equals(opinion.getStatus())) {
				doneParentTaskIds.add(opinion.getParentTaskId());
			}
		}
        // 获取流程定义中“允许已阅撤回”配置项的值
        String defId = oldList.get(0).get("procDefId").toString();
        DefaultBpmDefinition def = bpmDefinitionManager.getById(defId);
        BpmDefinitionAccessor bpmDefinitionAccessor = AppUtil.getBean(BpmDefinitionAccessor.class);
        List<BpmNodeDef> startNodes = bpmDefinitionAccessor.getStartNodes(defId);
        String startNodeId = startNodes.get(0).getNodeId();
        String isReadRevoke = def.getIsReadRevoke();
        for (Map<String, Object> map : oldList) {
            boolean exist = false;
            // 在非串签、非并签类型时,相同taskKey的数据已经判断过的无需再次判断
            // (如果是串签、并签类型时,因为同一个taskKey会产生多个任务，所以无法直接根据taskKey判断已办是否可以撤回)
            if (!map.containsKey("signType") || BeanUtils.isEmpty(map.get("signType"))) {
                for (Map<String, Object> map2 : newList) {
                    if (map2.containsKey("taskKey") && map.containsKey("taskKey")) {
                        if (BeanUtils.isNotEmpty(map2.get("taskKey")) && map2.get("taskKey").toString().equals(map.get("taskKey"))) {
                            exist = true;
                            break;
                        }
                    }
                }
            }
            newList.add(map);
            // 没有opinionStatus时跳过该已办
            if (exist || !map.containsKey("opinionStatus") || BeanUtils.isEmpty(map.get("opinionStatus"))) {
                continue;
            }
            String opinionStatus = (String) map.get("opinionStatus");
            
            //自动跳过的第一个用户任务，可以撤回
            if (startNodeId.equals(map.get("taskKey")) &&  OpinionStatus.SKIP.getKey().equals(opinionStatus)) {
            	map.put("revoke", true);
            	continue;
			}
            
            // 发起节点、跳过(非启动流程跳过)、已撤回、待审批、传阅、传阅回复，征询，流转 下的已办不能撤回
            if (( OpinionStatus.SKIP.getKey().equals(opinionStatus) && !SkipResult.SKIP_FIRST.equals(map.get("skipType")) )
            		||  OpinionStatus.START.getKey().equals(opinionStatus)
                    || OpinionStatus.REVOKER.getKey().equals(opinionStatus)
                    || OpinionStatus.AWAITING_CHECK.getKey().equals(opinionStatus)
                    || OpinionStatus.COPYTO.getKey().equals(opinionStatus)
                    || OpinionStatus.COPYTO_REPLY.getKey().equals(opinionStatus)
                    || OpinionStatus.INQU.getKey().equals(opinionStatus)
                    || OpinionStatus.TRANS_FORMING.getKey().equals(opinionStatus)) {
                continue;
            }

            
            String signType = MapUtil.getString(map, "signType");
            // 如果是串签、并签、并审类型及其前置任务类型，通过对应的撤回处理器进行判断
            if (StringUtil.isNotEmpty(signType) && !CustomSignNodeDef.AFTER_SIGN.equals(signType)) {
                RevokeService revokeService = RevokeServiceFactory.getRevokeService(signType);
                if(revokeService != null) {
                	revokeService.canRevoke(map, opinionList.getRows(), isReadRevoke);
                }
            }
            // 否则使用常规判断逻辑
            else {
            	String status = null;
                if (map.containsKey("status")) {
                    status = MapUtil.getString(map, "status");
                }

	            OpinionStatus opinionStatusEnum = OpinionStatus.fromKey(opinionStatus);
	            // 根据已办中的审批记录状态来判断
	            switch (opinionStatusEnum) {
	                // 征询、转办允许撤回
	                case INQU:
	                case DELIVERTO:
	                	map.put("revoke", isInquDeliverToApproval(map, isReadRevoke, opinionStatusEnum));
	                    break;
	                case AGREE:
	                case OPPOSE:
	                case START:
	                case DELIVERTO_AGREE:
	                case DELIVERTO_OPPOSE:
	                case TRANS_AGREE:
	                case SKIP:  
	                    map.put("revoke", isRevokeNormalApproval(map, status,awaitingParentTaskIds, isReadRevoke ,readTaskIds) );
	                    break;
	                case REJECT:
	                case BACK_TO_START:
	                    if (ProcessInstanceStatus.STATUS_BACK.getKey().equals(status)
	                            || ProcessInstanceStatus.STATUS_BACK_TOSTART.getKey().equals(status)) {
	                        // 驳回发起人 允许撤回
	                        map.put("revoke", isRevokeBackApproval(map, opinionList.getRows(), isReadRevoke));
	                    }
	                    break;
	                default:
	                    break;
	            }
            }
        }
        pageList.setRows(newList);
    }

    // 驳回发起人允许撤回的判断
    private boolean isRevokeBackApproval(Map<String, Object> map, List<DefaultBpmCheckOpinion> list,
            String isReadRevoke) {
        String instId = map.get("id").toString();
        int isRead = 0;
        List<DefaultBpmTask> runningTasks = bpmTaskManager.getByInstId(instId);
        if (BeanUtils.isNotEmpty(runningTasks)) {
    		List<BpmReadRecord> bpmReadRecord = bpmReadRecordManager.getByTaskIds(runningTasks);
            if (BeanUtils.isNotEmpty(bpmReadRecord)) {
                isRead = 1;
            }
        } else {
            return false;
        }

        if (isRead == 1 && "false".equals(isReadRevoke)) {
            return false;
        }
        return true;
    }
    
    // 判断征询和转办任务是否可以撤回
    public boolean isInquDeliverToApproval(Map<String, Object> map, String isReadRevoke, OpinionStatus opinionStatus) {
    	// 如果允许已阅撤回
    	if("true".equals(isReadRevoke)) {
    		return true;
    	}
    	
    	String taskId = MapUtil.getString(map, "taskId");
		if (StringUtil.isEmpty(taskId)) {
			return false;
		}
		// 转办
		if(opinionStatus.equals(OpinionStatus.DELIVERTO)) {
			BpmTaskTurn bpmTaskTurn = bpmTaskTurnManager.getByTaskId(taskId);
			List<BpmReadRecord> bpmReadRecords = bpmReadRecordManager.getByTaskIdandrecord(taskId, null);
			if (BeanUtils.isEmpty(bpmReadRecords)) {
				// 没有阅读记录时，允许撤回
				return true;
            }
		}
		// 征询
		else {
			// 获取征询接收人列表
			List<BpmTransReceiver> transReceiverList = bpmTransReceiverManager.getByTaskId(taskId);
			if(BeanUtils.isNotEmpty(transReceiverList)) {
				int size = transReceiverList.size();
				// 取出所有的征询接收人
				String[] receiverIds = new String[size];
				for(int i = 0; i < size; i++) {
					receiverIds[i] = transReceiverList.get(i).getReceiverId();
				}
				// 查询该任务的所有征询接收人的阅读记录
				List<BpmReadRecord> bpmReadRecords = bpmReadRecordManager.getByTaskIdandrecord(taskId, receiverIds);
				if (BeanUtils.isEmpty(bpmReadRecords)) {
					// 没有阅读记录时，允许撤回
					return true;
	            }
			}
		}
    	return false;
    }

	/**
	 * 
	 * @param map
	 *            当前已办对象
	 * @param instStatus
	 *            实例状态
	 * @param parentTaskIds
	 *            当前待办的前置待办任务id
	 * @param isReadRevoke
	 *            流程配置的是否允许已阅撤回
	 * @param readTaskIds
	 *            已阅的任务id
	 * @return
	 */
	private boolean isRevokeNormalApproval(Map<String, Object> map, String instStatus, Set<String> parentTaskIds,
			String isReadRevoke, Set<String> readTaskIds) {

		if (!ProcessInstanceStatus.STATUS_RUNNING.getKey().equals(instStatus)
				&& !ProcessInstanceStatus.STATUS_REVOKE.getKey().equals(instStatus)) {
			return false;
		}

		String curStatus = (String) map.get("opinionStatus");
		String taskId = map.containsKey("taskId") ? (String) map.get("taskId") : "";
		// 发起节点，允许撤回
		if (OpinionStatus.START.getKey().equals(curStatus)) {
			return true;
			// 该条已办，是当前任务的上一条，并且当前任务没有已阅，或者配置了允许已阅撤回。则当前已办可以撤回
		} else if (parentTaskIds.contains(taskId) && (!readTaskIds.contains(taskId) || ("true".equals(isReadRevoke)))) {
			return true;
		}

		return false;
	}

    @Transactional
	public void doRevoke(CustomSignRevokeParam revokeParam)  throws Exception {
		String signType = revokeParam.getSignType();
		// 如果是串签、并签、并审类型及其前置任务类型，通过对应的撤回处理器进行判断
        if (StringUtil.isNotEmpty(signType) && !CustomSignNodeDef.AFTER_SIGN.equals(signType)) {
            RevokeService revokeService = RevokeServiceFactory.getRevokeService(signType);
            if(revokeService != null) {
            	revokeService.doRevoke(revokeParam);
            }
        }
	}
}
