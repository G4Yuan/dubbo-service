package com.qktz.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.avos.avoscloud.AVException;
import com.jfinal.plugin.activerecord.Model;
import com.qktz.consts.DataQueryMC;
import com.qktz.service.thrift.intelligent.BusinessException;
import com.qktz.service.thrift.intelligent.ChinaStocksVane;
import com.qktz.service.thrift.intelligent.PortfolioInfo;
import com.qktz.service.thrift.intelligent.PortfolioPager;



/** 
* @ClassName: Portfolio 
* @Description: 高手组合 
* @author yuanwei  
* @date 2017年6月27日 下午4:34:08 
*/
@SuppressWarnings("all")
public class Portfolio extends Model<Portfolio>{

	private static final long serialVersionUID = 1L;
	public static Portfolio dao = new Portfolio();
	private java.lang.Integer id;
	private java.lang.String portfolio_title;                     //组合标题
	private java.lang.String portfolio_summary;                   //组合简介
	private java.lang.String portfolio_labels;                    //组合标签
	private java.lang.Integer risk_level;                         //风险评级 
	private java.util.Date valid_start_date;                      //组合开始日期
	private java.lang.Double init_value;                          //初始资金
	private java.lang.String portfolio_categories;                //组合类别
	private java.lang.Integer portfolio_status;                   //组合状态 
	private java.lang.String account_id;                          //资金账号_ID
	private java.lang.String user_object_id;                      //创建信息_obejct_ID
	private java.util.Date createdAt;                             //创建时间
	private java.util.Date updatedAt;                             //更新时间
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.String getPortfolio_title() {
		return this.get("portfolio_title");
	}
	public void setPortfolio_title(java.lang.String portfolio_title) {
		this.set("portfolio_title", portfolio_title);
	}
	public java.lang.String getPortfolio_summary() {
		return this.get("portfolio_summary");
	}
	public void setPortfolio_summary(java.lang.String portfolio_summary) {
		this.set("portfolio_summary", portfolio_summary);
	}
	public java.lang.String getPortfolio_labels() {
		return this.get("portfolio_labels");
	}
	public void setPortfolio_labels(java.lang.String portfolio_labels) {
		this.set("portfolio_labels", portfolio_labels);
	}
	public java.lang.Integer getRisk_level() {
		return this.get("risk_level");
	}
	public void setRisk_level(java.lang.Integer risk_level) {
		this.set("risk_level", risk_level);
	}
	public java.util.Date getValid_start_date() {
		return this.get("valid_start_date");
	}
	public void setValid_start_date(java.util.Date valid_start_date) {
		this.set("valid_start_date", valid_start_date);
	}
	public java.lang.Double getInit_value() {
		return this.get("init_value");
	}
	public void setInit_value(java.lang.Double init_value) {
		this.set("init_value", init_value);
	}
	public java.lang.String getPortfolio_categories() {
		return this.get("portfolio_categories");
	}
	public void setPortfolio_categories(java.lang.String portfolio_categories) {
		this.set("portfolio_categories", portfolio_categories);
	}
	public java.lang.Integer getPortfolio_status() {
		return this.get("portfolio_status");
	}
	public void setPortfolio_status(java.lang.Integer portfolio_status) {
		this.set("portfolio_status", portfolio_status);
	}
	public java.lang.String getAccount_id() {
		return this.get("account_id");
	}
	public void setAccount_id(java.lang.String account_id) {
		this.set("account_id", account_id);
	}
	public java.util.Date getCreatedAt() {
		return this.get("createdAt");
	}
	public void setCreatedAt(java.util.Date createdAt) {
		this.set("createdAt", createdAt);
	}
	public java.util.Date getUpdatedAt() {
		return this.get("updatedAt");
	}
	public void setUpdatedAt(java.util.Date updatedAt) {
		this.set("updatedAt", updatedAt);
	}
	public java.lang.String getUser_object_id() {
		return this.get("user_object_id");
	}
	public void setUser_object_id(java.lang.String user_object_id) {
		this.set("user_object_id", user_object_id);
	}
	//无条件查询
	public List<PortfolioInfo> findPortfolio(int offset, int pageSize){
		//格式化时间
		SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<PortfolioInfo> portfolioInfos = new ArrayList<PortfolioInfo>();
		PortfolioInfo portfolioInfo = null;
		StringBuilder sql = new StringBuilder("select * from portfolio where 1= 1");
		List<Portfolio> obj = new ArrayList<Portfolio>();
		if(pageSize != 0){
			sql.append(" order by createdAt desc limit ?,?");
			obj = Portfolio.dao.find(sql.toString(), new Object[]{offset, pageSize});
		}else{
			sql.append(" order by createdAt desc");
			obj = Portfolio.dao.find(sql.toString());
		}
		PortfolioCreator creator = null;
		for(Portfolio item:obj){
			portfolioInfo = new PortfolioInfo();
			if (StringUtils.isNotBlank(item.getUser_object_id())) {
				creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",item.getUser_object_id());
				if(creator != null){
					portfolioInfo.setUser_object_id(creator.getUser_object_id());
					portfolioInfo.setUser_name(creator.getUser_name());
					portfolioInfo.setUser_logo(creator.getUser_logo());
					portfolioInfo.setUser_summary(creator.getUser_summary());	
					portfolioInfo.setQualificationName(creator.getQualificationName());
					portfolioInfo.setQualificationNum(creator.getQualificationNum());
				}else {
					portfolioInfo.setUser_object_id(item.getUser_object_id());
					DataQueryMC mc = new DataQueryMC();
					PortfolioCreator pCreator = null;
					try {
						pCreator = mc.getBossUser(item.getUser_object_id());
						if (pCreator != null) {
							portfolioInfo.setUser_name(pCreator.getUser_name());
							portfolioInfo.setUser_logo(pCreator.getUser_logo());
							portfolioInfo.setUser_summary(pCreator.getUser_summary());	
							portfolioInfo.setQualificationName(pCreator.getQualificationName());
							portfolioInfo.setQualificationNum(pCreator.getQualificationNum());
						}
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}
			}
			portfolioInfo.setPfl_id(item.getId());
			portfolioInfo.setPortfolio_title(item.getPortfolio_title());
			portfolioInfo.setPortfolio_summary(item.getPortfolio_summary());
			portfolioInfo.setPortfolio_labels(item.getPortfolio_labels());
			portfolioInfo.setRisk_level(item.getRisk_level());
			portfolioInfo.setValid_start_date(sDate_time.format(item.getValid_start_date()));
			portfolioInfo.setInit_value(item.getInit_value());
			portfolioInfo.setPortfolio_categories(item.getPortfolio_categories());
			portfolioInfo.setPortfolio_status(item.getPortfolio_status());
			portfolioInfo.setAccount_id(item.getAccount_id());			
			portfolioInfo.setCreatedAt(sDate_time.format(item.getCreatedAt()));
			portfolioInfo.setUpdatedAt(sDate_time.format(item.getUpdatedAt()));
			portfolioInfos.add(portfolioInfo);
		}
		return portfolioInfos;
	}
	
	//根据名称查询
	public List<PortfolioInfo> findPortfolioByTitle(String title, int offset, int pageSize){
		//格式化时间
		SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<PortfolioInfo> portfolioInfos = new ArrayList<PortfolioInfo>();
		PortfolioInfo portfolioInfo = null;
		StringBuilder sql = new StringBuilder("select * from portfolio where 1= 1");
		List<Portfolio> obj = new ArrayList<Portfolio>();
		if(pageSize != 0){
			sql.append(" and portfolio_title like ? order by createdAt desc limit ?,?");
			obj = Portfolio.dao.find(sql.toString(), new Object[]{title, offset, pageSize});
		}else{
			sql.append(" and portfolio_title like ? order by createdAt desc");
			obj = Portfolio.dao.find(sql.toString(),new Object[]{title});
		}
		PortfolioCreator creator = null;
		for(Portfolio item:obj){
			portfolioInfo = new PortfolioInfo();
			if (StringUtils.isNotBlank(item.getUser_object_id())) {
				creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",item.getUser_object_id());
				if(creator != null){
					portfolioInfo.setUser_object_id(creator.getUser_object_id());
					portfolioInfo.setUser_name(creator.getUser_name());
					portfolioInfo.setUser_logo(creator.getUser_logo());
					portfolioInfo.setUser_summary(creator.getUser_summary());	
					portfolioInfo.setQualificationName(creator.getQualificationName());
					portfolioInfo.setQualificationNum(creator.getQualificationNum());
				}else {
					portfolioInfo.setUser_object_id(item.getUser_object_id());
					DataQueryMC mc = new DataQueryMC();
					PortfolioCreator pCreator = null;
					try {
						pCreator = mc.getBossUser(item.getUser_object_id());
						if (pCreator != null) {
							portfolioInfo.setUser_name(pCreator.getUser_name());
							portfolioInfo.setUser_logo(pCreator.getUser_logo());
							portfolioInfo.setUser_summary(pCreator.getUser_summary());	
							portfolioInfo.setQualificationName(pCreator.getQualificationName());
							portfolioInfo.setQualificationNum(pCreator.getQualificationNum());
						}
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}
			}
			portfolioInfo.setPfl_id(item.getId());
			portfolioInfo.setPortfolio_title(item.getPortfolio_title());
			portfolioInfo.setPortfolio_summary(item.getPortfolio_summary());
			portfolioInfo.setPortfolio_labels(item.getPortfolio_labels());
			portfolioInfo.setRisk_level(item.getRisk_level());
			portfolioInfo.setValid_start_date(sDate_time.format(item.getValid_start_date()));
			portfolioInfo.setInit_value(item.getInit_value());
			portfolioInfo.setPortfolio_categories(item.getPortfolio_categories());
			portfolioInfo.setPortfolio_status(item.getPortfolio_status());
			portfolioInfo.setAccount_id(item.getAccount_id());			
			portfolioInfo.setCreatedAt(sDate_time.format(item.getCreatedAt()));
			portfolioInfo.setUpdatedAt(sDate_time.format(item.getUpdatedAt()));
			portfolioInfos.add(portfolioInfo);
		}
		return portfolioInfos;
	}
	
	//根据时间查询
	public List<PortfolioInfo> findPortfolioByTime(String create_time, int offset, int pageSize){
			//格式化时间
			SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			List<PortfolioInfo> portfolioInfos = new ArrayList<PortfolioInfo>();
			PortfolioInfo portfolioInfo = null;
			StringBuilder sql = new StringBuilder("select * from portfolio where 1= 1");
			List<Portfolio> obj = new ArrayList<Portfolio>();
			if(pageSize != 0){
				sql.append(" and createdAt like ? order by createdAt desc limit ?,?");
				obj = Portfolio.dao.find(sql.toString(), new Object[]{create_time, offset, pageSize});
			}else{
				sql.append(" and createdAt like ? order by createdAt desc");
				obj = Portfolio.dao.find(sql.toString(),new Object[]{create_time});
			}
			PortfolioCreator creator = null;
			for(Portfolio item:obj){
				portfolioInfo = new PortfolioInfo();
				if (StringUtils.isNotBlank(item.getUser_object_id())) {
					creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",item.getUser_object_id());
					if(creator != null){
						portfolioInfo.setUser_object_id(creator.getUser_object_id());
						portfolioInfo.setUser_name(creator.getUser_name());
						portfolioInfo.setUser_logo(creator.getUser_logo());
						portfolioInfo.setUser_summary(creator.getUser_summary());	
						portfolioInfo.setQualificationName(creator.getQualificationName());
						portfolioInfo.setQualificationNum(creator.getQualificationNum());
					}else {
						portfolioInfo.setUser_object_id(item.getUser_object_id());
						DataQueryMC mc = new DataQueryMC();
						PortfolioCreator pCreator = null;
						try {
							pCreator = mc.getBossUser(item.getUser_object_id());
							if (pCreator != null) {
								portfolioInfo.setUser_name(pCreator.getUser_name());
								portfolioInfo.setUser_logo(pCreator.getUser_logo());
								portfolioInfo.setUser_summary(pCreator.getUser_summary());	
								portfolioInfo.setQualificationName(pCreator.getQualificationName());
								portfolioInfo.setQualificationNum(pCreator.getQualificationNum());
							}
						} catch (BusinessException e) {
							e.printStackTrace();
						}
					}
				}
				portfolioInfo.setPfl_id(item.getId());
				portfolioInfo.setPortfolio_title(item.getPortfolio_title());
				portfolioInfo.setPortfolio_summary(item.getPortfolio_summary());
				portfolioInfo.setPortfolio_labels(item.getPortfolio_labels());
				portfolioInfo.setRisk_level(item.getRisk_level());
				portfolioInfo.setValid_start_date(sDate_time.format(item.getValid_start_date()));
				portfolioInfo.setInit_value(item.getInit_value());
				portfolioInfo.setPortfolio_categories(item.getPortfolio_categories());
				portfolioInfo.setPortfolio_status(item.getPortfolio_status());
				portfolioInfo.setAccount_id(item.getAccount_id());			
				portfolioInfo.setCreatedAt(sDate_time.format(item.getCreatedAt()));
				portfolioInfo.setUpdatedAt(sDate_time.format(item.getUpdatedAt()));
				portfolioInfos.add(portfolioInfo);
			}
			return portfolioInfos;
		}
	//根据时间查询
	public List<PortfolioInfo> findPortfolioByStatus(int status, int offset, int pageSize){
			//格式化时间
			SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			List<PortfolioInfo> portfolioInfos = new ArrayList<PortfolioInfo>();
			PortfolioInfo portfolioInfo = null;
			StringBuilder sql = new StringBuilder("select * from portfolio where 1= 1");
			List<Portfolio> obj = new ArrayList<Portfolio>();
			if(pageSize != 0){
				sql.append(" and portfolio_status= ? order by createdAt desc limit ?,?");
				obj = Portfolio.dao.find(sql.toString(), new Object[]{status, offset, pageSize});
			}else{
				sql.append(" and portfolio_status= ? order by createdAt desc");
				obj = Portfolio.dao.find(sql.toString(),new Object[]{status});
			}
			PortfolioCreator creator = null;
			for(Portfolio item:obj){
				portfolioInfo = new PortfolioInfo();
				if (StringUtils.isNotBlank(item.getUser_object_id())) {
					creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",item.getUser_object_id());
					if(creator != null){
						portfolioInfo.setUser_object_id(creator.getUser_object_id());
						portfolioInfo.setUser_name(creator.getUser_name());
						portfolioInfo.setUser_logo(creator.getUser_logo());
						portfolioInfo.setUser_summary(creator.getUser_summary());	
						portfolioInfo.setQualificationName(creator.getQualificationName());
						portfolioInfo.setQualificationNum(creator.getQualificationNum());
					}else {
						portfolioInfo.setUser_object_id(item.getUser_object_id());
						DataQueryMC mc = new DataQueryMC();
						PortfolioCreator pCreator = null;
						try {
							pCreator = mc.getBossUser(item.getUser_object_id());
							if (pCreator != null) {
								portfolioInfo.setUser_name(pCreator.getUser_name());
								portfolioInfo.setUser_logo(pCreator.getUser_logo());
								portfolioInfo.setUser_summary(pCreator.getUser_summary());	
								portfolioInfo.setQualificationName(pCreator.getQualificationName());
								portfolioInfo.setQualificationNum(pCreator.getQualificationNum());
							}
						} catch (BusinessException e) {
							e.printStackTrace();
						}
					}
				}
				portfolioInfo.setPfl_id(item.getId());
				portfolioInfo.setPortfolio_title(item.getPortfolio_title());
				portfolioInfo.setPortfolio_summary(item.getPortfolio_summary());
				portfolioInfo.setPortfolio_labels(item.getPortfolio_labels());
				portfolioInfo.setRisk_level(item.getRisk_level());
				portfolioInfo.setValid_start_date(sDate_time.format(item.getValid_start_date()));
				portfolioInfo.setInit_value(item.getInit_value());
				portfolioInfo.setPortfolio_categories(item.getPortfolio_categories());
				portfolioInfo.setPortfolio_status(item.getPortfolio_status());
				portfolioInfo.setAccount_id(item.getAccount_id());			
				portfolioInfo.setCreatedAt(sDate_time.format(item.getCreatedAt()));
				portfolioInfo.setUpdatedAt(sDate_time.format(item.getUpdatedAt()));
				portfolioInfos.add(portfolioInfo);
			}
			return portfolioInfos;
		}
	//根据类别查询
	public List<PortfolioInfo> findPortfolioByCategories(String categories, int offset, int pageSize){
			//格式化时间
			SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			List<PortfolioInfo> portfolioInfos = new ArrayList<PortfolioInfo>();
			PortfolioInfo portfolioInfo = null;
			StringBuilder sql = new StringBuilder("select * from portfolio where 1= 1");
			List<Portfolio> obj = new ArrayList<Portfolio>();
			if(pageSize != 0){
				sql.append(" and portfolio_categories like ? order by createdAt desc limit ?,?");
				obj = Portfolio.dao.find(sql.toString(), new Object[]{categories, offset, pageSize});
			}else{
				sql.append(" and portfolio_categories like ? order by createdAt desc");
				obj = Portfolio.dao.find(sql.toString(),new Object[]{categories});
			}
			PortfolioCreator creator = null;
			for(Portfolio item:obj){
				portfolioInfo = new PortfolioInfo();
				if (StringUtils.isNotBlank(item.getUser_object_id())) {
					creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",item.getUser_object_id());
					if(creator != null){
						portfolioInfo.setUser_object_id(creator.getUser_object_id());
						portfolioInfo.setUser_name(creator.getUser_name());
						portfolioInfo.setUser_logo(creator.getUser_logo());
						portfolioInfo.setUser_summary(creator.getUser_summary());	
						portfolioInfo.setQualificationName(creator.getQualificationName());
						portfolioInfo.setQualificationNum(creator.getQualificationNum());
					}else {
						portfolioInfo.setUser_object_id(item.getUser_object_id());
						DataQueryMC mc = new DataQueryMC();
						PortfolioCreator pCreator = null;
						try {
							pCreator = mc.getBossUser(item.getUser_object_id());
							if (pCreator != null) {
								portfolioInfo.setUser_name(pCreator.getUser_name());
								portfolioInfo.setUser_logo(pCreator.getUser_logo());
								portfolioInfo.setUser_summary(pCreator.getUser_summary());	
								portfolioInfo.setQualificationName(pCreator.getQualificationName());
								portfolioInfo.setQualificationNum(pCreator.getQualificationNum());
							}
						} catch (BusinessException e) {
							e.printStackTrace();
						}
					}
				}
				portfolioInfo.setPfl_id(item.getId());
				portfolioInfo.setPortfolio_title(item.getPortfolio_title());
				portfolioInfo.setPortfolio_summary(item.getPortfolio_summary());
				portfolioInfo.setPortfolio_labels(item.getPortfolio_labels());
				portfolioInfo.setRisk_level(item.getRisk_level());
				portfolioInfo.setValid_start_date(sDate_time.format(item.getValid_start_date()));
				portfolioInfo.setInit_value(item.getInit_value());
				portfolioInfo.setPortfolio_categories(item.getPortfolio_categories());
				portfolioInfo.setPortfolio_status(item.getPortfolio_status());
				portfolioInfo.setAccount_id(item.getAccount_id());			
				portfolioInfo.setCreatedAt(sDate_time.format(item.getCreatedAt()));
				portfolioInfo.setUpdatedAt(sDate_time.format(item.getUpdatedAt()));
				portfolioInfos.add(portfolioInfo);
			}
			return portfolioInfos;
		}
}
