package com.hotent.uc.ws;

import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hotent.base.exception.BaseException;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.model.User;

@Service
public class WebserviceUserHandler {
	@Resource
	UserManager userManager;
	@Resource
	PasswordEncoder passwordEncoder;
	
	public void sysUserOperate(WsFacadeUser wsFacadeUser) throws Exception {
		String operateType = wsFacadeUser.getOperatetype();
		switch(operateType){
			case WsFacadeUser.OPERATE_TYPE_ADD:
				addUser(wsFacadeUser);
				break;
			case WsFacadeUser.OPERATE_TYPE_DEL:
				delUser(wsFacadeUser);
				break;
			case WsFacadeUser.OPERATE_TYPE_UPD:
				updUser(wsFacadeUser);
				break;
			case WsFacadeUser.OPERATE_TYPE_UPD_TIME:
				updUserAndTime(wsFacadeUser);
				break;
			case WsFacadeUser.OPERATE_TYPE_ADD_ORDER:
				addUserAndOrder(wsFacadeUser);
				break;
			default:
				throw new BaseException(String.format("未知的操作类型： %s", operateType));
		}
	}
	
	private void addUser(WsFacadeUser wsFacadeUser) {
		User user = convert2User(wsFacadeUser);
		userManager.create(user);
	}
	
	private void delUser(WsFacadeUser wsFacadeUser) throws Exception {
		String account = wsFacadeUser.getAccount();
		User oldUser = userManager.getByAccount(account);
		if(BeanUtils.isEmpty(oldUser)) {
			throw new BaseException(String.format("账号为：%s 的用户不存在或已经被删除.", account));
		}
		else {
			userManager.remove(oldUser.getId());
		}
	}
	
	private void updUser(WsFacadeUser wsFacadeUser) throws Exception {
		String account = wsFacadeUser.getAccount();
		User oldUser = userManager.getByAccount(account);
		if(BeanUtils.isEmpty(oldUser)) {
			throw new BaseException(String.format("账号为：%s 的用户不存在或已经被删除.", account));
		}
		else {
			oldUser.setFullname(wsFacadeUser.getFullname());
			oldUser.setEmail(wsFacadeUser.getEmail());
			oldUser.setMobile(wsFacadeUser.getMobile());
			oldUser.setUpdateTime(LocalDateTime.now());
			oldUser.setStatus(1);
			userManager.update(oldUser);
		}
	}
	
	private void updUserAndTime(WsFacadeUser wsFacadeUser) {
		throw new BaseException("接口未实现");
	}
	
	private void addUserAndOrder(WsFacadeUser wsFacadeUser) {
		throw new BaseException("接口未实现");
	}
	
	private User convert2User(WsFacadeUser wsFacadeUser) {
		User user = new User();
		user.setId(UniqueIdUtil.getSuid());
		user.setPassword(passwordEncoder.encode(WsFacadeUser.defaulPassword));
		user.setAccount(wsFacadeUser.getAccount());
		user.setEmail(wsFacadeUser.getEmail());
		user.setMobile(wsFacadeUser.getMobile());
		user.setFullname(wsFacadeUser.getFullname());
		user.setCreateTime(LocalDateTime.now());
		user.setUpdateTime(LocalDateTime.now());
		user.setFrom(User.FROM_WEBSERVICE);
		user.setStatus(1);
		user.setHasSyncToWx(User.HASSYNCTOWX_NO);
		return user;
	}
}
