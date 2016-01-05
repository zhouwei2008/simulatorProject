package com.htf;

public enum HtfReturnEnum {
	
	 SUCCESS("0","0","成功"),
	 
	 FAILTRX("2","2001"," 交易失败"),
	 
	 ERRFORMAT("2","2010","报文格式错误"),
	 
	 FAILSIGN("2","2011","验证签名失败"),
	 
	 NETERROR("2","2003","网络异常"),
	 
	 SYSERROR("2","2004","基金交易系统异常"),
	 
	 BANKAUTHERR("2","2012","银行鉴权失败"),
	 
	 LOSTPARAM("2","2013","获得客户失败，读取为空"),
	 
	 CHARGEFAIL("2","2014","充值失败"),
	 
	 TRXWRITEBACKFAIL("2","2030","回写汇添富下单流水失败"),
	 
	 FAILMEROPEN("2","2031","开户失败"),
	 
	 NOTSUPPORTCARDTYPE("9","9010","该银行类型暂不支持"),
	 
	 ERRCARDINFO("9","9011","银行卡信息错误"),
	 
	 NOTBANDING("9","9012","银行卡已解约，交易失败"),
	 
	 IPFREEZE("9","9013","非法IP地址已被冻结"),
	 
	 ACCFREEZE("9","9014","账户已冻结"),
	 
	 BANKCARDNOTHAVE("9","9015","银行卡未登记"),
	 
	 HAVEERRPARAMETERS("9","9016","请求参数异常或不全"),
	 
	 NOTOPENACC("9","9199","未开户（重复开户查询）"),
	 
	 USERINFONOTFULL("9","9102","用户信息不完整"),
	 
	 BANKCARDUSED("9","9103","银行卡被占用"),
	 
	 IDVALFAIL("9","9109","身份证验证失败"),
	 
	 REPEATSIGN("9","9131","该卡已签约，无须重复签约"),
	 
	 REPEATMOBILE("9","9135","手机号码重复"),
	 
	 TRXPASSFAIL("9","9161","交易密码验证失败"),
	 
	 SENDMESSAGEFAIL("9","9171","发送短信验证码失败"),
	 
	 PASSTIMEMESSAGE("9","9175","短信过期 您输入的短信验证码已过期，请点击重新发送按钮再试一次"),
	 
	 MINMONEYERR("9","9201","充值金额不得少于0.01元"),
	 
	 MAXMONEYERR("9","9202","超出银行限额"),
	 
	 MAXCOUNTERR("9","9203","超出最高申购份额"),
	 
	 MAXDAYCOUNTERR("9","9204","超出日最高申购份额"),
	 
	 SYSCLOSECHARGE("9","9205","系统暂停充值"),
	 
	 TRXHANDLE("9","9206","交易处理中"),
	 
	 MAXDAYMONEY("9","9210","单笔交易金额超过大额限制"),
	 
	 SUMMONEYTOOMUCH("9","9211","累交易计金额超过大额限制"),
	 
	 ERRSTATUS("9","9212","基金状态异常"),
	 
	 MINWITHDRAWMONEYERR("9","9301","取现金额不得少于0.01元"),
	 
	 MINWITHDRAWCOUNTERR("9","9302","取现低于最小持有份额"),
	 
	 MAXWITHDRAWMONEYERR("9","9310","取现金额超出取现限额"),
	 
	 MAXWITHDRAWCOUNTERR("9","9311","取现金额超出可取份额"),
	 
	 NOTENOUGHCOUNT("9","9310","支付份额不足"),
	 
	 CHARGESTOP("9","9320","取现已暂停"),
	 
	 MINCHARGEERR("9","9302","取现低于最小持有份额"),
	 
	 DELECARDFAIL("9","9401","删卡失败，该银行卡有交易正在进行中，无法删除");
	 
	 
	 
	 private String sysCode;
	 private String resCode;
	 private String resDesc;
	 
	 private HtfReturnEnum(String sysCode,String resCode,String resDesc) {
		this.resCode=resCode;
		this.resDesc=resDesc;
		this.sysCode=sysCode;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	    
}
