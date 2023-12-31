package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 节点按钮操作处理器。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-16-上午9:07:00
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class ButtonsBpmDefXmlHandler extends AbstractBpmDefXmlHandler<List<Button>> {
	
//	 <ext:buttons>
//     <ext:button name="" alias=""></ext:button>
//   </ext:buttons>
	private String getXml(List<Button> buttons){
		String xml="";
		
		if(BeanUtils.isEmpty(buttons)) return "";
		
		try {
			XMLBuilder ruleBuilder = XMLBuilder.create("ext:buttons");
			ruleBuilder.a("xmlns:ext",BpmConstants.BPM_XMLNS);
			Boolean isFalg = false;
            for(Button btn:buttons){
                if ("lockUnlock".equals(btn.getAlias()) && btn.getIsLock()==true){
                    isFalg = true;
                    break;
                }
            }
			for(Button btn:buttons){
			    String isLock = "false";
			    if(isFalg){
                    isLock = "true";
                }
				XMLBuilder build= ruleBuilder.e("ext:button").a("name", btn.getName())
				.a("alias", btn.getAlias())
				.a("isLock", isLock);
				if(BeanUtils.isNotEmpty(btn.getBeforeScript())){
					build.e("ext:beforeScript").d(btn.getBeforeScript());
				}
				if(BeanUtils.isNotEmpty(btn.getAfterScript())){
					build.e("ext:afterScript").d(btn.getAfterScript());
				}
				
				if(BeanUtils.isNotEmpty(btn.getGroovyScript())){
					build.e("ext:groovyScript").d(btn.getGroovyScript());
				}
				
				if(BeanUtils.isNotEmpty(btn.getRejectMode())){
					build.e("ext:rejectMode").d(btn.getRejectMode());
				}
				
				ruleBuilder.up();
			}
			xml= ruleBuilder.asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return xml;
	}
	
	public static void main(String[] args) {
		
		//
		//List<Button> buttons=new ArrayList<Button>();
		//Button btn1=new Button("同意","agree","return true","return false;");
		//Button btn2=new Button("反对","oppose");
		//Button btn3=new Button("反对2","oppose","return true","return false;");
		//Button btn4=new Button("反对1","oppose");
		//
		//buttons.add(btn1);
		//buttons.add(btn2);
		//buttons.add(btn3);
		//buttons.add(btn4);
		//
		//ButtonsBpmDefXmlHandler handler=new ButtonsBpmDefXmlHandler();
		//String xml=handler.getXml(buttons);
		//Document doc=Dom4jUtil.loadXml(xml);
		//System.out.println(doc.asXML());
		
	}
	
	@Override
	protected String getXml(String defId, String nodeId, List<Button> buttons) {
		String xml=getXml(buttons);
		String xPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:buttons";
		String xParentPath="//ext:*[@bpmnElement='"+nodeId+"']";
		
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		
		String rtnXml= DefXmlHandlerUtil.getXml(defXml, xml, xParentPath, xPath);
		
		return rtnXml;
	}
	
	

}
