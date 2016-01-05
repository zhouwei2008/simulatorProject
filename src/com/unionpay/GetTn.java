package com.unionpay;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unionpay.acp.sdk.SDKConfig;

public class GetTn extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1109588597345331240L;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			SDKConfig.getConfig().loadPropertiesFromSrc();
			Map<String,String> respMap = new HashMap<String,String>();
			Map<String, String> data = new HashMap<String, String>();
			// 版本号
			data.put("version", request.getParameter("version"));
			// 字符集编码 默认"UTF-8"
			data.put("encoding", request.getParameter("encoding"));
			// 签名方法 01 RSA
			data.put("signMethod", request.getParameter("signMethod"));
			// 交易类型 01-消费
			data.put("txnType", request.getParameter("txnType"));
			// 交易子类型 01:自助消费 02:订购 03:分期付款
			data.put("txnSubType", request.getParameter("txnSubType"));
			// 业务类型
			data.put("bizType", request.getParameter("bizType"));
			// 渠道类型，07-PC，08-手机
			data.put("channelType", request.getParameter("channelType"));
			// 前台通知地址 ，控件接入方式无作用
			data.put("frontUrl", request.getParameter("frontUrl"));
			// 后台通知地址
			data.put("backUrl", request.getParameter("backUrl"));
			// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
			data.put("accessType", request.getParameter("accessType"));
			// 商户号码，请改成自己的商户号
			data.put("merId", request.getParameter("merId"));
			// 商户订单号，8-40位数字字母
			data.put("orderId", request.getParameter("orderId"));
			// 订单发送时间，取系统时间
			data.put("txnTime", request.getParameter("txnTime"));
			// 交易金额，单位分
			data.put("txnAmt", request.getParameter("txnAmt"));
			if("04".equals(request.getParameter("txnType"))){
				data.put("origQryId", request.getParameter("origQryId"));
			}
			// 交易币种
			data.put("currencyCode", request.getParameter("currencyCode"));

			data = SignUntil.signData(data);
			if("04".equals(request.getParameter("txnType"))){
				respMap=UnionpayService.SimulatorUnionpayQueryResult(data);
			}else{
				respMap=UnionpayService.SimulatorUnionpayReturnTn(data);
			}
			// Set response content type
			response.setContentType("text/html");
			String respStr=respMap.toString().replace("{", "").replace("}", "").replaceAll(", ", "&");

			// Actual logic goes here.
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(respStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void destroy() {
		// do nothing.
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		this.doGet(request, response);
	}
}
