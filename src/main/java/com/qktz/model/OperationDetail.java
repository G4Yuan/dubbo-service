package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: OperationDetail 
* @Description: 操作细节展示
* @author yuanwei
* @date 2017年7月3日 下午5:54:53 
* @version V1.0 
*/
@SuppressWarnings("all")
public class OperationDetail extends Model<OperationDetail>{

	private static final long serialVersionUID = 1L;
	public static OperationDetail dao = new OperationDetail();
	private java.lang.Integer id;
	private java.lang.String data;                                      //数据
	private java.util.Date trading_day;                                 //交易日
	private java.lang.Integer portfolio_id;
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.String getData() {
		return this.get("data");
	}
	public void setData(java.lang.String data) {
		this.set("data", data);
	}
	public java.util.Date getTrading_day() {
		return this.get("trading_day");
	}
	public void setTrading_day(java.util.Date trading_day) {
		this.set("trading_day", trading_day);
	}
	public java.lang.Integer getPortfolio_id() {
		return this.get("portfolio_id");
	}
	public void setPortfolio_id(java.lang.Integer portfolio_id) {
		this.set("portfolio_id", portfolio_id);
	}
	
}
 