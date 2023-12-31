package com.hotent.form.persistence.dao;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.CustomChart;

/**
 * 自定义图表
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface CustomChartDao extends BaseMapper<CustomChart> {
    CustomChart getChartByAlias(String alias);

    List<CustomChart> listChartByAlias(String alias);
}
