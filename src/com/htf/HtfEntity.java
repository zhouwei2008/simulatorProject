package com.htf;

import com.Validator;


public class HtfEntity {
	//签名方式
	@Validator(maxsize=8)
	private String sign_type;
	//接口版本
	@Validator(maxsize=8)
	private String service_version;
	//字符集
	@Validator(maxsize=8)
	private String input_charset;
	//网点号
	@Validator(maxsize=10, nullable=false,regx="^[A-Za-z0-9]+$")
	private String sp_partner;
	//请求流水号
	@Validator(maxsize=32, nullable=false,regx="^[A-Za-z0-9]+$")
	private String transaction_id;
	//用户IP
	@Validator(maxsize=15, nullable=false,regx="^(([0-2]*[0-9]+[0-9]+)\\.([0-2]*[0-9]+[0-9]+)\\.([0-2]*[0-9]+[0-9]+)\\.([0-2]*[0-9]+[0-9]+))$")
	private String spbill_create_ip;
	//用户姓名
	@Validator(maxsize=32, nullable=false)
	private String buyer_name;
	//用户证件类型   1为身份证，不支持其他证件类型
	@Validator(regx="1", nullable=false)
	private int buyer_cert_type;
	//用户证件号码
	@Validator(regx="^(\\d{18}|\\d{17}(\\d|X|x))$", nullable=false)
	private String buyer_cert_no;
	//银行卡银行编号
	@Validator(regx="\\d{3}", nullable=false)
	private String bank_code;
	//银行卡号
	@Validator(regx="^\\d{16}|\\d{19}$", nullable=false)
	private String bank_acco;
	//渠道方客户账号
	@Validator(maxsize=64, nullable=false,regx="^[A-Za-z0-9]+$")
	private String partner_acco_no;
	//附加数据
	private String attach;
	//请求时间
	@Validator(regx="\\d{14}", nullable = false)
	private String acc_time;
	//短信验证码
	@Validator(regx="\\d{6}", nullable = false)
	private String authCode;
	//手机号码
	@Validator(regx="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", nullable = false)
	private String mobile_number;
	@Validator(regx="^[A-Za-z0-9]+$", maxsize=12,nullable = false)
	private String trans_type;
	//基金公司客户号
	@Validator(regx="^[A-Za-z0-9]+$", maxsize=10,nullable = false)
	private String fund_acco_no;
	//基金代码
	@Validator(regx="^[A-Za-z0-9]+$", maxsize=8,nullable = false)
	private String fund_no;
	//金额
	@Validator(regx="^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$", maxsize=8,nullable = false)
	private String fund_units;
	//订单类型
	@Validator(regx="\\d{1}", maxsize=1,nullable = false)
	private int order_type;
	//请求生成时间
	@Validator(regx="\\d{14}", nullable = false)
	private String time_start;
	//原请求流水号
	@Validator(regx="^[A-Za-z0-9]+$", maxsize=32,nullable = false)
	private String ref_transaction_id;
	//金额
	@Validator(regx="^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$", nullable = false, maxsize=16)
	private String total_fee;
	//币种
	private int fee_type;
	
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getService_version() {
		return service_version;
	}
	public void setService_version(String service_version) {
		this.service_version = service_version;
	}
	public String getInput_charset() {
		return input_charset;
	}
	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}
	public String getSp_partner() {
		return sp_partner;
	}
	public void setSp_partner(String sp_partner) {
		this.sp_partner = sp_partner;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getBuyer_name() {
		return buyer_name;
	}
	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}
	public int getBuyer_cert_type() {
		return buyer_cert_type;
	}
	public void setBuyer_cert_type(int buyer_cert_type) {
		this.buyer_cert_type = buyer_cert_type;
	}
	public String getBuyer_cert_no() {
		return buyer_cert_no;
	}
	public void setBuyer_cert_no(String buyer_cert_no) {
		this.buyer_cert_no = buyer_cert_no;
	}
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public String getBank_acco() {
		return bank_acco;
	}
	public void setBank_acco(String bank_acco) {
		this.bank_acco = bank_acco;
	}
	public String getPartner_acco_no() {
		return partner_acco_no;
	}
	public void setPartner_acco_no(String partner_acco_no) {
		this.partner_acco_no = partner_acco_no;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getAcc_time() {
		return acc_time;
	}
	public void setAcc_time(String acc_time) {
		this.acc_time = acc_time;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	public String getTrans_type() {
		return trans_type;
	}
	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}
	public String getFund_no() {
		return fund_no;
	}
	public void setFund_no(String fund_no) {
		this.fund_no = fund_no;
	}
	public String getFund_units() {
		return fund_units;
	}
	public void setFund_units(String fund_units) {
		this.fund_units = fund_units;
	}
	public int getOrder_type() {
		return order_type;
	}
	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getRef_transaction_id() {
		return ref_transaction_id;
	}
	public void setRef_transaction_id(String ref_transaction_id) {
		this.ref_transaction_id = ref_transaction_id;
	}
	public String getFund_acco_no() {
		return fund_acco_no;
	}
	public void setFund_acco_no(String fund_acco_no) {
		this.fund_acco_no = fund_acco_no;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public int getFee_type() {
		return fee_type;
	}
	public void setFee_type(int fee_type) {
		this.fee_type = fee_type;
	}
	
}
