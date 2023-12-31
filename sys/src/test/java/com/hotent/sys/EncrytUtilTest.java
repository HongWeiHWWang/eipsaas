package com.hotent.sys;

import static org.junit.Assert.*;
import org.junit.Test;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.StringUtil;

public class EncrytUtilTest {
	
	@Test
	public void test(){
		try {
			String orginWord = "sdswqwsad!$@#dsa";
			String encrypt = EncryptUtil.encrypt(orginWord);
			assertTrue(StringUtil.isNotEmpty(encrypt));
			String decrypt = EncryptUtil.decrypt(encrypt);
			assertEquals(orginWord, decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
