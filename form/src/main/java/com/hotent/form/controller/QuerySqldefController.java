package com.hotent.form.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ZipUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.form.model.QueryMetafield;
import com.hotent.form.model.QuerySqldef;
import com.hotent.form.persistence.manager.QueryMetafieldManager;
import com.hotent.form.persistence.manager.QuerySqldefManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 * 
 * <pre>
 * 描述：自定义SQL设置 控制器类
 * 构建组：x5-bpmx-platform
 * 作者:Aschs
 * 邮箱:6322665042@qq.com
 * 日期:2016-06-13 16:46:33
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@Api(tags = "自定义SQL")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@RequestMapping("/form/query/querySqldef")
public class QuerySqldefController extends BaseController<QuerySqldefManager, QuerySqldef>{
	@Resource
	QuerySqldefManager querySqldefManager;
	@Resource
	QueryMetafieldManager queryMetafieldManager;
	@Resource
	DatabaseContext databaseContext;
	
	
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "自定义SQL定义列表(分页条件查询)数据", httpMethod = "POST", notes = "自定义SQL定义列表(分页条件查询)数据")	
	public @ResponseBody PageList<QuerySqldef> listJson(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception {
		return querySqldefManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "自定义SQL定义明细", httpMethod = "GET", notes = "自定义SQL定义明细")
	public QuerySqldef getJson(@ApiParam(name ="id", value = "id") @RequestParam Optional<String> id,
							 @ApiParam(name ="alias", value = "alias") @RequestParam Optional<String> alias) throws Exception {
		String id_ = id.orElse("");
		String alias_ = alias.orElse("");
		QuerySqldef querySqldef =null;
		if (StringUtil.isNotEmpty(id_)) {
			querySqldef = querySqldefManager.get(id_);
		}else if(StringUtil.isNotEmpty(alias_)){
			querySqldef=querySqldefManager.getByAlias(alias_);
		}
		if(querySqldef!=null){
			querySqldef.setMetafields(queryMetafieldManager.getBySqlId(querySqldef.getId()));
		}
		return querySqldef;
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存自定义SQL定义", httpMethod = "POST", notes = "保存自定义SQL定义")
	public CommonResult<String> save(@ApiParam(name="queryView",value="保存自定义SQL定义信息", required = true) @RequestBody QuerySqldef querySqldef) throws Exception{
		String id = querySqldef.getId();
		String resultMsg = "添加自定义SQL查询成功";
		if(!StringUtil.isEmpty(id)){
			resultMsg = "更新自定义SQL查询成功";
		}
		querySqldefManager.save(querySqldef);
		return new CommonResult<String>(true, resultMsg);
	}
	
	@RequestMapping(value="refreshFields",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "刷新字段", httpMethod = "GET", notes = "刷新字段")
	public List<QueryMetafield> refreshFields(@ApiParam(name="refreshFields",value="刷新字段", required = true) @RequestParam String id) throws Exception{
		return querySqldefManager.refreshFields(id);
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除自定义SQL定义记录", httpMethod = "DELETE", notes = "批量删除自定义SQL定义记录")
	public CommonResult<String> remove(@ApiParam(name="ids",value="自定义SQL定义ID!多个ID用,分割", required = true)@RequestParam String ids) throws Exception{
		String[] aryIds=null;
		if(!StringUtil.isEmpty(ids)){
			aryIds=ids.split(",");
		}
		querySqldefManager.removeByIds(aryIds);
		return new CommonResult<String>(true, "删除自定义SQL定义成功");
	}
	

	@RequestMapping(value = "checkSql", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "验证sql", httpMethod = "POST", notes = "验证sql")
	public CommonResult<String> checkSql(@ApiParam(name = "sql", value = "待验证的sql") @RequestBody ObjectNode obj ) throws Exception {
		CommonResult<String> result = null;
		try(DatabaseSwitchResult dResult = databaseContext.setDataSource(obj.get("dsName").asText())){
	    	JdbcTemplate jdbcTemplate = null;
			jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
			String sql = obj.get("sql").asText();
			if(SQLUtil.containsSqlInjection(sql)){
				result = new CommonResult<>(false,"sql语句含有非法注入！");
				return result;
			}
			jdbcTemplate.execute(sql);
			result = new CommonResult<>("验证通过");
		} catch (Exception e) {
			result = new CommonResult<>(false,"SQL验证失败:"+e.getMessage());
		}
		return result;
	}
	
	
	@RequestMapping(value="export",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "导出数据", httpMethod = "GET", notes = "导出数据")
	public void export(HttpServletResponse response,HttpServletRequest request,
			@ApiParam(name="ids",value="ids", required = true)@RequestParam String ids) throws Exception {
			if (BeanUtils.isEmpty(ids))
				return;
//			List<String> idList = Arrays.asList(ids);
			String[] idList = ids.split(",");
			String xml = querySqldefManager.export(idList); // 输出xml
			String fileName = "sqldef_"+DateFormatUtil.format(LocalDateTime.now(), "yyyy_MMdd_HHmm");
			HttpUtil.downLoadFile(request, response, xml, "sqldef.xml", fileName);
	}
	
	@RequestMapping(value="import", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "导入BO对象", httpMethod = "POST", notes = "导入BO对象")
	public CommonResult<String> importBo(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("file");
		String unZipFilePath = null;
		try {
		//	String rootRealPath = request.getSession().getServletContext().getRealPath(ROOT_PATH); // 操作的根目录
			String rootRealPath = (FileUtil.getIoTmpdir() +"/attachFiles/unZip/").replace("/", File.separator);// 操作的根目录
			FileUtil.createFolder(rootRealPath, true);
			String name = fileLoad.getOriginalFilename();
			String fileDir = StringUtil.substringBeforeLast(name, ".");
			
			ZipUtil.unZipFile(fileLoad, rootRealPath); // 解压文件
			unZipFilePath = rootRealPath + File.separator + fileDir; // 解压后文件的真正路径
			
			// 导入xml
			querySqldefManager.importDef(unZipFilePath);
			return new CommonResult<>(true, "导入成功");
		} catch (Exception e) {
			return new CommonResult<>(false, "导入失败："+e.getMessage());
		} finally {
			if(StringUtil.isNotEmpty(unZipFilePath)){
				File formDir = new File(unZipFilePath);
				if (formDir.exists()) {
					FileUtil.deleteDir(formDir); // 删除解压后的目录
				}
			}
		}
	}
}
