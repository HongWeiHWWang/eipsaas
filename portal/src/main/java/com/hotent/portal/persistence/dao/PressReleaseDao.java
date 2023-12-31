package com.hotent.portal.persistence.dao;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.portal.model.PressRelease;

/**
 * 
 * <pre> 
 * 描述：新闻公告 DAO接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-04-02 18:17:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface PressReleaseDao extends BaseMapper<PressRelease> {
	
	@Select("select * from w_xwgg ${ew.customSqlSegment}")
	List<PressRelease> getAll(@Param(Constants.WRAPPER) Wrapper<PressRelease> wrapper);

	List<String> getNews(LocalDateTime nowDate);
}
