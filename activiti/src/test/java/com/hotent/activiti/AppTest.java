package com.hotent.activiti;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void shouldAnswerWithTrue() {
		/*boolean b1 = AppTest.containsSqlInjection("and nm=1");
		assertEquals("b1不为true", true, b1);
		boolean b2 = AppTest.containsSqlInjection("niamsh delete from ");
		assertEquals("b2不为true", true, b2);
		boolean b3 = AppTest.containsSqlInjection("stand");
		assertEquals("b3不为false", false, b3);
		boolean b4 = AppTest.containsSqlInjection("and");
		assertEquals("b4不为true", true, b4);
		boolean b5 = AppTest.containsSqlInjection("niasdm%asjdj");
		assertEquals("b5不为true", true, b5);*/
		boolean b1 = AppTest.containsSqlInjection(null);
		System.out.println(b1);
//		assertEquals("b1不为true", true, b1);
	}

	/**
	 * 是否含有sql注入，返回true表示含有
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean containsSqlInjection(String obj) {
		//\\b(exec|insert|drop|grant|alter|delete|update|truncate)\\b|(\\*|;|\\+|'|%)
		Pattern pattern = Pattern.compile(
				"\\b(exec|insert|drop|grant|alter|delete|update|truncate)\\b");
		Matcher matcher = pattern.matcher(obj);
		return matcher.find();
	}
}
