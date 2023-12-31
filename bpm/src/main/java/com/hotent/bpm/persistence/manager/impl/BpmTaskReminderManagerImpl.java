package com.hotent.bpm.persistence.manager.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.jms.Notice;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.template.impl.FreeMarkerEngine;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.BpmIdentityService;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.dao.BpmTaskReminderDao;
import com.hotent.bpm.persistence.manager.BpmReminderHistoryManager;
import com.hotent.bpm.persistence.manager.BpmSecretaryManageManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskReminderManager;
import com.hotent.bpm.persistence.model.BpmReminderHistory;
import com.hotent.bpm.persistence.model.BpmTaskReminder;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.util.BpmUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：任务催办 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:miaojf
 * 邮箱:miaojf@jee-soft.cn
 * 日期:2016-07-28 16:52:36
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmTaskReminderManager")
public class BpmTaskReminderManagerImpl extends BaseManagerImpl<BpmTaskReminderDao, BpmTaskReminder> implements BpmTaskReminderManager{

	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmIdentityService bpmIdentityService;
	@Resource
	BpmIdentityExtractService bpmIdentityExtractService;
	@Resource
	BpmReminderHistoryManager bpmReminderHistoryManager;
	@Resource
	NatTaskService natTaskService;
    @Resource
    BpmInstService bpmInstService;
    @Resource
    BoDataService boDataService;
    
	@Override
    @Transactional
	public void deleteByTaskId(String taskId) {
		baseMapper.deleteByTaskId(taskId);		
	}
	
	@Override
	public List<BpmTaskReminder> getTriggerReminders() {
		String currentTime = TimeUtil.getCurrentTime();
		String timeSql = " trigger_date_ <='"+currentTime+"'";
		String dbType = SQLUtil.getDbType();
		if("oracle".equals(dbType)) {
			timeSql = " trigger_date_ <= to_date('"+currentTime+"','yyyy-mm-dd hh24:mi:ss') ";
		}
		return baseMapper.getTriggerReminders(timeSql);
	}
	
	@Override
    @Transactional
	public void executeTaskReminderJob() throws Exception {
		//获取当前时间要出发的所有催办项
		List<BpmTaskReminder> reminderList = this.getTriggerReminders();
		
		
		for (BpmTaskReminder reminder : reminderList) {
			DefaultBpmTask task = bpmTaskManager.get(reminder.getTaskId());
			
			if(BeanUtils.isEmpty(task)){
				this.remove(reminder.getId());
				continue;
			}
			
			task.setIdentityList(bpmIdentityService.searchByNode(task.getProcInstId(),task.getNodeId()));
			
			// 如果执行到期	动作任务被处理。则继续
			boolean taskComplate = executeDueAction(reminder,task);
			
			//发送催办
			if(reminder.getIsSendMsg()==1) sendMsg(reminder,task);
			
			if(taskComplate) continue;
			
			//处理预警
			handleWarning(reminder,task);
			
			// 如果预警为无动作，无催办，无预警。则删除改催办
			if(BpmTaskReminder.TASK_DUE_ACTION_NO_ACTION.equals(reminder.getDueAction())
					&& (reminder.getIsSendMsg()==0 || reminder.getMsgCount()<=0)
					&& StringUtil.isEmpty(reminder.getWarningset())){
				this.remove(reminder.getId());
				return;
			}
			// 更新催办
			this.update(reminder);
		}
		
		
	}
	
	// 处理预警
    @Transactional
    private void handleWarning(BpmTaskReminder reminder, DefaultBpmTask task) throws IOException {
        if(StringUtil.isEmpty(reminder.getWarningset())) return;
        ArrayNode newWarningSet = JsonUtil.getMapper().createArrayNode();
        ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(reminder.getWarningset());
        for (int i = 0; i < array.size(); i++) {
            ObjectNode warn =(ObjectNode) array.get(i);
            LocalDateTime warningDate = TimeUtil.convertString(warn.get("warnDate").asText());
            //处理预警
            if(warningDate.isBefore(LocalDateTime.now())){
                task.setPriority(warn.get("level").asLong());
                bpmTaskManager.updateTaskPriority(task.getId(),warn.get("level").asLong());
                createReminderHistory(reminder, "Warning", warn.get("warnName").asText(),task);
            }else{
                //如果未来要处理的待办早于 催办下次要触发的时间。则修改triggerDate
                if(warningDate.isBefore(reminder.getTriggerDate())){
                    reminder.setTriggerDate(warningDate);
                }
                //将未处理的预警设置到下次之中
                newWarningSet.add(warn);
            }
        }

        if(newWarningSet.size()>0){
            reminder.setWarningset(JsonUtil.toJson(newWarningSet));
        }else{
            reminder.setWarningset("");
        }
    }


    /**
     * 如果无动作则返回 true
     * 如果执行脚本。则修改为无动作
     * 如果结束流程，完成任务则删除当前催办
     * @return false
     */
    @Transactional
    private boolean executeDueAction(BpmTaskReminder reminder, DefaultBpmTask task) {
        if(reminder.getDueDate().isAfter(LocalDateTime.now())) {//任务还未到期
            reminder.setTriggerDate(reminder.getDueDate()); //尝试设置触发时间
            return false;
        }
        //无动作 //任务到期
        if(BpmTaskReminder.TASK_DUE_ACTION_NO_ACTION.equals(reminder.getDueAction())){
            return false;
        }
        String msg = "";
        //自动下一任务
        BpmTaskActionService bpmTaskActionService = AppUtil.getBean(BpmTaskActionService.class);
       if(BpmTaskReminder.TASK_DUE_ACTION_AUTO_NEXT.equals(reminder.getDueAction())){
            DefaultTaskFinishCmd taskFinishCmd = new DefaultTaskFinishCmd();
            taskFinishCmd.setTaskId(reminder.getTaskId());
            taskFinishCmd.setActionName("agree");
            taskFinishCmd.setApprovalOpinion("催办到期自动完成");
            try {
                bpmTaskActionService.finishTask(taskFinishCmd);
                msg = "已经自动完成当前任务！";
                this.deleteByTaskId(reminder.getTaskId());//完成当前任务后，删除与该任务关联的催办
            } catch (Exception e) {
                msg = "自动完成当前任务失败！："+e.getMessage();
                e.printStackTrace();
            }
        }
        //结束掉流程
        else if(BpmTaskReminder.TASK_DUE_ACTION_END_PROCESS.equals(reminder.getDueAction())){
            try {
                bpmTaskActionService.endProcessByTaskId(reminder.getTaskId(), reminder.getMsgType(), "催办任务到期，自动结束流程！","");
                //直接走结束方法似乎不会走插件了。所以。这里把催办全清掉。
                this.deleteByTaskId(reminder.getTaskId());
                msg = "已经结束当前流程！";
            } catch (Exception e) {
                msg = "自动结束当前流程失败！："+e.getMessage();
                e.printStackTrace();
            }
        }
        //执行脚本
        else if(BpmTaskReminder.TASK_DUE_ACTION_CALL_METHOD.equals(reminder.getDueAction())&&StringUtil.isNotEmpty(reminder.getDueScript())){
            Map<String, Object> vars = new HashMap<String, Object>();
            Map<String, Object> variables = vars= natTaskService.getVariables(reminder.getTaskId());
            vars.putAll(variables);
            vars.put("task",task);
            GroovyScriptEngine groovyScriptEngine = AppUtil.getBean(GroovyScriptEngine.class);

            try {
                groovyScriptEngine.execute(reminder.getDueScript(), vars);
            } catch (Exception e) {
                createReminderHistory(reminder, reminder.getDueAction(), "执行脚本"+reminder.getDueScript()+"\n失败！"+e.getMessage(),task);
            }

            // 执行过一次脚本以后就无动作。直到自动被删除
            reminder.setDueAction(BpmTaskReminder.TASK_DUE_ACTION_NO_ACTION);
            createReminderHistory(reminder, reminder.getDueAction(), "执行脚本成功："+reminder.getDueScript(),task);
            return false;
        }
       //自动完成任务类型则返回true，不再继续处理催办
        createReminderHistory(reminder, reminder.getDueAction(), msg,task);
        return true;
    }

    /**
     * 发送消息的处理
     * 发消息开始时间 +时间间隔=下次发送消息开始时间，更新发消息开始时间
     * 发消息次数减 1
     * 如果消息发送完毕。则count为0；
     * @throws Exception
     */
    @Transactional
    private void sendMsg(BpmTaskReminder reminder, DefaultBpmTask task) throws Exception {
        LocalDateTime  beginSend = reminder.getMsgSendDate();
        int count = reminder.getMsgCount();
        //如果还没有到发送时间,或者已经催办完毕
        if(beginSend.isAfter(LocalDateTime.now()) || count<=0) return;

        int interval = reminder.getMsgInterval();
        //每次 次数减1 ，开始时间加上一间隔。
        reminder.setMsgSendDate(TimeUtil.getLocalDateTimeByMills(TimeUtil.getNextTime(TimeUtil.MINUTE,interval,TimeUtil.getTimeMillis(beginSend))));
        reminder.setMsgCount(count-1);

        // 如果消息触发时间早于其他则设置为下次触发时间
        if(reminder.getMsgSendDate().isBefore(reminder.getTriggerDate())){
            reminder.setTriggerDate(reminder.getMsgSendDate());
        }

        FreeMarkerEngine FreeMarkerEngine=AppUtil.getBean(FreeMarkerEngine.class);
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("title", task.getSubject());
        vars.put("time", reminder.getRelDate());
        vars.put("task", task);
        vars.put("startorName", ContextUtil.getCurrentUser().getFullname());
        vars.put("startDate", DateUtil.getCurrentTime("yyyy-MM-dd"));
        //流程变量
        vars.put("flowKey_", task.getProcDefKey());
        vars.put("instanceId_", task.getProcInstId());
        vars.put("startUser", ContextUtil.getCurrentUser().getFullname());
        //表单变量
        BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
        List<ObjectNode> boDatas = boDataService.getDataByInst(bpmProcessInstance);
        if(BeanUtils.isNotEmpty(boDatas)){
            for(ObjectNode objectNode :boDatas){
                String boName=objectNode.get("boDefAlias").asText();
                Map<String, Object> dataMap = new  HashMap<>();
                try {
                    dataMap = JsonUtil.toMap(JsonUtil.toJson(objectNode.get("data")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    vars.put(boName +"." + entry.getKey(),  entry.getValue());
                }
            }
        }
        String html,text;
        try {
             html= BpmUtil.getTitleByRule(reminder.getHtmlMsg(), vars);
             text=FreeMarkerEngine.parseByTemplate(reminder.getPlainMsg(), vars);
        }catch(Exception e) {
            createReminderHistory(reminder, "sendMsg", "发送消息失败！"+e.getMessage(), task);
            return;
        }
        String userNames = "";
        //获取用户
        List<IUser> recievers = new ArrayList<IUser>();

        //将用户抽取出来。
        recievers= bpmIdentityExtractService.extractUser(bpmIdentityService.searchByNode(task.getProcInstId(),task.getNodeId()));

        Set<String> leaderIdSet = new HashSet<>();

        if(StringUtil.isNotZeroEmpty(task.getOwnerId())){
            userNames = task.getOwnerName();
            leaderIdSet.add(task.getOwnerId());
        }else{
            for (BpmIdentity identity : task.getIdentityList()) {
                userNames +=identity.getName()+",";
                leaderIdSet.add(identity.getId());
            }
        }
        Set<String> secreIdSet =new HashSet<>();
        if ("1".equals(reminder.getSendPerson())) {
            BpmSecretaryManageManager bManage = AppUtil.getBean(BpmSecretaryManageManager.class);
            Map<String, Set<String>> secretarys = bManage.getSecretaryByleaderIds(leaderIdSet, task.getProcDefKey());
            for (Iterator<Entry<String, Set<String>>> iterator = secretarys.entrySet().iterator(); iterator.hasNext();) {
                Entry<String, Set<String>> next = iterator.next();
                if (StringUtil.isEmpty(next.getKey())) {
                    iterator.remove();
                }
                for (Iterator<String> iterator2 = next.getValue().iterator(); iterator2.hasNext();) {
                    String secreId = iterator2.next();
                    //如果秘书同时也是候选人
                    if (leaderIdSet.contains(secreId) || StringUtil.isEmpty(secreId)) {
                        iterator2.remove();
                    }else{
                        secreIdSet.add(secreId);
                    }
                }
            }
            Map<String, ObjectNode>  userMap = new  HashMap<>();
            if (!secreIdSet.isEmpty()) {
                UCFeignService ucFeignService =AppUtil.getBean(UCFeignService.class);
                Set<String> userIdSet = new HashSet<>();
                userIdSet.addAll(leaderIdSet);
                userIdSet.addAll(secreIdSet);
                ArrayNode user = ucFeignService.getUserByIdsOrAccounts(StringUtil.join(userIdSet));
                for (JsonNode jsonNode : user) {
                    ObjectNode userNode = (ObjectNode) jsonNode;
                    userMap.put(userNode.get("id").asText(), userNode);
                }
                Set<String> sendSecretaryName =new HashSet<>();
                for (Iterator<Entry<String, Set<String>>> iterator = secretarys.entrySet().iterator(); iterator.hasNext();) {
                    Entry<String, Set<String>> next = iterator.next();
                    String subject = "【"+userMap.get(next.getKey()).get("fullname").asText()+"】共享任务催办";
                    Set<String> sendSecretaryId =new HashSet<>();
                    for (Iterator<String> iterator2 = next.getValue().iterator(); iterator2.hasNext();) {
                        String secreId = iterator2.next();
                        sendSecretaryId.add(secreId);
                        sendSecretaryName.add(userMap.get(secreId).get("fullname").asText());
                    }
                    sendMsg(subject,reminder,text,html,StringUtil.join(sendSecretaryId).split(","));
                }
                userNames+=StringUtil.join(sendSecretaryName);
            }
        }
        String[] recieverAccounts = MessageUtil.parseAccountOfUser(recievers);
        sendMsg("任务催办",reminder,text,html,recieverAccounts);
        createReminderHistory(reminder, "sendMsg", "向["+userNames+"]发送催办消息成功！", task);
    }

    //发送催办消息
    @Transactional
    private void sendMsg(String subject,BpmTaskReminder reminder,  String text, String html,String[] recieverAccounts) {
        System.err.println(subject+"-----"+StringUtil.join(recieverAccounts));
        NoticeMessageType[] messageTypes = MessageUtil.parseNotifyType(reminder.getMsgType());
        PortalFeignService PortalFeignService = AppUtil.getBean(PortalFeignService.class);
        for(NoticeMessageType type : messageTypes){
            if(type.isPlain()) {
                Notice notice = new Notice();
                notice.setSubject(subject);
                notice.setReceivers(recieverAccounts);
                notice.setContent(text);
                notice.setMessageTypes(new NoticeMessageType[]{type});
                PortalFeignService.sendNoticeToQueue(notice);
            }
            else {
                Notice notice = new Notice();
                notice.setSubject(subject);
                notice.setReceivers(recieverAccounts);
                notice.setContent(html);
                notice.setMessageTypes(new NoticeMessageType[]{type});
                PortalFeignService.sendNoticeToQueue(notice);
            }
        }
    }

    //创建催办执行历史
    @Transactional
    private void createReminderHistory(BpmTaskReminder reminder, String type, String msg, DefaultBpmTask task) {
        BpmReminderHistory history = new BpmReminderHistory();
        history.setExecuteDate(LocalDateTime.now());
        history.setNodeId(task.getNodeId());
        history.setNodeName(task.getName());
        history.setInstId(task.getProcInstId());
        history.setIsntName(task.getSubject());
        history.setRemindType(type);
        history.setNote(msg);
        history.setUserId(task.getOwnerId());
        history.setId(UniqueIdUtil.getSuid());
        bpmReminderHistoryManager.create(history);
    }

}
