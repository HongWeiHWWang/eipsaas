package com.hotent.form.vo;

import com.hotent.base.query.QueryFilter;

/**
 * @author qiuxd
 * @company 广州宏天软件股份有限公司
 * @email qiuxd@jee-soft.cn
 * @date 2020/05/19
 */
public class ExportSubVo {
    private String alias;
    private String refId;
    private String type;
    private String filterKey;
    private String expField;
    private QueryFilter queryFilter;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public String getExpField() {
        return expField;
    }

    public void setExpField(String expField) {
        this.expField = expField;
    }

    public QueryFilter getQueryFilter() {
        return queryFilter;
    }

    public void setQueryFilter(QueryFilter queryFilter) {
        this.queryFilter = queryFilter;
    }
}
