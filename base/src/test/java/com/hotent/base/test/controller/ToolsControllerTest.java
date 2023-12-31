package com.hotent.base.test.controller;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hotent.base.BaseTestCase;
import com.hotent.base.controller.ToolsController;

public class ToolsControllerTest extends BaseTestCase{
	@Resource
	ToolsController toolsController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(toolsController).build();
	}

	@Test
	public void getPinyin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/base/tools/v1/getPinyin")
																	.contentType(MediaType.APPLICATION_JSON)
																	.param("chinese","宏天软件"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	@Test
	public void encryptPassword() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/base/tools/v1/encryptDbPassword")
																	.contentType(MediaType.APPLICATION_JSON)
																	.param("password","1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
}
