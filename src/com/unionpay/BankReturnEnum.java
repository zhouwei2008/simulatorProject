package com.unionpay;

public enum BankReturnEnum {
	
	 SUCCESS("00","成功"),
	 
	 FAILTRX("01"," 交易失败"),
	 
	 NOTOPEN("02","系统未开放或暂时关闭"),
	 
	 OUTTIME("03","交易通讯超时，请发起查询"),
	 
	 FAILSTATUS("04","交易状态未明，请查询对账结果"),
	 
	 TRXHAND("05","交易已受理，请稍后查询交易结果"),
	 
	 ERRFORMAT("10","报文格式错误"),
	 
	 FAILSIGN("11","验证签名失败"),
	 
	 REPEATTRX("12","重复交易"),
	 
	 LOSTPARAM("13","报文交易要素缺失"),
	 
	 FAILBATCHFORMAT("14","批量文件格式错误"),
	 
	 TRXNOTPASS("30","交易未通过"),
	 
	 FAILMERSTATUS("31","商户状态不正确"),
	 
	 NOTTRXPERMISSION("32","无此交易权限"),
	 
	 MAXTRXMONEY("33","交易金额超限"),
	 
	 NOTHAVETRX("34","查无此交易"),
	 
	 FAILOLDTRX("35","原交易不存在或状态不正确 "),
	 
	 NOTMATCHOLDTRX("36","与原交易信息不符 "),
	 
	 OUTMAXCOUNT("37","已超过最大查询次数或操作过于频繁"),
	 
	 RISKCONTROL("38","银联风险受限"),
	 
	 OUTDATETIME("39","交易不在受理时间范围内"),
	 
	 FAILBANDING("40","绑定关系检查失败"),
	 
	 FAILBATCHSTATUS("41","批量状态不正确，无法下载"),
	 
	 OUTCHARGETIME("42","扣款成功但交易超过规定支付时间"),
	 
	 ERRCARD("60","交易失败，详情请咨询您的发卡行 "),
	 
	 FAILCARDNUM("61","输入的卡号无效，请确认后输入"),
	 
	 ERRCARDMER("62","交易失败，发卡银行不支持该商户，请更换其他银行卡"),
	 
	 ERRCARDSTATUS("63","卡状态不正确"),
	 
	 NOTMUCHMONEY("64","卡上的余额不足 "),
	 
	 FAILPASSWORD("65","输入的密码、有效期或 CVN2 有误，交易失败"),
	 
	 FAILCARDSIGN("66","持卡人身份信息或手机号输入不正确，验证失败"),
	 
	 PASSWORDTIMEMUCH("67","密码输入次数超限"),
	 
	 ERRSYS("99","通用错误");
	 
	 private String resCode;
	 private String resDesc;
	 
	 private BankReturnEnum(String resCode,String resDesc) {
		this.resCode=resCode;
		this.resDesc=resDesc;
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
	    
}
