package com.hotent.bpm.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmSecretaryManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * <pre> 
 * 描述：秘书管理表 DAO接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2019-09-16 10:07:13
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BpmSecretaryManageDao extends BaseMapper<BpmSecretaryManage> {

    /**
     * 根据当前登录用户ID获取该用户的领导
     * @param userId
     * @return
     */
    List<BpmSecretaryManage> getSecretaryByUserId(@Param("userId") String userId);
}
