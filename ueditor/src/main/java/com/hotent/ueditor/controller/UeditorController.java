package com.hotent.ueditor.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hotent.ueditor.ActionEnter;


@RestController
@CrossOrigin
@RequestMapping("/ueditor")
public class UeditorController {

	@Autowired 
	private ActionEnter actionEnter;
	
	@ResponseBody
	@RequestMapping("/upload")
	public String exe(HttpServletRequest request){
		return actionEnter.exec(request);
	}
}