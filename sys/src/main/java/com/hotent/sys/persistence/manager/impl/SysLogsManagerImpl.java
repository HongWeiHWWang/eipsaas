package com.hotent.sys.persistence.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.sys.persistence.dao.SysLogsDao;
import com.hotent.sys.persistence.manager.SysLogsManager;
import com.hotent.sys.persistence.model.SysLogs;

/**
 * 
 * <pre> 
 * 描述：系统操作日志 处理实现类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-08-31 10:59:25
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("sysLogsManager")
public class SysLogsManagerImpl extends BaseManagerImpl<SysLogsDao, SysLogs> implements SysLogsManager{
	
	@Override
	public void removeByEexcutionTime(List<Map<String, Object>> params) {
		baseMapper.removeByEexcutionTime(params);
	}
}
