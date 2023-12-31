package com.hotent.bo.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.bo.model.BoData;
import com.hotent.bo.util.BoUtil;

public class BoDataTest {
	
	@Test
	public void testBoData() throws IOException{
		String name = "洛亚";
		String loverName = "奥丽娜";
		String maleFrid = "雷克斯";
		String maleSex = "男";
		
		Map<String,Object> row=new HashMap<String, Object>();
		row.put("name", "ray");
		
		BoData bdo = new BoData();
		bdo.set ("id", "1");
		bdo.set("name", name);

		BoData bdo1 = new BoData();
		bdo1.set("name", loverName);

		BoData bdo21 = new BoData();
		bdo21.set("name", maleFrid);
		bdo21.set("sex", maleSex);
		BoData bdo22 = new BoData();
		bdo22.set("name", "尤雅");
		bdo21.addSubRow("lover", bdo22);
		bdo21.addInitDataMap("lover", row);
		
		
		bdo.addSubRow("lover", bdo1);
		
		bdo.addInitDataMap("lover",row);
		bdo.addSubRow("friends", bdo21);
		bdo.addInitDataMap("friends", row);
		bdo.addSubRow("friends", bdo22);
		bdo.addSubRow("friends", bdo1);
		
		assertEquals(name, bdo.getString("name"));
		
		List<BoData> lovers = bdo.getSubByKey("lover");
		
		assertEquals(1, lovers.size());
		BoData lover = lovers.get(0);
		
		assertEquals(loverName, lover.getString("name"));
		
		List<BoData> friends = bdo.getSubByKey("friends");
		assertEquals(3, friends.size());
		
		for (BoData boData : friends) {
			if(maleFrid.equals(boData.getString("name"))){
				assertEquals(maleSex, boData.getString("sex"));
			}
		}
		
		ObjectNode json = BoUtil.toJSON(bdo, true);
		assertTrue(json.has("name"));
		assertEquals(name, json.get("name").asText());
		
		BoData transJSON = BoUtil.transJSON(json);
		assertEquals(name, transJSON.getString("name"));
	}
}
