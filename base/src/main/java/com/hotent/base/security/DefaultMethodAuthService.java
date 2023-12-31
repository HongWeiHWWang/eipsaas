package com.hotent.base.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DefaultMethodAuthService implements MethodAuthService {
	public List<HashMap<String, String>>  getMethodAuth(){
		ArrayList<HashMap<String, String>>  result = new  ArrayList<HashMap<String, String>>();
		return result;
	}
}
