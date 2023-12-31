package com.hotent.bpm.defxml;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


public class ContextFactory {
	private static Map<String,JAXBContext>  contexts =  Collections.synchronizedMap(new LinkedHashMap<String,JAXBContext> ());
	
	@SuppressWarnings("unchecked")
	public static JAXBContext newInstance(Class<? extends Object>... classes) throws JAXBException{
		JAXBContext jAXBContext=null;
		String newKey="";
		for(Class<? extends Object> cls:classes){
			newKey+=cls.getName()+",";
		}
		newKey=newKey.substring(0,newKey.length()-1);
		
		for(String key:contexts.keySet()){
			if(key.equals(newKey)){
				jAXBContext = contexts.get(key);
				break;
			}
		}
		if(jAXBContext==null){
			jAXBContext = JAXBContext.newInstance(classes);
			contexts.put(newKey, jAXBContext);
		}

		return jAXBContext;
	}

	
}
