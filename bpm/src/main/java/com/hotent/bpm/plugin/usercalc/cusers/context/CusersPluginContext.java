package com.hotent.bpm.plugin.usercalc.cusers.context;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.cuserrel.def.ExecutorVar;
import com.hotent.bpm.plugin.usercalc.cusers.def.CusersPluginDef;
import com.hotent.bpm.plugin.usercalc.cusers.runtime.CusersPlugin;
import com.jamesmurty.utils.XMLBuilder;

public class CusersPluginContext extends AbstractUserCalcPluginContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8757352972830358986L;

	//	<xs:enumeration value="start"></xs:enumeration>
//    <xs:enumeration value="prev"></xs:enumeration>
//    <xs:enumeration value="spec"></xs:enumeration>
//    <xs:enumeration value="var"></xs:enumeration>
	@Override
	public String getDescription() {
		String str="";
		CusersPluginDef def=(CusersPluginDef) this.getBpmPluginDef();
		if(def==null) return "";
		String source=def.getSource();
		if("currentUser".equals(source)){
			str="当前登录人";
		}
		if("start".equals(source)){
			str="发起人";
		}
		else if("prev".equals(source)){
			str="上一步执行人";
		}
		else if("var".equals(source)){
			str=def.getVar().getSource()+"["+def.getVar().getExecutorType()+":"+def.getVar().getName()+"]";
		}
		else if("spec".equals(source)){
			str=def.getUserName();
		}
		
		return str;
	}



	@Override
	public String getTitle() {
		return "用户";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return CusersPlugin.class;
	}

	@Override
	public String getPluginXml() {
		CusersPluginDef def=(CusersPluginDef) this.getBpmPluginDef();
		if(def==null) return "";
		try {
			String source=def.getSource();
			
			XMLBuilder xmlBuilder = XMLBuilder.create("cusers")
					.a("xmlns", "http://www.jee-soft.cn/bpm/plugins/userCalc/cusers")
					.a("extract", def.getExtract().getKey())
					.a("logicCal", def.getLogicCal().getKey())
					.a("source", def.getSource());
			
			if("var".equals(source)){
				xmlBuilder.e("var").a("source", def.getVar().getSource())
								   .a("name", def.getVar().getName())
								   .a("executorType", def.getVar().getExecutorType())
								   .a("userValType", def.getVar().getUserValType());
			}
			
			if("spec".equals(source)){
				xmlBuilder.e("members").a("account", def.getAccount()).a("userName", def.getUserName());
			}
			
			return xmlBuilder.asString();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}		
		
		return "";
		
	}

	//	<cusers xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/cusers"  logicCal="or" extract="no" type="start">
	//    <var name=""/>
	//    <members account="" userName=""/>
	//</cusers>
	@Override
	protected BpmPluginDef parseElement(Element element) {

		String source=element.getAttribute("source");
		CusersPluginDef def=new CusersPluginDef();
		def.setSource(source);
		Element varEl= XmlUtil.getChildNodeByName(element, "var");
		if(varEl!=null){
			String name=varEl.getAttribute("name");
			String varSource=varEl.getAttribute("source");
			String executorType=varEl.getAttribute("executorType");
			String userValType=varEl.getAttribute("userValType");
			String groupValType=varEl.getAttribute("groupValType");
			String dimension=varEl.getAttribute("dimension");
			ExecutorVar executorVar = new ExecutorVar(varSource, name, executorType,userValType,groupValType, dimension);
			def.setExecutorVar(executorVar); 
		}
		
		Element memberEl= XmlUtil.getChildNodeByName(element, "members");
		if(memberEl!=null){
			String account=memberEl.getAttribute("account");
			String userName=memberEl.getAttribute("userName");
			
			def.setAccount(account);
			def.setUserName(userName);
		}
		
		return def;
	}
	
	
//	 <xs:enumeration value="start"></xs:enumeration>
//     <xs:enumeration value="prev"></xs:enumeration>
//     <xs:enumeration value="spec"></xs:enumeration>
//     <xs:enumeration value="var"></xs:enumeration>
	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) throws Exception{
		//{"accounts":"zhangsan","extractType":"EXACT_NOEXACT","logicType":"OR","pluginName":"","type":"start","userNames":"张三","var":""}
        String source=pluginJson.get("source").asText();

        CusersPluginDef def=new CusersPluginDef();
        def.setSource(source);

        if("var".equals(source)){
            ExecutorVar executorVar = null;
            try {
                executorVar = (ExecutorVar) JsonUtil.toBean(pluginJson.get("var").asText(),ExecutorVar.class);
            } catch (Exception e) {
                executorVar = (ExecutorVar) JsonUtil.toBean(pluginJson.get("var"),ExecutorVar.class);
            }

            def.setExecutorVar(executorVar);
        }

        if("spec".equals(source)){
            String accounts=pluginJson.get("account").asText();
            String userNames=pluginJson.get("userName").asText();
            def.setAccount(accounts);
            def.setUserName(userNames);
        }


        return def;
	}
	
	public static void main(String[] args) throws Exception {
  		CusersPluginDef def=new CusersPluginDef();
  		def.setAccount("zhangsan");
  		def.setUserName("张三");
  		def.setSource("var");
  		def.setExecutorVar(new ExecutorVar("source", "name", "group", "account", "", ""));
  		ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(def);
  		System.out.println(obj.toString());
  	
  		String source=def.getSource();
		
		XMLBuilder xmlBuilder = XMLBuilder.create("cusers")
				.a("xmlns", "http://www.jee-soft.cn/bpm/plugins/userCalc/cusers")
				.a("extract", def.getExtract().getKey())
				.a("logicCal", def.getLogicCal().getKey())
				.a("source", def.getSource());
		
		if("var".equals(source)){
			xmlBuilder.e("var").a("source", def.getVar().getSource())
							   .a("name", def.getVar().getName())
							   .a("executorType", def.getVar().getExecutorType())
							   .a("userValType", def.getVar().getUserValType());
		}
		
		if("spec".equals(source)){
			xmlBuilder.e("members").a("account", def.getAccount()).a("userName", def.getUserName());
		}
  		
  		System.out.println(xmlBuilder.asString());
  		CusersPluginContext ctx=new CusersPluginContext();
		System.out.println(ctx.getType());
	}

}
