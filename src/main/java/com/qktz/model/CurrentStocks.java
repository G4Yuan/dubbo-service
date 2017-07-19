package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: CurrentStocks 
* @Description: 当前股票
* @author yuanwei  
* @date 2017年6月30日 下午12:56:52 
* @version V1.0 
*/
@SuppressWarnings("all")
public class CurrentStocks extends Model<CurrentStocks>{

	private static final long serialVersionUID = 1L;
	public static CurrentStocks dao = new CurrentStocks();
	private java.lang.Integer id;
	private java.lang.String stock_name;                     //股票名称
	private java.lang.String stock_code;                     //股票代码
	private java.lang.String market_code;                    //市场代码
	private java.lang.Double cost_price;                     //成本价格
	private java.lang.Double last_price;                     //最终价格
	private java.lang.String stock_info;                     //股票信息
	private java.lang.Integer position;                      //仓位
	private java.lang.Integer quantity;                      //数量
	private java.lang.Double yield;                          //收益
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
	public java.lang.Double getCost_price() {
		return this.get("cost_price");
	}
	public void setCost_price(java.lang.Double cost_price) {
		this.set("cost_price", cost_price);
	}
	public java.lang.Double getLast_price() {
		return this.get("last_price");
	}
	public void setLast_price(java.lang.Double last_price) {
		this.set("last_price", last_price);
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
	public java.lang.Integer getPosition() {
		return this.get("position");
	}
	public void setPosition(java.lang.Integer position) {
		this.set("position", position);
	}
	public java.lang.Integer getQuantity() {
		return this.get("quantity");
	}
	public void setQuantity(java.lang.Integer quantity) {
		this.set("quantity", quantity);
	}
	
}
 