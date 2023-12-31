/**
 * 描述：TODO
 * 包名：com.hotent.runtime.persistence.model.query
 * 文件名：DefQueryFields.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-1-3-上午11:07:37
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.persistence.model.query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryOP;
import com.hotent.bpm.persistence.constants.Bool;
import com.hotent.bpm.persistence.constants.ProcDefTestStatus;

/**
 * <pre> 
 * 描述：简化流程定义查询字段的构造（对使用者屏蔽字段名称）
 * 构建组：x5-bpmx-core
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-1-3-上午11:07:37
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmDefQueryFields {
	private List<QueryField> queryFields = new ArrayList<QueryField>();

	/**
	 * 返回查询字段集合
	 * @return
	 */
	public List<QueryField> getQueryFields() {
		return queryFields;
	}
	public BpmDefQueryFields addName(String value){
		QueryField queryField = new QueryField("NAME_",value,QueryOP.LIKE);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addName(QueryOP op,String value){
		QueryField queryField = new QueryField("NAME_",value,op);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addDefKey(String value){
		QueryField queryField = new QueryField("DEF_KEY_",value,QueryOP.LIKE);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addDefKey(QueryOP op,String value){
		QueryField queryField = new QueryField("DEF_KEY_",value,op);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addTypeId(String typeId){
		QueryField queryField = new QueryField("TYPE_ID_",typeId,QueryOP.EQUAL);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addStatus(String status){
		QueryField queryField = new QueryField("STATUS_",status,QueryOP.EQUAL);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addStatus(List<String> statusList){		
		QueryField queryField = new QueryField("STATUS_",statusList,QueryOP.IN);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addTestStatus(ProcDefTestStatus testStatus){
		QueryField queryField = new QueryField("TEST_STATUS_",testStatus,QueryOP.EQUAL);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addBpmnDefId(String bpmnDefId){
		QueryField queryField = new QueryField("BPMN_DEF_ID_",bpmnDefId);
		queryFields.add(queryField);
		return this;		
	}
	public BpmDefQueryFields addBpmnDeployId(String bpmnDeployId){
		QueryField queryField = new QueryField("BPMN_DEPLOY_ID_",bpmnDeployId);
		queryFields.add(queryField);
		return this;		
	}	
	public BpmDefQueryFields addIsMain(Bool trueOrFalse){
		QueryField queryField = new QueryField("IS_MAIN_",trueOrFalse.value());
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addCreateBy(String userId){
		QueryField queryField = new QueryField("CREATE_BY_",userId);
		queryFields.add(queryField);
		return this;
	}
	public BpmDefQueryFields addCreateTimeRange(LocalDateTime startTime,LocalDateTime endTime){
		if(startTime!=null && endTime!=null){
			List<LocalDateTime> dateList = new ArrayList<LocalDateTime>();
			dateList.add(startTime);
			dateList.add(endTime);
			QueryField queryField = new QueryField("CREATE_TIME_",dateList,QueryOP.BETWEEN);
			queryFields.add(queryField);
		}else if(startTime!=null){
			QueryField queryField = new QueryField("CREATE_TIME_",startTime,QueryOP.GREAT_EQUAL);
			queryFields.add(queryField);			
		}else if(endTime!=null){
			QueryField queryField = new QueryField("CREATE_TIME_",endTime,QueryOP.LESS_EQUAL);
			queryFields.add(queryField);
		}
		return this;
	}
}
