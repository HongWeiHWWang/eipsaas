package com.hotent.form.persistence.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.FormRight;



/**
 * 表单授权管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface FormRightManager extends BaseManager<FormRight>{
	/**
	 * 删除实例权限。
	 * @param flowKey
	 */
	void removeInst(String flowKey);
	
	/**
	 * 
	 * 如果流程表单，节点已经授权了表单， 但已经更换了表单， 则删除原来的
	 * @param formKey
	 * @param flowKey
	 * @param nodeId
	 * @param parentFlowKey
	 */
	void remove(String formKey,String flowKey,String nodeId,String parentFlowKey);
	
	/**
	 * 删除流程节点上的权限。
	 * @param flowKey
	 */
	void remove(String flowKey,String nodeId, String parentFlowKey);
	
	/**
	 * 删除流程定义权限。
	 * @param flowKey
	 * @param parentFlowKey
	 */
	void remove(String flowKey,String parentFlowKey);
	
	/**
	 * 保存表单权限。
	 * @param formKey			表单key		对应form_definition 的key 字段。
	 * @param flowKey			流程key
	 * @param parentFlowKey		父流程key
	 * @param nodeId			节点ID
	 * @param permission		流程权限
	 * @param type				类型(1,流程权限,2,实例权限)
	 */
	void save(String formKey, String flowKey,String parentFlowKey,
			String nodeId,String permission,int type,String isCheckOpinion);
	/**
	 * 获取表单权限设定。
	 * {
		    "table": {
		        "gerejianli": {
		            "description": "个人简历",
		            "fields":{
				            "name": {
				                "description": "姓名",
				                "read": [
				                    {
				                        "type": "everyone"
				                    }
				                ],
				                "write": [
				                    {
				                        "type": "user",
				                        "id":"1,2,..."
				                        "name":"a,b,..."
				                    }
				                ],
				                "required": [
				                    {
				                        "type": "script",
				                        "id":"xxxxxxxxx"
				                    }
				                ]
				            },
				            "age": {
				                "description": "年龄",
				                "read": [
				                    {
				                        "type": "everyone"
				                    }
				                ],
				                "write": [
				                    {
				                        "type": "none"
				                    }
				                ],
				                "required": [
				                    {
				                        "type": "none"
				                    }
				                ]
				            }
				    }
		            ,
		            "main": true
		        },
		        "education": {
		            "description": "教育经历",
		            fields":{
			            "school": {
			                "description": "毕业学校",
			                "read": [
			                    {
			                        "type": "everyone"
			                    }
			                ],
			                "write": [
			                    {
			                        "type": "none"
			                    }
			                ],
			                "required": [
			                    {
			                        "type": "none"
			                    }
			                ]
			            }
			        }
		            ,
		            "main": false,
		            "rights": {
		                "hidden": false,
		                "add": true,
		                "del": true,
		                "required": true
		            }
		        }
		    },
		    "opinion": {
		        "bumenjingli": {
		            "description": "部门经理",
		            "read": [
		                {
		                    "type": "everyone"
		                }
		            ],
		            "write": [
		                {
		                    "type": "none"
		                }
		            ],
		            "required": [
		                {
		                    "type": "none"
		                }
		            ]
		        },
		        "caiwu": {
		            "description": "财务意见",
		            "read": [
		                {
		                    "type": "everyone"
		                }
		            ],
		            "write": [
		                {
		                    "type": "none"
		                }
		            ],
		            "required": [
		                {
		                    "type": "none"
		                }
		            ]
		        }
		    }
		}
	 * @param formKey				form_definition 的key字段。
	 * @param flowKey
	 * @param parentFlowKey
	 * @param nodeId
	 * @param isGlobalPermission 是否获取全局表单权限。一般情况直接传true
	 * @return
	 * @throws IOException 
	 */
	JsonNode getPermissionSetting(String formKey,String flowKey,String parentFlowKey,String nodeId,int type, boolean isGlobalPermission) throws IOException;
	
	/**
	 * 获取默认表单权限。
	 * {
		    "fields": {
		        "table1": {
		            "name": "w",
		            "age": "b"
		        },
		        "table2": {
		            "name": "w",
		            "age": "r"
		        },
		        "table3": {
		            "name": "w",
		            "age": "w"
		        }
		    },
		    "table":{
		     "table1":{"hidden":true}
		     "table2":{"hidden":false,"add":"true","del":"true","required":"true"}
		    },
		    "opinion":{"jzyj":"w","zxyj":"r","zxyj":"b"}
		}	
	 * @param formKey			表单KEY 对应BPM_FROM key字段。
	 * @param flowKey
	 * @param parentFlowKey
	 * @param nodeId
	 * @param type
	 * @return
	 * @throws IOException 
	 */
	JsonNode getPermission(String formKey,String flowKey,String parentFlowKey,String nodeId,int type, boolean isGlobalPermission) throws IOException;
	/**
	 * 获取表单权限设定。
	 * {
		    "table": {
		        "gerejianli": {
		            "description": "个人简历",
		            "fields":{
				            "name": {
				                "description": "姓名",
				                "read": [
				                    {
				                        "type": "everyone"
				                    }
				                ],
				                "write": [
				                    {
				                        "type": "user",
				                        "id":"1,2,..."
				                        "name":"a,b,..."
				                    }
				                ],
				                "required": [
				                    {
				                        "type": "script",
				                        "id":"xxxxxxxxx"
				                    }
				                ]
				            },
				            "age": {
				                "description": "年龄",
				                "read": [
				                    {
				                        "type": "everyone"
				                    }
				                ],
				                "write": [
				                    {
				                        "type": "none"
				                    }
				                ],
				                "required": [
				                    {
				                        "type": "none"
				                    }
				                ]
				            }
				    }
		            ,
		            "main": true
		        },
		        "education": {
		            "description": "教育经历",
		            fields":{
			            "school": {
			                "description": "毕业学校",
			                "read": [
			                    {
			                        "type": "everyone"
			                    }
			                ],
			                "write": [
			                    {
			                        "type": "none"
			                    }
			                ],
			                "required": [
			                    {
			                        "type": "none"
			                    }
			                ]
			            }
			        }
		            ,
		            "main": false,
		            "rights": {
		                "hidden": false,
		                "add": true,
		                "del": true,
		                "required": true
		            }
		        }
		    },
		    "opinion": {
		        "bumenjingli": {
		            "description": "部门经理",
		            "read": [
		                {
		                    "type": "everyone"
		                }
		            ],
		            "write": [
		                {
		                    "type": "none"
		                }
		            ],
		            "required": [
		                {
		                    "type": "none"
		                }
		            ]
		        },
		        "caiwu": {
		            "description": "财务意见",
		            "read": [
		                {
		                    "type": "everyone"
		                }
		            ],
		            "write": [
		                {
		                    "type": "none"
		                }
		            ],
		            "required": [
		                {
		                    "type": "none"
		                }
		            ]
		        }
		    }
		}
	 * @param formDefKey 表单定义Key
	 * @param isInstance 是否实例表单
	 * @return
	 * @throws IOException 
	 */
	JsonNode getDefaultByFormDefKey(String formKey,boolean isInstance) throws IOException;
	/**
	 * 通过表单key获取表单权限
	 * @param formKey	key
	 * @param isInstance  是否只读权限
	 * @return
	 * @throws IOException 
	 */
	JsonNode getByFormKey(String formKey,boolean isReadOnly) throws IOException;
	
	/**
	 * 通过权限配置计算表单权限
	 * @param permissionConf
	 * @return
	 * @throws IOException 
	 */
	JsonNode calcFormPermission(JsonNode permissionConf) throws IOException;
	
	/**
	 * 
	 * 通过表单key删除表单权限
	 * @param formKey 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void removeByFormKey(String formKey);
	/**
	 *
	 * 获取流程启动时的流程权限
	 * 1. 获取开始节点
	 * 2. 获取第一个任务节点
	 * 3. 获取全局
	 * 4. 在form_rights中根据formKey获取
	 * 5. 获取表单元数据的授权
	 * @param formKey 表单key
	 * @param flowKey 流程定义KEY
	 * @param nodeId 节点ID
	 * @param nextNodeId 下一个节点ID
	 * @return 
	 * JsonNode
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	JsonNode getStartPermission(String formKey, String flowKey, String nodeId, String nextNodeId) throws IOException;

	
	/**
	 * 根据表单key获得权限列表
	 * @param flowKey表单key
	 * @return
	 */
	List<FormRight> getByFlowKey(String flowKey);

	void importFormRights(String formRightsXml);
	
	/**
	 * 
	 * TODO方法名称描述  获取表单授权页面中表的排序顺序
	 * @param formKey
	 * @return 
	 * List<Map<String,String>>
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	List<Map<String,String>> getTableOrderBySn(String formKey) throws IOException;
	
	/**
	 * 根据流程key进行删除。
	 * @param flowKey
	 * @param parentFlowKey
	 * @param permissionType
	 */
	void removeByFlowKey(@Param("flowKey")String flowKey, @Param("parentFlowKey")String parentFlowKey,@Param("permissionType")int permissionType);
	
	/**
	 * 获取表单权限设定。
	 * {
		    "table": {
		        "gerejianli": {
		            "description": "个人简历",
		            "fields":{
				            "name": {
				                "description": "姓名",
				                "read": [
				                    {
				                        "type": "everyone"
				                    }
				                ],
				                "write": [
				                    {
				                        "type": "user",
				                        "id":"1,2,..."
				                        "name":"a,b,..."
				                    }
				                ],
				                "required": [
				                    {
				                        "type": "script",
				                        "id":"xxxxxxxxx"
				                    }
				                ]
				            },
				            "age": {
				                "description": "年龄",
				                "read": [
				                    {
				                        "type": "everyone"
				                    }
				                ],
				                "write": [
				                    {
				                        "type": "none"
				                    }
				                ],
				                "required": [
				                    {
				                        "type": "none"
				                    }
				                ]
				            }
				    }
		            ,
		            "main": true
		        },
		        "education": {
		            "description": "教育经历",
		            fields":{
			            "school": {
			                "description": "毕业学校",
			                "read": [
			                    {
			                        "type": "everyone"
			                    }
			                ],
			                "write": [
			                    {
			                        "type": "none"
			                    }
			                ],
			                "required": [
			                    {
			                        "type": "none"
			                    }
			                ]
			            }
			        }
		            ,
		            "main": false,
		            "rights": {
		                "hidden": false,
		                "add": true,
		                "del": true,
		                "required": true
		            }
		        }
		    },
		    "opinion": {
		        "bumenjingli": {
		            "description": "部门经理",
		            "read": [
		                {
		                    "type": "everyone"
		                }
		            ],
		            "write": [
		                {
		                    "type": "none"
		                }
		            ],
		            "required": [
		                {
		                    "type": "none"
		                }
		            ]
		        },
		        "caiwu": {
		            "description": "财务意见",
		            "read": [
		                {
		                    "type": "everyone"
		                }
		            ],
		            "write": [
		                {
		                    "type": "none"
		                }
		            ],
		            "required": [
		                {
		                    "type": "none"
		                }
		            ]
		        }
		    }
		}
	 * @param design 表单设计页面数据
	 * @param isInstance
	 * @return
	 * 请使用formDefId 获取默认表单字段权限
	 */
	JsonNode getDefaultByDesign(String formDefId,String design,boolean isInstance) throws Exception;

    /**
     * 根据流程定义KEY、节点ID判断当前节点审批记录是否显示
     * @param flowKey
     * @param nodeId
     * @return
     */
    String getByTeam(String flowKey,String nodeId);
    
    /**
     * 通过表单KEY清空所有权限设置
     * @param flowKey
     */
    void emptyAll(String flowKey);
}
