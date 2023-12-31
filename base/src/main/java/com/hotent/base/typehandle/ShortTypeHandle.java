package com.hotent.base.typehandle;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 处理实体类中为Boolean，Jdbc中为char的类型转换器
 * <p>
 * 除了Postgresql以外，其他数据库可以自动转换。
 * 用法：@TableField(typeHandler = ShortTypeHandle.class)
 * </p>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月24日
 */
public class ShortTypeHandle extends BaseTypeHandler<Object> {
	public ShortTypeHandle() {
    }
	@Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
    	if (parameter instanceof LocalDateTime) {
    		parameter = Timestamp.valueOf((LocalDateTime)parameter);
		}
        ps.setObject(i, parameter);
    }

	@Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object result = rs.getObject(columnName);
        return rs.wasNull() ? null : dealResult(result);
    }
	@Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object result = rs.getObject(columnIndex);
        return rs.wasNull() ? null : dealResult(result);
    }
	@Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object result = cs.getObject(columnIndex);
        return cs.wasNull() ? null : dealResult(result);
    }

    /**
     * 为了解决错误：
     * 26-Sep-2018 14:21:06.634 WARNING [http-apr-8080-exec-6] org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver.handleHttpMessageNotWritable Failed to write HTTP message: org.springframework.http.converter.HttpMessageNotWritableException:
     * Could not write JSON: No serializer found for class java.io.ByteArrayInputStream and no properties discovered to create BeanSerializer
     * (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS);
     * nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class java.io.ByteArrayInputStream and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
     * (through reference chain: java.util.HashMap["pageData"]->java.util.ArrayList[0]->java.util.HashMap["UPDATE_TIME"]->oracle.sql.TIMESTAMP["stream"])
     * @param result
     * @return
     * @throws SQLException
     */
    private Object dealResult(Object result) throws SQLException {
    	if(result instanceof Boolean){
			if((boolean) result){
				return 1;
			}else{
				return 0;
			}
		}else{
			return result;
		}
    }
    
    
    /**
	 * 此方法是在插入是进行设置参数
	 * 参数：	PreparedStatement 
	 * 		int	i				为Jdbc预编译时设置参数的索引值
	 * 		Object obj			要插入的参数值
	 * 		JdbcType jdbcType	要插入JDBC的类型
	 */
    @Override
	public void setParameter(PreparedStatement ps, int i, Object obj, JdbcType jdbcType) throws SQLException {
		if(obj == null){
			ps.setInt(i, 0);
		}
		Boolean par = (Boolean)obj;
		if(par){
			ps.setInt(i, 1);
		}else{
			ps.setInt(i, 0);
		}
	}
}
