package com.hotent.bo.persistence.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.bo.model.BoDef;

/**
 * 流程定义处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoDefManager extends BaseManager<BoDef> {
	/**
	 * 根据id数组批量删除bo定义
	 * <pre>
	 * 该方法会批量删除BoDef，关联的BoEnt、BoDef与BoEnt的关联关系
	 * </pre>
	 * @param ids
	 */
	void removeByIds(String... ids);
	/**
	 * 根据defId名称获取整个实体定义
	 * <pre>
	 * 获取bo定义并构建关联数据
	 * </pre>
	 * 
	 * @param defId	bo定义id
	 * @return		bo定义
	 */
	BoDef getByDefId(String defId);
	
	/**
	 * 更新流程定义
	 * @param boDef
	 */
	void update(BoDef boDef);
	/**
	 * 通过别名获取bo定义
	 * <pre>
	 * 获取bo定义并构建关联数据
	 * </pre>
	 * 
	 * @param alias	别名
	 * @return		bo定义
	 */
	BoDef getByAlias(String alias);
	
	/**
	 * 通过别名获取bo定义
	 * <pre>
	 * 只获取bo定义，不递归构建关联数据
	 * </pre>
	 * 
	 * @param alias	别名
	 * @return		bo定义
	 */
	BoDef getPureByAlias(String alias);

	/**
	 * 根据XML解析成bo定义对象。
	 * 
	 * @param xml	xml格式bo定义数据
	 * @return		bo定义
	 */
	List<BoDef> parseXml(String xml);

	/**
	 * 将bo定义序列化成XML字符串。
	 * 
	 * @param boDef	bo定义
	 * @return		xml格式bo定义数据
	 */
	String serialToXml(List<BoDef> boDefs);

	/**
	 * 保存bo定义
	 * <pre>
	 * 用于导入bo定义
	 * </pre>
	 * 
	 * @param boDef
	 * @return 返回导入信息
	 */
	List<BoDef> importBoDef(List<BoDef> boDefs);

	/**
	 * 保存bo定义到数据库中
	 * <pre>
	 * 保存bo定义的同时保存实体、属性及对应的关联关系，
	 * 该方法要求传入指定格式的json字符串，参考的json数据可以查看
	 * 项目中src/test/resources/json/bodef-test.json
	 * </pre>
	 * @param json
	 * @throws Exception
	 */
	void save(String json) throws Exception;
	
	/**
	 * 通过bo定义id获取bo的json格式定义
	 * @param id	bo定义id
	 * @return		json格式定义数据
	 */
	ObjectNode getBOJson(String id) throws IOException;
	
	/**
	 * 通过别名删除bo定义
	 * @param alias	别名
	 */
	int removeByAlias(String alias);
	
	/**
	 * 根据表单key获取对应的bo列表
	 * @param formKey
	 * @return
	 */
	List<BoDef> getByFormKey(String formKey);
	
	
	/**
	 * 通过bo定义code获取bo的json格式定义
	 * @param code	bo定义code
	 * @return		json格式定义数据
	 */
	ObjectNode getBOJsonByBoDefCode(String code) throws IOException;

	/**
	 * 修改分类
	 * @param boDef
	 * @return
	 */
	void updateCategory(BoDef boDef);

	/**
	 * 保存表单数据
	 * @param json
	 * @return
	 */
	String saveFormData(String json) throws Exception;

	/**
	 * 获取绑定数据
	 * @param id
	 * @param alias
	 * @return
	 */
	Map getBindData(String id, String alias) throws IOException;

	List<Map<String,String>> getBpmDefinitionData(String formKey) throws Exception;

	/**
	 * 获取隐藏字段
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	List getHideAttr(String tableName) throws Exception;

	/**
	 * 获取bo定义详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	ObjectNode getBoDefDetails(String id) throws Exception;

	/**
	 * 获取BO树形数据
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	ObjectNode getBoTreeData(String ids) throws Exception;
}
