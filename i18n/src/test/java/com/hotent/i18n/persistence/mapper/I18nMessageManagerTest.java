package com.hotent.i18n.persistence.mapper;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.SQLUtil;
import com.hotent.i18n.I18nTestCase;
import com.hotent.i18n.persistence.manager.I18nMessageManager;
import org.junit.Test;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-11 13:50
 */
public class I18nMessageManagerTest extends I18nTestCase {

    @Resource
    I18nMessageManager i18nMessageManager;

    @Test
    public void testGetList(){
        QueryFilter queryFilter = QueryFilter.build()
                                             .withDefaultPage();
                                           //.withParam("key","value");
        PageList<Map<String,String>> map = i18nMessageManager.getList(queryFilter);
        assertEquals(0,map.getPage());;
    }

    /**
     * 根据key获取国际化资源（各种类型的查询集合，不是单纯的单条记录）
     */
    @Test
    public void testGetByMesKey(){
        String key = "uc";
        Map<String,Object> map = i18nMessageManager.getByMesKey(key,SQLUtil.getDbType());
        assertEquals(key,map.get("key_"));
    }

    /**
     * 根据国际化资源key删除资源
     */
    @Test
    public void testDelByKey(){
        String key = "key";
        i18nMessageManager.delByKey(key);
        Map<String,Object> map = i18nMessageManager.getByMesKey(key,SQLUtil.getDbType());
        assertTrue(map==null);
        System.err.println(map);
    }

    /**
     * 根据key和type删除国际化资源
     */
    @Test
    public void testDelByKeyAndType(){
        String key = "key";
        String type = "type";
        i18nMessageManager.delByKeyAndType(key,type);
    }

    /**
     * 新增/修改/删除国际化资源
     */
    @Test
    public void testSaveI18nMessage(){
        String key = "newKey";
        List<Map<String, String>> mesTypeInfo = new ArrayList<Map<String,String>>();
        Map<String,String> mapOne = new HashMap<String,String>();
        mapOne.put("val","value");
        mapOne.put("type","type");
        mesTypeInfo.add(mapOne);
        String oldKey="oldkey";
        Map<String,Object> map = i18nMessageManager.saveI18nMessage(key,mesTypeInfo,oldKey);
        assertEquals(true,map.get("result"));
    }

    /**
     * 国际化资源
     */
    @Test
    public void testGetSearchList(){
        String val = "flower";
        List<Map<String, String>> map = i18nMessageManager.getSearchList(val);
        assertFalse(map==null);
    }
}
