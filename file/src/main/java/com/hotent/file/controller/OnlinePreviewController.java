package com.hotent.file.controller;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.attachment.AttachmentService;
import com.hotent.base.attachment.AttachmentServiceFactory;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.template.impl.FreeMarkerEngine;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.file.service.FilePreview;
import com.hotent.file.service.FilePreviewFactory;
import com.hotent.file.util.AppFileUtil;
import com.hotent.file.util.FileUtils;
import com.hotent.file.util.OfficeToPdf;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 附件在线预览
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月14日
 */
@RestController
@RequestMapping("/file/onlinePreviewController/v1")
@Api(tags="附件在线预览")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
@SuppressWarnings({ "rawtypes" })
public class OnlinePreviewController {
	@Resource
	FilePreviewFactory previewFactory;
	@Resource
	FileManager fileManager;
	@Resource
	FileUtils fileUtils;
	@Value("${file.file.dir}")
	String fileDir;

	@RequestMapping(value="onlinePreview", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "附件在线预览", httpMethod = "GET", notes = "附件在线预览")
	public JsonNode onlinePreview(HttpServletRequest request, HttpServletResponse response,@ApiParam(name="fileId",value="附件ID")@RequestParam String fileId) throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		DefaultFile fileMode = fileManager.get(fileId);
		if(fileMode!=null){
			AttachmentServiceFactory attachmentHandlerFactory = AppUtil.getBean(AttachmentServiceFactory.class);
			AttachmentService attachmentService = attachmentHandlerFactory.getCurrentServices(AppFileUtil.getSaveType());
			boolean ref=attachmentService.chekckFile(fileMode);
			if(ref){
				FilePreview filePreview = previewFactory.get(fileMode);
				String Result=filePreview.filePreviewHandle(fileMode, map);
				map.put("result",Result);
				map.remove("project");
			}else{
				map.put("result","error");
			}
		}
		JsonNode object=JsonUtil.toJsonNode(map);
		return  object;
	}



	@RequestMapping(value="getFileByPathAndId_{fileId}_{ext}", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据ID和类型找到处理后的附件", httpMethod = "GET", notes = "根据ID和类型找到附件")
	public void getFileByPathAndId(HttpServletResponse response, @PathVariable(name="fileId") String fileId, @PathVariable(name="ext") String ext) throws IOException{
		String fullPath=fileDir+fileId+"."+ext;
        String type = "text/html;charset="+getCharset(fullPath);
        if("pdf".equals(ext)){
            type="application/pdf";;
        }
		response.setContentType(type);
		byte[] bytes = FileUtil.readByte(fullPath);
		if(bytes!=null&&bytes.length>0){
			response.getOutputStream().write(bytes);
		}

	}




	@RequestMapping(value="getFileById_{fileId}", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据文件ID找到上传过的文件", httpMethod = "GET", notes = "根据文件ID找到上传过的文件")
	public void getFileById(HttpServletRequest request, HttpServletResponse response, @PathVariable(name="fileId") String fileId) throws Exception {
		fileManager.downloadFile(fileId, response.getOutputStream());
	}




	//根据文件路径获取文件编码格式
    public static String getCharset(String pathName) {
        File file = new File(pathName);
        if (!file.exists()) {
            return "";
        }
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }

            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
            }
        }

        return charset;
    }
}
