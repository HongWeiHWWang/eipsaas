package com.hotent.uc.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.PinyinUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.ZipUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.uc.dao.DemensionDao;
import com.hotent.uc.dao.OrgDao;
import com.hotent.uc.dao.OrgPostDao;
import com.hotent.uc.dao.OrgUserDao;
import com.hotent.uc.dao.UserDao;
import com.hotent.uc.dao.UserParamsDao;
import com.hotent.uc.dao.UserRoleDao;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OperateLogManager;
import com.hotent.uc.manager.OrgAuthManager;
import com.hotent.uc.manager.OrgJobManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.OrgPostManager;
import com.hotent.uc.manager.OrgUserManager;
import com.hotent.uc.manager.ParamsManager;
import com.hotent.uc.manager.PropertiesService;
import com.hotent.uc.manager.RoleManager;
import com.hotent.uc.manager.UserGroupManager;
import com.hotent.uc.manager.UserImportManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserParamsManager;
import com.hotent.uc.manager.UserRoleManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.OperateLog;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRole;
import com.hotent.uc.params.org.OaAsyncObject;
import com.hotent.uc.params.user.UserImportVo;
import com.hotent.uc.util.OperateLogUtil;

/**
 * 
 * <pre> 
 * 描述：用户表 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:50
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class UserImportManagerImpl implements UserImportManager{
	protected static Logger logger = LoggerFactory.getLogger(UserImportManagerImpl.class);
	//导入数据code重复时给code加的后缀
	protected static String IMPORT_NEW_SUFFIX = "_imp";

	@Autowired
	private UserDao userDao;
	@Autowired
	OrgUserDao orgUserDao;
	@Autowired
	OrgManager orgService;
	@Autowired
	OrgPostManager orgPostService;
	@Autowired
	OrgJobManager orgJobService;
	@Autowired
	DemensionManager demensionService;
	@Autowired
	OrgUserManager orgUserService;
	/*@Autowired
    LdapUserService ldapUserService;*/
	@Autowired
	PropertiesService propertiesService;
	@Autowired
	UserRoleManager userRoleService;
	@Autowired
	UserParamsManager userParamsService;
	@Autowired
	UserGroupManager userGroupService;
	@Autowired
	RoleManager roleService;
	@Autowired
	OrgAuthManager orgAuthService;
	@Autowired
	ParamsManager paramsService;
	@Autowired
	UserManager userService;
	@Autowired
	DemensionDao demensionDao;
	@Autowired
	OperateLogManager operateLogService;


    @Transactional
	public Map<String, Object> importUser(MultipartFile file, String demId)
			throws Exception {

		Map<String,Object> rtnMap = new HashMap<String, Object>();
		Boolean result = true;
		String preCode = "";
		String console = "";
		String reqUrl = "/api/user/v1/users/importExcelUser";
		Demension demension = demensionService.get(demId);
		if(BeanUtils.isEmpty(demension)){
			console = "未选择导入维度";
			throw new RuntimeException(console);
		}
		preCode = demension.getDemCode();
		ArrayNode logArray = JsonUtil.getMapper().createArrayNode();
		if(file == null || file.isEmpty()){
			result = false;
			console = "文件为空！";
			throw new RuntimeException(console);
		}
		
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
		if(!fileExt.toUpperCase().equals(".XLS") && !fileExt.toUpperCase().equals(".XLSX")){
			result = false;
			console = "上传文件不是excel类型！";
			throw new RuntimeException(console);
		}
		
		Boolean isExcel2003 = true;
		if(fileExt.toUpperCase().equals(".XLSX")){
			isExcel2003 = false;
		}
		Workbook wb = null;
		if(isExcel2003){
			 wb = new HSSFWorkbook(file.getInputStream());
		}else{
			wb = new XSSFWorkbook(file.getInputStream());
		}
		
		
		
		//新增用户、组织列表
		List<Org> orgList = new ArrayList<Org>();
		List<User> userList = new ArrayList<User>();
		Map<String,User> updUserList = new HashMap<String,User>();//更新用户组
		List<OrgUser> orgUserList = new ArrayList<OrgUser>();//用户组织关系
		List<OrgPost> orgPostList = new ArrayList<OrgPost>();//岗位列表
		List<OrgJob> orgJobList = new ArrayList<OrgJob>();//职务列表
		Map<String,String> orgMap = new HashMap<String,String>();
		Map<String,String> orgPostAddMap = new HashMap<String,String>();
		Map<String,OrgPost> orgPostMapList = new HashMap<String,OrgPost>();//从待添加的数据中获取用
		Map<String,String> userMap = new HashMap<String,String>();
		Map<String,String> userAccountMap = new HashMap<String,String>();
		Map<String,String> isMasterMap = new HashMap<String,String>();
		
		int sheets = wb.getNumberOfSheets();
		logger.error("开始导入");
		for(int s=0;s<sheets;s++){
			try {
				Sheet sheet = wb.getSheetAt(s);
				if(sheet.getPhysicalNumberOfRows() < 2){
					doLog(false, reqUrl, "",  "sheet"+(s+1)+"没有填写数据!", true, true);
					recordLog(logArray,"Excel表格sheet"+(s+1)+"没有填写数据!",null);
					System.out.println("sheet"+(s+1)+"没有填写数据!");
					continue;
				}
				Map<String,Integer> headMap = new HashMap<String,Integer>();
				for(int i=0;i<sheet.getRow(0).getLastCellNum();i++){
					Row row = sheet.getRow(0);
					headMap.put(row.getCell(i).getStringCellValue().trim(), i);
				}
				int begin = sheet.getFirstRowNum();
				int end = sheet.getLastRowNum();
				//处理excel表中的数据
				for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
					try {
						Row row = sheet.getRow(i);
						if(BeanUtils.isEmpty(row)){
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行数据为空，未导入",null);
							logger.error("Excel表格sheet"+(s+1)+"第"+(i+1)+"行数据为空，未导入");
							continue;
						}
						Org currentOrg = null;//当前行所指向的组织
						
						//用户信息
						if(BeanUtils.isEmpty(headMap.get("帐号"))){
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号为空，未导入",null);
							continue;
						}
						Cell accountCell = row.getCell(headMap.get("帐号"));
						if(BeanUtils.isEmpty(accountCell)||StringUtil.isEmpty(accountCell.getStringCellValue())){
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号为空，未导入",null);
							continue;
						}
						String userAccount = accountCell.getStringCellValue().trim().toLowerCase();
						if(userAccount.trim().length()>30){
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号长度大于30，未导入",userAccount);
							continue;
						}
						String userName = "";//姓名
						if(headMap.get("姓名")!=null&&BeanUtils.isNotEmpty(row.getCell(headMap.get("姓名")))
								&&StringUtil.isNotEmpty(row.getCell(headMap.get("姓名")).getStringCellValue())){
							userName = row.getCell(headMap.get("姓名")).getStringCellValue().trim();
						}else{
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行姓名为空，未导入",null);
							continue;
						}
						if(userName.trim().length()>30){
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行姓名长度大于30，未导入",userName);
							continue;
						}
						
						if(!checkSameUser(userAccount,userName,userMap)){
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号已存在",userAccount);
							logger.error("Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号已存在。");
							continue;
						}
						// 检测数据库中该帐号已被逻辑删除的用户是否存在，若存在，这提示，不能导入
						User delUser = userDao.getDelDataByAccount(userAccount);
						if(BeanUtils.isNotEmpty(delUser)){
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号在数据库中已被逻辑删除，若要添加此帐号用户，请先物理删除此数据",userAccount);
							logger.error("Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号在数据库中已被逻辑删除，若要添加此帐号用户，请先物理删除此数据。");
							continue;
						}
						
						if(BeanUtils.isNotEmpty(headMap.get("组织单元名称"))&&BeanUtils.isNotEmpty(row.getCell(headMap.get("组织单元名称")))){
							String orgInfo = row.getCell(headMap.get("组织单元名称")).getStringCellValue().trim();//组织单元名
							int subIndex = 1;
							if(!orgInfo.startsWith("/")){
								subIndex = 0;
							}
							String[] orgNameArr = orgInfo.substring(subIndex, orgInfo.length()).split("/");
							Map<String,String> pathNameMap = new HashMap<String,String>();
							for(int j = 0;j<=orgNameArr.length-1;j++){
								if(j == 0){
									pathNameMap.put("父级路径", "");
									pathNameMap.put("子级路径", "/"+orgNameArr[j]);
								}else{
									pathNameMap.put("父级路径", pathNameMap.get("子级路径"));
									pathNameMap.put("子级路径", pathNameMap.get("子级路径")+"/"+orgNameArr[j]);
								}
								List<Org> selectOrgList = orgService.getByPathName(pathNameMap.get("子级路径"));
								Org nowOrg = null;
								if(BeanUtils.isNotEmpty(selectOrgList)){
									for (Org org : selectOrgList) {
										if(demId.equals(org.getDemId())){
											nowOrg = org;
											break;
										}
									}
								}
								if(BeanUtils.isEmpty(nowOrg)){//为空，考虑新增组织
									if(j==0){
										Org org = new Org();
										org.setId(UniqueIdUtil.getSuid());
										org.setName(orgNameArr[j]);
										org.setDemId(demId);
										org.setParentId("0");
										org.setPathName(pathNameMap.get("子级路径"));
										org.setPath(demId+"."+org.getId()+".");
										org.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(orgNameArr[j]).replace("-", "_"));
										currentOrg = dealOrg(org,orgList,preCode,orgMap);
									}else{
										currentOrg = dealOrgUnder(pathNameMap.get("父级路径"),pathNameMap.get("子级路径"),orgList,demId,preCode,orgMap);
									}
								}else{
									currentOrg = nowOrg;
								}
							}
						}
						
						String sex = "";
						if(headMap.get("性别")!=null&&BeanUtils.isNotEmpty(row.getCell(headMap.get("性别")))
								&&StringUtil.isNotEmpty(row.getCell(headMap.get("性别")).getStringCellValue())){
							sex = row.getCell(headMap.get("性别")).getStringCellValue().trim();
							if(StringUtil.isNotEmpty(sex)&&!"男".equals(sex)&&!"女".equals(sex)&&!"未知".equals(sex)){
								sex = "未知";
							}
						}
						String userStatus = "";//员工状态
						if(headMap.get("员工状态")!=null&&BeanUtils.isNotEmpty(row.getCell(headMap.get("员工状态")))){
							row.getCell(headMap.get("员工状态")).setCellType(Cell.CELL_TYPE_STRING);
							userStatus = row.getCell(headMap.get("员工状态")).getStringCellValue().trim();
						}
						
						
						String email = "";
						if(headMap.get("邮箱")!=null&&row.getCell(headMap.get("邮箱")) != null){
							row.getCell(headMap.get("邮箱")).setCellType(Cell.CELL_TYPE_STRING);
							email = row.getCell(headMap.get("邮箱")).getStringCellValue();
							if(StringUtil.isNotEmpty(email)){
								email = email.toLowerCase();
							}
							if(!checkEmail(email)){
								recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行邮箱格式不正确，未导入邮箱",email);
								email = "";
							}
						}
						
						String mobile = "";
						
						if(headMap.get("手机号码")!=null&&row.getCell(headMap.get("手机号码")) != null){
							row.getCell(headMap.get("手机号码")).setCellType(Cell.CELL_TYPE_STRING);
							mobile = row.getCell(headMap.get("手机号码")).getStringCellValue();
							if(StringUtil.isNotEmpty(mobile))
							if(!(org.apache.commons.lang.StringUtils.isNumeric(mobile)&&mobile.trim().length()==11)){
								recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行手机号码格式不正确，未导入手机号码",mobile);
								mobile = "";
							}
						}
						
						String isMaster = "";//主组织 ：“1”或者“是”为主组织 其他为非主组织
						if(headMap.get("是否主组织")!=null&&row.getCell(headMap.get("是否主组织")) != null){
							row.getCell(headMap.get("是否主组织")).setCellType(Cell.CELL_TYPE_STRING);
							isMaster = row.getCell(headMap.get("是否主组织")).getStringCellValue();
						}
						
						String isCharge = "";//负责人：0或空为非负责人；“1”为负责人；“2”为主负责人
						if(headMap.get("部门负责人")!=null&&row.getCell(headMap.get("部门负责人")) != null){
							row.getCell(headMap.get("部门负责人")).setCellType(Cell.CELL_TYPE_STRING);
							isCharge = row.getCell(headMap.get("部门负责人")).getStringCellValue();
						}
						
						String address = "";
						if(headMap.get("地址")!=null&&row.getCell(headMap.get("地址")) != null){
							address = row.getCell(headMap.get("地址")).getStringCellValue();
						}
						
						String userNumber = "";//员工编号
						if(headMap.get("员工编号")!=null&&row.getCell(headMap.get("员工编号")) != null){
							row.getCell(headMap.get("员工编号")).setCellType(Cell.CELL_TYPE_STRING);
							userNumber = row.getCell(headMap.get("员工编号")).getStringCellValue().trim();
						}
//						else{
//							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行员工编号为空，未导入",null);
//							continue;
//						}
//						if(!checkSameUserNumber(userNumber,userAccount,userAccountMap)){
//							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行员工编号已存在。",null);
//							continue;
//						}
						
						if(userNumber.trim().length()>30){
							recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行员工编号长度大于30，未导入",userNumber);
							continue;
						}
						
						String education = "";//学历
						if(headMap.get("学历")!=null&&row.getCell(headMap.get("学历")) != null){
							row.getCell(headMap.get("学历")).setCellType(Cell.CELL_TYPE_STRING);
							education = row.getCell(headMap.get("学历")).getStringCellValue().trim();
						}
						
						String idCard = "";//身份证号
						if(headMap.get("身份证号")!=null&&row.getCell(headMap.get("身份证号")) != null){
							row.getCell(headMap.get("身份证号")).setCellType(Cell.CELL_TYPE_STRING);
							idCard = row.getCell(headMap.get("身份证号")).getStringCellValue().trim();
						}
						
						//生日
						Date birth = null;
						if(headMap.get("生日")!=null&&row.getCell(headMap.get("生日"))!=null){
							if(row.getCell(headMap.get("生日")).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){//数字类型日期
								birth = HSSFDateUtil.getJavaDate(row.getCell(headMap.get("生日")).getNumericCellValue());
							}else if(row.getCell(headMap.get("生日")).getCellType() == HSSFCell.CELL_TYPE_STRING){//字符串类型
								String birthday = row.getCell(headMap.get("生日")).getStringCellValue().toString().trim();//生日
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								birth = sdf.parse(birthday);
							}
						}
						
						//入职日期
						Date entryDate = null;
						if(headMap.get("入职日期")!=null&&row.getCell(headMap.get("入职日期"))!=null){
							if(row.getCell(headMap.get("入职日期")).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){//数字类型日期
								entryDate = HSSFDateUtil.getJavaDate(row.getCell(headMap.get("入职日期")).getNumericCellValue());
							}else if(row.getCell(headMap.get("入职日期")).getCellType() == HSSFCell.CELL_TYPE_STRING){//字符串类型
								String entryDay = row.getCell(headMap.get("入职日期")).getStringCellValue().toString().trim();//生日
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								entryDate =sdf.parse(entryDay);
							}
						}
						
						String phone = "";
						if(headMap.get("电话")!=null&&row.getCell(headMap.get("电话")) != null){
							row.getCell(headMap.get("电话")).setCellType(Cell.CELL_TYPE_STRING);
							phone = row.getCell(headMap.get("电话")).getStringCellValue();
						}
						
						//岗位和职务导入规则：一一对应导入，多个用;号隔开，如果没有职务，则不导入岗位，如果有职务，则对应顺序以岗位为准（第一个岗位对应第一个职务，以此类推）(如果岗位有多个，职务只有一个时，导入同一个职务中)
						//职务  如果此行数据存在岗位，那么职务也一定是存在的，因为岗位必须与职务关联，若只有岗位而无职务，那必是人为破坏
						List<OrgJob> currentReldefs = null;
						if(currentOrg!=null&&headMap.get("职务")!=null&&row.getCell(headMap.get("职务")) != null){
							currentReldefs = dealOrgJob(row.getCell(headMap.get("职务")).getStringCellValue(),orgJobList,preCode);
						}
						
						//岗位   
						List<OrgPost> rels = new ArrayList<OrgPost>();
						if(currentOrg!=null&&headMap.get("岗位")!=null&&row.getCell(headMap.get("岗位")) != null && BeanUtils.isNotEmpty(currentReldefs) && StringUtil.isNotEmpty(row.getCell(headMap.get("岗位")).getStringCellValue())){//在职务存在时，才处理岗位
							String relNameStr = row.getCell(headMap.get("岗位")).getStringCellValue();
							String[] relNames = relNameStr.split(";");
							boolean isOneRelDef = currentReldefs.size()==1?true:false;
							for (int j = 0; j < relNames.length; j++) {
								String relId = UniqueIdUtil.getSuid();
								String relCode = PinyinUtil.getPinYinHeadChar(relNames[j]);
								OrgPost rel = orgPostService.getByCode(currentOrg.getCode()+"_"+relCode);
								if(BeanUtils.isNotEmpty(rel)&&!rel.getName().equals(relNames[j])){
									rel = getByRelNameAndCode(relNames[j],currentOrg.getCode()+"_"+relCode);
									if(BeanUtils.isEmpty(rel)){
										relCode += "_"+relId.substring(relId.length()-3);
									}
								}
								OrgJob orgJob = isOneRelDef?currentReldefs.get(0):currentReldefs.get(j);
								if(isOneRelDef||(j<currentReldefs.size()&&BeanUtils.isNotEmpty(orgJob))){
									if(BeanUtils.isEmpty(rel)){
										rel = new OrgPost();
										rel.setId(relId);
										rel.setRelDefId(orgJob.getId());
										rel.setOrgId(currentOrg.getId());
										rel.setCode(relCode);
										rel.setName(relNames[j]);
										String code = currentOrg.getCode()+"_"+rel.getCode();
										OrgPost rel2 = orgPostMapList.get(code);//从待添加的数据中获取
										if(BeanUtils.isEmpty(rel2)){
											rels.add(rel);
											orgPostAddMap.put(rel.getId(), code);
											orgPostMapList.put(code, rel);
										}else{
											if(!rel.getName().equals(rel2.getName())){
												relCode += "_"+relId.substring(relId.length()-3);
												rel.setCode(relCode);
												rels.add(rel);
												orgPostAddMap.put(rel.getId(), code);
												orgPostMapList.put(code, rel);
											}else{
												rel2.setCode(code);
												rels.add(rel2);
											}
										}
									}else{
										rels.add(rel);
									}
								}
							}
						}
						
						User user = userDao.getByAccount(userAccount);
						if(BeanUtils.isEmpty(user)){
							user = findUserFromNews(userList, userAccount);
							if(BeanUtils.isEmpty(user)){
								user = new User();
								user.setAccount(userAccount.toLowerCase());
								user.setFullname(userName);
								user.setAddress(address);
								user.setSex(sex);
								//user.setStaffStatus(userStatus);
								if("在职".equals(userStatus)||StringUtil.isEmpty(userStatus)){
									user.setStatus(User.STATUS_NORMAL);
								}else if("离职".equals(userStatus)){
									user.setStatus(User.STATUS_LEAVE);
								}else if("未激活".equals(userStatus)){
									user.setStatus(User.STATUS_NOT_ACTIVE);
								}else{
									user.setStatus(User.STATUS_DISABLED);
								}
								user.setEmail(email);
								user.setMobile(mobile);
								user.setPhone(phone);
								if(BeanUtils.isNotEmpty(birth)){
									user.setBirthday(DateFormatUtil.parse(birth).toLocalDate() );
								}
								if(BeanUtils.isNotEmpty(entryDate)){
									user.setEntryDate(DateFormatUtil.parse(entryDate).toLocalDate() );
								}
								user.setEducation(education);
								user.setIdCard(idCard);
								user.setPassword(EncryptUtil.encryptSha256("123456"));
								user.setFrom(User.FROM_EXCEL);
//								if(StringUtil.isNotEmpty(userNumber)){
//									if(BeanUtils.isEmpty(userService.getByNumber(userNumber))){
//										user.setUserNumber(userNumber);
//									}else{
//										recordLog(logArray,"人员【"+userAccount+"】员工编码设置失败：已存在，请在系统中重新设置。",user);
//									}
//								}
								user.setCreateTime(LocalDateTime.now());
							}
						}else{
							if(!user.getFullname().equals(userName)){
								recordLog(logArray,"Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号已存在",userName);
								logger.error("Excel表格sheet"+(s+1)+"第"+(i+1)+"行帐号已存在。");
							}else{//更新用户
								user.setFullname(userName);
								user.setAddress(address);
								user.setSex(sex);
								if("在职".equals(userStatus)||StringUtil.isEmpty(userStatus)){
									user.setStatus(1);
								}else if("离职".equals(userStatus)){
									user.setStatus(User.STATUS_LEAVE);
								}else if("未激活".equals(userStatus)){
									user.setStatus(User.STATUS_NOT_ACTIVE);
								}else{
									user.setStatus(User.STATUS_DISABLED);
								}
								user.setEmail(email);
								user.setMobile(mobile);
								user.setPhone(phone);
								user.setBirthday(DateFormatUtil.parse(birth).toLocalDate());
								user.setEntryDate(DateFormatUtil.parse(entryDate).toLocalDate());
								user.setEducation(education);
								user.setIdCard(idCard);
//								if(StringUtil.isNotEmpty(userNumber)){
//									User us = userService.getByNumber(userNumber);
//									if(BeanUtils.isEmpty(us)){
//										user.setUserNumber(userNumber);
//									}else{
//										if(!us.getAccount().equals(user.getAccount())){
//											recordLog(logArray,"人员【"+userAccount+"】员工编码设置失败：已存在。",user);
//										}
//									}
//								}
								updUserList.put(user.getAccount(), user);
							}
						}
						try {
							user = dealUser(user,userList);
							userMap.put(user.getAccount(), user.getFullname());
							userAccountMap.put(user.getUserNumber(), user.getAccount());
						} catch (Exception e) {e.printStackTrace();}
						
						//用户、组织、岗位关系处理
						try {
							dealUserOrgPost(user,currentOrg,rels,orgUserList,orgPostList,orgPostAddMap,isMaster,isCharge,isMasterMap,demension.getId());
						} catch (Exception e) {}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		orgPostMapList = null;
		orgMap = null;
		//记录新增失败的组织编号
		Set<String> failedOrgs = new HashSet<String>();
		//新增用户
		for(User u : userList){
			try {
				u.setCreateTime(LocalDateTime.now());
				userService.create(u);
				//给新增用户添加默认角色
				bindRole(u);
				doLog(true, reqUrl, u, "", true, true);
				logger.error("导入人员【"+u.getAccount()+"】成功！");
			} catch (Exception e) {
				recordLog(logArray,"导入人员【"+u.getAccount()+"】失败："+e.getMessage(),u);
				logger.error("导入人员【"+u.getAccount()+"】失败："+e.getMessage());
			}
			
		}
		//新增组织
		for(Org o : orgList){
			try {
				if(BeanUtils.isEmpty(orgService.getByCode(o.getCode()))){
					if(!failedOrgs.contains(o.getParentId())){
						orgService.create(o);
						doLog(true, reqUrl, o, "", true, true);
						logger.error("导入组织【"+o.getName()+"】成功！");
					}else{
						failedOrgs.add(o.getId());
						doLog(false, reqUrl, o, "导入组织【"+o.getName()+"】失败（该组织的父组织导入失败）。", true, true);
						logger.error("导入组织【"+o.getName()+"】失败（该组织的父组织导入失败）。");
					}
				}else{
					failedOrgs.add(o.getId());
					recordLog(logArray,"导入组织【"+o.getName()+"】失败，该组织编号【"+o.getCode()+"】已存在",o);
					logger.error("导入组织【"+o.getName()+"】失败，该组织编号【"+o.getCode()+"】已存在！");
				}
			} catch (Exception e) {
				failedOrgs.add(o.getId());
				recordLog(logArray,"导入组织【"+o.getName()+"】失败："+e.getMessage(),o);
				logger.error("导入组织【"+o.getName()+"："+JsonUtil.toJsonNode(o)+"】失败："+e.getMessage());
			}
		}
		
		//更新用户
		for(User u : updUserList.values()){
			try {
				userService.update(u);
				// 绑定默认角色
	            bindRole(u);
	            doLog(true, reqUrl,u, "", true, false);
				logger.error("更新人员【"+u.getAccount()+"】成功！");
			} catch (Exception e) {
				recordLog(logArray,"更新用户【"+u.getAccount()+"】失败："+e.getMessage(),u);
				logger.error("更新用户【"+u.getAccount()+"】失败："+e.getMessage());
			}
		}
		
		//新增岗位
		for(OrgPost rel :orgPostList){
			try {
				if(BeanUtils.isEmpty(orgPostService.getByCode(rel.getCode()))){
					if(!failedOrgs.contains(rel.getOrgId())){
						orgPostService.create(rel);
						doLog(true, reqUrl,rel, "", true, true);
						logger.error("导入岗位【"+rel.getName()+"】成功！");
					}else{
						doLog(false, reqUrl,rel, "导入岗位【"+rel.getName()+"】失败（该岗位所在组织导入失败）！", true, true);
						logger.error("导入岗位【"+rel.getName()+"】失败（该岗位所在组织导入失败）！");
					}
				}else{
					recordLog(logArray,"新增岗位【"+rel.getName()+"】失败，该岗位编号【"+rel.getCode()+"】已存在",rel);
					logger.error("新增岗位【"+rel.getName()+"】失败，该岗位编号【"+rel.getCode()+"】已存在！");
				}
			} catch (Exception e) {
				recordLog(logArray,"新增岗位【"+rel.getName()+"】失败："+e.getMessage(),rel);
				logger.error("新增岗位【"+rel.getName()+"："+JsonUtil.toJsonNode(rel)+"】失败："+e.getMessage());
			}
		}
		
		//新增职务
		for(OrgJob reldef:orgJobList){
			try {
				orgJobService.create(reldef);
				doLog(true, reqUrl,reldef, "", true, true);
				logger.error("导入职务【"+reldef.getName()+"】成功！");
			} catch (Exception e) {
				recordLog(logArray,"新增职务【"+reldef.getName()+"】失败："+e.getMessage(),reldef);
				logger.error("新增职务【"+reldef.getName()+"："+JsonUtil.toJsonNode(reldef)+"】失败："+e.getMessage());
			}
		}
		
		//用户组织关系
		for(OrgUser orgUser :orgUserList){
			try {
				if(!failedOrgs.contains(orgUser.getOrgId())){
					orgUserService.create(orgUser);
					doLog(true, reqUrl,orgUser, "", true, true);
					logger.error("新增用户组织关系【"+JsonUtil.toJsonNode(orgUser)+"】成功！");
				}else{
					doLog(false, reqUrl,orgUser, "新增用户组织关系失败（组织导入失败）！", true, true);
					logger.error("新增用户组织关系【"+JsonUtil.toJsonNode(orgUser)+"】失败（组织导入失败）！");
				}
			} catch (Exception e) {
				recordLog(logArray,"新增用户组织关系【"+JsonUtil.toJsonNode(orgUser)+"】失败："+e.getMessage(),orgUser);
				logger.error("新增用户组织关系【"+JsonUtil.toJsonNode(orgUser)+"】失败："+e.getMessage());
			}
		}
		if(BeanUtils.isEmpty(userList)&&BeanUtils.isEmpty(orgList)&&BeanUtils.isEmpty(updUserList)
			&&BeanUtils.isEmpty(orgPostList)&&BeanUtils.isEmpty(orgJobList)&&BeanUtils.isEmpty(orgUserList)){
			result = false;
			console = "未导入或更新任何用户、组织信息！";
		}
		logger.error("导入完成");
		rtnMap.put("result", result);
		rtnMap.put("console", console);
		rtnMap.put("log", logArray);
		return rtnMap;
	}
	
	/**
	 * 检测数据库中和待导入的数据中是否存在账号相同姓名不同的用户，如果存在，则不导入用户和用户的其他信息
	 * @param account
	 * @param name
	 * @param userMap
	 * @return
	 */
	private boolean checkSameUser(String account,String name,Map<String,String> userMap){
		String mapName = userMap.get(account);
		if(StringUtil.isNotEmpty(mapName)&&!mapName.equals(name)){
			return false;
		}
		User user = userDao.getByAccount(account);
		if(BeanUtils.isNotEmpty(user)&&!user.getFullname().equals(name)){
			return false;
		}
		return true;
	}
	
	/**
	 * 检测数据库中和待导入的数据中是否存在员工编号相同帐号不同的用户，如果存在，则不导入用户和用户的其他信息
	 * @param userNumber
	 * @param account
	 * @param userAccountMap
	 * @return
	 */
	private boolean checkSameUserNumber(String userNumber,String account,Map<String,String> userAccountMap){
		String mapAccount = userAccountMap.get(userNumber);
		if(StringUtil.isNotEmpty(mapAccount)&&!mapAccount.equals(account)){
			return false;
		}
		User user = userDao.getByNumber(userNumber);
		if(BeanUtils.isNotEmpty(user)&&!user.getAccount().equals(account)){
			return false;
		}
		return true;
	}
	
	/**
	 * 查找当前待导入用户中是否已有相同账号人员
	 * @param users
	 * @param account
	 * @return
	 */
	private User findUserFromNews(List<User> users,String account){
		for (User user : users) {
			if(user.getAccount().equals(account)){
				return user;
			}
		}
		return null;
	}
	
	private void recordLog(ArrayNode logArray,String msg,Object param) throws IOException{
		String reqUrl = "/api/user/v1/users/importExcelUser";
		doLog(false, reqUrl, BeanUtils.isNotEmpty(param)?JsonUtil.toJsonNode(param):"", msg, true, true);
		logArray.add(logArray.size()+1+"、"+msg);
	}
	
	private boolean checkEmail(String email){
		try {
			String check = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		   Pattern regex = Pattern.compile(check);
		   Matcher matcher = regex.matcher(email);
		   return matcher.matches();
		} catch (Exception e) {}
		return false;
	}
	
	/**
	 * 添加需要新增的组织
	 * @param org
	 * @param orgList
	 * @return
	 */
	public Org dealOrg(Org org,List<Org> orgList,String preCode,Map<String,String> orgMap){
		Org rtn = null;
		if(BeanUtils.isEmpty(orgList)){
			if(BeanUtils.isNotEmpty(orgMap.get(org.getCode()))){
				org.setCode(org.getCode()+"_"+org.getId().substring(org.getId().length()-3));
			}
			orgList.add(org);
			orgMap.put(org.getCode(), org.getId());
			rtn = org;
		}else{
			Boolean flag = true; 
			for(int i=0;i<orgList.size();i++){
				if(orgList.get(i).getPathName().equals(org.getPathName()) ){
					flag = false;
					rtn = orgList.get(i);
				}
				if(orgList.get(i).getName().equals(org.getName()) &&  orgList.get(i) != org){//新增数据中，组织同名的情况
					org.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(org.getName())+(i+1));
				}
				Org systemOrg = orgService.getByCode(org.getCode());//判断是否与数据库中组织代码重复
				if(BeanUtils.isNotEmpty(systemOrg)){
					org.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(org.getName())+(i+1)+(i+1));
				}
				if(i == (orgList.size()-1) && flag ){
					if(BeanUtils.isNotEmpty(orgMap.get(org.getCode()))){
						org.setCode(org.getCode()+"_"+org.getId().substring(org.getId().length()-3));
					}
					orgList.add(org);
					orgMap.put(org.getCode(), org.getId());
					rtn = org;
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 处理组织的上下级关系
	 * @param supperPathName  父级路径名
	 * @param underPathName  子级路径名
	 * @param demId 对应维度id
	 * @param orgList
	 * @return
	 */
	public Org dealOrgUnder(String supperPathName,String underPathName,List<Org> orgList,String demId,String preCode,Map<String,String> orgMap){
		String underName = underPathName.substring(underPathName.lastIndexOf("/")+1, underPathName.length());
		List<Org> chirList = orgService.getByPathName(underPathName);
		if(BeanUtils.isNotEmpty(chirList)){//子组织不为空
			for (Org org : chirList) {
				if(demId.equals(org.getDemId())){
					return org;
				}
			}
		}
		//子组织为空的情况
		List<Org> pList = orgService.getByPathName(supperPathName);
		List<Org> parentList = new ArrayList<Org>();
		for (Org org : pList) {
			if(demId.equals(org.getDemId())){
				parentList.add(org);
			}
		}
		Org chird = new Org();
		if(BeanUtils.isEmpty(parentList)){//父组织为空，直接从orgList中寻找父级组织
			for(int i=0;i<orgList.size();i++){
				if(orgList.get(i).getPathName().equals(supperPathName)){
					chird.setId(UniqueIdUtil.getSuid());
					chird.setParentId(orgList.get(i).getId());
					chird.setDemId(demId);
					chird.setName(underName);
					chird.setPathName(underPathName);
					chird.setPath(orgList.get(i).getPath()+chird.getId()+".");
					chird.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(underName));
					//chird.setOrgType("实体");
				}
			}
		}else{//父组织不为空，从数据库中拿父组织
			chird.setId(UniqueIdUtil.getSuid());
			chird.setParentId(parentList.get(0).getId());
			chird.setDemId(demId);
			chird.setName(underName);
			chird.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(underName));
			chird.setPathName(underPathName);
			chird.setPath(parentList.get(0).getPath()+chird.getId()+".");
			//chird.setOrgType("实体");
		}
		
		return dealOrg(chird,orgList,preCode,orgMap);
	}
	
	/**
	 * 处理excel中的职务
	 * @param reldefName 职务名称
	 * @param orgJobList 新增职务列表
	 * @return
	 */
	public List<OrgJob> dealOrgJob(String reldefNameStr,List<OrgJob> orgJobList,String preCode){
		if(StringUtil.isEmpty(reldefNameStr)) return null;
		String[] reldefNames = reldefNameStr.split(";");
		List<OrgJob> rtn = new ArrayList<OrgJob>();
		for (String reldefName : reldefNames) {
			OrgJob reldef = null;
			List<OrgJob> selectList = orgJobService.getByName(reldefName);
			if(BeanUtils.isNotEmpty(selectList)){
				reldef =  selectList.get(0);
			}else{
				Boolean flag = true;
				reldef = new OrgJob();
				reldef.setId(UniqueIdUtil.getSuid());
				reldef.setCode(PinyinUtil.getPinYinHeadChar(reldefName));
				reldef.setName(reldefName);
				OrgJob sysOrgJob = orgJobService.getByCode(reldef.getCode());
				if(BeanUtils.isNotEmpty(sysOrgJob)){
					reldef.setCode(reldef.getCode()+reldef.getId());
				}
				if(BeanUtils.isEmpty(orgJobList)){
					orgJobList.add(reldef);
				}else{
					for(int i=0;i<orgJobList.size();i++){
						if(orgJobList.get(i).getName().equals(reldefName)){
							flag = false;
							reldef = orgJobList.get(i);
						}
						if(i == (orgJobList.size()-1) && flag){
							//新增
							orgJobList.add(reldef);
						}
					}
				}
			}
			if(BeanUtils.isNotEmpty(reldef)){
				rtn.add(reldef);
			}
		}
		
		
		return rtn;
	}
	
	/**
	 * 处理新增用户列表
	 * @param user
	 * @param userList
	 * @return
	 */
	public User dealUser(User user,List<User> userList) throws Exception{
		User rtn = new User();
		if(StringUtil.isNotEmpty(user.getId())){
			rtn = user;
		}else{
			Boolean flag = true;
			if(BeanUtils.isEmpty(userList)){
				user.setId(UniqueIdUtil.getSuid());
				rtn = user;
				userList.add(user);
			}else{
				for(int i=0;i<userList.size();i++){
					if(userList.get(i).getAccount().equals(user.getAccount()) && userList.get(i) != user){
						logger.error("Excel表格中用户帐号重复："+userList.get(i).getAccount());
					}
					if(userList.get(i).getAccount().equals(user.getAccount()) && userList.get(i) == user){
						flag = false;
						rtn = userList.get(i);
					}
					if(i == (userList.size()-1) && flag){
						user.setId(UniqueIdUtil.getSuid());
						rtn = user;
						userList.add(user);
					}
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 处理用、组织、岗位关系
	 * @param user
	 * @param org
	 * @param orgPost
	 * @param orgUserList
	 * @param orgPostList
	 * @throws SQLException 
	 */
	public void dealUserOrgPost(User user,Org org,List<OrgPost> orgPosts,List<OrgUser> orgUserList,List<OrgPost> orgPostList,Map<String,String> orgPostAddMap,
			String isMaster,String isCharge,Map<String,String> isMasterMap,String demId) throws SQLException{
		if(BeanUtils.isEmpty(org)) return;
		if(BeanUtils.isNotEmpty(orgPosts)){
			for (OrgPost orgPost : orgPosts) {
				OrgUser orgUser = new OrgUser();
				if(StringUtil.isNotEmpty(orgPostAddMap.get(orgPost.getId()))){//在数据库中没有存在，考虑是否加入新增岗位列表中
					Boolean flag = true;
					if(BeanUtils.isEmpty(orgPostList)){//如果需要新增的岗位列表为空，则直接加入进去
						orgPost.setCode(org.getCode()+"_"+orgPost.getCode());
						orgPostList.add(orgPost);
					}else{
						//不为空则判断，需要增加的岗位。是否已经存在于需要增加的岗位列表里。以免重复新增
						for(int i=0;i<orgPostList.size();i++){
							if(orgPostList.get(i).getOrgId().equals(orgPost.getOrgId()) && orgPostList.get(i).getName().equals(orgPost.getName())){
								orgPost = orgPostList.get(i);
								flag = false;
							}
							//列表循环到最后一条数据，还不存在，则新增进去
							if(flag && i == (orgPostList.size()-1)){
								orgPost.setCode(org.getCode()+"_"+orgPost.getCode());
								orgPostList.add(orgPost);
							}
						}
					}
				}
				QueryFilter filter = QueryFilter.build();
				//filter.setClazz(OrgUser.class);
				filter.addFilter("orgId", orgPost.getOrgId(), QueryOP.EQUAL,FieldRelation.AND);
				filter.addFilter("userId", user.getId(), QueryOP.EQUAL,FieldRelation.AND);
				filter.addFilter("relId", orgPost.getId(), QueryOP.EQUAL,FieldRelation.AND);
				//查询用户在该组织下，是否已存在该岗位。
				List<OrgUser> sysOrgUserList =  orgUserService.query(filter).getRows();
				if(BeanUtils.isEmpty(sysOrgUserList)){//不存在则添组织岗位信息
					orgUser.setId(UniqueIdUtil.getSuid());
					orgUser.setOrgId(orgPost.getOrgId());//组织id
					orgUser.setRelId(orgPost.getId());//岗位编号
					orgUser.setUserId(user.getId());
					dealMasterAndCharge(isMaster, isCharge, orgUser,isMasterMap,demId);
					orgUserList.add(orgUser);
				}
				//判断用户与组织是否已经存在关系（即还没挂上岗位的记录）
		/*		Map<String,Object> params = new HashMap<String,Object>();
				params.put("orgId", orgPost.getOrgId());
				params.put("userId", user.getId());
				params.put("relIdNull", "1");
				List<OrgUser> sysOrgUserList1 = orgUserService.getByParms(params);
				if(BeanUtils.isEmpty(sysOrgUserList) && BeanUtils.isEmpty(sysOrgUserList1)){//不存在则添加组织用户关系
					orgUser.setId(UniqueIdUtil.getSuid());
					orgUser.setOrgId(orgPost.getOrgId());//组织id
					orgUser.setRelId(orgPost.getId());//岗位编号
					orgUser.setUserId(user.getId());
					dealMasterAndCharge(isMaster, isCharge, orgUser,isMasterMap,demId);
					orgUserList.add(orgUser);
				}else if(BeanUtils.isNotEmpty(sysOrgUserList1)){//用户与组织已存在关系，只是还没关联具体的岗位，做更新操作
					orgUser = sysOrgUserList1.get(0);
					orgUser.setRelId(orgPost.getId());
					orgUserService.update(orgUser);
				}*/
			}
		}
		//处理用户组织关系
		OrgUser orgUser = new OrgUser();
		QueryFilter filter = QueryFilter.build();
		//filter.setClazz(OrgUser.class);
		filter.addFilter("userId", user.getId(), QueryOP.EQUAL,FieldRelation.AND);
		filter.addFilter("orgId", org.getId(), QueryOP.EQUAL,FieldRelation.AND);
		//查询当前用户和当前组织关系在数据库中是否存在
		List<OrgUser> sysOrgUserList = orgUserService.query(filter).getRows();
		boolean isExitOrg=false;
		 for (OrgUser orgUser2 : sysOrgUserList) {
			 //遍历结果集，如果只有存在不含岗位的记录，则表明该组织已有改用户
			if (BeanUtils.isEmpty(orgUser2.getRelId())) isExitOrg=true;
		 }
		if(!isExitOrg){//不存在则添加组织用户关系
			orgUser.setId(UniqueIdUtil.getSuid());
			orgUser.setOrgId(org.getId());//组织id
			orgUser.setUserId(user.getId());
			dealMasterAndCharge(isMaster, isCharge, orgUser,isMasterMap,demId);
			orgUserList.add(orgUser);
		}
	}
	
	/**
	 * 处理负责人和主组织
	 */
	private void dealMasterAndCharge(String isMaster,String isCharge,OrgUser orgUser,Map<String,String> isMasterMap,String demId){
		if(StringUtil.isNotEmpty(isMaster)&&(isMaster.equals("1")||isMaster.equals("是"))
				&&hasMaster(orgUser.getUserId(), orgUser.getOrgId(),demId)&&StringUtil.isEmpty(isMasterMap.get(orgUser.getUserId()+"_"+demId))){
			orgUser.setIsMaster(1);
			isMasterMap.put(orgUser.getUserId()+"_"+demId, "1");
		}else{
			orgUser.setIsMaster(0);
		}
		if(StringUtil.isNotEmpty(isCharge)){
			//1：负责人；2：主负责
			if(isCharge.equals("1")){
				orgUser.setIsCharge(1);
			}else if(isCharge.equals("2")){
				if(BeanUtils.isEmpty(orgUserDao.getChargesByOrgId(orgUser.getOrgId(), 2))){
					orgUser.setIsCharge(2);
				}
			}
		}else{
			orgUser.setIsCharge(0);
		}
	}
	
	private boolean hasMaster(String userId,String orgId,String demId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<OrgUser> orgUsers = orgUserDao.getByParms(map);
		if(BeanUtils.isNotEmpty(orgUsers)){
			for (OrgUser orgUser : orgUsers) {
				if(!orgUser.getOrgId().equals(orgId)&&orgUser.getIsMaster() == 1){
					Org org = orgService.get(orgUser.getOrgId());
					if(BeanUtils.isNotEmpty(org)&&org.getDemId().equals(demId)){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private OrgPost getByRelNameAndCode(String name,String code){
		QueryFilter queryFilter = QueryFilter.build();
		//queryFilter.setClazz(OrgPost.class);
		queryFilter.addFilter("relName", name, QueryOP.EQUAL,FieldRelation.AND);
		List<OrgPost> orgPosts =orgPostService.query(queryFilter).getRows();
		if(BeanUtils.isNotEmpty(orgPosts)){
			for (OrgPost orgPost : orgPosts) {
				if(orgPost.getCode().startsWith(code)){
					return orgPost;
				}
			}
		}
		return null;
	}
	
	// 给AD同步用户，和导入用户绑定一个默认角色
    @Transactional
    private void bindRole(User user){
        if(BeanUtils.isNotEmpty(user)){
            String userId = user.getId();
            if (userRoleService.getByRoleIdUserId("1", userId) != null) return;
            UserRole userRole = new UserRole();
            userRole.setId(UniqueIdUtil.getSuid());
            userRole.setUserId(userId);
            userRole.setRoleId("1");
            userRoleService.create(userRole);
        }
    }
	
	
	@Override
	public CommonResult<String> importExcelUser(MultipartFile file, String demCode){
		try {
			Demension demension = demensionDao.getByCode(demCode);
			if(BeanUtils.isEmpty(demension)){
				 return new CommonResult<String>(false, "根据维度编码【"+demCode+"】未找到对应维度！", "");
			}
			Map<String,Object> rtn = this.importUser(file,demension.getId());
			Boolean isTrue = (Boolean)rtn.get("result");
			String msg = isTrue?"导入成功！":"导入失败";
			ArrayNode logArry = (ArrayNode) rtn.get("log");
			String log = "";
			if(BeanUtils.isNotEmpty(logArry)){
				StringBuffer logs = new StringBuffer();
				for (Object object : logArry) {
					logs.append(object.toString());
					logs.append("；");
				}
				log = logs.toString();
			}
			return new CommonResult<String>(isTrue, msg, log);
		} catch (Exception e) {
			return new CommonResult<String>(false, e.getMessage(), "");
		}
	}
	
	
	@Override
    @Transactional
	public CommonResult<String> importZipUser(MultipartFile file,UserImportVo importVo)
			throws Exception {
		try {
			String fileDir = StringUtil.substringBeforeLast(file.getOriginalFilename().toString(), ".");
			String rootRealPath = (FileUtil.getIoTmpdir() +"/attachFiles/unZip/").replace("/", File.separator);
			FileUtil.createFolder(rootRealPath, true);
			// 解压文件
			ZipUtil.unZipFile(file, rootRealPath); 
			String unZipFilePath = rootRealPath + File.separator + fileDir; // 解压后文件的真正路径
			Map<String,String> errUserIds = new HashMap<String, String>();
			//导入用户
			importUser(importVo,unZipFilePath,errUserIds);
			//导入用户组织关系
			if(importVo.isOrg()){
				Map<String,String> errDemIds = new HashMap<String, String>();
				Map<String,String> errOrgIds = new HashMap<String, String>();
				Map<String,String> errJobIds = new HashMap<String, String>();
				Map<String,String> errPostIds = new HashMap<String, String>();
				
				//导入维度
				importDemension(importVo,unZipFilePath,errDemIds);
				//导入组织
				importOrg(importVo,unZipFilePath,errDemIds,errOrgIds);
				//导入职务
				importJob(importVo,unZipFilePath,errJobIds);
				//导入岗位
				importPost(importVo,unZipFilePath,errOrgIds,errJobIds,errPostIds);
				//导入用户组织关系
				importOrgUser(importVo,unZipFilePath,errUserIds,errOrgIds,errPostIds);
			}
			
			if(importVo.isRole()){
				Map<String,String> errRoleIds = new HashMap<String, String>();
				//导入角色
				importRole(importVo,unZipFilePath, errRoleIds);
				//导入用户角色关系
				importUserRole(importVo,unZipFilePath, errRoleIds, errUserIds);
			}
			
			//删除解压文件
			FileUtil.deleteDir(new File(unZipFilePath));
			return new CommonResult<String>(true, "数据导入成功！", "");
		} catch (Exception e) {
			return new CommonResult<String>(true, "数据导入失败！"+e.getMessage(), "");
		}
	}
	
	/**
     * 导入用户
     * @param rootPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Transactional
    private void importUser(UserImportVo importVo,String unZipFilePath,Map<String,String> errUserIds) throws FileNotFoundException, IOException{
    	Sheet sheet = this.getSheet(unZipFilePath, "user");
    	if(BeanUtils.isNotEmpty(sheet)){
			Map<String,Integer> headMap = this.getHeadMap(sheet);
			int begin = sheet.getFirstRowNum();  
			int end = sheet.getLastRowNum();  
			//处理excel表中的数据
			for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
				String fullname = null;
				User user = null;
				try {
					boolean isAdd = false;
					Row row = sheet.getRow(i);
					String id = (String) this.getCellValue(headMap, row, "id", "String");
					fullname = (String) this.getCellValue(headMap, row, "fullname", "String");
					String account = (String) this.getCellValue(headMap, row, "account", "String");
					String userNumber = (String) this.getCellValue(headMap, row, "userNumber", "String");
					user = userService.get(id);
					if(BeanUtils.isNotEmpty(user)){
						if(user.getAccount().equals(account)||(StringUtil.isNotEmpty(userNumber)&&userNumber.equals(user.getUserNumber()))){
							if(!importVo.isCover()){
								continue;
							}
						}else{
							String newId = UniqueIdUtil.getSuid();
							user.setId(newId);
							errUserIds.put(id,newId);
						}
					}else{
						user = userService.getByAccount(account);
						if(BeanUtils.isNotEmpty(user)){
							if(user.getFullname().equals(fullname)){
								//判断配置，系统存在时如何更新
								continue;
							}else{
								doLog(false, "/api/user/v1/users/importZipUser", fullname, "账号相同，姓名不同", false, isAdd);
								System.out.println("账号相同，姓名不同");
								errUserIds.put(id, id);
								continue;
							}
						}else{
							user = userService.getByNumber(userNumber);
							if(BeanUtils.isNotEmpty(user)){
								if(user.getFullname().equals(fullname)){
									//判断配置，系统存在时如何更新
									continue;
								}else{
									doLog(false, "/api/user/v1/users/importZipUser", userNumber, "工号相同，姓名不同", false, isAdd);
									System.out.println("工号相同，姓名不同");
									errUserIds.put(id, id);
									continue;
								}
							}
						}
						
					}
					if(BeanUtils.isEmpty(user)){
						user = new User();
						isAdd = true;
					}
					
					//是否覆盖更新
					String password = (String) this.getCellValue(headMap, row, "password", "String");
					String email = (String) this.getCellValue(headMap, row, "email", "String");
					String mobile = (String) this.getCellValue(headMap, row, "mobile", "String");
					LocalDateTime createTime = (LocalDateTime) this.getCellValue(headMap, row, "createTime", "datetime");
					String address = (String) this.getCellValue(headMap, row, "address", "String");
					String photo = (String) this.getCellValue(headMap, row, "photo", "String");
					String sex = (String) this.getCellValue(headMap, row, "sex", "String");
					String from = (String) this.getCellValue(headMap, row, "from", "String");
					Integer status = (Integer) this.getCellValue(headMap, row, "status", "int");
					Integer hasSyncToWx = (Integer) this.getCellValue(headMap, row, "hasSyncToWx", "int");
					String notifyType = (String) this.getCellValue(headMap, row, "notifyType", "String");
					String idCard = (String) this.getCellValue(headMap, row, "idCard", "String");
					String phone = (String) this.getCellValue(headMap, row, "phone", "String");
					LocalDate birthday = (LocalDate) this.getCellValue(headMap, row, "birthday", "date");
					LocalDate entryDate = (LocalDate) this.getCellValue(headMap, row, "entryDate", "date");
					String education = (String) this.getCellValue(headMap, row, "education", "String");
					LocalDateTime updateTime = (LocalDateTime) this.getCellValue(headMap, row, "updateTime", "date");
					String isDelete = (String) this.getCellValue(headMap, row, "isDelete", "String");
					
					
					if(isAdd||(!isAdd&&importVo.isCover())){
						user.setAccount(account.toLowerCase());
						user.setUserNumber(userNumber);
						user.setFullname(fullname);
						user.setPassword(password);
						user.setEmail(email);
						user.setMobile(mobile);
						user.setCreateTime(createTime);
						user.setAddress(address);
						user.setPhoto(photo);
						user.setSex(sex);
						user.setFrom(from);
						user.setStatus(status);
						user.setHasSyncToWx(hasSyncToWx);
						user.setNotifyType(notifyType);
						user.setIdCard(idCard);
						user.setPhone(phone);
						user.setBirthday(birthday);
						user.setEntryDate(entryDate);
						user.setEducation(education);
						user.setUpdateTime(updateTime);
						user.setIsDelete(isDelete);
					}
					if(isAdd){
						user.setId(id);
						userService.create(user);
						doLog(true, "/api/user/v1/users/importZipUser", user, "", false, isAdd);
					}else{
						if(importVo.isCover()){
							userService.update(user);
							doLog(true, "/api/user/v1/users/importZipUser", user, "", false, isAdd);
						}
					}
				} catch (Exception e) {
					doLog(false, "/api/user/v1/users/importZipUser", user, "用户【"+fullname+"】导入失败："+e.getMessage(), false, true);
					logger.error("用户【"+fullname+"】导入失败："+e.getMessage());
				}
			}

    	}
    }
    
    /**
     * 导入维度
     * @param rootPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @SuppressWarnings("static-access")
    @Transactional
	private void importDemension(UserImportVo importVo,String unZipFilePath,Map<String,String> errDemIds) throws FileNotFoundException, IOException{
    	Sheet sheet = this.getSheet(unZipFilePath, "demension");
    	if(BeanUtils.isNotEmpty(sheet)){
			Map<String,Integer> headMap = this.getHeadMap(sheet);
			int begin = sheet.getFirstRowNum();  
			int end = sheet.getLastRowNum();  
			//处理excel表中的数据
			for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
				String demName = null;
				Demension dem = null;
				try {
					boolean isAdd = false;
					Row row = sheet.getRow(i);
					String id = (String) this.getCellValue(headMap, row, "id", "String");
					String demCode = (String) this.getCellValue(headMap, row, "demCode", "String");
					demName = (String) this.getCellValue(headMap, row, "demName", "String");
					dem = demensionService.get(id);
					if(BeanUtils.isNotEmpty(dem)){
						if(!dem.getDemCode().equals(demCode)){
							String newId = UniqueIdUtil.getSuid();
							id = newId;
							errDemIds.put(id, newId);
							dem = null;
						}
					}else{
						dem = demensionDao.getByCode(demCode);
						if(BeanUtils.isNotEmpty(dem)){
							if(!dem.getDemName().equals(demName)){
								//当编码重复时根据设置判断是重新生成编码还是不导入
								demCode += this.IMPORT_NEW_SUFFIX;
							}else{
								errDemIds.put(id, dem.getId());
							}
						}
					}
					
					if(BeanUtils.isEmpty(dem)){
						dem = new Demension();
						dem.setId(id);
						isAdd = true;
					}
					if(isAdd||(!isAdd&&importVo.isCover())){
						String demDesc = (String) this.getCellValue(headMap, row, "demDesc", "String");
						Integer isDefault = (Integer) this.getCellValue(headMap, row, "isDefault", "int");
						LocalDateTime updateTime = (LocalDateTime) this.getCellValue(headMap, row, "updateTime", "date");
						String isDelete = (String) this.getCellValue(headMap, row, "isDelete", "String");
						
						dem.setDemCode(demCode);
						dem.setDemDesc(demDesc);
						dem.setDemName(demName);
						dem.setIsDefault(isDefault);
						dem.setUpdateTime(updateTime);
						dem.setIsDelete(isDelete);
						if(isAdd){
							demensionService.create(dem);
							doLog(true, "/api/user/v1/users/importZipUser", dem, "", false, isAdd);
						}else{
							demensionService.update(dem);
							doLog(true, "/api/user/v1/users/importZipUser", dem, "", false, isAdd);
						}
					}
					
				} catch (Exception e) {
					doLog(false, "/api/user/v1/users/importZipUser", dem, "维度【"+demName+"】导入失败："+e.getMessage(), false, false);
					logger.error("维度【"+demName+"】导入失败："+e.getMessage());
				}
			}

    	}
    }
    
    /**
     * 导入组织
     * @param rootPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @SuppressWarnings("static-access")
    @Transactional
	private void importOrg(UserImportVo importVo,String unZipFilePath,Map<String,String> errDemIds,Map<String,String> errOrgIds) throws FileNotFoundException, IOException{
    	Sheet sheet = this.getSheet(unZipFilePath, "org");
    	if(BeanUtils.isNotEmpty(sheet)){
			Map<String,Integer> headMap = this.getHeadMap(sheet);
			int begin = sheet.getFirstRowNum();  
			int end = sheet.getLastRowNum();  
			//处理excel表中的数据
			for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
				String name = null;
				Org org = null;
				try {
					boolean isAdd = false;
					Row row = sheet.getRow(i);
					String id = (String) this.getCellValue(headMap, row, "id", "String");
					name = (String) this.getCellValue(headMap, row, "name", "String");
					String code = (String) this.getCellValue(headMap, row, "code", "String");
					String parentId = (String) this.getCellValue(headMap, row, "parentId", "String");
					org = orgService.get(id);
					if(BeanUtils.isNotEmpty(org)){
						if(!org.getCode().equals(code)){
							String newId = UniqueIdUtil.getSuid();
							id = newId;
							errOrgIds.put(id, newId);
							org = null;
						}
					}else{
						org = orgService.getByCode(code);
						if(BeanUtils.isNotEmpty(org)){
							if(!org.getName().equals(name)){
								//当编码重复时根据设置判断是重新生成编码还是不导入
								code += this.IMPORT_NEW_SUFFIX;
							}else{
								errOrgIds.put(id, org.getId());
							}
							
						}
					}
					
					if(BeanUtils.isEmpty(org)){
						org = new Org();
						org.setId(id);
						isAdd = true;
					}
					
					if(isAdd||(!isAdd&&importVo.isCover())){
						String grade = (String) this.getCellValue(headMap, row, "grade", "String");
						String demId = (String) this.getCellValue(headMap, row, "demId", "String");
						Object orderNoStr = this.getCellValue(headMap, row, "orderNo", "String");
						Long orderNo = BeanUtils.isEmpty(orderNoStr)?null:Long.parseLong((String)orderNoStr);
						String path = (String) this.getCellValue(headMap, row, "path", "String");
						String pathName = (String) this.getCellValue(headMap, row, "pathName", "String");
						LocalDateTime updateTime = (LocalDateTime) this.getCellValue(headMap, row, "updateTime", "date");
						String isDelete = (String) this.getCellValue(headMap, row, "isDelete", "String");
						org.setCode(code);
						org.setName(name);
						org.setDemId(StringUtil.isNotEmpty(errDemIds.get(demId))?errDemIds.get(demId):demId);
						org.setParentId(parentId);
						org.setGrade(grade);
						org.setOrderNo(orderNo);
						org.setPath(path);
						org.setPathName(pathName);
						org.setUpdateTime(updateTime);
						org.setIsDelete(isDelete);
						if(isAdd){
							orgService.create(org);
							doLog(true, "/api/user/v1/users/importZipUser", org, "", false, isAdd);
						}else{
							orgService.update(org);
							doLog(true, "/api/user/v1/users/importZipUser", org, "", false, isAdd);
						}
					}
				} catch (Exception e) {
					doLog(false, "/api/user/v1/users/importZipUser", org,"组织【"+name+"】导入失败："+e.getMessage(), false, true);
					logger.error("组织【"+name+"】导入失败："+e.getMessage());
				}
			}

    	}
    }
    
    /**
     * 导入用户组织关系
     * @param rootPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Transactional
    private void importOrgUser(UserImportVo importVo,String unZipFilePath,Map<String,String> errUserIds,Map<String,String> errOrgIds,Map<String,String> errPostIds) throws FileNotFoundException, IOException{
    	Sheet sheet = this.getSheet(unZipFilePath, "orgUser");
    	if(BeanUtils.isNotEmpty(sheet)){
			Map<String,Integer> headMap = this.getHeadMap(sheet);
			int begin = sheet.getFirstRowNum();  
			int end = sheet.getLastRowNum();  
			//处理excel表中的数据
			for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
				String id = null;
				OrgUser orgUser = null;
				try {
					Row row = sheet.getRow(i);
					id = (String) this.getCellValue(headMap, row, "id", "String");
					orgUser = orgUserService.get(id);
					boolean isAdd = false;
					if(BeanUtils.isEmpty(orgUser)){
						isAdd = true;
						orgUser = new OrgUser();
						orgUser.setId(UniqueIdUtil.getSuid());
					}
					String orgId = (String) this.getCellValue(headMap, row, "orgId", "String");
					String userId = (String) this.getCellValue(headMap, row, "userId", "String");
					String relId = (String) this.getCellValue(headMap, row, "relId", "String");
					Integer isMaster = (Integer) this.getCellValue(headMap, row, "isMaster", "int");
					Integer isCharge = (Integer) this.getCellValue(headMap, row, "isCharge", "int");
					LocalDateTime startDate = (LocalDateTime) this.getCellValue(headMap, row, "startDate", "date");
					LocalDateTime endDate = (LocalDateTime) this.getCellValue(headMap, row, "endDate", "date");
					Integer isRelActive = (Integer) this.getCellValue(headMap, row, "isRelActive", "int");
					LocalDateTime updateTime = (LocalDateTime) this.getCellValue(headMap, row, "updateTime", "date");
					String isDelete = (String) this.getCellValue(headMap, row, "isDelete", "String");
					orgUser.setOrgId(StringUtil.isNotEmpty(errOrgIds.get(orgId))?errOrgIds.get(orgId):orgId);
					orgUser.setUserId(StringUtil.isNotEmpty(errUserIds.get(userId))?errUserIds.get(userId):userId);
					orgUser.setRelId(StringUtil.isNotEmpty(errPostIds.get(relId))?errPostIds.get(relId):relId);
					orgUser.setIsMaster(isMaster);
					orgUser.setIsCharge(isCharge);
					orgUser.setStartDate(startDate);
					orgUser.setEndDate(endDate);
					orgUser.setIsRelActive(isRelActive);
					orgUser.setUpdateTime(updateTime);
					orgUser.setIsDelete(isDelete);
					if(isAdd){
						orgUserService.create(orgUser);
						doLog(true, "/api/user/v1/users/importZipUser", orgUser, "", false, isAdd);
					}else{
						orgUserService.update(orgUser);
						doLog(true, "/api/user/v1/users/importZipUser", orgUser, "", false, isAdd);
					}
					
				} catch (Exception e) {
					doLog(false, "/api/user/v1/users/importZipUser", orgUser, "用户组织关系Id为【"+id+"】导入失败："+e.getMessage(), false, true);
					logger.error("用户组织关系Id为【"+id+"】导入失败："+e.getMessage());
				}
			}

    	}
    }
    
    /**
     * 导入职务
     * @param rootPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @SuppressWarnings("static-access")
    @Transactional
	private void importJob(UserImportVo importVo,String unZipFilePath,Map<String,String> errJobIds) throws FileNotFoundException, IOException{
    	Sheet sheet = this.getSheet(unZipFilePath, "job");
    	if(BeanUtils.isNotEmpty(sheet)){
			Map<String,Integer> headMap = this.getHeadMap(sheet);
			int begin = sheet.getFirstRowNum();  
			int end = sheet.getLastRowNum();  
			//处理excel表中的数据
			for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
				String name = null;
				OrgJob job = null;
				try {
					boolean isAdd = false;
					Row row = sheet.getRow(i);
					String id = (String) this.getCellValue(headMap, row, "id", "String");
					name = (String) this.getCellValue(headMap, row, "name", "String");
					String code = (String) this.getCellValue(headMap, row, "code", "String");
					
					job = orgJobService.get(id);
					if(BeanUtils.isNotEmpty(job)){
						if(!job.getCode().equals(code)){
							String newId = UniqueIdUtil.getSuid();
							id = newId;
							errJobIds.put(id, newId);
							job = null;
						}
					}else{
						job = orgJobService.getByCode(code);
						if(BeanUtils.isNotEmpty(job)){
							if(!job.getName().equals(name)){
								//当编码重复时根据设置判断是重新生成编码还是不导入
								code += this.IMPORT_NEW_SUFFIX;
							}else{
								errJobIds.put(id, job.getId());
							}
							
						}
					}
					
					if(BeanUtils.isEmpty(job)){
						job = new OrgJob();
						job.setId(id);
						isAdd = true;
					}
					
					if(isAdd||(!isAdd&&importVo.isCover())){
						String postLevel = (String) this.getCellValue(headMap, row, "postLevel", "String");
						String description = (String) this.getCellValue(headMap, row, "description", "String");
						LocalDateTime updateTime = (LocalDateTime) this.getCellValue(headMap, row, "updateTime", "date");
						String isDelete = (String) this.getCellValue(headMap, row, "isDelete", "String");
						job.setCode(code);
						job.setName(name);
						job.setPostLevel(postLevel);
						job.setDescription(description);
						job.setUpdateTime(updateTime);
						job.setIsDelete(isDelete);
						if(isAdd){
							orgJobService.create(job);
							doLog(true, "/api/user/v1/users/importZipUser", job, "", false, isAdd);
						}else{
							orgJobService.update(job);
							doLog(true, "/api/user/v1/users/importZipUser", job, "", false, isAdd);
						}
					}
					
				} catch (Exception e) {
					doLog(false, "/api/user/v1/users/importZipUser", job, "职务【"+name+"】导入失败："+e.getMessage(), false, true);
					logger.error("职务【"+name+"】导入失败："+e.getMessage());
				}
			}

    	}
    }
    
    /**
     * 导入岗位
     * @param rootPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @SuppressWarnings("static-access")
    @Transactional
	private void importPost(UserImportVo importVo,String unZipFilePath,Map<String,String> errOrgIds,Map<String,String> errJobIds,Map<String,String> errPostIds) throws FileNotFoundException, IOException{
    	Sheet sheet = this.getSheet(unZipFilePath, "post");
    	if(BeanUtils.isNotEmpty(sheet)){
			Map<String,Integer> headMap = this.getHeadMap(sheet);
			int begin = sheet.getFirstRowNum();  
			int end = sheet.getLastRowNum();  
			//处理excel表中的数据
			for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
				String relName = null;
				OrgPost post = null;
				try {
					boolean isAdd = false;
					Row row = sheet.getRow(i);
					String id = (String) this.getCellValue(headMap, row, "id", "String");
					relName = (String) this.getCellValue(headMap, row, "relName", "String");
					String relCode = (String) this.getCellValue(headMap, row, "relCode", "String");
					
					post = orgPostService.get(id);
					if(BeanUtils.isNotEmpty(post)){
						if(!post.getCode().equals(relCode)){
							String newId = UniqueIdUtil.getSuid();
							id = newId;
							errPostIds.put(id, newId);
							post = null;
						}
					}else{
						post = orgPostService.getByCode(relCode);
						if(BeanUtils.isNotEmpty(post)){
							if(!post.getName().equals(relName)){
								//当编码重复时根据设置判断是重新生成编码还是不导入
								relCode += this.IMPORT_NEW_SUFFIX;
							}else{
								errPostIds.put(id, post.getId());
							}
							
						}
					}
					
					if(BeanUtils.isEmpty(post)){
						post = new OrgPost();
						post.setId(id);
						isAdd = true;
					}
					
					if(isAdd||(!isAdd&&importVo.isCover())){
						LocalDateTime updateTime = (LocalDateTime) this.getCellValue(headMap, row, "updateTime", "date");
						String isDelete = (String) this.getCellValue(headMap, row, "isDelete", "String");
						
						String orgId = (String) this.getCellValue(headMap, row, "orgId", "String");
						String relDefId = (String) this.getCellValue(headMap, row, "relDefId", "String");
						Integer isCharge = (Integer) this.getCellValue(headMap, row, "isCharge", "int");
						post.setCode(relCode);
						post.setName(relName);
						post.setOrgId(StringUtil.isNotEmpty(errOrgIds.get(orgId))?errOrgIds.get(orgId):orgId);
						post.setRelDefId(StringUtil.isNotEmpty(errJobIds.get(relDefId))?errJobIds.get(relDefId):relDefId);
						post.setIsCharge(isCharge);
						post.setUpdateTime(updateTime);
						post.setIsDelete(isDelete);
						if(isAdd){
							orgPostService.create(post);
							doLog(true, "/api/user/v1/users/importZipUser", post, "", false, isAdd);
						}else{
							orgPostService.update(post);
							doLog(true, "/api/user/v1/users/importZipUser", post, "", false, isAdd);
						}
					}
					
				} catch (Exception e) {
					doLog(false, "/api/user/v1/users/importZipUser", post, "岗位【"+relName+"】导入失败："+e.getMessage(), false, true);
					logger.error("岗位【"+relName+"】导入失败："+e.getMessage());
				}
			}

    	}
    }
    
    /**
     * 导入角色
     * @param rootPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @SuppressWarnings("static-access")
    @Transactional
	private void importRole(UserImportVo importVo,String unZipFilePath,Map<String,String> errRoleIds) throws FileNotFoundException, IOException{
    	Sheet sheet = this.getSheet(unZipFilePath, "role");
    	if(BeanUtils.isNotEmpty(sheet)){
			Map<String,Integer> headMap = this.getHeadMap(sheet);
			int begin = sheet.getFirstRowNum();  
			int end = sheet.getLastRowNum();  
			//处理excel表中的数据
			for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
				String name = null;
				Role role = null;
				try {
					boolean isAdd = false;
					Row row = sheet.getRow(i);
					String id = (String) this.getCellValue(headMap, row, "id", "String");
					name = (String) this.getCellValue(headMap, row, "name", "String");
					String code = (String) this.getCellValue(headMap, row, "code", "String");
					
					role = roleService.get(id);
					if(BeanUtils.isNotEmpty(role)){
						if(!role.getCode().equals(code)){
							String newId = UniqueIdUtil.getSuid();
							id = newId;
							errRoleIds.put(id, newId);
							role = null;
						}
					}else{
						role = roleService.getByAlias(code);
						if(BeanUtils.isNotEmpty(role)){
							if(!role.getName().equals(name)){
								//当编码重复时根据设置判断是重新生成编码还是不导入
								code += this.IMPORT_NEW_SUFFIX;
							}else{
								errRoleIds.put(id, role.getId());
							}
							
						}
					}
					
					if(BeanUtils.isEmpty(role)){
						role = new Role();
						role.setId(id);
						isAdd = true;
					}
					
					if(isAdd||(!isAdd&&importVo.isCover())){
						String description = (String) this.getCellValue(headMap, row, "description", "String");
						Integer enabled = (Integer) this.getCellValue(headMap, row, "enabled", "int");
						LocalDateTime updateTime = (LocalDateTime) this.getCellValue(headMap, row, "updateTime", "date");
						String isDelete = (String) this.getCellValue(headMap, row, "isDelete", "String");
						role.setCode(code);
						role.setName(name);
						role.setEnabled(enabled);
						role.setDescription(description);
						role.setUpdateTime(updateTime);
						role.setIsDelete(isDelete);
						if(isAdd){
							roleService.create(role);
							doLog(true, "/api/user/v1/users/importZipUser", role, "", false, isAdd);
						}else{
							roleService.update(role);
							doLog(true, "/api/user/v1/users/importZipUser", role, "", false, isAdd);
						}
					}
				} catch (Exception e) {
					doLog(false, "/api/user/v1/users/importZipUser", role, "角色【"+name+"】导入失败："+e.getMessage(), false, true);
					logger.error("角色【"+name+"】导入失败："+e.getMessage());
				}
			}

    	}
    }
    
    /**
     * 导入用户角色关系
     * @param rootPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Transactional
    private void importUserRole(UserImportVo importVo,String unZipFilePath,Map<String,String> errRoleIds,Map<String,String> errUserIds) throws FileNotFoundException, IOException{
    	Sheet sheet = this.getSheet(unZipFilePath, "userRole");
    	if(BeanUtils.isNotEmpty(sheet)){
			Map<String,Integer> headMap = this.getHeadMap(sheet);
			int begin = sheet.getFirstRowNum();  
			int end = sheet.getLastRowNum();  
			//处理excel表中的数据
			for(int i=begin+1; i<=end; i++){// 从第二行开始读取数据
				String id = null;
				UserRole userRole = null;
				try {
					Row row = sheet.getRow(i);
					id = (String) this.getCellValue(headMap, row, "id", "String");
					userRole = userRoleService.get(id);
					boolean isAdd = false;
					if(BeanUtils.isEmpty(userRole)){
						isAdd = true;
						userRole = new UserRole();
						userRole.setId(UniqueIdUtil.getSuid());
					}
					String roleId = (String) this.getCellValue(headMap, row, "roleId", "String");
					String userId = (String) this.getCellValue(headMap, row, "userId", "String");
					LocalDateTime updateTime = (LocalDateTime) this.getCellValue(headMap, row, "updateTime", "date");
					String isDelete = (String) this.getCellValue(headMap, row, "isDelete", "String");
					userRole.setRoleId(StringUtil.isNotEmpty(errRoleIds.get(roleId))?errRoleIds.get(roleId):roleId);
					userRole.setUserId(StringUtil.isNotEmpty(errUserIds.get(userId))?errUserIds.get(userId):userId);
					userRole.setUpdateTime(updateTime);
					userRole.setIsDelete(isDelete);
					if(isAdd){
						userRoleService.create(userRole);
						doLog(true, "/api/user/v1/users/importZipUser", userRole, "", false, isAdd);
					}else{
						userRoleService.update(userRole);
						doLog(true, "/api/user/v1/users/importZipUser", userRole, "", false, isAdd);
					}
					
				} catch (Exception e) {
					doLog(false, "/api/user/v1/users/importZipUser", userRole, "用户角色Id为【"+id+"】导入失败："+e.getMessage(), false, true);
					logger.error("用户角色Id为【"+id+"】导入失败："+e.getMessage());
				}
			}

    	}
    }
    
    /**
     * 获取excel的第一行（表头）
     * @param sheet
     * @return
     */
    private Map<String,Integer> getHeadMap(Sheet sheet){
    	Map<String,Integer> headMap = new HashMap<String,Integer>();
		for(int i=0;i<sheet.getRow(0).getLastCellNum();i++){
			Row row = sheet.getRow(0);
			headMap.put(row.getCell(i).getStringCellValue().trim(), i);
		}
		return headMap;
    }
    
    /**
     * 获取导入excel页
     * @param sheet
     * @return
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    private Sheet getSheet(String unZipFilePath,String type) throws FileNotFoundException, IOException{
    	Sheet sheet = null;
    	File excelFile = new File(unZipFilePath + File.separator + type +".xls");  
    	if(excelFile.exists()&&BeanUtils.isNotEmpty(excelFile)){
    		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
			sheet = wb.getSheetAt(0);
    	}
		return sheet;
    }
    
    private Object getCellValue(Map<String,Integer> headMap,Row row,String field,String type) throws ParseException{
    	Object value = null;
    	if(BeanUtils.isNotEmpty(headMap.get(field))){
    		Cell cell = row.getCell(headMap.get(field));
        	if("String".equals(type)){
        		if(BeanUtils.isNotEmpty(cell)&&StringUtil.isNotEmpty(cell.getStringCellValue())){
        			value = cell.getStringCellValue().trim();
        		}
        	}else if("int".equals(type)){
        		if(BeanUtils.isNotEmpty(cell)&&BeanUtils.isNotEmpty(cell.getStringCellValue())){
        			value = Integer.valueOf(cell.getStringCellValue());
        		}
        	}else if("boolean".equals(type)){
        		if(BeanUtils.isNotEmpty(cell)&&BeanUtils.isNotEmpty(cell.getBooleanCellValue())){
        			value = cell.getBooleanCellValue();
        		}
        	}else if("date".equals(type)||"datetime".equals(type)){
        		if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){//数字类型日期
        			value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
    			}else if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){//字符串类型
    				String formate = "datetime".equals(type)?StringPool.DATE_FORMAT_DATETIME:StringPool.DATE_FORMAT_DATE;
    				SimpleDateFormat sdf = new SimpleDateFormat(formate);
    				SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
    				if(StringUtil.isNotEmpty(cell.getStringCellValue())){
    					String dateStr = cell.getStringCellValue().toString().trim();
        				Date date =sdf1.parse(dateStr);
        				String sDate=sdf.format(date);
        				value = sdf.parse(sDate);
    				}
    			}
        	}
    	}
    	return value;
    }
    
    /**
     * 记录导入日志
     * @param isSuccess
     * @param reqUrl
     * @param param
     * @param msg
     * @param isExcel
     * @param isAdd
     */
    private void doLog(boolean isSuccess,String reqUrl,Object param,String msg,boolean isExcel,boolean isAdd){
    	try {
    		if(isSuccess){
        		String preMsg = isAdd?"新增数据：":"更新数据：";
        		OperateLogUtil.doLogAsync(new OperateLog(1, "POST", reqUrl, isExcel?"导入Excel用户数据":"导入Zip用户数据",preMsg+JsonUtil.toJsonNode(param), "", ""));
        	}else{
        		String params = "";
        		try {
        			params = BeanUtils.isNotEmpty(param)?JsonUtil.toJson(param):"";
    			} catch (Exception e) {
    				params = param.toString();
    			}
        		OperateLogUtil.doLogAsync(new OperateLog(0, "POST", reqUrl, isExcel?"导入Excel用户数据":"导入Zip用户数据", params, "", msg));
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	@Override
	public CommonResult<String> syncADUsers(String action, String ip) throws Exception {
		return null;
	}

	@Override
	public CommonResult<String> syncSoap(OaAsyncObject oaAsyncObject, String ip) throws Exception {
		return null;
	}
}
