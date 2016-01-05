package com;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class CommonUntil {

	// 获得配置文件properties属性值
	public String getProperties(String name) {
		Properties defaultProps = new Properties();
		InputStream in = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream("htfInfoSet.properties");
			defaultProps.load(in);
			return defaultProps.getProperty(name);
		} catch (Exception e) {
			System.err.println("Error: could not find the config of bank");
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @throws ParseException
	 */
	public static int calcBetweenDays(String date1, String date2) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
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
	
	/**
     * 取得两个日期间隔秒数（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     *
     * @return 间隔秒数
     */
    public long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (Math.abs(sysDate.getTimeInMillis() - failDate.getTimeInMillis())) / 1000;
    }
    
	/**
	 * Title: getAfterDay<br/> 
	 * Description:获取当前时间的后一天时间<br/> 
	 * Company: gigold<br/> 
	 *@author: zhouyong-pc
	 *@date : 2016-1-5 下午2:01:47
	 *@parameter:
		 **/
	public Date getAfterDay(Date date){  
    	Calendar cl = Calendar.getInstance();   
    	cl.setTime(date);
    	cl.add(Calendar.DAY_OF_MONTH, 1);
        Date day=cl.getTime();
        return day;
    }  
   
   public static void main(String[] args) throws ParseException {
	   Date one = new SimpleDateFormat("yyyyMMddHHmmss").parse("20151231152735");
	   Date two = new SimpleDateFormat("yyyyMMddHHmmss").parse("20151231152845");
	   CommonUntil cu= new CommonUntil();
	   System.out.println(cu.getDiffSeconds(one,two));
	   System.out.println(new SimpleDateFormat("yyyyMMdd").format(cu.getAfterDay(new Date())));
   }
   
}
