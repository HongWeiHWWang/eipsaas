package com.hotent.sys.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.sys.persistence.dao.SysIdentityDao;
import com.hotent.sys.persistence.manager.SysIdentityManager;
import com.hotent.sys.persistence.model.SysIdentity;

@Service("identityManager")
public class SysIdentityManagerImpl extends BaseManagerImpl<SysIdentityDao, SysIdentity> implements SysIdentityManager{

	/**
	 * 判读流水号别名是否已经存在
	 * @param id  id为null 表明是新增的流水号，否则为更新流水号
	 * @param alias
	 * @return
	 */
	@Override
	public boolean isAliasExisted(Map<String,Object> params) {
		Integer i = baseMapper.isAliasExisted(params);
		return i > 0;
	}
	
	
	/**
	 * 根据流程规则别名获取得当前流水号。
	 * @param alias		流水号规则别名。
	 * @return
	 */
	public String getCurIdByAlias(String alias){
		SysIdentity identity=baseMapper.getByAlias(alias);
		Integer curValue=identity.getCurValue();
		if(curValue==null) curValue=identity.getInitValue();
		String rtn=getByRule(identity.getRegulation(),identity.getNoLength(),curValue);
		return rtn;
	}
	
	/**
	 * 根据规则返回需要显示的流水号。
	 * @param rule			流水号规则。
	 * @param length		流水号的长度。
	 * @param curValue		流水号的当前值。
	 * @return
	 */
	private String getByRule(String rule,int length,  int curValue){
		Calendar c = Calendar.getInstance();
		int year= c.get(Calendar.YEAR);
		int month= c.get(Calendar.MONTH) +1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String yearStr = "" + year;
		String shortMonth="" + month;
		String longMonth=(month<10)?"0" + month :"" + month;
		
		String shortDay="" + day;
		String longDay=(day<10)?"0" + day :"" + day;
		
		/**处理需填充部分的长度 start**/
		String pre = rule.replace("{yyyy}", yearStr)
				.replace("{MM}", longMonth)
				.replace("{mm}", shortMonth)
				.replace("{DD}", longDay)
				.replace("{dd}", shortDay)
				.replace("{NO}", "")
				.replace("{no}", "" );
		int fillLen = StringUtils.isEmpty(pre)?0:pre.length();
		/**处理需填充部分的长度 end**/
		String seqNo=getSeqNo(rule,curValue,length);
		
		String rtn=rule.replace("{yyyy}", yearStr)
				.replace("{MM}", longMonth)
				.replace("{mm}", shortMonth)
				.replace("{DD}", longDay)
				.replace("{dd}", shortDay)
				.replace("{NO}", seqNo)
				.replace("{no}", seqNo );
		
		
		return rtn;
	}
	
	/**
	 * 根据当前流水号的值和流水号显示的长度。
	 * <pre>
	 * 比如：当前流水号为55 ，显示长度为5那么这个方法返回：00055。
	 * </pre>
	 * @param curValue		当前流水号的值。
	 * @param length		显示的长度。
	 * @return
	 */
	private static String getSeqNo(String rule,int curValue,int length){
		String tmp=curValue +"";
		int len= 0 ;
		if(rule.indexOf("no")>-1){
			len = length;
		}else{
			len = length-tmp.length();
		}
		String rtn="";
		for (int i = 0; i < len; i++) {
			rtn=rtn+"0";
		}
		if(rule.indexOf("no")>-1){
			return tmp + rtn;
		}else{
			return rtn + tmp;
		}
		
	}
	
	/**
	 * 根据流程规则别名获取得下一个流水号。
	 * @param alias		流水号规则别名。
	 * @return
	 */
	public  synchronized String  nextId(String alias){
		SysIdentity identity = baseMapper.getByAlias(alias);
		
		Result result=genResult(identity);
		
		int tryTimes = 0;
		while(result.getRtn()==0){
			tryTimes ++ ; // 防止在使用中修改步长，导致死循环
			if(tryTimes > 100) throw new RuntimeException("获取流水号失败！ " +identity.getAlias());
			
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			identity.setCurValue(result.getCurValue());
			result=genResult(identity);
		}
		return result.getIdNo();
	}
	
	public  Result genResult(SysIdentity identity) {
		String rule = identity.getRegulation();
		int step = identity.getStep();
		int genEveryDay = identity.getGenType();
		
		//如果失败过一次、使用失败的当前值。没有失败
		Integer curValue = identity.getCurValue();
		/*if(failCurValue != 0) curValue = failCurValue;*/
		
		if (curValue == 0) curValue = identity.getInitValue();

		// 每天都生成
		if (genEveryDay == 0) {
			curValue = curValue + step;
		} else {
			String formatString = getFormatString(genEveryDay);

			String curDate = getCurDate(formatString);
			String oldDate  = identity.getCurDate();
			if (!curDate.equals(oldDate)) {
				identity.setCurDate(curDate);
				curValue = identity.getInitValue();
			} else {
				curValue = curValue + step;
			}
		}
		identity.setNewCurValue(curValue);
		int i = 0;
		i = baseMapper.updByAlias(identity);
		Result result = new Result(0, "",curValue);
		if (i > 0) {
			String rtn = getByRule(rule, identity.getNoLength(), curValue);
			result.setIdNo(rtn);
			result.setRtn(1);
		}
		return result;
	}

	public String getFormatString(Integer type){
		String formatString = "yyyyMMdd";
		if (type==2){
			formatString="yyyyMM";
		}else if (type==3){
			formatString="yyyy";
		}
		return formatString;
	}

	/**
	 * 返回当前日期。格式为 年月日。
	 * @return
	 */
	public String getCurDate(String formatString){
		return 	DateFormatUtil.format(LocalDateTime.now(), formatString);
		
	}
	
	/**
	 * 预览时，获取前十个流水号
	 * @param alias
	 * @return
	 */
	public List<SysIdentity> getPreviewIden(String alias){
		int genNum=10;
		SysIdentity identity=baseMapper.getByAlias(alias);
		String rule=identity.getRegulation();
		int step=identity.getStep();
		Integer curValue=identity.getCurValue();
		if(curValue==null) curValue= identity.getInitValue();
		List<SysIdentity> tempList= new ArrayList<SysIdentity>();
		for (int i = 0; i < genNum; i++) {
			SysIdentity identitytemp= new SysIdentity();
			if (i > 0) {
				curValue += step;
			}
			String rtn=getByRule(rule,identity.getNoLength(),curValue);
			identitytemp.setId(curValue.toString());
			identitytemp.setCurIdenValue(rtn);
			tempList.add(identitytemp);
		}
		return tempList;
	}
	
	
	public class Result {
		
		private int rtn=0;
		private String idNo="";
		private int curValue=0;
		
		public Result(int rtn, String idNo, int curValue) {
			this.rtn = rtn;
			this.idNo = idNo;
			this.setCurValue(curValue);
		}
		
		
		public int getRtn() {
			return rtn;
		}
		public void setRtn(int rtn) {
			this.rtn = rtn;
		}
		public String getIdNo() {
			return idNo;
		}
		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}

		public int getCurValue() {
			return curValue;
		}

		public void setCurValue(int curValue) {
			this.curValue = curValue;
		}
		
		
	}
}
