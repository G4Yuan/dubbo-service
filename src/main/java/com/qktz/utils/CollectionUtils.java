package com.qktz.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public final class CollectionUtils {
    
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = LoggerFactory.getLogger(CollectionUtils.class);
	private static final String TEMP_ENCODING = "ISO-8859-1";  
    private static final String DEFAULT_ENCODING = "UTF-8";  

	/**
	 * 得到UTC时间，类型为Date，格式为"yyyy-MM-dd HH:mm:ss" 如果获取失败，返回null
	 * @return
	 */
	public static Date getUTCTimeStr() {
		StringBuffer UTCTimeBuffer = new StringBuffer();
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day);
		UTCTimeBuffer.append(" ").append(hour).append(":").append(minute).append(":").append(second);
		try {
			return format.parse(UTCTimeBuffer.toString());
			// return UTCTimeBuffer.toString() ;
		} catch (Exception e) {
			logger.info("转换失败",e);
			//e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将UTC时间转换为东八区时间
	 * @param UTCTime
	 * @return
	 */
	public static Date getLocalTimeFromUTC(Date UTCTime) {
		Date UTCDate = null;
		String localTimeStr = null;
		Date localTimeDate = null;
		try {
			UTCDate = UTCTime;
			format.setTimeZone(TimeZone.getTimeZone("GMT-8"));
			localTimeStr = format.format(UTCDate);
			localTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(localTimeStr);
		} catch (Exception e) {
			logger.info("转换失败",e);
			//e.printStackTrace();
		}

		return localTimeDate;
	}

	/**
	 * date 转  XMLGregorianCalendar
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public static javax.xml.datatype.XMLGregorianCalendar getXMLGregorianCalendar(java.util.Date date)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		javax.xml.datatype.DatatypeFactory dtf = javax.xml.datatype.DatatypeFactory.newInstance();
		return dtf.newXMLGregorianCalendar(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1,
				calendar.get(calendar.DAY_OF_MONTH), calendar.get(calendar.HOUR), calendar.get(calendar.MINUTE),
				calendar.get(calendar.SECOND), calendar.get(calendar.MILLISECOND),
				calendar.get(calendar.ZONE_OFFSET) / (1000 * 60));
	}

	/**
	 * XMLGregorianCalendar 转 date
	 * @param cal
	 * @return
	 */
	public static Date getDateFromXMLGregorianCalendar(XMLGregorianCalendar cal) {
		return cal.toGregorianCalendar().getTime();
	}
	
	/**
	* 将对象序列化成字符串
    * @param serStr 
    * @return Object 
    * @throws IOException 
	*/
	public static String writeToStr(Object obj) throws IOException {
		// 此类实现了一个输出流，其中的数据被写入一个 byte 数组。
		// 缓冲区会随着数据的不断写入而自动增长。可使用 toByteArray() 和 toString() 获取数据。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 专用于java对象序列化，将对象进行序列化
		ObjectOutputStream objectOutputStream = null;
		String serStr = null;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			serStr = byteArrayOutputStream.toString(TEMP_ENCODING);
			serStr = java.net.URLEncoder.encode(serStr, DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			objectOutputStream.close();
		}
		return serStr;
	} 
	
	/** 
	 * 将序列化的字符串反序列化成对象 
	 * @param serStr 系列化的字符串 
	 * @return Object 反序列化后得到原始的对象 
	 * @throws IOException 
	 */ 
	public static Object deserializeFromStr(String serStr) throws IOException {
		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			String deserStr = java.net.URLDecoder.decode(serStr, DEFAULT_ENCODING);
			byteArrayInputStream = new ByteArrayInputStream(deserStr.getBytes(TEMP_ENCODING));
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			objectInputStream.close();
			byteArrayInputStream.close();
		}
		return null;
	}
	
	public static void main(String[] args) {
		//Date UTCTimeStr = getUTCTimeStr();
		//System.out.println(UTCTimeStr);
		//System.out.println(getLocalTimeFromUTC(UTCTimeStr));
	}

}
