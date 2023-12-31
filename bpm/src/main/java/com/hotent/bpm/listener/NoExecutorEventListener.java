package com.hotent.bpm.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.hotent.bpm.api.event.NoExecutorEvent;
import com.hotent.bpm.api.event.NoExecutorModel;

/**
 * 没有任务执行人监听器。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-5-10-下午3:25:58
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class NoExecutorEventListener implements  ApplicationListener<NoExecutorEvent>,Ordered {

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public void onApplicationEvent(NoExecutorEvent ev) {
		NoExecutorModel model=(NoExecutorModel)ev.getSource();
		
		System.out.println(model.getNodeId());
		
	}
}
