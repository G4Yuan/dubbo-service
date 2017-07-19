package com.qktz.model; 

import com.jfinal.plugin.activerecord.Model;

/** 
* @ClassName: Creator 
* @Description: 创建者
* @author yuanwei
* @date 2017年7月6日 下午6:09:27 
* @version V1.0 
*/
@SuppressWarnings("all")
public class PortfolioCreator extends Model<PortfolioCreator>{

	private static final long serialVersionUID = 1L;
	public static PortfolioCreator dao = new PortfolioCreator();
	private java.lang.Integer id;
	private java.lang.String user_object_id;
	private java.lang.String user_name;                           //姓名
	private java.lang.String user_logo;                           //头像
	private java.lang.String user_summary;                        //简介
	private java.lang.String roleType;                            //角色类型
	private java.lang.String qualificationName;                   //资格证书名称
	private java.lang.String qualificationNum;                    //资格证书
	private java.util.Date createdAt;
	private java.util.Date updatedAt;
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
	public java.lang.String getUser_name() {
		return this.get("user_name");
	}
	public void setUser_name(java.lang.String user_name) {
		this.set("user_name", user_name);
	}
	public java.lang.String getUser_logo() {
		return this.get("user_logo");
	}
	public void setUser_logo(java.lang.String user_logo) {
		this.set("user_logo", user_logo);
	}
	public java.lang.String getUser_summary() {
		return this.get("user_summary");
	}
	public void setUser_summary(java.lang.String user_summary) {
		this.set("user_summary", user_summary);
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
	public java.lang.String getRoleType() {
		return this.get("roleType");
	}
	public void setRoleType(java.lang.String roleType) {
		this.set("roleType", roleType);
	}
	
}
 