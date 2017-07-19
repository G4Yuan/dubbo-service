package com.qktz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qktz.service.thrift.intelligent.BusinessException;
import com.qktz.utils.tools.RedisTools;
import com.qktz.utils.tools.ZKTools;


public class CommonUtils {
	private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	/**
	 * 有效手机号码前缀
	 */
	private static String[] mobile_prefix = {
			"134","135","136","137","138","139","147","182","183","184","187","188",
			"150","151","152","157","158","159","178","130","131","132","145","155",
			"156","171","175","176","185","186","133","149","153","173","177","180","181","189","170"};
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 数字
	 */
	private static int[] numbers = {0,1,2,3,4,5,6,7,8,9};
	/**
	 * 随机获取一个有效的手机号码前缀
	 * @return
	 */
	public static String getRandMobilePrefix(){
		int len = mobile_prefix.length;
		Random rd = new Random();
		return mobile_prefix[rd.nextInt(len-1)];
	}
	/**
	 * 获取指定长度的数字串
	 * @param len
	 * @return
	 */
	public static String getRandStr(int len){
		int length = numbers.length;
		StringBuffer sb = new StringBuffer();
		Random rd = new Random();
		for(int i=0;i<len;i++){
			sb.append(numbers[rd.nextInt(length-1)]);
		}
		return sb.toString();
	}
	/**
	 * 获取文档编号
	 * @return
	 */
	public static String getDocNo(String prefix){
		StringBuffer docNo = new StringBuffer(prefix);
		docNo.append(sdf.format(new Date()));
		long num = RedisTools.incr("doc_no");
		docNo.append(String.format("%04d", num));
		return docNo.toString();
	}
	/**
	 * "A=3,B=4,C=6,D=10"
	 * @return
	 */
	public static Map<String,Integer> transStr2Json(String statards){
		Map<String,Integer> json = new HashMap<String,Integer>();
		if(StringUtils.isBlank(statards)) return json;
		String[] scores = statards.split(",");
		for(String score:scores){
			if(score.contains("=")){
				String[] q = score.split("=");
				String key = q[0];
				String value = q[1];
				try {
					json.put(key, Integer.parseInt(value));
				} catch (Exception e) {
					logger.error("", e);
				}
				
			}
		}
		return json;
	}
	/**
	 * 生成uuid code
	 * @return
	 */
	public static String genCode(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 以2开头，生成随机手机号
	 * @return
	 * @throws BusinessException 
	 */
	public static String getRobotNum() throws BusinessException{
		String phoneNum_Head = ZKTools.getPrivateConfData("common").get(
				"phonenum_head");
		if (!(phoneNum_Head.equals("2")||phoneNum_Head.equals("3"))) 
			throw new BusinessException(100002,"配置手机号Head错误");
		String phoneNum = phoneNum_Head;
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			int num = rand.nextInt(9);
			phoneNum += String.valueOf(num);			
		}
		return phoneNum;
	}
	
	/**
	 * 以2开头，生成随机手机号
	 * @return
	 * @throws BusinessException 
	 */
	public static String getRobotNickname() throws BusinessException{
		
		String phoneNum = "9";
		Random rand = new Random();
		for (int i = 0; i < 5; i++) {
			int num = rand.nextInt(9);
			phoneNum += String.valueOf(num);			
		}
		return "用户"+phoneNum;
	}
	
	 /**
	   * 将UTC时间转换为东八区时间
	   * @param UTCTime
	   * @return
	   */
	  public static String getLocalTimeFromUTC(String UTCTime){
		UTCTime = UTCTime.replace("Z", " UTC");//注意是空格+UTC
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
	    Date UTCDate = null ;
	    String localTimeStr = null ;
	    try {
	      UTCDate = format.parse(UTCTime);
	      sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")) ;
	      localTimeStr = sdf.format(UTCDate) ;
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return localTimeStr ;
	  }
	  
	 /**
	  * 
	  * @Title: daysOfTwo 
	  * @Description: 日期相隔天数 
	  * @param fDate
	  * @param oDate
	   */
	  public static int daysOfTwo(Date fDate, Date oDate) {
	       Calendar aCalendar = Calendar.getInstance();
	       aCalendar.setTime(fDate);
	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       aCalendar.setTime(oDate);
	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       return day2 - day1;

	    }
	
}
