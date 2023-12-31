package com.hotent.base.sqlbuilder;




/**
 * <pre>
 * 描述：TODO
 * 作者：lyj
 * 邮箱:liyj@jee-soft.cn
 * 日期:2014-7-16-下午6:07:26
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface ISqlBuilder {
	void setModel(SqlBuilderModel model);
	
	String getSql();

	String analyzeResultField();

	String analyzeConditionField();

	String analyzeSortField();

}
