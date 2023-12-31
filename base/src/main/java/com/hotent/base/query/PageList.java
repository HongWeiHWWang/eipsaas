package com.hotent.base.query;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 返回的分页列表数据
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月2日
 */
@ApiModel(description="任务查询参数对象")
public class PageList<E>{
	@ApiModelProperty(name="rows",notes="结果列表")
	List<E> rows;
	@ApiModelProperty(name="total",notes="总记录数")
	long total;
	@ApiModelProperty(name="page",notes="当前页码")
	long page;
	@ApiModelProperty(name="pageSize",notes="每页条数")
	long pageSize;
	
	public PageList(){
	}
	
	@SuppressWarnings("unchecked")
	public PageList(List<E> c){
		if(c instanceof IPage){
			IPage<E> page = (IPage<E>)c;
			this.setRows(page.getRecords());
			this.total = page.getTotal();
			this.page = page.getCurrent();
			this.pageSize = page.getSize();
		}
		else{
			this.setRows(c);
			this.total = this.rows.size();
		}
	}
	
	public PageList(IPage<E> page){
		this.setRows(page.getRecords());
		this.total = page.getTotal();
		this.page = page.getCurrent();
		this.pageSize = page.getSize();
	}
	
	public List<E> getRows() {
		return rows;
	}
	public void setRows(List<E> rows) {
		this.rows = rows;
		if(rows.size() == 0) {
			// 解决rows类型为EmptyList时，对rows进行add操作会报错的问题
			this.rows = new ArrayList<>();
		}
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getPage() {
		return page;
	}
	public void setPage(long page) {
		this.page = page;
	}
	public long getPageSize() {
		return pageSize;
	}
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
}
