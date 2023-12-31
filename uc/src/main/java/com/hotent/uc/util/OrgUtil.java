package com.hotent.uc.util;

import static com.cronutils.model.field.expression.FieldExpressionFactory.always;
import static com.cronutils.model.field.expression.FieldExpressionFactory.and;
import static com.cronutils.model.field.expression.FieldExpressionFactory.every;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;
import static com.cronutils.model.field.expression.FieldExpressionFactory.questionMark;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronConstraintsFactory;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.expression.Weekdays;
import com.cronutils.model.field.value.SpecialChar;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.uc.dao.PropertiesDao;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserRelManager;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.Properties;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRel;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.group.GroupIdentity;
import com.hotent.uc.params.user.UserVo;



public class OrgUtil {
	/**
	 * 根据用户组对应id抽取用户
	 * @param type
	 * @param idStr
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<User> getUserListByTypeId(String type,String idStr) throws Exception{
		List<User> list = new ArrayList<User>();
		UserManager userManager = (UserManager) AppUtil.getBean(UserManager.class);
		if(StringUtil.isNotEmpty(idStr)){
			String[] ids = idStr.split(",");
			for (String id : ids) {
				if(StringUtil.isNotEmpty(id)){
					if(UserRel.GROUP_USER.equals(type)){
						User user = userManager.get(id);
						if(BeanUtils.isNotEmpty(user)){
							list.add(user);
						}
					}else if(UserRel.GROUP_ORG.equals(type)){
						List listOrg = userManager.getUserListByOrgId(id);
						if(BeanUtils.isNotEmpty(listOrg)){
							list.addAll(listOrg);
						}
					}else if(UserRel.GROUP_ROLE.equals(type)){
						List listRole = userManager.getUserListByRoleId(id);
						if(BeanUtils.isNotEmpty(listRole)){
							list.addAll(listRole);
						}
					}else if(UserRel.GROUP_POS.equals(type)){
						List listPos = userManager.getListByPostId(id);
						if(BeanUtils.isNotEmpty(listPos)){
							list.addAll(listPos);
						}
					}else if(UserRel.GROUP_JOB.equals(type)){
						List listJob = userManager.getListByJobId(id);
						if(BeanUtils.isNotEmpty(listJob)){
							list.addAll(listJob);
						}
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 用户列表去重复
	 * @param users
	 */
    public static void removeDuplicate(List<User> users){
    	if(BeanUtils.isNotEmpty(users)){
    		for  ( int  i  =   0 ; i  <  users.size()  -   1 ; i ++ )  {       
    			   for  ( int  j  =  users.size()  -   1 ; j  >  i; j -- )  {       
    	             if  (users.get(j).getAccount().equals(users.get(i).getAccount()))  {       
    	            	 users.remove(j);       
    	              }        
    	          }        
    	      }  
    	}
    }
    
    /**
	 * 列表根据字段去重复
     * @param <E>
	 * @param objects
	 */
    public static <E> void removeDuplicate(List<E> objects,String field){
    	if(BeanUtils.isNotEmpty(objects)){
    		for  ( int  i  =   0 ; i  <  objects.size()  -   1 ; i ++ )  {       
    			   for  ( int  j  =  objects.size()  -   1 ; j  >  i; j -- )  {       
    	             if  (getValueByKey(objects.get(j), field).equals(getValueByKey(objects.get(i),field)))  {       
    	            	 objects.remove(j);       
    	              }        
    	          }        
    	      }  
    	}
    }
    
    public static Set<GroupIdentity> convertToGroupIdentity(List<User> users) throws IOException{
    	Set<GroupIdentity> identitys = new HashSet<GroupIdentity>();
    	if(BeanUtils.isNotEmpty(users)){
    		if(users.get(0) instanceof User){
    			for (User user : users) {
            		GroupIdentity identity = new GroupIdentity();
            		identity.setId(user.getId());
            		identity.setCode(user.getAccount());
            		identity.setName(user.getFullname());
            		identity.setGroupType(GroupIdentity.TYPE_USER);
            		identitys.add(identity);
        		}
    		}else{
    			for (Object obj : users) {
					ObjectNode jsonObj = (ObjectNode) JsonUtil.toJsonNode(obj);
					GroupIdentity identity = new GroupIdentity();
            		identity.setId(jsonObj.get("id_").toString());
            		identity.setCode(jsonObj.get("account_").toString());
            		identity.setName(jsonObj.get("fullname_").toString());
            		identity.setGroupType(GroupIdentity.TYPE_USER);
            		identitys.add(identity);
				}
    		}
    		
    	}
    	return identitys;
    }
    
    /**
	 * 将User转换为UserVo
	 * @param user
	 * @return
	 */
    public static UserVo convertToUserVo(User user){
		 if(BeanUtils.isEmpty(user)){
			 return null;
		 }
		return new UserVo(user);
	}
	 
    public static List<UserVo> convertToUserVoList(List<User> users){
		 List<UserVo> list = new ArrayList<UserVo>();
		 for (User user : users) {
			 list.add(convertToUserVo(user));
		}
		 return list;
	 }
    
    /**
	 * 判断是否包含
	 * @param ids
	 * @param id
	 * @return
	 */
	public static boolean isContains(String ids,String id){
		if(StringUtil.isNotEmpty(ids)&&StringUtil.isNotEmpty(id)){
			if(ids.contains(id)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 *  键在对象中所对应得值 没有查到时返回空字符串
	 * @param obj
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getValueByKey(Object obj, String key) {
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            try {
                if (f.getName().endsWith(key)) {
                    return f.get(obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 没有查到时返回空字符串
        return "";
    }
	
	public static CommonResult<Integer> getRemovePhysiMsg(Integer num){
		return new CommonResult<Integer>(true, String.format("成功删除%s条记录", num.intValue()),num.intValue());
	}
	
	/**
	 * list数组转字符数组
	 * @param array
	 * @return
	 */
	public static String[] toStringArray(List<String> array){
		if(BeanUtils.isNotEmpty(array)){
			String[] strs = new String[array.size()];
			for (int i = 0; i < array.size(); i++) {
				strs[i] = array.get(i);
			}
			return strs;
		}
		return null;
	}
	
	
	public static QueryFilter getDataByTimeFilter(String btimeStr,String etimeStr) throws ParseException {
		QueryFilter filter = QueryFilter.build();
		LocalDateTime btime = StringUtil.isNotEmpty(btimeStr)?DateFormatUtil.parse(btimeStr):null;
		LocalDateTime etime = StringUtil.isNotEmpty(etimeStr)?DateFormatUtil.parse(etimeStr):null;
		//如果结束日期不为空并且没有填写时间，则将其设置为当天的最后时间
		if(BeanUtils.isNotEmpty(etime)&&etimeStr.indexOf(StringPool.COLON) < 0){
			etime=DateUtil.setAsEnd(etime);
		}
		if(BeanUtils.isNotEmpty(btime)&&BeanUtils.isNotEmpty(etime)){
			if(DateUtil.getTime(btime, etime)<0){
				throw new RuntimeException("开始时间不能大于结束时间！");
			}
			List<LocalDateTime> list = new ArrayList<LocalDateTime>();
			list.add(btime);
			list.add(etime);
			filter.addFilter("UPDATE_TIME_", list, QueryOP.BETWEEN, FieldRelation.AND);
		}else{
			if(BeanUtils.isNotEmpty(btime)){
				filter.addFilter("UPDATE_TIME_", btime, QueryOP.GREAT_EQUAL, FieldRelation.AND);
			}
			if(BeanUtils.isNotEmpty(etime)){
				filter.addFilter("UPDATE_TIME_", etime, QueryOP.LESS_EQUAL, FieldRelation.AND);
			}
		}
		return filter;
	}
	
	/**
	 * 封装in语句
	 * @param value
	 * @param splitStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getSubInSql(Object value,String splitStr){
		
		if (value instanceof String)
		{ // 字符串形式，通过逗号分隔
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			StringTokenizer st = new StringTokenizer(value.toString(), splitStr);
			while (st.hasMoreTokens())
			{
				sb.append("\'");
				sb.append(st.nextToken());
				sb.append("\'");
				sb.append(",");
			}
			sb = new StringBuilder(sb.substring(0, sb.length() - 1));
			sb.append(")");
			return sb.toString();
		} else if (value instanceof List)
		{ // 列表形式
			List<Object> objList = (List<Object>) value;
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for (Object obj : objList)
			{
				sb.append("\"");
				sb.append(obj.toString());
				sb.append("\"");
				sb.append(",");
			}
			sb = new StringBuilder(sb.substring(0, sb.length() - 1));
			sb.append(")");
			return sb.toString();
		} else if (value instanceof String[])
		{ // 列表形式
			String[] objList = (String[]) value;
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for (Object obj : objList)
			{
				sb.append("\"");
				sb.append(obj.toString());
				sb.append("\"");
				sb.append(",");
			}
			sb = new StringBuilder(sb.substring(0, sb.length() - 1));
			sb.append(")");
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 添加组织相关的过滤条件
	 * @param queryFilter
	 * @param exportObject
	 * @throws Exception
	 */
	public static void getOrgParams(QueryFilter queryFilter,OrgExportObject exportObject) throws Exception{
		exportObject.setBtime(null);
		exportObject.setEtime(null);
		OrgManager orgService = AppUtil.getBean(OrgManager.class);
		List<Org> orgs = orgService.getOrgByTime(exportObject);
		if(BeanUtils.isNotEmpty(orgs)){
			List<String> orgIds = new ArrayList<String>();
			for (Org org : orgs) {
				orgIds.add(org.getId());
			}
			queryFilter.addFilter("ORG_ID_", orgIds, QueryOP.IN, FieldRelation.AND);
		}
	}

	
	/**
	 * 判断用户组是否为汇报节点
	 * @param queryFilter
	 * @param exportObject
	 * @throws Exception
	 */
	public static boolean checkUserGruopIsUserRel(String type,String  value) {
        QueryFilter<UserRel> queryFilter=QueryFilter.build();
       // queryFilter.setClazz(UserRel.class);
        queryFilter.addFilter("GROUP_TYPE_", type, QueryOP.EQUAL, FieldRelation.AND);
        queryFilter.addFilter("VALUE_", value, QueryOP.EQUAL, FieldRelation.AND);
        UserRelManager userRelService = AppUtil.getBean(UserRelManager.class);
		return userRelService.query(queryFilter).getRows().size()>0;
	}
	
	public static  User getCurrentUser(){
		/*		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken))
		{
			Object details = auth.getPrincipal();
			
			if(details !=null){
				if(details instanceof UserDetails)
				{
					account = ((UserDetails) details).getUsername();
				}
			}
			UserDetailsService userDetailsService=AppUtil.getBean(UserDetailsService.class);
	    	UserDetails user = userDetailsService.loadUserByUsername(account);
	    	if(user instanceof User){
	    		User userVo =  (User)user;
	    		return userVo;
	    	}
		} */
    	return null;
    }
	
	
	
	public static String getScheduledCron(String jsonStr) throws IOException{
		String cronStr = "";
		if(StringUtil.isNotEmpty(jsonStr)){
			ObjectNode json = (ObjectNode) JsonUtil.toJsonNode(jsonStr);
			//每N分钟执行
			String time =  json.get("time").toString();
//			//日期
//			String date = json.containsKey("date")?json.("date"):"0";
//			//秒
//			String second = json.containsKey("second")?json.get("second"):"0";
			//分钟
			String minute = json.findValue("minute") != null?json.get("minute").toString():"0";
			//小时
			String hour = json.findValue("hour") != null?json.get("hour").toString():"0";
			//天
			String day = json.findValue("day") != null?json.get("day").toString():"0";
			//周
			String week = json.findValue("week") != null?json.get("week").toString():"0";
			//计划类型
			String type = json.get("triggerType").toString();
			
			CronDefinition cronDefinition = CronDefinitionBuilder.defineCron()
	        .withSeconds().and()
	        .withMinutes().and()
	        .withHours().and()
	        .withDayOfMonth().supportsL().supportsW().supportsLW().supportsQuestionMark().and()
	        .withMonth().and()
	        .withDayOfWeek().withValidRange(1, 7).withMondayDoWValue(2).supportsHash().supportsL().supportsQuestionMark().and()
	        .withCronValidation(CronConstraintsFactory.ensureEitherDayOfWeekOrDayOfMonth())
	        .instance();
	    	
			CronBuilder builder = null;
	    	
	    	switch (type) {
			//执行一次
			case "cron":
				cronStr = json.get("cron").toString();
				break;
			//每N分钟执行一次
			case "time":
				 builder = CronBuilder.cron(cronDefinition)
	            .withMonth(always())
	            .withDoW(questionMark())
	            .withDoM(always())
	            .withHour(always())
	            .withMinute(every(on(0), Integer.valueOf(time)))
	            .withSecond(on(0));
				break;
			//每天N时执行
			case "day":
				builder = CronBuilder.cron(cronDefinition)
	            .withMonth(always())
	            .withDoW(questionMark())
	            .withDoM(always())
	            .withHour(on(Integer.valueOf(hour)))
	            .withMinute(on(Integer.valueOf(minute)))
	            .withSecond(on(0));
				break;
			//每周每周N执行
			case "week":
				builder = CronBuilder.cron(cronDefinition)
	            .withMonth(always())
	            .withDoW(and(buildWeekdays(week)))
	            .withDoM(questionMark())
	            .withHour(on(Integer.valueOf(hour)))
	            .withMinute(on(Integer.valueOf(minute)))
	            .withSecond(on(0));
				break;
			//每月的某些天执行
			case "month":
				 builder = CronBuilder.cron(cronDefinition)
	            .withMonth(always())
	            .withDoW(questionMark())
	            .withDoM(and(builddays(day)))
	            .withHour(on(Integer.valueOf(hour)))
	            .withMinute(on(Integer.valueOf(minute)))
	            .withSecond(on(0));
				break;
			default:
				break;
			}
	    	
	    	if(BeanUtils.isNotEmpty(builder)){
	    		Cron cron = builder.instance();
		        cronStr = cron.asString();
		        if("week".equals(type)){
		        	cronStr = transCronWeek(cronStr);
		        }
	    	}
		}
		return cronStr;
	}
	
	private static String transCronWeek(String cronStr){
		String[] strs = cronStr.split(" ");
		String weekStr = strs[strs.length-1];
		String[] weeks = weekStr.split(",");
		StringBuilder weekBuilder = new StringBuilder();
		for (String week : weeks) {
			switch (week) {
			case "1":
				weekBuilder.append(",MON");
				break;
			case "2":
				weekBuilder.append(",TUE");
				break;
			case "3":
				weekBuilder.append(",WED");
				break;
			case "4":
				weekBuilder.append(",THU");
				break;
			case "5":
				weekBuilder.append(",FRI");
				break;
			case "6":
				weekBuilder.append(",SAT");
				break;
			case "7":
				weekBuilder.append(",SUN");
				break;
			default:
				break;
			}
		}
		String weekBuilderStr = weekBuilder.toString();
		if(weekBuilderStr.startsWith(",")){
			weekBuilderStr = weekBuilderStr.replaceFirst(",", "");
			cronStr = cronStr.replace(weekStr, weekBuilderStr);
		}
		return cronStr;
	}
	
	private static List<FieldExpression> buildWeekdays(String str) throws IOException{
		List<FieldExpression> list = new ArrayList<FieldExpression>();
		ArrayNode split = (ArrayNode) JsonUtil.toJsonNode(str);
		for (Object object : split) {
			switch (object.toString()) {
			case "MON":
				list.add(on(Weekdays.MONDAY.getWeekday()));
				break;
			case "TUE":
				list.add(on(Weekdays.TUESDAY.getWeekday()));
				break;
			case "WED":
				list.add(on(Weekdays.WEDNESDAY.getWeekday()));
				break;
			case "THU":
				list.add(on(Weekdays.THURSDAY.getWeekday()));
				break;
			case "FRI":
				list.add(on(Weekdays.FRIDAY.getWeekday()));
				break;
			case "SAT":
				list.add(on(Weekdays.SATURDAY.getWeekday()));
				break;
			case "SUN":
				list.add(on(Weekdays.SUNDAY.getWeekday()));
				break;
			default:
				break;
			}
		}
		return list;
	}
	
	private static List<FieldExpression> builddays(String str) throws IOException{
		List<FieldExpression> list = new ArrayList<FieldExpression>();
		ArrayNode split = (ArrayNode) JsonUtil.toJsonNode(str);
		for (Object object : split) {
			if(!"L".equals(object.toString())){
				list.add(on(Integer.valueOf(object.toString())));
			}else{
				list.add(on(SpecialChar.L));
			}
		}
		return list;
	}
	
	
	public static String getScheduledCronLog(String jsonStr) throws IOException{
		String cronStr = "";
		if(StringUtil.isNotEmpty(jsonStr)){
			ObjectNode json = (ObjectNode) JsonUtil.toJsonNode(jsonStr);
			//每N分钟执行
			String time =  json.get("time").toString();
//			//日期
//			String date = json.containsKey("date")?json.get("date"):"0";
//			//秒
//			String second = json.containsKey("second")?json.get("second"):"0";
			//分钟
			String minute = json.findValue("minute") != null?json.get("minute").toString():"0";
			//小时
			String hour = json.findValue("hour") != null?json.get("hour").toString():"0";
			//天
			String day = json.findValue("day") != null?json.get("day").toString():"0";
			//周
			String week = json.findValue("week") != null?json.get("week").toString():"0";
			//计划类型
			String type = json.get("triggerType").toString();
			
	    	switch (type) {
			//执行一次
			case "cron":
				cronStr = "Cron表达式【"+json.get("cron")+"】";
				break;
			//每N分钟执行一次
			case "time":
				cronStr = "每"+time+"分钟执行一次";
				break;
			//每天N时执行
			case "day":
				cronStr = "每天"+hour+"时"+minute+"分执行";
				break;
			//每周每周N执行
			case "week":
				cronStr = "每"+buildWeekdaysLog(week)+"的"+hour+"时"+minute+"分执行";
				break;
			//每月的某些天执行
			case "month":
				cronStr = "每月"+builddaysLog(day)+"的"+hour+"时"+minute+"分执行";
				break;
			default:
				break;
			}
		}
		return cronStr;
	}
	
	private static String buildWeekdaysLog(String str) throws IOException{
		StringBuilder weeks = new StringBuilder();
		ArrayNode split = (ArrayNode) JsonUtil.toJsonNode(str);
		for (Object object : split) {
			switch (object.toString()) {
			case "MON":
				weeks.append("周一，");
				break;
			case "TUE":
				weeks.append("周二，");
				break;
			case "WED":
				weeks.append("周三，");
				break;
			case "THU":
				weeks.append("周四，");
				break;
			case "FRI":
				weeks.append("周五，");
				break;
			case "SAT":
				weeks.append("周六，");
				break;
			case "SUN":
				weeks.append("周日，");
				break;
			default:
				break;
			}
		}
		return weeks.toString();
	}
	
	private static String builddaysLog(String str) throws IOException{
		StringBuilder days = new StringBuilder();
		ArrayNode split = (ArrayNode) JsonUtil.toJsonNode(str);
		for (Object object : split) {
			if(!"L".equals(object.toString())){
				days.append(object.toString()+"日，");
			}else{
				days.append("最后一天，");
			}
		}
		return days.toString();
	}
	
	
	/** 
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
     * 参考文章： http://developer.51cto.com/art/201111/305181.htm 
     *  
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
     *  
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
     * 192.168.1.100 
     *  
     * 用户真实IP为： 192.168.1.110 
     *  
     * @param request 
     * @return 
     */  
    public static String getIpAddress(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
    
    public static String getDbCronByCode(String code) throws IOException{
    	String cron = "";
    	PropertiesDao propertiesDao = AppUtil.getBean(PropertiesDao.class);
    	Properties properties = propertiesDao.getByCode(code);
    	if(BeanUtils.isNotEmpty(properties)){
    		cron = getScheduledCron(properties.getValue());
    	}
    	return cron;
    }
    
    /**
     * 处理有父子关系的权限组织根目录
     * @param list
     */
    public static void dealRepeatAuthRoot(List<Org> list){
    	if(BeanUtils.isNotEmpty(list)&&list.size()>1){
    		for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {       
 			   for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
 				   String jPath = list.get(j).getPath();
 				   String iPath = list.get(i).getPath();
 	             if  (jPath.startsWith(iPath))  {       
 	            	list.remove(j);       
 	              }else if(iPath.startsWith(jPath)) {
 	            	 list.remove(i);      
 	              }
 	          }        
 	      } 
    	}
    }
}

