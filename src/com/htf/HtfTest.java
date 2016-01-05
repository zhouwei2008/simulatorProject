package com.htf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HtfTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap= new HashMap<String, Object>();
		//快捷通道开户接口
		//paramMap=createQuickChannelSignMap();
		
		//删卡接口测试
		//paramMap=createDelCardMap();
		
		//快捷短信验证码获取接口测试组装
		//paramMap=createVerifyCodeMap();
		
		//申购接口测试组装
		paramMap=createChargeMap();
		
		//普通赎回接口(取现)测试组装
		//paramMap=createRedeemMap();
		
		//快捷赎回接口(取现)测试组装
		//paramMap=createImRedeemMap();
		
		//单笔交易查询接口测试
		//paramMap=createSingleTradeQryMap();
		// 调用汇添富接口
		Map<String, Object> responseMap = null;
		try{
			HttpClientService httpClientService = new HttpClientService();
			responseMap = httpClientService.httpClientRequest("http://127.0.0.1:8080/simulatorProject/getHtf.do", paramMap);
			System.out.println("responseMap is :"+responseMap.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Map<String,Object> getMap(){
		 Map<String,Object> paramMap=new HashMap<String,Object>();
		 paramMap.put("sign_type", "RSA");
		 paramMap.put("service_version", "1.0");
		 paramMap.put("input_charset", "UTF-8");
		 paramMap.put("sign", "");
		 return paramMap;
	}
	/**
	 * Title: createQuickChannelSign<br/> 
	 * Description: 快捷通道开户接口测试组装<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午10:10:45
	 *@parameter:
		 **/
	public static Map<String,Object> createQuickChannelSignMap(){
		Map<String, Object> paramMap = getMap();
		paramMap.put("sp_partner", "1000000040");
		paramMap.put("transaction_id","1234567890123");
		paramMap.put("spbill_create_ip","192.168.68.19");
		paramMap.put("buyer_name","ZHOUWEI");
		paramMap.put("buyer_cert_type","1");
		paramMap.put("buyer_cert_no","132336198101162018");
		paramMap.put("bank_code","103");
		paramMap.put("bank_acco","6524000000231567986");
		paramMap.put("partner_acco_no","U151111161537111");
		paramMap.put("acc_time",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMap.put("authCode","568439");
		paramMap.put("mobile_number","13611236742");
		
		paramMap.put("jrnType", "01"); // 01:开户 02:删卡 03:获取验证码 04:申购 05:普通赎回 06:快速赎回 07:单笔查询 08:通用额度查询 09:对账文件下载 10:支付 11:退款
		return paramMap;
	}
	
	/**
	 * Title: createDelCardMap<br/> 
	 * Description: 删卡接口测试组装<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午10:16:03
	 *@parameter:
		 **/
	public static Map<String,Object> createDelCardMap(){
		Map<String, Object> paramMap = getMap();
		
		paramMap.put("trans_type", "delCard");
		paramMap.put("sp_partner", "1000000040");
		paramMap.put("transaction_id","1234567890123");
		paramMap.put("spbill_create_ip","192.168.68.19");
		paramMap.put("buyer_name","ZHOUWEI");
		paramMap.put("buyer_cert_type","1");
		paramMap.put("buyer_cert_no","132336198101162018");
		paramMap.put("bank_code","103");
		paramMap.put("bank_acco","6524000000231567986");
		paramMap.put("partner_acco_no","U151111161537111");
		paramMap.put("fund_acco_no","10000000");
		paramMap.put("acc_time",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		paramMap.put("jrnType", "02"); // 01:开户 02:删卡 03:获取验证码 04:申购 05:普通赎回 06:快速赎回 07:单笔查询 08:通用额度查询 09:对账文件下载 10:支付 11:退款
		return paramMap;
	}
	
	/**
	 * Title:createVerifyCodeMap<br/> 
	 * Description: 快捷短信验证码获取接口测试组装<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午10:15:18
	 *@parameter:
		 **/
	public static Map<String,Object> createVerifyCodeMap(){
		Map<String, Object> paramMap = getMap();
		paramMap.put("sp_partner", "1000000040");
		paramMap.put("transaction_id","1234567890123");
		paramMap.put("spbill_create_ip","192.168.68.19");
		paramMap.put("buyer_name","ZHOUWEI");
		paramMap.put("buyer_cert_type","1");
		paramMap.put("buyer_cert_no","132336198101162018");
		paramMap.put("bank_code","103");
		paramMap.put("bank_acco","6524000000231567986");
		paramMap.put("partner_acco_no","U151111161537111");
		paramMap.put("mobile_number","13611236742");
		
		paramMap.put("jrnType", "03"); // 01:开户 02:删卡 03:获取验证码 04:申购 05:普通赎回 06:快速赎回 07:单笔查询 08:通用额度查询 09:对账文件下载 10:支付 11:退款
		return paramMap;
	}
	
	/**
	 * Title: createChargeMap<br/> 
	 * Description:充值接口 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午10:17:59
	 *@parameter:
	**/
	public static Map<String,Object> createChargeMap(){
		Map<String, Object> paramMap = getMap();
		paramMap.put("sp_partner", "1000000040");
		paramMap.put("transaction_id","1234567890123");
		paramMap.put("spbill_create_ip","192.168.68.19");
		paramMap.put("fund_acco_no","10000000");
		paramMap.put("bank_code","103");
		paramMap.put("bank_acco","6524000000231567986");
		paramMap.put("partner_acco_no","U151111161537111");
		paramMap.put("fund_no","20000000");
		paramMap.put("total_fee","1000.02");
		paramMap.put("order_type","1");//申购

		paramMap.put("time_start",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		paramMap.put("jrnType", "04"); // 01:开户 02:删卡 03:获取验证码 04:申购 05:普通赎回 06:快速赎回 07:单笔查询 08:通用额度查询 09:对账文件下载 10:支付 11:退款
		return paramMap;
	}
	
	/**
	 * Title: createRedeemMap<br/> 
	 * Description:普通赎回接口(取现)测试组装 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午10:17:59
	 *@parameter:
	**/
	public static Map<String,Object> createRedeemMap(){
		Map<String, Object> paramMap = getMap();
		paramMap.put("sp_partner", "1000000040");
		paramMap.put("transaction_id","1234567890123");
		paramMap.put("spbill_create_ip","192.168.68.19");
		paramMap.put("fund_acco_no","10000000");
		paramMap.put("bank_code","103");
		paramMap.put("bank_acco","6524000000231567986");
		paramMap.put("partner_acco_no","U151111161537111");
		paramMap.put("fund_no","20000000");
		paramMap.put("fund_units","1000");
		paramMap.put("order_type","2");//取现

		paramMap.put("time_start",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		paramMap.put("jrnType", "05"); // 01:开户 02:删卡 03:获取验证码 04:申购 05:普通赎回 06:快速赎回 07:单笔查询 08:通用额度查询 09:对账文件下载 10:支付 11:退款
		return paramMap;
	}
	
	/**
	 * Title: createImRedeemMap<br/> 
	 * Description:快速赎回接口(取现)测试组装 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午10:17:59
	 *@parameter:
	**/
	public static Map<String,Object> createImRedeemMap(){
		Map<String, Object> paramMap = getMap();
		paramMap.put("sp_partner", "1000000040");
		paramMap.put("transaction_id","1234567890123");
		paramMap.put("spbill_create_ip","192.168.68.19");
		paramMap.put("fund_acco_no","10000000");
		paramMap.put("bank_code","103");
		paramMap.put("bank_acco","6524000000231567986");
		paramMap.put("partner_acco_no","U151111161537111");
		paramMap.put("fund_no","20000000");
		paramMap.put("fund_units","1000");
		paramMap.put("order_type","3");//快速取现

		paramMap.put("time_start",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		paramMap.put("jrnType", "06"); // 01:开户 02:删卡 03:获取验证码 04:申购 05:普通赎回 06:快速赎回 07:单笔查询 08:通用额度查询 09:对账文件下载 10:支付 11:退款
		return paramMap;
	}
	
	/**
	 * Title: createSingleTradeQryMap<br/> 
	 * Description:单笔交易查询接口测试组装 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午10:25:11
	 *@parameter:
		 **/
	public static Map<String,Object> createSingleTradeQryMap(){
		Map<String, Object> paramMap = getMap();
		paramMap.put("sp_partner", "1000000040");
		paramMap.put("transaction_id","1234567890124");
		paramMap.put("spbill_create_ip","192.168.68.19");
		paramMap.put("ref_transaction_id","1234567890123");
		paramMap.put("partner_acco_no","U151111161537111");
		paramMap.put("order_type","1");//1：申购, 2：赎回，3：快速赎回，4支付，5退款

		paramMap.put("time_start",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		paramMap.put("jrnType", "07"); // 01:开户 02:删卡 03:获取验证码 04:申购 05:普通赎回 06:快速赎回 07:单笔查询 08:通用额度查询 09:对账文件下载 10:支付 11:退款
		return paramMap;
	}

}
