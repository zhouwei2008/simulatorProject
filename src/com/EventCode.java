package com;

public class EventCode {
		
		//基本信息判断的以3开始
		public static final String WEB_PARAMNULL                  ="F0024";           	//参数缺少
		public static final String WEB_PARAMFORMAT      		  ="F0025";           	//参数类型或格式错误
		public static final String WEB_PARAMEMPTY       		  ="F0026";           	//参数空值错误
		public static final String WEB_PARAM_LENGTH_OVERFLOW      ="F0027";           	//参数长度溢出
		public static final String WEB_PARAM_LENGTH    			  ="F0028";           	//长度不符设定
		public static final String WEB_PARAM_LOST       		  ="F0029";           	//缺少必要参数
		public static final String WEB_PARAM_CONFLICT   		  ="F0030";           	//参数冲突
		public static final String WEB_PARAM_FILTER     		  ="F0031";           	//参数过滤错误,含非法字符

		public static final String CREATE_REFUNDORDER_ERROR 	  ="F0034";				//创建退款订单错误

		public static final String CREATE_REFUNDORDER_JRN_ERROR   ="F0035";				//创建退款订单流水错误
		public static final String CREATE_ORDER_ERROR   		  ="F0036";				//创建支付订单错误
		public static final String CREATE_ORDER_JRN_ERROR   	  ="F0037";				//创建支付订单流水错误
}
