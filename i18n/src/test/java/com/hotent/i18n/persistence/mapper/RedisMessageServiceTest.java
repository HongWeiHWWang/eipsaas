package com.hotent.i18n.persistence.mapper;

import com.hotent.i18n.I18nTestCase;
import com.hotent.i18n.support.service.MessageService;
import org.junit.Test;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-12 14:19
 */
public class RedisMessageServiceTest extends I18nTestCase {

    @Resource
    MessageService messageService;

    /**
     * 通过资源KEY和国际化类型获取资源值
     */
    @Test
    public void testGetMessage() {
        String code = "i18nMessageError.deleteSuccess";
        String type = "en-US";
        String val = "Delete I18nMessageError Success";//redis服务器里面的code对应value值
        String message = messageService.getMessage(code, type);
        assertEquals(message,val);
    }

    /**
     * 批量获取资源值
     */
    @Test
    public void testGetMessages(){
        String type = "en-US";
        List<String> codelist = new ArrayList<>();
        codelist.add("i18nMessageError.deleteSuccess");
        codelist.add("i18nMessage.deleteFail");
        Map<String, String> map = messageService.getMessages(codelist,type);
        assertFalse(map==null);
    }

    /**
     * 初始化国际化资源到Cache中
     */
    @Test
    public void testInitMessage(){
        Map<String, Map<String, String>> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("en-US","zhaoxiangyun");
        map1.put("zh-TW","趙祥雲");
        map1.put("zh-CN","赵祥云");
        map.put("zxy",map1);
        messageService.initMessage();
    }

    /**
     * 清除对应缓存
     */
    @Test
    public void testHdel(){
        String key = "zxy";
        String field = "zh-TW";
        messageService.hdel(key,field);
    }

    /**
     * 根据键删除缓存
     */
    @Test
    public void test(){
        String key = "zxy";
        messageService.delByKey(key);
    }

    /**
     * 清空Cache中的所有国际化资源
     */
    @Test
    public void testClearAllMessage(){
        messageService.clearAllMessage();
    }
}