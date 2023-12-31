package com.hotent.sys.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.SysModule;

public interface SysModuleDao extends BaseMapper<SysModule> {
    SysModule getModuleByCode(String code);
}
