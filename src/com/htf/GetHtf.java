package com.htf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class GetHtf extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1109588597345331240L;
	
	public Logger logger = LoggerFactory.getLogger(this.getClass());
	// 私钥 用于我们提交的数据加密
	public static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANJwlPjJaR5XGkx4KcK5UyVi8YAHrZp1xEhMTkvY7PxUX+mzvo5ruaAaPzIEgsgeNqCRJq9B8frzzRgZcQkUrKLMIO1RtNH+98lgP1dGp/gR0RYRxtlcSOzH4r4XdHCELc2WX7Le8Pv+4x4qIspRD5TvnAIARhZg2EavqteK8D8BAgMBAAECgYEAsllV+FACtgMON9bzVaPpUts7X82iFTFQDBIXnXKYzvXXX+LFUkD8TjNQcdye1mm81HKQPaJ/Kbesj4soYJDUCY8XlcEjiRSjTJsEb3euL9DBukBNFL6ffXectnc863PNP6PjAufpHVYjDUtCjj1mJpyHA17mxdTMSwDgBxK6HN0CQQD255rKJeFtyUwnF9frc22rK3pIkh6AeRoLIfKpN/EiiNzoC8TeVZfzBbgofFSWwmI9Qhd0A6NcUdrUQ+urZddvAkEA2jEZvifd4GxQT7E7WpuPRLQTh1rlrZF2Yq6wokVHe/fJICP5PCavIrzbTO4u+E4hBvAnKpkReCNBDCgFo42YjwJANUwrksV4kx1n7exstKu3GcMJ445/PRZnM84BNtwXlm/a19Bqj+AEK/apGTw1elG9qSGSNH3wjqYwya9buSUDywJBAI6qtE5l+uoXaJvzIwAn3xHpPSuv/6XyCxqTZuNzsT4Z0uS1IO53zORF4I743rV1QJssxC9STt3jD1FHighbOXkCQQDtH7f5nhkK4hVOWA2bewQTayg1DRpX3sQmKni4pQt9NdljDUSMqo+iV5rlKqsz9C5l/Y/DoyflJ4LfD2dlj5Tw";
	// 公钥 用于对返回的数据解密
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDScJT4yWkeVxpMeCnCuVMlYvGAB62adcRITE5L2Oz8VF/ps76Oa7mgGj8yBILIHjagkSavQfH6880YGXEJFKyizCDtUbTR/vfJYD9XRqf4EdEWEcbZXEjsx+K+F3RwhC3Nll+y3vD7/uMeKiLKUQ+U75wCAEYWYNhGr6rXivA/AQIDAQAB";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("这个是doget方法的开始");
		HtfUntil htf = new HtfUntil();
		try {
			String sign = request.getParameter("sign");
			
			String msg = request.getParameter("msg");
			
			String ss = htf.decryptDataWithPubkey(PUBLIC_KEY, sign, msg);

			Map<String, String> respMap = new HashMap<String, String>();
			if(ss==null || "".equals(ss)){
				respMap.put("retCode", HtfReturnEnum.FAILSIGN.getSysCode());
				respMap.put("retMsg", HtfReturnEnum.FAILSIGN.getResDesc());
			}else{
				respMap = htf.convertResultString2Map(ss);
				
				HtfService htfService = new HtfService();
				if("01".equals(respMap.get("jrnType"))){//开户
					respMap = htfService.getHtfQuickChannelSign(respMap);
				}
				if("02".equals(respMap.get("jrnType"))){//删卡
					respMap = htfService.getHtfDelCard(respMap);
				}
				if("03".equals(respMap.get("jrnType"))){// 03:获取验证码
					respMap = htfService.getHtfVerifyCode(respMap);
				}
				if("04".equals(respMap.get("jrnType"))){//04:申购
					respMap = htfService.getHtfQuickChannelSign(respMap);
				}
				if("05".equals(respMap.get("jrnType"))){// 05:普通赎回
					respMap = htfService.getHtfRedeem(respMap);
				}
				if("06".equals(respMap.get("jrnType"))){// 06:快速赎回
					respMap = htfService.getHtfImRedeem(respMap);
				}
				if("07".equals(respMap.get("jrnType"))){// 07:单笔查询 
					respMap = htfService.getHtfSingleTradeQry(respMap);
				}
				if("08".equals(respMap.get("jrnType"))){//08:通用额度查询
					respMap = htfService.getHtfQuickChannelSign(respMap);
				}
				if("09".equals(respMap.get("jrnType"))){// 09:对账文件下载 
					respMap = htfService.getHtfQuickChannelSign(respMap);
				}
				if("10".equals(respMap.get("jrnType"))){//  10:支付 
					respMap = htfService.getHtfQuickChannelSign(respMap);
				}
				if("11".equals(respMap.get("jrnType"))){// 11:退款
					respMap = htfService.getHtfQuickChannelSign(respMap);
				}
			}
			JSONObject jsonObject = JSONObject.fromObject(respMap);
	        // 获取HTF请求报文
	        String respStr = htf.getHTFRequstBody(jsonObject.toString());
	        
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			out.println(respStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		// do nothing.
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
