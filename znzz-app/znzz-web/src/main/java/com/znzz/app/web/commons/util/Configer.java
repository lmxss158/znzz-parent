package com.znzz.app.web.commons.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 加载Ics-properties配置文件
 * @author wangqiang
 *
 */
public class Configer extends Properties{
	private static Configer instance;
	private static BufferedReader in;
	private static Log log = Logs.get() ;
	private static String icsPath = "base.properties" ;
	
	private Configer() {
		String path = this.getClass().getClassLoader()
				.getResource(icsPath).getPath();
		log.debug("读取配置文件路径 ::" +path);
		File file = new File(path);
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Configer getInstance() {
		if (instance == null) {
			synchronized (Configer.class) {
				if (instance == null) {
					instance = new Configer();
					try {
						instance.load(in);
					} catch (IOException e) {
						instance = null;
						e.printStackTrace();
					}
				}
			}
		}
		return instance;
	}
	
	public int getInt(String key){
		String value=instance.getProperty(key);
		if(value==null)
			return -1;
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}

	public static void main(String[] arg) {
		log.debug(Configer.getInstance().get("imageUploadPath"));
	}
}
