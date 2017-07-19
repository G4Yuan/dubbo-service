package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: PortfolioDealDynamics 
* @Description: 组合动态交易 
* @author yuanwei 
* @date 2017年7月3日 下午4:49:18 
* @version V1.0 
*/
@SuppressWarnings("all")
public class PortfolioDealDynamics extends Model<PortfolioDealDynamics>{

	private static final long serialVersionUID = 1L;
	public static PortfolioDealDynamics dao = new PortfolioDealDynamics();
    private java.lang.Integer id;
	private java.lang.String stock_name;                          //股票名称
    private java.lang.String owner_name;                          //持股人
    private java.lang.String stock_code;                          //股票代码
    private java.lang.String objectId;           
    private java.util.Date operation_datetime;                    //操作时间
    private java.lang.Double total_yield;                         //总收益
    private java.lang.String operation_tag;                       //操作方式
    private java.lang.Double operation_price;                     //操作价格
    private java.lang.String market_code;                         //市场代码
    private java.lang.String owner_avatar;                        //owner_logo
    private java.lang.Integer recorder_id;
    private java.lang.Integer operation_quantity;                 //操作数量
    private java.lang.String user_object_id;                      //投顾人ID
    private java.lang.String accountId;                           //资金ID
    private java.util.Date createdAt;                             //创建时间
    private java.util.Date updatedAt;                             //更新时间
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
	public java.lang.String getOwner_name() {
		return this.get("owner_name");
	}
	public void setOwner_name(java.lang.String owner_name) {
		this.set("owner_name", owner_name);
	}
	public java.lang.String getObjectId() {
		return this.get("objectId");
	}
	public void setObjectId(java.lang.String objectId) {
		this.set("objectId", objectId);
	}
	public java.lang.Double getTotal_yield() {
		return this.get("total_yield");
	}
	public void setTotal_yield(java.lang.Double total_yield) {
		this.set("total_yield", total_yield);
	}
	public java.lang.String getOwner_avatar() {
		return this.get("owner_avatar");
	}
	public void setOwner_avatar(java.lang.String owner_avatar) {
		this.set("owner_avatar", owner_avatar);
	}
	public java.lang.Integer getRecorder_id() {
		return this.get("recorder_id");
	}
	public void setRecorder_id(java.lang.Integer recorder_id) {
		this.set("recorder_id", recorder_id);
	}
	public java.lang.String getUser_object_id() {
		return this.get("user_object_id");
	}
	public void setUser_object_id(java.lang.String user_object_id) {
		this.set("user_object_id", user_object_id);
	}
	public java.lang.String getAccountId() {
		return this.get("accountId");
	}
	public void setAccountId(java.lang.String accountId) {
		this.set("accountId", accountId);
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

}
 