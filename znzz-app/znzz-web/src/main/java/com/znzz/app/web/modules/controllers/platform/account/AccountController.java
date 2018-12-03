package com.znzz.app.web.modules.controllers.platform.account;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.ViewModel;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.instrument.modules.models.Ins_ScardDevice;
import com.znzz.app.instrument.modules.service.InsDeviceFacilityService;
import com.znzz.app.instrument.modules.service.InsDeviceInfoService;
import com.znzz.app.instrument.modules.service.InsGatewayService;
import com.znzz.app.instrument.modules.service.InsHomeService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.util.DateUtils;

@IocBean
@At("/platform/account")
public class AccountController {
	
	 @Inject
	    private InsHomeService homeService ;
	    @Inject
	    private SysUnitService unitService ;
	    @Inject
	    private RedisService redisService ;
	    @Inject 
		private ScadaDeviceServcie scardService ;
	    @Inject
	    private InsDeviceFacilityService facilityService ;
	    @Inject
	    private InsDeviceInfoService deviceinfoServcie ;
	    @Inject
	    private InsGatewayService gatewayService ;
	// 综合监控
	@At("")
	@Ok("beetl:/platform/account/index.html")
	@RequiresAuthentication
	public Object index() {
		 //Map<String, String> map2 = homeService.getDeviceFacilityHourList() ;
        Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
        String topUnitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
        
        Map<String, List<String>> unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
        ArrayList<String> list = new ArrayList<String>() ;
        list.add(topUnitId) ;
        unitService.getChildUnitList(unitTreeGroup, topUnitId, list);
        //顶级下所有单位ids
        String allUnitds = StringUtils.strip(Json.toJson(list),"[]") ;
        
        Map map = homeService.getHomeAllCount(allUnitds) ;
        
        //24小时
        List<String> fiveDays = DateUtils.getPastDateIntervalDay(5) ;//获取过去五天日期
        List<JSONObject> lineList = new ArrayList<JSONObject>() ;
        for(String day : fiveDays){
        	JSONObject historyoObj = homeService.getHomeUnitHistory(topUnitId, day) ;
        	lineList.add(historyoObj) ;
        }
        
        
        //六个月数据
        List<Map<String, String>> useTimeNumMap = homeService.getUseTimeNum(allUnitds) ;
        //受监控数量
        Map<String, String> unitDeviceNumMap = homeService.getUnitDeviceNum(allUnitds) ;
        
        //功率
        Map<String, String> unitPowerMap = homeService.getUnitPower(topUnitId, DateUtils.getDateStr(new Date())) ;
        
        map.putAll(unitDeviceNumMap);
        map.put("monthData",useTimeNumMap);
        map.put("lineList",  lineList) ;
        map.put("fiveDays", Json.toJson(fiveDays)) ;
        map.putAll(unitPowerMap); 
        return map ;
	}
	
	

	// 监控页跳转
	@At("/monitor")
	@Ok("beetl:/platform/account/monitor.html")
	@RequiresAuthentication
	public void monitor() {
	}
	/**
     * 获取统计实时数据
     * @return
     */
    @At("/powerData")
    @Ok("json:full")
    @RequiresAuthentication
    public Object getPowerData(){
    	Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
        String topUnitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
        Map<String, String> unitPowerMap = homeService.getUnitPower(topUnitId, DateUtils.getDateStr(new Date())) ;
        return unitPowerMap ;
    }
    /**
     * 综合监控二级页面需要数据
     * @param unitId
     * @return
     */
    @At("/getPageData")
    @Ok("json:full")
    @RequiresAuthentication
    public Object getPageData(@Param("unitId") String unitId,@Param("assettype") String  assettype,@Param("deviceState") String deviceState,@Param("pageNo") int pageNo){
    		
    	  
    	 Map<String, Object> pageDataMap = new HashMap<String,Object>() ;
    	
    	 Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
         Map<String, List<String>> unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
         ArrayList<String> list = new ArrayList<String>() ;

         if(StringUtils.isBlank(unitId)){
        	  Subject subject = SecurityUtils.getSubject();
             Sys_user user = (Sys_user) subject.getPrincipal();
             unitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
         }
         
         list.add(unitId) ;
         unitService.getChildUnitList(unitTreeGroup, unitId, list);
         //该单位下级所有单位ids
         String allUnitds = StringUtils.strip(Json.toJson(list),"[]") ;
    	//受监控数量
        Map<String, String> unitDeviceNumMap = homeService.getUnitDeviceNum(allUnitds) ;
        //单位开机数,关机数,离线数
        Map unitDeviceStateNum = homeService.getHomeAllCount(allUnitds) ;
        //单位24小时数量
        JSONObject historyMap = homeService.getHomeUnitHistory(unitId, DateUtils.getDateStr(new Date())) ;
        //该单位下的所有设备列表
        int pageSize =21;
        NutMap map =homeService.getUnitDeviceList(allUnitds,assettype,deviceState,pageNo, pageSize) ;
        List<Map> deviceList =(List<Map>) map.get("data");
        int total=map.getInt("recordsFiltered");
        int  totalPage = (total + pageSize -1) / pageSize;
        //获取该单位下的所有单位的使用时长
        Map<String, String> deviceDurationMap = facilityService.findUnitDeviceUseTimeListByMonth(DateUtils.getDateStr(new Date()), allUnitds) ;
		//获取scard数据
        Map<String ,Ins_ScardDevice> scardMap = scardService.getScardMap(redisService) ;
        
        
        List<Map<String, Object>> compositeDeviceData = CompositeDeviceData(scardMap,deviceList,deviceDurationMap,deviceState) ;
        
        
        pageDataMap.putAll(unitDeviceNumMap);
        pageDataMap.putAll(unitDeviceStateNum);
        pageDataMap.put("lineListByUnit",historyMap.toString());
        pageDataMap.put("dataList", compositeDeviceData) ;
        pageDataMap.put("totalPage", totalPage) ;
        pageDataMap.put("currentNum", deviceList.size()) ;
        return pageDataMap ;
    }
    
   
	
	/**
     * 综合监控三级页面
     * @param
     * @return
     */
    @At("/getDataDetail")
    @Ok("beetl:/platform/account/detail.html")
    @RequiresAuthentication
    public Object getDataDetail(@Param("deviceCode") String deviceCode){
    	Map<String, Object> facilityDayMap = facilityService.getDeviceFacilityDayLastThirtyDays(deviceCode) ;
     	Map<String, Object> deviceFacilityYear = facilityService.getDeviceFacilityYear(deviceCode) ;
    	facilityDayMap.putAll(deviceFacilityYear); 
    	//最近一个月
    	List<JSONObject> listMonth = facilityService.getDeviceFacilityUseTimeOfProject(deviceCode, 1) ;
    	//最近一年
    	List<JSONObject> listYear = facilityService.getDeviceFacilityUseTimeOfProject(deviceCode, 2) ;
    	facilityDayMap.put("listMonth", listMonth) ;
    	facilityDayMap.put("listYear", listYear) ;
    	facilityDayMap.put("deviceCode", deviceCode) ;
    	
    	return facilityDayMap ;
    	
    }
    /**
     * 组合设备电压电流 功率使用时长 开关机状态 离线在线
     * @param scardMap
     * @param deviceList
     * @param deviceDurationMap
     * @return
     */
    public List<Map<String,Object>>  CompositeDeviceData(Map<String ,Ins_ScardDevice> scardMap , List<Map> deviceList,Map<String, String> deviceDurationMap,String deviceState){
    	List<Map<String,Object>> list = new ArrayList<Map<String, Object>>() ;
    	Map<String, String> ipAndLocation = gatewayService.getIpAndLocation() ;
    	for(Map deviceMap :deviceList){
    		Map<String,Object> tempMap = new HashMap<String,Object>() ;
    		String deviceCode =  (String) deviceMap.get("deviceCode") ;
    		String duration = deviceDurationMap.get(deviceCode) ;
    		if(StringUtils.isBlank(duration)){
    			duration="0" ;
    		}
    		Map<String, Object> pcvMap = scardService.getPowerNum(scardMap.get(deviceCode)) ;
    		
    		int currentState =(int) pcvMap.get("state");//0:在线 1:离线(离线指断网等)
    		int powerState =(int) pcvMap.get("powerState");// 0 开机 1关机
    		String ip = (String) pcvMap.get("ip") ;
    		String location = ipAndLocation.get(ip) ;
    		if(StringUtils.isBlank(location)){
    			location ="未知" ;
    		}
    		tempMap.put("ipLocation", location) ;
    		
    		if(StringUtils.isNotBlank(deviceState)){//存在筛选条件
		    			int deviceState_bak=Integer.parseInt(deviceState);
		    			if(2==deviceState_bak){//离线页面传值是2
		        			if(1==currentState){
		        				tempMap.putAll(deviceMap);
		        	    		tempMap.put("duration", duration) ;
		        	    		tempMap.putAll(pcvMap);
		        	    		list.add(tempMap) ;
		        			}
		        		}else {//开机关机
		        			if(powerState==deviceState_bak&&0==currentState){
		        				tempMap.putAll(deviceMap);
		        	    		tempMap.put("duration", duration) ;
		        	    		tempMap.putAll(pcvMap);
		        	    		list.add(tempMap) ;
		        			}
		    		}
    		}else{//无存在筛选条件全部状态
    			tempMap.putAll(deviceMap);
	    		tempMap.put("duration", duration) ;
	    		tempMap.putAll(pcvMap);
	    		list.add(tempMap) ;
			
    		}
    		
    	}
    	
    	return list ;
    }
	
    
    // 新版综合监控
 	@At("/indexNew")
 	@Ok("re:beetl:/platform/account/index2.html")
 	@RequiresAuthentication
 	public Object indexNew(ViewModel model,HttpServletRequest request) {
 		 //Map<String, String> map2 = homeService.getDeviceFacilityHourList() ;
         Subject subject = SecurityUtils.getSubject();
         Sys_user user = (Sys_user) subject.getPrincipal();
         Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
         String topUnitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
         
         Map<String, List<String>> unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
         ArrayList<String> list = new ArrayList<String>() ;
         list.add(topUnitId) ;
         unitService.getChildUnitList(unitTreeGroup, topUnitId, list);
         //顶级下所有单位ids
         String allUnitds = StringUtils.strip(Json.toJson(list),"[]") ;
         
         Map map = homeService.getHomeAllCount(allUnitds) ;
         
         //24小时
         List<String> fiveDays = DateUtils.getPastDateIntervalDay(5) ;//获取过去五天日期
         List<JSONObject> lineList = new ArrayList<JSONObject>() ;
         for(String day : fiveDays){
         	JSONObject historyoObj = homeService.getHomeUseTrend(topUnitId, day) ;
         	lineList.add(historyoObj) ;
         }
         
         
         //六个月数据
         //List<Map<String, String>> useTimeNumMap = homeService.getUseTimeNum(allUnitds) ;
         List<Map> deviceUseRate = homeService.getCurDeviceUseRate(allUnitds,0,5) ;
         //受监控数量
         Map<String, String> unitDeviceNumMap = homeService.getUnitDeviceNum(allUnitds) ;
         
         //部门设备实时使用状况
        // Map<String, String> unitPowerMap = homeService.getUnitPower(topUnitId, DateUtils.getDateStr(new Date())) ;
        // Map<String, Object> statusData = statusData(unitTreeGroup,topUnitId,list) ;
         
         
         map.putAll(unitDeviceNumMap);
         map.put("deviceUseRate",deviceUseRate);
         map.put("lineList",  lineList) ;
         map.put("fiveDays", Json.toJson(fiveDays)) ;
         
        // map.putAll(statusData); 
        
         putColorAndSymobl(map,request) ;
         model.setAll(map);
         return null;
 	}
 	/**
 	 * 各部门近一周内的开机率
 	 * @param unitTreeGroup
 	 * @param topUnitId
 	 * @return
 	 */
 	 @At("/statusData")
     @Ok("json:full")
     @RequiresAuthentication
     public Map<String, Object> statusData(Map<String, List<String>> unitTreeGroup,String topUnitId,List<String> li){
          //获取当前登录用户的最顶级单位下所有单位id
 		 if(li!=null){
 			 li.remove(topUnitId) ;
 		 }else{
 	 			 Subject subject = SecurityUtils.getSubject();
 	 	         Sys_user user = (Sys_user) subject.getPrincipal();
 	 	         Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
 	 	         topUnitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
 	 	         unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;
 	 	         li = new ArrayList<String>();
 	 	         unitService.getChildUnitList(unitTreeGroup, topUnitId, li);
 		 }
 		 
 		 
          
          Map<String, Object> statusData = new  HashMap<String, Object>();
          StringBuilder uname = new StringBuilder();
          StringBuilder powerOnRate = new StringBuilder();
          
          if(li.size()>0){
        	  String allUnitds = StringUtils.strip(Json.toJson(li),"[]") ;
        	  //获取所有单位的开关机数量
        	 // Map<String,int[]> homeAllCount = homeService.getHomeAllCountByDepart(allUnitds) ;
        	  //查询每个单位最近七天使用时长大于60分钟的设置为开机数
        	  Map<String,Integer> homeAllCount = homeService.getHomePowerOnCountByDepartFrDayDuration(allUnitds) ;
        	  //查询各个单位的挤入设备数量
        	  Map<String, Integer> unitLinkCollectNum = homeService.getUnitLinkCollectNum(allUnitds) ;
        	  //获取各个单位的名称
        	  Map<String, String> unitAndName = unitService.getUnitAndName(allUnitds) ;
        	  
        	  HashMap<String,Float> map = new HashMap<String,Float>();  
              ValueComparator bvc =  new ValueComparator(map);  
              TreeMap<String,Float> sorted_map = new TreeMap<String,Float>(bvc); 
        	  
        	  
        	  
        	  for(int i=0;i<li.size() ;i++){
            	  String key = li.get(i) ;
             	 ArrayList<String> list = new ArrayList<String>();
             	 list.add(key) ;
             	 unitService.getChildUnitList(unitTreeGroup, key, list);
             	
                 String unitName = unitAndName.get(key);
                 float pon =0 ;
                 float lnm =0 ;
                 for(int s=0;s<list.size();s++){
                	 String ccode = list.get(s) ;
                	 Integer sre = homeAllCount.get(ccode) ;
                	 Integer lre = unitLinkCollectNum.get(ccode) ;
                	 if(sre!=null){
                		 pon+=sre ;
                	 }
                	 if(lre!=null){
                		 lnm+=lre ;
                	 }
                	 
                	 
                 }
                 float i1 = 0 ;
                 if(lnm!=0){
                	 i1 = (pon/(lnm*7))*100 ;
                	 i1=i1>100?100:i1 ;
                 }
                 
				 map.put(unitName, i1);
                
              }
        	  
        	  
        	  sorted_map.putAll(map); 
        	  Iterator<Entry<String, Float>> iterator = sorted_map.entrySet().iterator() ;
        	  while(iterator.hasNext()){
        		  Entry<String, Float> entry = iterator.next() ;
        		  uname.append( entry.getKey()).append(",");
        		  String str = String.format("%.2f", entry.getValue()) ;
                  powerOnRate.append(str).append(",");
        	  }
          }
          
          statusData.put("uname", StringUtils.strip(uname.toString(), ","));
          statusData.put("powerOnRate", StringUtils.strip(powerOnRate.toString(), ","));
          return statusData;
          
     }
    
 	// 监控页跳转
 	@At("/monitorNew")
 	@Ok("re:beetl:/platform/account/monitor2.html")
 	@RequiresAuthentication
 	public void monitorNew(HttpServletRequest request,ViewModel model) {
 		Map<String, Object> map = new HashMap<String,Object>() ;
        putColorAndSymobl(map,request) ;
 		model.setAll(map);
 	}
 	 
 	 /**
     * 新版综合监控二级页面需要数据
     * @param unitId
     * @return
     */
    @At("/getPageDataNew")
    @Ok("json:full")
    @RequiresAuthentication
    public Object getPageDataNew(@Param("unitId") String unitId,@Param("assettype") String  assettype,@Param("deviceState") String deviceState,@Param("pageNo") int pageNo){
    		
    	  
    	 Map<String, Object> pageDataMap = new HashMap<String,Object>() ;
    	
    	 Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
         Map<String, List<String>> unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
         ArrayList<String> list = new ArrayList<String>() ;

         if(StringUtils.isBlank(unitId)){
        	  Subject subject = SecurityUtils.getSubject();
             Sys_user user = (Sys_user) subject.getPrincipal();
             unitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
         }
         
         list.add(unitId) ;
         unitService.getChildUnitList(unitTreeGroup, unitId, list);
         //该单位下级所有单位ids
         String allUnitds = StringUtils.strip(Json.toJson(list),"[]") ;
    	//受监控数量
        Map<String, String> unitDeviceNumMap = homeService.getUnitDeviceNum(allUnitds) ;
        //单位开机数,关机数,离线数
        Map unitDeviceStateNum = homeService.getHomeAllCount(allUnitds) ;
        //单位24小时数量
        JSONObject historyMap = homeService.getHomeUseTrend(unitId, DateUtils.getDateStr(new Date())) ;
        //该单位下的所有设备列表
        int pageSize =21;
        NutMap map =homeService.getUnitDeviceList(allUnitds,assettype,deviceState,pageNo, pageSize) ;
        List<Map> deviceList =(List<Map>) map.get("data");
        int total=map.getInt("recordsFiltered");
        int  totalPage = (total + pageSize -1) / pageSize;
        //获取该单位下的所有单位的使用时长
        Map<String, String> deviceDurationMap = facilityService.findUnitDeviceUseTimeListByMonth(DateUtils.getDateStr(new Date()), allUnitds) ;
		//获取scard数据
        Map<String ,Ins_ScardDevice> scardMap = scardService.getScardMap(redisService) ;
        
        
        List<Map<String, Object>> compositeDeviceData = CompositeDeviceData(scardMap,deviceList,deviceDurationMap,deviceState) ;
        
        
        pageDataMap.putAll(unitDeviceNumMap);
        pageDataMap.putAll(unitDeviceStateNum);
        pageDataMap.put("lineListByUnit",historyMap.toString());
        pageDataMap.put("dataList", compositeDeviceData) ;
        pageDataMap.put("totalPage", totalPage) ;
        pageDataMap.put("currentNum", deviceList.size()) ;
        return pageDataMap ;
    }
 	
    /**
     * 综合监控三级页面
     * @param
     * @return
     */
    @At("/getDataDetailNew")
    @Ok("beetl:/platform/account/detail2.html")
    @RequiresAuthentication
    public Object getDataDetailNew(@Param("deviceCode") String deviceCode,@Param("deviceType") String deviceType){
    	Map<String, Object> facilityDayMap = facilityService.getDeviceFacilityDayLastThirtyDays(deviceCode) ;
     	Map<String, Object> deviceFacilityYear = facilityService.getDeviceFacilityYear(deviceCode) ;
    	facilityDayMap.putAll(deviceFacilityYear); 
    	//最近一个月
    	List<JSONObject> listMonth = facilityService.getDeviceFacilityUseTimeOfProject(deviceCode, 1) ;
    	//最近一年
    	List<JSONObject> listYear = facilityService.getDeviceFacilityUseTimeOfProject(deviceCode, 2) ;
    	facilityDayMap.put("listMonth", listMonth) ;
    	facilityDayMap.put("listYear", listYear) ;
    	facilityDayMap.put("deviceCode", deviceCode) ;
    	facilityDayMap.put("deviceType", deviceType) ;
    	Map<String, String> departAndChargePerson = deviceinfoServcie.getDepartAndChargePerson(deviceCode) ;
    	facilityDayMap.putAll(departAndChargePerson); 
    	return facilityDayMap ;
    	
    }
    
    
  	
    
    /**
     * 填充颜色和控制轴实体还是空心
     * @param map
     * @param request
     */
    public void putColorAndSymobl(Map map,HttpServletRequest request){
    	String color="" ;
    	boolean flag = false;
        // 获取cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
       	 String name = cookie.getName();
       	 String value = cookie.getValue();
       	 if(Globals.THEME.equals(name) && Globals.BLACK.equals(value)){
       		 flag = true;
       		 break;
       	 }
		}
        String colorTemp="";
        String consoleSymbol="";
        String icon="" ;
        if(flag){//黑色
        	 color=Globals.BLACK ;
          	 colorTemp =Globals.WHITE;
          	 consoleSymbol=Globals.EMPTYDIAMOND;
        }else {//白色
        	color=Globals.WHITE ;
          	 colorTemp=Globals.BLACK ;
          	 consoleSymbol=Globals.DIAMOND;
          	icon="2" ;
        }
        map.put("css",color) ;
        map.put("colorTemp",colorTemp) ;
        map.put("consoleSymbol",consoleSymbol) ;
        map.put("icon",icon);
    }
    /**
 	 * 部门实时使用情况分析
 	 * @param unitTreeGroup
 	 * @param topUnitId
 	 * @return
 	 */
 	 @At("/statusData2")
     @Ok("json:full")
     @RequiresAuthentication
     public Map<String, Object> statusData2(Map<String, List<String>> unitTreeGroup,String topUnitId,List<String> li){
          //获取当前登录用户的最顶级单位下所有单位id
 		 if(li!=null){
 			 li.remove(topUnitId) ;
 		 }else{
 	 			 Subject subject = SecurityUtils.getSubject();
 	 	         Sys_user user = (Sys_user) subject.getPrincipal();
 	 	         Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
 	 	         topUnitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
 	 	         unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;
 	 	         li = new ArrayList<String>();
 	 	         unitService.getChildUnitList(unitTreeGroup, topUnitId, li);
 		 }
 		 
 		 
          
          Map<String, Object> statusData = new  HashMap<String, Object>();
          StringBuilder uname = new StringBuilder();
          StringBuilder powerOnNum = new StringBuilder();
          StringBuilder powerOffNum = new StringBuilder();
          StringBuilder outLineNum = new StringBuilder();
          
          if(li.size()>0){
        	  String allUnitds = StringUtils.strip(Json.toJson(li),"[]") ;
        	  //获取所有单位的开关机数量
        	  Map<String,int[]> homeAllCount = homeService.getHomeAllCountByDepart(allUnitds) ;
        	  Map<String, String> unitAndName = unitService.getUnitAndName(allUnitds) ;
        	  //单位id降序
        	  Set<String> unitIdDesc = homeAllCount.keySet() ;
        	  li.removeAll(unitIdDesc) ;
        	  li.addAll(0,unitIdDesc) ;
        	  
        	  for(int i=0;i<li.size() ;i++){
            	  String key = li.get(i) ;
             	 ArrayList<String> list = new ArrayList<String>();
             	 list.add(key) ;
             	 unitService.getChildUnitList(unitTreeGroup, key, list);
             	
             	//该单位下级所有单位ids
                 // String allUnitds = StringUtils.strip(Json.toJson(list),"[]") ;
                //单位开机数,关机数,离线数
                // Map<String,Integer> unitDeviceStateNum = homeService.getHomeAllCount(allUnitds) ;
                 String unitName = unitAndName.get(key);
                 int pon =0 ;
                 int pof =0 ;
                 int out =0;
                 for(int s=0;s<list.size();s++){
                	 String ccode = list.get(s) ;
                	 int[] sre = homeAllCount.get(ccode) ;
                	 if(sre!=null&&sre.length>0){
                		 pon+=sre[0];
                		 pof+=sre[1];
                		 out+=sre[2];
                	 }
                 }
                 
                 uname.append(unitName).append(",");
                 powerOnNum.append(pon).append(",");
                 powerOffNum.append(pof).append(",");
                 outLineNum.append(out).append(",");
                
              }
          }
          
          
          statusData.put("uname", StringUtils.strip(uname.toString(), ","));
          statusData.put("dePowerOnNum", StringUtils.strip(powerOnNum.toString(), ","));
          statusData.put("dePowerOffNum", StringUtils.strip(powerOffNum.toString(), ","));
          statusData.put("dePoutLineNum", StringUtils.strip(outLineNum.toString(), ","));
          return statusData;
          
     }
    
}

/**
 * 比较器
 * @author wangqiang
 *
 */
class ValueComparator implements Comparator<String> {  
	  
    Map<String, Float> base;  
    //这里需要将要比较的map集合传进来
    public ValueComparator(Map<String, Float> base) {  
        this.base = base;  
    }  
  
    // Note: this comparator imposes orderings that are inconsistent with equals.    
    //比较的时候，传入的两个参数应该是map的两个key，根据上面传入的要比较的集合base，可以获取到key对应的value，然后按照value进行比较   
    public int compare(String a, String b) {  
        if (base.get(a) >= base.get(b)) {  
            return -1;  
        } else {  
            return 1;  
        } // returning 0 would merge keys  
    }  
}  


