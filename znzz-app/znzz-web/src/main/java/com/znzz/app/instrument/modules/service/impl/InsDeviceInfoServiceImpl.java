package com.znzz.app.instrument.modules.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.service.InsDeviceInfoService;
import com.znzz.framework.base.service.BaseServiceImpl;
@IocBean(args = { "refer:dao" })
public class InsDeviceInfoServiceImpl extends BaseServiceImpl<Ins_DeviceInfo> implements InsDeviceInfoService{

	public InsDeviceInfoServiceImpl(Dao dao) {
		super(dao);
	}

	@Override
	public void insertList(List<Ins_DeviceInfo> deviceInfoList) {
		dao().insert(deviceInfoList);
	}

	@Override
	public void saveOrUpdate(List<Ins_DeviceInfo> deviceInfoList) {

		for (Ins_DeviceInfo deviceInfo : deviceInfoList) {
			//id不为空进行更新操作
			if(deviceInfo.getId() != null){
				dao().updateIgnoreNull(deviceInfo);
			}else {//进行插入操作
				dao().insert(deviceInfo);
			}
		}
	}

	@Override
	public Ins_DeviceInfo getDeviceInfo(String deviceCode) {
		Ins_DeviceInfo info = dao().fetch(Ins_DeviceInfo.class, Cnd.where("deviceCode", "=", deviceCode));
		return info;
	}
	/**
	 * 查询获取使用单位和责任人
	 */
	@Override
	public Map<String, String> getDepartAndChargePerson(String deviceCode) {
		Sql sql = Sqls.create("SELECT IFNULL(b.`name`,'') as name ,IFNULL(c.username,'') as username from ins_device_info a LEFT JOIN sys_unit b ON a.borrowDepart = b.id LEFT JOIN sys_user c ON a.chargePerson = c.id WHERE a.deviceCode = @deviceCode") ;
		sql.setParam("deviceCode", deviceCode) ;
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String,String>> list = new ArrayList<Map<String,String>>();
				Map<String, String> map = new HashMap<String,String>() ;
				String unitName = "" ;
				String userName = "" ;
				while (rs.next()) {
					unitName =  rs.getString("name") ;
				 	userName =  rs.getString("username") ;
				}
				map.put("unitName", unitName) ;
				map.put("userName", userName) ;
				list.add(map);
				
				return list;
			}
		});
		dao().execute(sql) ;
		Map map = sql.getList(Map.class).get(0) ;
		return map;
	}

	@Override
	public void updateList(List<Ins_DeviceInfo> deviceList) {
		for (Ins_DeviceInfo deviceInfo : deviceList) {
			if (deviceInfo.getId() != null){
				dao().updateIgnoreNull(deviceInfo);
			}
		}
	}
}