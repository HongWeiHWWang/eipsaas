package com.hotent.bpm.engine.form;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.def.BpmSubTableRight;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.service.BoSubDataHandlers;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.uc.api.impl.util.ContextUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * 子表数据读取。
 *
 * @author ray
 *
 */
@Service("boSubDataHandlersImpl")
public class BoSubDataHandlersImpl implements BoSubDataHandlers {

    @Resource
    BpmDefinitionAccessor bpmDefinitionAccessor;
    @Resource
    GroovyScriptEngine groovyScriptEngine;

    /**
     * 根据外键获取子表数据
     *
     * @param boEnt		bo实体
     * @param fkValue	外键值
     * @return			子表数据列表
     */
    @Override
    public CommonResult<String> getSubDataSqlByFk(ObjectNode boEnt, Object fkValue,String defId,String nodeId,String parentDefKey) throws Exception {
        // 获取子表权限
        BpmSubTableRight bpmSubTableRight = getSubTableRight(defId, nodeId, parentDefKey, boEnt);

        // 拼装sql
        String sql = "";
        if (boEnt.get("type").asText().equals("manytomany")) {
            sql = "select A.* from " + boEnt.get("tableName").asText() + " A , form_bo_data_relation B where " + " B.SUB_BO_NAME = '" + boEnt.get("name").asText() + "' AND A." + boEnt.get("pkKey").asText() + "=B.FK_  AND B.PK_=?";
        } else {
            String fk = boEnt.get("fk").asText();
            if(StringUtil.isEmpty(fk)){
                throw new RuntimeException("通过添加外部表构建业务对象时必须指定外键");
            }
            sql = "select * from " + boEnt.get("tableName").asText() + " A  where A." + fk + "=?";
        }
        sql = handleRight(bpmSubTableRight, fkValue, sql);
        return new CommonResult<>(true, "获取成功！",sql);
    }

    /**
     * 获取权限。
     *
     * @param defId
     * @param nodeId
     * @param parentDefKey
     * @param boEnt
     * @return
     * @throws Exception
     */
    private BpmSubTableRight getSubTableRight(String defId, String nodeId, String parentDefKey, ObjectNode boEnt) throws Exception {
        if(StringUtil.isEmpty(nodeId) || "undefined".equals(nodeId)) return null;
        BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
        if(BeanUtils.isEmpty(nodeDef) || NodeType.START.equals(nodeDef.getType()) || NodeType.END.equals(nodeDef.getType())) return null;
        UserTaskNodeDef utnd = (UserTaskNodeDef) nodeDef;
        BpmSubTableRight bpmSubTableRight = null;
        List<BpmSubTableRight> list=utnd.getBpmSubTableRightByParentDefKey(parentDefKey);
        for (BpmSubTableRight bsr : list) {
            if (bsr.getTableName().equals(boEnt.get("name").asText())) {
                bpmSubTableRight = bsr;
                break;
            }
        }
        return bpmSubTableRight;
    }

    private String handleRight(BpmSubTableRight right, Object fkValue, String sql) throws Exception {
        if (right == null)
            return sql;
        if (right.getRightType().equals("script")) {
            String str = groovyScriptEngine.executeString(right.getScript(), new HashMap<String, Object>());
            sql += " and " + str;
        } else if (right.getRightType().equals("curUser")) {
            sql = "select a.* from ("+sql+") a , bpm_bus_link b where a.ID_ = B.businesskey_str_ and  B.start_id_="+ ContextUtil.getCurrentUserId();
        }else if(right.getRightType().equals("curOrg")){
            sql = "select a.* from ("+sql+") a , bpm_bus_link b where a.ID_ = B.businesskey_str_ and  B.start_group_id_="+ ContextUtil.getCurrentGroupId();
        }
        return sql;
    }
}
