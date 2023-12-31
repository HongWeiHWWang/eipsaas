package com.hotent.im.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.model.IUser;

public class ImMqttUtil {
	
	/**
	 * 生成一个默认的会话标题
	 * @param elements
	 * @return
	 */
	public static String generateSessionTile(List<IUser> userList){
		StringJoiner joiner = new StringJoiner(ImConstant.SESSION_TITLE_DELIMITER);
		int length =  userList.size() > 5 ? 5 : userList.size();
		for(int i = 0 ; i < length ; i++){
			IUser user = userList.get(i);
			joiner.add(user.getFullname());
		}
        return joiner.toString();
	}
	
	/**
	 * 生成p2p会话的编号
	 * @param userArray
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateP2PSessionCode(List<IUser> userList) throws Exception{
		List<String> list = new ArrayList<String>();
		for(IUser user : userList){
			list.add(user.getAccount());
		}
		Collections.sort(list);
		String str = String.join("",list)+ImConstant.DEFUALT_SESSION_SECRET;
		MessageDigest md5Digest = MessageDigest.getInstance("MD5");
		String newMdt5 = EncryptUtil.parseByte2HexStr(md5Digest.digest(str.getBytes()));
		return newMdt5;
	}
	
	/**
	 * 生成群组的会话编号
	 */
	public static String generateTeamSessionCode(List<IUser> userList) throws Exception{
		List<String> list = new ArrayList<String>();
		for(IUser user : userList){
			list.add(user.getAccount());
		}
		Collections.sort(list);
		String str = String.join("",list)+DateUtil.getCurrentTimeInMillis();
		MessageDigest md5Digest = MessageDigest.getInstance("MD5");
		String newMdt5 = EncryptUtil.parseByte2HexStr(md5Digest.digest(str.getBytes()));
		return newMdt5;
	}
	
	/**
	 * 生成一个uuid
	 */
	public static String generateUUID(){
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}
}
