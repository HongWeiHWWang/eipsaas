package com.hotent.uc.manager.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.dao.PropertiesDao;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.PropertiesService;
import com.hotent.uc.model.Properties;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.properties.PropertiesVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：portal_sys_properties 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-07-28 09:19:53
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class PropertiesManagerImpl extends BaseManagerImpl <PropertiesDao, Properties> implements PropertiesService{
	
	@Override
	public List<String> getGroups() {
		return baseMapper.getGroups();
	}

	@Override
	public boolean isExist(Properties properties) {
		Integer rtn = baseMapper.isExist(properties.getCode(),properties.getId());
		return rtn > 0;
	}
	
	
	public  Map<String,String>  reloadProperty(){
		Map<String,String> map=new HashMap<String, String>();
		List<Properties> list=this.getAll();
		for(Properties property:list){
			map.put(property.getCode().toLowerCase(), property.getRealVal());
		}
		return map;
	}

	@Override
	public String getByCode(String code) {
		Properties properties = baseMapper.getByCode(code);
		return properties.getValue();
	}

	@Override
	public String getByCode(String code, String defaultValue) {
		String val=getByCode(code);
		if(StringUtil.isEmpty(val)) return defaultValue;
		return val;
	}

	@Override
	public Integer getIntByCode(String code) {
		String val= getByCode(code);
		if(StringUtil.isEmpty(val)) return 0;
		Integer rtn=Integer.parseInt(val);
		return rtn;
	}

	@Override
	public Integer getIntByCode(String code, Integer defaulValue) {
		String val= getByCode(code);
		if(StringUtil.isEmpty(val)) return defaulValue;
		Integer rtn=Integer.parseInt(val);
		return rtn;
	}

	@Override
	public Long getLongByCode(String code) {
		String val= getByCode(code);
		if(StringUtil.isEmpty(val)) return 0L;
		Long rtn=Long.parseLong(val);
		return rtn;
	}

	@Override
	public boolean getBooleanByCode(String code) {
		String val= getByCode(code);
		return Boolean.parseBoolean(val);
	}

	@Override
	public boolean getBooleanByCode(String code, boolean defaulValue) {
		String val= getByCode(code);
		if(StringUtil.isEmpty(val)) return defaulValue;
		if("1".equals(val)) return true;
		return Boolean.parseBoolean(val);
	}

    @Override
    public Properties getPropertiesByCode(String code) {
        return baseMapper.getByCode(code);
    }

	@Override
    @Transactional
	public void removeByCode(String ...codes) {
		for (String als : codes) {
			baseMapper.removeByCode(als,LocalDateTime.now());
		}
	}

	@Override
    @Transactional
	public CommonResult<String> updateProperties(PropertiesVo propertiesVo)
			throws Exception {
		if(StringUtil.isEmpty(propertiesVo.getCode())){
			throw new RequiredException("更新系统参数失败，系统参数编码【code】必填！");
		}
		Properties properties = baseMapper.getByCode(propertiesVo.getCode());
		if(BeanUtils.isEmpty(properties)){
			return new CommonResult<String>(false, "更新系统参数失败，系统参数编码【"+properties.getCode()+"】不存在！", "");
		}
		if(StringUtil.isNotEmpty(propertiesVo.getName())){
			properties.setName(propertiesVo.getName());
		}
		if(propertiesVo.getGroup()!=null){
			properties.setGroup(propertiesVo.getGroup());
		}
		if(propertiesVo.getValue()!=null){
			properties.setValue(propertiesVo.getValue());
		}
		if(propertiesVo.getDescription()!=null){
			properties.setDescription(propertiesVo.getDescription());
		}
		this.update(properties);
		return new CommonResult<String>(true, "更新系统参数成功！", "");
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
}
