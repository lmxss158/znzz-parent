package com.znzz.app.web.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
/**
 * 时间工具类
 * @author wangqiang
 *
 */
public class DateUtils {
	
	


	/**
	 * 获取当前小时
	 * @return
	 */
	public static String getCurHour(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH") ;
		return sdf.format(new Date()) ;
	}
	
	/**
	 * 获取Date
	 * @return
	 */
	public static Date getDateObj(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
		Date date=null ;
		try {
			date = sdf.parse(time) ;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date ;
	}
	
	
	/**
	 * 获取当前到分
	 * @return
	 */
	public static String getCurHourMinute(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm") ;
		return sdf.format(new Date()) ;
	}
	/**
	 * 获取时间 yyyy-MM-dd HH:mm:ss 
	 * @return
	 */
	public static String getDateStr(Date time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		return sdf.format(new Date()) ;
	}
	/**
	 * 获取时间 yyyy-MM-dd HH:mm:ss 
	 * @return
	 */
	public static String getDateStr2(Date time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		return sdf.format(new Date()) ;
	}
	
	/**
	 * 获取时间 yyyy-MM-dd HH:mm:ss 
	 * @return
	 */
	public static String getDateStrFromDate(Date time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		return sdf.format(time) ;
	}
	/**
	 * 获取时间 毫秒值 
	 * @return
	 */
	public static long getDateTime(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime() ;
	}
	
	/**
	 * 获取最近六个月
	 * @return
	 */
	public static List<String> getSixMonthCur(){
		String dateString;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		List<String> rqList = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
		    dateString = sdf.format(cal.getTime());
		    rqList.add(dateString.substring(0, 7));
		    cal.add(Calendar.MONTH, -1);
		}
		//Collections.reverse(rqList);
		return rqList ;
	}
	
	/**
	 * 获取每天整点分钟
	 * @return
	 */
	public static List<String> getFiveMinute(){
		List<String> rqList = null ;
		try {
			String dateString;
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date date = sdf.parse("00:00") ;
			cal.setTime(date);
			rqList = new ArrayList<>();
			for (int i = 0; i < 24*12; i++) {
			    dateString = sdf.format(cal.getTime());
			    rqList.add(dateString);
			    cal.add(Calendar.MINUTE, +5);
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		
		return rqList ;
	}
	
	/**
	 * 判断当前时间是否为今天
	 * @param time
	 * @return
	 */
	public static boolean isToday(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      Date date = null;
		      try {
		          date = sdf.parse(time);
		      } catch (ParseException e) {
		    	  e.printStackTrace();
		      }
		     Calendar c1 = Calendar.getInstance();              
		     c1.setTime(date);                                 
		     int year1 = c1.get(Calendar.YEAR);
		     int month1 = c1.get(Calendar.MONTH)+1;
		     int day1 = c1.get(Calendar.DAY_OF_MONTH);     
		    Calendar c2 = Calendar.getInstance(); 
		    c2.setTime(new Date());
		     int year2 = c2.get(Calendar.YEAR);
		     int month2 = c2.get(Calendar.MONTH)+1;
		     int day2 = c2.get(Calendar.DAY_OF_MONTH);   
		    if(year1 == year2 && month1 == month2 && day1 == day2){
		         return true;
		     }
		     return false;   
	}
	/**
	 * 目标分钟点是否小于指定时间点
	 * @param curMinute
	 * @param targetMinute
	 * @return
	 */
	public static boolean compareMinute(String curMinute,String targetMinute){
		boolean flag = false ;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm") ;
			try {
				long time = sdf.parse(targetMinute).getTime() ;
				long time2 = sdf.parse(curMinute).getTime() ;
				flag = time<=time2 ;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag ;
	}
	/**
	 * 获取昨日
	 * @param type
	 * @return
	 */
	public static long getYesterDayTime(Integer type){
		String str="";
		if(type==0){
			str="yyyy-MM-dd 00:00:00" ;
		}else{
			str="yyyy-MM-dd 23:59:59" ;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(str) ;
		 Calendar   cal   =   Calendar.getInstance();
		 cal.add(Calendar.DATE,   -1);
		 String yesterday = sdf.format(cal.getTime());
		
		 long time =0 ;
		 try {
			time = getDateTime(yesterday) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return  time;
	}
	/**
	 * 获取昨日Date类型
	 * @return
	 */
	public static Date getYesterDayDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00") ;
		 Calendar   cal   =   Calendar.getInstance();
		 cal.add(Calendar.DATE,   -1);
		 String yesterday = sdf.format(cal.getTime());
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		 Date date = null ;
		 try {
			 date = format.parse(yesterday);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return date ;
	}
	/**
	 * 获取昨日str类型
	 * @return
	 */
	public static String getYesterDayStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00") ;
		 Calendar   cal   =   Calendar.getInstance();
		 cal.add(Calendar.DATE,   -1);
		 String yesterday = sdf.format(cal.getTime());
		 return yesterday ;
	}
	public static String getYesterDayStr2(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;
		 Calendar   cal   =   Calendar.getInstance();
		 cal.add(Calendar.DATE,   -1);
		 String yesterday = sdf.format(cal.getTime());
		 return yesterday ;
	}
	/**
	 * 获取最近三十天
	 * @return
	 */
	public static List<String> getLastThrityDays(){
	     List<String> list = new ArrayList<String>() ;
		 Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例  
		 ca.setTime(new Date()); // 设置时间为当前时间  
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		for(int i=0;i<30;i++){
			ca.add(Calendar.DATE, -1);// 日期减1  
			Date resultDate = ca.getTime(); // 结果  
			String re = sdf.format(resultDate);
			list.add(re) ;
		}
		Collections.reverse(list); 
		return list ;
	}
	
	/**
	 * 获取最近十二个月
	 * @return
	 */
	public static List<String> getLastTwelveMonths(){
		String dateString;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		List<String> rqList = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
		    dateString = sdf.format(cal.getTime());
		    rqList.add(dateString.substring(0, 7));
		    cal.add(Calendar.MONTH, -1);
		}
		Collections.reverse(rqList);
		return rqList ;
	}
	
	public static ArrayList<String> getPastDateIntervalDay(int intervals ) {  
	       ArrayList<String> pastDaysList = new ArrayList<>();  
	       for (int i = 0; i <intervals; i++) {  
	           pastDaysList.add(getPastDate(i));  
	       }  
	       Collections.reverse(pastDaysList);
	       return pastDaysList;  
	} 
	
	public static String getPastDate(int past) {  
	       Calendar calendar = Calendar.getInstance();  
	       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);  
	       Date today = calendar.getTime();  
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
	       String result = format.format(today);  
	       return result;  
	   }  
	
	/**
	 * 计算时间差(分钟)
	 */
	public static int getTimeHour(Date startTime , Date endTime){
		
		int time=0 ; ;
		
		if(startTime!=null && endTime!=null){
		   long cal = endTime.getTime()-startTime.getTime() ;
		   time= (int) ((cal/1000)/60);
		  
		}
		
		return time ;
	}
	
	/**
	 * 获取当月工作日
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getWorkDay(int year,int month){
		   if(year==0||month==0){
			   Calendar cal = Calendar.getInstance();
			   year = cal.get(Calendar.YEAR);
			   month = cal.get(Calendar.MONTH )+1;
		   }
		
	        int i=0 ;
	        Calendar cal = Calendar.getInstance();    
	        cal.set(Calendar.YEAR, year);    
	        cal.set(Calendar.MONTH,  month - 1);    
	        cal.set(Calendar.DATE, 1);    
	            
	            
	        while(cal.get(Calendar.YEAR) == year &&     
	                cal.get(Calendar.MONTH) < month){    
	            int day = cal.get(Calendar.DAY_OF_WEEK);    
	                
	            if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){    
	                i++ ;  
	            }    
	            cal.add(Calendar.DATE, 1);    
	        }    
	        return i;    
		    
	}
	
	
	public static String converteToDay(Object object,Integer type){
		String str = "";
		if(object!=null){
			SimpleDateFormat sdf = null ;
			if(type==1){
				sdf = new SimpleDateFormat("yyyy-MM-dd");  
			}else if(type==2){
				sdf = new SimpleDateFormat("yyyy年MM月dd日");
			}else if(type==4){
				sdf = new SimpleDateFormat("yyyy.MM.dd");
			}else{//3
				sdf = new SimpleDateFormat("yyyyMM");
			}
			Date  date =null ;
			if(object instanceof String){
				try {
					SimpleDateFormat tmepsdf = new SimpleDateFormat("yyyy-MM-dd");  
					date = tmepsdf.parse(object+"") ;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				date = (Date) object ;
			}
			
			str = sdf.format(date) ;	
		}
		 
		 return str ;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getWorkDay(0,0));
 	}
	
}
