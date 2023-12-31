package com.hotent.bpm.api.context;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;

/**
 * 流程上下文数据工具类。
 * @author ray
 *
 */
public class BpmContextUtil {
	/**
	 * 获取上下文的bo实例数据。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,ObjectNode> getBoFromContext(){
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		if (cmd == null || cmd.getTransitVars(BpmConstants.BO_INST) == null) {
			return new HashMap<>();
		}
		return (Map<String, ObjectNode>) cmd.getTransitVars(BpmConstants.BO_INST);
	}
	
	/**
	 * 将bo数据放到上下文。
	 * 这种试用在没有cmd的环境中。
	 * @param ObjectNodes
	 * @throws IOException 
	 */
	public static void setBoToContext(List<ObjectNode> boDatas) throws Exception{
		ActionCmd cmd =ContextThreadUtil.getActionCmd();
		if (BeanUtils.isEmpty(cmd)) {
			cmd =new BaseActionCmd();
			ContextThreadUtil.setActionCmd(cmd);
		}
		
		Map<String,ObjectNode> boMap=new HashMap<String, ObjectNode>();
		
		for(ObjectNode data:boDatas){
			String code= data.get("boDefAlias").asText();
			boMap.put(code, data);
			if(data.has("data")){
				JsonNode jsonNode = data.get("data");
				boMap.put(code, (ObjectNode) jsonNode);
			}
		}
		cmd.addTransitVars(BpmConstants.BO_INST, boMap);
		
	}
	
}
