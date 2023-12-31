package com.hotent.portal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.model.MessageNews;
import com.hotent.portal.persistence.manager.MessageNewsManager;

/**
 * 
 * <pre> 
 * 描述：新闻公告 控制器类
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-20 16:04:35
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/portal/messageNews/v1")
@Api(tags="新闻公告")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class MessageNewsController extends BaseController<MessageNewsManager, MessageNews>{
	@Resource
	MessageNewsManager messageNewsManager;
	
	/**
	 * 新闻公告列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="新闻公告数据列表", httpMethod = "POST", notes = "获取新闻公告列表")
	public PageList<MessageNews> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<MessageNews> queryFilter,@ApiParam(name="isPublic",value="是否只查询发布的新闻公告")@RequestParam Optional<Boolean> isPublic) throws Exception{
		boolean isp = isPublic.orElse(false);
		if(isp){
			queryFilter.addFilter("FStatus", "2", QueryOP.EQUAL, FieldRelation.AND, "2");
		}
		return messageNewsManager.query(queryFilter);
	}

    /**
     * 新闻公告列表(分页条件查询)数据 （vue发文公告）
     * @param request
     * @return
     * @throws Exception
     * PageJson
     * @exception
     */
    @PostMapping("/listNotice")
    @ApiOperation(value="新闻公告数据列表", httpMethod = "POST", notes = "获取新闻公告列表")
    public PageList<MessageNews> listNotice(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<MessageNews> queryFilter,@ApiParam(name="isPublic",value="是否只查询发布的新闻公告")@RequestParam Optional<Boolean> isPublic) throws Exception{
        boolean isp = isPublic.orElse(false);
        if(isp){
            queryFilter.addFilter("FStatus", "2", QueryOP.EQUAL, FieldRelation.AND, "2");
        }
        return messageNewsManager.query(queryFilter);
    }
	
	/**
	 * 新闻公告明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="新闻公告数据详情",httpMethod = "GET",notes = "新闻公告数据详情")
	public MessageNews get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return messageNewsManager.get(id);
	}

    /**
     * 新闻公告明细页面
     * @param id
     * @return
     */
    @GetMapping(value="/getById")
    @ApiOperation(value="新闻公告数据详情",httpMethod = "GET",notes = "新闻公告数据详情")
    public MessageNews getById(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id){
        return messageNewsManager.get(id);
    }
	
    /**
	 * 新增新闻公告
	 * @param messageNews
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新新闻公告数据", httpMethod = "POST", notes = "新增,更新新闻公告数据")
	public CommonResult<String> save(@ApiParam(name="messageNews",value="新闻公告业务对象", required = true)@RequestBody MessageNews messageNews) throws Exception{
		String msg = "添加新闻公告成功";
		if(StringUtil.isEmpty(messageNews.getId())){
			messageNewsManager.create(messageNews);
		}else{
			messageNewsManager.update(messageNews);
			 msg = "更新新闻公告成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除新闻公告记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除新闻公告记录", httpMethod = "DELETE", notes = "删除新闻公告记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		messageNewsManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除新闻公告记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除新闻公告记录", httpMethod = "DELETE", notes = "批量删除新闻公告记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String ids) throws Exception{
		messageNewsManager.removeByIds(Arrays.asList(ids.split(",")));
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	

	@RequestMapping(value="publicMsgNews",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "发布新闻公告记录", httpMethod = "POST", notes = "发布新闻公告记录")
	public CommonResult<String>  publicMsgNews(@ApiParam(name="idStrs",value="业务主键（列表）", required = true)@RequestBody String array) throws Exception{
		if(StringUtil.isNotEmpty(array)){
			String[] ids = array.split(",");
			for (String id : ids) {
				MessageNews msgNew = messageNewsManager.get(id);
				if(BeanUtils.isNotEmpty(msgNew)){
					msgNew.setFStatus("2");
					messageNewsManager.update(msgNew);
				}
			}
		}else{
			return new CommonResult<String>(false, "请传入需发布的新闻公告id");
		}
		return new CommonResult<String>(true, "发布成功");
	}
}
