package com.hotent.bpm.api.event;

import com.hotent.bpm.api.model.delegate.BpmDelegateTask;

/**
 * @author qiuxd
 * @company 广州宏天软件股份有限公司
 * @email qiuxd@jee-soft.cn
 * @date
 */
public class NodeNotifyModel extends NotifyTaskModel{

    private String bpmDefId = "";

    private String nodeId = "";

    private BpmDelegateTask task;

    private String subject = "";

    private String content = "";

    private String timing = "";

    public NodeNotifyModel(){}

    public NodeNotifyModel(String bpmDefId,String nodeId){
        this.bpmDefId = bpmDefId;
        this.nodeId = nodeId;
    }

    public String getBpmDefId() {
        return bpmDefId;
    }

    public void setBpmDefId(String bpmDefId) {
        this.bpmDefId = bpmDefId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public BpmDelegateTask getTask() {
        return task;
    }

    public void setTask(BpmDelegateTask task) {
        this.task = task;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
