package com.hotent.bo.bodef;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;


/**
 * BO定义接口类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoDefService {

	/**
	 * 根据bo别名获取bo定义
	 * <pre>
	 * 除了获取这个bo的定义，还包括其相关实体和属性定义
	 * </pre>
	 * @param alias	bo定义别名
	 * @return		bo定义
	 */
	BoDef getByAlias(String alias);
	
	/**
	 * 根据bo别名获取bo定义
	 * <pre>
	 * 只获取bo定义，不包括其实体和属性定义
	 * </pre>
	 * @param alias	bo定义别名
	 * @return		bo定义
	 */
	BoDef getPureByAlias(String alias);
	
	/**
	 * 根据bo定义ID获取bo定义
	 * <pre>
	 * 这个定义是完整的BO定义结构，包括相关实体和属性定义。
	 * </pre>
	 * @param defId	bo定义ID
	 * @return		bo定义
	 */
	BoDef getByDefId(String defId);
	
	
	/**
	 * 根据BO定义ID获取BO定义的XML数据结构
	 * @param defId	bo定义ID
	 * @return		bo定义（xml格式）
	 * @throws JAXBException 
	 */
	String getXmlByDefId(String defId) throws JAXBException;
	
	/**
	 * 根据xml返回实体定义
	 * <pre>
	 * 解析XML返回bo定义
	 * </pre>
	 * @param xml	bo定义xml
	 * @return		bo定义
	 * @throws JAXBException 
	 * @throws UnsupportedEncodingException 
	 */
	BoDef parseXml(String xml) throws UnsupportedEncodingException, JAXBException;
	
	/**
	 * 根据实体名称获取bo实体
	 * @param name	bo实体名称
	 * @return		bo实体
	 */
	BoEnt getEntByName(String name);

	/**
	 * 外部导入式更新 boDefs
	 * @param boDefs	要导入的bo定义列表
	 * @return			完成导入的bo定义列表
	 */
	List<BoDef> importBoDef(List<BoDef> boDefs);
}
