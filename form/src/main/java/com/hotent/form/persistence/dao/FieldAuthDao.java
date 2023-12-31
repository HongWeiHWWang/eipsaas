package com.hotent.form.persistence.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.FieldAuth;

/**
 * 字段授权
 * <pre> 
 * 描述：form_field_auth DAO接口
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-10-27 14:37:11
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface FieldAuthDao extends BaseMapper<FieldAuth> {
	/**
	 * 通过实体名获取字段权限设置
	 * @param className
	 * @return
	 */
	FieldAuth getByEntName(String entName);
	/**
	 * 通过表名获取字段权限设置
	 * @param tableName
	 * @return
	 */
	FieldAuth getByTableName(String tableName);
	
	/**
	 * 通过类名获取字段权限设置
	 * @param className
	 * @return
	 */
	FieldAuth getByClassName(String className);
}
