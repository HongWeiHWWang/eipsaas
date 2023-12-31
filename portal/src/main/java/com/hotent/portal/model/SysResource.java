package com.hotent.portal.model;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.model.Tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 子系统资源 实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author Li
 * @email liyang@jee-soft.cn
 * @date 2018年6月21日
 */
@ApiModel(description="子系统资源 实体对象")
public class SysResource implements Tree{

	@ApiModelProperty(name="id", notes="主键")
	protected String id; 

	@ApiModelProperty(name="systemId", notes="子系统ID")
	protected String systemId; 
	
	@ApiModelProperty(name="alias", notes="资源别名")
	protected String alias; 

	@ApiModelProperty(name="name", notes="资源名")
	protected String name; 
	
	@ApiModelProperty(name="defaultUrl", notes="默认地址")
	protected String defaultUrl; 

	@ApiModelProperty(name="enableMenu", notes="显示到菜单(1:显示  0:不显示)", allowableValues="0,1")
	protected Integer enableMenu; 

	@ApiModelProperty(name="hasChildren", notes="是否有子节点")
	protected Integer hasChildren; 

	@ApiModelProperty(name="opened", notes="OPENED_")
	protected Integer opened; 

	@ApiModelProperty(name="icon", notes="图标")
	protected String icon=""; 
	
	@ApiModelProperty(name="newWindow", notes="打开新窗口")
	protected Integer newWindow; 

	@ApiModelProperty(name="sn", notes="排序")
	protected Long sn; 

	@ApiModelProperty(name="parentId", notes="父资源ID")
	protected String parentId;

	@ApiModelProperty(name="relResources", notes="资源的URL列表")
	protected List<RelResource> relResources = new ArrayList<RelResource>();
	
	@ApiModelProperty(name="children", notes="子系统资源列表")
	protected List<SysResource> children = new ArrayList<SysResource>();
	
	@ApiModelProperty(name="checked", notes="是否已分配给角色")
	protected boolean checked=false;
	
	@ApiModelProperty(name="isParent", notes="是否父节点")
	protected String isParent;//是否有子节点数据
	
	
	public String getIsParent() {
		return isParent;
	}

	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 设置子系统ID
	 * @param systemId 子系统ID
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	/**
	 * 返回 子系统ID
	 * @return
	 */
	public String getSystemId() {
		return this.systemId;
	}
	
	/**
	 * 设置资源别名
	 * @param alias 资源别名
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * 返回 资源别名
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}
	
	/**
	 * 设置资源名
	 * @param name 资源名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 资源名
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 设置默认地址
	 * @param defaultUrl 默认地址
	 */
	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}
	
	/**
	 * 返回 默认地址
	 * @return
	 */
	public String getDefaultUrl() {
		return this.defaultUrl;
	}
	
	/**
	 * 设置显示到菜单
	 * @param enableMenu 显示到菜单
	 */
	public void setEnableMenu(Integer enableMenu) {
		this.enableMenu = enableMenu;
	}
	
	/**
	 * 返回 显示到菜单(1,显示,0 ,不显示)
	 * @return
	 */
	public Integer getEnableMenu() {
		return this.enableMenu;
	}
	
	/**
	 *	设置是否有子系统
	 * @param hasChildren 是否有子系统
	 */
	public void setHasChildren(Integer hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	/**
	 * 返回 是否有子节点
	 * @return
	 */
	public Integer getHasChildren() {
		return this.hasChildren;
	}
	
	/**
	 * 设置 OPENED_
	 * @param opened OPENED_
	 */
	public void setOpened(Integer opened) {
		this.opened = opened;
	}
	
	/**
	 * 返回 OPENED_
	 * @return
	 */
	public Integer getOpened() {
		return this.opened;
	}
	
	/**
	 * 设置图标
	 * @param icon 图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * 返回 图标
	 * @return
	 */
	public String getIcon() {
		return this.icon;
	}
	
	/**
	 * 设置打开新窗口
	 * @param newWindow 打开新窗口
	 */
	public void setNewWindow(Integer newWindow) {
		this.newWindow = newWindow;
	}
	
	/**
	 * 返回 打开新窗口
	 * @return
	 */
	public Integer getNewWindow() {
		return this.newWindow;
	}
	
	/**
	 * 设置序号
	 * @param sn
	 */
	public void setSn(Long sn) {
		this.sn = sn;
	}
	
	/**
	 * 返回序号
	 * @return
	 */
	public Long getSn() {
		return this.sn;
	}
	
	/**
	 * 返回父id
	 * @return
	 */
	public String getParentId() {
		return parentId;
	}
	
	/**
	 * 设置父id
	 * @param parentId 父id
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 返回资源url列表
	 * @return
	 */
	public List<RelResource> getRelResources() {
		return relResources;
	}

	/**
	 * 设置资源url列表
	 * @param relResources
	 */
	public void setRelResources(List<RelResource> relResources) {
		this.relResources = relResources;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) 	return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SysResource other = (SysResource) obj;
		if (id.equals(other.id)) return true;
		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("systemId", this.systemId) 
		.append("alias", this.alias) 
		.append("name", this.name) 
		.append("defaultUrl", this.defaultUrl) 
		.append("enableMenu", this.enableMenu) 
		.append("hasChildren", this.hasChildren) 
		.append("opened", this.opened) 
		.append("parentId",this.parentId)
		.append("icon", this.icon) 
		.append("newWindow", this.newWindow) 
		.append("sn", this.sn) 
		.toString();
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children){
		this.children = children;
	}

	@Override
	public String getText() {
		return this.name;
	}

	@Override
	public void setIsParent(String isParent) {
	}
}