package com.hotent.uc.service;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsFacade extends Serializable {
	
	UserDetails loadUserDetails(Collection<GrantedAuthority> authorities, Object obj) throws Exception;
}
