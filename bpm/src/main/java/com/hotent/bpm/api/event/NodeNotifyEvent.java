package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author qiuxd
 * @company 广州宏天软件股份有限公司
 * @email qiuxd@jee-soft.cn
 * @date
 */
public class NodeNotifyEvent extends ApplicationEvent {

    public NodeNotifyEvent(NodeNotifyModel source) {
        super(source);
    }
}
