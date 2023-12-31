package com.hotent.system.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;

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
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.system.consts.DingTalkConsts;
import com.hotent.system.consts.WeChatOffAccConsts;
import com.hotent.system.consts.WeChatWorkConsts;
import com.hotent.system.enums.ExterUniEnum;
import com.hotent.system.model.SysExternalUnite;
import com.hotent.system.persistence.manager.SysExternalUniteManager;
import com.hotent.system.util.DingTalkTokenUtil;
import com.hotent.system.util.ExtUniUtil;
import com.hotent.system.util.WechatWorkTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/portal/sysExternalUnite/v1")
@Api(tags="第三方集成")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysExternalUniteController  extends BaseController {
    @Resource
    SysExternalUniteManager sysExternalUniteManager;

    @RequestMapping(value="listJson", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "第三方系统集成列表", httpMethod = "POST", notes = "第三方系统集成列表")
    public PageList<SysExternalUnite> list(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter){
        return sysExternalUniteManager.query(queryFilter);
    }


    @RequestMapping(value="getJson", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "获得集成对象", httpMethod = "GET", notes = "获得集成对象")
    public SysExternalUnite getJson(@ApiParam(name="id",value="主键")@RequestParam String id) throws Exception{
        if(StringUtil.isEmpty(id)){
            return new SysExternalUnite();
        }
        SysExternalUnite sysExternalUnite=sysExternalUniteManager.get(id);
        return sysExternalUnite;
    }

    @RequestMapping(value="generateMenuUrl", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "生成应用菜单url", httpMethod = "GET", notes = "生成应用菜单url")
    public CommonResult<String> generateMenuUrl(@ApiParam(name="id",value="主键")@RequestParam String id) throws Exception{
        SysExternalUnite sysExternalUnite=sysExternalUniteManager.get(id);
        String menuUrl = "-1";
        String type = sysExternalUnite.getType();
        if(ExterUniEnum.WeChatWork.getKey().equals(type)){
        	menuUrl= WeChatWorkConsts.generateMenuUrl(sysExternalUnite.getBaseUrl(),sysExternalUnite.getCorpId());
		}else if(ExterUniEnum.Dingtalk.getKey().equals(type)){
			menuUrl= DingTalkConsts.generateMenuUrl(sysExternalUnite.getBaseUrl(),sysExternalUnite.getCorpId());
		}else if(ExterUniEnum.WeChatOfficialAccounts.getKey().equals(type)){
			menuUrl= WeChatOffAccConsts.generateMenuUrl(sysExternalUnite.getBaseUrl(),sysExternalUnite.getCorpId());
		}
        return new CommonResult<>(true,"",menuUrl);
    }

    @PostMapping(value="save",  produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "保存系统第三方集成信息", httpMethod = "POST", notes = "保存系统第三方集成信息")
    public CommonResult<String> save(@ApiParam(name="sysExternalUnite",value="第三方集成信息")@RequestBody SysExternalUnite sysExternalUnite) throws Exception{
        CommonResult<String> resultMsg=null;
        String id=sysExternalUnite.getId();
        try {
            Boolean isTypeExists = sysExternalUniteManager.isTypeExists(sysExternalUnite.getType(),sysExternalUnite.getId());
            if(isTypeExists){
                for(ExterUniEnum en : ExterUniEnum.values()){
                    if(en.getKey().equals(sysExternalUnite.getType())){
                        return new CommonResult<String>(false,"【"+en.getLabel()+"】已集成,无需重复添加！");
                    }
                }
            }
            if(StringUtil.isEmpty(id)){
                sysExternalUnite.setId(UniqueIdUtil.getSuid());
                sysExternalUnite.setCorpName(ExterUniEnum.getLabelByKey(sysExternalUnite.getType()));
                sysExternalUniteManager.create(sysExternalUnite);
                resultMsg=new CommonResult<String>(true,"添加成功");
            }else{
                sysExternalUniteManager.update(sysExternalUnite);
                resultMsg=new CommonResult<String>(true,"更新成功");
            }
        } catch (Exception e) {
            resultMsg=new CommonResult<String>(false,"对第三方集成操作失败");
        }
        return  resultMsg;

    }


    @RequestMapping(value="removes",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "批量删除系统第三方集成记录", httpMethod = "DELETE", notes = "批量删除系统第三方集成记录")
    public CommonResult<String> batchRemove(@ApiParam(name="主键集合",value="主键集合", required = true) @RequestParam String...ids) throws Exception{
        try {
            sysExternalUniteManager.removeByIds(Arrays.asList(ids));
            return  new CommonResult("删除成功");
        } catch (Exception e) {
            return  new CommonResult(false,"删除失败");
        }
    }

    @RequestMapping(value="syncUser", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "将本系统的用户同步到第三方平台", httpMethod = "GET", notes = "将本系统的用户同步到第三方平台")
    public CommonResult syncUser(@ApiParam(name="id",value="主键")@RequestParam String id) throws Exception{
        try {
            sysExternalUniteManager.syncUser(id);
            return new CommonResult("上传通讯录成功");
        } catch (IOException e) {
            return new CommonResult(false,"上传通讯录失败"+e.getMessage());
        }
    }
    @RequestMapping(value="pullUser", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "从第三方系统拉取通讯录至本系统", httpMethod = "GET", notes = "从第三方系统拉取通讯录至本系统")
    public CommonResult pullUser(@ApiParam(name="id",value="主键")@RequestParam String id) throws Exception{
    	try {
    		sysExternalUniteManager.pullUser(id);
    		return new CommonResult("拉取通讯录成功");
    	} catch (IOException e) {
    		return new CommonResult(false,"拉取通讯录失败"+e.getMessage());
    	}
    }

    @RequestMapping(value="saveAgent", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "保存系统第三方集成信息", httpMethod = "POST", notes = "保存系统第三方集成信息")
    public CommonResult<String> saveAgent(@ApiParam(name="sysExternalUnite",value="第三方集成信息")@RequestBody SysExternalUnite sysExternalUnite) throws Exception{
        CommonResult<String> resultMsg=null;
        try {
            sysExternalUniteManager.saveAgent(sysExternalUnite);
            resultMsg=new CommonResult<>("操作成功");
        } catch (Exception e) {
            resultMsg=new CommonResult<>(false,"对第三方集成操作失败:"+e.getMessage());
        }
        return resultMsg;
    }


    @RequestMapping(value="getToken", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取getToken", httpMethod = "GET", notes = "获取getToken")
	public String getToken(String type) throws IOException {
		String token="";
        if(ExterUniEnum.WeChatWork.getKey().equals(type)){
            token=WechatWorkTokenUtil.getToken();
        }else if(ExterUniEnum.Dingtalk.getKey().equals(type)){
            token=DingTalkTokenUtil.getToken();
        }
        return token;
	}
    
    @RequestMapping(value="getUserInfoUrl", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "获取getUserInfoUrl", httpMethod = "GET", notes = "获取getUserInfoUrl")
    public String getUserInfoUrl(@ApiParam(name="type",value="第三方集成类型")@RequestParam String type,
                           @ApiParam(name="code",value="应用code")@RequestParam String code) throws Exception{
        String url="";
        if(ExterUniEnum.WeChatWork.getKey().equals(type)){
            url=WeChatWorkConsts.getQyWxUserInfo(code);
        }else if(ExterUniEnum.Dingtalk.getKey().equals(type)){
            url=DingTalkConsts.getUserInfo(code);
        }else if(ExterUniEnum.WeChatOfficialAccounts.getKey().equals(type)){
            url=WeChatOffAccConsts.getWxAccessToken(code);
        }
        return url;
    }

}
