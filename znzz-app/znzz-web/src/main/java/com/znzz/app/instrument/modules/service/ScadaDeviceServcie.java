package com.znzz.app.instrument.modules.service;

import java.util.List;
import java.util.Map;

import org.nutz.integration.jedis.RedisService;

import com.znzz.app.instrument.modules.models.Ins_ScardDevice;

public interface ScadaDeviceServcie {
	
	/**
	 * 从SCADA获取数据
	 * @return
	 */
	public List<Ins_ScardDevice> getDataFromScada(String cip);
	
	/**
	 * 将scard转换为map形式
	 * @param list
	 * @return
	 */
	Map<String ,Ins_ScardDevice> converToMap(List<Ins_ScardDevice> list) ;
	
	/**
	 * 获取scard数据另一种方式
	 * @param redis
	 * @return
	 */
	 Map<String ,Ins_ScardDevice> getScardMap(RedisService redis) ;
	 
	 /**
	  * 获取设备电流电压
	  * @param scardService
	  * @return
	  */
	 public Map<String,Object> getPowerNum(Ins_ScardDevice scardService);
	 
	 
	 
	 /**
	  * 请求scard获取数据 
	  * @param param
	  * @param method
	  * @return 返回 {"Result":"1"} 这种格式
	  */
	String getResult(String[] param, String method);
	
	/**
	 * scard返回数据原生格式
	 * @param param
	 * @param method
	 * @return
	 */
	public String invoke(Object[] param, String method) ;
}
