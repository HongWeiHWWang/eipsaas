package com.hotent.file.controller;

import ch.qos.logback.core.util.ContextUtil;
import com.hotent.base.context.BaseContext;
import com.hotent.base.query.QueryOP;
import com.hotent.sys.persistence.model.SysType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.activemq.model.JmsSysTypeChangeMessage;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.jms.JmsProducer;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.file.model.Catalog;
import com.hotent.file.persistence.manager.CatalogManager;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 *
 * <pre>
 * 描述：附件目录控制器类
 * 构建组：x5-bpmx-platform
 * 作者:maoww
 * 邮箱:maoww@jee-soft.cn
 * 日期:2018-05-15 11:45:41
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping("/file/catalog/v1")
@Api(tags="附件目录管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
@SuppressWarnings({"rawtypes"})
public class CatalogController extends BaseController<CatalogManager,Catalog>{
	@Resource
	CatalogManager catalogManager;

	@Resource
	IUserService userService;
	@Resource
	JmsProducer jmsProducer;

	@Resource
	BaseContext baseContext;

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "目录列表(分页条件查询)数据", httpMethod = "POST", notes = "目录列表(分页条件查询)数据")
	public PageList<Catalog> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return catalogManager.query(queryFilter);
	}

	@RequestMapping(value="getJson", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "附件目录详情", httpMethod = "GET", notes = "附件目录详情")
	public  Catalog getJson(@ApiParam(name="id",value="主键")@RequestParam String id) throws Exception{
		Catalog catalog = new Catalog();
		if(!StringUtil.isEmpty(id)){
			catalog=catalogManager.get(id);
		}
		return catalog;
	}

	@RequestMapping(value="save", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存附件目录信息", httpMethod = "POST", notes = "保存目录信息")
	public CommonResult save(@ApiParam(name="catalog",value="附件目录对象")@RequestBody Catalog catalog) throws Exception{
			String id=catalog.getId();
			String resultMsg;
		try {
			//判断附件名称是否重复
			List<Catalog> catalogList = catalogManager.getListByParentId(catalog.getParentId(), catalog.getName());
			if(BeanUtils.isNotEmpty(catalogList)) {
				return new CommonResult(false, "名称已存在，请重新输入");
			}
			if(StringUtil.isEmpty(id)){
				catalog.setId(UniqueIdUtil.getSuid());
				catalogManager.create(catalog);
				resultMsg="添加附件目录成功";
			}else{
				Catalog oldType = catalogManager.get(catalog.getId());
				catalogManager.update(catalog);
				jmsProducer.sendToTopic(new JmsSysTypeChangeMessage("FILE_TYPE","",catalog.getName(),oldType.getName(),1));
				resultMsg="更新附件目录成功";
			}

			return new CommonResult(resultMsg);
		} catch (Exception e) {
			resultMsg="对附件目录操作失败";
			return new CommonResult(false,resultMsg);
		}
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "删除目录分类", httpMethod = "DELETE", notes = "删除目录分类")
	public CommonResult remove(@ApiParam(name="id",value="主键")@RequestParam	String id) throws Exception{
		try {
			List listId=new ArrayList();
			List<String> childrenIds = catalogManager.getDepartmentList(id, listId);
			childrenIds.add(id);
			String[] ids=new String[childrenIds.size()];
			for (int i = 0; i < childrenIds.size(); i++) {
				ids[i]=childrenIds.get(i);
			}
			QueryFilter filter = QueryFilter.build();
			filter.addFilter("ID", ids, QueryOP.IN);
			PageList<Catalog> query = catalogManager.query(filter);
			if (BeanUtils.isNotEmpty(query) && BeanUtils.isNotEmpty(query.getRows())) {
				for (Catalog type : query.getRows()) {
					jmsProducer.sendToTopic(new JmsSysTypeChangeMessage("FILE_TYPE","",type.getName(),type.getName(),2));
				}
			}
			catalogManager.removeByIds( Arrays.asList(ids));
			return new CommonResult("删除附件目录成功");
		} catch (Exception e) {
			return new CommonResult(false,"删除附件目录失败");
		}
	}

	@RequestMapping(value="getTree", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "得到树形附件分类目录树", httpMethod = "GET", notes = "得到树形附件分类目录树")
	public List<Catalog> getTree() throws Exception {
		IUser user=null;
		String accout=baseContext.getCurrentUserAccout();
		if(StringUtil.isNotEmpty(accout)){
			user=userService.getUserByAccount(accout);
		}
		List<Catalog> listCatalog=new ArrayList<Catalog>();
		if(BeanUtils.isNotEmpty(user) && user.isAdmin()){
			listCatalog=catalogManager.list();
		}else if(BeanUtils.isNotEmpty(user)){
			listCatalog=catalogManager.getCatalogByCreateBy(user.getUserId());
		}
		Catalog cl = new Catalog();
		cl.setName("附件分类");
		cl.setId("-1");
		listCatalog.add(cl);
		List listSysType=new ArrayList();
		for(Catalog entity : listCatalog){
			Catalog sysTypes = new Catalog();
			sysTypes = entity;
			sysTypes.setOpen(true);
			QueryFilter queryFilter=QueryFilter.build();
			queryFilter.addFilter("parentId", entity.getId(),QueryOP.EQUAL);
			List<Catalog> sys_Type = catalogManager.query(queryFilter).getRows();
			if(sys_Type!=null && sys_Type.size()==0){
				sysTypes.setIsParent("true");
			}
			listSysType.add(entity);
		}
		List<Catalog> rtnList=BeanUtils.listToTree(listSysType);
		return rtnList;
	}
}
