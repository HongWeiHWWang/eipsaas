package com.hotent.form.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.form.persistence.manager.CustomDialogManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 组合对话框实体对象
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月13日
 */
@ApiModel(description="组合对话框实体对象")
@TableName("form_combinate_dialog")
public class CombinateDialog extends BaseModel<CombinateDialog> {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty("名字")
	@TableField("name_")
	protected String name; 
	
	@ApiModelProperty("别名")
	@TableField("alias_")
	protected String alias;
	
	@ApiModelProperty("宽度")
	@TableField("width_")
	protected Integer width;
	
	@ApiModelProperty("高度")
	@TableField("height_")
	protected Integer height;
	
	@ApiModelProperty("树形对话框ID")
	@TableField("tree_dialog_id_")
	protected String treeDialogId;
	
	@ApiModelProperty("树形对话框名称")
	@TableField("tree_dialog_name_")
	protected String treeDialogName;
	
	@ApiModelProperty("列表对话框ID")
	@TableField("list_dialog_id_")
	protected String listDialogId; 
	
	@ApiModelProperty("列表对话框名称")
	@TableField("list_dialog_name_")
	protected String listDialogName;
	
	@ApiModelProperty("树数据返回数据对应列表数据的查询条件")
	@TableField("field_")
	protected String field;
	// 以下字段跟数据库无关
	@TableField(exist=false)
	private CustomDialog treeDialog;
	@TableField(exist=false)
	private CustomDialog listDialog;

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 id_
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 name_
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
	 * 返回 alias_
	 * 
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * 返回 width_
	 * 
	 * @return
	 */
	public Integer getWidth() {
		return this.width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * 返回 height_
	 * 
	 * @return
	 */
	public Integer getHeight() {
		return this.height;
	}

	public void setTreeDialogId(String treeDialogId) {
		this.treeDialogId = treeDialogId;
	}

	/**
	 * 返回 tree_dialog_id_
	 * 
	 * @return
	 */
	public String getTreeDialogId() {
		return this.treeDialogId;
	}

	public void setTreeDialogName(String treeDialogName) {
		this.treeDialogName = treeDialogName;
	}

	/**
	 * 返回 tree_dialog_name_
	 * 
	 * @return
	 */
	public String getTreeDialogName() {
		return this.treeDialogName;
	}

	public void setListDialogId(String listDialogId) {
		this.listDialogId = listDialogId;
	}

	/**
	 * 返回 list_dialog_id_
	 * 
	 * @return
	 */
	public String getListDialogId() {
		return this.listDialogId;
	}

	public void setListDialogName(String listDialogName) {
		this.listDialogName = listDialogName;
	}

	/**
	 * 返回 list_dialog_name_
	 * 
	 * @return
	 */
	public String getListDialogName() {
		return this.listDialogName;
	}

	public void setField(String field) {
		this.field = field;
	}

	/**
	 * 返回 树数据返回数据对应列表数据的查询条件
	 * 
	 * @return
	 */
	public String getField() {
		return this.field;
	}

	public CustomDialog getTreeDialog() {
		if (StringUtil.isEmpty(treeDialogId)) {
			return null;
		}
		treeDialog = AppUtil.getBean(CustomDialogManager.class).get(treeDialogId);
		return treeDialog;
	}

	public CustomDialog getListDialog() {
		if (StringUtil.isEmpty(listDialogId)) {
			return null;
		}
		listDialog = AppUtil.getBean(CustomDialogManager.class).get(listDialogId);
		return listDialog;
	}

}