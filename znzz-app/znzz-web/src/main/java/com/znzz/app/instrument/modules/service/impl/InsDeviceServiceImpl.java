package com.znzz.app.instrument.modules.service.impl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceState;
import com.znzz.app.instrument.modules.service.InsDeviceService;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 设备管理实现类
 * @author wangqiang
 *
 */
@IocBean(args = {"refer:dao"})
public class InsDeviceServiceImpl extends BaseServiceImpl<Ins_DeviceState> implements InsDeviceService {
	@Inject
	SysUnitService unitService ;
	
	public InsDeviceServiceImpl(Dao dao) {
		super(dao);
	}
	/**
	 * 获取设备列表
	 */
	@Override
	public NutMap findDeviceList(String deviceCode, String borrowDepart, String chargePerson,
			Integer state, Integer outField,Integer powerState ,String location,int page ,int rows,int draw,List<DataTableOrder> orders,List<DataTableColumn> columns) {
		
		if(powerState!=null&&powerState==2){//关机的未知就是离线
			powerState = null ;
			state = 1 ;
		}
		
		Sql sql = Sqls.create("SELECT DISTINCT(a.deviceCode) as deviceCode, b.deviceName ,b.deviceVersion,c.state,c.powerState,b.outField,d.locationInfo as gatewayLocation,e.name,f.username AS chargePerson FROM ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode LEFT JOIN ins_device_state c ON a.deviceCode = c.deviceCode LEFT JOIN ins_assets_info d ON a.deviceCode = d.assetCode LEFT JOIN sys_unit e ON b.borrowDepart = e.id LEFT JOIN sys_user f ON f.id = b.chargePerson  $condition  $having $order");
	   
		Criteria cri = Cnd.cri() ;
		if(StringUtils.isNotBlank(borrowDepart)){
			
			String childList = unitService.getChildList(borrowDepart) ;
			cri.where().and("b.borrowDepart", "in", childList);
		}
		
		if(StringUtils.isNotBlank(chargePerson)){
			cri.where().and("b.chargePerson", "=", chargePerson);
		}
		
		if(state!=null){
			cri.where().and("c.state", "=", state);
		}
		
		if(outField!=null){
			cri.where().and("b.outField", "=", outField);
		}
		
		if(powerState!=null){
			cri.where().and("c.powerState","=",powerState) ;
		}
		
		if(StringUtils.isNotBlank(location)){
			cri.where().andLike("d.locationInfo", location.trim()) ;
		}
		
		if(StringUtils.isNotBlank(cri.toString())){
			sql.setCondition(cri) ;
		}
		
		if(StringUtils.isNotBlank(deviceCode)){//统一编号  , 设备名称  , 设备型号
			SimpleCriteria cri2 = Cnd.cri() ;
			String str = "%"+deviceCode.trim()+"%" ;
			String par ="";
			if(StringUtils.isBlank(cri.toString())){
				par="where" ;
			}else{
				par="and" ;
			}
			Sql sql2 = Sqls.create("$par (a.deviceCode like @str or b.deviceName like @str or b.deviceVersion like @str)") ;
			sql2.setVar("par",par) ;
			sql2.setParam("str", str) ;
			sql.setVar("having", sql2);
		}
		
		if (orders != null && orders.size() > 0) {
			Cnd  cri2 = Cnd.NEW() ;
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cri2.getOrderBy().orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
            sql.setVar("order", cri2) ;
       }
		
		
		NutMap nmap = data(rows,page,draw,sql,sql) ;
		
//		Pager pager = dao().createPager(page, rows) ;
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
//		dao().execute(sql);
//		List<Map> list = sql.getList(Map.class);
//		NutMap nmap = new NutMap();
//		nmap.put("recordsFiltered", Daos.queryCount(dao(), sql));
//        nmap.put("data", list);
//        nmap.put("draw", page);
//        nmap.put("recordsTotal", rows);
		return nmap;
	}
    /**
     * 更新设备状态
     */
	@Override
	public void updateDeviceState(Ins_DeviceState inDeviceState) {
		//更新设备状态
		Sql sql = Sqls.create("INSERT INTO ins_device_state (deviceCode,state,powerState,locationInfo) VALUES(@deviceCode, @state,@powerState,@locationInfo) ON DUPLICATE KEY UPDATE state = @state,powerState=@powerState,locationInfo=@locationInfo");
		sql.setParam("deviceCode",inDeviceState.getDeviceCode()) ;
		sql.setParam("state", inDeviceState.getState());
		sql.setParam("powerState", inDeviceState.getPowerState());
		sql.setParam("locationInfo", inDeviceState.getLocationInfo());
		
		dao().execute(sql) ;
		
	}
    /**
     * 获取监控中的设备
     */
	@Override
	public List<Ins_DeviceState> getDeviceStateList() {
		
		Sql sql = Sqls.create("SELECT a.deviceCode,b.state,b.powerState,b.locationInfo from ins_collect a LEFT JOIN ins_device_state b ON a.deviceCode =b.deviceCode ") ;
		Entity<Ins_DeviceState> entity = dao().getEntity(Ins_DeviceState.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Ins_DeviceState.class);
	}
	 /**
     * 获取监控中的设备（在线且开机状态的设备）
     */
	@Override
	public List<Ins_DeviceState> getBootDeviceStateList() {
		
		Sql sql = Sqls.create("SELECT a.deviceCode,b.state,b.powerState,b.locationInfo from ins_collect a LEFT JOIN ins_device_state b ON a.deviceCode =b.deviceCode where b.state=0 and b.powerState=0 ") ;
		Entity<Ins_DeviceState> entity = dao().getEntity(Ins_DeviceState.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Ins_DeviceState.class);
	}
    /**
     * 删除设备状态
     */
	@Override
	public int delDeviceState(String[] deviceCode) {
		return this.dao().clear(Ins_DeviceState.class, Cnd.where("deviceCode", "in", deviceCode));
	}
	
	
	@Override
	public boolean checkDeviceInfoDepartAndPerson(String deviceCode) {
		
		List<Ins_Collect> query = dao().query(Ins_Collect.class, Cnd.where("deviceCode", "=", deviceCode));
		
		return false;
	}

	
	
}
