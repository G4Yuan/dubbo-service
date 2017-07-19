package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: PortfolioIndustryV 
* @Description: 
* @author yuanwei
* @date 2017年6月30日 下午8:30:57 
* @version V1.0 
*/
@SuppressWarnings("unused")
public class PortfolioIndustry extends Model<PortfolioIndustry>{

	private static final long serialVersionUID = 1L;
	public static PortfolioIndustry dao = new PortfolioIndustry();
	private java.lang.Integer id;
	private java.lang.String industry;                         //行业
	private java.lang.String stocks;                           //股票信息
	private java.lang.Double weight;                           //权重
	private java.lang.String w_from_v2;                        //是否是industry_v2
	private java.lang.Integer portfolio_id;
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.String getIndustry() {
		return this.get("industry");
	}
	public void setIndustry(java.lang.String industry) {
		this.set("industry", industry);
	}
	public java.lang.String getStocks() {
		return this.get("stocks");
	}
	public void setStocks(java.lang.String stocks) {
		this.set("stocks", stocks);
	}
	public java.lang.Double getWeight() {
		return this.getDouble("weight");
	}
	public void setWeight(java.lang.Double weight) {
		this.set("weight", weight);
	}
	public java.lang.String getW_from_v2() {
		return this.get("w_from_v2");
	}
	public void setW_from_v2(java.lang.String w_from_v2) {
		this.set("w_from_v2", w_from_v2);
	}
	public java.lang.Integer getPortfolio_id() {
		return this.get("portfolio_id");
	}
	public void setPortfolio_id(java.lang.Integer portfolio_id) {
		this.set("portfolio_id", portfolio_id);
	}
	
}
 