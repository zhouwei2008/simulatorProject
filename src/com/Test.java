package com;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unionpay.acp.sdk.CertUtil;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SecureUtil;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String str ="txnType=01&respCode=00&frontUrl=http://61.148.205.138:8080/ACPTest/acp_front_url.do&currencyCode=156&channelType=08&Tn=201512471106475220000098403&respDesc=??&merId=302430148160010&txnSubType=01&txnAmt=1&version=5.0.0&signMethod=01&backUrl=http://61.148.205.138:8080/mobileUnti/acp_back_url.do&certId=40220995861346480087409489142384722381&encoding=UTF-8&bizType=000201&signature=fNr3xTLH//ayC8uZW8dmBMAFRlnlNjPwUF3g0nD2FCLsGI1sQShlTy+j7u2Dzp9yCpDMAE1GfidD0IuuBsP9RlMACV7bUfevZer2nalitE04EcaieLpWHgSIlNDTJCkWXnJU1OSVYeiMmM2vP11mqkrHfQPZb1+wY50ZAzzS1zM=&orderId=20151217111003&txnTime=20151217111003&accessType=0";
//		Map map=convertResultStringToMap(str);
//		validate(map,"UTF-8");
//		System.out.println(str);
		
		String regx="\\d{12}";
		
		Pattern pattern = Pattern.compile(regx);
		
		Matcher matcher = pattern.matcher("132336");
		
		if(matcher.matches()){
			
			System.out.println("SUCCESS"); 
		}
		
//		String regex = "a+";  
//        Pattern pattern = Pattern.compile(regex);  
//        Matcher matcher = pattern.matcher("okaaaa LetmeAseeaaa aa booa");  
//        String s = matcher.toString();  
//        System.out.println(s);  
		
	}
	 private static void convertResultStringJoinMap(String res, Map<String, String> map)
	  {
	    if ((res != null) && (!"".equals(res.trim()))) {
	      String[] resArray = res.split("&");
	      if (resArray.length != 0)
	        for (String arrayStr : resArray)
	          if ((arrayStr != null) && (!"".equals(arrayStr.trim())))
	          {
	            int index = arrayStr.indexOf("=");
	            if (-1 != index)
	            {
	              map.put(arrayStr.substring(0, index), arrayStr
	                .substring(index + 1));
	            }
	          }
	    }
	  }

	  public static Map<String, String> coverResultString2Map(String result)
	  {									
	    return convertResultStringToMap(result);
	  }

	  public static Map<String, String> convertResultStringToMap(String result)
	  {
	    if (result.contains("{"))
	    {
	      String separator = "\\{";
	      String[] res = result.split(separator);

	      Map map = new HashMap();

	      convertResultStringJoinMap(res[0], map);

	      for (int i = 1; i < res.length; i++)
	      {
	        int index = res[i].indexOf("}");

	        String specialValue = "{" + 
	          res[i].substring(0, index) + "}";

	        int indexKey = res[(i - 1)].lastIndexOf("&");
	        String specialKey = res[(i - 1)].substring(indexKey + 1, 
	          res[(i - 1)].length() - 1);

	        map.put(specialKey, specialValue);

	        String normalResult = res[i].substring(index + 2, res[i]
	          .length());

	        convertResultStringJoinMap(normalResult, map);
	      }

	      return map;
	    }

	    return convertResultString2Map(result);
	  }
	  private static Map<String, String> convertResultString2Map(String res)
	  {
	    Map map = null;
	    if ((res != null) && (!"".equals(res.trim()))) {
	      String[] resArray = res.split("&");
	      if (resArray.length != 0) {
	        map = new HashMap(resArray.length);
	        for (String arrayStr : resArray)
	          if ((arrayStr != null) && (!"".equals(arrayStr.trim())))
	          {
	            int index = arrayStr.indexOf("=");
	            if (-1 != index)
	            {
	              map.put(arrayStr.substring(0, index), arrayStr
	                .substring(index + 1));
	            }
	          }
	      }
	    }
	    return map;
	  }
	  
	  public static boolean validate(Map<String, String> resData, String encoding)
	  {
	    String stringSign = (String)resData.get("signature");
	    LogUtil.writeLog("返回报文中signature=[" + stringSign + "]");

	    String certId = (String)resData.get("certId");
	    LogUtil.writeLog("返回报文中certId=[" + certId + "]");

	    String stringData = coverMap2String(resData);
	    LogUtil.writeLog("返回报文中(不含signature域)的stringData=[" + stringData + "]");
	    try
	    {
	      return SecureUtil.validateSignBySoft(
	        CertUtil.getValidateKey(certId), SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData,encoding));
	    } catch (UnsupportedEncodingException e) {
	      LogUtil.writeErrorLog(e.getMessage(), e);
	    } catch (Exception e) {
	      LogUtil.writeErrorLog(e.getMessage(), e);
	    }
	    return false;
	  }
	  public static String coverMap2String(Map<String, String> data)
	  {
	    TreeMap tree = new TreeMap();
	    Iterator it = data.entrySet().iterator();
	    while (it.hasNext()) {
	      Map.Entry en = (Map.Entry)it.next();
	      if (!"signature".equals(((String)en.getKey()).trim()))
	      {
	        tree.put((String)en.getKey(), (String)en.getValue());
	      }
	    }
	    it = tree.entrySet().iterator();
	    StringBuffer sf = new StringBuffer();
	    while (it.hasNext()) {
	      Map.Entry en = (Map.Entry)it.next();
	      sf.append((String)en.getKey() + "=" + (String)en.getValue() + 
	        "&");
	    }
	    return sf.substring(0, sf.length() - 1);
	  }
}
