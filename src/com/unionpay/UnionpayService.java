package com.unionpay;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;

import com.Clazz;
import com.ResponseDeposit;
import com.unionpay.acp.sdk.HttpClient;
import com.unionpay.acp.sdk.SDKUtil;


public class UnionpayService {
	
	private static Logger logger= Logger.getLogger(UnionpayService.class);
	//商户ID 为银行分配
	private final static String MERID="302430148160010";
	//版本信息，银行发布的版本号
	private final static String VERSION="5.0.0";
	//编码，银行系统的编码格式
	private final static String ENCODING="UTF-8";
	//签名方法、银行进行签名的方法
	private final static String SIGNMETHOD="01";
	//系统是否开启
	private final static boolean ISOPEN=true;
	
	/**
	 * Title: SimulatorUnionpayReturnTn<br/> 
	 * Description:模拟银行返回Tn <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-22 上午10:41:00
	 *@parameter:
	 *@param submitFromData 
	 *@return Map
	 *
	**/
	public static Map<String,String> SimulatorUnionpayReturnTn(Map<String, String> submitFromData){
		Map<String,String> newSubmitFromData = new HashMap<String,String>();
		newSubmitFromData=submitFromData;
		String type="getTn";
		try{
			//得到上传的验签字段
			String oldSignature=submitFromData.get("signature");
			newSubmitFromData.put("merId", MERID);
			newSubmitFromData.put("version", VERSION);
			newSubmitFromData.put("encoding", ENCODING);
			newSubmitFromData.put("signMethod", SIGNMETHOD);
			newSubmitFromData.remove("signature");
			
			newSubmitFromData=SignUntil.signData(newSubmitFromData);
			//得到新生成的验签字段
			String newSignature=newSubmitFromData.get("signature");
			
			UnionpayService unionpayService = new UnionpayService();
			//验证签名是否正确 11
			if(!oldSignature.equals(newSignature)){
				submitFromData.put("respCode", BankReturnEnum.FAILSIGN.getResCode());
				submitFromData.put("respMsg", BankReturnEnum.FAILSIGN.getResDesc());
				submitFromData=SignUntil.signData(submitFromData);
				return submitFromData;
			}
			//http连接异常返回空map
			String HttpErr=unionpayService.getProperties("HTTP_ERR");
			if("true".equals(HttpErr)){
				Map<String,String> map=submitUrl(submitFromData,"");
				if(map.toString().length()<=2){
					map=SignUntil.signData(map);
					return map;
				}
			}
			//验证格式是否正确 10,是否缺少必要字段 13及交易金额是否超限33
			newSubmitFromData=validationRequestDeposit(submitFromData,type);
			if(newSubmitFromData.get("respCode")!=null){
				submitFromData=SignUntil.signData(submitFromData);
				return newSubmitFromData;
			}
			//验证系统是否开启 02
			if(!ISOPEN){
				submitFromData.put("respCode", BankReturnEnum.NOTOPEN.getResCode());
				submitFromData.put("respMsg", BankReturnEnum.NOTOPEN.getResDesc());
				submitFromData=SignUntil.signData(submitFromData);
				return submitFromData;
			}
			//验证订单是否是处理中状态03\04\05
			
			String orderStatus=unionpayService.getProperties("ORDER_HANDLE");
			if("03".equals(orderStatus)){
				submitFromData.put("respCode", BankReturnEnum.OUTTIME.getResCode());
				submitFromData.put("respMsg", BankReturnEnum.OUTTIME.getResDesc());
				submitFromData=SignUntil.signData(submitFromData);
				return submitFromData;
			}
			if("04".equals(orderStatus)){
				submitFromData.put("respCode", BankReturnEnum.FAILSTATUS.getResCode());
				submitFromData.put("respMsg", BankReturnEnum.FAILSTATUS.getResDesc());
				submitFromData=SignUntil.signData(submitFromData);
				return submitFromData;
			}
			if("05".equals(orderStatus)){
				submitFromData.put("respCode", BankReturnEnum.TRXHAND.getResCode());
				submitFromData.put("respMsg", BankReturnEnum.TRXHAND.getResDesc());
				submitFromData=SignUntil.signData(submitFromData);
				return submitFromData;
			}
			//重复的订单，获取的tn相同
			String outOrderNo=unionpayService.getProperties("REFUND_OUTORDER_NO");
			if(outOrderNo.indexOf(submitFromData.get("orderId"))>=0){
				submitFromData.put("respCode", BankReturnEnum.SUCCESS.getResCode());
				submitFromData.put("respMsg", BankReturnEnum.SUCCESS.getResDesc());
				submitFromData.put("Tn", outOrderNo+"001");
				submitFromData=SignUntil.signData(submitFromData);
				return submitFromData;
			}
			//银行商户状态验证 31
			String merStatus=unionpayService.getProperties("UNIONPAY_MER_STATUS");
			if(!"00".equals(merStatus)){
				submitFromData.put("respCode", BankReturnEnum.FAILMERSTATUS.getResCode());
				submitFromData.put("respMsg", BankReturnEnum.FAILMERSTATUS.getResDesc());
				submitFromData=SignUntil.signData(submitFromData);
				return submitFromData;
			}
			//自动生成Tn
			String tn=getTn();
			submitFromData.put("Tn", tn);
			submitFromData.put("respCode", "00");
			submitFromData.put("respDesc", "成功");
			submitFromData=SignUntil.signData(submitFromData);
		}catch(Exception e){
			submitFromData.put("respCode", BankReturnEnum.FAILTRX.getResCode());
			submitFromData.put("respDesc", BankReturnEnum.FAILTRX.getResDesc());
			submitFromData=SignUntil.signData(submitFromData);
			return submitFromData;
		}
		return submitFromData;
	}
	/**
	 * Title: SimulatorUnionpayQueryResult<br/> 
	 * Description: 模拟银行退款返回<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-23 下午1:40:16
	 *@parameter:submitFromData  提交银行所需字段map
	**/
	public static Map<String,String> SimulatorUnionpayQueryResult(Map<String, String> submitFromData){
		Map<String,String> respMap = new HashMap<String,String>();
		respMap = submitFromData;
		String type="refund";
		try{
			//得到上传的验签字段
			String oldSignature=submitFromData.get("signature");
			respMap.put("merId", MERID);
			respMap.put("version", VERSION);
			respMap.put("encoding", ENCODING);
			respMap.put("signMethod", SIGNMETHOD);
			respMap.remove("signature");
			respMap=SignUntil.signData(respMap);
			//得到新生成的验签字段
			String newSignature=respMap.get("signature");
			//验证签名是否正确 11
			if(!oldSignature.equals(newSignature)){
				respMap.put("respCode", BankReturnEnum.FAILSIGN.getResCode());
				respMap.put("respMsg", BankReturnEnum.FAILSIGN.getResDesc());
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			//验证格式是否正确 10,是否缺少必要字段 13及交易金额是否超限33
			respMap=validationRequestDeposit(submitFromData,type);
			if(respMap.get("respCode")!=null){
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			//验证系统是否开启 02
			if(!ISOPEN){
				respMap.put("respCode", BankReturnEnum.NOTOPEN.getResCode());
				respMap.put("respMsg", BankReturnEnum.NOTOPEN.getResDesc());
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			//验证订单是否是处理中状态03\04\05
			UnionpayService unionpayService = new UnionpayService();
			String orderStatus=unionpayService.getProperties("ORDER_HANDLE");
			if("03".equals(orderStatus)){
				respMap.put("respCode", BankReturnEnum.OUTTIME.getResCode());
				respMap.put("respMsg", BankReturnEnum.OUTTIME.getResDesc());
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			if("04".equals(orderStatus)){
				respMap.put("respCode", BankReturnEnum.FAILSTATUS.getResCode());
				respMap.put("respMsg", BankReturnEnum.FAILSTATUS.getResDesc());
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			if("05".equals(orderStatus)){
				respMap.put("respCode", BankReturnEnum.TRXHAND.getResCode());
				respMap.put("respMsg", BankReturnEnum.TRXHAND.getResDesc());
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			//验证订单是否重复 12
			String haveOutOrderNo=unionpayService.getProperties("REFUND_OUTORDER_NO");
			if(haveOutOrderNo.indexOf(submitFromData.get("orderId"))>=0){
				respMap.put("respCode", BankReturnEnum.REPEATTRX.getResCode());
				respMap.put("respMsg", BankReturnEnum.REPEATTRX.getResDesc());
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			//银行商户状态验证 31
			String merStatus = unionpayService.getProperties("UNIONPAY_MER_STATUS");
			if(!"00".equals(merStatus)){
				respMap.put("respCode", BankReturnEnum.FAILMERSTATUS.getResCode());
				respMap.put("respMsg", BankReturnEnum.FAILMERSTATUS.getResDesc());
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			//超出银行受理时间 39
			String txnTime = submitFromData.get("txnTime");
			String nowTime = new SimpleDateFormat("yyyyMMdd").format(new Date());
			int day=calcBetweenDays(txnTime,nowTime);
			if(day>30){
				respMap.put("respCode", BankReturnEnum.OUTDATETIME.getResCode());
				respMap.put("respMsg", BankReturnEnum.OUTDATETIME.getResDesc());
				respMap=SignUntil.signData(respMap);
				return respMap;
			}
			respMap.put("respCode", "00");
			respMap.put("respDesc", "成功");
			respMap=SignUntil.signData(respMap);
			
		}catch(Exception e){
			respMap.put("respCode", BankReturnEnum.FAILTRX.getResCode());
			respMap.put("respMsg", BankReturnEnum.FAILTRX.getResDesc());
			respMap=SignUntil.signData(respMap);
			return respMap;
		}
		return respMap;
	}
	
	
	/**
	 * Title: validationRequestDeposit<br/> 
	 * Description:验证上传字段 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-22 上午10:38:17
	 *@parameter:
	 * @param submitFromData 提交map        
	**/
	public static Map<String,String> validationRequestDeposit(Map<String, String> submitFromData,String type){
		Map<String,String> respMap = new HashMap<String,String>();
		respMap=submitFromData;
		ResponseDeposit res =  new ResponseDeposit();
		logger.info("银行验证必须字段开始");
    	try {
    		String version =submitFromData.get("version");
			res=Clazz.Annotation(UnionpayEntity.class, "version", version);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[version]");
	    		return respMap;
	    	} 
			String encoding=submitFromData.get("encoding");
			res=Clazz.Annotation(UnionpayEntity.class, "encoding", encoding);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[encoding]");
	    		return respMap;
	    	}
			String signMethod=submitFromData.get("signMethod");
			res=Clazz.Annotation(UnionpayEntity.class, "signMethod", signMethod);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[signMethod]");
	    		return respMap;
	    	}
			String txnType=submitFromData.get("txnType");
			res=Clazz.Annotation(UnionpayEntity.class, "txnType", txnType);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[txnType]");
	    		return respMap;
	    	}
			String txnSubType=submitFromData.get("txnSubType");
			res=Clazz.Annotation(UnionpayEntity.class, "txnSubType", txnSubType);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[txnSubType]");
	    		return respMap;
	    	}
			String bizType=submitFromData.get("bizType");
			res=Clazz.Annotation(UnionpayEntity.class, "bizType", bizType);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[bizType]");
	    		return respMap;
	    	}
			String channelType=submitFromData.get("channelType");
			res=Clazz.Annotation(UnionpayEntity.class, "channelType", channelType);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[channelType]");
	    		return respMap;
	    	}
			String backUrl=submitFromData.get("backUrl");
			res=Clazz.Annotation(UnionpayEntity.class, "backUrl", backUrl);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[backUrl]");
	    		return respMap;
	    	}
			String accessType=submitFromData.get("accessType");
			res=Clazz.Annotation(UnionpayEntity.class, "accessType", accessType);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[accessType]");
	    		return respMap;
	    	}
			String merId=submitFromData.get("merId");
			res=Clazz.Annotation(UnionpayEntity.class, "merId", merId);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[merId]");
	    		return respMap;
	    	}
			String orderId=submitFromData.get("orderId");
			res=Clazz.Annotation(UnionpayEntity.class, "orderId", orderId);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[orderId]");
	    		return respMap;
	    	}
			String txnTime=submitFromData.get("txnTime");
			res=Clazz.Annotation(UnionpayEntity.class, "txnTime", txnTime);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[txnTime]");
	    		return respMap;
	    	}
			String txnAmt=submitFromData.get("txnAmt");
			res=Clazz.Annotation(UnionpayEntity.class, "txnAmt", txnAmt);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else if("F0028".equals(res.getRespCode())){//交易金额超限
					respMap.put("respCode", BankReturnEnum.MAXTRXMONEY.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[txnAmt]");
	    		return respMap;
	    	}
			String currencyCode=submitFromData.get("currencyCode");
			res=Clazz.Annotation(UnionpayEntity.class, "currencyCode", currencyCode);
			if(!"00000".equals(res.getRespCode())){
				if("F0029".equals(res.getRespCode())){
					respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
				}else{
					respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
				}
				respMap.put("respMsg", res.getRespCode()+"[currencyCode]");
	    		return respMap;
	    	}
			//原交易的查询id
			if("refund".equals(type)){
				String origQryId =submitFromData.get("origQryId");
				res=Clazz.Annotation(UnionpayEntity.class, "origQryId", origQryId);
				if(!"00000".equals(res.getRespCode())){
					if("F0029".equals(res.getRespCode())){
						respMap.put("respCode", BankReturnEnum.LOSTPARAM.getResCode());
					}else{
						respMap.put("respCode", BankReturnEnum.ERRFORMAT.getResCode());
					}
					respMap.put("respMsg", res.getRespCode()+"[origQryId]");
					return respMap;
				} 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			respMap.put("respCode", BankReturnEnum.ERRSYS.getResCode());
			respMap.put("respMsg", BankReturnEnum.ERRSYS.getResDesc());
    		return respMap;
		}
    	
		return respMap;
	}
	
	/**
	 * Title: getTn<br/> 
	 * Description: 生成随机Tn<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2015-12-23 下午1:44:25
	 *@parameter:
	 **/
	public static String getTn() throws Exception{
		String tn="";
		try{
			//取得当前时间
			String date=new SimpleDateFormat("yyyyMMssHHmmssSSS").format(new Date());
			//G+17位时间+10位序列号=28位流水号
			tn=date+left(10,randomApp());
		}catch(Exception e){
			
		}
		return tn;
	}
	//左补齐位数
	public static String  left(int digit, int num){
		String str = "";
		try{
			str=String.format("%0"+digit+"d", num);
		}catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}
	//得到随机数
	public static int  randomApp(){
		Random ran=new Random();
		int s=0;
		try{
			s=ran.nextInt(100000);
		}catch(Exception e){
			e.printStackTrace();
		}	
		return s;
	}
	//获得配置文件properties属性值
	public String getProperties(String name){
	    Properties defaultProps = new Properties();
        InputStream in = null;
        try {
        	in = this.getClass().getClassLoader().getResourceAsStream("bankInfoSet.properties");
            defaultProps.load(in);
            return defaultProps.getProperty(name);
        } catch (Exception e) {
            System.err.println("Error: could not find the config of bank");
            e.printStackTrace();
            return null;
        }
		
	}
	 /**
     *计算两个日期之间相差的天数
     * @throws ParseException
     */
    public static int calcBetweenDays(String date1, String date2) throws ParseException {
    	SimpleDateFormat df= new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date d1 = df.parse(date1);
        Date d2 = df.parse(date2);

        cal.setTime(d1);
        long t1 = cal.getTimeInMillis();
        cal.setTime(d2);
        long t2 = cal.getTimeInMillis();
        long days = (t2 - t1) / (1000 * 3600 * 24);
        int a = (int) days;
        return a;
    }
    
    public static Map<String, String> submitUrl(Map<String, String> submitFromData, String requestUrl) {
		String resultString = "";
		System.out.println("requestUrl====" + requestUrl);
		System.out.println("submitFromData====" + submitFromData.toString());
		/**
		 * 发送
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, ENCODING);
			if (200 == status) {
				resultString = hc.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> resData = new HashMap<String, String>();
		/**
		 * 验证签名
		 */
		if (null != resultString && !"".equals(resultString)) {
			// 将返回结果转换为map
			resData = SDKUtil.convertResultStringToMap(resultString);
			if (SDKUtil.validate(resData, ENCODING)) {
				System.out.println("验证签名成功");
			} else {
				System.out.println("验证签名失败");
				logger.warn("验证签名失败");
			}
			// 打印返回报文
			System.out.println("打印返回报文：" + resultString);
		}
		return resData;
	}
    
    public static void main(String[] args) throws ParseException {
    	UnionpayService us= new UnionpayService();
		int day=calcBetweenDays("20151022", "20151222");
		System.out.println("两个日期相差"+day+"天");
		String outOrder=us.getProperties("HTTP_ERR");
		System.out.println("获取属性配置文件中外部订单号:"+outOrder+"true".equals(outOrder));
		System.out.println("字符串所在的起始位置:"+outOrder.indexOf("20151026141501"));
		
		
	}
}
