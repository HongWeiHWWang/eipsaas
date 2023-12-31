package com.hotent.portal.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.model.PressRelease;
import com.hotent.portal.persistence.manager.PressReleaseManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：新闻公告 控制器类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-04-02 18:17:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/portal/pressRelease/v1")
@Api(tags="pressReleaseController")
public class PressReleaseController extends BaseController<PressReleaseManager, PressRelease>{
	@Resource
	PressReleaseManager pressReleaseManager;
	
	/**
	 * 新闻公告列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="新闻公告数据列表", httpMethod = "POST", notes = "获取新闻公告列表")
	public PageList<PressRelease> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<PressRelease> queryFilter) throws Exception{
		return pressReleaseManager.query(queryFilter);
	}
	@GetMapping("/getByType")
	@ApiOperation(value="根据类型获取数据", httpMethod = "GET", notes = "根据类型获取数据")
	public List<PressRelease> getByType(@ApiParam(name="queryFilter",value="查询对象")@RequestParam String FLbtssfl,@RequestParam String FFbfs) throws Exception{
		return pressReleaseManager.getByType(FLbtssfl,FFbfs);
	}
	
	@GetMapping("/getNews")
	@ApiOperation(value="查询告示板的所有数据", httpMethod = "GET", notes = "根据类型获取数据")
	public JsonNode getNews() throws Exception{
		List<Object> news = pressReleaseManager.getNews();
		JsonNode jsonNode = JsonUtil.toJsonNode(news);
		return jsonNode;
	}
	
	/**
	 * 新闻公告明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="新闻公告数据详情",httpMethod = "GET",notes = "新闻公告数据详情")
	public PressRelease get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return pressReleaseManager.get(id);
	}
	
    /**
	 * 新增新闻公告
	 * @param pressRelease
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新新闻公告数据", httpMethod = "POST", notes = "新增,更新新闻公告数据")
	public CommonResult<String> save(@ApiParam(name="pressRelease",value="新闻公告业务对象", required = true)@RequestBody PressRelease pressRelease) throws Exception{
		String msg = "添加新闻公告成功";
		if(StringUtil.isEmpty(pressRelease.getId())){
			pressReleaseManager.create(pressRelease);
		}else{
			pressReleaseManager.update(pressRelease);
			 msg = "更新新闻公告成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除新闻公告记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除新闻公告记录", httpMethod = "DELETE", notes = "批量删除新闻公告记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		pressReleaseManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
}
