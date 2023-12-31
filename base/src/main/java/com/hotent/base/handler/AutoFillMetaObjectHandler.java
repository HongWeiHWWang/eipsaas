package com.hotent.base.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hotent.base.context.BaseContext;

/**
 * 字段自动填充处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月8日
 */
@Component
public class AutoFillMetaObjectHandler implements MetaObjectHandler{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	BaseContext baseContext;

	@Override
	public void insertFill(MetaObject metaObject) {
		logger.info("start insert fill ....");
		this.strictInsertFill(metaObject, "createBy", String.class, baseContext.getCurrentUserId());
		this.strictInsertFill(metaObject, "createOrgId", String.class, baseContext.getCurrentOrgId());
		this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, getSetterTypeOfLocal(metaObject, "createTime"));
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		logger.info("start update fill ....");
		this.strictUpdateFill(metaObject, "updateBy", String.class, baseContext.getCurrentUserId());
		this.setFieldValByName("updateTime", getSetterTypeOfLocal(metaObject, "updateTime"), metaObject);
	}

	/**
	 *  通过属性元信息获取指定属性的当前值
	 *  <p>主要解决不同的日期类型的字段当前值</p>
	 * @param metaObject
	 * @param propertyName
	 * @return
	 */
	private Object getSetterTypeOfLocal(MetaObject metaObject, String propertyName){
		Class<?> targetType = metaObject.getSetterType(propertyName);
		Object obj = null;
		if(LocalDateTime.class.equals(targetType)) {
			obj = LocalDateTime.now();
		}
		else if(LocalDate.class.equals(targetType)) {
			obj = LocalDate.now();
		}
		else if(Date.class.equals(targetType)) {
			obj = new Date();
		}
		return obj;
	}
}
