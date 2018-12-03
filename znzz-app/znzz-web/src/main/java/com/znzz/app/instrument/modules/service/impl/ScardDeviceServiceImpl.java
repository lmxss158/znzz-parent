package com.znzz.app.instrument.modules.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;

import com.znzz.app.instrument.modules.models.Ins_PointList;
import com.znzz.app.instrument.modules.models.Ins_ScardDevice;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.web.commons.base.Globals;

import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * cxf从scard获取数据
 * 
 * @author wangqiang
 *
 */
@IocBean
public class ScardDeviceServiceImpl implements ScadaDeviceServcie {
	private static final Logger logger = Logger.getLogger("scadaLogger");

	private static String wsdlURL;
	private static String namespaceUrl;
	private static org.apache.cxf.endpoint.Client client ;
	@Inject
	InsbcardService insbcardService;

	public ScardDeviceServiceImpl() {
		Properties prop = new Properties();
		try {
			prop.load(ScardDeviceServiceImpl.class.getClassLoader().getResourceAsStream("scard.properties"));
			wsdlURL = prop.getProperty("wsdlURL");
			namespaceUrl = prop.getProperty("namespaceUrl");
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			client = dcf.createClient(wsdlURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	@Override
	public List<Ins_ScardDevice> getDataFromScada(String cip) {
		List<Ins_ScardDevice> devices2 = null;
		try {
			String jsonString = invoke(new String[]{cip}, Globals.GETALLSTATEMESSAGE);
			
			if("0".equals(jsonString)||StringUtils.isBlank(jsonString)){//scard那边未连接
				devices2 = new ArrayList<Ins_ScardDevice>();
				logger.info("scada返回数据为空");
			}else{
				JSONObject jobj = new JSONObject(jsonString);
				jsonString = jobj.get("deviceList").toString();

				devices2 = Json.fromJsonAsList(Ins_ScardDevice.class, jsonString);
				logger.info("=======获取scada数据成功======"+jsonString);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("=======获取scada数据失败(设备状态数据)======"+e.getMessage());
		}

		return devices2;

	}

	/**
	 * 转map
	 * 
	 * @param list
	 * @return
	 */
	@Override
	public Map<String, Ins_ScardDevice> converToMap(List<Ins_ScardDevice> list) {
		Map map = new HashMap<>();

		// 获取生产编号与设备统一编号对应关系
		Map<String, String> orginMap = insbcardService.getOrignCodeAndDeviceCode();
		if (list != null) {
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Ins_ScardDevice device = (Ins_ScardDevice) iterator.next();
				String deviceId = device.getDeviceId();// 生产编号
				if (orginMap.containsKey(deviceId)) {// 生产编号有对应的设备编号
					map.put(orginMap.get(deviceId), device);
				}
			}
		}

		return map;
	}

	public static void main(String[] args) {
		ScardDeviceServiceImpl impl = new ScardDeviceServiceImpl();

		impl.getDataFromScada("192.168.0.228");
	}

	/**
	 * 获取scard数据另一种方式
	 * 
	 * @param redis
	 * @return
	 */
	@Override
	public Map<String, Ins_ScardDevice> getScardMap(RedisService redisService) {
		Map<String, Ins_ScardDevice> scardMap = null;
		try {
			if (redisService.exists(Globals.SCARDDATA)) {
				String scardData = redisService.get(Globals.SCARDDATA);
				if (StringUtils.isNotBlank(scardData)) {
					scardMap = Json.fromJsonAsMap(Ins_ScardDevice.class, scardData);
				}
			}
			if (scardMap == null) {
				scardMap = converToMap(getDataFromScada(""));
				redisService.set(Globals.SCARDDATA, Json.toJson(scardMap));
			}
		} catch (JedisConnectionException e) {
			scardMap = converToMap(getDataFromScada(""));
			e.printStackTrace(); 
		}
		

		return scardMap;
	}

	/**
	 * 从获取该设备的电流电压功率
	 * 
	 * @param scardService
	 * @return
	 */
	public Map<String, Object> getPowerNum(Ins_ScardDevice scardService) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		Double voltage = 0.0;
		Double current = 0.0;
		Integer powerState = 0;
		Integer state = 0;
		String ip = "" ;
		if (scardService != null) {
			List<Ins_PointList> pointList = scardService.getPointList();
			String ip2 = scardService.getIp() ;
			if(StringUtils.isNotBlank(ip2)){
				ip = ip2 ;
			}
			for (int j = 0; j < pointList.size(); j++) {
				Ins_PointList point = pointList.get(j);
				// 电压
				if (point.getPoint().equalsIgnoreCase("Voltage")) {
					voltage = Double.parseDouble(point.getValue());
				}
				// 电流
				if (point.getPoint().equalsIgnoreCase("Current")) {
					current = Double.parseDouble(point.getValue()) / 1000;
				}

				if (point.getPoint().equalsIgnoreCase("State")) {
					powerState = Integer.parseInt(point.getValue());
				}

			}
		} else {// 离线
			state = 1;
		}

		map.put("state", state);
		map.put("powerState", powerState);
		map.put("voltage", voltage);
		map.put("current", current);
		map.put("power", (voltage * current) / 1000);
        map.put("ip", ip) ;
		return map;

	}
	// 参数为IP或编号 要调用的接口方法
	// 返回结果为 1 或 0
	@Override
	public String getResult(String[] param, String method) {
		String result = null;
		try {

			String jsonString = invoke(param, method) ;
			// 对传入的数据进行验证，看是否为控
			if ("[]".equals(jsonString)) {
				logger.info("scada传入数据为空，请检查scada传入的数据");
				return null;
			}
			Map<String, String> resultMap = Json.fromJsonAsMap(String.class, jsonString);

			for (String key : resultMap.keySet()) {
				result = resultMap.get(key);
			}

			logger.info("=======获取scada数据成功======"+jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("=======获取scada数据失败======"+e.getMessage());
		}
		return result;

	}

	/**
	 * 请求scard
	 * 
	 * @param method
	 * @param param
	 * @return
	 */
	public String invoke(Object[] param, String method) {
		String str = "";
		try {
			
			QName qName = new QName(namespaceUrl, method);
			Object[] res = client.invoke(qName, param);
			str = (String) res[0];

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
