package com.qktz.function; 

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qktz.consts.Constants;
import com.qktz.model.LatestOperation;
import com.qktz.model.Portfolio;
import com.qktz.model.PortfolioCreator;
import com.qktz.model.PortfolioYield;
import com.qktz.utils.CommonUtils;

/** 
* @ClassName: AppPagerQuery 
* @Description: 查询数据
* @author yuanwei 
* @date 2017年7月11日 下午6:58:14 
* @version V1.0 
*/
public class AppPagerQuery implements Constants{
     
	// 日志组件
	private Logger logger = LoggerFactory.getLogger(UpdatePortfolio.class);
	private Gson gson = new Gson();
	//CommonUtils
	CommonUtils commonUtils = new CommonUtils();
		
	@SuppressWarnings("all")
	public List<String> getPagerSimple(String categories, int offset,int pageSize) {
		logger.info("#reqId={},categories={},offset={},pageSize={}#{}#", 
				offset, pageSize, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		
		StringBuilder sql = new StringBuilder("select * from portfolio where 1= 1");
		List<Portfolio> obj = new ArrayList<Portfolio>();
		if(pageSize != 0){
			sql.append(" and portfolio_categories like ? and portfolio_status=6 order by createdAt desc limit ?,?");
			obj = Portfolio.dao.find(sql.toString(), new Object[]{categories, offset, pageSize});
		}else{
			sql.append(" and portfolio_categories like ? and portfolio_status=6 order by createdAt desc");
			obj = Portfolio.dao.find(sql.toString(),new Object[]{categories});
		}
		
		List<String> list = new ArrayList<String>();
		JSONObject jsonObj = null;
		PortfolioCreator creator = null;
		PortfolioYield yield = null;
		LatestOperation operation = null;
		for(Portfolio item:obj){
			jsonObj = new JSONObject();
			//组合信息
			JSONObject portfolioJSON = new JSONObject();
			portfolioJSON.put("title", item.getPortfolio_title());
			portfolioJSON.put("portfolio_summary", item.getPortfolio_summary());
			int day = 0;
			try {
				 day = commonUtils.daysOfTwo(item.getCreatedAt(), sDate_time.parse(sDate_time.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			portfolioJSON.put("working_day", day);
			portfolioJSON.put("createdAt", sDate_time.format(item.getCreatedAt()));
			jsonObj.put("portfolio", portfolioJSON);
			//个人信息
			JSONObject creatorJSON = null;
			creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",item.getUser_object_id());
			if (creator != null) {
				creatorJSON = new JSONObject();
				creatorJSON.put("user_name", creator.getUser_name());
				creatorJSON.put("user_object_id", creator.getUser_object_id());
				creatorJSON.put("user_logo", creator.getUser_logo());
				creatorJSON.put("user_summary", creator.getUser_summary());
				creatorJSON.put("qualificationName", creator.getQualificationName());
				creatorJSON.put("qualificationNum", creator.getQualificationNum());
			}
			jsonObj.put("creator", creatorJSON);
		    //收益信息
			JSONObject yieldJSON = null;
			yield = PortfolioYield.dao.findFirst("select * from portfolio_yield where portfolio_id= ?",item.getId());
			if (yield != null) {
				yieldJSON = new JSONObject();
				yieldJSON.put("total_yield", yield.getTotal_yield());
				yieldJSON.put("today_yield", yield.getToday_yield());
				yieldJSON.put("success_ratio", yield.getSuccess_ratio());
			}
			jsonObj.put("yield", yieldJSON);
			//最新个股
			JSONObject dealJSON = null;
			operation = LatestOperation.dao.findFirst("select * from latest_operation where portfolio_id= ? order by operation_datetime limit 1",item.getId());
			if (operation != null) {
				dealJSON = new JSONObject();
				dealJSON.put("stock_name", operation.getStock_name());
				dealJSON.put("price", operation.getOperation_price());
				dealJSON.put("way", operation.getOperation_tag());
				dealJSON.put("quantity", operation.getOperation_quantity());
				dealJSON.put("datetime", sDate_time.format(operation.getOperation_datetime()));
			}
			jsonObj.put("latest_operation", dealJSON);
			
			String json = jsonObj.toJSONString();
			list.add(json);
		}
		
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return list;
	}
	
//	public static void main(String[] args) {
//		DbManager.start();
//		String categories = "公司";
//		String portfolio_categories = "%" +categories+ "%";
//		AppPagerQuery aQuery = new AppPagerQuery();
//		aQuery.getPagerSimple(portfolio_categories, 0, 20);
//	}
}
 