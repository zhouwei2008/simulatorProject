package com.htf;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class HtfUntil {
	
	// 私钥 用于我们提交的数据加密
	public static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANJwlPjJaR5XGkx4KcK5UyVi8YAHrZp1xEhMTkvY7PxUX+mzvo5ruaAaPzIEgsgeNqCRJq9B8frzzRgZcQkUrKLMIO1RtNH+98lgP1dGp/gR0RYRxtlcSOzH4r4XdHCELc2WX7Le8Pv+4x4qIspRD5TvnAIARhZg2EavqteK8D8BAgMBAAECgYEAsllV+FACtgMON9bzVaPpUts7X82iFTFQDBIXnXKYzvXXX+LFUkD8TjNQcdye1mm81HKQPaJ/Kbesj4soYJDUCY8XlcEjiRSjTJsEb3euL9DBukBNFL6ffXectnc863PNP6PjAufpHVYjDUtCjj1mJpyHA17mxdTMSwDgBxK6HN0CQQD255rKJeFtyUwnF9frc22rK3pIkh6AeRoLIfKpN/EiiNzoC8TeVZfzBbgofFSWwmI9Qhd0A6NcUdrUQ+urZddvAkEA2jEZvifd4GxQT7E7WpuPRLQTh1rlrZF2Yq6wokVHe/fJICP5PCavIrzbTO4u+E4hBvAnKpkReCNBDCgFo42YjwJANUwrksV4kx1n7exstKu3GcMJ445/PRZnM84BNtwXlm/a19Bqj+AEK/apGTw1elG9qSGSNH3wjqYwya9buSUDywJBAI6qtE5l+uoXaJvzIwAn3xHpPSuv/6XyCxqTZuNzsT4Z0uS1IO53zORF4I743rV1QJssxC9STt3jD1FHighbOXkCQQDtH7f5nhkK4hVOWA2bewQTayg1DRpX3sQmKni4pQt9NdljDUSMqo+iV5rlKqsz9C5l/Y/DoyflJ4LfD2dlj5Tw";
	// 公钥 用于对返回的数据解密
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDScJT4yWkeVxpMeCnCuVMlYvGAB62adcRITE5L2Oz8VF/ps76Oa7mgGj8yBILIHjagkSavQfH6880YGXEJFKyizCDtUbTR/vfJYD9XRqf4EdEWEcbZXEjsx+K+F3RwhC3Nll+y3vD7/uMeKiLKUQ+U75wCAEYWYNhGr6rXivA/AQIDAQAB";


	public static void main(String[] args) {
//		String str = "{\"tradeType\":\"03\",\"spbill_create_ip\":\"175.11.92.23\",\"sign_type\":\"\",\"input_charset\":\"UTF-8\",\"partner_acco_no\":\"U151111161537111\",\"buyer_name\":null,\"bank_code\":null,\"service_version\":\"1.0\",\"transaction_id\":\"2015111810395787277200010800\",\"sign\":\"\",\"bank_acco\":null,\"mobile_number\":null,\"sp_partner\":\"1000000040\",\"buyer_cert_type\":null,\"buyer_cert_no\":null}";
//		str = str.replaceAll("\"", "").replace("{", "").replace("}", "");
//		Map<String, String> map = convertResultString2Map(str);
//		System.out.println(map.get("tradeType"));
		String msg="eyJhdXRoQ29kZSI6IjU2ODQzOSIsInNwYmlsbF9jcmVhdGVfaXAiOiIxMjcuMC4wLjEiLCJzaWdu%250D%250AX3R5cGUiOiIiLCJpbnB1dF9jaGFyc2V0IjoiVVRGLTgiLCJwYXJ0bmVyX2FjY29fbm8iOiJVMTUx%250D%250AMTExMTYxNTM3MTExIiwiYnV5ZXJfbmFtZSI6IlpIT1VXRUkiLCJiYW5rX2NvZGUiOiI2NTI0MDAw%250D%250AMDAwMjMxNTY3OTg1Iiwic2VydmljZV92ZXJzaW9uIjoiMS4wIiwidHJhbnNhY3Rpb25faWQiOiIx%250D%250AMjM0NTY3ODkwMTIzIiwic2lnbiI6IiIsImJhbmtfYWNjbyI6IjEwMyIsIm1vYmlsZV9udW1iZXIi%250D%250AOiIxMzYxMTIzNjc0MiIsInNwX3BhcnRuZXIiOiIxMDAwMDAwMDQwIiwiYnV5ZXJfY2VydF90eXBl%250D%250AIjoiMSIsImJ1eWVyX2NlcnRfbm8iOiIxMzIzMzYxOTgxMDExNjIwMTgiLCJhY2NfdGltZSI6IjIw%250D%250AMTUxMjI5MTgyNzI4In0%253D";
        String sign="ccBuLzhMCS9OdEKwgRJLRmz%2Flom%2FxHCzkoZPzKICnIrCz%2Bc7Dc%2Brsf13%2B7nnE7bbUfUoNbsYy81BZDZIuCjYZ49djMwo1rzMPyzXWc7icXjDsc%2FGaL9GK2t2zkCYp%2FWg2bUNk7HkJHija6Q%2BCB5yKrmoin%2FQsQWaS5TD9IwOXis%3D";
//        String msg1="eyJhdXRoQ29kZSI6IjU2ODQzOSIsInNwYmlsbF9jcmVhdGVfaXAiOiIxMjcuMC4wLjEiLCJzaWdu%0D%0AX3R5cGUiOiIiLCJpbnB1dF9jaGFyc2V0IjoiVVRGLTgiLCJwYXJ0bmVyX2FjY29fbm8iOiJVMTUx%0D%0AMTExMTYxNTM3MTExIiwiYnV5ZXJfbmFtZSI6IlpIT1VXRUkiLCJiYW5rX2NvZGUiOiI2NTI0MDAw%0D%0AMDAwMjMxNTY3OTg1Iiwic2VydmljZV92ZXJzaW9uIjoiMS4wIiwidHJhbnNhY3Rpb25faWQiOiIx%0D%0AMjM0NTY3ODkwMTIzIiwic2lnbiI6IiIsImJhbmtfYWNjbyI6IjEwMyIsIm1vYmlsZV9udW1iZXIi%0D%0AOiIxMzYxMTIzNjc0MiIsInNwX3BhcnRuZXIiOiIxMDAwMDAwMDQwIiwiYnV5ZXJfY2VydF90eXBl%0D%0AIjoiMSIsImJ1eWVyX2NlcnRfbm8iOiIxMzIzMzYxOTgxMDExNjIwMTgiLCJhY2NfdGltZSI6IjIw%0D%0AMTUxMjMwMTAwMTM5In0%3D";
//        String sign1="MeJfLQr2LoDvNvSd2OP+BrOGbdwSZCggaE0jDvwOvueI8y8MjK1U+eDftsReG2dMKjdiFhadchhLr/G6rBWn+KOZifOOUlTN46acdHk55bqNDcis15OSoVUHtN6VzuUHEUia87RGoAX7tteWTzmd9yoRVE6HlWrZYsI7Yp60bQE=";
        HtfUntil htf = new HtfUntil();
        @SuppressWarnings("deprecation")
		String ss=htf.decryptDataWithPubkey(PUBLIC_KEY,URLDecoder.decode(sign),URLDecoder.decode(msg));
       // String ss1=htf.decryptDataWithPubkey(PUBLIC_KEY,sign1,msg1);
        Map<String,String> respMap = htf.convertResultString2Map(ss);
        HtfService htfService = new HtfService();
        respMap=htfService.getHtfQuickChannelSign(respMap);
        System.out.println("respMap is :"+respMap);
        JSONObject jsonObject = JSONObject.fromObject(respMap);
        // 获取HTF请求报文
        String respStr = htf.getHTFRequstBody(jsonObject.toString());
        System.out.println("respStr is "+respStr);
        
        System.out.println("11111111"+getAfterDay(new Date()));

	}
	
	/**
	 * Title: convertResultString2Map<br/> 
	 * Description:把String转换为Map <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午11:19:56
	 *@parameter:
		 **/
	public Map<String, String> convertResultString2Map(String res) {
		Map<String, String> map = null;
		if ((res != null) && (!"".equals(res.trim()))) {
			res=res.replaceAll("\"", "").replace("{", "").replace("}", "");
			String[] resArray = res.split(",");
			if (resArray.length != 0) {
				map = new HashMap<String, String>(resArray.length);
				for (String arrayStr : resArray)
					if ((arrayStr != null) && (!"".equals(arrayStr.trim()))) {
						int index = arrayStr.indexOf(":");
						if (-1 != index) {
							map.put(arrayStr.substring(0, index),
									arrayStr.substring(index + 1));
						}
					}
			}
		}
		return map;
	}

	
	/**
	 * Title: decryptDataWithPubkey<br/> 
	 * Description:解密加密串 <br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午11:21:01
	 *@parameter:
		 **/
	public String decryptDataWithPubkey(String publicKey, String sign,String msg) {
		String plainText = "";
		try {
			boolean verifyFlag = false;
			msg = URLDecoder.decode(msg, "UTF-8");
			verifyFlag = rsaVerify(publicKey, sign, msg, "UTF-8");
			if (verifyFlag) {
				BASE64Decoder base64decoder = new BASE64Decoder();
				plainText = new String(base64decoder.decodeBuffer(msg), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if ("".equals(plainText)) {
			return null;
		}
		return plainText;
	}

	private boolean rsaVerify(String pubKey, String sign, String src,
			String encoding) {
		boolean rs = false;
		try {
			BASE64Decoder base64decoder = new BASE64Decoder();
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
					base64decoder.decodeBuffer(pubKey));
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);

			Signature sigEng = Signature.getInstance("SHA1withRSA");
			sigEng.initVerify(rsaPubKey);
			sigEng.update(src.getBytes(encoding));
			byte[] signature = base64decoder.decodeBuffer(sign);
			rs = sigEng.verify(signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Title: getHTFRequstBody<br/> 
	 * Description: 加密返回参数<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午11:22:51
	 *@parameter:
		 **/
	public String getHTFRequstBody(String jsonData) {
        String[] reqmsgs = getRequestMsg(jsonData, PRIVATE_KEY);
        StringBuffer postMsg = new StringBuffer();
        postMsg.append("sign=").append(reqmsgs[0]).append("&msg=")
                .append(reqmsgs[1]);
        return postMsg.toString();

    }
	
	public String[] getRequestMsg(String post, String privateKey) {
        String param[] = new String[2];
        try {
            BASE64Encoder base64encoder = new BASE64Encoder();
            String content = post;
            content = base64encoder.encode(content.getBytes("UTF-8"));
            String econ = envolopData(privateKey, content);
            content = URLEncoder.encode(content, "UTF-8");
            param[0] = econ;
            param[1] = content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }
	/**
	 * Title: envolopData<br/> 
	 * Description: 去除不必要的字段<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-4 上午11:23:29
	 *@parameter:
		 **/
	public String envolopData(String privKey, String dataStr) {
        String plainText = "";
        try {
            BASE64Decoder base64decoder = new BASE64Decoder();
            BASE64Encoder base64encoder = new BASE64Encoder();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64decoder.decodeBuffer(privKey));
            KeyFactory fac = KeyFactory.getInstance("RSA");

            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
            Signature sigEng = Signature.getInstance("SHA1withRSA");
            sigEng.initSign(privateKey);
            sigEng.update(dataStr.getBytes("UTF-8"));
            byte[] signature = sigEng.sign();
            plainText = base64encoder.encodeBuffer(signature);
            plainText = plainText.replaceAll("\r|\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }
	
	/**
	 * Title: getAfterDay<br/> 
	 * Description:获取当前时间的后一天时间<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-5 下午2:01:47
	 *@parameter:
		 **/
    @SuppressWarnings("unused")
	private static Date  getAfterDay(Date date){  
    	Calendar cl = Calendar.getInstance();   
    	cl.setTime(date);
    	cl.add(Calendar.DAY_OF_MONTH, 1);
        Date day=cl.getTime();
        return day;
    }  
}
