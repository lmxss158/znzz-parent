package com.znzz.app.instrument.modules.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.znzz.app.web.commons.util.GetConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;

import com.casic.iot.client.DeviceStateClient;
import com.casic.iot.client.IotAcquireClient;
import com.casic.iot.model.request.DeviceStateRequest;
import com.casic.iot.model.request.IotAcquireRequest;
import com.casic.iot.model.response.DeviceStateResponse;
import com.casic.iot.model.response.IotAcquireResponse;
import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.models.Ins_DeviceState;
import com.znzz.app.instrument.modules.models.Ins_PointList;
import com.znzz.app.instrument.modules.models.Ins_ScardDevice;
import com.znzz.app.instrument.modules.models.Ins_YunGateway;
import com.znzz.app.instrument.modules.service.InsDeviceService;
import com.znzz.app.instrument.modules.service.InsYunGatewayService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.YunDeviceBean;
import com.znzz.framework.base.service.BaseServiceImpl;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 云网管理实现类
 * @author chenzhongliang
 *
 */
@IocBean(args = { "refer:dao" })
public class InsYunGatewayServiceImpl extends BaseServiceImpl<Ins_YunGateway> implements InsYunGatewayService {
	@Inject 
	private ScadaDeviceServcie scadaService ;
	@Inject 
	private InsDeviceService insDeviceService ;
	@Inject
	private RedisService redisService;

	private static final Log LOG = Logs.get();
	public InsYunGatewayServiceImpl(Dao dao) {
		super(dao);
	}

	@Override
	public List<String> getCodeList(String code) {
		// 自定义sql
		Sql sql = Sqls.create("SELECT device_code FROM ins_yun_gateway WHERE device_code = '" + code + "'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("device_code"));
				}
				return list;
			}
		});

		return dao().execute(sql).getList(String.class);
	}

	@Override
	public List<String> getDeviceCodeList(String code) {
				// 自定义sql
				Sql sql = Sqls.create("SELECT deviceCode FROM ins_device_info WHERE deviceCode = '" + code + "'");
				// 设置回调函数
				sql = sql.setCallback(new SqlCallback() {

					@Override
					public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
						List<String> list = new ArrayList<>();
						while (rs.next()) {
							list.add(rs.getString("deviceCode"));
						}
						return list;
					}
				});

				return dao().execute(sql).getList(String.class);
	}

	@Override
	public Ins_DeviceInfo findDevicebyCode(String code) {
		// 自定义sql
		Sql sql = Sqls.create("SELECT a.deviceCode,a.deviceName,a.deviceVersion,b.name FROM ins_device_info a left join sys_unit b on a.borrowDepart=b.id WHERE a.deviceCode = '" + code + "'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Ins_DeviceInfo> list = new ArrayList<>();
				while (rs.next()) {
					Ins_DeviceInfo id = new Ins_DeviceInfo();
					id.setDeviceCode(rs.getString("deviceCode"));
					id.setDeviceName(rs.getString("deviceName"));
					id.setDeviceVersion(rs.getString("deviceVersion"));
					id.setBorrowDepart(rs.getString("name"));
					list.add(id);
				}
				return list;
			}
		});
		List<Ins_DeviceInfo> list = dao().execute(sql).getList(Ins_DeviceInfo.class);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public String insertOrUpdateFromJson(YunDeviceBean yun) {
		int size = yun.getEquipmentmasg().size();
		for(int i=0;i<size;i++){
			if(yun.getEquipmentmasg().get(i).getDevnum()==null || "".equals(yun.getEquipmentmasg().get(i).getDevnum())){
				return "json数据有误，请仔细检查是否存在设备编号为空";
			}
			
			Ins_YunGateway  iyg = new Ins_YunGateway();
			iyg.setCreateTime(new Date());
			iyg.setGateway_id(yun.getIotmsg().getIot());
			iyg.setAccesskey(yun.getIotmsg().getAccesskey());
			iyg.setGateway_name(yun.getIotmsg().getName());
			iyg.setDevice_code(yun.getEquipmentmasg().get(i).getDevnum());
			iyg.setDevice_id(yun.getEquipmentmasg().get(i).getEquipment());
			
			//判断统一编号是否存在在系统设备里
			List<String> codeList = getDeviceCodeList(iyg.getDevice_code());
			if(codeList.isEmpty() && codeList.size()==0){
	    		return "系统中不存在统一编号为"+iyg.getDevice_code()+"的设备"; 		//在设备表里没有该编号
	    	}
			//根据统一编号判断，修改或新增insertOrUpdate(iyg);
			List<String> yunList = getCodeList(iyg.getDevice_code());
			if (yunList != null && yunList.size() > 0) {
				//该编号在云网表里已存在,执行update
				Cnd cnd = Cnd.where("device_code", "=", yun.getEquipmentmasg().get(i).getDevnum());
				dao().update("ins_yun_gateway", Chain.make("createTime", iyg.getCreateTime()), cnd);
				dao().update("ins_yun_gateway", Chain.make("accesskey", iyg.getAccesskey()), cnd);
				dao().update("ins_yun_gateway", Chain.make("gateway_id", iyg.getGateway_id()), cnd);
				dao().update("ins_yun_gateway", Chain.make("gateway_name", iyg.getGateway_name()), cnd);
				dao().update("ins_yun_gateway", Chain.make("device_id", iyg.getDevice_id()), cnd);
				
				//dao().updateIgnoreNull(iyg);
			}else{
				//执行insert
				dao().insert(iyg);
			}
		}
		
		return "0";
	}
	
	@Override
	public void insertOrUpdate(Ins_YunGateway iyg){
		List<String> yunList = this.getCodeList(iyg.getDevice_code());
		if (yunList != null && yunList.size() > 0) {	//修改云网数据
			Sql sql = Sqls.create("UPDATE ins_yun_gateway SET gateway_id='"+iyg.getGateway_id()
			+"',gateway_name='"+iyg.getGateway_name()+"',accesskey='"+iyg.getAccesskey()
			+"',device_id='"+iyg.getDevice_id()
			+"' WHERE  device_code='"+iyg.getDevice_code()+"'");
			dao().execute(sql);
			
		}else{
			dao().insert(iyg);
		}
	}
	
	@Override
	public void uploadCloudbyRealTime() {
		//获取scada所有设备列表（直接从redis缓存获取，缓存没有的返回空）
		Map<String, Ins_ScardDevice> map = getMapFromRedis();
		//获取所有监控到的设备信息
		List<Ins_DeviceState> list = insDeviceService.getDeviceStateList();
		if(map!=null && map.size()>0){
			for(int i=0;i<list.size();i++){
				
				String deviceCode = list.get(i).getDeviceCode();
				Ins_ScardDevice scardDevice = map.get(deviceCode);
				Integer state = list.get(i).getState() ;	//离线状态
				//Integer powerState = list.get(i).getPowerState() ;	//开关机状态
				
				if(state==0){	//在线
					
					Ins_YunGateway yunGateway = getYunGatewayList(deviceCode);
					
					if(yunGateway!=null){
						String deviceid = yunGateway.getDevice_id();
						String accesskey = yunGateway.getAccesskey();
						String iot = yunGateway.getGateway_id();//网关id
						Double voltage = 0.0;
						Double current = 0.0;
						if(scardDevice!=null){
							List<Ins_PointList> pointList = scardDevice.getPointList();
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
							}
						}
						
						Double power = (voltage * current) / 1000;	//功率
						update2CloudbyRealTime(deviceid,accesskey,iot,voltage,current,power);
						
					}
					
				}
			
			}
		}
		
	}
	

	private Map<String, Ins_ScardDevice> getMapFromRedis() {
		Map<String, Ins_ScardDevice> scardMap = null;
		try {
			if (redisService.exists(Globals.SCARDDATA)) {
				String scardData = redisService.get(Globals.SCARDDATA);
				if (StringUtils.isNotBlank(scardData)) {
					scardMap = Json.fromJsonAsMap(Ins_ScardDevice.class, scardData);
				} 
			}
			
		} catch (JedisConnectionException e) {
			e.printStackTrace(); 
		}
		
		return scardMap;

	}

	private void update2CloudbyRealTime(String deviceid, String accesskey, String iot,Double voltage, Double current, Double power) {
		// 实时数据采集接口
		IotAcquireClient client = new IotAcquireClient(Configer.getInstance().getProperty("REALTIME_URL"), accesskey);
		// 创建IotAcquireRequest对象用来上传数据
		IotAcquireRequest request = new IotAcquireRequest();
		// 设置网关id
		request.setIot(iot);
		// 设置采集的设备id
		request.setEquipment(deviceid);
		// 时间戳
		long t = System.currentTimeMillis();
			
		request.addData("Voltage", voltage, t);
		request.addData("Current", current, t);
		request.addData("Power", power, t);
		IotAcquireResponse result = client.execute(request);
		if ((result != null) && (result.getStatus() != 200)){
			LOG.info("实时数据采集接口上传到云网 status : " + result.getStatus() + " message : " + result.getMsg() + " deviceid : " + deviceid);
		}

		
	}

	public Ins_YunGateway getYunGatewayList(String code) {
		// 自定义sql
		Sql sql = Sqls.create("SELECT * FROM ins_yun_gateway WHERE device_code = '" + code + "'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Ins_YunGateway> list = new ArrayList<>();
				while (rs.next()) {
					Ins_YunGateway yun = new Ins_YunGateway();
					yun.setAccesskey(rs.getString("accesskey"));
					yun.setDevice_code(rs.getString("device_code"));
					yun.setDevice_id(rs.getString("device_id"));
					yun.setGateway_id(rs.getString("gateway_id"));
					list.add(yun);
				}
				return list;
			}
		});
		List<Ins_YunGateway> list = dao().execute(sql).getList(Ins_YunGateway.class);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void uploadCloudbyState() {
		//获取所有监控到的设备信息（在线且开机状态的设备）
		List<Ins_DeviceState> list = insDeviceService.getBootDeviceStateList();
		if(list!=null && list.size()>0){
			Map<String,Map<String,Object>> iotInfoList = new HashMap<String,Map<String,Object>>();
			
			for(int i=0;i<list.size();i++){
				String deviceCode = list.get(i).getDeviceCode();
				
				Ins_YunGateway yunGateway = getYunGatewayList(deviceCode);
							
				if(yunGateway!=null){
					String deviceid = yunGateway.getDevice_id();
					String accesskey = yunGateway.getAccesskey();
					String iot = yunGateway.getGateway_id();//网关id
					//设备信息不完整时不作处理
					if((deviceid==null) || (accesskey==null) || (iot==null)){
						continue;
					}
					
					//判断是否存在既有网关
					if(iotInfoList.containsKey(accesskey)){
						//accesskey已存在
						Map<String, Object> iotInfo = iotInfoList.get(accesskey);
						List<String> deviceList = (List<String>)iotInfo.get("deviceList");
						deviceList.add(deviceid);
						
					}else{
						//accesskey不存在，创建网关
						Map<String, Object> iotInfo = new HashMap<String,Object>();
						iotInfo.put("iot", iot);
						List<String> deviceList = new ArrayList<String>();
						deviceList.add(deviceid);
						iotInfo.put("deviceList", deviceList);
						
						iotInfoList.put(accesskey, iotInfo);
					}
				}
							
			}



			//迭代网关列表，进行状态数据上传
			Iterator<String> it = iotInfoList.keySet().iterator();
			while(it.hasNext()){
				String accesskey = it.next();
				Map<String, Object> iotInfo = iotInfoList.get(accesskey);
				String iot = (String)iotInfo.get("iot");
				List<String> deviceList = (List<String>)iotInfo.get("deviceList");
				//设备状态采集接口
				DeviceStateClient client = new DeviceStateClient(Configer.getInstance().getProperty("STATE_URL"),accesskey);
				//DeviceStateRequest对象获取
				DeviceStateRequest req = new DeviceStateRequest();
				//网关id
				req.setIot(iot);
				
				long t = System.currentTimeMillis();
				for (String deviceid : deviceList) {
					req.addData(deviceid, 1000, t);
				}
				
				DeviceStateResponse result = client.execute(req);
				if ((result != null) && (result.getStatus() != 200)){
					LOG.info("状态数据采集接口上传到云网 status : " + result.getStatus() + " message : " + result.getMsg());
				}

			}




//			/*******************************自定义accessKey iot time 上传数据******start**********/
//			/*setDataToCloud();*/
//
//
//
//			/*******************************自定义accessKey iot time 上传数据******end**********/
//
		}
		String flag = Configer.getInstance().getProperty("uploadToCloud");
		if("true".equals(flag)){
			LOG.info("----------------------------状态数据采集接口上传到云网-配置--------start------------");
			//accesskey
			String myAccesskey = "KX36lZTHa96YLp7oV5KR0Q==";
			//iot
			String myIot = "10000048332530";
			//time
			long myTime = System.currentTimeMillis();
			//读取deviceId
			List<String> myDeviceIds = GetConfigUtil.getPropertiesValue("uploadToCloud.properties");
	
	
			//设备状态采集接口
			DeviceStateClient client2 = new DeviceStateClient(Configer.getInstance().getProperty("STATE_URL"),myAccesskey);
			//DeviceStateRequest对象获取
			DeviceStateRequest req2 = new DeviceStateRequest();
			//网关id
			req2.setIot(myIot);
	
			//遍历
			for (String myDeviceId : myDeviceIds) {
				req2.addData(myDeviceId,"1000",myTime);
			}
	
			DeviceStateResponse result2 = client2.execute(req2);
			LOG.info("----------------------------状态数据采集接口上传到云网-配置--------end------------");
		}

	}

	private void setDataToCloud() {
		boolean flag = (boolean) Configer.getInstance().get("uploadToCloud");
		if (flag){
			//accesskey
			String myAccesskey = "KX36lZTHa96YLp7oV5KR0Q==";
			//iot
			String myIot = "10000048332530";
			//time
			long myTime = System.currentTimeMillis();
			//读取deviceId
			List<String> myDeviceIds = GetConfigUtil.getPropertiesValue("uploadToCloud.properties");


			//设备状态采集接口
			DeviceStateClient client2 = new DeviceStateClient(Configer.getInstance().getProperty("STATE_URL"),myAccesskey);
			//DeviceStateRequest对象获取
			DeviceStateRequest req2 = new DeviceStateRequest();
			//网关id
			req2.setIot(myIot);

			//遍历
			for (String myDeviceId : myDeviceIds) {
				req2.addData(myDeviceId,"1000",myTime);
			}

			DeviceStateResponse result2 = client2.execute(req2);
		}
	}
	

/*	private void update2CloudbyState(String deviceid, String accesskey, String iot) {
		DeviceStateClient client = new DeviceStateClient(Configer.getInstance().getProperty("STATE_URL"),accesskey);
		DeviceStateRequest req = new DeviceStateRequest();
		// 网关id
		req.setIot(iot);
		// 时间戳
		long t = System.currentTimeMillis();
		List<String> list = getdeviceidList(iot);
		for (int i = 0; i < list.size(); i++) {
			req.addData(list.get(i), 1000, t);
		}
		req.addData(deviceid, 1000, t);
		DeviceStateResponse result = client.execute(req);
	}*/

	@Override
	public List<String> checkID(String code, String id) {
		// 自定义sql
		Sql sql = Sqls.create("SELECT device_code FROM ins_yun_gateway WHERE device_code = '" + code + "' and id!='"+id+"'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("device_code"));
				}
				return list;
			}
		});

		return dao().execute(sql).getList(String.class);
	}

	/* 
	 * 获取下拉菜单的统一编号
	 */
	@Override
	public NutMap assetCodeListSelect() {
		// sql语句
		String sql = "select a.* from ins_device_info a where deviceCode not in (select device_code from ins_yun_gateway)";
		// 创建sql
		Sql sqls = Sqls.queryRecord(sql);
		// 封装实体类
		sqls = sqls.setEntity(dao().getEntity(Ins_DeviceInfo.class));
		// 设置回调函数
		sqls.setCallback(Sqls.callback.entities());
		// 执行sql
		dao().execute(sqls);
		// 获取查询结果
		List<Ins_DeviceInfo> list = sqls.getList(Ins_DeviceInfo.class);
		// 封装到nutMap里
		NutMap re = new NutMap();
		re.addv("data", list);
		return re;
	}

	/* 
	 * 获取修改页面下拉框
	 */
	@Override
	public NutMap assetCodeListSelect2(String id) {
		// sql语句
		String sql = "select deviceCode from ins_device_info where deviceCode not in (select device_code from ins_yun_gateway where id!='"+id+"')";
		// 创建sql
		Sql sqls = Sqls.create(sql);
		// 封装实体类
		sqls = sqls.setEntity(dao().getEntity(Ins_Collect.class));
		// 设置回调函数
		sqls.setCallback(Sqls.callback.entities());
		// 执行sql
		dao().execute(sqls);
		// 获取查询结果
		List<Ins_Collect> list = sqls.getList(Ins_Collect.class);
		// 封装到nutMap里
		NutMap re = new NutMap();
		re.addv("data", list);
		return re;
	}
}
