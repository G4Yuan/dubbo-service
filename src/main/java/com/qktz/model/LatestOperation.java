package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: LatestOperation 
* @Description: 最新操作
* @author yuanwei 
* @date 2017年7月3日 下午1:58:08 
* @version V1.0 
*/
@SuppressWarnings("all")
public class LatestOperation extends Model<LatestOperation>{

	private static final long serialVersionUID = 1L;
	public static LatestOperation dao = new LatestOperation();
	private java.lang.Integer id;
	private java.lang.String stock_name;                         //股票名称
	private java.util.Date operation_datetime;                   //操作时间
	private java.lang.String stock_code;                         //股票代码
	private java.lang.String market_code;                        //市场代码
	private java.lang.Integer operation_quantity;                //操作数量
	private java.lang.Double operation_price;                    //操作价格
	private java.lang.String operation_tag;                      //操作方式
	private java.lang.Integer portfolio_id;
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.String getStock_name() {
		return this.get("stock_name");
	}
	public void setStock_name(java.lang.String stock_name) {
		this.set("stock_name", stock_name);
	}
	public java.util.Date getOperation_datetime() {
		return this.get("operation_datetime");
	}
	public void setOperation_datetime(java.util.Date operation_datetime) {
		this.set("operation_datetime", operation_datetime);
	}
	public java.lang.String getStock_code() {
		return this.get("stock_code");
	}
	public void setStock_code(java.lang.String stock_code) {
		this.set("stock_code", stock_code);
	}
	public java.lang.String getMarket_code() {
		return this.get("market_code");
	}
	public void setMarket_code(java.lang.String market_code) {
		this.set("market_code", market_code);
	}
	public java.lang.Integer getOperation_quantity() {
		return this.get("operation_quantity");
	}
	public void setOperation_quantity(java.lang.Integer operation_quantity) {
		this.set("operation_quantity", operation_quantity);
	}
	public java.lang.Double getOperation_price() {
		return this.get("operation_price");
	}
	public void setOperation_price(java.lang.Double operation_price) {
		this.set("operation_price", operation_price);
	}
	public java.lang.String getOperation_tag() {
		return this.get("operation_tag");
	}
	public void setOperation_tag(java.lang.String operation_tag) {
		this.set("operation_tag", operation_tag);
	}
	public java.lang.Integer getPortfolio_id() {
		return this.get("portfolio_id");
	}
	public void setPortfolio_id(java.lang.Integer portfolio_id) {
		this.set("portfolio_id", portfolio_id);
	}
	
}
 