package com.znzz.app.web.commons.quartz.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.znzz.app.instrument.modules.models.Ins_DevicePower;
import com.znzz.app.instrument.modules.models.Ins_PointList;
import com.znzz.app.instrument.modules.models.Ins_ScardDevice;
import com.znzz.app.instrument.modules.service.InsHomeService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.util.DateUtils;

/**
 * 计算设备功率定时
 * @author wangqiang
 *
 */
@IocBean
public class InsDevicePowerJob implements Job{
	private static final Log log = Logs.get();
	@Inject
	private InsHomeService homeService ;
	@Inject
	private SysUnitService sysUnitService ;
	@Inject 
	private ScadaDeviceServcie scardService ;
	@Inject
	public RedisService redisService;
	@Inject
	protected Dao dao;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~统计设备功率任务开始~~~~~~~~~~");
		//获取单位与受监控设备对应关系
		Map<String, List<String>> unitDeviceMap = homeService.getUnitDevice() ;
		//查询单位id与父id
		Map<String, String> upMap = sysUnitService.getUnitIdAndParentId() ;
		//将map转为顶级与子级关系
		Map<String, List<String>> tpMap = sysUnitService.getTopUnitGroup(upMap) ;
		//转换为顶级与设备列表关系
		Map<String, List<String>> tdMap = homeService.converteMap(tpMap, unitDeviceMap) ;
		//获取当前时间到分
		String hourMinute = DateUtils.getCurHourMinute() ;
		//获取所有设备列表
		
		Map<String ,Ins_ScardDevice> scardMap = scardService.getScardMap(redisService) ;
		//获取单位功率map
		Map<String, Ins_DevicePower> unitPowerMap = homeService.getTodayUnitPower("") ;
		
		ArrayList<Ins_DevicePower> insertList = new ArrayList<Ins_DevicePower>() ;
		ArrayList<Ins_DevicePower> updateList = new ArrayList<Ins_DevicePower>() ;
		//遍历tpMap
		Iterator<Entry<String, List<String>>> it = tdMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, List<String>> entry = it.next() ;
			//获取该顶级单位下的设备列表
			List<String> deviceList = entry.getValue() ;
			//顶级单位码
			String topUnitId = entry.getKey() ;
			Double powerNum = 0.0 ;
			for(int i=0;i<deviceList.size();i++){
				String deviceCode = deviceList.get(i) ;
				Ins_ScardDevice scardDevice = scardMap.get(deviceCode) ;
				if(scardDevice!=null){
					powerNum+=getPowerNum(scardDevice) ;
				}
			}
			
		   Ins_DevicePower ins_DevicePower = unitPowerMap.get(topUnitId) ;
		   Map<String, Double> data = null ;
		   if(ins_DevicePower==null){
			   ins_DevicePower = new Ins_DevicePower() ;
			   ins_DevicePower.setDate(new Date());
			   ins_DevicePower.setUnitId(topUnitId);
			   data = new HashMap<String,Double>() ;
		   }else{
			   data = Json.fromJsonAsMap(Double.class,ins_DevicePower.getHistoryData()) ;
		   }
		   
		   data.put(hourMinute, powerNum) ;
		   
		   ins_DevicePower.setHistoryData(Json.toJson(data));
		   
		   if(ins_DevicePower.getId()!=null){
			   updateList.add(ins_DevicePower) ;
		   }else{
			   insertList.add(ins_DevicePower) ;
		   }
		   
		}
		
		
		homeService.insertOrUpdateUnitPower(insertList,updateList);
		log.info("~~~~~~~~~~统计设备功率任务结束~~~~~~~~~~");
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));

	}
	
	
	
	//获取电流电压
	public Double getPowerNum(Ins_ScardDevice scardService){
		Double voltage = 0.0;
		Double current = 0.0;
		List<Ins_PointList> pointList = scardService.getPointList() ;
		for(int j=0;j<pointList.size();j++){
			Ins_PointList point = pointList.get(j);
			//电压
			if(point.getPoint().equalsIgnoreCase("Voltage")){
				voltage = Double.parseDouble(point.getValue()) ;
			}
			//电流
			if(point.getPoint().equalsIgnoreCase("Current")){
				current = Double.parseDouble(point.getValue()) ;
			}
			
			
		}
		
		return (voltage*current)/1000 ;
		
	}
	

}
