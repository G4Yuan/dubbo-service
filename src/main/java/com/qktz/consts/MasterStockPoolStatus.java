package com.qktz.consts;
/**
 * 高手股票池状态
 * @author zhangxf
 * @since 二〇一七年七月三日 14:43:09
 * @version 1.0
 */
public enum MasterStockPoolStatus {
	PENDING_REVIEW(1, "编辑待审核"),
	REVIEW_REFUSED(2, "编辑审核拒绝"),
	RISK_REVIEW(3, "待风控审核"),
	RISK_REFUSED(4, "风控审核拒绝"),
	PENDING_RELEASE(5, "待发布"),
	OK(6, "正常"),
	BAN(7, "禁用");
	
	private int code;
	private String msg;
	
	private MasterStockPoolStatus(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public static  String getMsg(int code){
		MasterStockPoolStatus[] pools = MasterStockPoolStatus.values();
		for(MasterStockPoolStatus s:pools){
			if(s.getCode() == code) return s.getMsg();
		}
		return "";
	}
}
