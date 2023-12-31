package com.hotent.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 基础实体类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月6日
 */
public abstract class BaseModel<T extends BaseModel<?>> extends Model<T>{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 获取主键值
	 * @return
	 */
	public String getPkVal() {
		Serializable pkVal = pkVal();
		if(pkVal!=null) {
			return pkVal.toString();
		}
		return null;
	}
}
