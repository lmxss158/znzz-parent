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
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;

import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceBcard;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.models.Ins_Gateway;
                  import com.znzz.app.instrument.modules.service.InsGatewayService;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.CollectBindDeviceBean;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 
 * @ClassName: InsbcardServiceImpl   
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fengguoxin
 * @date 2017年9月21日 下午1:52:25   
 */
@IocBean(args = {"refer:dao"})
public class InsbcardServiceImpl extends BaseServiceImpl<Ins_DeviceBcard> implements InsbcardService {

	@Inject
    private RedisService redisService ;
	public InsbcardServiceImpl(Dao dao) {
		super(dao);
	}

	@Override
	public boolean checkGatewayCode(String gatewayCode, Cnd cnd) {
		List<Record> query = dao().query("ins_gateway", cnd.and("gatewayCode", "=", gatewayCode));
		// 如果有重复则返回true
		if (query.size() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void insertList(List<Ins_DeviceBcard> ins_bcardList) {
		// 分别创建存放采集器和设备的list
				List<Ins_DeviceBcard> bcardsInsert = new ArrayList<>();
				List<Ins_DeviceBcard> bcardsUpdate = new ArrayList<>();
		
				// 创建采集器和设备对象
				Ins_DeviceBcard bcard = null;
				
				// 遍历循环放入到各个list当中
				
				for (Ins_DeviceBcard bean : ins_bcardList) {
					bcard = new Ins_DeviceBcard();
					bcard.setBcardCode(bean.getBcardCode());
					bcard.setOrignCode(bean.getOrignCode());
					bcard.setOperateTime(bean.getOperateTime());
		
					Cnd cndBcardCode = Cnd.where("bcardCode","=",bean.getBcardCode());
					Cnd cndOrignCode = Cnd.where("orignCode","=",bean.getOrignCode());
					Ins_DeviceBcard fetchBcode = fetch(cndBcardCode);
					Ins_DeviceBcard fetchCcode = fetch(cndOrignCode);
					if(null!=fetchBcode){//更新
						bcard.setId(fetchBcode.getId());
						update(bcard);
					}else if(null!=fetchCcode){
						bcard.setId(fetchCcode.getId());
						update(bcard);
					}
					else{//插入
						bcardsInsert.add(bcard);
					}
				}
				
				// 进行批量插入
				dao().insert(bcardsInsert);
		
		
	}
    //检查网管是否存在
	@Override
	public boolean checkbcardCode( String bcardCode1, String bcardCode, Cnd cnd) { 
		
		
		
		Sql sql = Sqls.create("SELECT b.bcardCode FROM (SELECT * from ins_device_bcard a WHERE  a.bcardCode != '"+bcardCode1+"') b  WHERE b.bcardCode = '"+bcardCode+"';") ;
				sql = sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("bcardCode"));
				}
				return list;
				}
				});

		 List<String> list = dao().execute(sql).getList(String.class);
		 if(list.size()>0&&list!=null){  //集合不为空返回true
			 return true; 
		 }else{
			 return false;
		 }
		
	}

	/**
	 * 获取生产编号所对应的B绑定的设备统一code
	 * @return
	 */
	@Override
	public Map<String, String> getOrignCodeAndDeviceCode() {
		Map<String, String> remap = new HashMap<String,String>() ;
		try {
			if(redisService.exists(Globals.ORIGNDEVICECODE)){
				String data = redisService.get(Globals.ORIGNDEVICECODE) ;
				remap = Json.fromJsonAsMap(String.class, data) ;
			}else{
				remap = queryOrignCodeAndDeviceCode() ;
				if(remap.size()>0){
					redisService.set(Globals.ORIGNDEVICECODE,Json.toJson(remap)) ;
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			remap = queryOrignCodeAndDeviceCode() ;
		}
		
		
		return remap;
	}
	
	/**
	 * 查库 获取生产编号所对应的B绑定的设备统一code
	 * @return
	 */
	public Map<String,String> queryOrignCodeAndDeviceCode(){
		Sql sql = Sqls.create("SELECT a.orignCode,b.deviceCode from ins_device_bcard a INNER JOIN ins_collect b ON a.bcardCode = b.collectCode") ;
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String, String>> list = new ArrayList<Map<String,String>>() ;
				Map<String,String> map = new HashMap<String,String>() ;
				while (rs.next()) {
					map.put(rs.getString("orignCode"),rs.getString("deviceCode")) ;
					map.put(rs.getString("deviceCode"),rs.getString("orignCode")) ;
				}
				list.add(map) ;
				return list;
			}
		});
		Map<String, String> remap = dao().execute(sql).getList(Map.class).get(0) ;
		return remap ;
	}
	
	
	/**
	 * 删除生产编号对应设备统一编号的key
	 */
	@Override
	public void delOrignCodeAndDeviceCodeKey() {
		redisService.del(Globals.ORIGNDEVICECODE) ;
	}
   
	@Override
	public boolean checkorignCode(String orignCode1 ,String orignCode, Cnd cnd) {
		
		
		Sql sql = Sqls.create("SELECT b.orignCode FROM (SELECT * from ins_device_bcard a WHERE  a.orignCode != '"+orignCode1+"') b  WHERE b.orignCode = '"+orignCode+"';") ;
				sql = sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("orignCode"));
				}
				return list;
				}
				});

		 List<String> list = dao().execute(sql).getList(String.class);
		 if(list.size()>0&&list!=null){  //集合不为空返回true
			 return true; 
		 }else{
			 return false;
		 }
	}
		
	@Override
	public boolean checkorignCode_add(String orignCode, Cnd cnd) {
		List<Record> query = dao().query("ins_device_bcard", cnd.and("orignCode", "=", orignCode));
		// 如果有重复则返回true
		   if (query.size() == 0) {
			return true;
			}
			return false;
	}
	
	@Override
	public boolean checkbcardCode_add(String bcardCode, Cnd cnd) { 
		
		List<Record> query = dao().query("ins_device_bcard", cnd.and("bcardCode", "=", bcardCode.trim()));
	// 如果有重复则返回false
	   if (query.size() == 0) {
		return true;
		}
		return false;
	}

	@Override
	public Map<String, String> convertClollectCodeAndDeviceCode(List<CollectBindDeviceBean> list) {
		
		Map<String,String> map = new HashMap<String,String>() ;
		for(CollectBindDeviceBean bean:list){
			String deviceCode = bean.getDeviceCode() ;
			String collectCode = bean.getCollectCode() ;
			Ins_DeviceBcard ins_DeviceBcard = dao().fetch(Ins_DeviceBcard.class, Cnd.where("bcardCode", "=", collectCode)) ;
			String orignCode =null ;
			if(ins_DeviceBcard!=null){
				orignCode=ins_DeviceBcard.getOrignCode() ;
			}
			
			map.put(deviceCode, orignCode) ;
			map.put(orignCode, deviceCode) ;
		}
		
		return map;
	}
}
