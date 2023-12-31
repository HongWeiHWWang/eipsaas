package com.hotent.form.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.FormRight;


/**
 * 
 * <pre> 
 * 描述：form_right DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-04-19 14:22:02
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface FormRightDao extends BaseMapper<FormRight> {
	
	/**
	 * 根据流程定义Id获取权限配置数据。
	 * @param actDefId			流程定义KEY
	 * @param parentFlowKey		父级流程定义KEY
	 * @return
	 */
	FormRight getByFlowKey(@Param("flowKey")String flowKey,@Param("parentFlowKey")String parentFlowKey,@Param("permissionType")int permissionType) ;
	
	
	/**
	 * 根据流程定义ID节点ID 和父流程定义ID获取权限配置数据。
	 * @param flowKey
	 * @param nodeId
	 * @param parentFlowKey
	 * @param permissionType
	 * @return
	 */
	FormRight getByFlowNodeId(@Param("flowKey")String flowKey,@Param("nodeId")String nodeId, @Param("parentFlowKey")String parentFlowKey, @Param("permissionType")int permissionType);
	
	/**
	 * 根据表单的Key获取表单配置的基础权限。
	 * @param formKey
	 * flowKey 为null 的数据
	 * @param isReadOnly 是否只读权限
	 * @return
	 */
	FormRight getByFormKey(@Param("formKey")String formKey, @Param("isReadOnly")boolean isReadOnly);
	
	/**
	 * 根据流程key和流程节点删除权限。
	 * @param flowKey
	 * @param nodeId
	 * @param parentFlowKey
	 */
	void removeByFlowNode(@Param("flowKey")String flowKey,@Param("nodeId")String nodeId, @Param("parentFlowKey")String parentFlowKey);
	
	/**
	 * 根据流程key进行删除。
	 * @param flowKey
	 * @param parentFlowKey
	 * @param permissionType
	 */
	void removeByFlowKey(@Param("flowKey")String flowKey, @Param("parentFlowKey")String parentFlowKey,@Param("permissionType")int permissionType);

	
	/**
	 * 根据formKey删除表单权限。
	 * @param formKey
	 */
	void removeByFormKey(String formKey);


	List<FormRight> getAllByFlowKey(String flowKey);

	//根据流程定义KEY、节点ID判断当前节点审批记录是否显示
    String getByTeam(@Param("flowKey")String flowKey, @Param("nodeId")String nodeId);
    
    /**
     * 根据flowKey删除表单权限
     * @param flowKey
     */
    void emptyAll(@Param("flowKey")String flowKey);
}
