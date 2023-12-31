package com.hotent.sys.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.SysLogs;

/**
 * 
 * <pre> 
 * 描述：系统操作日志 DAO接口
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-08-31 10:59:25
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysLogsDao extends BaseMapper<SysLogs> {

	void removeByEexcutionTime(@Param("params") List<Map<String, Object>> params);
}
