package com.hotent.base.test.util;

import static org.junit.Assert.*;
import org.junit.Test;
import com.hotent.base.util.BeanUtils;

/**
 * BeanUtils测试
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年8月24日
 */
public class BeanUtilsTest{
	@Test
	public void testJson() throws Exception{
		String[] ary = new String[] {};
		boolean empty = BeanUtils.isEmpty(ary);
		assertTrue(empty);
	}
}
