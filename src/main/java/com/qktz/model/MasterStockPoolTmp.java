package com.qktz.model;
import com.jfinal.plugin.activerecord.Model;
public class MasterStockPoolTmp extends Model<MasterStockPoolTmp> {
	private static final long serialVersionUID = 1L;
	public static MasterStockPoolTmp dao = new MasterStockPoolTmp();
	private java.lang.Long id;
	private java.lang.String name;
	private java.lang.String summary;
	private java.lang.String strategyRemark;
	private java.lang.String labels;
	private java.lang.Integer status;
	private java.lang.Long creator;
	private java.util.Date createTime;
	private java.lang.Long modifier;
	private java.util.Date modifyTime;
	public java.lang.Long getId(){
		return this.get("id");
	}
	public void setId(java.lang.Long id){
		this.set("id",id);
	}
	public java.lang.String getName(){
		return this.get("name");
	}
	public void setName(java.lang.String name){
		this.set("name",name);
	}
	public java.lang.String getSummary(){
		return this.get("summary");
	}
	public void setSummary(java.lang.String summary){
		this.set("summary",summary);
	}
	
	public java.lang.String getStrategyRemark() {
		return this.get("strategy_remark");
	}
	public void setStrategyRemark(java.lang.String strategyRemark) {
		this.set("strategy_remark",strategyRemark);
	}
	public java.lang.String getLabels(){
		return this.get("labels");
	}
	public void setLabels(java.lang.String labels){
		this.set("labels",labels);
	}
	public java.lang.Integer getStatus(){
		return this.get("status");
	}
	public void setStatus(java.lang.Integer status){
		this.set("status",status);
	}
	public java.lang.Long getCreator(){
		return this.get("creator");
	}
	public void setCreator(java.lang.Long creator){
		this.set("creator",creator);
	}
	public java.util.Date getCreateTime(){
		return this.get("create_time");
	}
	public void setCreateTime(java.util.Date create_time){
		this.set("create_time",create_time);
	}
	public java.lang.Long getModifier(){
		return this.get("modifier");
	}
	public void setModifier(java.lang.Long modifier){
		this.set("modifier",modifier);
	}
	public java.util.Date getModifyTime(){
		return this.get("modify_time");
	}
	public void setModifyTime(java.util.Date modify_time){
		this.set("modify_time",modify_time);
	}
	public void copyToTemp(MasterStockPool stockPool){
		if(stockPool == null || stockPool.getId()<=0) return ;
		this.setId(stockPool.getId());
		this.setCreateTime(stockPool.getCreateTime());
		this.setCreator(stockPool.getCreator());
		this.setModifier(stockPool.getModifier());
		this.setModifyTime(stockPool.getModifyTime());
		this.setLabels(stockPool.getLabels());
		this.setName(stockPool.getName());
		this.setStatus(stockPool.getStatus());
		this.setSummary(stockPool.getSummary());
		this.setStrategyRemark(stockPool.getStrategyRemark());
		this.save();
	}
}