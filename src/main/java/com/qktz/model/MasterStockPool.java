package com.qktz.model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Model;
import com.qktz.consts.Constants;
import com.qktz.service.thrift.intelligent.AmoySharePoolInfo;
import com.qktz.service.thrift.intelligent.BaseMasterStockPool;
import com.qktz.service.thrift.intelligent.MasterQueryCondition;
import com.qktz.service.thrift.intelligent.MasterStockInfo;
import com.qktz.service.thrift.intelligent.StockInfo;
@SuppressWarnings("all")
public class MasterStockPool extends Model<MasterStockPool> {
	private static final long serialVersionUID = 1L;
	public static MasterStockPool dao = new MasterStockPool();
	private java.lang.Long id;
	private java.lang.String name;
	private java.lang.String strategyRemark;
	private java.lang.String summary;
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
	/**
	 * 分页查询公告淘股数据
	 * @return
	 */
	public List<BaseMasterStockPool> findByPager(MasterQueryCondition condition, int offset, int pageSize){
		StringBuffer sql = new StringBuffer(Constants.MASTER_STOCK_UNION);
		if(condition != null){
			if(StringUtils.isNotBlank(condition.getName()))
				sql.append(" and d.name like '%"+condition.getName()+"%'");
			if(condition.getStatus() >0 )
				sql.append(" and d.status="+condition.getStatus());
		}
		sql.append(" order by d.create_time desc");
		List<MasterStockPool> info_list = MasterStockPool.dao.find(sql.toString());
		if(pageSize != 0){
			sql.append(" limit ?,? ");
			info_list = MasterStockPool.dao.find(sql.toString(), new Object[]{offset, pageSize});
		}
		List<BaseMasterStockPool> ret = new ArrayList<BaseMasterStockPool>();
		BaseMasterStockPool info = null;
		for(MasterStockPool pool:info_list){
			info = new BaseMasterStockPool();
			info.setId(pool.getId());
			info.setName(pool.getName());
			info.setStatus(pool.getStatus());
			info.setSummary(pool.getSummary());
			String label = pool.getLabels() == null ? "" : pool.getLabels();
			info.setLabels(Arrays.asList(label.split(",")));
			info.setStocks(MasterStock.dao.findMasterStockInfoList(pool.getId(),1));
			info.setHistory_stocks(MasterStock.dao.findMasterStockInfoList(pool.getId(),3));
			info.setOperations(MasterStockPoolOperatorFlow.dao.findListByPoolId(pool.getId()));
			info.setCreator(pool.getCreator());
			info.setCreate_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(pool.getCreateTime()));
			info.setModifier(pool.getModifier());
			info.setModify_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(pool.getModifyTime()));
			info.setOper_timestamp(pool.getModifyTime().getTime());
			ret.add(info);
		}
		return ret;
	}
}