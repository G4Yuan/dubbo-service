package com.qktz.model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Model;
import com.qktz.consts.Constants;
import com.qktz.service.thrift.intelligent.AmoySharePoolInfo;
import com.qktz.service.thrift.intelligent.StockInfo;
@SuppressWarnings("all")
public class AmoySharePool extends Model<AmoySharePool> {
	private static final long serialVersionUID = 1L;
	public static AmoySharePool dao = new AmoySharePool();
	private java.lang.Long id;
	private java.lang.String stockToday;
	private java.lang.String stockNotice;
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
	public String getStockToday(){
		return this.get("stock_today");
	}
	public void setStockToday(String stock_today){
		this.set("stock_today",stock_today);
	}
	public String getStockNotice(){
		return this.get("stock_notice");
	}
	public void setStockNotice(String stock_notice){
		this.set("stock_notice",stock_notice);
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
	public List<AmoySharePoolInfo> findByPager(int offset, int pageSize){
		List<AmoySharePool> info_list = AmoySharePool.dao.find("select * from amoy_share_pool order by create_time desc ");
		if(pageSize != 0){
			info_list = AmoySharePool.dao.find("select * from amoy_share_pool order by create_time desc limit ?,?", new Object[]{offset, pageSize});
		}
		List<AmoySharePoolInfo> ret = new ArrayList<AmoySharePoolInfo>();
		AmoySharePoolInfo info = null;
		for(AmoySharePool item:info_list){
			info = new AmoySharePoolInfo();
			info.setId(Integer.parseInt(item.getId()+""));
			if(StringUtils.isNotBlank(item.getStockToday())){
				info.setStock_today(JSON.parseArray(item.getStockToday(), StockInfo.class));
			}else{
				info.setStock_today(new ArrayList<StockInfo>());
			}
			if(StringUtils.isNotBlank(item.getStockNotice())){
				info.setStock_notice(JSON.parseArray(item.getStockNotice(), StockInfo.class));
			}else{
				info.setStock_notice(new ArrayList<StockInfo>());
			}
			info.setModifier(Integer.parseInt(item.getModifier()+""));
			info.setCreator(Integer.parseInt(item.getCreator()+""));
			info.setCreate_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(item.getCreateTime()));
			info.setModify_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(item.getCreateTime()));
			ret.add(info);
		}
		return ret;
	}
	/**
	 * 
	 * @return
	 */
	public AmoySharePoolInfo findCurrentRecord(){
		AmoySharePool asp = AmoySharePool.dao.findFirst("select * from amoy_share_pool order by create_time desc ");
		AmoySharePoolInfo apsi = null;
		if(asp != null){
			apsi = new AmoySharePoolInfo();
			apsi.setCreate_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(asp.getCreateTime()));
			apsi.setId(Integer.parseInt(asp.getId()+""));
			apsi.setModifier(Integer.parseInt(asp.getModifier()+""));
			apsi.setCreator(Integer.parseInt(asp.getCreator()+""));
			apsi.setModify_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(asp.getModifyTime()));
			if(StringUtils.isNotBlank(asp.getStockToday())){
				apsi.setStock_today(JSON.parseArray(asp.getStockToday(), StockInfo.class));
			}else{
				apsi.setStock_today(new ArrayList<StockInfo>());
			}
			if(StringUtils.isNotBlank(asp.getStockNotice())){
				apsi.setStock_notice(JSON.parseArray(asp.getStockNotice(), StockInfo.class));
			}else{
				apsi.setStock_notice(new ArrayList<StockInfo>());
			}
		}
		return apsi;
	}
	/**
	 * 数据拷贝
	 * @return
	 */
	public AmoySharePoolInfo copy(){
		AmoySharePoolInfo info = new AmoySharePoolInfo();
		info.setCreate_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(this.getCreateTime()));
		info.setId(Integer.parseInt(this.getId()+""));
		info.setModifier(Integer.parseInt(this.getModifier()+""));
		info.setCreator(Integer.parseInt(this.getCreator()+""));
		info.setModify_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(this.getModifyTime()));
		if(StringUtils.isNotBlank(this.getStockToday())){
			info.setStock_today(JSON.parseArray(this.getStockToday(), StockInfo.class));
		}else{
			info.setStock_today(new ArrayList<StockInfo>());
		}
		if(StringUtils.isNotBlank(this.getStockNotice())){
			info.setStock_notice(JSON.parseArray(this.getStockNotice(), StockInfo.class));
		}else{
			info.setStock_notice(new ArrayList<StockInfo>());
		}
		return info;
	}
}