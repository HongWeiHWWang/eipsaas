package com.hotent.runtime.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.runtime.dao.BpmTransRecordDao;
import com.hotent.runtime.manager.BpmTransReceiverManager;
import com.hotent.runtime.manager.BpmTransRecordManager;
import com.hotent.runtime.model.BpmTransRecord;

/**
 * 
 * <pre> 
 * 描述：移交记录 处理实现类
 * 构建组：x7
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2019-02-18 09:46
 * </pre>
 */
@Service("bpmTransRecordManager")
public class BpmTransRecordManagerImpl extends BaseManagerImpl<BpmTransRecordDao, BpmTransRecord> implements BpmTransRecordManager {
	@Resource
    BpmTaskManager bpmTaskManager;
    @Resource
    BpmTransReceiverManager bpmTransReceiverManager;
    @Resource
    BpmProcessInstanceManager bpmProcessInstanceManager;
    @Resource
	BpmTaskCandidateManager bpmTaskCandidateManager;
    @Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
    BpmTaskNoticeManager bpmTaskNoticeManager;

	/**
	 * 修改任务的所属人和执行人
	 * @param bpmTransRecord
	 */
	private void toTransBpmTask(BpmTransRecord bpmTransRecord) throws IOException {
        ArrayNode insts = (ArrayNode) JsonUtil.toJsonNode(bpmTransRecord.getInsts());
        List<String> instLists=new ArrayList<String>();
        StringBuffer instIds=new StringBuffer();
        //listMapinst用来存储流程实例id和流程定义id插入审批历史移交干预用到的
        for (JsonNode node : insts) {
            ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(node);
            instLists.add(obj.get("instId").asText());
            instIds.append(obj.get("instId").asText()+",");
        }
        bpmTransRecord.setProcinstIds(instIds.toString());
        //添加流程实例组，查询任务时的过滤条件之一
        bpmTransRecord.setInstIds(instLists);

        //节点多个审批人时修改候选人表中的候选人为移交人
        Map<String,Object> canMap = new HashMap<>();
		canMap.put("userId",bpmTransRecord.getTransfer());
		canMap.put("executor",bpmTransRecord.getTransfered());
		canMap.put("instIds",bpmTransRecord.getInstIds());
		bpmTaskCandidateManager.updateExecutor(canMap);

		bpmCheckOpinionManager.updateQualfieds(bpmTransRecord.getInstIds(),bpmTransRecord.getTransfer(),bpmTransRecord.getTransfered(),
				bpmTransRecord.getTransferName(),bpmTransRecord.getTransferedName());

	    Map<String,Object> ownerMap = new HashMap<String, Object>();
		ownerMap.put("userId", bpmTransRecord.getTransfer());
		ownerMap.put("ownerId", bpmTransRecord.getTransfered());
		ownerMap.put("ownerName", bpmTransRecord.getTransferedName());
		ownerMap.put("instIds", bpmTransRecord.getInstIds());
		bpmTaskManager.updateOwner(ownerMap);
		bpmTaskNoticeManager.updateOwner(ownerMap);

		Map<String,Object> assigneeMap = new HashMap<String, Object>();
		assigneeMap.put("userId", bpmTransRecord.getTransfer());
		assigneeMap.put("assigneeId", bpmTransRecord.getTransfered());
		assigneeMap.put("assigneeName", bpmTransRecord.getTransferedName());
		assigneeMap.put("instIds", bpmTransRecord.getInstIds());
		bpmTaskManager.updateAssignee(assigneeMap);
		bpmTaskNoticeManager.updateAssignee(assigneeMap);
		//征询 修改流转接收人信息
		Map<String,Object> receiverMap = new HashMap<String, Object>();
		receiverMap.put("receiver", bpmTransRecord.getTransferedName());
		receiverMap.put("receiverId", bpmTransRecord.getTransfered());
		receiverMap.put("receiverIds", bpmTransRecord.getTransfer());
		receiverMap.put("instIds", bpmTransRecord.getInstIds());
		bpmTransReceiverManager.updateReceiver(receiverMap);

        super.create(bpmTransRecord);
	}


	@Override
	@Transactional
	public CommonResult<String> turnOver(BpmTransRecord bpmTransRecord) {
        try{
            //处理bpmTask,修改所属人和执行人
            toTransBpmTask(bpmTransRecord);
        } catch (Exception e) {
            return new CommonResult<String>(false,"移交失败："+e.getMessage());
        }
        return new CommonResult<String>(true,"移交成功！");
	}
	
}
