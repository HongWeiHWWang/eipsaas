package com.hotent.form.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "querySqldefs")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuerySqldefXml {
	
	@XmlElement(name = "querySqldef", type = QuerySqldef.class)
	private QuerySqldef querySqldef;
	
	@XmlElementWrapper(name="metafields")
	@XmlElement(name="field",type=QueryMetafield.class)
	private List<QueryMetafield> metafieldList;
	
	@XmlElementWrapper(name="views")
	@XmlElement(name="view",type=QueryView.class)
	private List<QueryView> queryViewList;

	public QuerySqldef getQuerySqldef() {
		return querySqldef;
	}

	public void setQuerySqldef(QuerySqldef querySqldef) {
		this.querySqldef = querySqldef;
	}

	public List<QueryMetafield> getMetafieldList() {
		return metafieldList;
	}

	public void setMetafieldList(List<QueryMetafield> metafieldList) {
		this.metafieldList = metafieldList;
	}

	public List<QueryView> getQueryViewList() {
		return queryViewList;
	}

	public void setQueryViewList(List<QueryView> queryViewList) {
		this.queryViewList = queryViewList;
	} 
}
