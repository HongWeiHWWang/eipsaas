package com.hotent.portal.persistence.manager.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysIndexLayoutManage;
import com.hotent.portal.model.SysObjRights;
import com.hotent.portal.persistence.dao.SysIndexLayoutManageDao;
import com.hotent.portal.persistence.manager.AuthorityManager;
import com.hotent.portal.persistence.manager.SysIndexLayoutManageManager;
/**
 * 布局管理 Service类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@Service("sysIndexLayoutManageManager")
public class SysIndexLayoutManageManagerImpl extends BaseManagerImpl<SysIndexLayoutManageDao, SysIndexLayoutManage> implements SysIndexLayoutManageManager{
	@Resource
	private SysIndexColumnManagerImpl sysIndexColumnService;
	@Resource
	private AuthorityManager authorityManager;
	
	public SysIndexLayoutManageManagerImpl() {
	}

	@Override
	public SysIndexLayoutManage getLayoutList(String id,List<SysIndexColumn> columnList,Short type) {
		SysIndexLayoutManage sysIndexLayoutManage = null;
		if(StringUtil.isNotEmpty(id)){
			sysIndexLayoutManage = this.get(id);
		}
		if (BeanUtils.isEmpty(sysIndexLayoutManage))
			return getDefaultIndexLayout(type);
		String designHtml = "";
		try {
			designHtml = Base64.getFromBase64(sysIndexLayoutManage.getDesignHtml());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		sysIndexLayoutManage.setDesignHtml(sysIndexColumnService.parserDesignHtml(
				designHtml, columnList));
		return sysIndexLayoutManage;
	}
	


	public String getDefaultMobileDesignHtml() {
		return   "<div class=\"lyrow ui-draggable\" style=\"display: block;\">"
				+ "<div class=\"preview\"><input type=\"text\" value=\"一列(12)\" readonly=\"readonly\" class=\"form-control\"></div>"
				+ "<div class=\"view\">"
				+ "<div class=\"row clearfix\">"
				+ "<div class=\"col-md-12 column ui-sortable\"></div>"
				+ "</div>"
				+ "</div>"
				+ "</div>";
	}
	public String getDefaultDesignHtml() {
		return   "<div class=\"lyrow ui-draggable\" style=\"display: block;\">"
				+ "<a href=\"#close\" class=\"remove label label-danger\"><i class=\"glyphicon-remove glyphicon\"></i> 删除</a>"
				+ "<span class=\"drag label label-default\"><i class=\"glyphicon glyphglyphicon glyphicon-move\"></i> 拖动</span>"
				+ "<div class=\"preview\"><input type=\"text\" value=\"一列(12)\" readonly=\"readonly\" class=\"form-control\"></div>"
				+ "<div class=\"view\">"
				+ "<div class=\"row clearfix\">"
				+ "<div class=\"col-md-12 column ui-sortable\"></div>"
				+ "</div>"
				+ "</div>"
				+ "</div>";
	}
	
	private SysIndexLayoutManage getDefaultIndexLayout(Short type) {
		String designHtml  = getDefaultDesignHtml();
		if(type.equals(SysIndexLayoutManage.TYPE_MOBILE)){
			designHtml = getDefaultMobileDesignHtml();
		}
		SysIndexLayoutManage sysIndexLayoutManage  = new SysIndexLayoutManage();
		sysIndexLayoutManage.setDesignHtml(designHtml);
		sysIndexLayoutManage.setIsDef((short) 0);
		return sysIndexLayoutManage;
	}



	@Override
	public String getMyHasRightsLayout() {
		Map<String, Set<String>> relationMap= authorityManager.getUserRightMap();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("relationMap", relationMap);
		params.put("objType", SysObjRights.RIGHT_TYPE_INDEX_MANAGE);
		List<SysIndexLayoutManage> list = baseMapper.getByUserIdFilter(params);
		if(BeanUtils.isNotEmpty(list))
			return list.get(0).getTemplateHtml();
		return "";
	}

	@Override
	public String getManagerLayout() {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("orgIds", null);
		params.put("isDef", 1);
		List<SysIndexLayoutManage> list = baseMapper.getManageLayout(params);
		if(BeanUtils.isNotEmpty(list) )
			return list.get(0).getDesignHtml();
		return null;
	}

	@Override
	public String obtainIndexManageData(String layoutId) {
		SysIndexLayoutManage sysIndexLayoutManage = this.get(layoutId);
		if (BeanUtils.isNotEmpty(sysIndexLayoutManage))
			return sysIndexLayoutManage.getTemplateHtml();
		//2.找自己拥有权限的管理布局 ，按是否默认，排序
		String html = getMyHasRightsLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
		//3、找自己所属子组织没权限但设置默认布局；
//		html = getHasRightsLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
		//4、如果找不到找系统管理员的设置默认布局;
		html = getManagerLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
		//5、再找不到则使用系统默认布局。
		if (BeanUtils.isEmpty(html))
			html = getDefaultDesignHtml();
		return "";
	}

	@Override
	public SysIndexLayoutManage getEnableByOrgIdAndType(String orgId,Short layoutType) {
		Map<String, Object> params = new HashMap<>();
		params.put("orgId", orgId);
		params.put("layoutType", layoutType);
		return baseMapper.getEnableByOrgIdAndType(params);
	}

	@Override
	public Boolean isExistName(String name) {
		Integer count = baseMapper.isExistName(name);
		return count > 0;
	}

	@Override
	public List<SysIndexLayoutManage> getByOrgIdAndLayoutType(String orgId, Short layoutType) {
		Map<String, Object> params = new HashMap<>();
		params.put("orgId", orgId);
		params.put("layoutType", layoutType);
		return baseMapper.getByOrgIdAndLayoutType(params);
	}

	@Override
	public void cancelOrgIsDef(String orgId, Short layoutType) {
		Map<String, Object> params = new HashMap<>();
		params.put("orgId", orgId);
		params.put("layoutType", layoutType);
		baseMapper.cancelOrgIsDef(params);
	}

	@Override
	public String obtainIndexManageMobileData(String layoutId) {
		SysIndexLayoutManage sysIndexLayoutManage = this.get(layoutId);
		return sysIndexLayoutManage.getTemplateHtml();
	}

	@Override
	public SysIndexLayoutManage getByIdAndType(String id, Short type) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("type", type);
		return baseMapper.getByIdAndType(params);
	}

	@Override
	public String getMobileManagerLayout() {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("layoutType", SysIndexLayoutManage.TYPE_MOBILE);
		params.put("orgIds", null);
		params.put("isDef", 1);
		List<SysIndexLayoutManage> list = baseMapper.getManageLayout(params);
		if(BeanUtils.isNotEmpty(list) )
			return list.get(0).getDesignHtml();
		return null;
	}

	@Override
	public SysIndexLayoutManage getSharedByOrgIdAndType(String orgId,Short layoutType) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("layoutType", layoutType);
		return baseMapper.getSharedByOrgIdAndType(params);
	}

	@Override
	public SysIndexLayoutManage getSharedByOrgIds(List<String> orgIds, Short layoutType) {
		List<SysIndexLayoutManage> list = baseMapper.getSharedByOrgIds(orgIds, layoutType);
		if(BeanUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public void setEnable(String id, Short enable) {
		SysIndexLayoutManage layout = this.get(id);
		//如果是启用，必须把现有的其他停用
		if(enable ==1){
			List<SysIndexLayoutManage> list = this.getByOrgIdAndLayoutType(layout.getOrgId(), layout.getLayoutType());
			for(SysIndexLayoutManage item : list){
				if(item.getEnable()==1){
					item.setEnable((short)0);
					this.update(item);
				}
			}
		}
		layout.setEnable(enable);
		this.update(layout);
	}
}
