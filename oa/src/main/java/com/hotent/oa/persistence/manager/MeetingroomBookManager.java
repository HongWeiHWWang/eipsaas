package com.hotent.oa.persistence.manager;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.oa.model.MeetingroomBook;

import java.io.IOException;

/**
 * 
 * <pre> 
 * 描述：会议室预约 处理接口
 * 构建组：x7
 * 作者:David
 * 邮箱:376514860@qq.com
 * 日期:2023-12-24 01:13:25
 * 版权：wijo
 * </pre>
 */
public interface MeetingroomBookManager extends BaseManager<MeetingroomBook>{


    PageList<ObjectNode> getBookList(QueryFilter queryFilter) throws IOException;
}
