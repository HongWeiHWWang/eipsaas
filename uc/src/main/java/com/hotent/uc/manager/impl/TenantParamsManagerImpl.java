package com.hotent.uc.manager.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.TenantParamsDao;
import com.hotent.uc.manager.ParamsManager;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.manager.TenantParamsManager;
import com.hotent.uc.model.Params;
import com.hotent.uc.model.TenantManage;
import com.hotent.uc.model.TenantParams;
import com.hotent.uc.params.params.ParamObject;

/**
 * 
 * <pre> 
 * 描述：租户扩展参数值 处理实现类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:54:36
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("tenantParamsManager")
public class TenantParamsManagerImpl extends BaseManagerImpl<TenantParamsDao, TenantParams> implements TenantParamsManager{

	@Resource
	TenantManageManager tenantManageManager;
	@Resource
	ParamsManager paramsService;
	
	@Override
	public List<TenantParams> getByTenantId(String tenantId) {
		return baseMapper.getByTenantId(tenantId);
	}

	@Override
	@Transactional
	public void deleteByTenantId(String tenantId) {
		baseMapper.deleteByTenantId(tenantId);
	}

	@Override
	@Transactional
	public CommonResult<String> saveUserParams(String tenantId,
			List<ParamObject> params) {
		if(StringUtil.isEmpty(tenantId)){
			return new CommonResult<>(false, "租户id不能为空！");
		}
		try {
			StringBuilder pcodes = new StringBuilder();
			TenantManage tenant = tenantManageManager.get(tenantId);
			boolean isTrue = false;
			if(BeanUtils.isEmpty(tenant)){
				return new CommonResult<>(false, "保存失败，没找到租户id为【"+tenantId+"】的租户！");
			}else{
				List<ObjectNode> list = new ArrayList<ObjectNode>();
				if(BeanUtils.isNotEmpty(params)){
					for (ParamObject paramObject : params) {
						Params param = null;
						try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
							param = paramsService.getByAlias(paramObject.getAlias());
						}
						if(BeanUtils.isNotEmpty(param)&&"3".equals(param.getType())){
							list.add((ObjectNode) JsonUtil.toJsonNode(paramObject.toString()));
							isTrue = true;
						}else{
							pcodes.append(paramObject.getAlias()+"，");
						}
					}
				}
				if(BeanUtils.isNotEmpty(list)){
					this.saveParams(tenantId, list);
					return new CommonResult<String>(true, isTrue&&StringUtil.isEmpty(pcodes.toString())?"保存成功":"部分保存成功，用户参数编码："+pcodes+"不存在！", "");
				}
				return new CommonResult<>(false, "未保存任何参数，租户参数编码："+pcodes+"不存在！");
			}
		} catch (Exception e) {
			String msg=e.getMessage();
			if(msg.indexOf("ORA-12899")>-1) msg="参数值过长";
			return new CommonResult<>(false, "租户扩展参数保存失败："+msg);
		}
	}

    @Transactional
	private void saveParams(String tenantId, List<ObjectNode> lists) throws SQLException {
		
		for (ObjectNode ObjectNode : lists) {
			 TenantParams params = baseMapper.getByTenantIdAndCode(tenantId, ObjectNode.get("alias").asText());
			 if(BeanUtils.isNotEmpty(params)){
				 if(ObjectNode.hasNonNull("value") && !"null".equals(ObjectNode.get("value").asText())){
					 params.setValue(ObjectNode.get("value").asText());
				 }else{
					 params.setValue("");
				 }
				 this.update(params);
			}else{
				TenantParams tenantParams = new TenantParams();
				tenantParams.setCode(ObjectNode.get("alias").asText());
				if(!"null".equals(ObjectNode.get("value"))){
					tenantParams.setValue(ObjectNode.get("value").asText());
				}
				tenantParams.setTenantId(tenantId);
				tenantParams.setId(UniqueIdUtil.getSuid());
				this.create(tenantParams);
			}
		}
	}
	
}
