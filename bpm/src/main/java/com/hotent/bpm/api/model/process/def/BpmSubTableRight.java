package com.hotent.bpm.api.model.process.def;

import java.io.Serializable;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;

/**
 * 子表数据权限定义。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014年8月21日-下午5:46:31
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmSubTableRight implements Serializable {
	
	private static final long serialVersionUID = 3124663466667563610L;

	/**
	 * 节点ID。
	 */
	private String nodeId="";

	/**
	 * 父流程定义KEY
	 */
	private String parentDefKey="";
	
	/**
	 * 权限类别（curUser，当前人，curOrg，当前组织,script 脚本)
	 */
	private String rightType="script";
	
	/**
	 * ent名
	 */
	private String tableName="";

	/**
	 * 脚本
	 */
	private String script="";
	

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParentDefKey() {
		if(StringUtil.isEmpty(parentDefKey)){
			return BpmConstants.LOCAL;
		}
		return parentDefKey;
	}

	public void setParentDefKey(String parentDefKey) {
		this.parentDefKey = parentDefKey;
	}

	public String getRightType() {
		return rightType;
	}

	public void setRightType(String rightType) {
		this.rightType = rightType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
