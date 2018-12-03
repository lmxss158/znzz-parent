package com.znzz.app.web.commons.quartz.job;

import java.text.SimpleDateFormat;
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
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityHistory;
import com.znzz.app.instrument.modules.service.InsHomeService;
import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.util.DateUtils;
/**
 * 统计单位数据每天设备历史记录
 * @author wangqiang
 *
 */
@IocBean
public class InsDeviceFacilityHistoryJob implements Job{
	private static final Log log = Logs.get();
	@Inject
	private InsHomeService homeService ;
	@Inject
	private SysUnitService sysUnitService ;
	@Inject
	protected Dao dao;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~统计设备监控台数与运行台数任务开始~~~~~~~~~~");
		//查询单位id与父id
		Map<String, String> upMap = sysUnitService.getUnitIdAndParentId() ;
		//将map转为顶级与子级关系
		//Map<String, List<String>> tpMap = sysUnitService.getTopUnitGroup(upMap) ;
		Map<String, List<String>> tpMap = sysUnitService.getUnitTreeGroup(upMap) ;
		//获取单位接入数(开机+关机)
		Map<String, Integer> ucMap = homeService.getUnitCollectNum() ;
		//获取单位开机数
		Map<String, Integer> poMap = homeService.getUnitPowerOnNum() ;
		//获取单位今日历史
		Map<String, Ins_DeviceFacilityHistory> uhmap = homeService.getTodayUnitHistory(null) ;
		//插入list
		List<Ins_DeviceFacilityHistory> insertList = new ArrayList<Ins_DeviceFacilityHistory>() ;
		//更新list
		List<Ins_DeviceFacilityHistory> updateList = new ArrayList<Ins_DeviceFacilityHistory>() ;
		
		String hour = DateUtils.getCurHour() ;
		
		//遍历tpMap
		Iterator<Entry<String, List<String>>> it = tpMap.entrySet().iterator();
		while(it.hasNext()){
			int totalUcNum =0 ;
			int totalPoNum =0 ;
			Entry<String, List<String>> entry = it.next() ;
			String unitId = entry.getKey() ;
			List<String> list = new ArrayList<String>() ;
			list.add(unitId) ;
			sysUnitService.getChildUnitList(tpMap, unitId, list);
			
			
			for(int i=0;i<list.size();i++){//遍历累加每级的个数
				String childId = list.get(i) ;
				Integer ucNum = isNull(ucMap.get(childId)) ;//接入数 
				Integer poNum = isNull(poMap.get(childId)) ;//开机数
				totalUcNum+=ucNum ;
				totalPoNum+=poNum ;
			}
			Ins_DeviceFacilityHistory history = null ;
			Map<String, String> map = null ;
			if(uhmap.containsKey(unitId)){//包含子级
				history = uhmap.get(unitId) ;
				String data = history.getHistoryData() ;
				map = Json.fromJsonAsMap(String.class, data) ;
			}else{
				history = new Ins_DeviceFacilityHistory() ;
				history.setDate(new Date());
				history.setUnitId(unitId);
				map = new HashMap<String,String>() ;
			}
			
			map.put(hour, totalPoNum+"/"+totalUcNum) ;
			history.setHistoryData(Json.toJson(map));
			
			if(null==history.getId()){
				insertList.add(history) ;
			}else{
				updateList.add(history) ;
			}
			
		}
		homeService.insertListHistory(insertList);
		homeService.updateListHistory(updateList); 
		log.info("~~~~~~~~~~统计设备监控台数与运行台数任务结束~~~~~~~~~~");
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));

	}
	
	
	public int isNull(Integer num){
		return num==null?0:num ;
	}
	
	
	
}
