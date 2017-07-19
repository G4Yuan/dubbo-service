package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: Account 
* @Description: 资金信息 
* @author yuanwei  
* @date 2017年6月28日 上午11:25:59 
* @version V1.0 
*/
@SuppressWarnings("all")
public class Account extends Model<Account>{

	private static final long serialVersionUID = 1L;
	public static Account dao = new Account();
	private java.lang.Integer id;
	private java.lang.String groupBmId;
	private java.lang.String isUsed;
	private java.lang.String accountId;
	public java.lang.Integer getId() {
		return this.get("id");
	}
	public void setId(java.lang.Integer id) {
		this.set("id", id);
	}
	public java.lang.String getGroupBmId() {
		return this.get("groupBmId");
	}
	public void setGroupBmId(java.lang.String groupBmId) {
		this.set("groupBmId", groupBmId);
	}
	public java.lang.String getIsUsed() {
		return this.get("isUsed");
	}
	public void setIsUsed(java.lang.String isUsed) {
		this.set("isUsed", isUsed);
	}
	public java.lang.String getAccountId() {
		return this.get("accountId");
	}
	public void setAccountId(java.lang.String accountId) {
		this.set("accountId", accountId);
	}
	
}
 