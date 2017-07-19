package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: PortfolioYield 
* @Description: 组合收益 
* @author yuanwei  
* @date 2017年6月30日 下午1:57:30 
* @version V1.0 
*/
@SuppressWarnings("all")
public class PortfolioYield extends Model<PortfolioYield>{

	private static final long serialVersionUID = 1L;
	public static PortfolioYield dao = new PortfolioYield();
	private java.lang.Integer id;
	private java.lang.Double today_yield;                     //日收益
	private java.lang.Double week_yield;                      //周收益	
	private java.lang.Double month_yield;                     //月收益
	private java.lang.Double total_yield;                     //总收益
	private java.lang.Double annualized_yield;                //年化收益
	private java.lang.Double total_value;                     //总资产
	private java.lang.Double cash;                            //可用资金
	private java.lang.Double current_position;                //当前仓位
	private java.lang.Double SZ399300_yield;                  //沪深300同期收益
	private java.lang.Double success_ratio;                   //成功率
	private java.lang.Double win_ratio;                       //胜率
	private java.lang.Double max_drawdown;                    //最大回撤
	private java.lang.Double month_max_drawdown;              //月最大回撤
	private java.lang.Integer portfolio_id;                   //组合ID
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.Double getToday_yield() {
		return this.get("today_yield");
	}
	public void setToday_yield(java.lang.Double today_yield) {
		this.set("today_yield", today_yield);
	}
	public java.lang.Double getWeek_yield() {
		return this.get("week_yield");
	}
	public void setWeek_yield(java.lang.Double week_yield) {
		this.set("week_yield", week_yield);
	}
	public java.lang.Double getMonth_yield() {
		return this.get("month_yield");
	}
	public void setMonth_yield(java.lang.Double month_yield) {
		this.set("month_yield", month_yield);
	}
	public java.lang.Double getTotal_yield() {
		return this.get("total_yield");
	}
	public void setTotal_yield(java.lang.Double total_yield) {
		this.set("total_yield", total_yield);
	}
	public java.lang.Double getAnnualized_yield() {
		return this.get("annualized_yield");
	}
	public void setAnnualized_yield(java.lang.Double annualized_yield) {
		this.set("annualized_yield", annualized_yield);
	}
	public java.lang.Double getTotal_value() {
		return this.get("total_value");
	}
	public void setTotal_value(java.lang.Double total_value) {
		this.set("total_value", total_value);
	}
	public java.lang.Double getCash() {
		return this.get("cash");
	}
	public void setCash(java.lang.Double cash) {
		this.set("cash", cash);
	}
	public java.lang.Double getCurrent_position() {
		return this.get("current_position");
	}
	public void setCurrent_position(java.lang.Double current_position) {
		this.set("current_position", current_position);
	}
	public java.lang.Double getSZ399300_yield() {
		return this.get("SZ399300_yield");
	}
	public void setSZ399300_yield(java.lang.Double SZ399300_yield) {
		this.set("SZ399300_yield", SZ399300_yield);
	}
	public java.lang.Double getSuccess_ratio() {
		return this.get("success_ratio");
	}
	public void setSuccess_ratio(java.lang.Double success_ratio) {
		this.set("success_ratio", success_ratio);
	}
	public java.lang.Double getWin_ratio() {
		return this.get("win_ratio");
	}
	public void setWin_ratio(java.lang.Double win_ratio) {
		this.set("win_ratio", win_ratio);
	}
	public java.lang.Double getMax_drawdown() {
		return this.get("max_drawdown");
	}
	public void setMax_drawdown(java.lang.Double max_drawdown) {
		this.set("max_drawdown", max_drawdown);
	}
	public java.lang.Double getMonth_max_drawdown() {
		return this.get("month_max_drawdown");
	}
	public void setMonth_max_drawdown(java.lang.Double month_max_drawdown) {
		this.set("month_max_drawdown", month_max_drawdown);
	}
	public java.lang.Integer getPortfolio_id() {
		return this.get("portfolio_id");
	}
	public void setPortfolio_id(java.lang.Integer portfolio_id) {
		this.set("portfolio_id", portfolio_id);
	}

}
 