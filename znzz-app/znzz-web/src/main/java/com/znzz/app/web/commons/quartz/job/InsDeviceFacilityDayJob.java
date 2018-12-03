package com.znzz.app.web.commons.quartz.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.znzz.app.instrument.modules.models.Ins_SwitchingFlow;
import com.znzz.app.instrument.modules.service.InsDeviceFacilityService;
import com.znzz.app.instrument.modules.service.InsSwitchingFlowService;
import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.web.commons.util.DateUtils;
/**
 * 按天统计设备使用时长
 * @author wangqiang
 *
 */
@IocBean
public class InsDeviceFacilityDayJob implements Job{
	
	private static final Log log = Logs.get();
	@Inject
	private InsDeviceFacilityService facilityService ;
	@Inject
	private InsSwitchingFlowService switchflowService ;
	@Inject
	protected Dao dao;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~日使用时长统计任务开始~~~~~~~~~~");
		List<Ins_SwitchingFlow> yesterDayList = switchflowService.findYesterDayList() ;
		//将yestaerDayList转换为 deviceCode,使用时长
		Map<String, Integer> flowMap = switchFlowMapConverter(yesterDayList) ;
		facilityService.insertDeviceFacilityDay(flowMap);
		log.info("~~~~~~~~~~日使用时长统计任务结束~~~~~~~~~~");
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));

	}
	
	
	/**
	 * 将 使用时长list转换为 deviceCode 和 时长
	 * @param list
	 * @return
	 */
	public Map<String,Integer> switchFlowMapConverter(List<Ins_SwitchingFlow> list){
		Map<String,Integer> map = new HashMap<String,Integer>() ;
		for(int i=0;i<list.size();i++){
			Ins_SwitchingFlow flow = list.get(i) ;
			
			Integer time = flow.getUserTime() ;
			String deviceCode = flow.getDeviceCode() ;
			//开机时间
			long powerOnTime = flow.getPowerOnTime().getTime() ;
			long startTime = DateUtils.getYesterDayTime(0) ;//昨日起始时间毫秒值
			long endTime = DateUtils.getYesterDayTime(1) ;//昨日终止时间毫秒值
			
			Date powerOffTime = flow.getPowerOffTime() ;
			Date outLineTime = flow.getOutLineTime() ;
			
			
			
			Integer useTime = 0 ;//分钟
			if(time==null){//开机
				if(powerOnTime<=startTime){//昨日之前开机
					useTime = 24*60 ;
				}else{//昨日之后开机
					useTime = (int) ((endTime - powerOnTime)/60000) ;
				}
			}else{//关机/离线
				if(powerOnTime<=startTime){//昨日之前开机
					long t = powerOffTime==null?outLineTime.getTime():powerOffTime.getTime();
					useTime = (int) ((t -startTime)/60000) ;
				}else{//昨日之后开机
					useTime = time ;
				}
			}
			
			if(map.containsKey(deviceCode)){
				Integer min = map.get(deviceCode) ;
				Integer remin =  min+useTime ;
				if(remin<=24*60){//防止错误数据
					map.put(deviceCode, remin) ;
				}
			}else{
				map.put(deviceCode, useTime) ;
			}
			
			
		}
		
		return map;
	}
	
	

	
}
