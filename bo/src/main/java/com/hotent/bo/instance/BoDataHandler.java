package com.hotent.bo.instance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoResult;

/**
 * bo实例数据处理器
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoDataHandler {
	
	/**
	 * 保存bo数据
	 * @param id		主键
	 * @param defId		bo定义ID
	 * @param curData	bo数据
	 * @return			保存结果
	 */
	List<BoResult> save(String id,String defId,BoData curData) throws IOException;
	
	/**
	 * 根据实例ID和bo定义alias获取bo数据(最多返回两层)
	 * <pre>
	 * 1.根据bodefAlias获取bo定义;
	 * 2.根据bo定义和ID获取数据。
	 * </pre>
	 * @param id			bo数据主键
	 * @param bodefAlias	bo定义的别名
	 * @return				bo数据
	 */
	BoData getById(Object id, String bodefAlias) throws IOException;
	
	/**
	 * 根据实例ID和bo定义alias获取bo数据(不限层级)
	 * <pre>
	 * 1.根据bodefAlias获取bo定义;
	 * 2.根据bo定义和ID获取数据。
	 * </pre>
	 * @param id			bo数据主键
	 * @param bodefAlias	bo定义别名
	 * @return				bo数据
	 */
	BoData getResById(Object id,String bodefAlias) throws IOException;
	
	/**
	 * 根据bo定义别名返回 bo数据对象实例
	 * @param bodefAlias	bo定义别名
	 * @return				bo数据
	 */
	BoData getByBoDefAlias(String bodefAlias);
	
	/**
	 * 获取存储方式
	 * @return	存储方式
	 */
	String saveType();
	
	/**
	 * 通过bo定义别名和bo数据主键集合删除数据
	 * @param bodefAlias	bo定义别名
	 * @param aryIds		bo数据主键集合
	 */
	void removeBoData(String bodefAlias, String[] aryIds);
	
	/**
	 * 通过bo定义别名和查询参数获取bo数据列表(不分页)
	 * @param bodefAlias	bo定义别名
	 * @param param			查询参数(可为空)
	 * @return				bo数据列表
	 */
	List<Map<String,Object>> getList(String bodefAlias, Map<String, Object> param);
	
	/**
	 * 通过bo定义别名和查询参数获取bo数据列表(分页)
	 * @param bodefAlias	bo定义别名
	 * @param queryFilter	通用查询条件
	 * @return				bo数据列表(分页)
	 */
	PageList<Map<String, Object>> getList(String bodefAlias, QueryFilter queryFilter);
	
	
	BoData getByBoDefCode(String bodefCode);
	
}
