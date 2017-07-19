package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;


/** 
* @ClassName: HistoryStocks 
* @Description: 历史个股 
* @author yuanwei  
* @date 2017年6月29日 下午6:05:39 
* @version V1.0 
*/
@SuppressWarnings("all")
public class HistoryStocks extends Model<HistoryStocks>{

	private static final long serialVersionUID = 1L;
	public static HistoryStocks dao = new HistoryStocks();
	private java.lang.Integer id;
	private java.lang.String stock_name;                     //股票名称
	private java.lang.String stock_code;                     //股票代码
	private java.lang.String market_code;                    //市场代码
	private java.lang.Double buy_price;                      //买入价格
	private java.lang.Double sell_price;                     //卖出价格
	private java.lang.Double price;                          //当前价格
	private java.lang.String stock_info;                     //股票信息
	private java.lang.Double yield;                          //收益
	private java.util.Date operation_datetime;               //操作时间
	private java.lang.Integer portfolio_id;
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.String getStock_code() {
		return this.get("stock_code");
	}
	public void setStock_code(java.lang.String stock_code) {
		this.set("stock_code", stock_code);
	}
	public java.lang.Integer getPortfolio_id() {
		return this.get("portfolio_id");
	}
	public void setPortfolio_id(java.lang.Integer portfolio_id) {
		this.set("portfolio_id", portfolio_id);
	}
	public java.lang.String getStock_name() {
		return this.get("stock_name");
	}
	public void setStock_name(java.lang.String stock_name) {
		this.set("stock_name", stock_name);
	}
	public java.lang.String getMarket_code() {
		return this.get("market_code");
	}
	public void setMarket_code(java.lang.String market_code) {
		this.set("market_code", market_code);
	}
	public java.lang.Double getBuy_price() {
		return this.get("buy_price");
	}
	public void setBuy_price(java.lang.Double buy_price) {
		this.set("buy_price", buy_price);
	}
	public java.lang.Double getSell_price() {
		return this.get("sell_price");
	}
	public void setSell_price(java.lang.Double sell_price) {
		this.set("sell_price", sell_price);
	}
	public java.lang.Double getPrice() {
		return this.get("price");
	}
	public void setPrice(java.lang.Double price) {
		this.set("price", price);
	}
	public java.lang.String getStock_info() {
		return this.get("stock_info");
	}
	public void setStock_info(java.lang.String stock_info) {
		this.set("stock_info", stock_info);
	}
	public java.lang.Double getYield() {
		return this.get("yield");
	}
	public void setYield(java.lang.Double yield) {
		this.set("yield", yield);
	}
	public java.util.Date getOperation_datetime() {
		return this.get("operation_datetime");
	}
	public void setOperation_datetime(java.util.Date operation_datetime) {
		this.set("operation_datetime", operation_datetime);
	}
	
}
 