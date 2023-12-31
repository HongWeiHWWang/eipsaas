package com.hotent.bpmModel.manager;

import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.bpmModel.model.BpmOftenFlow;

/**
 * 
 * <pre> 
 * 描述：通用流程 处理接口
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-03-04 15:23:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmOftenFlowManager extends BaseManager<BpmOftenFlow>{
	/**
	 * 通过用户ID和流程key删除常用流程
	 * <pre>
	 * 不传入defkeys时，会删除该用户下的所有常用流程
	 * </pre>
	 * @param userId
	 * @param defkeys
	 */
    void removeByUserIdAndDefKeys(String userId, List<String> defkeys);
    
    /**
     * 添加或更新通用的常用流程
     * @param defkeys
     */
    void saveOrUpdateCommonFlow(String defkeys) throws Exception;
    
    /**
     * 保存我的常用流程
     * @param userId
     * @param userName
     * @param list
     */
    void saveMyFlow(String userId, String userName, ArrayNode list);
    
    /**
     * 通过用户ID和流程key查询常用流程列表
     * @param userId
     * @param defKeys
     * @return
     */
    List<BpmOftenFlow> getBpmOftenFlows(String userId, String defKey);
}
