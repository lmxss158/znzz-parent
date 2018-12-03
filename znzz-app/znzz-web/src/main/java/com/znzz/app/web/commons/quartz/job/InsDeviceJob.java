package com.znzz.app.web.commons.quartz.job;

import java.util.*;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.instrument.modules.service.InsGatewayService;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.znzz.app.instrument.modules.models.Ins_DeviceState;
import com.znzz.app.instrument.modules.models.Ins_PointList;
import com.znzz.app.instrument.modules.models.Ins_ScardDevice;
import com.znzz.app.instrument.modules.models.Ins_SwitchingFlow;
import com.znzz.app.instrument.modules.service.InsDeviceService;
import com.znzz.app.instrument.modules.service.InsSwitchingFlowService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.util.DateUtils;

/**
 * 设备定时任务 :
 *   webservice 从scard 获取设备列表数据
 * @author wangqiang
 *
 */
@IocBean
@DisallowConcurrentExecution
public class InsDeviceJob implements Job{
	
	 private static final Log log = Logs.get();
	 
	@Inject private ScadaDeviceServcie scardService ;
	@Inject private InsDeviceService insDeviceService ;
	@Inject private InsSwitchingFlowService insSwitchFlowService ;
	@Inject private InsAssetsService insAssetsService ;
	@Inject private InsGatewayService gatewayService ;
	@Inject	public RedisService redisService;
	@Inject	protected Dao dao;

	/**
	 * 定时任务 执行入口
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~更新设备监控状态任务开始~~~~~~~~~~");
		//获取所有设备列表
		Map map = scardService.converToMap(scardService.getDataFromScada("")) ;
		redisService.set(Globals.SCARDDATA, Json.toJson(map)) ;
		controle(map);
		log.info("~~~~~~~~~~更新设备监控状态任务结束~~~~~~~~~~");
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));

	}
    
	
	
	/**
	 * 定时任务 处理逻辑
	 * @param scardMap
	 */
	public void controle(Map<String ,Ins_ScardDevice> scardMap){
		
		try {
			//获取所有已监测设备的上次状态
			List<Ins_DeviceState> deviceStateList = insDeviceService.getDeviceStateList() ;
			StringBuilder deviceCodeBuild = new StringBuilder();//存统一编号
			Map<String,String> ipMap = new HashMap<String,String>();//存ip

			for(int i=0;i<deviceStateList.size();i++){
				
				Ins_DeviceState ins_DeviceState = deviceStateList.get(i) ;
				String deviceCode = ins_DeviceState.getDeviceCode() ;
				Ins_ScardDevice scardDevice = scardMap.get(deviceCode) ;
				Integer state = ins_DeviceState.getState() ;
				Integer powerState = ins_DeviceState.getPowerState() ;
				String ip="" ;
				Integer powerStateFrScard = null ;
				if(scardDevice!=null){
					deviceCodeBuild.append("'").append(deviceCode).append("'").append(",");
					ip = scardDevice.getIp() ;
					ipMap.put(deviceCode,ip) ;
					powerStateFrScard =  getPowerStateFrScard(scardDevice) ;
				}
				
				boolean flag = false ;
				
				if(scardDevice==null){//离线
					if(state !=null){//有数据
						if(state==0){//原在线
							//原开机(1)设置离线时间 计算使用时长 设置位置  (2)状态表更新为离线  开关机设置为null  位置设置为null
							if(powerState==0){
								setSwitchFlowEndTime(deviceCode,ip,0);
							}
							//关机流水表不操作
							setDeviceState(ins_DeviceState,1,null,null) ;
							flag = true ;
						}
					}else{//没有数据
						
						setDeviceState(ins_DeviceState,1,null,null) ;
						flag = true ;
					}
					
					
				}else{//在线
					
					if(state==null){//原没有数据
						setDeviceState(ins_DeviceState,0,powerStateFrScard,ip) ;
						flag = true ;
						
						if(powerStateFrScard==0){//scard开机 流水表 插入设置开机时间 位置
							addSwitchFlow(deviceCode,ip);
						}
						
					}else{//有数据
						//上次在线
						/**
						 *    开开   不变更
	                                                                            开关   (1)状态表更新开关状态 位置
				                (2)流水表设置关机时间 计算使用时长
				                                         关开   (1)装填表更新开关状态 位置
	                               (2)流水表 插入 开机时间 位置
				       		  关关   不变更
						 */
						if(state==0){
							//开开 如果位置变更,则变更
							if(powerState==0&&powerStateFrScard == 0){
								String oldIp = ins_DeviceState.getLocationInfo();
								if(StringUtils.isNotBlank(ip)&&!(ip.equals(oldIp))){//新旧ip不一致
									setSwitchFlowEndTime(deviceCode,oldIp,1);//将旧的ip履历关机
									addSwitchFlow(deviceCode,ip) ;//将新的ip履历开机
									//更新状态表中的ip
									ins_DeviceState.setLocationInfo(ip);
									flag = true ;
								}
							}else if(powerState==0 && powerStateFrScard ==1){//开关
								//状态表更新开关状态和位置
								setDeviceState(ins_DeviceState,state,powerStateFrScard,ip);
								flag = true ;
								//流水表 设置关机时间,计算使用时长
								setSwitchFlowEndTime(deviceCode,ip,1);
							}else if(powerState==1 && powerStateFrScard ==0){//关开
								//状态表更新开关状态和位置
								setDeviceState(ins_DeviceState,state,powerStateFrScard,ip);
								flag = true ;
								//流水表 插入 开机时间 位置
								addSwitchFlow(deviceCode,ip) ;
							}
							//关关 不变更
							
							
						}else{//上次离线
							//(1)状态表 更新为在线  开关机状态 位置
							setDeviceState(ins_DeviceState,0,powerStateFrScard,ip) ;
							flag = true ;
		                    // (2)流水表 插入 开机时间 位置
							if(powerStateFrScard==0){//scard 传来开机状态才插入
								addSwitchFlow(deviceCode,ip) ;
							}
						}
					}
				}
				if(flag){
					insDeviceService.updateDeviceState(ins_DeviceState);
				}
			}
			//变更受监控资产表中的位置信息
			if(StringUtils.isNotBlank(deviceCodeBuild)){
				//根据统一编号查出资产表实体bean列表
				List<Ins_Assets> assetBeanList = insAssetsService.getInsAssetBeanList(StringUtils.strip(deviceCodeBuild.toString(), ","));
				Map<String, String> ipAndLocation = gatewayService.getIpAndLocation();
				for(int in =0 ;in<assetBeanList.size();in++){
					Ins_Assets assets = assetBeanList.get(in);
					String locationInfoOld = assets.getLocationInfo();//原地址
					String assetCode = assets.getAssetCode();
					String ip = ipMap.get(assetCode);//新地址
					String locationInfoNew = ipAndLocation.get(ip);
					if(StringUtils.isBlank(locationInfoNew)){//收集器管理中没有录入位置
						locationInfoNew = ip ;//将ip赋值
					}
					if(StringUtils.isNotBlank(locationInfoNew)&&!(locationInfoNew.equals(locationInfoOld))){//新位置和旧位置不同时才更新
						insAssetsService.updateInsAssetBeanLocationInfo(assetCode,locationInfoNew) ;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	/**
	 * 流水表设置离线时间及使用时长
	 */
	public void setSwitchFlowEndTime(String deviceCode,String ip,int type){
		
		List<Ins_SwitchingFlow> list = insSwitchFlowService.fetchSwitchFlow(deviceCode) ;
		int size = list.size() ;
		if(size>0){
				if(size>1){//有错误数据
					insSwitchFlowService.clearRepeateFlowData(deviceCode) ;
				}
				Ins_SwitchingFlow switchFlow = list.get(0) ;
				if(switchFlow!=null){
					
					int timehm = DateUtils.getTimeHour(switchFlow.getPowerOnTime(),new Date()) ;
					Date date = new Date() ;
					if(timehm<0){//错误时间数据(两个定时同时跑,频繁来回设置开机关机,服务器时间却又不一致)
						timehm = 0 ;
						date = switchFlow.getPowerOnTime() ;
					}
					
					if(type==0){
						switchFlow.setOutLineTime(date);
					}else{//关机 设置位置,离线不设置位置保留上次的位置
						switchFlow.setPowerOffTime(date);
						String fetchLocationInfo = insSwitchFlowService.fetchLocationInfo(ip) ;
						if(StringUtils.isBlank(fetchLocationInfo)){//如果收集器中没有录入,则展示ip
							fetchLocationInfo=ip ;
						}
						switchFlow.setLocationInfo(fetchLocationInfo);
					}
					
					switchFlow.setUserTime(timehm);
					insSwitchFlowService.updateSwitchFlow(switchFlow);//更新
				}
				
			
		}
		
	}
	/**
	 *流水表添加 
	 */
	public void addSwitchFlow(String deviceCode,String ip){
		List<Ins_SwitchingFlow> list = insSwitchFlowService.fetchSwitchFlow(deviceCode) ;
		if(list.size()==0){//防止数据错乱,履历表中没有开机记录时才插入最新的开机数据
			Ins_SwitchingFlow switchingFlow = new Ins_SwitchingFlow();
			switchingFlow.setDeviceCode(deviceCode);
			switchingFlow.setPowerOnTime(new Date());
			String fetchLocationInfo = insSwitchFlowService.fetchLocationInfo(ip) ;
			if(StringUtils.isBlank(fetchLocationInfo)){
				fetchLocationInfo=ip ;
			}
			switchingFlow.setLocationInfo(fetchLocationInfo);
			insSwitchFlowService.insertSwitchFlow(switchingFlow);
		}
	}
	
	
	/**
	 * 状态表设置状态
	 */
	public void setDeviceState(Ins_DeviceState ins_DeviceState,Integer state,Integer powerState,String ip){
		
		ins_DeviceState.setState(state);
		ins_DeviceState.setPowerState(powerState);
		if(StringUtils.isNotBlank(ip)){//不将ip设置为null
			ins_DeviceState.setLocationInfo(ip);
		}

	}
	
	
	
	
	
	
	/**
	 * 获取开关机状态
	 */
	public Integer getPowerStateFrScard(Ins_ScardDevice scardDevice){
		Integer powerstate = 0;
		List<Ins_PointList> pointList = scardDevice.getPointList() ;
		for(int j=0;j<pointList.size();j++){
			Ins_PointList point = pointList.get(j);
			if(point.getPoint().equalsIgnoreCase("State")){
				powerstate = Integer.parseInt(point.getValue()) ;
				break ;
			}
		}
		
		return powerstate ;
	}
	
	

}
