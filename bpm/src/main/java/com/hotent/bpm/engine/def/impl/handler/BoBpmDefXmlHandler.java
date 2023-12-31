package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmBoDef;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.jamesmurty.utils.XMLBuilder;


/**
 * BO数据定义修改。 
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014年8月21日-下午3:23:06
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class BoBpmDefXmlHandler extends AbstractBpmDefXmlHandler<BpmBoDef >{

	@Override
	protected String getXml(String defId, String nodeId,BpmBoDef boDef) {
		String xml=getXml(boDef);
		
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		
		String xParentPath="//ext:extProcess";
		String xPath="//ext:extProcess/ext:boList";
		
		String rtnXml= DefXmlHandlerUtil.getXml(defXml, xml, xParentPath, xPath);
		//System.out.println(rtnXml);
		return rtnXml;
	}
	
	private String getXml(BpmBoDef bodef){
		
		List<ProcBoDef> list=bodef.getBoDefs();
		
		if(BeanUtils.isEmpty(list)) return "";
		
		StringBuffer sb=new StringBuffer();
		try {
			
			sb.append("<ext:boList xmlns:ext=\""+BpmConstants.BPM_XMLNS+"\" saveMode=\""+bodef.getBoSaveMode()+"\" >\n");
			for(ProcBoDef boDef : list){
				String isRequired =boDef.isRequired()? "1":"0";
				
				XMLBuilder xmlBuilder = XMLBuilder.create("ext:boDef")
						.a("name", boDef.getName())
						.a("isRequired", isRequired)
						.a("key", boDef.getKey())
				        .a("parentDefKey", boDef.getParentDefKey());
				sb.append(xmlBuilder.asString()+"\n");
			}
			sb.append("</ext:boList>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

}
