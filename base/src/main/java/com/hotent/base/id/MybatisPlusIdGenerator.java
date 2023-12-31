package com.hotent.base.id;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * 实现mp的id生成器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月9日
 */
public class MybatisPlusIdGenerator implements IdentifierGenerator{
	@Resource
	IdGenerator idGenerator;
	
	@Override
	public Long nextId(Object entity) {
		return idGenerator.nextId();
	}
}
