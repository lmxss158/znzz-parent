package com.znzz.app.instrument.modules.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.instrument.modules.models.Ins_DeviceFacility;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityDay;
import com.znzz.app.instrument.modules.service.InsDeviceFacilityService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.util.DateUtils;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
/**
 * 设备使用状况实现类
 * @author wangqiang
 *
 */
@IocBean(args = { "refer:dao" })
public class InsDeviceFacilityServiceImpl extends BaseServiceImpl<Ins_DeviceFacility> implements InsDeviceFacilityService {

	public InsDeviceFacilityServiceImpl(Dao dao) {
		super(dao);
	}
	/**
	 * 获取设备使用状况list
	 */
	@Override
	public NutMap findDeviceFacilityList(String deviceCode,String date,int length, int start, int draw,List<DataTableOrder> orders,List<DataTableColumn> columns) {
		
		Sql sql = Sqls.create("SELECT a.id, a.deviceCode,b.deviceName,b.deviceVersion,a.month,a.duration,a.offLineTime,((a.duration+IFNULL(a.offLineTime,0))- (SELECT IFNULL(SUM(duration),0)  from ins_device_facility WHERE pid=a.id)) as unassigned FROM ins_device_facility a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode where a.pid=0 $var $var2 $order");
		if(StringUtils.isNotBlank(deviceCode)){//统一编号  , 设备名称  , 设备型号
			String str = "%"+deviceCode.trim()+"%" ;
			Sql sql2 = Sqls.create("and ( a.deviceCode like @str or b.deviceName like @str or b.deviceVersion like @str )") ;
			sql2.setParam("str", str) ;
			sql.setVar("var", sql2) ;
		}
	  
		if(StringUtils.isNotBlank(date)){
			Sql sql3 = Sqls.create("and date_format(a.month,'%Y-%m')=@date") ;
			sql3.setParam("date", date) ;
			sql.setVar("var2", sql3) ;
		}
		
		if (orders != null && orders.size() > 0) {
			Cnd  cri2 = Cnd.NEW() ;
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cri2.getOrderBy().orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir()).orderBy("id", "desc");
            }
            sql.setVar("order", cri2) ;
       }
		NutMap nmap = data(length,start,draw,sql,sql) ;
		return nmap;
	}
	
	/**
	 * 批量插入数据
	 */
	@Override
	public void insertDeviceFacility(List<Ins_DeviceFacility> list) {
		dao().fastInsert(list) ;
	}
	
	/**
	 * 根据pid 获取子列表
	 */
	@Override
	public NutMap findChildDeviceFacilityList(Integer pid,int length, int start, int draw,List<DataTableOrder> orders, List<DataTableColumn> columns) {
		Sql sql = Sqls.create("SELECT a.id,a.projectCode ,b.name,a.chTimeStart,a.chTimeEnd,a.duration ,a.operateTime from ins_device_facility a LEFT JOIN ins_project_info b ON a.projectCode = b.code  WHERE a.pid=@pid $order");
		sql.setParam("pid", pid) ;
		
		if (orders != null && orders.size() > 0) {
			Cnd  cri2 = Cnd.NEW() ;
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cri2.getOrderBy().orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
            sql.setVar("order", cri2) ;
       }
		
		NutMap nmap = data(length,start,draw,sql,sql) ;
		return nmap;
	}
	@Override
	public Map<String,Integer> findDurtaionById(Integer pid) {
		
		
		Sql sqlp = Sqls.create("SELECT SUM(duration) as count from ins_device_facility WHERE pid =@id") ;
		sqlp.setParam("id", pid) ;
		// 设置回调函数
		sqlp = sqlp.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Integer> list = new ArrayList<>();
					while (rs.next()) {
							list.add(rs.getInt("count"));
					}
						return list;
				}
		});
		HashMap<String, Integer> map = new HashMap<String,Integer>() ;
		Ins_DeviceFacility facility = dao().fetch(Ins_DeviceFacility.class, pid) ;
		map.put("assignNum", dao().execute(sqlp).getList(Integer.class).get(0)) ;
		
		Integer time = facility.getOffLineTime() ;
		if(time==null){
			time= 0 ;
		}
		map.put("allNum",facility.getDuration()+time) ;
		
		return map ;
	}
	
	/**
	 * 删除
	 */
	@Override
	public void delFacilityById(Integer id) {
		dao().delete(Ins_DeviceFacility.class, id) ;
	}
	
	
	/**
	 * 获取当月list
	 */
	@Override
	public List<Ins_DeviceFacility> findCurMonthList() {
		Sql sql = Sqls.create("SELECT * from ins_device_facility WHERE pid=0 AND DATE_FORMAT(month, '%Y%m') = DATE_FORMAT(CURDATE()-1, '%Y%m')") ;
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao().getEntity(Ins_DeviceFacility.class));
		dao().execute(sql);
		List<Ins_DeviceFacility> list = sql.getList(Ins_DeviceFacility.class);
		
		return list;
	}
	
	/**
	 * 插入或更新for定时任务用
	 */
	@Override
	public void insertOrUpdate(Map<String, Integer> countMap, Map<String, Ins_DeviceFacility> dfMap) {
		
		 Calendar cal = Calendar.getInstance();  
         cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
         cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH)); 
		 
		if(countMap!=null){
			  
			for (Map.Entry<String, Integer> entry : countMap.entrySet()) {  
			    Ins_DeviceFacility facility=null ;
			    String deviceCode = entry.getKey() ;
				if(dfMap.containsKey(deviceCode)){
					facility = dfMap.get(deviceCode);
					
					if(!facility.getDuration().equals(entry.getValue())){//值相等不更新
						facility.setDuration(entry.getValue());
						dao().update(facility) ;
					}
				}else{
					facility = new Ins_DeviceFacility() ;
					facility.setDeviceCode(deviceCode);
					facility.setMonth(cal.getTime());
					facility.setPid(0);
					facility.setDuration(entry.getValue());
					dao().insert(facility) ;
				}
			   
			}
		}
		
		
		
	}
	
	/**
	 * 获取指定单位下的设备使用时长
	 * @param month
	 * @param unitIds
	 * @return
	 */
	@Override
	public Map<String, String> findUnitDeviceUseTimeListByMonth(String month, String unitIds) {
	    Sql sql = Sqls.create("SELECT  a.deviceCode , (a.duration+IFNULL(offLineTime,0)) as duration  from ins_device_facility a LEFT JOIN ins_device_info b ON a.deviceCode=b.deviceCode WHERE a.pid=0 AND DATE_FORMAT(a.month, '%Y%m') = DATE_FORMAT(@month, '%Y%m') AND b.borrowDepart IN ($unitIds)") ;
	    sql.setParam("month", month) ;
	    sql.setVar("unitIds", unitIds) ;
	    sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String,String>> list = new ArrayList<Map<String,String>>() ;
				Map<String,String> map = new HashMap<String,String>() ;
				while (rs.next()) {
					map.put(rs.getString("deviceCode"),rs.getString("duration")) ;
				}
				 list.add(map) ;
				return list;
			}
		});
	    
		return  dao().execute(sql).getList(Map.class).get(0);
	}
	
	/**
	 * 批量插入Ins_DeviceFacilityDay
	 */
	@Override
	public void insertDeviceFacilityDay(Map<String, Integer> map) {
		
		Date yesterDaydate = DateUtils.getYesterDayDate() ;
		ArrayList<Ins_DeviceFacilityDay> list = new ArrayList<Ins_DeviceFacilityDay>() ;
		ArrayList<Ins_DeviceFacilityDay> ulist = new ArrayList<Ins_DeviceFacilityDay>() ;
		//遍历tpMap
		Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
		while(it.hasNext()){
			
			Entry<String, Integer> entry = it.next() ;
			
			Ins_DeviceFacilityDay deviceFacilityDay = dao().fetch(Ins_DeviceFacilityDay.class, Cnd.where("DATE_FORMAT(day, '%Y%m%d')", "=",DateUtils.getYesterDayStr2()).and("deviceCode","=",entry.getKey())) ;
			if(deviceFacilityDay==null){
				Ins_DeviceFacilityDay day = new Ins_DeviceFacilityDay() ;
				day.setDay(yesterDaydate);
				day.setDeviceCode(entry.getKey());
				day.setDuration(entry.getValue());
				list.add(day) ;
			}else{
				deviceFacilityDay.setDuration(entry.getValue());
				ulist.add(deviceFacilityDay);
			}
		}
		if(list.size()>0){
			dao().fastInsert(list) ;
		}
		if(ulist.size()>0){
			dao().update(ulist) ;
		}
	}
	/**
	 * 获取最近三十天日使用时长
	 * @param deviceCode
	 * @return
	 */
	@Override
	public Map<String, Object> getDeviceFacilityDayLastThirtyDays(String deviceCode) {
		Sql sql = Sqls.create("SELECT date_format(day,'%Y-%m-%d') as day,duration from ins_device_facility_day WHERE `day`>=DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND deviceCode =@deviceCode");
		sql.setParam("deviceCode", deviceCode) ;
		 sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String,Integer>> list = new ArrayList<Map<String,Integer>>() ;
				Map<String,Integer> map = new HashMap<String,Integer>() ;
				while (rs.next()) {
					map.put(rs.getString("day"),rs.getInt("duration")) ;
				}
				 list.add(map) ;
				return list;
			}
		});
		Map<String,Integer> map = dao().execute(sql).getList(Map.class).get(0) ;
		Map<String, Object> reMap = new HashMap<String,Object>() ;
		List<String> thrityDays = DateUtils.getLastThrityDays() ;
		List<Integer> durationList = new ArrayList<Integer>() ;
		for(String l : thrityDays){
			Integer temp = 0 ;
			if(map.containsKey(l)){
				 temp = map.get(l) ;
			}
			
			durationList.add(temp) ;
			
		}
		
		reMap.put("facilityDate", StringUtils.strip(Json.toJson(thrityDays),"{}")) ;
		reMap.put("durationList", StringUtils.strip(Json.toJson(durationList),"{}")) ;
		 
		return reMap;
	}
	
	/**
	 * 获取最近一年的 月使用时长
	 */
	@Override
	public Map<String, Object> getDeviceFacilityYear(String deviceCode) {
		Sql sql = Sqls.create("SELECT date_format(`month`, '%Y-%m') as month ,(duration+IFNULL(offLineTime,0))AS duration  from ins_device_facility WHERE pid = 0 AND date_format(`month`, '%Y-%m') >= date_format(DATE_SUB(CURDATE(), interval 12 month),'%Y-%m') AND date_format(`month`, '%Y-%m') < date_format(	CURDATE(),'%Y-%m') AND deviceCode =@deviceCode");
		sql.setParam("deviceCode", deviceCode) ;
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String,Integer>> list = new ArrayList<Map<String,Integer>>() ;
				Map<String,Integer> map = new HashMap<String,Integer>() ;
				while (rs.next()) {
					map.put(rs.getString("month"),rs.getInt("duration")) ;
				}
				 list.add(map) ;
				return list;
			}
		});
		
		Map<String,Integer> map = dao().execute(sql).getList(Map.class).get(0) ;
		Map<String, Object> reMap = new HashMap<String,Object>() ;
		List<String> lastTwelveMonths = DateUtils.getLastTwelveMonths() ;
		List<Integer> durationList = new ArrayList<Integer>() ;

		for(String l : lastTwelveMonths){
			Integer temp = 0 ;
			if(map.containsKey(l)){
				 temp = map.get(l) ;
			}
			
			durationList.add(temp) ;
			
		}
		
		reMap.put("facilityMonthDate", StringUtils.strip(Json.toJson(lastTwelveMonths),"{}")) ;
		reMap.put("monthDurationList", StringUtils.strip(Json.toJson(durationList),"{}")) ;
		return reMap;
	}
	
	/**
	 * 统计最近时间,设备再各个项目中的使用时长
	 */
	@Override
	public List<JSONObject> getDeviceFacilityUseTimeOfProject(String deviceCode,Integer type) {
		
		String monthStr="date_format($timeField,'%Y-%m') = date_format(DATE_SUB(CURDATE(),INTERVAL 1 MONTH),'%Y-%m')";
		String yearStr="(date_format($timeField,'%Y-%m') <= date_format(DATE_SUB(CURDATE(),INTERVAL 1 MONTH),'%Y-%m') AND date_format($timeField,'%Y-%m') >= date_format(DATE_SUB(CURDATE(),INTERVAL 12 MONTH),'%Y-%m'))" ;
		String str="" ;
		String tempStr="" ;
		if(type==1){//最近一月
			//str="date_format(a.chTimeStart,'%Y-%m') = date_format(DATE_SUB(CURDATE(),INTERVAL 1 MONTH),'%Y-%m')" ;
			tempStr = monthStr;
		}else{
			//str="(date_format(a.chTimeStart,'%Y-%m') <= date_format(DATE_SUB(CURDATE(),INTERVAL 1 MONTH),'%Y-%m') AND date_format(a.chTimeStart,'%Y-%m') >= date_format(DATE_SUB(CURDATE(),INTERVAL 12 MONTH),'%Y-%m'))" ;
			tempStr = yearStr;
		}
		
		str = tempStr.replace("$timeField", "a.chTimeStart");
		
//		Sql sql = Sqls.create("SELECT IFNULL(b.`name`,a.projectCode) as name, FORMAT(SUM(CASE WHEN a.chTimeStart>=DATE_SUB(CURDATE(), $time) THEN a.duration ELSE (a.duration/(datediff( a.chTimeEnd , a.chTimeStart)+1))*(DATEDIFF(a.chTimeEnd,DATE_SUB(CURDATE(), $time))+1) END ),0) AS totalDuration from ins_device_facility a LEFT JOIN ins_project_info b on a.projectCode = b.`code` where a.pid!=0  AND a.deviceCode=@deviceCode AND (a.chTimeStart >= DATE_SUB(CURDATE(), $time) OR (a.chTimeStart < DATE_SUB(CURDATE(), $time) AND DATE_SUB(CURDATE(), $time)<=a.chTimeEnd )) GROUP BY a.projectCode");
		Sql sql = Sqls.create("SELECT IFNULL(b.`name`,a.projectCode) as name ,SUM(a.duration) AS totalDuration FROM ins_device_facility a LEFT JOIN ins_project_info b ON a.projectCode = b.`code` WHERE a.deviceCode=@deviceCode AND a.pid !=0 AND $time GROUP BY a.projectCode,b.name");
		sql.setParam("deviceCode", deviceCode) ;
		sql.setVar("time", str) ;
		
		sql = sql.setCallback(new SqlCallback() {
			
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				Integer temp = 0 ;
				List<JSONObject> list = new ArrayList<JSONObject>() ;
				while (rs.next()) {
					JSONObject object = new JSONObject() ;
					int totalDuration = rs.getInt("totalDuration") ;
					object.put("value", totalDuration) ;
					object.put("name", rs.getString("name")) ;
					list.add(object) ;
					temp+=totalDuration ;
				}
				
				JSONObject finallyObj = new JSONObject() ;
				finallyObj.put("durated", temp) ;
				list.add(finallyObj) ;
				
				return list;
			}
		});
		List<JSONObject> list = dao().execute(sql).getList(JSONObject.class) ;
		JSONObject finallyObj = list.get(list.size()-1) ;
		int durated = finallyObj.getInt("durated") ;
		
		/*if(type==1){//最近一月
			str=" date_format(month,'%Y-%m') = date_format(DATE_SUB(CURDATE(),INTERVAL 1 MONTH),'%Y-%m')" ;
		}else{
			str=" (date_format(month,'%Y-%m') <= date_format(DATE_SUB(CURDATE(),INTERVAL 1 MONTH),'%Y-%m') AND date_format(month,'%Y-%m')>= date_format(DATE_SUB(CURDATE(),INTERVAL 12 MONTH),'%Y-%m'))" ;
		}*/
		str = tempStr.replace("$timeField", "month");
		
		Integer totalDuration = getUnDurationByTime(deviceCode,str);
		JSONObject jsonObject = new JSONObject() ;
		int value = totalDuration-durated;
		jsonObject.put("value", value<0?0:value) ;
		jsonObject.put("name", Globals.UNDURATION) ;
		list.remove(list.size()-1);
		list.add(jsonObject);
 		
		return list;
	}
	
	/**
	 * 更新设备离线时长
	 */
	@Override
	public void updateDeviceFacilityOffLineTime(String deviceCode, String month, int time) {
		
		int count = dao().count(Ins_DeviceFacility.class, Cnd.where("deviceCode", "=", deviceCode).and("date_format(month,'%Y-%m')", "=", month)) ;
		if(count>0){
			Sql sql = Sqls.create("UPDATE ins_device_facility SET offLineTime = IFNULL(offLineTime,0)+@time WHERE deviceCode=@deviceCode AND date_format(month,'%Y-%m')=@month") ;
			sql.setParam("deviceCode", deviceCode) ;
			sql.setParam("time", time) ;
			sql.setParam("month", month) ;
			dao().execute(sql) ;
		}else{
			Ins_DeviceFacility facility = new Ins_DeviceFacility() ;
			facility.setDeviceCode(deviceCode);
			facility.setMonth(DateUtils.getDateObj(month));
			facility.setDuration(0);
			facility.setOffLineTime(time);
			facility.setPid(0);
			dao().insert(facility) ;
		}
		
	}
	
	/**
	 * 获取指定时间段内的设备的总的使用时长
	 * @param deviceCode
	 * @param time
	 * @return
	 */
	public Integer  getUnDurationByTime(String deviceCode,String time){
		//最近时间内 总时长 加上 各月份的 离线总时长
		//Sql sql = Sqls.create("SELECT SUM(a.totalDuration+IFNULL(b.offLineTime,0)) as totalDuration FROM ( SELECT DATE_FORMAT(day, '%Y%m') month, IFNULL(SUM(duration),0) as totalDuration FROM ins_device_facility_day WHERE deviceCode = @deviceCode  AND `day` >=DATE_SUB(CURDATE(), $time) GROUP BY DATE_FORMAT(day, '%Y%m') ) a LEFT JOIN ( SELECT DATE_FORMAT(`month`, '%Y%m') month, IFNULL(offLineTime,0) as offLineTime FROM ins_device_facility WHERE deviceCode =@deviceCode AND pid=0 ) b ON a.`month` = b.`month`");
		Sql sql = Sqls.create("SELECT SUM(duration+IFNULL(offLineTime,0)) as totalDuration  from ins_device_facility WHERE deviceCode=@deviceCode AND pid=0 AND $time") ;
		sql.setParam("deviceCode", deviceCode) ;
		sql.setVar("time", time) ;
		
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Integer> list = new ArrayList<Integer>() ;
				while (rs.next()) {
					int count = rs.getInt("totalDuration") ;
					list.add(count) ;
				}
				return list;
			}
		});
		List<Integer> list = dao().execute(sql).getList(Integer.class) ;
		int r =0 ;
		if(list.size()>0){
			r = list.get(0) ;
		}
		return r ;
	}
	/**
	 * 获取当月日使用时长
	 */
	@Override
	public Map<String,Integer> getCurnMonthList() {
		Sql sql = Sqls.create("SELECT deviceCode ,SUM(duration) as duration from ins_device_facility_day WHERE DATE_FORMAT(`day`, '%Y%m') = DATE_FORMAT(CURDATE()-1, '%Y%m') GROUP BY deviceCode") ;
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				Map<String,Integer> map =new HashMap<String,Integer>();
				List<Map<String,Integer>> list = new ArrayList<Map<String,Integer>>() ;
				while (rs.next()) {
					String deviceCode = rs.getString("deviceCode") ;
					int duration = rs.getInt("duration") ;
					map.put(deviceCode, duration) ;
				}
				list.add(map);
				return list;
			}
		});
		
		Map map = dao().execute(sql).getList(Map.class).get(0) ;
		return map ;
	}

}
