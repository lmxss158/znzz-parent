package com.znzz.app.instrument.modules.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;

import com.znzz.app.instrument.modules.models.Ins_SwitchingFlow;
import com.znzz.app.instrument.modules.service.InsSwitchingFlowService;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.util.DateUtils;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.SwitchingFlowForm;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
/**
 * 状态履历实现类
 * @author wangqiang
 *
 */
@IocBean(args = {"refer:dao"})
public class InsSwitchingFlowServiceImpl extends BaseServiceImpl implements InsSwitchingFlowService{
	
	@Inject
	SysUnitService unitService ;
	
	/**
	 * 构造方法
	 * @param dao
	 */
	public InsSwitchingFlowServiceImpl(Dao dao) {
		super(dao);
	}
    /**
     * 获取列表
     */
	@Override
	public NutMap findSwitchFlowList(SwitchingFlowForm form,List<DataTableOrder> orders, List<DataTableColumn> columns) {
		
		Sql sql = Sqls.create("SELECT a.deviceCode, b.deviceName,b.deviceVersion,a.powerOnTime,a.powerOffTime,a.outLineTime,a.userTime,a.locationInfo,a.outField,c.name,d.username AS chargePerson FROM ins_switching_flow a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode LEFT JOIN sys_unit c ON b.borrowDepart = c.id LEFT JOIN sys_user d ON d.id = b.chargePerson $condition $var $order");
		Sql countSql = Sqls.create("SELECT count(*) FROM ins_switching_flow a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode LEFT JOIN sys_unit c ON b.borrowDepart = c.id LEFT JOIN sys_user d ON d.id = b.chargePerson $condition $var ");
		Criteria cri = Cnd.cri() ;
		if(StringUtils.isNoneBlank(form.getBorrowDepart())){
			String childList = unitService.getChildList(form.getBorrowDepart()) ;
			cri.where().and("b.borrowDepart", "in", childList);
		}
		if(StringUtils.isNoneBlank(form.getChargePerson())){
			cri.where().and("b.chargePerson", "=", form.getChargePerson()) ;
		}
		if(form.getOutField()!=null){
			cri.where().and("a.outField", "=", form.getOutField()) ;
		}
		if(StringUtils.isNoneBlank(form.getPowerOnTime())){
			String powerOnTime = form.getPowerOnTime() ;
			String[] time = powerOnTime.split("-") ;
			cri.where().andBetween("a.powerOnTime", time[0], time[1]) ;
		}
		if(StringUtils.isNoneBlank(form.getPowerOffTime())){
			String powerOffTime = form.getPowerOffTime();
			String[] time = powerOffTime.split("-") ;
			cri.where().andBetween("a.powerOffTime", time[0], time[1]) ;
		}
		
		if(StringUtils.isNotBlank(form.getDeviceCode())){
			String code = form.getDeviceCode() ;
			
			String str = "%"+code.trim()+"%" ;
			String par="" ;
			if(StringUtils.isBlank(cri.toString())){
				par="where" ;
			}else{
				par="and" ;
			}
			Sql sql2 = Sqls.create(" $par (a.deviceCode like @str or b.deviceName like @str or b.deviceVersion like @str)") ;
			sql2.setParam("str", str) ;
			sql2.setVar("par", par) ;
			sql.setVar("var", sql2) ;
			countSql.setVar("var",sql2);
		}

		if (orders != null && orders.size() > 0) {
			Cnd  cri2 = Cnd.NEW() ;
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cri2.getOrderBy().orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
            sql.setVar("order", cri2) ;
       }
		sql.setCondition(cri);
		countSql.setCondition(cri);
		NutMap re = dataCountByCustom(form.getLength(),form.getStart(),form.getDraw(),countSql,sql) ;
		
//		Pager pager = dao().createPager(form.getStart(), form.getLength()) ;
//		
//		 
//		NutMap re = new NutMap();
//		re.put("recordsFiltered", Daos.queryCount(dao(), sql));
//		sql.setPager(pager) ;
//		sql.setCallback(new SqlCallback() {
//			@Override
//			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
//				LinkedList<Map<String,Object>> list = new LinkedList<Map<String , Object>>() ;
//				ResultSetMetaData rsmd=rs.getMetaData();
//				int count = rsmd.getColumnCount();
//				String key=null;
//				while(rs.next()){
//					Map map = new HashMap<String,Object>();
//					for(int i=1;i<=count;i++){
//						key = rsmd.getColumnName(i).toLowerCase();
//						map.put(key,rs.getObject(i));
//					}
//					list.add(map);
//				}
//				return list ;
//			}
//		});
//        re.put("data", dao().execute(sql).getList(Map.class));
//        re.put("draw", form.getDraw());
//        re.put("recordsTotal", form.getLength());
		return re;
	}
    /**
     * 根据deviceCode获取设备状态履历
     */
	@Override
	public NutMap findSwitchFlowByDeviceCode(SwitchingFlowForm form) {
		
		NutMap map = new NutMap();
		long count=0 ;
		List<Record> res =new ArrayList<Record>() ;
		if(StringUtils.isNoneBlank(form.getDeviceCode())){
			Sql sql = Sqls.queryRecord("SELECT a.deviceCode,b.deviceName,b.deviceVersion,a.powerOnTime,a.powerOffTime,a.outLineTime,a.userTime,a.locationInfo from ins_switching_flow a , ins_device_info b WHERE a.deviceCode = b.deviceCode AND a.deviceCode=@deviceCode order by a.powerOnTime desc") ;
			sql.setParam("deviceCode", form.getDeviceCode());
//			Pager pager = new Pager(form.getStart(), form.getLength()) ;
//			sql.setPager(pager);
//			dao().execute(sql);
//			res= sql.getList(Record.class);
//			pager.setRecordCount((int) Daos.queryCount(dao(), sql));
//			count = Daos.queryCount(dao(), sql) ;
			map = data(form.getLength(),form.getStart(),form.getDraw(),sql,sql) ;
			
		}else{
			map.put("recordsFiltered", count);
			map.put("data", res);
			map.put("draw", form.getDraw());
			map.put("recordsTotal", form.getLength());
		}
	   
		
		
		return map;
	}
     /**
      * 添加一条设备装填履历
      */
	@Override
	public void insertSwitchFlow(Ins_SwitchingFlow form) {
		dao().insert(form) ;
	}
	/**
	 * 更新一条设备状态履历
	 */
	@Override
	public void updateSwitchFlow(Ins_SwitchingFlow form) {
		dao().update(form) ;
	}
    /**
     * 获取最新的一条设备装填履历
     */
	@Override
	public List<Ins_SwitchingFlow> fetchSwitchFlow(String deviceCode) {
		
		Sql sql = Sqls.create("SELECT * from ins_switching_flow WHERE deviceCode=@deviceCode AND userTime is null ORDER BY id DESC ") ;
		sql.setParam("deviceCode", deviceCode) ;
		Entity<Ins_SwitchingFlow> entity = dao().getEntity(Ins_SwitchingFlow.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
		return sql.getList(Ins_SwitchingFlow.class);
	}
    /**
     * 根据ip获取设备的位置
     */
	@Override
	public String fetchLocationInfo(String ip) {
		
		Sql sql = Sqls.create("SELECT gatewayLocation from ins_gateway WHERE ip=@ip LIMIT 1") ;
		sql.setParam("ip",ip) ;
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

					@Override
					public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
						List<String> list = new ArrayList<>();
						while (rs.next()) {
							list.add(rs.getString("gatewayLocation"));
						}
						return list;
					}
				});
		List<String> list = dao().execute(sql).getList(String.class); 
		String localstr="" ;
		if(list.size()>0){
			localstr = list.get(0) ;
		}
		return localstr;
	}
     /**
      * 删除设备状态履历
      */
	@Override
	public int delSwitchFlow(String[] deviceCode) {
		return this.dao().clear(Ins_SwitchingFlow.class, Cnd.where("deviceCode", "in", deviceCode));
	}
	
	/**
	 * 获取当月的履历记录
	 */
	@Override
	public List<Ins_SwitchingFlow> findCurMonthList() {
		
	   Sql sql = Sqls.create("SELECT * FROM	ins_switching_flow WHERE	DATE_FORMAT(powerOffTime, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') OR DATE_FORMAT(outLineTime, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') OR  userTime is NULL") ;
	   sql.setCallback(Sqls.callback.entities());
	   sql.setEntity(dao().getEntity(Ins_SwitchingFlow.class));
	   dao().execute(sql);
	   List<Ins_SwitchingFlow> list = sql.getList(Ins_SwitchingFlow.class);
		
		return list;
	}
	
	/**
	 * 查询近三十天的履历记录
	 */
	@Override
	public List<Ins_SwitchingFlow> findCurThrityDaysList() {
		Sql sql = Sqls.create("SELECT * FROM	ins_switching_flow WHERE	(powerOffTime >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) OR outLineTime >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) OR userTime IS NULL) AND deviceCode in (SELECT assetCode from ins_assets_unit)") ;
		sql.setCallback(Sqls.callback.entities());
	    sql.setEntity(dao().getEntity(Ins_SwitchingFlow.class));
	    dao().execute(sql);
	    List<Ins_SwitchingFlow> list = sql.getList(Ins_SwitchingFlow.class);
		return list;
	}
	
	/**
	 * 查询昨日履历记录
	 */
	@Override
	public List<Ins_SwitchingFlow> findYesterDayList() {
		
		Sql sql = Sqls.create("SELECT * from ins_switching_flow WHERE (userTime IS NULL  or  (powerOffTime > date_sub(CURDATE(),interval 1 day) OR outLineTime > date_sub(CURDATE(),interval 1 day))) AND powerOnTime < DATE_ADD(str_to_date(DATE_FORMAT(NOW(),'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL -1 SECOND)") ;
		sql.setCallback(Sqls.callback.entities());
	    sql.setEntity(dao().getEntity(Ins_SwitchingFlow.class));
	    dao().execute(sql);
	    List<Ins_SwitchingFlow> list = sql.getList(Ins_SwitchingFlow.class);
		return list;
		 
	}
	/**
	 * 设置设备履历结束
	 */
	@Override
	public void setSwitchFlowEnd(String deviceCode) {
		Sql sql = Sqls.create("SELECT * from ins_switching_flow WHERE deviceCode =@deviceCode AND userTime is null") ;
		sql.setParam("deviceCode", deviceCode) ;
		sql.setCallback(Sqls.callback.entities());
	    sql.setEntity(dao().getEntity(Ins_SwitchingFlow.class));
	    dao().execute(sql);
	    List<Ins_SwitchingFlow> list = sql.getList(Ins_SwitchingFlow.class);
	    
	    for(Ins_SwitchingFlow flow :list){
	    	flow.setOutLineTime(new Date());
	    	flow.setUserTime(DateUtils.getTimeHour(flow.getPowerOnTime(),new Date()));
	    	updateSwitchFlow(flow) ;
	    }
	    
	}
	/**
	 * 清除重复的履历数据
	 */
	@Override
	public void clearRepeateFlowData(String deviceCode) {
		Sql sql = Sqls.create("DELETE FROM	ins_switching_flow WHERE deviceCode =@deviceCode AND userTime is null AND id NOT IN (SELECT a.id FROM(SELECT MAX(id) as id FROM ins_switching_flow WHERE deviceCode = @deviceCode AND userTime IS NULL) a);") ;
		sql.setParam("deviceCode", deviceCode) ;
		dao().execute(sql) ;
	}
}
