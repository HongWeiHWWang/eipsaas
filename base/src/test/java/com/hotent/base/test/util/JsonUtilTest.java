package com.hotent.base.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.example.model.Student;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.time.DateFormatUtil;

/**
 * JsonUtil测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
public class JsonUtilTest{
	@SuppressWarnings("static-access")
	@Test
	public void testJson() throws Exception{
		String name = "张三";
		LocalDate now = LocalDate.now();
		Student student = new Student("", name, now, new Short("1"), String.format("我叫%s，小学一年级学生", name));
		String json = JsonUtil.toJson(student);
		assertTrue(json.contains(name));
		
		Map<String, Object> map = JsonUtil.toMap(json);
		Object nameObj = map.get("name");
		assertEquals(name, nameObj);
		
		LocalDateTime aa = LocalDateTime.now();
		System.out.println(DateFormatUtil.formaDatetTime(aa));
		System.out.println(aa.getYear()+"--"+ aa.getMonthValue()+"--"+ aa.getDayOfMonth());
		LocalDateTime aa1=aa.of(aa.getYear(), aa.getMonthValue(), aa.getDayOfMonth(), 0, 0);
		System.out.println(DateFormatUtil.formaDatetTime(aa1));
		
		LocalDateTime nextDay = aa1.plusDays(1);
		System.out.println(DateFormatUtil.formaDatetTime(nextDay));
		
		System.out.println(JsonUtil.toJsonNode(json));
		Student bean = JsonUtil.toBean(json, Student.class);
		assertEquals(name, bean.getName());
		
		JsonNode jsonNode = JsonUtil.toJsonNode(bean);
		assertTrue(jsonNode.isObject());
		
		Student bean2 = JsonUtil.toBean(jsonNode, Student.class);
		assertEquals(name, bean2.getName());
		
		ObjectNode objectNode = jsonNode.deepCopy();
		String jsonNode2 = objectNode.get("name").asText();
		assertEquals(name, jsonNode2);
		JsonNode jsonBirthday = objectNode.get("birthday");
		assertEquals(DateFormatUtil.DATE_FORMAT_DATE.format(now), jsonBirthday.asText());
		
		JsonNode jsonNode3 = JsonUtil.toJsonNode(json);
		assertEquals(jsonNode.toString(), jsonNode3.toString());
		
		json = String.format("[%s]", json);
		List<Student> list = JsonUtil.toBean(json, new TypeReference<List<Student>>(){});
		assertEquals(1, list.size());
		assertEquals(name, list.get(0).getName());
		
		String array = "[{\"name\":\"aa\"}]";
		JsonNode node = JsonUtil.toJsonNode(array);
		System.out.println(node.toString());
		ArrayNode aNode = (ArrayNode) node;
		ObjectNode names = (ObjectNode) aNode.get(0);
		
		System.out.println(names.get("name").isTextual());
		
		//把jsonObject 转到jsonArray
		System.out.println("把jsonObject 转到jsonArray");
		String str = "{\"a\":{\"id\":\"1\",\"name\":\"a\"},\"b\":{\"id\":\"2\",\"name\":\"b\"}}";
		JsonNode node1 = JsonUtil.toJsonNode(str);
		ArrayNode array1 = JsonUtil.objectToArray(node1);
		System.out.println(array1.toString());
		
		//JSONArray转成JSONObject
		System.out.println("JSONArray转成JSONObject");
		ObjectNode node2 = JsonUtil.arrayToObject(array1, "id");
		System.out.println(node2.toString());
		
		//删除空节点
		String str1 = "[{\"id\":\"1\",\"name\":\"a\"},{\"id\":\"2\",\"name\":\"b\"},{\"id\":\"3\",\"name\":null}]";
		ArrayNode array2 = (ArrayNode) JsonUtil.toJsonNode(str1);
		JsonUtil.removeNull(array2);
		System.out.println(array2.toString());
		
		//判断array是否为空
		String str2 = "[]";
		ArrayNode array3 = (ArrayNode) JsonUtil.toJsonNode(str2);
		System.out.println(JsonUtil.isEmptyJsonArr(str2));
		System.out.println(JsonUtil.isEmpty(array3));
		
		String str3 = "{\"id\":\"1\",\"name\":\"a\"}";
		JsonNode node4 = JsonUtil.toJsonNode(str3);
		System.out.println("默认："+JsonUtil.getString((ObjectNode)node4,"id2","ddd"));
		
		ObjectNode node3 =new JsonUtil().getMapper().createObjectNode();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		node3.put("createTime", df.format(LocalDateTime.now()));
		System.out.println(node3.toString());
	}
}
