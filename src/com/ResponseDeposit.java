package com;

import java.io.Serializable;

public class ResponseDeposit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//资金渠道流水号
	private String fdcJrnNo;
	//银联受理订单号
	private String bankTn;
	//支付订单状态
	private String orderStatus;
	//外部订单提交时间
	private String outOrderTime;
	//订单金额
	private int amt;
	//查询结果码
	private String respCode;
	//结果码描述
	private String respDesc;
	//外部订单号
	private String outOrderNo;
	//退款金额
	private int refundAmt;
	
	public String getFdcJrnNo() {
		return fdcJrnNo;
	}
	public void setFdcJrnNo(String fdcJrnNo) {
		this.fdcJrnNo = fdcJrnNo;
	}
	public String getBankTn() {
		return bankTn;
	}
	public void setBankTn(String bankTn) {
		this.bankTn = bankTn;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOutOrderTime() {
		return outOrderTime;
	}
	public void setOutOrderTime(String outOrderTime) {
		this.outOrderTime = outOrderTime;
	}
	public int getAmt() {
		return amt;
	}
	public void setAmt(int amt) {
		this.amt = amt;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	public String getOutOrderNo() {
		return outOrderNo;
	}
	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}
	public int getRefundAmt() {
		return refundAmt;
	}
	public void setRefundAmt(int refundAmt) {
		this.refundAmt = refundAmt;
	}
	
}
