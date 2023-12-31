package com.hotent.system.persistence.manager;


import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.system.IntegrateTestCase;
import com.hotent.system.enums.ExterUniEnum;
import com.hotent.system.model.SysExternalUnite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;


public class SysExternalUniteManagerTest extends IntegrateTestCase {
    @Resource
    SysExternalUniteManager sysExternalUniteManager;

    @Before
    public void runBeforeTestMethod() {
        System.out.println("测试开始");
    }

    @After
    public void runAfterTestMethod() {
        System.out.println("测试完成");
    }

    @Test
    public void testCurd(){
        String id=UniqueIdUtil.getSuid();
        SysExternalUnite sysExternalUnite=new SysExternalUnite();
        sysExternalUnite.setType(ExterUniEnum.Dingtalk.getKey());
        sysExternalUnite.setCorpName("钉钉");
        sysExternalUnite.setId(id);
        sysExternalUniteManager.create(sysExternalUnite);
        SysExternalUnite sysExternalUnite1=sysExternalUniteManager.getDingtalk();
        assertEquals(id, sysExternalUnite1.getId());
        assertTrue(sysExternalUniteManager.isTypeExists(ExterUniEnum.Dingtalk.getKey(),UniqueIdUtil.getSuid()));
        assertFalse(BeanUtils.isNotEmpty(sysExternalUniteManager.getWeChatOfficialAccounts()));
    }
}
