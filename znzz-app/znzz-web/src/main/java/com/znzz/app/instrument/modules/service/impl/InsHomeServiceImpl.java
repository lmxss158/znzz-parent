package com.znzz.app.instrument.modules.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;

import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityHistory;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityHour;
import com.znzz.app.instrument.modules.models.Ins_DevicePower;
import com.znzz.app.instrument.modules.models.Ins_DeviceState;
import com.znzz.app.instrument.modules.models.Ins_DeviceUseTrend;
import com.znzz.app.instrument.modules.models.Ins_Gateway;
import com.znzz.app.instrument.modules.service.InsHomeService;
import com.znzz.app.web.commons.util.DateUtils;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.OffsetPager;

/**
 * 首页统计实现类
 * 
 * @author wangqiang
 *
 */
@IocBean(args = { "refer:dao" })
public class InsHomeServiceImpl extends BaseServiceImpl<Ins_Gateway> implements InsHomeService {

	public InsHomeServiceImpl(Dao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取首页下面三个饼状图数据
	 */
	@Override
	public Map getHomeAllCount(String unitIds) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			Sql sql = Sqls.create(
					"SELECT e.borrowDepart ,IFNULL(sum(e.powerOnNum),0) as powerOnNum,IFNULL(SUM(e.powerOffNum),0) as powerOffNum,IFNULL(sum(e.outLineNum),0) as outLineNum from (SELECT	c.borrowDepart,	CASE b.powerState WHEN 0 THEN COUNT(*) ELSE 0 END powerOnNum,  CASE b.powerState WHEN 1 THEN COUNT(*) ELSE 0 END powerOffNum,  CASE b.state WHEN 1 THEN COUNT(*) ELSE 0 END outLineNum FROM	ins_collect a LEFT JOIN ins_device_state b ON a.deviceCode = b.deviceCode LEFT JOIN ins_device_info c ON a.deviceCode = c.deviceCode GROUP BY	c.borrowDepart,	b.state, b.powerState) e $condition GROUP BY  e.borrowDepart");
			if(StringUtils.isNotBlank(unitIds)){
				Criteria cri = Cnd.cri() ;
				cri.where().and("e.borrowDepart", "in",unitIds) ; 
				sql.setCondition(cri) ;
			}

			// 设置回调函数
			sql = sql.setCallback(new SqlCallback() {

				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
					Map<String, Integer> map = new HashMap<String, Integer>();
					int powerOnNum = 0;
					int powerOffNum = 0;
					int outLineNum = 0;

					while (rs.next()) {
						powerOnNum += rs.getInt("powerOnNum");
						powerOffNum += rs.getInt("powerOffNum");
						outLineNum += rs.getInt("outLineNum");

					}
					map.put("powerOnNum", powerOnNum);
					map.put("powerOffNum", powerOffNum);
					map.put("outLineNum", outLineNum);
					list.add(map);
					return list;
				}

			});

			map = dao().execute(sql).getList(Map.class).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map getHomeAllCountByDepart(String unitIds) {
		Map<String, int[]> map = new HashMap<String, int[]>();
		try {
			Sql sql = Sqls.create(
					"SELECT e.borrowDepart ,sum(e.powerOnNum) as powerOnNum,SUM(e.powerOffNum) as powerOffNum,sum(e.outLineNum) as outLineNum from (SELECT	c.borrowDepart,	CASE b.powerState WHEN 0 THEN COUNT(*) ELSE 0 END powerOnNum,  CASE b.powerState WHEN 1 THEN COUNT(*) ELSE 0 END powerOffNum,  CASE b.state WHEN 1 THEN COUNT(*) ELSE 0 END outLineNum FROM	ins_collect a LEFT JOIN ins_device_state b ON a.deviceCode = b.deviceCode LEFT JOIN ins_device_info c ON a.deviceCode = c.deviceCode GROUP BY	c.borrowDepart,	b.state, b.powerState) e WHERE e.borrowDepart IS NOT null $var GROUP BY  e.borrowDepart ORDER BY (powerOnNum+powerOffNum+OutLineNum) desc");
			if(StringUtils.isNotBlank(unitIds)){
				Sql sql2 = Sqls.create(" and e.borrowDepart in ($var) ") ;
				sql2.setVar("var", unitIds) ;
				sql.setVar("var", sql2) ;
			}

			// 设置回调函数
			sql = sql.setCallback(new SqlCallback() {

				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<Map<String, int[]>> list = new ArrayList<Map<String, int[]>>();
					Map<String, int[]> map = new HashMap<String, int[]>();
					while (rs.next()) {
						int[] arr = new int[3] ;
						String dpcode = rs.getString("borrowDepart") ;
						arr[0] = rs.getInt("powerOnNum");
						arr[1] =rs.getInt("powerOffNum");
						arr[2] = rs.getInt("outLineNum");
						map.put(dpcode, arr) ;	
					}
					list.add(map);
					return list;
				}

			});

			map = dao().execute(sql).getList(Map.class).get(0);
			map = sortMapByValue(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	
	/**
	 * 统计开机数与采集数并更新
	 */
	@Override
	public void updateInsDeviceFacilityHourBean(Ins_DeviceFacilityHour facilityHour) {
		// 开机数
		int powerOnNum = dao().count(Ins_DeviceState.class, Cnd.where("state", "=", 0).and("powerState", "=", 0));
		// 采集数量
		int collectNum = dao().count(Ins_Collect.class);

		facilityHour.setPowerOnNum(powerOnNum);
		facilityHour.setCollectNum(collectNum);

		dao().update(facilityHour);
	}

	/**
	 * 获取bean
	 */
	@Override
	public Ins_DeviceFacilityHour getDeviceFacilityHourBean(Integer hour) {
		Ins_DeviceFacilityHour facilityHour = dao().fetch(Ins_DeviceFacilityHour.class,
				Cnd.where("dateHour", "=", hour));
		return facilityHour;
	}

	/**
	 * 获取设备24小时运行状况
	 */
	@Override
	public Map<String, String> getDeviceFacilityHourList() {
		List<Ins_DeviceFacilityHour> list = dao().query(Ins_DeviceFacilityHour.class, null);
		// Map<Integer, Ins_DeviceFacilityHour> tempmap = new HashMap<>() ;
		StringBuilder hourStr = new StringBuilder();
		StringBuilder powerOnStr = new StringBuilder();
		StringBuilder collectStr = new StringBuilder();
		/*
		 * for(int i=0;i<list.size();i++){ Ins_DeviceFacilityHour facilityHour =
		 * list.get(i) ; tempmap.put(facilityHour.getDateHour(), facilityHour) ;
		 * }
		 */

		Integer[] curArr = getCurArr();

		for (int j = 0; j < curArr.length; j++) {
			// Ins_DeviceFacilityHour hour = tempmap.get(curArr[j]) ;
			Ins_DeviceFacilityHour hour = list.get(curArr[j]);
			hourStr.append("'" + curArr[j] + ":00'").append(",");
			powerOnStr.append(hour.getPowerOnNum()).append(",");
			collectStr.append(hour.getCollectNum()).append(",");
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("timeData", hourStr.toString());
		map.put("powerOnData", powerOnStr.toString());
		map.put("collectStrData", collectStr.toString());
		return map;
	}

	public Integer[] getCurArr() {

		Integer[] timeArr = new Integer[24];
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		int curHour = Integer.parseInt(sdf.format(new Date()));
		int n = 0;
		for (int i = curHour + 1; i < 24; i++) {
			timeArr[n] = i;
			n++;
		}

		for (int i = 0; i <= curHour; i++) {
			timeArr[n] = i;
			n++;
		}

		return timeArr;

	}

	/**
	 * 获取每个单位的收集总数(开机+关机)
	 */
	@Override
	public Map<String, Integer> getUnitCollectNum() {
		Sql sql = Sqls.create(
				"SELECT b.borrowDepart ,count(*) as num FROM ins_collect a LEFT JOIN ins_device_state c ON a.deviceCode=c.deviceCode LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode WHERE c.powerState in (0,1)  GROUP BY b.borrowDepart");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
				Map<String, Integer> map = new HashMap<String, Integer>();
				while (rs.next()) {
					map.put(rs.getString("borrowDepart"), rs.getInt("num"));
				}
				list.add(map);
				return list;
			}

		});
		return dao().execute(sql).getList(Map.class).get(0);
	}

	/**
	 * 获取每个单位的开机数量
	 */
	@Override
	public Map<String, Integer> getUnitPowerOnNum() {
		Sql sql = Sqls.create(
				"SELECT c.borrowDepart ,count(*) as num FROM ins_collect a LEFT JOIN ins_device_state b ON a.deviceCode=b.deviceCode LEFT JOIN ins_device_info c ON b.deviceCode = c.deviceCode WHERE b.powerState=0 GROUP BY c.borrowDepart");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
				Map<String, Integer> map = new HashMap<String, Integer>();
				while (rs.next()) {
					map.put(rs.getString("borrowDepart"), rs.getInt("num"));
				}
				list.add(map);
				return list;
			}

		});
		return dao().execute(sql).getList(Map.class).get(0);
	}

	/**
	 * 获取今日单位历史记录
	 */
	@Override
	public Map<String, Ins_DeviceFacilityHistory> getTodayUnitHistory(String unitId) {

		Criteria cri = Cnd.cri();
		if (StringUtils.isNotBlank(unitId)) {
			cri.where().and("unitId", "=", unitId);
		}
		cri.where().and("DATEDIFF(date,NOW())", "=", 0);// 今天的数据
		Map<String, Ins_DeviceFacilityHistory> map = new HashMap<String, Ins_DeviceFacilityHistory>();
		List<Ins_DeviceFacilityHistory> list = dao().query(Ins_DeviceFacilityHistory.class, cri);

		for (int i = 0; i < list.size(); i++) {
			Ins_DeviceFacilityHistory history = list.get(i);
			map.put(history.getUnitId(), history);
		}

		return map;
	}
	
	/**
	 * 获取今日单位使用趋势
	 */
	@Override
	public Map<String, Ins_DeviceUseTrend> getTodayUnitUseTrend(String unitId) {

		Criteria cri = Cnd.cri();
		if (StringUtils.isNotBlank(unitId)) {
			cri.where().and("unitId", "=", unitId);
		}
		cri.where().and("DATEDIFF(date,NOW())", "=", 0);// 今天的数据
		Map<String, Ins_DeviceUseTrend> map = new HashMap<String, Ins_DeviceUseTrend>();
		List<Ins_DeviceUseTrend> list = dao().query(Ins_DeviceUseTrend.class, cri);

		for (int i = 0; i < list.size(); i++) {
			Ins_DeviceUseTrend trend = list.get(i);
			map.put(trend.getUnitId(), trend);
		}

		return map;
	}
	
	/**
	 * 批量插入
	 */
	@Override
	public void insertListHistory(List<Ins_DeviceFacilityHistory> list) {
		dao().fastInsert(list);

	}

	/**
	 * 批量更新
	 */
	@Override
	public void updateListHistory(List<Ins_DeviceFacilityHistory> list) {
		for (int i = 0; i < list.size(); i++) {
			dao().update(list.get(i));
		}
	}

	
	/**
	 * 批量插入
	 */
	@Override
	public void insertListUseTrend(List<Ins_DeviceUseTrend> list) {
		dao().fastInsert(list);

	}

	/**
	 * 批量更新
	 */
	@Override
	public void updateListUseTrend(List<Ins_DeviceUseTrend> list) {
		for (int i = 0; i < list.size(); i++) {
			dao().update(list.get(i));
		}
	}
	
	/**
	 * 首页柱状图展示
	 */
	@Override
	public JSONObject getHomeUnitHistory(String unitId, String time) {
		// 查询该单位某一天的数据
		Sql sql = Sqls.create(
				"SELECT * from ins_device_facility_history WHERE date_format(date,'%Y-%m-%d')=date_format(@time,'%Y-%m-%d') AND unitId=@unitId");
		sql.setParam("time", time);
		sql.setParam("unitId", unitId);
		Entity<Ins_DeviceFacilityHistory> entity = dao().getEntity(Ins_DeviceFacilityHistory.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		Ins_DeviceFacilityHistory history = sql.getObject(Ins_DeviceFacilityHistory.class);
		String data = "{}";
		if (history != null) {
			data = history.getHistoryData();
		}

		boolean today = DateUtils.isToday(time + " 00:00:00");
		String curMinute = "";
		if (today) {
			curMinute = DateUtils.getCurHourMinute();
		}

		Map<String, String> map = Json.fromJsonAsMap(String.class, data);
		StringBuilder collectStr = new StringBuilder();
		StringBuilder powerOnStr = new StringBuilder();
		for (int i = 0; i < 24; i++) {
			String n = "0";
			String m = "0";
			String numStr = fillNum(i);

			if ((today && DateUtils.compareMinute(curMinute, numStr + ":00")) || !today) {
				if (map.containsKey(numStr)) {
					String numstr = map.get(numStr);
					String[] strarr = numstr.split("/");
					n = strarr[0];
					m = strarr[1];
				}

				powerOnStr.append(n).append(","); // 开机数
				collectStr.append(m).append(","); // 接入数
			}

		}
		JSONObject jobj = new JSONObject();
		jobj.put("powerOnData", StringUtils.strip(powerOnStr.toString(), ","));
		jobj.put("collectStrData", StringUtils.strip(collectStr.toString(), ","));
		return jobj;
	}

	
	/**
	 * 首页24小时使用趋势图数据获取
	 */
	@Override
	public JSONObject getHomeUseTrend(String unitId, String time) {
		// 查询该单位某一天的数据
		Sql sql = Sqls.create(
				"SELECT * from ins_device_use_trend WHERE date_format(date,'%Y-%m-%d')=date_format(@time,'%Y-%m-%d') AND unitId=@unitId");
		sql.setParam("time", time);
		sql.setParam("unitId", unitId);
		Entity<Ins_DeviceFacilityHistory> entity = dao().getEntity(Ins_DeviceFacilityHistory.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		Ins_DeviceFacilityHistory history = sql.getObject(Ins_DeviceFacilityHistory.class);
		String data = "{}";
		if (history != null) {
			data = history.getHistoryData();
		}

		boolean today = DateUtils.isToday(time + " 00:00:00");
		String curMinute = "";
		if (today) {
			curMinute = DateUtils.getCurHourMinute();
		}

		Map<String, String> map = Json.fromJsonAsMap(String.class, data);
		StringBuilder powerOnStr = new StringBuilder();
		for (int i = 0; i < 24; i++) {
			String n = "0";
			String numStr = fillNum(i);

			if ((today && DateUtils.compareMinute(curMinute, numStr + ":00")) || !today) {
				if (map.containsKey(numStr)) {
					String numstr = map.get(numStr);
					String[] strarr = numstr.split("/");
					n = strarr[0];
				}

				powerOnStr.append(n).append(","); // 开机数
			}

		}
		JSONObject jobj = new JSONObject();
		jobj.put("powerOnData", StringUtils.strip(powerOnStr.toString(), ","));
		return jobj;
	}
	
	
	/**
	 * 数字补零
	 * 
	 * @param i
	 * @return
	 */
	public String fillNum(int i) {
		String str = "";
		if (i < 10) {
			str = "0" + i;
		} else {
			str = String.valueOf(i);
		}

		return str;
	}

	/**
	 * 获取顶级下所有单位近六月的平均使用时长,并且map中带月份
	 */
	@Override
	public List<Map<String, String>> getUseTimeNum(String unitIds) {
		Sql sql = Sqls.create(
				"SELECT IF(FORMAT((avg(duration+IFNULL(offLineTime,0))/(30*8*60))*100,2)>100,100,IF(FORMAT((avg(duration+IFNULL(offLineTime,0))/(30*8*60))*100,2)=0.00,0,FORMAT((avg(duration+IFNULL(offLineTime,0))/(30*8*60))*100,2))) as durationRate ,date_format(`month`, '%Y-%m') as time FROM	ins_device_facility a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode WHERE	pid = 0 AND  b.borrowDepart in($unitIds) AND  a.month>date_sub(now(),interval 6 month) GROUP BY   time ");
		sql.setVar("unitIds", unitIds);

		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				Map<String, String> userTimeMap = new HashMap<String, String>();
				while (rs.next()) {
					userTimeMap.put(rs.getString("time"), rs.getString("durationRate"));
				}
				list.add(userTimeMap);
				return list;
			}
		});

		Map map = dao().execute(sql).getList(Map.class).get(0);
		List<Map<String, String>> list = converteDateAndData(map);

		return list;
	}

	/**
	 * 封装首页需要参数
	 * 
	 * @param map
	 */
	public List<Map<String, String>> converteDateAndData(Map<String, String> map) {

		List<String> monthCur = DateUtils.getSixMonthCur();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < monthCur.size(); i++) {
			Map<String, String> reMap = new HashMap<String, String>();
			String month = monthCur.get(i);
			String rate = map.get(month);
			if (StringUtils.isBlank(rate)) {
				rate = "0";
			}
			reMap.put("month", month);
			reMap.put("rate", rate);
			reMap.put("class", "myclass" + i);
			list.add(reMap);
		}

		return list;
	}

	/**
	 * 查询单位下受监控的总台数
	 */
	@Override
	public Map<String, String> getUnitDeviceNum(String unitIds) {
		Sql sql = Sqls.create(
				"SELECT count(*) as num from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode WHERE b.borrowDepart in ($unitIds) ;");
		sql.setVar("unitIds", unitIds);
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				Map<String, String> collectNumMap = new HashMap<String, String>();
				collectNumMap.put("collectNum", "0");
				while (rs.next()) {
					collectNumMap.put("collectNum", rs.getString("num"));
				}
				list.add(collectNumMap);
				return list;
			}
		});

		return dao().execute(sql).getList(Map.class).get(0);
	}

	/**
	 * 获取单位与设备关系
	 */
	@Override
	public Map<String, List<String>> getUnitDevice() {
		Sql sql = Sqls.create(
				"SELECT b.borrowDepart,a.deviceCode from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode=b.deviceCode ");
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, List<String>>> list = new ArrayList<Map<String, List<String>>>();
				Map<String, List<String>> unitDeviceMap = new HashMap<String, List<String>>();
				while (rs.next()) {
					List<String> deviceList = unitDeviceMap.get(rs.getString("borrowDepart"));
					if (deviceList == null) {
						deviceList = new ArrayList<String>();
					}
					deviceList.add(rs.getString("deviceCode"));
					unitDeviceMap.put(rs.getString("borrowDepart"), deviceList);
				}
				list.add(unitDeviceMap);
				return list;
			}
		});

		return dao().execute(sql).getList(Map.class).get(0);
	}

	/**
	 * 转换为顶级单位与设备列表关系 顶级与子级关系Map 单位与设备Map
	 * 
	 * @return
	 */
	@Override
	public Map<String, List<String>> converteMap(Map<String, List<String>> tMap,
			Map<String, List<String>> unitDeviceMap) {
		// 存储 顶级与设备列表关系
		HashMap<String, List<String>> tdMap = new HashMap<String, List<String>>();

		// 遍历tpMap
		Iterator<Entry<String, List<String>>> it = tMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<String>> entry = it.next();
			// 顶级id
			String topUnitId = entry.getKey();
			List<String> deviceList = tdMap.get(topUnitId);
			if (deviceList == null) {
				deviceList = new ArrayList<String>();
			}

			// 子级列表
			List<String> list = entry.getValue();
			list.add(topUnitId);
			for (int i = 0; i < list.size(); i++) {// 累加
				String childId = list.get(i);
				List<String> childDeivceList = unitDeviceMap.get(childId);
				if (childDeivceList != null) {
					deviceList.addAll(childDeivceList);
				}
			}

			tdMap.put(topUnitId, deviceList);
		}

		return tdMap;
	}

	/**
	 * 获取今日功率历史记录
	 */
	@Override
	public Map<String, Ins_DevicePower> getTodayUnitPower(String unitId) {

		Criteria cri = Cnd.cri();
		if (StringUtils.isNotBlank(unitId)) {
			cri.where().and("unitId", "=", unitId);
		}
		cri.where().and("DATEDIFF(date,NOW())", "=", 0);// 今天的数据
		Map<String, Ins_DevicePower> map = new HashMap<String, Ins_DevicePower>();
		List<Ins_DevicePower> list = dao().query(Ins_DevicePower.class, cri);

		for (int i = 0; i < list.size(); i++) {
			Ins_DevicePower history = list.get(i);
			map.put(history.getUnitId(), history);
		}

		return map;
	}

	/**
	 *
	 * 插入或更新单位功率
	 * @param insertList
	 * @param updateList
	 */

	@Override
	public void insertOrUpdateUnitPower(List<Ins_DevicePower> insertList, List<Ins_DevicePower> updateList) {
		for (int i = 0; i < updateList.size(); i++) {
			dao().update(updateList.get(i));
		}

		dao().fastInsert(insertList);

	}

	/**
	 * 获取每五分钟的单位功率数据
	 */

	@Override
	public Map<String, String> getUnitPower(String topUnitId, String time) {
		Sql sql = Sqls.create(
				"SELECT * from ins_device_power WHERE date_format(date,'%Y-%m-%d')=date_format(@time,'%Y-%m-%d') AND unitId=@unitId");
		sql.setParam("time", time);
		sql.setParam("unitId", topUnitId);

		Entity<Ins_DevicePower> entity = dao().getEntity(Ins_DevicePower.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		Ins_DevicePower history = sql.getObject(Ins_DevicePower.class);
		String data = "{}";
		if (history != null) {
			data = history.getHistoryData();
		}
		Map<String, String> map = Json.fromJsonAsMap(String.class, data);
		StringBuilder dateTime = new StringBuilder();
		StringBuilder powerStr = new StringBuilder();

		List<String> fiveMinute = DateUtils.getFiveMinute();

		String curMinute = "";

		boolean today = DateUtils.isToday(time);
		if (today) {
			curMinute = DateUtils.getCurHourMinute();
		}

		for (int i = 0; i < fiveMinute.size(); i++) {
			String n = "0";
			String numStr = fiveMinute.get(i);
			if (map.containsKey(numStr)) {
				n = map.get(numStr);
			}

			if (today) {// 今日只装比当前时间点小的数据
				if (DateUtils.compareMinute(curMinute, numStr)) {
					powerStr.append(n).append(","); // 功率
				}
			} else {// 往日全部装
				powerStr.append(n).append(","); // 功率
			}

			dateTime.append(numStr).append(","); // 时间

		}

		map.put("powerData", StringUtils.strip(powerStr.toString(), ","));
		map.put("dateData", StringUtils.strip(dateTime.toString(), ","));

		return map;
	}

	/**
	 * 查询该单位下设备列表
	 */
	@Override
	public NutMap getUnitDeviceList(String unitIds, String assettype, String deviceState, int pageNo, int pageSize) {

		String mysql = "";
		if (StringUtils.isNotBlank(assettype) || StringUtils.isNotBlank(deviceState)) {
			if (StringUtils.isNotBlank(deviceState) && StringUtils.isNotBlank(assettype)) {// 筛选2个条件
				if (Integer.parseInt(deviceState) == 2) {
					mysql = "SELECT a.deviceCode,b.deviceName,b.deviceVersion,b.assetType from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  LEFT JOIN ins_device_state c ON b.deviceCode = c.deviceCode WHERE b.borrowDepart in ($unitIds) and b.assettype =($assettype) and c.state =1";

				} else {
					mysql = "SELECT a.deviceCode,b.deviceName,b.deviceVersion,b.assetType from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  LEFT JOIN ins_device_state c ON b.deviceCode = c.deviceCode WHERE b.borrowDepart in ($unitIds) and b.assettype =($assettype) and c.powerState =($deviceState)";
				}

			} else if (StringUtils.isNotBlank(deviceState) && StringUtils.isEmpty(assettype)) {// 筛选开关机
				if (Integer.parseInt(deviceState) == 2) {
					mysql = "SELECT a.deviceCode,b.deviceName,b.deviceVersion,b.assetType from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  LEFT JOIN ins_device_state c ON b.deviceCode = c.deviceCode WHERE b.borrowDepart in ($unitIds)  and c.state =1";
				} else {
					mysql = "SELECT a.deviceCode,b.deviceName,b.deviceVersion,b.assetType from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  LEFT JOIN ins_device_state c ON b.deviceCode = c.deviceCode WHERE b.borrowDepart in ($unitIds) and c.powerState =($deviceState)";
				}
			} else if (StringUtils.isEmpty(deviceState) && StringUtils.isNotBlank(assettype)) {// 筛选资产类型
				mysql = "SELECT a.deviceCode,b.deviceName,b.deviceVersion,b.assetType from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode   WHERE b.borrowDepart in ($unitIds) and b.assettype =($assettype) ";
			}

		} else {// 分页查询全部
			mysql = "SELECT a.deviceCode,b.deviceName,b.deviceVersion,b.assetType from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  WHERE b.borrowDepart in ($unitIds) ";
		}

		Sql sql = Sqls.create(mysql);
		if (StringUtils.isNotBlank(assettype))
			sql.setVar("assettype", assettype);
		if (StringUtils.isNotBlank(deviceState) && 2 != Integer.parseInt(deviceState))
			sql.setVar("deviceState", deviceState);

		sql.setVar("unitIds", unitIds);
		/* NutMap nmap = data(18, pageNo, pageNo, mysql, mysql); */
		NutMap re = new NutMap();
		Pager pager = new OffsetPager((pageNo - 1) * pageSize, pageSize * pageNo);
		pager.setRecordCount((int) Daos.queryCount(this.dao(), sql));// 记录数需手动设置
		sql.setPager(pager);
		sql.setCallback(Sqls.callback.records());
		this.dao().execute(sql);
		re.put("recordsFiltered", pager.getRecordCount());
		re.put("data", sql.getList(Record.class));
		return re;
		/*
		 * Sql sql = Sqls.create(
		 * "SELECT a.deviceCode,b.deviceName,b.deviceVersion,b.assetType from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  WHERE b.borrowDepart in ($unitIds) limit (($pageNo)-1)*18,($pageNo)*18"
		 * ) ; sql.setVar("unitIds", unitIds) ; sql.setVar("pageNo", pageNo) ;
		 * ResultSet rs = (ResultSet)dao().execute(sql).getResult(); List<Map>
		 * list = new ArrayList<Map>() ; try { while (rs.next()) {
		 * Map<String,Object> map = new HashMap<String,Object>() ;
		 * map.put("deviceCode", rs.getString("deviceCode")) ;
		 * map.put("deviceName", rs.getString("deviceName")) ;
		 * map.put("deviceVersion", rs.getString("deviceVersion")) ;
		 * map.put("assetType", rs.getInt("assetType")) ; } }catch (Exception e)
		 * { e.printStackTrace(); }
		 * 
		 * Sql sql1 = Sqls.create(
		 * "SELECT count(a.deviceCode) from ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  WHERE b.borrowDepart in ($unitIds) "
		 * ) ; sql1.setVar("unitIds", unitIds) ; sql.setCondition(cnd);
		 * dao().count(DeviceForm.class, (Condition) sql1);
		 * 
		 * 
		 * return list;
		 */
	}

	public static void main(String[] args) throws ParseException {
		float   a   =   0/78f;   
		 float   b   =   (float)(Math.round(a*100))/100;
		
		
		System.out.println(b);
	}
	
	/**
	 * 获取前一个小时内的开机数
	 */
	@Override
	public Map<String, Integer> getLastHourPowerOnNum() {
		Sql sql = Sqls.create("SELECT  b.borrowDepart,  COUNT(DISTINCT(a.deviceCode)) AS num FROM  ins_switching_flow a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode WHERE (a.userTime IS NULL)  OR (a.userTime IS NOT NULL AND a.powerOnTime BETWEEN DATE_FORMAT(DATE_ADD(Now(),INTERVAL -1 hour), '%Y-%m-%d %H:%00:%00' ) and DATE_FORMAT( Now(), '%Y-%m-%d %H:%00:%00' ) ) OR (a.userTime is NOT NULL AND if(a.powerOffTime is NOT NULL ,a.powerOffTime,a.outLineTime) BETWEEN DATE_FORMAT(DATE_ADD(Now(),INTERVAL -1 hour), '%Y-%m-%d %H:%00:%00' ) and DATE_FORMAT( Now(), '%Y-%m-%d %H:%00:%00' ) ) GROUP BY	b.borrowDepart;");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>() ;
				Map<String, Integer> map = new HashMap<String ,Integer>() ;
				while (rs.next()) {
						map.put(rs.getString("borrowDepart"), rs.getInt("num")) ;
				}
				list.add(map);
				return list;
			}

			
		});
		return dao().execute(sql).getList(Map.class).get(0);
	}
	
	/**
	 * 获取当月设备各类使用率
	 */
	@Override
	public List<Map> getCurDeviceUseRate(String unitIds,int page,int rows) {
		Sql sql = Sqls.create(
				"SELECT c.`name` as name , IFNULL(d.duration,0) as duration from sys_dict c LEFT JOIN ( SELECT	avg(	duration + IFNULL(offLineTime, 0)	) AS duration,	left(a.deviceCode,5) as code FROM	ins_device_facility a,	ins_device_info b WHERE	a.deviceCode = b.deviceCode AND a.pid = 0 AND b.borrowDepart IN (	$unitIds ) AND date_format(a.MONTH, '%Y-%m') = date_format(now(), '%Y-%m') GROUP BY	left(a.deviceCode,5) ) d ON c.`code` = d.code WHERE c.code in ('W0804','W0604','W0812','D0901','W0702') " +
						"UNION " +
						"SELECT '精大贵稀' AS NAME, avg( a.duration + IFNULL(a.offLineTime, 0)) AS duration FROM ins_device_facility a, ins_assets_info c WHERE a.deviceCode = c.assetCode AND c.instrumentCategory IN ('1', '3') AND a.pid = 0 AND c.borrowDepart IN ($unitIds) AND date_format(a. MONTH, '%Y-%m') = date_format(now(), '%Y-%m')"+
						"ORDER BY LENGTH(NAME) asc limit @page ,@rows"
		);
		sql.setVar("unitIds", unitIds);
		sql.setParam("page", page);
		sql.setParam("rows", rows);
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				int day = DateUtils.getWorkDay(0, 0);
				float base=day*8*60 ;
				DecimalFormat df=new DecimalFormat("#.00");
				int s=0 ;
				while (rs.next()) {
					Map<String, String> userTimeMap = new HashMap<String, String>();
					float dur = rs.getFloat("duration") ;
					float i = (dur/base)*100 ;
					i=i>100?100:i ;
					String str = String.format("%.2f", i) ;
					str=removeZero(str);
					userTimeMap.put("name", rs.getString("name"));
					userTimeMap.put("rate", str);
					userTimeMap.put("class", "myclass" + s);
					s++ ;
					list.add(userTimeMap);
				}
				return list;
			}
		});

		List<Map> list  = dao().execute(sql).getList(Map.class);

		return list;
	}
	public String removeZero(String str){
	    if(str.indexOf(".") > 0){
	     //正则表达
	           str = str.replaceAll("0+?$", "");//去掉后面无用的零
	           str = str.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
	     }
	    return str ;
	}
	
	 public static Map<String, int[]> sortMapByValue(Map<String, int[]> oriMap) {
	        if (oriMap == null || oriMap.isEmpty()) {
	            return null;
	        }
	        Map<String, int[]> sortedMap = new LinkedHashMap<String, int[]>();
	        List<Map.Entry<String, int[]>> entryList = new ArrayList<Map.Entry<String, int[]>>(
	                oriMap.entrySet());
	        Collections.sort(entryList, new MapValueComparator());

	        Iterator<Map.Entry<String, int[]>> iter = entryList.iterator();
	        Map.Entry<String, int[]> tmpEntry = null;
	        while (iter.hasNext()) {
	            tmpEntry = iter.next();
	            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
	        }
	        return sortedMap;
	    }
	 /**
		 * 根据传入单位id,查询该单位最近七天日使用时长大于60分钟的设备数量
		 * @param unitIds
		 * @return
		 */
	@Override
	public Map getHomePowerOnCountByDepartFrDayDuration(String unitIds) {
		Map<String, int[]> map = new HashMap<String, int[]>();
		try {
			Sql sql = Sqls.create(
					"SELECT b.borrowDepart , COUNT(a.id) AS powerOnNum from ins_device_facility_day a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  WHERE a.duration>=60 AND a.`day`>=date_sub(curdate(), INTERVAL 7 DAY) $var GROUP BY b.borrowDepart");
			if(StringUtils.isNotBlank(unitIds)){
				Sql sql2 = Sqls.create(" and b.borrowDepart in ($var) ") ;
				sql2.setVar("var", unitIds) ;
				sql.setVar("var", sql2) ;
			}

			// 设置回调函数
			sql = sql.setCallback(new SqlCallback() {

				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
					Map<String, Integer> map = new HashMap<String, Integer>();
					while (rs.next()) {
						String dpcode = rs.getString("borrowDepart") ;
						int num = rs.getInt("powerOnNum");
						map.put(dpcode, num) ;	
					}
					list.add(map);
					return list;
				}

			});

			map = dao().execute(sql).getList(Map.class).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	/**
	 * 查询每个单位的接入数量
	 */
	@Override
	public Map<String, Integer> getUnitLinkCollectNum(String unitIds) {
		Sql sql = Sqls.create(
				"SELECT b.borrowDepart ,count(*) as num FROM ins_collect a  LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode  $var GROUP BY b.borrowDepart");
		if(StringUtils.isNotBlank(unitIds)){
			Sql sql2 = Sqls.create(" where b.borrowDepart in ($var) ") ;
			sql2.setVar("var", unitIds) ;
			sql.setVar("var", sql2) ;
		}
		
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
				Map<String, Integer> map = new HashMap<String, Integer>();
				while (rs.next()) {
					map.put(rs.getString("borrowDepart"), rs.getInt("num"));
				}
				list.add(map);
				return list;
			}

		});
		return dao().execute(sql).getList(Map.class).get(0);
	}
	
	
}
class MapValueComparator implements Comparator<Map.Entry<String, int[]>> {
    @Override
    public int compare(Entry<String, int[]> me1, Entry<String, int[]> me2) {
    	int[] mev1 = me1.getValue() ;
    	int[] mev2 = me2.getValue() ;
    	int m = mev1[0]+mev1[1]+mev1[2] ;
    	int n = mev2[0]+mev2[1]+mev2[2] ;
    	
        return n-m;
    }
}