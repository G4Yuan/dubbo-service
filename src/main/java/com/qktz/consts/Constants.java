package com.qktz.consts;

import java.text.SimpleDateFormat;

/**
 * 一些基本的常量
 * @author yuanwei
 * @date 二〇一七年六月二十日 16:27:50
 */
public interface Constants {
	public static final String CONTENT_TYPE="Content-Type";
	public static final String AUTHORIZATION="Authorization";
	public static final String APPLICATION_JSON="application/json;charset=utf-8";
	/**
	 * 默认时间格式
	 */
	public static final String DETAUL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * 用户状态 0 禁用 1活跃 
	 */
	public static final int USER_STATUS_OK = 1;
	public static final int USER_STATUS_BAN = 0;
	
	/***********************************官网 友情链接***********************************/
	public static final int LINK_STATUS_OK=1;			//可用
	public static final int LINK_STATUS_NO=0;	//禁用
	/**************************************************************************************/
	
	/**
	 * 钱投顾
	 */
	public static final String TOPIC_QTG_ASHARE = "ashare.indicator";			// A股风向标
	public static final String TOPIC_QTG_MASTER_GROUP = "master.group";			// 高手组合
	public static final String TOPIC_QTG_MASTER_SHARE = "master.share";			// 高手股票池
	public static final String TOPIC_QTG_STOCK_OPERATION = "stock.operation";	// 智能操盘手
	public static final String TOPIC_QTG_STOCK_POOL = "stock.pool";				// 智能股票池
	public static final String TOPIC_QTG_DAILY_STOCK = "stock.daily";			// 每日金股
	public static final String TOPIC_BOSS_AMOY_STOCK_POOl = "amoy.stock.pool";	// 公告淘股
	public static final String TOPIC_QTG_MASTER_REFERENCE = "master.reference";	// 掘金内参
	public static final String TOPIC_QTG_MASTER_SHARE_V2 = "master.stock.pool";			// 高手股票池
	
	/**
	 * 高手股票池
	 */
	public static final String OPERATOR_FLOW = "select * from master_stock_pool_operator_flow where master_id=? order by operator_time desc";
	public static final String MASTER_STOCK = "select * from master_stock where master_id=? and status=? order by current_yield desc";
	public static final String MASTER_STOCK_CNT = "select count(*) from master_stock where master_id=? and status=?";
	public static final String MASTER_STOCK_POOL = "select * from master_stock_pool where status=6 order by create_time desc";
	public static final String MASTER_STOCK_POOL_CNT = "select count(*) count from master_stock_pool where status=6 and status !=?";
	public static final String MASTER_STOCK_UPDATE = "update master_stock set status=? where master_id=? and status !=?";
	public static final String MASTER_STOCK_QUERY = "select * from master_stock where master_id=? and status =?";
	public static final String MASTER_STOCK_DELTE = "delete from master_stock where master_id=? and status =? ";
	public static final String MASTER_STOCK_UNION = "select * from (select * from master_stock_pool a where not exists( select * from  master_stock_pool_tmp c where c.id=a.id and c.status!=6) UNION select * from master_stock_pool_tmp b) d where 1=1";
	public static final String MASTER_STOCK_UNION_CNT ="select count(*) from (select * from master_stock_pool a where not exists( select * from  master_stock_pool_tmp c where c.id=a.id and c.status!=6) UNION select * from master_stock_pool_tmp b) d where 1=1";
	
	/**
	 * 股票池调出的股票
	 */
	public static final String MASTER_STOCK_OUT = "select * from master_stock where master_id=? and status=2 and stock_code not in(select stock_code from master_stock where master_id=?  and status=1 and stock_code in(select stock_code from master_stock where master_id=?  and status=2))";
	/**
	 * 格式化时间
	 */
	public static final SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sDate_zero = new SimpleDateFormat("yyyy-MM-dd '00:00:00'");
	public static final SimpleDateFormat sDate_max = new SimpleDateFormat("yyyy-MM-dd '23:59:59'");
	public static final SimpleDateFormat sDate_second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	/**
	 * 默认时间设置
	 */
	public static final String DEFAULT_DATE = "1970-01-01 00:00:00";
	
	/**
	 * 默认页数
	 */
	public static final Integer PAGE_SIZE = 30;
	
	public static final String NEED_SEND_MESSAGE_CATEGORY = "钱投顾";
	
	public static final String LAST_UPDATE_DATETIME = "2016-09-01 00:00:00.000";
	
	public static final boolean is_error = false;
}