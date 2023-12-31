package com.hotent.redis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 序列化工具
 * @author jason
 * @company 广州宏天软件股份有限公司
 * @email liygui@jee-soft.cn
 * @date 2020-02-20 14:20
 */
public class CustomRedisSerialize implements RedisSerializer<Object>  {
	
	/**
	 * 序列化 
	 * @param obj
	 * @return
	 */
	@Override
	public byte[] serialize(Object obj) throws SerializationException {
		if(obj == null) {
			return null;
		}
		ObjectOutputStream obi=null;
		ByteArrayOutputStream bai=null;
		try {
			bai=new ByteArrayOutputStream();
			obi=new ObjectOutputStream(bai);
			obi.writeObject(obj);
			byte[] byt=bai.toByteArray();
			return byt;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if(bytes == null) {
			return null;
		}
		ObjectInputStream oii=null;
		ByteArrayInputStream bis=null;
		bis=new ByteArrayInputStream(bytes);
		try {
			oii=new ObjectInputStream(bis);
			Object obj=oii.readObject();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}