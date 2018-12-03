package com.znzz.app.instrument.modules.service.impl;

import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.znzz.app.instrument.modules.service.InsCollectService;
import com.znzz.app.instrument.modules.service.InsDeviceFacilityService;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.instrument.modules.service.OffLineNoticeService;

@WebService(endpointInterface = "com.znzz.app.instrument.modules.service.OffLineNoticeService",serviceName = "OffLineNoticeService",targetNamespace="http://service.modules.instrument.app.znzz.com/")
@IocBean(name = "offLineNoticeService")
public class OffLineNoticeServiceImpl implements OffLineNoticeService {
	public static Log log = null ;
	@Inject
	InsCollectService insCollectService ;
	@Inject
	InsDeviceFacilityService inFacilityService ;
	@Inject
	InsbcardService insbcardService ;
	
	public OffLineNoticeServiceImpl() {
		log= Logs.getLog(this.getClass()) ;
	}
	
	
	@Override
	public String OffLineMessageNotice(String param) {
		JSONObject obj = new JSONObject() ;
		try {
			if(StringUtils.isNotBlank(param)){
				log.info("scard传入参数::"+param);
				JSONObject json = new JSONObject(param) ;
				//生产编号
				String orignCode = json.getString("deviceid") ;
				//获取生产编号与设备统一编号对应关系
				Map<String, String> map = insbcardService.getOrignCodeAndDeviceCode() ;
				String deviceCode = map.get(orignCode) ;
				//boolean flag = insCollectService.isBinding(deviceCode) ;
				if(deviceCode!=null){//有关联说明已被监控
					JSONArray pointList = json.getJSONArray("pointList") ;
					for(int i=0;i<pointList.length();i++){
						JSONObject pointObj = pointList.getJSONObject(i) ;
						String dateStr = pointObj.getString("date") ;
						int hour = pointObj.getInt("time") ;
						inFacilityService.updateDeviceFacilityOffLineTime(deviceCode, dateStr, hour*60);
					}
					
					
					obj.put("Result", 1) ;	
				}else{
					obj.put("Result", 0) ;
				}
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("Result", 0) ;
		}
		return obj.toString();
	}



	

}
