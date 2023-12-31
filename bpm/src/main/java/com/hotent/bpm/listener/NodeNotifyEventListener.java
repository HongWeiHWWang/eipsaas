package com.hotent.bpm.listener;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.jms.JmsActor;
import com.hotent.base.template.impl.FreeMarkerEngine;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.NodeNotifyEvent;
import com.hotent.bpm.api.event.NodeNotifyModel;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.model.form.DefaultForm;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.bpm.util.PortalDataUtil;

/**
 * @author qiuxd
 * @company 广州宏天软件股份有限公司
 * @email qiuxd@jee-soft.cn
 * @date
 */
@Service
public class NodeNotifyEventListener implements ApplicationListener<NodeNotifyEvent>, Ordered {
    @Resource
    BpmDefinitionAccessor bpmDefinitionAccessor;
    @Resource
    NatTaskService natTaskService;
    @Resource
    FreeMarkerEngine freeMarkerEngine;

    @Override
    public void onApplicationEvent(NodeNotifyEvent nodeNotifyEvent){
        NodeNotifyModel model = (NodeNotifyModel) nodeNotifyEvent.getSource();
        String defId = model.getBpmDefId();
        try{
            //获取流程各节点的消息设置
            BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
            ArrayNode nodes = JsonUtil.getMapper().createArrayNode();
            List<Form> formList = new ArrayList<Form>();
            List<NodeProperties> properties = new ArrayList<NodeProperties>();
            Map<String,List<Button>> btnMap = new HashMap<String, List<Button>>();
            Map<String, ObjectNode> nodeScriptMap = new HashMap<String, ObjectNode>();

            handNodeDefSetting("",bpmProcessDefExt.getBpmnNodeDefs(),properties,formList,nodes,btnMap,nodeScriptMap);
            	
            ActionCmd cmd = ContextThreadUtil.getActionCmd();
            String busData = cmd.getBusData();
            if(StringUtil.isEmpty(busData)) {
            	return;
            }
            ObjectNode node = (ObjectNode) JsonUtil.toJsonNode(busData);
            initModel(model,model.getTask(),cmd);
            getVars(model);
            for (NodeProperties property: properties){
                if (property.getNodeId().equals(model.getNodeId())){
                    if (StringUtil.isNotEmpty(model.getTiming()) && model.getTiming().equals(property.getSendType())){
                        if (!"null".equals(property.getEmail()) && BeanUtils.isNotEmpty(property.getEmail())){
                            String prop = property.getEmail();
                            String[] props = prop.split("\\.");
                            JsonNode n = node;
                            for (String p: props){
                                n = n.get(p);
                            }
                            JmsActor actor = new JmsActor();
                            List<JmsActor> actors = new ArrayList<>();
                            actor.setEmail(n.asText());
                            actors.add(actor);
                            model.setJmsActors(actors);
                            String content = parseHtmlContent(property.getTemplate(),model.getVars());
                            model.setContent(content);
                            MessageUtil.sendMsg(model,"mail","");
                        }
                        if ("null".equals(property.getPhone()) && BeanUtils.isNotEmpty(property.getPhone())){
                            if (property.getNodeId().equals(model.getNodeId())){
                                String prop = property.getPhone();
                                String[] props = prop.split("\\.");
                                JsonNode n = node;
                                for (String p: props){
                                    n = n.get(p);
                                }
                                JmsActor actor = new JmsActor();
                                List<JmsActor> actors = new ArrayList<>();
                                actor.setMobile(n.asText());
                                actors.add(actor);
                                model.setJmsActors(actors);
                                String content = parseHtmlContent(property.getTemplate(),model.getVars());
                                model.setContent(content);
                                MessageUtil.sendMsg(model,"sms","");
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void initModel(NodeNotifyModel model, BpmDelegateTask task,ActionCmd cmd) {
        model.setTaskId(task.getId());
        model.setBpmnInstId(task.getProcessInstanceId());
        model.setSubject((String)task.getVariable(BpmConstants.SUBJECT));
        model.setNodeId(task.getTaskDefinitionKey());
        model.setNodeName(task.getName());
        model.setProcInstId(task.getProcessInstanceId());
        model.setBpmnDefId(task.getBpmnDefId());
        model.setVars(task.getVariables());
        model.setActionType(ActionType.APPROVE);
        model.setActionName(cmd.getActionName());
        if(cmd instanceof TaskFinishCmd) {
        	model.setOpinion(((TaskFinishCmd)cmd).getApprovalOpinion());
        }
        
    }

    private void getVars(NodeNotifyModel model){
        String baseUrl= PortalDataUtil.getPropertyByAlias(TemplateConstants.TEMP_VAR.BASE_URL);
        ActionCmd taskCmd= ContextThreadUtil.getActionCmd();
        model.addVars(TemplateConstants.TEMP_VAR.BASE_URL, baseUrl)
                .addVars(TemplateConstants.TEMP_VAR.TASK_SUBJECT, model.getSubject()) //
                .addVars(TemplateConstants.TEMP_VAR.TASK_ID, model.getTaskId()) // 任务id
                .addVars(TemplateConstants.TEMP_VAR.CAUSE, model.getOpinion()) // 原因
                .addVars(TemplateConstants.TEMP_VAR.NODE_NAME, model.getNodeName())  // 节点名称
                .addVars(TemplateConstants.TEMP_VAR.AGENT, BeanUtils.isEmpty(model.getAgent())? "":model.getAgent().getFullname())// 代理人
                .addVars(TemplateConstants.TEMP_VAR.INST_SUBJECT,  model.getSubject())
                .addVars(TemplateConstants.TEMP_VAR.INST_ID, taskCmd.getInstId());
        DefaultBpmProcessInstance defaultBpmProcessInstance = (DefaultBpmProcessInstance) taskCmd.getTransitVars().get(BpmConstants.PROCESS_INST);
        if(BeanUtils.isNotEmpty(defaultBpmProcessInstance)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            model.addVars(TemplateConstants.TEMP_VAR.BPMNAME, defaultBpmProcessInstance.getProcDefName())
                    .addVars(TemplateConstants.TEMP_VAR.DATE, defaultBpmProcessInstance.getCreateTime().format(dateTimeFormatter))
                    .addVars(TemplateConstants.TEMP_VAR.CREATOR, defaultBpmProcessInstance.getCreator());
        }
    }

    //整理用户节点  节点信息，节点属性，节点表单，手机表单,节点按钮
    private void handNodeDefSetting(String parentDefKey,List<BpmNodeDef> nodeDefList,
                                    List<NodeProperties> properties,List<Form> formList, ArrayNode nodes,
                                    Map<String, List<Button>> btnMap,Map<String, ObjectNode> nodeScriptMap) throws IOException {
        for (BpmNodeDef nodeDef : nodeDefList) {
            String nodeId = nodeDef.getNodeId();
            NodeType type = nodeDef.getType();
            if (NodeType.START.equals(type)  || NodeType.CUSTOMSIGNTASK.equals(type)  || NodeType.USERTASK.equals(type) || NodeType.SIGNTASK.equals(type)) {
                ObjectNode node = JsonUtil.getMapper().createObjectNode();
                //节点信息
                node.put("name", nodeDef.getName());
                node.put("nodeId", nodeId);
                node.put("type", nodeDef.getType().toString());
                nodes.add(node);

                //节点表单，节点按钮，节点手机表单
                Form form = null;
                Form mobileForm = null;
                NodeProperties propertie = null;
                //本地节点。
                if (StringUtil.isEmpty(parentDefKey) || BpmConstants.LOCAL.equals(parentDefKey)){
                    propertie =nodeDef.getLocalProperties();
                    form = nodeDef.getForm();
                    mobileForm = nodeDef.getMobileForm();
                }else{
                    propertie = nodeDef.getPropertiesByParentDefKey(parentDefKey);
                    form = nodeDef.getSubForm(parentDefKey, FormType.PC);
                    mobileForm = nodeDef.getSubForm(parentDefKey, FormType.MOBILE);
                }
                //开始节点 添加属性配置
                if(propertie != null){
                    propertie.setNodeId(nodeId);
                    properties.add(propertie);
                }

                //设置form默认值
                if(form== null) {
                    form = new DefaultForm();
                    form.setType(FormCategory.INNER);
                }
                if(mobileForm== null){
                    mobileForm = new DefaultForm();
                    mobileForm.setFormType(FormType.MOBILE.value());
                }

                form.setNodeId(nodeId);
                mobileForm.setNodeId(nodeId);
                formList.add(mobileForm);
                formList.add(form);

                List<Button> buttons = nodeDef.getButtons();
                btnMap.put(nodeDef.getNodeId(), buttons);
            } else if (NodeType.SUBPROCESS.equals(type)) {
                SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef) nodeDef;
                BpmProcessDef<? extends BpmProcessDefExt> processDef = subProcessNodeDef.getChildBpmProcessDef();
                List<BpmNodeDef> bpmNodeDefs = processDef.getBpmnNodeDefs();
                handNodeDefSetting(parentDefKey, bpmNodeDefs, properties, formList,nodes,btnMap, nodeScriptMap);
            }
            //节点脚本
            Map<ScriptType, String> scriptMap = nodeDef.getScripts();
            if(!scriptMap.isEmpty()){
                nodeScriptMap.put(nodeId,(ObjectNode) JsonUtil.toJsonNode(scriptMap));
            }

        }
    }

    public String parseHtmlContent(String html,Map<String,Object> vars) {
        String content = "";
        try{
            content = freeMarkerEngine.parseByTemplate(html,vars);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
