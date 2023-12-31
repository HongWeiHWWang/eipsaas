package com.hotent.portal.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotent.activemq.model.JmsTableTypeConf;
import com.hotent.activemq.model.JmsTableTypeFiledDetail;
import com.hotent.portal.jms.JmsHandler;
import com.hotent.portal.jms.JmsMessageConsumer;
import com.hotent.portal.jms.impl.DingTalkHandler;
import com.hotent.portal.jms.impl.InnerHandler;
import com.hotent.portal.jms.impl.MailHandler;
import com.hotent.portal.jms.impl.SmsHandler;
import com.hotent.portal.jms.impl.VoiceHandler;
import com.hotent.portal.jms.impl.WxEnterpriseHandler;

/**
 * jms消息处理器配置类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
@Configuration
public class JmsHandlerConfig {
	@Resource
    MailHandler mailHandler;
    @Resource
    InnerHandler innerHandler;
    @Resource
    SmsHandler smsHandler;
    @Resource
    VoiceHandler voiceHandler;
    @Resource
    WxEnterpriseHandler wxEnterpriseHandler;
    @Resource
    DingTalkHandler dingTalkHandler;
    
	@Bean
    public List<JmsHandler> jmsHandList(){
        List<JmsHandler> list = new ArrayList<>();
        list.add(innerHandler);
        list.add(mailHandler);
        list.add(smsHandler);
        list.add(voiceHandler);
        list.add(wxEnterpriseHandler);
        list.add(dingTalkHandler);
        return list;
    }
	
	@Bean
	@ConditionalOnProperty(value="jms.enable", matchIfMissing = true)
	public JmsMessageConsumer messageConsumer(List<JmsHandler> jmsHandList) {
		JmsMessageConsumer jmsReceiverEventListener = new JmsMessageConsumer();
		jmsReceiverEventListener.setJmsHandList(jmsHandList);
		return jmsReceiverEventListener;
	}
	@Bean("portalTableTypeConf")
	public JmsTableTypeConf TableTypeConf() {
		JmsTableTypeConf.AddTypeConf("FILE_TYPE",new JmsTableTypeFiledDetail("portal_sys_file","ID_", "", "TYPE_"));
		return null;
	}
}
