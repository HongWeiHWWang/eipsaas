package com.hotent.bpm.plugin.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginContext;
import com.hotent.bpm.api.plugin.core.context.UserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmUserCalcPluginDef;

/**
 * 插件定义工具类。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-2-28-下午3:31:56
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class PluginContextUtil {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(PluginContextUtil.class);

	/**
	 * 根据父级元素对象返回插件上下文列表。
	 * <pre>
	 * 	 &lt;extPlugins>
     *          &lt;htplugin:userAssignPlugin>
     *              &lt;htplugin:startUserPlus logicCal="and">
     *                  &lt;htplugin:userId>${userId}&lt;/htplugin:userId>
     *              &lt;/htplugin:startUserPlus>
     *              &lt;htplugin:groupSelPlus>
     *                  &lt;htplugin:groupIds>1,2&lt;/htplugin:groupIds>
     *                  &lt;htplugin:groupNames>dep1,dep2&lt;/htplugin:groupNames>
     *              &lt;/htplugin:groupSelPlus>
     *          &lt;/htplugin:userAssignPlugin>
     *   &lt;/extPlugins>
	 *	如上例：
	 *  输入的元素为  &lt;extPlugins> 或 &lt;htplugin:userAssignPlugin>
	 *  
	 * </pre>
	 * @param parentNode	父级XML元素
	 * @return 
	 * List &lt;BpmPluginContext>
	 * @exception 
	 * @since  1.0.0
	 */
	public static List<UserCalcPluginContext> getUserCalcPluginContexts(Element parentNode){
		List<UserCalcPluginContext> list=new ArrayList<UserCalcPluginContext>();
		
		NodeList childNodes= parentNode.getChildNodes();
		
		for(int i=0;i<childNodes.getLength();i++){
			Node node=childNodes.item(i);
			
			//获得节点名，判断是否为空
			String name=node.getNodeName();
			if(name==null )  continue;
			
			if(node instanceof Element){
				Element el=(Element)node;				
				//获取算方法插件的上下文类。
				String pluginContextBeanId = name + PluginContext.PLUGINCONTEXT;
				UserCalcPluginContext pluginContext=(UserCalcPluginContext)AppUtil.getBean(pluginContextBeanId);
				
				if(pluginContext==null)  continue;
				
				//解析节点
				BpmUserCalcPluginDef bpmPluginDef =(BpmUserCalcPluginDef) pluginContext.parse(el);
				
				
				AbstractUserCalcPluginContext calcPluginContext=(AbstractUserCalcPluginContext)pluginContext;
				
				calcPluginContext.setBpmPluginDef(bpmPluginDef);
				
				//加入集合
				list.add(pluginContext);
			}
		}
				
		return list;
	}

	public static List<EventType> getEventTypes(String eventTypeString) {
		List<EventType> eventTypes = new ArrayList<EventType>();
		StringTokenizer st = new StringTokenizer(eventTypeString,",");
		while(st.hasMoreTokens()){
			String event = st.nextToken();
			EventType eventType = EventType.fromKey(event);
			if(eventType!=null)
				eventTypes.add(eventType);
		}
		return eventTypes;
	}
}
