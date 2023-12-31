package com.hotent.file.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.model.Tree;


 /**
 *
 * <pre>
 * 描述：附件目录对象
 * 构建组：x5-bpmx-platform
 * 作者:maoww
 * 邮箱:maoww@jee-soft.cn
 * 日期:2018-05-15 11:45:41
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @TableName("portal_sys_file_classify")
 @ApiModel(description="附件分类信息")
public class Catalog extends AutoFillModel<Catalog> implements Tree{


	@ApiModelProperty(name="id",notes="主键")
	@TableId("id")
	protected String id;

	@ApiModelProperty(name="name",notes="名称")
	@TableField("name")
	protected String name;

	@ApiModelProperty(name="parentId",notes="上级id")
	@TableField("parentId")
	protected String parentId;
	
	@ApiModelProperty(name="orderNo",notes="顺序")
	@TableField("orderNo")
	protected int orderNo;

	@ApiModelProperty("是否默认展开")
	@TableField(exist=false)
	private boolean open=true;

	@TableField(exist=false)
	protected List<Catalog> children = new ArrayList<Catalog>();

	@ApiModelProperty("是否父节点")
	@TableField(exist=false)
	protected String isParent;//是否有子节点数据



	 public String getIsParent() {
		return isParent;
	}


	 public boolean isOpen() {
		 return open;
	 }

	 public void setOpen(boolean open) {
		 this.open = open;
	 }



	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 id
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 name
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 返回 parentId
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}


	@Override
	@JsonIgnore
	public String getText() {
		return this.name;
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
		this.isParent=isParent;

	}


	public int getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
}
