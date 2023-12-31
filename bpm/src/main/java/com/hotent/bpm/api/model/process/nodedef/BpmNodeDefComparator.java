package com.hotent.bpm.api.model.process.nodedef;

import java.util.Comparator;

public class BpmNodeDefComparator implements Comparator<BpmNodeDef> {
    @Override
    public int compare(BpmNodeDef o1, BpmNodeDef o2) {
        int order1=o1.getOrder();
        int order2=o2.getOrder();
        if(order1==order2){
        	return o1.getNodeId().compareToIgnoreCase(o2.getNodeId());
        }else{
        	return (order1>order2)?1:-1;
        }
}
}