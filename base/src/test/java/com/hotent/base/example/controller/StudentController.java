package com.hotent.base.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hotent.base.controller.BaseController;
import com.hotent.base.example.manager.StudentManager;
import com.hotent.base.example.model.Student;
import io.swagger.annotations.Api;

/**
 * student controller
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
@RestController
@RequestMapping("/api/student/")
@Api(tags="学生")
public class StudentController extends BaseController<StudentManager, Student> {
}
