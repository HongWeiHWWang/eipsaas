package com.hotent.form.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "querySqldefXmlList")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuerySqldefXmlList {
	@XmlElement(name = "querySqldefXml", type = QuerySqldefXml.class)
	List<QuerySqldefXml> querySqlDefXmlList = new ArrayList<QuerySqldefXml>();
	
	public List<QuerySqldefXml> getQuerySqlDefList() {
		return querySqlDefXmlList;
	}

	public void setQuerySqlDefList(List<QuerySqldefXml> querySqlDefList) {
		this.querySqlDefXmlList = querySqlDefList;
	}

	public void addQuerySqlDef(QuerySqldefXml sqldef){
		querySqlDefXmlList.add(sqldef);
	}
}
