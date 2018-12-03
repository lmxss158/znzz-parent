package com.znzz.app.web.commons.quartz.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.znzz.app.instrument.modules.models.Ins_DeviceFacility;
import com.znzz.app.instrument.modules.models.Ins_SwitchingFlow;
import com.znzz.app.instrument.modules.service.InsCollectService;
import com.znzz.app.instrument.modules.service.InsDeviceFacilityService;
import com.znzz.app.instrument.modules.service.InsSwitchingFlowService;
import com.znzz.app.sys.modules.models.Sys_task;
@IocBean
public class InsDeviceFacilityJob implements Job{
	private static final Log log = Logs.get();
	@Inject
	private InsDeviceFacilityService facilityService ;
	@Inject
	private InsSwitchingFlowService switchflowService ;
	@Inject
	private InsCollectService collectService ;
	@Inject
	protected Dao dao;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~设备使用状况统计任务开始~~~~~~~~~~");
		
		//获取所有受监控的设备统一编码
		List<String> codeList = collectService.getMonitorDeviceCodeList() ;
		//获取当月日使用时长
		//获取月的flowList
		Map<String, Integer> countMap = facilityService.getCurnMonthList() ;
		//List<Ins_SwitchingFlow> flowList = switchflowService.findCurMonthList() ;
		//获取当月的facilityList
		List<Ins_DeviceFacility> deviceFacilityList = facilityService.findCurMonthList() ;
		
		//将deviceFacilityList 转为map 
		Map<String,Ins_DeviceFacility> dfMap = deviceFacilityMapConverter(deviceFacilityList) ;
		
		//将flowList 转为map
	//	Map<String ,Integer> countMap = switchFlowMapConverter(flowList) ;
		
		//将被监控的但没有使用的设备使用时长设置为0
		fillMinitorUseTime(countMap,codeList) ;
		
		//插入或更新
		facilityService.insertOrUpdate(countMap , dfMap) ;
		log.info("~~~~~~~~~~设备使用状况统计任务结束~~~~~~~~~~");
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));

	}

	
	/**
	 * 将状态履历中的设备当月使用时间进行统计,封装到map<deviceCode,count> 
	 * @param flowList
	 * @return
	 */
	
	private Map<String, Integer> switchFlowMapConverter(List<Ins_SwitchingFlow> flowList) {
		
		HashMap<String, Integer> map = new HashMap<String,Integer>() ;
		
		for(int i=0;i<flowList.size();i++){
			Integer temp =0 ;
			/**
			 *  (1)开机时间是当月 且关机或者离线的  tmep = userTime ;
     			(2)开机时间是当月之前的 , 且关机或者离线的 temp = 离线时间或关机时间 - 当月1号
     			(3)开机时间是当月 且处于开机状态的 temp= 当前时间 - 开机时间
     			(4)开机时间是当月之前的 , 且处于开始状态的  temp= 当前时间-当月1号
     				从countMap 中根据deviceCode 获取value 
        			(1)没有值,则直接countMap.put(deviceCode, temp)
					(2)有值 , 则countMap.put(deviceCode,value+temp);
			 */
			Ins_SwitchingFlow switchingFlow = flowList.get(i) ;
			Date powerOnTime = switchingFlow.getPowerOnTime() ;
			Date powerOffTime = switchingFlow.getPowerOffTime() ;
			Integer userTime = switchingFlow.getUserTime() ;
			String deviceCode = switchingFlow.getDeviceCode() ;
			//判断是否为当月
			if(isThisMonth(powerOnTime)){//当月
				if(userTime!=null){//关机或者离线
					temp = userTime ;
				}else{//开机状态
					temp = (int) ((new Date().getTime() - powerOnTime.getTime())/60000) ;
				}
			}else{//之前
				if(userTime!=null){//关机或离线
			        if(null == powerOffTime){//离线
			    	   powerOffTime = switchingFlow.getOutLineTime() ;
			        }
					temp = getTimeToFirstDay(powerOffTime) ;
				}else{
					temp = getTimeToFirstDay(new Date()) ;
				}
			}
			
			if(map.containsKey(deviceCode)){
				map.put(deviceCode, map.get(deviceCode)+temp) ;
			}else{
				map.put(deviceCode, temp);
			}
		}
		
		
		return map;
	}

	/**
	 * 将设备使用状况list转换为map<deviceCode,bean> 
	 * @param deviceFacilityList
	 * @return
	 */
	private Map<String, Ins_DeviceFacility> deviceFacilityMapConverter(List<Ins_DeviceFacility> deviceFacilityList) {
	 	Map<String, Ins_DeviceFacility> map = new HashMap<String ,Ins_DeviceFacility>() ;
		
		for(int i=0;i<deviceFacilityList.size();i++){
			Ins_DeviceFacility facility = deviceFacilityList.get(i) ;
			map.put(facility.getDeviceCode(), facility) ;
		}
		return map;
	}
	
	/**
	 * 将受监控的但未使用的设备使用时长设置为0
	 * @param map
	 * @param allMinitor
	 */
	public void fillMinitorUseTime(Map<String,Integer> map , List<String> allMinitor){
		
		for(String deviceCode :allMinitor){
			if(!map.containsKey(deviceCode)){
				map.put(deviceCode, 0) ;
			}
		}
	}
	
	
	
	//判断选择的日期是否是本月  
    public static boolean isThisMonth(Date time)  
    {  
         return isThisTime(time,"yyyy-MM");  
    }  
    private static boolean isThisTime(Date date,String pattern) {  
         SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
         String param = sdf.format(date);//参数时间  
         String now = sdf.format(new Date());//当前时间  
         if(param.equals(now)){  
           return true;  
         }  
         return false;  
    } 
	
    /**
     * 获取当前时间到月初一号的分钟差
     * @return
     */
    private int getTimeToFirstDay(Date time){
    	 Calendar cal = Calendar.getInstance();  
         cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
         cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH)); 
         return  (int) ((time.getTime() - cal.getTimeInMillis())/60000) ;
    }
    
    public static void main(String[] args) throws ParseException {
		
    	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
//    	Date date1 = f.parse("2017-08-25 14:21:00") ;
//    	 int temp = (int) ((new Date().getTime() - date1.getTime())/60000) ;
//    	 System.out.println(temp);
    	  Calendar cal = Calendar.getInstance();  
          cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
          cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH)); 
        String first = f.format(cal.getTime());
       // int tem =  (int) ((new Date().getTime() - cal.getTimeInMillis())/60000) ;
    	System.out.println(cal.getTime());
    	
	}
}
