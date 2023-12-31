package com.hotent.base;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.hotent.base.test.controller.AuthenticationRestControllerTest;
import com.hotent.base.test.controller.ToolsControllerTest;
import com.hotent.base.test.manager.CommonManagerTest;
import com.hotent.base.test.manager.StudentManagerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({AuthenticationRestControllerTest.class, CommonManagerTest.class, StudentManagerTest.class, ToolsControllerTest.class})
public class SuiteExecuteTests {

}
