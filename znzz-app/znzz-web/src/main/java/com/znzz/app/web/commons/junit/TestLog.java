package com.znzz.app.web.commons.junit;

import org.apache.log4j.Logger;

public class TestLog {
	
	private static final Logger log = Logger.getLogger("scadaLogger");//写入scada日志文件
	private static final Logger log1 = Logger.getLogger(TestLog.class);//写入普通日志文件
	
	public static void main(String[] args) {  
			log.info("22222222222222222");  //写入scada日志文件
			log1.info("啊飒飒就是就是结束时间");//写入普通日志文件
	    }  
	  
}
