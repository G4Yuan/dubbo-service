package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/**
* 
* @ClassName: Adviser 
* @Description: 投顾人员信息
* @author yuanwei  
* @date 2017年6月27日 下午5:56:59 
*/
@SuppressWarnings("all")
public class Adviser extends Model<Adviser>{

	private static final long serialVersionUID = 1L;
	public static Adviser dao = new Adviser();
	private java.lang.Integer id;
	private java.lang.String user_object_id;                     //投顾 Object_ID
	private java.lang.String name;                               //投顾人姓名
	private java.lang.String logo;                               //投顾人logo
	private java.lang.String summary;                            //投顾简介
	private java.lang.String qualificationName;                  //资格证书名称
	private java.lang.String qualificationNum;                   //资格证书
	private java.lang.Integer favorite_count;                    //喜欢点数
	private java.lang.Integer average_stock;                     //平均股票
	private java.lang.Integer handle_stock_num;                  //持股数量
	private java.lang.Integer month_deal_times;                  //月交易次数
    private java.lang.Double week_yield;                         //周收益
    private java.lang.Double month_yield;                        //月收益
    private java.lang.Double total_yield;                        //总收益
    private java.lang.Double year_yield;                         //年化收益
    private java.lang.Double success_ratio;                      //成功率
    private java.lang.Double max_drawdown;                       //最大回撤
    private java.lang.Double position;                           //当前仓位
	private java.util.Date createdAt;                            //创建时间
	private java.util.Date updatedAt;                            //更新时间
	public java.lang.Integer getId() { 
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.String getUser_object_id() {
		return this.get("user_object_id");
	}
	public void setUser_object_id(java.lang.String user_object_id) {
		this.set("user_object_id", user_object_id);
	}
	public java.lang.String getName() {
		return this.get("name");
	}
	public void setName(java.lang.String name) {
		this.set("name", name);
	}
	public java.lang.String getLogo() {
		return this.get("logo");
	}
	public void setLogo(java.lang.String logo) {
		this.set("logo", logo);
	}
	public java.lang.String getSummary() {
		return this.get("summary");
	}
	public void setSummary(java.lang.String summary) {
		this.set("summary", summary);
	}
	public java.lang.String getQualificationName() {
		return this.get("qualificationName");
	}
	public void setQualificationName(java.lang.String qualificationName) {
		this.set("qualificationName", qualificationName);
	}
	public java.lang.String getQualificationNum() {
		return this.get("qualificationNum");
	}
	public void setQualificationNum(java.lang.String qualificationNum) {
		this.set("qualificationNum", qualificationNum);
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
	public java.lang.Integer getFavorite_count() {
		return this.get("favorite_count");
	}
	public void setFavorite_count(java.lang.Integer favorite_count) {
		this.set("favorite_count", favorite_count);
	}
	public java.lang.Integer getAverage_stock() {
		return this.get("average_stock");
	}
	public void setAverage_stock(java.lang.Integer average_stock) {
		this.set("average_stock", average_stock);
	}
	public java.lang.Integer getHandle_stock_num() {
		return this.get("handle_stock_num");
	}
	public void setHandle_stock_num(java.lang.Integer handle_stock_num) {
		this.set("handle_stock_num", handle_stock_num);
	}
	public java.lang.Integer getMonth_deal_times() {
		return this.get("month_deal_times");
	}
	public void setMonth_deal_times(java.lang.Integer month_deal_times) {
		this.set("month_deal_times", month_deal_times);
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
	public java.lang.Double getYear_yield() {
		return this.get("year_yield");
	}
	public void setYear_yield(java.lang.Double year_yield) {
		this.set("year_yield", year_yield);
	}
	public java.lang.Double getSuccess_ratio() {
		return this.get("success_ratio");
	}
	public void setSuccess_ratio(java.lang.Double success_ratio) {
		this.set("success_ratio", success_ratio);
	}
	public java.lang.Double getMax_drawdown() {
		return this.get("max_drawdown");
	}
	public void setMax_drawdown(java.lang.Double max_drawdown) {
		this.set("max_drawdown", max_drawdown);
	}
	public java.lang.Double getPosition() {
		return this.get("position");
	}
	public void setPosition(java.lang.Double position) {
		this.set("position", position);
	}
	
}
 