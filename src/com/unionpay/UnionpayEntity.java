package com.unionpay;

import com.Validator;


public class UnionpayEntity {
	
	/**
	 * Title: UnionpayEntity<br/> 
	 * Description:银联测试结果模拟器实体类 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-21 上午10:49:51
	 *@parameters:
	 *
	**/
	// 版本号
	@Validator(regx="5.0.0", nullable = false)
	private String version;
	
	// 字符集编码 默认"UTF-8"
	@Validator(regx="UTF-8", nullable = false)
	private String encoding;
	
	// 签名方法 01 RSA
	@Validator(regx="01", nullable = false)
	private String signMethod;
	
	// 交易类型 01-消费
	@Validator(regx="\\d{2}", nullable = false)
	private String txnType;
	
	// 交易子类型 01:自助消费 02:订购 03:分期付款
	@Validator(regx="0[0-3]", nullable = false)
	private String txnSubType;
	
	// 业务类型
	@Validator(regx="\\d{6}", nullable = false)
	private String bizType;
	
	// 渠道类型，07-PC，08-手机
	@Validator(regx="07|08", nullable = false)
	private String channelType;
	
	// 前台通知地址 ，控件接入方式无作用
	private String frontUrl;
	
	// 后台通知地址
	@Validator(nullable = false)
	private String backUrl;
	
	// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
	@Validator(regx="0|1|2", nullable = false)
	private String accessType;
	
	// 商户号码
	@Validator(maxsize=32,regx="^[A-Za-z0-9]+$", nullable = false)
	private String merId;
	
	// 商户订单号
	@Validator(maxsize=40,regx="^[A-Za-z0-9]+$", nullable = false)
	private String orderId;
	
	// 订单发送时间
	@Validator(regx="\\d{14}", nullable = false)
	private String txnTime;
	
	// 交易金额，单位分
	@Validator(maxsize=12,regx = "^(\\d+)?$",nullable=false)
	private String txnAmt;
	
	// 交易币种
	@Validator(regx="156", nullable = false)
	private String currencyCode;
	
	//原消费的queryId
	@Validator(maxsize=40,regx="^[A-Za-z0-9]+$", nullable = false)
	private String origQryId;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnSubType() {
		return txnSubType;
	}

	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getFrontUrl() {
		return frontUrl;
	}

	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getOrigQryId() {
		return origQryId;
	}

	public void setOrigQryId(String origQryId) {
		this.origQryId = origQryId;
	}
	
	
}
