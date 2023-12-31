package com.hotent.file.extend;

import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.hotent.base.util.StringUtil;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailTablePolicy extends DynamicTableRenderPolicy {

    // 货品填充数据所在行数
    int goodsStartRow = 2;


    @Override
    public void render(XWPFTable table, Object data) {
        if (null == data) return;

        // 字表数据明细
        List<Map> subTable = (List<Map>) data;
        List<XWPFTableCell> templateCell =  table.getRow(goodsStartRow).getTableCells();
        Map<Integer,String> mapCell=new HashMap<>();
        for (int i = 0; i < templateCell.size(); i++) {
            String key=templateCell.get(i).getText().replace("{","").replace("}","");
            if(StringUtil.isNotEmpty(key)){
                key=key.substring(key.indexOf(".")+1,key.length());
            }
            mapCell.put(i,key);
        }
        if (null != subTable) {
            table.removeRow(goodsStartRow);
            for (int i = subTable.size()-1; i >= 0; i--) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(goodsStartRow);
                Map subMap=subTable.get(i);
                for (int j = 0; j <  templateCell.size(); j++) {
                    XWPFTableCell cell=insertNewTableRow.createCell();
                    String key=mapCell.get(j);
                    if(subMap.containsKey(key)){
                        cell.setText(subMap.get(key).toString());
                    }else{
                        cell.setText("");
                    }

                }
            }
        }
    }


}
