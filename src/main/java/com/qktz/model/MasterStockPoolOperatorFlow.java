package com.qktz.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.qktz.consts.Constants;
import com.qktz.service.thrift.intelligent.OperationInfo;

@SuppressWarnings("all")
public class MasterStockPoolOperatorFlow extends Model<MasterStockPoolOperatorFlow> {
	private static final long serialVersionUID = 1L;
	public static MasterStockPoolOperatorFlow dao = new MasterStockPoolOperatorFlow();
	private java.lang.Long id;
	private java.lang.Long masterId;
	private java.lang.Long operator;
	private java.lang.String operatorName;
	private java.lang.String remark;
	private java.lang.String operation;
	private java.util.Date operatorTime;

	public java.lang.Long getId() {
		return this.get("id");
	}

	public void setId(java.lang.Long id) {
		this.set("id", id);
	}

	public java.lang.Long getMasterId() {
		return this.get("master_id");
	}

	public void setMasterId(java.lang.Long master_id) {
		this.set("master_id", master_id);
	}

	public java.lang.Long getOperator() {
		return this.get("operator");
	}

	public void setOperator(java.lang.Long operator) {
		this.set("operator", operator);
	}

	public java.lang.String getOperatorName() {
		return this.get("operator_name");
	}

	public void setOperatorName(java.lang.String operator_name) {
		this.set("operator_name", operator_name);
	}
	
	public java.lang.String getRemark() {
		return this.get("remark");
	}

	public void setRemark(java.lang.String remark) {
		this.set("remark", remark);
	}

	public java.lang.String getOperation() {
		return this.get("operation");
	}

	public void setOperation(java.lang.String operation) {
		this.set("operation", operation);
	}

	public java.util.Date getOperatorTime() {
		return this.get("operator_time");
	}

	public void setOperatorTime(java.util.Date operator_time) {
		this.set("operator_time", operator_time);
	}

	public OperationInfo copyTo() {
		OperationInfo oper = new OperationInfo();
		oper.setOperator(this.getOperator());
		oper.setOperator_name(this.getOperatorName());
		oper.setOperation(this.getOperation());
		oper.setOperator_time(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).format(this.getOperatorTime()));
		return oper;
	}
	public void copy(OperationInfo info) {
		if(info == null) return ;
		this.setOperator(info.getOperator());
		this.setOperatorName(info.getOperator_name());
		this.setOperation(info.getOperation());
		try {
			this.setOperatorTime(new SimpleDateFormat(Constants.DETAUL_DATE_FORMAT).parse(info.getOperator_time()));
		} catch (ParseException e) {
			System.out.println(e);
		}
	}
	public List<OperationInfo> findListByPoolId(long poolId) {
		List<OperationInfo> ret = new ArrayList<OperationInfo>();
		List<MasterStockPoolOperatorFlow> flows = MasterStockPoolOperatorFlow.dao.find(Constants.OPERATOR_FLOW,
				new Object[] { poolId });
		for (MasterStockPoolOperatorFlow item : flows) {
			ret.add(item.copyTo());
		}
		return ret;
	}
}