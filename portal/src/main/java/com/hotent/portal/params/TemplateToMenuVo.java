package com.hotent.portal.params;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 日历操作对象
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年8月2日
 */
public class TemplateToMenuVo {

	@ApiModelProperty(name = "alias", notes = "菜单别名", required = true)
	protected String alias;
	
	@ApiModelProperty(name = "name", notes = "菜单名称", required = true)
	protected String name;

	@ApiModelProperty(name = "path", notes = "菜单路径", required = true)
	protected String path;

	@ApiModelProperty(name = "parentAlias", notes = "父菜单别名", required = true)
	protected String parentAlias;
	
	@ApiModelProperty(name = "templateAlias", notes = "业务数据模板别名", required = true)
	protected String templateAlias;

    @ApiModelProperty(name="href", notes="外部Url地址")
    protected String href;//是否有子节点数据
    @ApiModelProperty(name = "menuId", notes = "菜单Id", required = true)
    protected String menuId;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParentAlias() {
		return parentAlias;
	}

	public void setParentAlias(String parentAlias) {
		this.parentAlias = parentAlias;
	}

	public String getTemplateAlias() {
		return templateAlias;
	}

	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}
}