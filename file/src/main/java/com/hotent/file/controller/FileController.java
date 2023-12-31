package com.hotent.file.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.*;
import com.hotent.base.util.Base64;
import com.hotent.base.util.time.DateUtil;
import com.hotent.file.extend.DetailTablePolicy;
import com.hotent.file.extend.InstanceFlowOpinions;
import com.hotent.file.util.AppFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.aop.SysLogsAspect;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.context.BaseContext;
import com.hotent.base.controller.BaseController;
import com.hotent.base.exception.NotFoundException;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.model.UploadResult;
import com.hotent.file.persistence.manager.CatalogManager;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/system/file/v1")
@Api(tags = "附件管理")
@ApiGroup(group = { ApiGroupConsts.GROUP_PORTAL })
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FileController extends BaseController<FileManager, DefaultFile> {
	private Logger logger = LoggerFactory.getLogger(FileController.class);
	@Resource
	FileManager fileManager;
	@Resource
	IUserService userService;

	@Resource
	CatalogManager catalogManager;

	@Resource
	BaseContext baseContext;

	@RequestMapping(value = "list", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "附件列表(分页条件查询)数据", httpMethod = "POST", notes = "附件列表(分页条件查询)数据")
	public PageList<DefaultFile> list(
			@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter) {
		List<QueryField> listQueryField = queryFilter.getQuerys();
		List<String> listId = new ArrayList<>();
		QueryField query = new QueryField();
		String xbTypeId = "";
		for (QueryField queryField : listQueryField) {
			if ("xbTypeId".equals(queryField.getProperty())) {
				xbTypeId = queryField.getValue() + "";
				query.setProperty(queryField.getProperty());
				query.setRelation(queryField.getRelation());
				listQueryField.remove(queryField);
				break;
			}
		}
		if (StringUtil.isNotEmpty(xbTypeId)) {
			List<String> ids = catalogManager.getDepartmentList(xbTypeId, listId);
			ids.add(xbTypeId);
			query.setValue(ids);
			query.setOperation(QueryOP.IN);
			listQueryField.add(query);
			queryFilter.setQuerys(listQueryField);
		}
		return fileManager.query(queryFilter);
	}

	@RequestMapping(value = "fileGet", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获得附件对象", httpMethod = "GET", notes = "获得附件对象")
	public DefaultFile edit(@ApiParam(name = "id", value = "主键") @RequestParam String id) {
		DefaultFile file = null;
		if (StringUtil.isNotEmpty(id)) {
			file = fileManager.get(id);
		}
		return file;
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除附件", httpMethod = "POST", notes = "批量删除附件")
	public CommonResult remove(@ApiParam(name = "ids", value = "附件ID!多个ID用,分割") @RequestBody String ids)
			throws Exception {
		String[] aryIds = null;
		if (StringUtil.isNotEmpty(ids)) {
			aryIds = ids.split(",");
		}
		fileManager.delSysFileByIds(aryIds);
		return new CommonResult(true, "删除附件成功", null);
	}

	@RequestMapping(value = "fileUpload", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "附件上传操作", httpMethod = "POST", notes = "附件上传操作")
	public UploadResult fileUpload(@ApiParam(name = "params", value = "格式限定") @RequestParam Map<String, Object> params,
			@ApiParam(name = "files", value = "上传的文件流") @RequestBody List<MultipartFile> files) throws Exception {
		DefaultFile file = new DefaultFile();
		if (params.containsKey("file")) {
			file = JsonUtil.toBean(params.getOrDefault("file", "{}").toString(), DefaultFile.class);
		}
		String account = baseContext.getCurrentUserAccout();
		return fileManager.uploadFile(file, files, params.getOrDefault("fileFormates", "").toString(),
				userService.getUserByAccount(account));
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "附件上传操作", httpMethod = "POST", notes = "附件上传操作")
	public UploadResult upload(MultipartHttpServletRequest request,
			@ApiParam(name = "fileFormates", value = "格式要求") @RequestParam Optional<String> fileFormates)
			throws Exception {
		IUser user = null;
		String account = baseContext.getCurrentUserAccout();
		if (StringUtil.isNotEmpty(account)) {
			user = userService.getUserByAccount(account);
		}
		Map<String, MultipartFile> fileMaps = request.getFileMap();
		Iterator<MultipartFile> it = fileMaps.values().iterator();
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		while (it.hasNext()) {
			files.add(it.next());
		}
		DefaultFile file = new DefaultFile();
		return fileManager.uploadFile(file, files, fileFormates.orElse(""), user);
	}

	@RequestMapping(value = "downloadFile", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "附件下载", httpMethod = "GET", notes = "附件下载")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "fileId", value = "附件ID") @RequestParam String fileId) throws Exception {
//      response.reset();
		response.setContentType("APPLICATION/OCTET-STREAM");
		DefaultFile file = null;
		try (MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()) {
			file = fileManager.get(fileId);
		}
		if (BeanUtils.isEmpty(file)) {
			throw new NotFoundException(String.format("未找到fileId为: %s 的文件", fileId));
		}
		String fileName = file.getFileName() + "." + file.getExtensionName();
		String filedisplay = URLEncoder.encode(fileName, "utf-8");
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
		response.addHeader("filename", filedisplay);
		response.setHeader("Access-Control-Allow-Origin", "*");
		String type = new MimetypesFileTypeMap().getContentType(new File(file.getFilePath()));
		response.setContentType(type);
		fileManager.downloadFile(fileId, response.getOutputStream());
	}

	@RequestMapping(value = "getFileType", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据附件id取得附件类型。", httpMethod = "POST", notes = "根据附件id取得附件类型。")
	public String getFileType(@ApiParam(name = "fileId", value = "附件id") @RequestBody String fileId)
			throws IOException {
		DefaultFile DefaultFile = null;
		String type = "doc";
		if (StringUtil.isNotEmpty(fileId)) {
			DefaultFile = fileManager.get(fileId);
			type = DefaultFile.getExtensionName().toLowerCase();
		}
		return type;
	}

	@RequestMapping(value = "setXbTypeId", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "修改附件分类。", httpMethod = "POST", notes = "修改附件分类。")
	public CommonResult setXbTypeId(@ApiParam(name = "fileId", value = "附件id") @RequestBody List<String> fileId,
			@ApiParam(name = "xbTypeId", value = "分类ID") @RequestParam String xbTypeId,
			@ApiParam(name = "type", value = "分类名称") @RequestParam String type) throws Exception {
		fileManager.setXbTypeId(fileId, xbTypeId, type);
		return new CommonResult(true, "设置附件分类成功", null);
	}

	@RequestMapping(value = "updateFileExtraProp", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "更新附件的属性成功", httpMethod = "POST", notes = "更新附件的属性成功（包含扩展属性、流程实例ID、节点名称、流程标题、附件来源、所属分类）")
	public CommonResult updateFileExtraProp(
			@ApiParam(name = "files", value = "附件列表") @RequestBody List<DefaultFile> files) throws Exception {
		fileManager.updateFileExtraProp(files);
		return new CommonResult(true, "更新附件的属性成功", null);
	}

	@RequestMapping(value = "preview", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "附件预览", httpMethod = "GET", notes = "附件预览")
	public void preview(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "fileId", value = "附件ID") @RequestParam String fileId) throws Exception {
//        response.reset();
		try (MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()) {
			response.setContentType("text/html; charset=UTF-8");
			response.setContentType("image/jpeg");

			DefaultFile file = null;
			file = fileManager.get(fileId);
			if (BeanUtils.isEmpty(file)) {
				return;
			}
			String fileName = file.getFileName() + "." + file.getExtensionName();
			String filedisplay = fileName;
			String agent = request.getHeader("USER-AGENT");
			if (agent != null && agent.indexOf("MSIE") == -1 && agent.indexOf("Trident") == -1) {
				filedisplay = "=?UTF-8?B?" + (new String(Base64.getBase64(filedisplay))) + "?=";
			} else {
				filedisplay = URLEncoder.encode(filedisplay, "utf-8");
			}
			response.addHeader("filename", filedisplay);
			response.setHeader("Access-Control-Allow-Origin", "*");
			fileManager.downloadFile(fileId, response.getOutputStream());
		}catch (Exception e) {
			logger.error("预览附件失败");
		}
	}
	@RequestMapping(value="wordPrint", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "word模板打印", httpMethod = "POST", notes = "word模板打印")
	public String wordPrint(@ApiParam(name="objectNode",value="Json对象")@RequestBody ObjectNode objectNode) throws Exception{
		String boData=objectNode.get("boData").toString();
		String fileId=objectNode.get("fileId").asText();
		String subject=objectNode.get("subject").asText();
		ArrayNode flowOpinions = (ArrayNode) objectNode.get("flowOpinions");
		DefaultFile defaultFile=fileManager.get(fileId);
		DefaultFile model=new DefaultFile();
		model.setId(UniqueIdUtil.getSuid());
		String outputFilePath= AppFileUtil.createFilePath(AppFileUtil.getAttachPath() + File.separator+"print"+File.separator + baseContext.getCurrentUserAccout(), model.getId()+"."+defaultFile.getExtensionName());
		this.printFile(boData,defaultFile,outputFilePath,flowOpinions);
		model.setFileName(subject);
		model.setStoreType(DefaultFile.SAVE_TYPE_FOLDER);
		model.setFilePath(outputFilePath.replace(AppFileUtil.getAttachPath() + File.separator, ""));
		model.setExtensionName(defaultFile.getExtensionName());
		model.setIsDel((short) 0);
		model.setCreateTime(DateUtil.getCurrentDate());
		fileManager.create(model);
		return  model.getId();
	}

	private void printFile(String boData, DefaultFile defaultFile, String outputFilePath,ArrayNode  flowOpinions) throws IOException {
		Map boMap= new HashMap();
		List<String> subKey=new ArrayList<>();
		JsonUtil.toMap(boData).values().forEach(item->{
			((Map) item).forEach((key,val)->{
				if(key.toString().startsWith("sub_")){
					subKey.add(key.toString());
				}
				boMap.put(key,val);
			});
		});
		Configure
				.ConfigureBuilder configureBuilder= Configure.newBuilder();
		for (int i = 0; i <subKey.size() ; i++) {
			configureBuilder=configureBuilder.customPolicy(subKey.get(i), new DetailTablePolicy());
		}
		configureBuilder=configureBuilder.customPolicy("flowOpinions", new InstanceFlowOpinions());
		boMap.put("flowOpinions",flowOpinions);
		Configure config=configureBuilder.build();
		String filePath =defaultFile.getFilePath();
		String fullPath = StringUtil.trimSufffix(AppFileUtil.getAttachPath(), File.separator) + File.separator
				+ filePath.replace("/", File.separator);
		XWPFTemplate template = XWPFTemplate.compile(fullPath, config).render(boMap);
		FileOutputStream out = new FileOutputStream(outputFilePath);
		template.write(out);
		out.flush();
		out.close();
		template.close();
	}

}
