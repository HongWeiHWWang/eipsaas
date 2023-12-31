package com.hotent.portal.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.persistence.manager.SysLogsManager;
import com.hotent.sys.persistence.model.SysLogs;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：系统操作日志 控制器类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-08-31 10:59:25
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/sys/sysLogs/v1")
@Api(tags="系统日志")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysLogsController extends BaseController<SysLogsManager, SysLogs>{
	@Resource
	SysLogsManager sysLogsManager;
	
	/**
	 * 系统操作日志列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="系统操作日志数据列表", httpMethod = "POST", notes = "获取系统操作日志列表")
	public PageList<SysLogs> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<SysLogs> queryFilter) throws Exception{
		return sysLogsManager.query(queryFilter);
	}
	
	/**
	 * 系统操作日志明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	public SysLogs get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return sysLogsManager.get(id);
	}
	
    /**
	 * 新增系统操作日志
	 * @param sysLogs
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	public CommonResult<String> save(@ApiParam(name="sysLogs",value="系统操作日志业务对象", required = true)@RequestBody SysLogs sysLogs) throws Exception{
		String msg = "添加系统操作日志成功";
		if(StringUtil.isEmpty(sysLogs.getId())){
			sysLogsManager.create(sysLogs);
		}else{
			sysLogsManager.update(sysLogs);
			 msg = "更新系统操作日志成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除系统操作日志记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		sysLogsManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除系统操作日志记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		sysLogsManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
	
}
