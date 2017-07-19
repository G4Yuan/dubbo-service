package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: HistoryYield 
* @Description: 历史收益
* @author yuanwei
* @date 2017年7月3日 下午1:12:48 
* @version V1.0 
*/
@SuppressWarnings("all")
public class HistoryYield extends Model<HistoryYield>{

	private static final long serialVersionUID = 1L;
	public static HistoryYield dao = new HistoryYield();	
	private java.lang.Integer id;
	private java.util.Date trading_day;                      //交易日
	private java.lang.Double yield;                          //收益
	private java.lang.Double SZ399300_yield;                 //沪深300同期收益
	private java.lang.Integer portfolio_id;
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.util.Date getTrading_day() {
		return this.get("trading_day");
	}
	public void setTrading_day(java.util.Date trading_day) {
		this.set("trading_day", trading_day);
	}
	public java.lang.Double getYield() {
		return this.get("yield");
	}
	public void setYield(java.lang.Double yield) {
		this.set("yield", yield);
	}
	public java.lang.Double getSZ399300_yield() {
		return this.get("SZ399300_yield");
	}
	public void setSZ399300_yield(java.lang.Double SZ399300_yield) {
		this.set("SZ399300_yield", SZ399300_yield);
	}
	public java.lang.Integer getPortfolio_id() {
		return this.get("portfolio_id");
	}
	public void setPortfolio_id(java.lang.Integer portfolio_id) {
		this.set("portfolio_id", portfolio_id);
	}
	
}
 