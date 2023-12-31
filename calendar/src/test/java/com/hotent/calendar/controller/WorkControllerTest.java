package com.hotent.calendar.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.hotent.Calendar.controller.WorkController;
import com.hotent.Calendar.model.Calendar;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.calendar.CalendarTestCase;

class WorkControllerTest extends CalendarTestCase {

	@Resource
	WorkController workController;
	
	@Test
	void test() throws Exception {
		
		Calendar t = new Calendar();
		t.setIsDefault('1');
		t.setMemo("添加工作日历");
		t.setName("默认工作日历");
		workController.create(t);
		
		Calendar t2 = new Calendar();
		t2.setIsDefault('0');
		t2.setMemo("添加工作日历");
		t2.setName("默认工作日历");
		workController.create(t2);
		
		QueryFilter<Calendar> queryFilter = QueryFilter.<Calendar>build().withDefaultPage();
		PageList<Calendar> listJson = workController.listJson(queryFilter);
		assertTrue(BeanUtils.isNotEmpty(listJson));
		Calendar calendar = workController.get(t.getId());
		assertEquals("默认工作日历", calendar.getName());
		
		workController.setDefault(t.getId());
		
		
	}

}
