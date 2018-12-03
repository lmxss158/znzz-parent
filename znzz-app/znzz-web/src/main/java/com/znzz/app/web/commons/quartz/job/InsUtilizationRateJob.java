package com.znzz.app.web.commons.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.znzz.app.asset.moudules.services.InsAssetUnitService;
import com.znzz.app.instrument.modules.models.Ins_SwitchingFlow;
import com.znzz.app.instrument.modules.service.InsSwitchingFlowService;

/**
 * 使用率统计定时任务
 * @author wangqiang
 *
 */
@IocBean
public class InsUtilizationRateJob implements Job{

	private static final Log log = Logs.get();
	@Inject
	private InsSwitchingFlowService switchflowService ;
	@Inject
	private InsAssetUnitService  assetUnitService ;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~设备利用率统计任务开始~~~~~~~~~~");
		//近三十天履历中的设备
		List<Ins_SwitchingFlow> SFlist = switchflowService.findCurThrityDaysList() ;
		Map<String, Integer> map = converteMapCount(SFlist) ;
		assetUnitService.UpdateAssetUnitRate(map);
		log.info("~~~~~~~~~~设备利用率统计任务结束~~~~~~~~~~");
	}
	
	/**
	 * 装成map 统计时长
	 * @param SFlist
	 * @return
	 */
	public Map<String,Integer> converteMapCount(List<Ins_SwitchingFlow> SFlist){
		Map<String ,Integer> map = new HashMap<String,Integer>() ;
		/**
		 *  开机在 time之前  且 关机或离线的  temp = 关机/离线时间 - time
	                   	且 开机的        temp = 当前时间 - time
	  		开机在 time之后  且 关机或离线的  temp = useTime
	                   	且 开机的        temp = 当前时间 - 开机时间
		 */
		//获取三十天前的毫秒值
		long thrityMillis = getThrityDay() ;
		
		
		for(int i=0;i<SFlist.size();i++){
			int temp ;
			Ins_SwitchingFlow switchingFlow = SFlist.get(i) ;
			long powerOnMillis = switchingFlow.getPowerOnTime().getTime() ;
			if(powerOnMillis<thrityMillis){//开机时间在 三十天之前 之前
				
				if(null!=switchingFlow.getUserTime()){//关机或离线
			       long count =0 ;
			       if(null == switchingFlow.getPowerOffTime()){//离线
			    	   count = switchingFlow.getOutLineTime().getTime() ;
			       }else{//关机
			    	   count = (int) ((switchingFlow.getPowerOffTime().getTime())/60000) ;
			       }
			       
			       temp = (int) ((count - thrityMillis)/60000) ;
			       
				}else{//开机
					temp = (int) ((new Date().getTime() - thrityMillis)/60000) ;
				}
				
			}else{//开机在三十天之前  之后
				if(null!=switchingFlow.getUserTime()){//关机或离线
					temp = switchingFlow.getUserTime() ;
				}else{//开机
					temp = (int) ((new Date().getTime() - switchingFlow.getPowerOnTime().getTime())/60000) ;
				}
				
			}
			
			if(map.containsKey(switchingFlow.getDeviceCode())){
				Integer n = map.get(switchingFlow.getDeviceCode());
				map.put(switchingFlow.getDeviceCode(),temp+n);
			}else{
				map.put(switchingFlow.getDeviceCode(),temp);
			}
			
			
		}
		return map ;
	}
	
	
	public long getThrityDay(){
		//获取当前日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(today);
		theCa.add(theCa.DATE, -30);//最后一个数字30可改，30天的意思
		long millis = theCa.getTimeInMillis() ;
		return millis ;
	}

}
