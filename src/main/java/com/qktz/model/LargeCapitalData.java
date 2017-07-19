package com.qktz.model;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("all")
public class LargeCapitalData extends Model<LargeCapitalData>{

	private static final long serialVersionUID = 1L;
	public static LargeCapitalData dao = new LargeCapitalData();
	private java.util.Date trading_day;
	private java.lang.Double SH000002;
	private java.lang.Double SH000001;
	private java.lang.Double SZ399107;
	private java.lang.Double SZ399006;
	private java.lang.Double SZ399005;
	public java.util.Date getTrading_day() {
		return this.get("trading_day");
	}
	public void setTrading_day(java.util.Date trading_day) {
		this.set("trading_day", trading_day);
	}
	public java.lang.Double getSH000002() {
		return this.get("SH000002");
	}
	public void setSH000002(java.lang.Double sH000002) {
		this.set("SH000002", SH000002);
	}
	public java.lang.Double getSH000001() {
		return this.get("SH000001");
	}
	public void setSH000001(java.lang.Double sH000001) {
		this.set("SH000001", SH000001);
	}
	public java.lang.Double getSZ399107() {
		return this.get("SZ399107");
	}
	public void setSZ399107(java.lang.Double sZ399107) {
		this.set("SZ399107", SZ399107);
	}
	public java.lang.Double getSZ399006() {
		return this.get("SZ399006");
	}
	public void setSZ399006(java.lang.Double sZ399006) {
		this.set("SZ399006", SZ399006);
	}
	public java.lang.Double getSZ399005() {
		return this.get("SZ399005");
	}
	public void setSZ399005(java.lang.Double sZ399005) {
		this.set("SZ399005", SZ399005);
	}
	
}