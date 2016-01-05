package com.htf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.Clazz;
import com.CommonUntil;
import com.ResponseDeposit;
import com.unionpay.BankReturnEnum;

public class HtfService {
	/**
	 * Title: getHtfQuickChannelSign<br/>
	 * Description:快捷通道开户接口 <br/>
	 * Company: gigold<br/>
	 * 
	 * @author: zhouyong-pc
	 * @date : 2015-12-28 下午3:59:43
	 * @parameter:
	 **/
	public Map<String, String> getHtfQuickChannelSign(Map<String, String> submitFromData) {

		Map<String, String> respMap = new HashMap<String, String>();
		
		CommonUntil commonUntil = new CommonUntil();
		
		respMap=getMap();
		
		respMap.put("sp_partner", submitFromData.get("sp_partner"));
		
		respMap.put("transaction_id", submitFromData.get("transaction_id"));
		
		respMap.put("partner_acco_no", submitFromData.get("partner_acco_no"));
		
		respMap.put("buyer_name", submitFromData.get("buyer_name"));
		
		respMap.put("buyer_cert_type", submitFromData.get("buyer_cert_type"));
		
		respMap.put("buyer_cert_no", submitFromData.get("buyer_cert_no"));
		
		respMap.put("bank_code", submitFromData.get("bank_code"));
		
		respMap.put("bank_acco", submitFromData.get("bank_acco"));
		
		respMap.put("user_tp", "20");
		
		respMap.put("notify_type", "2");
		
		respMap.put("tran_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		String type="01";
		try {
			respMap=validationRequestDeposit(submitFromData,respMap,type);
			if(respMap.get("retCode")!=null){
				respMap.put("trade_status", "2");
				return respMap;
			}
			
			//非法IP
			String errIp = commonUntil.getProperties("ERR_IP");
			if(errIp.equals(submitFromData.get("spbill_create_ip"))){
				respMap.put("trade_status", "2");
				
				respMap.put("retCode", HtfReturnEnum.IPFREEZE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.IPFREEZE.getResDesc());
				
				return respMap;
			}
			//银行卡未登记
			String bankCard = commonUntil.getProperties("ERR_BANK_CARD");
			if(bankCard.equals(submitFromData.get("bank_acco"))){
				respMap.put("trade_status", "2");
				
				respMap.put("retCode", HtfReturnEnum.BANKCARDNOTHAVE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.BANKCARDNOTHAVE.getResDesc());
				
				return respMap;
			}
			//账户被冻结
			String freezeAcc= commonUntil.getProperties("FREEZE_ACC");
			if(freezeAcc.equals(submitFromData.get("partner_acco_no"))){
				respMap.put("trade_status", "2");
				
				respMap.put("retCode", HtfReturnEnum.ACCFREEZE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.ACCFREEZE.getResDesc());
				
				return respMap;
			}
			
			//重复手机号
			String repeatMobile= commonUntil.getProperties("REPEAT_MOBILE");
			if(repeatMobile.equals(submitFromData.get("mobile_number"))){
				respMap.put("trade_status", "2");
				
				respMap.put("retCode", HtfReturnEnum.REPEATMOBILE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.REPEATMOBILE.getResDesc());
				
				return respMap;
			}
			//短信验证码超时
			String sendTime=submitFromData.get("acc_time");
			String nowTime= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			Date one = new SimpleDateFormat("yyyyMMddHHmmss").parse(sendTime);
			Date two = new SimpleDateFormat("yyyyMMddHHmmss").parse(nowTime);
			if(commonUntil.getDiffSeconds(one, two)>60){
				respMap.put("trade_status", "2");
				
				respMap.put("retCode", HtfReturnEnum.PASSTIMEMESSAGE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.PASSTIMEMESSAGE.getResDesc());
				
				return respMap;
			}
			//基金公司客户号
			respMap.put("fund_acco_no", "10000000");
			
			respMap.put("trade_status", "0");
			
			respMap.put("retCode", HtfReturnEnum.SUCCESS.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.SUCCESS.getResDesc());
			
		} catch (Exception e) {							
			respMap.put("trade_status", "2");
			
			respMap.put("retCode", HtfReturnEnum.FAILTRX.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.FAILTRX.getResDesc());
			
			return respMap;
		}
		return respMap;
	}
	
	/**
	 * Title: getHtfDelCard<br/> 
	 * Description:删卡 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-31 上午9:56:13
	 *@parameter: Map submitFromData
		 **/
	public Map<String, String> getHtfDelCard(Map<String, String> submitFromData) {
		
		CommonUntil commonUntil = new CommonUntil();

		Map<String, String> respMap = new HashMap<String, String>();
		
		respMap = getMap();
		
		respMap.put("trans_type", submitFromData.get("trans_type"));
		
		respMap.put("sp_partner", submitFromData.get("sp_partner"));
		
		respMap.put("transaction_id", submitFromData.get("transaction_id"));
		
		respMap.put("is_redirect", "N");
		
		respMap.put("modify_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		String type ="02";
		try {
			respMap=validationRequestDeposit(submitFromData,respMap,type);
			if(respMap.get("retCode")!=null){
				respMap.put("trade_status", "2");
				return respMap;
			}
			//使用中的银行卡
			String useBankCard = commonUntil.getProperties("USE_BANK_CARD");
			if(useBankCard.equals(submitFromData.get("bank_acco"))){
				
				respMap.put("trade_status", "2");
				
				respMap.put("retCode", HtfReturnEnum.DELECARDFAIL.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.DELECARDFAIL.getResDesc());
				
				return respMap;
			}
			
			
			respMap.put("trade_status", "0");
			
			respMap.put("retCode", HtfReturnEnum.SUCCESS.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.SUCCESS.getResDesc());
			
		} catch (Exception e) {							
			respMap.put("trade_status", "2");
			
			respMap.put("retCode", HtfReturnEnum.DELECARDFAIL.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.DELECARDFAIL.getResDesc());
			
			return respMap;
		}
		return respMap;
	}

	/**
	 * Title: getHtfVerifyCode<br/> 
	 * Description: 快捷短信验证码获取接口<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-31 上午9:56:53
	 *@parameter:Map<String, String> submitFromData
	**/
	public Map<String, String> getHtfVerifyCode(Map<String, String> submitFromData) {

		Map<String, String> respMap = new HashMap<String, String>();
		
		respMap=getMap();
		
		respMap.put("sp_partner", submitFromData.get("sp_partner"));
		
		respMap.put("modify_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		String type ="03";
		try {
			respMap=validationRequestDeposit(submitFromData,respMap,type);
			if(respMap.get("retCode")!=null){
				return respMap;
			}
			
			respMap.put("retCode", HtfReturnEnum.SUCCESS.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.SUCCESS.getResDesc());
			
		} catch (Exception e) {							
			
			respMap.put("retCode", HtfReturnEnum.SENDMESSAGEFAIL.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.SENDMESSAGEFAIL.getResDesc());
			
			return respMap;
		}
		return respMap;
	}
	
	/**
	 * Title: getHtfRedeem<br/> 
	 * Description:普通赎回接口(取现) <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-31 上午10:08:52
	 *@parameter:Map<String, String> submitFromData
	**/
	public Map<String, String> getHtfRedeem(Map<String, String> submitFromData) {
		
		CommonUntil commonUntil = new CommonUntil();

		Map<String, String> respMap = new HashMap<String, String>();
		respMap=getMap();
		
		respMap.put("sp_partner", submitFromData.get("sp_partner"));
		
		respMap.put("partner_acco_no", submitFromData.get("partner_acco_no"));
		
		respMap.put("fund_units", submitFromData.get("fund_units"));
		
		respMap.put("transaction_id", submitFromData.get("transaction_id"));
		
		respMap.put("order_type", submitFromData.get("order_type"));
		
		respMap.put("tran_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		respMap.put("time_end", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		respMap.put("fund_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
		//总资产
		respMap.put("fund_balance", "888888");
		//当日充值金额
		respMap.put("today_fund_units", "8888");
		//当日可快取余额
		respMap.put("today_fund_units_T0", "6666");
		
		String type ="05";
		try {
			respMap=validationRequestDeposit(submitFromData,respMap,type);
			if(respMap.get("retCode")!=null){
				respMap.put("trade_status", "1");
				return respMap;
			}
			//非法IP
			String errIp = commonUntil.getProperties("ERR_IP");
			if(errIp.equals(submitFromData.get("spbill_create_ip"))){
				respMap.put("trade_status", "2");
				
				respMap.put("retCode", HtfReturnEnum.IPFREEZE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.IPFREEZE.getResDesc());
				
				return respMap;
			}
			//银行卡未登记
			String bankCard = commonUntil.getProperties("ERR_BANK_CARD");
			if(bankCard.equals(submitFromData.get("bank_acco"))){
				respMap.put("trade_status", "2");
				
				respMap.put("retCode", HtfReturnEnum.BANKCARDNOTHAVE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.BANKCARDNOTHAVE.getResDesc());
				
				return respMap;
			}
			//账户被冻结
			String freezeAcc= commonUntil.getProperties("FREEZE_ACC");
			if(freezeAcc.equals(submitFromData.get("partner_acco_no"))){
				respMap.put("trade_status", "1");
				
				respMap.put("retCode", HtfReturnEnum.ACCFREEZE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.ACCFREEZE.getResDesc());
				
				return respMap;
			}
			
			//金额过小
			String money= submitFromData.get("fund_units");
			if(Integer.parseInt(money)<0.01){
				respMap.put("trade_status", "1");
				
				respMap.put("retCode", HtfReturnEnum.MINWITHDRAWMONEYERR.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.MINWITHDRAWMONEYERR.getResDesc());
				
				return respMap;
			}
			//金额过大
			if(Integer.parseInt(money)>999999){
				respMap.put("trade_status", "1");
				
				respMap.put("retCode", HtfReturnEnum.MAXWITHDRAWCOUNTERR.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.MAXWITHDRAWCOUNTERR.getResDesc());
				
				return respMap;
			}
			
			
			respMap.put("trade_status", "0");
			
			respMap.put("retCode", HtfReturnEnum.SUCCESS.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.SUCCESS.getResDesc());
			
		} catch (Exception e) {		
			respMap.put("trade_status", "1");
			
			respMap.put("retCode", HtfReturnEnum.FAILTRX.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.FAILTRX.getResDesc());
			
			return respMap;
		}
		return respMap;
	}
	
	/**
	 * Title: getHtfImRedeem<br/> 
	 * Description:快速赎回接口(快速取现) <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-31 上午10:08:52
	 *@parameter:Map<String, String> submitFromData
	**/
	public Map<String, String> getHtfImRedeem(Map<String, String> submitFromData) {
		
		CommonUntil commonUntil = new CommonUntil();

		Map<String, String> respMap = new HashMap<String, String>();
		
		respMap=getMap();
		
		respMap.put("sp_partner", submitFromData.get("sp_partner"));
		
		respMap.put("partner_acco_no", submitFromData.get("partner_acco_no"));
		
		respMap.put("fund_units", submitFromData.get("fund_units"));
		
		respMap.put("transaction_id", submitFromData.get("transaction_id"));
		
		respMap.put("order_type", submitFromData.get("order_type"));
		
		respMap.put("tran_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		respMap.put("time_end", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		respMap.put("fund_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
		//失败金额
		respMap.put("fail_fund_units", "1000");
		//总资产
		respMap.put("fund_balance", "888888");
		//当日充值金额
		respMap.put("today_fund_units", "8888");
		//当日可快取余额
		respMap.put("today_fund_units_T0", "6666");
		//当日已快取金额
		respMap.put("today_get_units_T0", "9999");
		//当月已快取金额
		respMap.put("month_get_units_T0", "100000");
		
		String type ="06";
		try {
			respMap=validationRequestDeposit(submitFromData,respMap,type);
			if(respMap.get("retCode")!=null){
				respMap.put("trade_status", "3");
				respMap.put("trade_msg", "头寸不足");
				return respMap;
			}
			//非法IP
			String errIp = commonUntil.getProperties("ERR_IP");
			if(errIp.equals(submitFromData.get("spbill_create_ip"))){
				respMap.put("trade_status", "1");
				respMap.put("trade_msg", "失败");
				respMap.put("retCode", HtfReturnEnum.IPFREEZE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.IPFREEZE.getResDesc());
				
				return respMap;
			}
			//银行卡未登记
			String bankCard = commonUntil.getProperties("ERR_BANK_CARD");
			if(bankCard.equals(submitFromData.get("bank_acco"))){
				respMap.put("trade_status", "1");
				respMap.put("trade_msg", "失败");
				
				respMap.put("retCode", HtfReturnEnum.BANKCARDNOTHAVE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.BANKCARDNOTHAVE.getResDesc());
				
				return respMap;
			}
			//账户被冻结
			String freezeAcc= commonUntil.getProperties("FREEZE_ACC");
			if(freezeAcc.equals(submitFromData.get("partner_acco_no"))){
				respMap.put("trade_status", "1");
				respMap.put("trade_msg", "失败");
				
				respMap.put("retCode", HtfReturnEnum.ACCFREEZE.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.ACCFREEZE.getResDesc());
				
				return respMap;
			}
			
			//金额过小
			String money= submitFromData.get("fund_units");
			if(Integer.parseInt(money)<0.01){
				respMap.put("trade_status", "1");
				respMap.put("trade_msg", "失败");
				
				respMap.put("retCode", HtfReturnEnum.MINWITHDRAWMONEYERR.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.MINWITHDRAWMONEYERR.getResDesc());
				
				return respMap;
			}
			//金额过大
			if(Integer.parseInt(money)>999999){
				respMap.put("trade_status", "1");
				respMap.put("trade_msg", "失败");
				
				respMap.put("retCode", HtfReturnEnum.MAXWITHDRAWCOUNTERR.getSysCode());
				
				respMap.put("retmsg", HtfReturnEnum.MAXWITHDRAWCOUNTERR.getResDesc());
				
				return respMap;
			}
			respMap.put("trade_status", "0");
			
			respMap.put("trade_msg", "成功");
			
			respMap.put("trade_msg", "SUCCESS");
			
			respMap.put("retCode", HtfReturnEnum.SUCCESS.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.SUCCESS.getResDesc());
			
		} catch (Exception e) {		
			respMap.put("trade_status", "4");
			
			respMap.put("trade_msg", "部分失败，当失败处理");
			
			respMap.put("retCode", HtfReturnEnum.FAILTRX.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.FAILTRX.getResDesc());
			
			return respMap;
		}
		return respMap;
	}
	
	/**
	 * Title: getHtfSingleTradeQry<br/> 
	 * Description:单笔交易查询接口 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-31 上午10:08:52
	 *@parameter:Map<String, String> submitFromData
	**/
	public Map<String, String> getHtfSingleTradeQry(Map<String, String> submitFromData) {

		Map<String, String> respMap = new HashMap<String, String>();
		respMap=getMap();
		
		respMap.put("sp_partner", submitFromData.get("sp_partner"));
		
		respMap.put("partner_acco_no", submitFromData.get("partner_acco_no"));
		
		respMap.put("fund_units", submitFromData.get("fund_units")==null?"1000":submitFromData.get("fund_units"));
		
		respMap.put("ref_transaction_id", submitFromData.get("ref_transaction_id"));
		
		respMap.put("order_type", submitFromData.get("order_type"));
		
		respMap.put("tran_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		respMap.put("time_end", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		respMap.put("fund_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
		//失败金额
		respMap.put("fail_fund_units", "1000");
		
		String type ="07";
		try {
			respMap=validationRequestDeposit(submitFromData,respMap,type);
			if(respMap.get("retCode")!=null){
				respMap.put("trade_status", "1");
				respMap.put("trade_msg", "失败");
				return respMap;
			}
			respMap.put("trade_status", "0");
			
			respMap.put("trade_msg", "成功");
			
			respMap.put("retCode", HtfReturnEnum.SUCCESS.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.SUCCESS.getResDesc());
			
		} catch (Exception e) {		
			respMap.put("trade_status", "1");
			
			respMap.put("trade_msg", "失败");
			
			respMap.put("retCode", HtfReturnEnum.FAILTRX.getSysCode());
			
			respMap.put("retmsg", HtfReturnEnum.FAILTRX.getResDesc());
			
			return respMap;
		}
		return respMap;
	}
	
	public static Map<String,String> validationRequestDeposit(Map<String, String> submitFromData,Map<String, String> respMap,String type){
		ResponseDeposit res =  new ResponseDeposit();
    	try {
    		String sp_partner =submitFromData.get("sp_partner");
			res=Clazz.Annotation(HtfEntity.class, "sp_partner", sp_partner);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("retcode", HtfReturnEnum.LOSTPARAM.getSysCode());
				}else{
					respMap.put("retcode", HtfReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("retMsg", res.getRespCode()+"[sp_partner]");
	    		return respMap;
	    	} 
			String transaction_id=submitFromData.get("transaction_id");
			res=Clazz.Annotation(HtfEntity.class, "transaction_id", transaction_id);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("retMsg", res.getRespCode()+"[transaction_id]");
	    		return respMap;
	    	}
			String spbill_create_ip=submitFromData.get("spbill_create_ip");
			res=Clazz.Annotation(HtfEntity.class, "spbill_create_ip", spbill_create_ip);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("retMsg", res.getRespCode()+"[spbill_create_ip]");
	    		return respMap;
	    	}
			if(!("05".equals(type) || "06".equals(type) || "07".equals(type))){
				String buyer_name=submitFromData.get("buyer_name");
				res=Clazz.Annotation(HtfEntity.class, "buyer_name", buyer_name);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[buyer_name]");
					return respMap;
				}
				String buyer_cert_type=submitFromData.get("buyer_cert_type");
				res=Clazz.Annotation(HtfEntity.class, "buyer_cert_type", buyer_cert_type);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[buyer_cert_type]");
					return respMap;
				}
				String buyer_cert_no=submitFromData.get("buyer_cert_no");
				res=Clazz.Annotation(HtfEntity.class, "buyer_cert_no", buyer_cert_no);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[buyer_cert_no]");
					return respMap;
				}
			}
			if(!"07".equals(type)){
				String bank_code=submitFromData.get("bank_code");
				res=Clazz.Annotation(HtfEntity.class, "bank_code", bank_code);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[bank_code]");
					return respMap;
				}
				String bank_acco=submitFromData.get("bank_acco");
				res=Clazz.Annotation(HtfEntity.class, "bank_acco", bank_acco);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[bank_acco]");
					return respMap;
				}
			}
			String partner_acco_no=submitFromData.get("partner_acco_no");
			res=Clazz.Annotation(HtfEntity.class, "partner_acco_no", partner_acco_no);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("retMsg", res.getRespCode()+"[partner_acco_no]");
	    		return respMap;
	    	}
			if("01".equals(type)){
				String authCode=submitFromData.get("authCode");
				res=Clazz.Annotation(HtfEntity.class, "authCode", authCode);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[authCode]");
					return respMap;
				}
			}
			if("01".equals(type) || "02".equals(type)){
				String acc_time=submitFromData.get("acc_time");
				res=Clazz.Annotation(HtfEntity.class, "acc_time", acc_time);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[acc_time]");
					return respMap;
				}
			}
			if("03".equals(type) || "01".equals(type)){
				String mobile_number=submitFromData.get("mobile_number");
				res=Clazz.Annotation(HtfEntity.class, "mobile_number", mobile_number);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[mobile_number]");
					return respMap;
				}
			}
			if("02".equals(type)){
				String trans_type=submitFromData.get("trans_type");
				res=Clazz.Annotation(HtfEntity.class, "trans_type", trans_type);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[trans_type]");
		    		return respMap;
		    	}
			}
			if("02".equals(type) || "05".equals(type) || "06".equals(type)){
				String fund_acco_no=submitFromData.get("fund_acco_no");
				res=Clazz.Annotation(HtfEntity.class, "fund_acco_no", fund_acco_no);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[fund_acco_no]");
					return respMap;
				}
				
			}
			if("05".equals(type) || "06".equals(type) || "07".equals(type)){
				if(!"07".equals(type)){
					String fund_no=submitFromData.get("fund_no");
					res=Clazz.Annotation(HtfEntity.class, "fund_no", fund_no);
					if(!"00000".equals(res.getRespCode())){
						if("F0029".equals(res.getRespCode())){
							respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
						}else{
							respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
						}
						respMap.put("retMsg", res.getRespCode()+"[fund_no]");
						return respMap;
					}
					String fund_units=submitFromData.get("fund_units");
					res=Clazz.Annotation(HtfEntity.class, "fund_units", fund_units);
					if(!"00000".equals(res.getRespCode())){
						if("F0029".equals(res.getRespCode())){
							respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
						}else{
							respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
						}
						respMap.put("retMsg", res.getRespCode()+"[fund_units]");
						return respMap;
					}
				}
				String order_type=submitFromData.get("order_type");
				res=Clazz.Annotation(HtfEntity.class, "order_type", order_type);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[order_type]");
					return respMap;
				}
				String time_start=submitFromData.get("time_start");
				res=Clazz.Annotation(HtfEntity.class, "time_start", time_start);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[time_start]");	
					return respMap;
				}
			}
			//原请求流水号
			if("07".equals(type)){
				String ref_transaction_id=submitFromData.get("ref_transaction_id");
				res=Clazz.Annotation(HtfEntity.class, "ref_transaction_id", ref_transaction_id);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("retCode", HtfReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("retCode", HtfReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("retMsg", res.getRespCode()+"[ref_transaction_id]");	
					return respMap;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			respMap.put("retCode", BankReturnEnum.ERRSYS.getResCode());
			respMap.put("retMsg", BankReturnEnum.ERRSYS.getResDesc());
    		return respMap;
		}
		return respMap;
	}
	
	public static Map<String,String> getMap(){
		 Map<String,String> paramMap=new HashMap<String,String>();
		 paramMap.put("sign_type", "RSA");
		 paramMap.put("service_version", "1.0");
		 paramMap.put("input_charset", "UTF-8");
		 paramMap.put("sign", "");
		 return paramMap;
	}
}
