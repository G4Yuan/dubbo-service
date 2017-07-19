package com.qktz.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Model;
import com.qktz.service.thrift.intelligent.CSVanePager;
import com.qktz.service.thrift.intelligent.ChinaStocksVane;

@SuppressWarnings("all")
public class ChinaStockVaneModel extends Model<ChinaStockVaneModel>{

	private static final long serialVersionUID = 1L;
	public static ChinaStockVaneModel dao = new ChinaStockVaneModel();
	private java.lang.Integer id;
	private java.util.Date trading_day;
	private java.lang.Integer qk_index_level;
	private java.lang.String qk_index_level_text;
	private java.lang.String qk_index_text;
	private java.lang.String qk50_text;
	private java.lang.String qkAD_text;
	private java.lang.String emotion_index_text;
	private java.lang.String main_capital_unscramble;
	private java.lang.String creator;
	private java.lang.Integer buy_status;
	private java.lang.String emotion_data;
	private java.util.Date create_time;
	private java.util.Date modify_time;
	private java.lang.String fields_con;
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.util.Date getTrading_day() {
		return this.get("trading_day");
	}
	public void setTrading_day(java.util.Date trading_day) {
		this.set("trading_day", trading_day);
	}
	public java.lang.Integer getQk_index_level() {
		return this.get("qk_index_level");
	}
	public void setQk_index_level(java.lang.Integer qk_index_level) {
		this.set("qk_index_level", qk_index_level);
	}
	public java.lang.String getQk_index_text() {
		return this.get("qk_index_text");
	}
	public void setQk_index_text(java.lang.String qk_index_text) {
		this.set("qk_index_text", qk_index_text);
	}
	public java.lang.String getQk_index_level_text() {
		return this.get("qk_index_level_text");
	}
	public void setQk_index_level_text(java.lang.String qk_index_level_text) {
		this.set("qk_index_level_text", qk_index_level_text);
	}
	public java.lang.String getQk50_text() {
		return this.get("qk50_text");
	}
	public void setQk50_text(java.lang.String qk50_text) {
		this.set("qk50_text", qk50_text);
	}
	public java.lang.String getQkAD_text() {
		return this.get("qkAD_text");
	}
	public void setQkAD_text(java.lang.String qkAD_text) {
		this.set("qkAD_text", qkAD_text);
	}
	public java.lang.String getEmotion_index_text() {
		return this.get("emotion_index_text");
	}
	public void setEmotion_index_text(java.lang.String emotion_index_text) {
		this.set("emotion_index_text", emotion_index_text);
	}
	public java.lang.String getMain_capital_unscramble() {
		return this.get("main_capital_unscramble");
	}
	public void setMain_capital_unscramble(java.lang.String main_capital_unscramble) {
		this.set("main_capital_unscramble", main_capital_unscramble);
	}
	public java.lang.String getCreator() {
		return this.get("creator");
	}
	public void setCreator(java.lang.String creator) {
		this.set("creator", creator);
	}
	public java.lang.Integer getBuy_status() {
		return this.get("buy_status");
	}
	public void setBuy_status(java.lang.Integer buy_status) {
		this.set("buy_status", buy_status);
	}
	public java.lang.String getEmotion_data() {
		return this.get("emotion_data");
	}
	public void setEmotion_data(java.lang.String emotion_data) {
		this.set("emotion_data", emotion_data);
	}
	public java.util.Date getCreate_time() {
		return this.get("create_time");
	}
	public void setCreate_time(java.util.Date create_time) {
		this.set("create_time", create_time);
	}
	public java.util.Date getModify_time() {
		return this.get("modify_time");
	}
	public void setModify_time(java.util.Date modify_time) {
		this.set("modify_time", modify_time);
	}
	public java.lang.String getFields_con() {
		return this.get("fields_con");
	}
	public void setFields_con(java.lang.String fields_con) {
		this.set("fields_con", fields_con);
	}
	
	public List<ChinaStocksVane> findCSVane(int offset, int pageSize){
		List<ChinaStocksVane> csVanes = new ArrayList<ChinaStocksVane>();
		ChinaStocksVane csVane = null;
		StringBuilder sql = new StringBuilder("select * from china_stocks_vane where 1= 1");
		List<ChinaStockVaneModel> obj = new ArrayList<ChinaStockVaneModel>();
		if(pageSize != 0){
			sql.append(" order by create_time desc limit ?,?");
			obj = ChinaStockVaneModel.dao.find(sql.toString(), new Object[]{offset, pageSize});
		}else{
			sql.append(" order by create_time desc");
			obj = ChinaStockVaneModel.dao.find(sql.toString());
		}
		for(ChinaStockVaneModel item:obj){
			csVane = new ChinaStocksVane();
			csVane.setCsvid(item.getId());
			csVane.setQk_index_level(item.getQk_index_level());
			csVane.setQk_index_text(item.getQk_index_text());
			csVane.setQk_index_level_text(item.getQk_index_level_text());
			csVane.setQk50_text(item.getQk50_text());
			csVane.setQkAD_text(item.getQkAD_text());
			csVane.setMain_capital_unscramble(item.getMain_capital_unscramble());
			csVane.setCreator(item.getCreator());
            csVane.setBuy_status(item.getBuy_status());
			csVane.setEmotion_index_text(item.getEmotion_index_text());
			csVane.setTrading_day(new SimpleDateFormat("yyyy-MM-dd").format(item.getTrading_day()));
			csVane.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getCreate_time()));
			csVane.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getModify_time()));
			csVanes.add(csVane);
		}
		return csVanes;
	}
}