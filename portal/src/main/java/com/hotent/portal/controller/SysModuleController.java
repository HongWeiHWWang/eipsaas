package com.hotent.portal.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldSort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.ZipUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.sys.persistence.manager.SysModuleDetailManager;
import com.hotent.sys.persistence.manager.SysModuleManager;
import com.hotent.sys.persistence.model.SysModule;
import com.hotent.sys.persistence.model.SysModuleDetail;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping("/sys/sysModule/v1")
@Api(tags = "系统模块")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysModuleController extends BaseController<SysModuleManager, SysModule> {
    @Resource
    SysModuleManager sysModuleManager;
    @Resource
    SysModuleDetailManager sysModuleDetailManager;

    @Resource
    UCFeignService ucFeignService;

    @RequestMapping(value = "listJson", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "模块列表(分页条件查询)数据", httpMethod = "POST", notes = "模块列表(分页条件查询)数据")
    public @ResponseBody
    PageList<SysModule> listJson(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter<SysModule> queryFilter) {
        queryFilter.getSorter().add(new FieldSort("updateTime", Direction.DESC));
        return sysModuleManager.query(queryFilter);
    }


    @RequestMapping(value = "getJson", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "模块明细页面", httpMethod = "GET", notes = "模块明细页面")
    public SysModule getJson(@ApiParam(name = "id", value = "主键", required = false) @RequestParam String id) {
        if (StringUtil.isEmpty(id) ) {
            return new SysModule();
        }
        SysModule sysModule =  sysModuleManager.get(id);
        if (BeanUtils.isEmpty(sysModule)) {
            return new SysModule();
        }
        List<SysModuleDetail> details = sysModuleDetailManager.getModuleDetail(sysModule.getId(), null);
        sysModule.setModuleDetail(details);
        return sysModule;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "保存模块信息", httpMethod = "POST", notes = "保存模块信息")
    public CommonResult<String> save(@ApiParam(name = "sysModule", value = "模块信息对象") @RequestBody SysModule sysModule) {
        CommonResult<String> resultMsg = null;
        String id = sysModule.getId();
        boolean isDeploy = sysModule.isDeploy();
        try {
            if (StringUtil.isEmpty(id)) {
                SysModule oldSysModule = sysModuleManager.getModuleByCode(sysModule.getCode());
                if (BeanUtils.isNotEmpty(oldSysModule)) {
                    throw new RuntimeException("编码：" + sysModule.getCode() + "已存在，请输入其他模块编码！");
                } else {
                    sysModule.setId(UniqueIdUtil.getSuid());
                    IGroup iGroup = ContextUtil.getCurrentGroup();
                    if (BeanUtils.isNotEmpty(iGroup)) {
                        sysModule.setCreateOrgId(iGroup.getGroupId());
                    }
                    sysModule.setUpdateTime(LocalDateTime.now());
                    sysModuleManager.create(sysModule);
                    resultMsg = new CommonResult<String>(isDeploy ? "模块添加、发布成功" : "添加模块成功");
                }
            } else {
                sysModuleManager.update(sysModule);
                resultMsg = new CommonResult<String>(isDeploy ? "模块更新、发布成功" : "更新模块成功");
            }
        } catch (Exception e) {
            resultMsg = new CommonResult<String>(false, "对模块操作失败" + e.getMessage());
        }
        return resultMsg;
    }

    @RequestMapping(value = "saveModule", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "保存模块信息", httpMethod = "POST", notes = "保存模块信息")
    public CommonResult<String> saveModule(@ApiParam(name = "sysModule", value = "模块信息对象") @RequestBody SysModule sysModule) {
        CommonResult<String> resultMsg = null;
        String id = sysModule.getId();
        sysModuleManager.saveModule(sysModule);
        if (StringUtil.isEmpty(id)) {
            resultMsg = new CommonResult<String>(true,"添加模块信息成功",sysModule.getId());
        } else {
            resultMsg = new CommonResult<String>(true,"更新模块信息成功",sysModule.getId());
        }

        return resultMsg;
    }

    @RequestMapping(value = "remove", method = RequestMethod.DELETE, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "批量删除模块记录", httpMethod = "DELETE", notes = "批量删除模块记录")
    public CommonResult<String> remove(@ApiParam(name = "ids", value = "主键集合", required = true) @RequestParam(required = false) String... ids) {
        CommonResult<String> message = null;
        try {

            sysModuleManager.removeByIds(ids);
            message = new CommonResult<String>("删除模块成功");
        } catch (Exception e) {
            message = new CommonResult<String>(false, "删除模块失败");
        }
        return message;
    }

    @RequestMapping(value = "isExist", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "判断模块编号是否存在", httpMethod = "GET", notes = "判断模块编号是否存在")
    public boolean isExist(@ApiParam(name = "code", value = "模块编码", required = false) @RequestParam String code) throws Exception {
        boolean res=false;
        if (StringUtil.isNotEmpty(code)) {
            SysModule temp = sysModuleManager.getModuleByCode(code);
            res =BeanUtils.isNotEmpty(temp);
        }
        return res;
    }


    @RequestMapping(value = "exportModule", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "导出格式为*.zip的模块对象，zip文件包含一个xml文件，xml文件都是多个模块对象;", httpMethod = "GET", notes = "导出格式为*.zip的模块对象，zip文件包含一个xml文件，xml文件都是多个模块对象;")
    public void exportModule(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "ids", value = "主键集合") @RequestParam String... ids) throws Exception {
        if (BeanUtils.isEmpty(ids)) return;
        List<String> idList = Arrays.asList(ids);
        Map map = sysModuleManager.exportModules(idList); // 输出xml
        String fileName = "ht_module_" + DateFormatUtil.format(LocalDateTime.now(), "yyyy_MMdd_HHmm");
        HttpUtil.downLoadFile(request, response, map, fileName);

    }


    private final static String ROOT_PATH = "attachFiles" + File.separator + "tempZip"; // 导入和导出的文件操作根目录

    @RequestMapping(value = "importModule", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "导入模块对象", httpMethod = "POST", notes = "导入模块对象")
    public CommonResult<String> importModule(@ApiParam(name="file",value="导入的zip文件",required=true) @RequestBody MultipartFile file) throws Exception {
        CommonResult<String> message = null;
        String unZipFilePath = null;
        try {
            String rootRealPath = this.getClass().getClassLoader().getResource(File.separator).getPath() + ROOT_PATH;// 操作的根目录
            String name = file.getOriginalFilename();
            String fileDir = StringUtil.substringBeforeLast(name, ".");

            ZipUtil.unZipFile(file, rootRealPath); // 解压文件
            unZipFilePath = rootRealPath + File.separator + fileDir; // 解压后文件的真正路径
            // 导入xml
            sysModuleManager.importModules(unZipFilePath);
            message = new CommonResult<String>(true, "导入成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            message = new CommonResult<String>(false, "导入失败! " + e.getMessage());
        } finally {
            try {
                File formDir = new File(unZipFilePath);
                if (formDir.exists()) {
                    FileUtil.deleteDir(formDir); // 删除解压后的目录
                }
            } catch (Exception e2) {
                message = new CommonResult<String>(false, "请传入zip文件! ");
            }
        }
        return message;
    }


}
