package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: PortfolioDeal 
* @Description: 组合流水 
* @author yuanwei  
* @date 2017年6月29日 下午4:25:23 
* @version V1.0 
*/
@SuppressWarnings("all")
public class PortfolioDeal extends Model<PortfolioDeal>{

	private static final long serialVersionUID = 1L;
	public static PortfolioDeal dao = new PortfolioDeal();
	private java.lang.Integer id;
	private java.lang.String stock_name;                      //股票名称
	private java.lang.String stock_code;                      //股票代码	
	private java.lang.String objectId;                        //流水objectid
	private java.lang.Double price;                           //价格
	private java.util.Date deal_datetime;                     //流水时间
	private java.lang.Double yield;                           //收益
	private java.lang.Double turnover;                        //变更率
	private java.lang.String way;                             //方式
	private java.lang.String market_code;                     //市场代码
	private java.lang.String accountId;                       //资金ID
	private java.lang.Integer recorder_id;                    //记录ID
	private java.lang.Integer quantity;                       //数量
	private java.util.Date createdAt;                         //创建时间
	private java.util.Date updatedAt;                         //更新时间
	private java.lang.Integer portfolio_id;                   //组合ID
	
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.Integer getRecorder_id() {
		return this.get("recorder_id");
	}
	public void setRecorder_id(java.lang.Integer recorder_id) {
		this.set("recorder_id", recorder_id);
	}
	public java.util.Date getDeal_datetime() {
		return this.get("deal_datetime");
	}
	public void setDeal_datetime(java.util.Date deal_datetime) {
		this.set("deal_datetime", deal_datetime);
	}
	public java.lang.String getStock_name() {
		return this.get("stock_name");
	}
	public void setStock_name(java.lang.String stock_name) {
		this.set("stock_name", stock_name);
	}
	public java.lang.String getStock_code() {
		return this.get("stock_code");
	}
	public void setStock_code(java.lang.String stock_code) {
		this.set("stock_code", stock_code);
	}
	public java.lang.String getObjectId() {
		return this.get("objectId");
	}
	public void setObjectId(java.lang.String objectId) {
		this.set("objectId", objectId);
	}
	public java.lang.Double getPrice() {
		return this.get("price");
	}
	public void setPrice(java.lang.Double price) {
		this.set("price", price);
	}
	public java.lang.Double getYield() {
		return this.get("yield");
	}
	public void setYield(java.lang.Double yield) {
		this.set("yield", yield);
	}
	public java.lang.Double getTurnover() {
		return this.get("turnover");
	}
	public void setTurnover(java.lang.Double turnover) {
		this.set("turnover", turnover);
	}
	public java.lang.String getWay() {
		return this.get("way");
	}
	public void setWay(java.lang.String way) {
		this.set("way", way);
	}
	public java.lang.String getMarket_code() {
		return this.get("market_code");
	}
	public void setMarket_code(java.lang.String market_code) {
		this.set("market_code", market_code);
	}
	public java.lang.String getAccountId() {
		return this.get("accountId");
	}
	public void setAccountId(java.lang.String accountId) {
		this.set("accountId", accountId);
	}
	public java.lang.Integer getQuantity() {
		return this.get("quantity");
	}
	public void setQuantity(java.lang.Integer quantity) {
		this.set("quantity", quantity);
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
	public java.lang.Integer getPortfolio_id() {
		return this.get("portfolio_id");
	}
	public void setPortfolio_id(java.lang.Integer portfolio_id) {
		this.set("portfolio_id", portfolio_id);
	}
}
 