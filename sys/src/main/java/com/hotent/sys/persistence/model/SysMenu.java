package com.hotent.sys.persistence.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.model.Tree;
import com.hotent.base.util.JsonUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统菜单
 * 
 * <pre>
 *  
 * 描述：系统菜单 实体对象
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 09:34:15
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@TableName("portal_sys_menu")
@ApiModel(description = "系统菜单")
public class SysMenu extends BaseModel<SysMenu> implements Tree {
	private static final long serialVersionUID = 1L;
	public final static String IS_CHECKED_N = "false";
	public final static String IS_CHECKED_Y = "true";

	@ApiModelProperty(name = "id", notes = "主键")
	@TableId("id_")
	protected String id;

	@ApiModelProperty(name = "parentId", notes = "父id")
	@TableField("parent_id_")
	protected String parentId;

	@ApiModelProperty(name = "name", notes = "菜单名称")
	@TableField("name_")
	protected String name;

	@ApiModelProperty(name = "alias", notes = "别名")
	@TableField("alias_")
	protected String alias;

	@ApiModelProperty(name = "menuIcon", notes = "图标")
	@TableField("menu_icon_")
	protected String menuIcon;

	@ApiModelProperty(name = "open", notes = "默认展开    1展开 0 不展开")
	@TableField("opened_")
	protected Integer open;

	@ApiModelProperty(name = "sn", notes = "排序")
	@TableField("sn_")
	protected int sn;

	@ApiModelProperty(name = "activeTab", notes = "三级菜单激活的tab页")
	@TableField("active_tab_")
	protected String activeTab;

	@ApiModelProperty(name = "tabsStyle", notes = "三级菜单tab标签页风格")
	@TableField("tabs_style_")
	protected String tabsStyle;

	@ApiModelProperty(name = "path", notes = "路径")
	@TableField("path_")
	protected String path;
	
	@ApiModelProperty(name = "href", notes = "外部Url地址")
	@TableField("href_")
	protected String href;
	
	@TableField("tenant_id_")
	@ApiModelProperty(name="tenantId",notes="租户id")
	private String tenantId;

	@ApiModelProperty(name = "sysMethod", notes = "菜单下对相应的请求")
	@TableField(exist=false)
	protected List<SysMethod> sysMethods = new ArrayList<SysMethod>();
	@TableField(exist=false)
	protected List<SysMenu> children = new ArrayList<SysMenu>();

	@ApiModelProperty(name = "isParent", notes = "是否父节点")
	@TableField(exist=false)
	protected String isParent;// 是否有子节点数据

	@ApiModelProperty(name = "check", notes = "是否选中")
	@TableField(exist=false)
	protected String check;// 是否有子节点数据

	

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 返回 父id
	 * 
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 菜单名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 返回 别名用作state
	 * 
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	/**
	 * 返回 图标
	 * 
	 * @return
	 */
	public String getMenuIcon() {
		return this.menuIcon;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public void setOpen(boolean opened) {
		this.open = opened ? 1 : 0;
	}

	public boolean isOpen() {
		return 1 == this.open;
	}

	/**
	 * 返回 排序
	 * 
	 * @return
	 */
	public int getSn() {
		return this.sn;
	}

	public List<SysMethod> getSysMethods() {
		return sysMethods;
	}

	public void setSysMethods(List<SysMethod> sysMethods) {
		this.sysMethods = sysMethods;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public String getTabsStyle() {
		return tabsStyle;
	}

	public void setTabsStyle(String tabsStyle) {
		this.tabsStyle = tabsStyle;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("parentId", this.parentId)
				.append("name", this.name).append("alias", this.alias).append("menuIcon", this.menuIcon)
				.append("open", this.open).append("sn", this.sn).toString();
	}

	@Override
	@JsonIgnore
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getChildren() {
		return children;
	}

	@Override
	public void setChildren(List children) {
		this.children = children;

	}

	@Override
	public void setIsParent(String isParent) {
		// TODO Auto-generated method stub

	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public boolean equals(SysMenu menu) {
		if (menu == this) {
			return true;
		}
		try {
			return JsonUtil.toJson(this).equals(JsonUtil.toJson(menu));
		} catch (Exception e) {
			return false;
		}
	}

}