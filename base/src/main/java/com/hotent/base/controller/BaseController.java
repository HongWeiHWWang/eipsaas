package com.hotent.base.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 基础的控制器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月6日
 */
public class BaseController <M extends BaseManager<T>, T extends Model<T>>{
	@Autowired
    protected M baseService;
	
	@PostMapping("/")
	@ApiOperation("添加实体的接口")
	public CommonResult<String> create(@ApiParam(name="model", value="实体信息") @RequestBody T t) {
		boolean result = baseService.save(t);
		if(!result) {
			return new CommonResult<>(ResponseErrorEnums.FAIL_OPTION, null);
		}
		return new CommonResult<>();
	}

	@PostMapping(value="/query", produces={"application/json; charset=utf-8" })
	@ApiOperation("分页查询结果")
	public PageList<T> query(@ApiParam(name="queryFilter", value="分页查询信息") @RequestBody QueryFilter<T> queryFilter) {
		return baseService.query(queryFilter);
	}

	@GetMapping("/{id}")
	@ApiOperation("根据id查询实体")
	public T getById(@ApiParam(name="id", value="实体id") @PathVariable String id) {
		return baseService.getById(id);
	}

	@PutMapping("/")
	@ApiOperation("更新实体")
	public CommonResult<String> updateById(@ApiParam(name="model", value="实体信息") @RequestBody T t) {
		boolean result = baseService.updateById(t);
		if(!result) {
			return new CommonResult<>(ResponseErrorEnums.FAIL_OPTION, "更新实体失败");
		}
		return new CommonResult<>();
	}

	@DeleteMapping("/{id}")
	@ApiOperation("根据id删除")
	public CommonResult<String> deleteById(@ApiParam(name="id", value="实体id") @PathVariable String id) {
		boolean result = baseService.removeById(id);
		if(!result) {
			return new CommonResult<>(ResponseErrorEnums.FAIL_OPTION, "删除实体失败");
		}
		return new CommonResult<>();
	}
	
	@DeleteMapping("/")
	@ApiOperation("根据id集合批量删除")
	public CommonResult<String> deleteByIds(@ApiParam(name="ids", value="实体集合") @RequestParam String...ids) {
		boolean result = baseService.removeByIds(Arrays.asList(ids));
		if(!result) {
			return new CommonResult<>(ResponseErrorEnums.FAIL_OPTION, "删除实体失败");
		}
		return new CommonResult<>();
	}
}
