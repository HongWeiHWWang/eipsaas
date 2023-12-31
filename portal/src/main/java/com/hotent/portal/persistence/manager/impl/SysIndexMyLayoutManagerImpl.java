package com.hotent.portal.persistence.manager.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysIndexMyLayout;
import com.hotent.portal.persistence.dao.SysIndexMyLayoutDao;
import com.hotent.portal.persistence.manager.SysIndexColumnManager;
import com.hotent.portal.persistence.manager.SysIndexLayoutManageManager;
import com.hotent.portal.persistence.manager.SysIndexMyLayoutManager;
import com.hotent.portal.util.PortalUtil;
import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * portal_sys_layout 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@Service("sysIndexMyLayoutManager")
public class SysIndexMyLayoutManagerImpl extends BaseManagerImpl<SysIndexMyLayoutDao, SysIndexMyLayout> implements SysIndexMyLayoutManager{

	@Resource
	SysIndexLayoutManageManager sysIndexLayoutManageManager;
	@Resource
	SysIndexColumnManager sysIndexColumnManager;

	/**
	 * @return
	 */
	private String defaultIndexLayout() {
		String templateHtml=FileUtil.readFile(PortalUtil.getIndexTemplatePath()+"templates"+File.separator+"defaultIndexPages.ftl");
		return templateHtml;
	}

	@Override
	public SysIndexMyLayout getLayoutList(String userId,List<SysIndexColumn> columnList) {
		SysIndexMyLayout sysIndexMyLayout = baseMapper.getByUserId(userId);
		if (BeanUtils.isEmpty(sysIndexMyLayout))
			return getDefaultIndexLayout();
		String designHtml = "";
		try {
			designHtml = Base64.getFromBase64(sysIndexMyLayout.getDesignHtml());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sysIndexMyLayout.setDesignHtml(sysIndexColumnManager.parserDesignHtml(designHtml, columnList));
		return sysIndexMyLayout;
	}


	private SysIndexMyLayout getDefaultIndexLayout() {
		SysIndexMyLayout sysIndexMyLayout = new SysIndexMyLayout();
		sysIndexMyLayout.setDesignHtml(sysIndexLayoutManageManager.getDefaultDesignHtml());
		return sysIndexMyLayout;
	}
	@Override
	public void save(String html, String designHtml, String userId) {
		SysIndexMyLayout sysIndexMyLayout =baseMapper.getByUserId(userId);
		if(BeanUtils.isEmpty(sysIndexMyLayout)){
			sysIndexMyLayout = new SysIndexMyLayout();
			sysIndexMyLayout.setDesignHtml(designHtml);
			sysIndexMyLayout.setTemplateHtml(html);
			sysIndexMyLayout.setId(UniqueIdUtil.getSuid());
			sysIndexMyLayout.setUserId(userId);
			this.create(sysIndexMyLayout);
			
		}else{
			sysIndexMyLayout.setDesignHtml(designHtml);
			sysIndexMyLayout.setTemplateHtml(html);
			this.update(sysIndexMyLayout);
			
	    }
	}

	@Override
	public String obtainMyIndexData(String userId) {
		// 1、首先先找到自己定义的布局；
		SysIndexMyLayout sysIndexMyLayout = baseMapper.getByUserId(userId);
		if (BeanUtils.isNotEmpty(sysIndexMyLayout))
			return sysIndexMyLayout.getTemplateHtml();
		//2.找自己拥有权限的管理布局 ，按是否默认，排序
		String html = sysIndexLayoutManageManager.getMyHasRightsLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
		//3、找自己所属子组织没权限但设置默认布局；
//		html = sysIndexLayoutManageManager.getHasRightsLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
		//4、如果找不到找系统管理员的设置默认布局;
		html = sysIndexLayoutManageManager.getManagerLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
//		//5、再找不到则使用系统默认布局。
//		if (BeanUtils.isEmpty(html))
//			html = defaultIndexLayout();
		return "";
	}

	@Override
	public SysIndexMyLayout getByUser(String currentUserId) {
		return baseMapper.getByUserId(currentUserId);
	}

	@Override
	public String obtainIndexMyData(String layoutId) {
		SysIndexMyLayout sysIndexMyLayout = this.get(layoutId);
		if (BeanUtils.isNotEmpty(sysIndexMyLayout))
			return sysIndexMyLayout.getTemplateHtml();
		//2.找自己拥有权限的管理布局 ，按是否默认，排序
		String html = sysIndexLayoutManageManager.getMyHasRightsLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
		//3、找自己所属子组织没权限但设置默认布局；
//		html = sysIndexLayoutManageManager.getHasRightsLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
		//4、如果找不到找系统管理员的设置默认布局;
		html = sysIndexLayoutManageManager.getManagerLayout();
		if (BeanUtils.isNotEmpty(html))
			return html;
		//5、再找不到则使用系统默认布局。
		if (BeanUtils.isEmpty(html))
			html = defaultIndexLayout();
		return "";
	}

	@Override
	public void removeByUserId(String userId) {
		baseMapper.removeByUserId(userId);
	}

	@Override
	public void setValid(String id) {
		baseMapper.updateValid(0,ContextUtil.getCurrentUserId());
		baseMapper.setValid(id);
	}
}
