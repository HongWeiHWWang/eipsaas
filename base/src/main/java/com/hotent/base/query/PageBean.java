package com.hotent.base.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分页对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月4日
 */
@ApiModel(description="分页对象")
public class PageBean extends RowBounds implements Serializable{
	private static final long serialVersionUID = -3758630357085712072L;
	/**
	 * 默认每页显示的记录数
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;
	/**
	 * 默认显示第一页
	 */
	public final static int NO_PAGE = 1;

	@ApiModelProperty(name="page",notes="页号", example="1")
	protected int page = NO_PAGE;
	@ApiModelProperty(name="pageSize",notes="分页大小", example="10")
	protected int pageSize = DEFAULT_PAGE_SIZE;
	@ApiModelProperty(name="showTotal",notes="显示总数", example="true")
	protected boolean showTotal = true;

	public PageBean(){}

	public PageBean(Integer page){
		this.page = page;
	}

	public PageBean(Boolean showTotal){
		this.showTotal = showTotal;
	}

	public PageBean(Integer page, Integer pageSize){
		this.page = page;
		this.pageSize = pageSize;
	}

	public PageBean(Integer page, Integer pageSize, Boolean showTotal){
		this.page = page;
		this.pageSize = pageSize;
		this.showTotal = showTotal;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public boolean showTotal() {
		return showTotal;
	}

	public void setShowTotal(boolean showTotal) {
		this.showTotal = showTotal;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@JsonIgnore
	@Override
	public int getLimit() {
		return this.pageSize;
	}

	/**
	 * 获取起始行号
	 * @return 起始行号
	 */
	@JsonIgnore
	@Override
	public int getOffset() {
		return this.page > 0 ? (this.page - 1) * this.pageSize : 0;
	}
}
