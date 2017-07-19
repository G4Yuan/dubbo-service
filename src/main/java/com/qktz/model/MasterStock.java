package com.qktz.model;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.flume.source.SyslogUDPSource.syslogHandler;

import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Model;
import com.qktz.consts.Constants;
import com.qktz.mgr.DbManager;
import com.qktz.service.thrift.intelligent.MasterStockInfo;
@SuppressWarnings("all")
public class MasterStock extends Model<MasterStock> {
	private static final long serialVersionUID = 1L;
	public static MasterStock dao = new MasterStock();
	private java.lang.Long id;
	private java.lang.Double buyPrice;
	private java.lang.String stockCode;
	private java.lang.String stockProperty;
	private java.lang.String stockName;
	private java.lang.String recommendReason;
	private java.lang.String subject;
	private java.lang.Integer level;
	private java.lang.Double adviceBuyMin;
	private java.lang.Double adviceBuyMax;
	private java.lang.Double stopProfitMin;
	private java.lang.Double stopProfixMax;
	private java.lang.Double stopLossMin;
	private java.lang.Double stopLossMax;
	private java.lang.Double currentYield;
	private java.lang.Double currentPrice;
	private java.lang.Double currentZdf;
	private java.lang.Integer status;
	private java.lang.Long masterId;
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
	public java.lang.Double getBuyPrice(){
		return ((BigDecimal)this.get("buy_price")).doubleValue();
	}
	public void setBuyPrice(java.lang.Double buy_price){
		this.set("buy_price",buy_price);
	}
	public java.lang.String getStockCode(){
		return this.get("stock_code");
	}
	public void setStockCode(java.lang.String stock_code){
		this.set("stock_code",stock_code);
	}
	public java.lang.String getStockProperty(){
		return this.get("stock_property");
	}
	public void setStockProperty(java.lang.String stock_property){
		this.set("stock_property",stock_property);
	}
	public java.lang.String getStockName(){
		return this.get("stock_name");
	}
	public void setStockName(java.lang.String stock_name){
		this.set("stock_name",stock_name);
	}
	public java.lang.String getRecommendReason(){
		return this.get("recommend_reason");
	}
	public void setRecommendReason(java.lang.String recommend_reason){
		this.set("recommend_reason",recommend_reason);
	}
	public java.lang.String getSubject(){
		return this.get("subject");
	}
	public void setSubject(java.lang.String subject){
		this.set("subject",subject);
	}
	public java.lang.Integer getLevel(){
		return this.get("level");
	}
	public void setLevel(java.lang.Integer level){
		this.set("level",level);
	}
	public java.lang.Double getAdviceBuyMin(){
		return ((BigDecimal)this.get("advice_buy_min")).doubleValue();
	}
	public void setAdviceBuyMin(java.lang.Double advice_buy_min){
		this.set("advice_buy_min",advice_buy_min);
	}
	public java.lang.Double getAdviceBuyMax(){
		return ((BigDecimal)this.get("advice_buy_max")).doubleValue();
	}
	public void setAdviceBuyMax(java.lang.Double advice_buy_max){
		this.set("advice_buy_max",advice_buy_max);
	}
	public java.lang.Double getStopProfitMin(){
		return ((BigDecimal)this.get("stop_profit_min")).doubleValue();
	}
	public void setStopProfitMin(java.lang.Double stop_profit_min){
		this.set("stop_profit_min",stop_profit_min);
	}
	public java.lang.Double getStopProfixMax(){
		return ((BigDecimal)this.get("stop_profix_max")).doubleValue();
	}
	public void setStopProfixMax(java.lang.Double stop_profix_max){
		this.set("stop_profix_max",stop_profix_max);
	}
	public java.lang.Double getStopLossMin(){
		return ((BigDecimal)this.get("stop_loss_min")).doubleValue();
	}
	public void setStopLossMin(java.lang.Double stop_loss_min){
		this.set("stop_loss_min",stop_loss_min);
	}
	public java.lang.Double getStopLossMax(){
		return ((BigDecimal)this.get("stop_loss_max")).doubleValue();
	}
	public void setStopLossMax(java.lang.Double stop_loss_max){
		this.set("stop_loss_max",stop_loss_max);
	}
	
	public java.lang.Double getCurrentYield() {
		return ((BigDecimal)this.get("current_yield")).doubleValue();
	}
	public void setCurrentYield(java.lang.Double currentYield) {
		this.set("current_yield",currentYield);
	}
	public java.lang.Double getCurrentPrice() {
		return ((BigDecimal)this.get("current_price")).doubleValue();
	}
	public void setCurrentPrice(java.lang.Double currentPrice) {
		this.set("current_price",currentPrice);
	}
	public java.lang.Double getCurrentZdf() {
		return ((BigDecimal)this.get("current_zdf")).doubleValue();
	}
	public void setCurrentZdf(java.lang.Double currentZdf) {
		this.set("current_zdf",currentZdf);
	}
	public java.lang.Integer getStatus(){
		return this.get("status");
	}
	public void setStatus(java.lang.Integer status){
		this.set("status",status);
	}
	public java.lang.Long getMasterId(){
		return this.get("master_id");
	}
	public void setMasterId(java.lang.Long master_id){
		this.set("master_id",master_id);
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
	 * 数据拷贝
	 * @param info
	 */
	public void copy(MasterStockInfo info){
		if(info == null) return ;
		this.setStockCode(info.getStock_code());
		this.setStockName(info.getStock_name());
		this.setStockProperty(info.getStock_property());
		this.setLevel(info.getLevel());
		this.setSubject(info.getStrategy());
		this.setRecommendReason(info.getRecommend_reason());
		this.setBuyPrice(info.getBuy_price());
		this.setAdviceBuyMin(info.getAdvice_buy_min());
		this.setAdviceBuyMax(info.getAdvice_buy_max());
		this.setStopProfitMin(info.getStop_profit_min());
		this.setStopProfixMax(info.getStop_profit_max());
		this.setStopLossMin(info.getStop_loss_min());
		this.setStopLossMax(info.getStop_loss_max());
		this.setCurrentPrice(info.getCurrent_price());
		this.setCurrentYield(info.getCurrent_yield());
		this.setCurrentZdf(info.getCurrent_zdf());
	}
	
	/**
	 * 数据拷贝
	 * @param info
	 */
	public MasterStockInfo copyTo(){
		MasterStockInfo info = new MasterStockInfo();
		info.setStock_code(this.getStockCode());
		info.setStock_name(this.getStockName());
		info.setStock_property(this.getStockProperty());
		info.setLevel(this.getLevel());
		info.setStrategy(this.getSubject());
		info.setRecommend_reason(this.getRecommendReason());
		info.setBuy_price(this.getBuyPrice()==null?0.00D:this.getBuyPrice());
		info.setAdvice_buy_min(this.getAdviceBuyMin()==null?0.00D:this.getAdviceBuyMin());
		info.setAdvice_buy_max(this.getAdviceBuyMax()==null?0.00D:this.getAdviceBuyMax());
		info.setStop_profit_min(this.getStopProfitMin()==null?0.00D:this.getStopProfitMin());
		info.setStop_profit_max(this.getStopProfixMax()==null?0.00D:this.getStopProfixMax());
		info.setStop_loss_min(this.getStopLossMin()==null?0.00D:this.getStopLossMin());
		info.setStop_loss_max(this.getStopLossMax()==null?0.00D:this.getStopLossMax());
		info.setCurrent_price(this.getCurrentPrice()==null?0.00D:this.getCurrentPrice());
		info.setCurrent_yield(this.getCurrentYield()==null?0.00D:this.getCurrentYield());
		info.setCurrent_zdf(this.getCurrentZdf()==null?0.00D:this.getCurrentZdf());
		if(this.getModifyTime() != null){
			info.setOper_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(this.getModifyTime()));
			info.setOper_timestamp(this.getModifyTime().getTime());
		}
			
		return info;
	}
	/**
	 * 获取有效股票列表
	 * @param masterPoolId
	 * @return
	 */
	public List<MasterStockInfo> findMasterStockInfoList(long masterPoolId, int status){
		List<MasterStock> stocks = MasterStock.dao.find(
				Constants.MASTER_STOCK, new Object[]{masterPoolId, status});
		List<MasterStockInfo> ret = new ArrayList<MasterStockInfo>();
		for(MasterStock stock:stocks){
			ret.add(stock.copyTo());
		}
		return ret;
	}
	/**
	 * 获取股票池指定股票代码的详情
	 * @param poolId	股票池id
	 * @param stockCode	股票池指定股票代码
	 * 
	 * @return
	 */
	public MasterStock findByPoolCode(long poolId, String stockCode){
		return this.dao.findFirst("select * from master_stock where master_id=? and stock_code=?", new Object[]{poolId, stockCode});
	}
	
	/**
	 * 获取股票池指定股票代码的详情
	 * @param poolId	股票池id
	 * @param stockCode	股票池指定股票代码
	 * @param status 状态  1 正常 2编辑  3 调出
	 * 
	 * @return
	 */
	public MasterStock findByPoolCodeAndStatus(long poolId, String stockCode, int status){
		return this.dao.findFirst("select * from master_stock where master_id=? and stock_code=? and status=?", new Object[]{poolId, stockCode, status});
	}
	
	/**
	 * 获取调出股票
	 * @param poolId	股票池id
	 * @param stockCode	股票池指定股票代码
	 * 
	 * @return
	 */
	public List<MasterStock> findByOut(long poolId, String stockCodes){
		return this.dao.find("select * from master_stock where master_id=? and stock_code not in ("+stockCodes+") and status != 3", new Object[]{poolId});
	}
}