package com.hotent.i18n.persistence.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.i18n.I18nTestCase;
import com.hotent.i18n.persistence.manager.I18nMessageTypeManager;
import com.hotent.i18n.persistence.model.I18nMessageType;
import org.junit.Test;
import javax.annotation.Resource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-11 09:13
 */
public class I18nMessageTypeManagerTest extends I18nTestCase {
    @Resource
    I18nMessageTypeManager i18nMessageTypeManager;

    @Test
    public void test(){
        I18nMessageType i18nMessageType = new I18nMessageType();
        String suid = idGenerator.getSuid();
        String type = "zxy";
        i18nMessageType.setId(suid);
        i18nMessageType.setDesc("赵祥云");
        i18nMessageType.setType(type);
        //新增
        i18nMessageTypeManager.create(i18nMessageType);
        //查询
        I18nMessageType get = i18nMessageTypeManager.get(suid);
        assertEquals(type,get.getType());
        //更新
        type = "zhaoxiangyun";
        i18nMessageType.setType(type);
        i18nMessageTypeManager.update(i18nMessageType);
        //根据type获取语言类型
        I18nMessageType entity = i18nMessageTypeManager.getByType(type);
        assertEquals(type,entity.getType());

        // 通过ID删除数据
        i18nMessageTypeManager.remove(suid);
        I18nMessageType sstd = i18nMessageTypeManager.getOne(Wrappers.<I18nMessageType>lambdaQuery().eq(I18nMessageType::getType, type));
        assertTrue(sstd == null);
    }
}
