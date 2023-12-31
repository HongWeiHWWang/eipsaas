package com.hotent.base.test.controller;

import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.hotent.base.BaseTestCase;
import com.hotent.base.controller.AuthenticationRestController;
import com.hotent.base.jwt.JwtAuthenticationRequest;
import com.hotent.base.util.JsonUtil;

public class AuthenticationRestControllerTest extends BaseTestCase{
	@Resource
	AuthenticationRestController authenticationRestController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(authenticationRestController).build();
	}

	@Test
	public void auth() throws Exception {
		JwtAuthenticationRequest request = new JwtAuthenticationRequest();
		request.setUsername("admin");
		request.setPassword("123456");

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
																    .contentType(MediaType.APPLICATION_JSON)
																    .content(JsonUtil.toJson(request)))
									 .andExpect(MockMvcResultMatchers.status().isOk())
									 .andDo(MockMvcResultHandlers.print())
									 .andReturn();

		logger.info(mvcResult.getResponse().getContentAsString());
	}
}
