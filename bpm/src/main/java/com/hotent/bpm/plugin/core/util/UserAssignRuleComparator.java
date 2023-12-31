package com.hotent.bpm.plugin.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;

public class UserAssignRuleComparator  implements Comparator<UserAssignRule> {

	public int compare(UserAssignRule o1, UserAssignRule o2) {
		
		int groupNo1=o1.getGroupNo();
		int groupNo2=o2.getGroupNo();
		
		return (groupNo1==groupNo2)?0:(groupNo1>groupNo2)?1:-1;
	}

	
	public static void main(String[] args) {
		UserAssignRule o1=new UserAssignRule();
		UserAssignRule o2=new UserAssignRule();
		UserAssignRule o3=new UserAssignRule();
		
		o1.setName("group1");
		o1.setGroupNo(1);
		o2.setName("group2");
		o2.setGroupNo(1);
		o3.setName("group3");
		o3.setGroupNo(3);
		
		
		
		UserAssignRuleComparator comparator=new UserAssignRuleComparator();
		
		List<UserAssignRule> list=new ArrayList<UserAssignRule>();
		
		list.add(o3);
		list.add(o2);
		list.add(o1);
		
		Collections.sort(list, comparator);
		
		for(UserAssignRule userRule:list){
			System.out.println(userRule.getName());
		}
		
	}
	

}
