package com.qktz.function; 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kohsuke.rngom.digested.DDataPattern;

import com.jfinal.plugin.activerecord.Db;
import com.qktz.consts.Constants;
import com.qktz.mgr.DbManager;
import com.qktz.model.Adviser;
import com.qktz.model.Portfolio;
import com.qktz.model.PortfolioCreator;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2017年7月6日 上午11:50:06 
 * 类说明 
 */
public class Test implements Constants{

	 public static String ReadFile(String Path) {
	        BufferedReader reader = null;
	        String laststr = "";
	        try {
	            FileInputStream fileInputStream = new FileInputStream(Path);
	            InputStreamReader inputStreamReader = new InputStreamReader(
	                    fileInputStream, "utf-8");
	            reader = new BufferedReader(inputStreamReader);
	            String tempString = null;
	            while ((tempString = reader.readLine()) != null) {
	                laststr += tempString;
	            }
	            reader.close();
	            inputStreamReader.close();
	            fileInputStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	           try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	           
	        }
	        return laststr;
	    }
	/** 
     * @param args 
	 * @throws ParseException 
     */  
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws ParseException {  
    	DbManager.start();
            String data = ReadFile("C:\\Users\\Administrator\\Desktop\\test.json");
            com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(data);
           SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-ddHH: mm: ss");
           List<Portfolio> portfolios = new ArrayList<Portfolio>();
//           List<PortfolioCreator> portfolioCreators = new ArrayList<PortfolioCreator>();
            for (Object object : jsonArray) {
            	com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject) object;
            	Date aa = ss.parse(json.getString("valid_start_date"));
            	Portfolio portfolio = new Portfolio();
//			   PortfolioCreator creator = new PortfolioCreator();
			   portfolio.setPortfolio_title(json.getString("title"));
			   portfolio.setPortfolio_summary(json.getString("summary"));
			   portfolio.setPortfolio_labels(json.getJSONArray("labels").toJSONString());
			   portfolio.setRisk_level(json.getIntValue("risk_level"));
			   portfolio.setValid_start_date(sDate_time.parse(sDate_time.format(aa)));
			   portfolio.setInit_value(json.getDouble("init_value"));
			   portfolio.setPortfolio_categories(json.getJSONArray("portfolio_categories").toJSONString());
			   portfolio.setPortfolio_status(json.getIntValue("portfolio_status"));
			   portfolio.setAccount_id(json.getString("account"));
			   portfolio.setUser_object_id(json.getString("user_object_id"));
			   portfolio.setCreatedAt(sDate_time.parse(sDate_time.format(ss.parse(json.getString("createdAt")))));
			   portfolio.setUpdatedAt(sDate_time.parse(sDate_time.format(ss.parse(json.getString("updatedAt")))));
//			   creator.setCreator_logo(json.getString("creator_logo"));
//			   creator.setCreator_name(json.getString("creator_name"));
//			   creator.setCreator_object_id(json.getString("creator_object_id"));
//			   creator.setCreator_summary(json.getString("creator_summary"));
			   
			   portfolios.add(portfolio);
//			   portfolioCreators.add(creator);
            }
//            List<Adviser> advisers = new ArrayList<Adviser>();
//            for (Object object : jsonArray) {
//				com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject) object;
//				Adviser adviser = new Adviser();
//				adviser.setName(json.getString("name"));
//				adviser.setLogo(json.getString("logo"));
//				adviser.setSummary(json.getString("summary"));
//				adviser.setFavorite_count(json.getInteger("favorite_count"));
//				adviser.setAverage_stock(json.getInteger("average_stock"));
//				adviser.setHandle_stock_num(json.getInteger("handle_stock_num"));
//				adviser.setMonth_deal_times(json.getInteger("month_deal_times"));
//				adviser.setUser_object_id(json.getString("user_object_id"));
//				adviser.setCreatedAt(sDate_time.parse(json.getString("createdAt")));
//				adviser.setUpdatedAt(sDate_time.parse(json.getString("updatedAt")));
//				advisers.add(adviser);
//			}
//            Db.batchSave(advisers, 1000);
//            Db.batchSave(portfolioCreators, 1000);
            Db.batchSave(portfolios, 1000);
    }  
}
 