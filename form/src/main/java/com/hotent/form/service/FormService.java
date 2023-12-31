package com.hotent.form.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.CommonResult;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoResult;
import com.hotent.form.model.Form;
import com.hotent.form.vo.FormRestfulModel;

public interface FormService {

	/**
	 * 调用form模块的restful接口。根据BO别名获BO定义
	 * 
	 * @param alias
	 * @throws IOException 
	 * @returnBpmForm boDefService.getByName
	 */
	public BoDef getBoDefByAlias(String alias) throws IOException;
	
	/**
	 * 根据业务对象别名或id获取主BoEnt
	 * 
	 * @param alias
	 * @param defId
	 * @throws IOException 
	 * @returnBpmForm boDefService.getByName
	 */
	public ObjectNode getMainBOEntByDefAliasOrId(String alias,String defId) throws IOException;


	/**
	 * 调用form模块接口处理bo数据
	 * @param id  boid。空为新增。不为空则更新  
	 * @param defId bo定义id
	 * @param boData 业务数据
	 * @param saveType 保存类型。1，database 。2，boObject
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	List<BoResult> handlerBoData(String id, String defId, ObjectNode boData, String saveType) throws JsonParseException, JsonMappingException, IOException;



	/** 
	 *  对应bodatahandler.getByBoDefCode
	 * @param saveMode 保存方式
	 * @param code  bocode
	 * @return
	 */
	BoData getBodataByDefCode(String saveMode, String code);


	/**
	 *  对应bodatahandler.getByBoDefCode
	 *  根据实例ID和bo定义code获取BODATA，只返回两层。
	 * 1.根据bodefCode获取bo定义。
	 * 2.根据bo定义获取数据。
	 * @param saveMode
	 * @param id
	 * @param code
	 * @return 返回值不变
	 * @throws IOException 
	 */
	BoData getBodataById(String saveMode ,String id, String code) throws IOException;

	/**
	 * 根据formkey获取表单。
	 * @param formKey
	 * @return FormModel
	 */
	Form getByFormKey(String formKey);



	/**
	 *   根据bodef获得导出用的xml文件
	 * @param formKeys
	 * @return
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	String getBoDefExportXml(ObjectNode bodef) throws JAXBException, JsonParseException, JsonMappingException, IOException;



	/**
	 * 根据表单ID取得表单对象。
	 * @param formId
	 * @return FormModel
	 */

	List<BoDef> importBo(String bodefXml);


	/**
	 *   导入bodef对象
	 * @param bos
	 * @return
	 */
	List<BoDef> importBoDef(List<BoDef> bos);


	/**
	 *   导入form
	 * @param formfXml
	 * @return
	 */
	void importForm(String formXmlStr);



	/**
	 * 通过bo定义id获取bo的json格式定义 bODefManager.getBOJson(def.get("id"));
	 * @param id	bo定义id
	 * @return		json格式定义数据
	 * @throws IOException 
	 */
	ObjectNode getBoJosn(String id) throws IOException;


	/**
	 * 通过bo实例name获取boent  boDefService.getEntByName
	 * @param id	bo实体name
	 * @return		json格式定义数据
	 */
	BoEnt getBoEntByName(String name);

	Form getByFormId(String formId);

	/**
	 * 根据formKey 导出表单
	 * @param formKeys
	 * @return
	 */
	String getFormExportXml(String formKeys);
	
	/**
	 * 根据boCode 获取BoData
	 * @param boCode
	 * @return
	 */
	public List<BoData> getBoDataByBoKeys(List<String> boCode);

	public Map<String, String> getFormAndBoExportXml(ObjectNode obj) throws JAXBException;

	/**
	 * 根据业务数据关联对象清除流程相关数据
	 * @param links
	 * @throws Exception
	 */
	void removeDataByBusLink(JsonNode links)throws Exception;

	public CommonResult<String> importFormAndBo(ObjectNode obj) throws Exception;
	/**
	 * 通过FormRestfulModel获取BoData
	 * @param model
	 * @return
	 * @throws IOException
	 */
	BoData getByFormRestfulModel(FormRestfulModel model) throws IOException;

}
