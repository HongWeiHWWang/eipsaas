package com.hotent.sys.persistence.manager.impl;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.persistence.dao.DataDictDao;
import com.hotent.sys.persistence.manager.DataDictManager;
import com.hotent.sys.persistence.manager.SysTypeManager;
import com.hotent.sys.persistence.model.DataDict;
import com.hotent.sys.persistence.model.SysType;
import com.hotent.sys.util.BeanExcelUtils;
import com.hotent.sys.vo.DataDictExcelVo;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

@Service("dataDictManager")
public class DataDictManagerImpl extends BaseManagerImpl<DataDictDao, DataDict> implements DataDictManager{
	@Resource
	SysTypeManager sysTypeManager;
	
	@Override
	public List<DataDict> getByTypeId(String typeId) {
		return baseMapper.getByTypeId(typeId);
	}
	@Override
	public DataDict getByDictKey(String typeId, String key) {
		Map<String, Object> params = new HashMap<>();
		params.put("typeId", typeId);
		params.put("key", key);
		return baseMapper.getByDictKey(params);
	}
	
	@Override
	public void removeByIds(String ...ids) {
		if(BeanUtils.isNotEmpty(ids)){
			for(String id : ids){
				this.remove(id);
				List<DataDict> childs = getChildrenByParentId(id);
				for(DataDict dict : childs){
					this.remove(dict.getId());
				}
				
			}
		}
		super.removeByIds(ids);
	}
	/**
	 * 获取一级子节点
	 * @param id
	 * @return
	 */
	@Override
	public List<DataDict> getFirstChilsByParentId(String id){
		return baseMapper.getByParentId(id);
	}
	/**
	 * 获取所有的子节点
	 * @param id
	 * @return
	 */
	@Override
	public List<DataDict> getChildrenByParentId(String id){
		List<DataDict> childs = baseMapper.getByParentId(id);
		return getChilds(childs);
	}
	/**
	 * 通过子节点查询子节点
	 * @param childs
	 * @return
	 */
	private List<DataDict> getChilds(List<DataDict> childs){
		List<DataDict> dataDict = new ArrayList<DataDict>();
		// 如果孩子不为空 查询孩子的孩子
		if(BeanUtils.isNotEmpty(childs)){
			for(DataDict dict : childs){
				List<DataDict> children =  baseMapper.getByParentId(dict.getId());  
				//如果孩子的孩子们不为空  则通过孩子们 查询他们的孩子们
				if(BeanUtils.isNotEmpty(children)){
					children = getChilds(children);
					dataDict.addAll(children);
				}
				
			}
			dataDict.addAll(childs);
		}
		return dataDict;
	}
	@Override
	public void delByDictTypeId(String dictTypeId) {
		baseMapper.delByDictTypeId(dictTypeId);
	}
	
	
	
	
	/**
	 * 更新排序  sn
	 * @param dicId
	 * @param sn
	 */
	@Override
	public void updSn(String dicId, int sn) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", dicId);
		params.put("sn", sn);
		baseMapper.updSn(params);
	}
	@Override
	public CommonResult<String> removeByTypeIds(String typeIds) {
		if (StringUtil.isNotEmpty(typeIds)) {
			List<SysType> typeList = sysTypeManager.getChildByTypeId(typeIds);
			String[] typeIdList = new String[typeList.size()+1];
			typeIdList[0] = typeIds;
			for (int i = 0; i < typeList.size(); i++) {
				typeIdList[i+1] = typeList.get(i).getId();
			}
			sysTypeManager.removeByIds(typeIdList);
			for (String typeId : typeIdList) {
				baseMapper.delByDictTypeId(typeId);
			}
		}
		return new CommonResult<>("操作成功");
	}
	@Override
	public void importData(List<MultipartFile> files, String typeId) throws Exception {
		Iterator<MultipartFile> it = files.iterator();
		Map<String, DataDict> data = new HashMap<String, DataDict>();
		//数据字典分类
		SysType sysType = sysTypeManager.get(typeId);
		if(BeanUtils.isEmpty(sysType)) {
			throw new BaseException("请选择数据字典分类进行导入");
		}
		while (it.hasNext()) {
			MultipartFile file = it.next();
			List<DataDictExcelVo> list= BeanExcelUtils.readExcel(DataDictExcelVo.class,file);
			for (DataDictExcelVo vo:list) {
				String pKey = vo.getPidKey();
				String key = vo.getKey();
				String name = vo.getName();
				Map<String, Object> params = new HashMap<>();
				params.put("typeId", typeId);
				params.put("key", key);
				// 验证字典key 是否已经存在
				DataDict dict= baseMapper.getByDictKey(params);
				if(dict != null){
					throw new BaseException("该字典项值已经存在");
				}
				
				DataDict dataDict = new DataDict();
				dataDict.setTypeId(typeId);
				if(StringUtil.isEmpty(pKey)) {
					dataDict.setParentId(typeId);
				}else {
					//
					Map<String, Object> pMap = new HashMap<>();
					pMap.put("typeId", typeId);
					pMap.put("key", pKey);
					
					DataDict dtDict= baseMapper.getByDictKey(pMap);
					if(BeanUtils.isEmpty(dtDict)) {
						//从添加的列表中获取
						DataDict dmDict = data.get(pKey);
						if(BeanUtils.isEmpty(dmDict)) {
							throw new BaseException("请输入正确的父节点key");
						}else {
							dataDict.setParentId(dmDict.getId());
						}
					}
					dataDict.setParentId(dtDict.getId());
				}
				dataDict.setKey(key);
				dataDict.setName(name);
				this.create(dataDict);
				//添加列表
				data.put(key, dataDict);
			}
		}
	}

}
