package com.znzz.app.instrument.modules.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceBcard;
import com.znzz.app.instrument.modules.models.Ins_DeviceState;
import com.znzz.app.instrument.modules.models.Ins_Gateway;
import com.znzz.app.instrument.modules.service.InsGatewayService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.CollectBindDeviceBean;
import com.znzz.framework.base.service.BaseServiceImpl;



@IocBean(args = {"refer:dao"})
public class InsGatewayServiceImpl extends BaseServiceImpl<Ins_Gateway> implements InsGatewayService {


	@Inject
	ScadaDeviceServcie   scadaDeviceServcie;
		
	public InsGatewayServiceImpl(Dao dao) {
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
	public int deleteByIp(String ip) {
		dao().clear("ins_gateway", Cnd.where("ip", "=", ip));
		return 0;
	}
	@Override
	public String insertList(List<Ins_Gateway> ins_AcardList) {
		
		        String errorStr="" ;
		        String re="" ;
		    	// 创建收集器对象
				Ins_Gateway acard = null;
				List<Ins_Gateway> acards = new ArrayList<>();
				List<String> listIP = new ArrayList<>();
				//存放临时bean
				Map<String,Ins_Gateway> tempMap = new HashMap<String,Ins_Gateway>() ;
				
				for(Ins_Gateway gateway: ins_AcardList){
					String ip = gateway.getIp();
					Cnd cndip = Cnd.where("ip","=",ip);
					//数据库存在相同ip，则更新
					Ins_Gateway ins_Gateway = fetch(cndip);
					if(null!=ins_Gateway){
						ins_Gateway.setGatewayCode(gateway.getGatewayCode());
						ins_Gateway.setGatewayName(gateway.getGatewayName());
						ins_Gateway.setGatewayLocation(gateway.getGatewayLocation().toString());
						ins_Gateway.setCreateTime(gateway.getCreateTime());
						update(ins_Gateway);
					}else{
						tempMap.put(ip, gateway);
						listIP.add(ip);
					}
					
					
					
				}
				
				
				int size = listIP.size();
				if(size==0){
					return re;
				}else{
					String listStr = listIP.toString() ;
					
					//String[] data = (String[]) listIP.toArray(new String[size]);
					String[] data = new String[]{listStr} ;
					String scardRe = scadaDeviceServcie.invoke(data, Globals.SETACARDBATCH);
					JSONObject obj = new JSONObject(scardRe) ;
					JSONArray arr = obj.getJSONArray("ipList") ;
					for(int i=0;i<arr.length();i++){
						JSONObject jobj = arr.getJSONObject(i) ;
						String ipOld = jobj.getString("ip") ;
						
						String ip = ipOld.replace("[", "").replace(" ", "").replace("]", "");
						 
						
						int num = jobj.getInt("result");
						if(num==0){//失败
							String gatewayCode = tempMap.get(ip).getGatewayCode();
							errorStr+=gatewayCode+"," ;
						}else{//成功
							
							
							Ins_Gateway bean = tempMap.get(ip);
							acard = new Ins_Gateway();
							acard.setCreateTime(bean.getCreateTime());
							acard.setGatewayCode(bean.getGatewayCode());
							acard.setGatewayLocation(bean.getGatewayLocation().toString());
							acard.setGatewayName(bean.getGatewayName());
							acard.setIp(bean.getIp());
						    acards.add(acard);
							
						}
						
						
					}
					if(acards.size()>0){
						// 进行批量插入
						dao().insert(acards);
						
					}
					
					if(StringUtils.isNotBlank(errorStr)){
						errorStr = StringUtils.strip(errorStr, ",") ;
						re="SCADA接口错误，编号为:"+errorStr+"导入失败,其余数据导入成功" ;
					}
				}
				
				
			
				return re;
		
		
	}
	/**
	 * 查询ip及对应的位置
	 */
	@Override
	public Map<String, String> getIpAndLocation() {
		Sql sql = Sqls.create("SELECT ip , gatewayLocation from ins_gateway;") ;
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String,String>> list = new ArrayList<Map<String,String>>();
				Map<String,String> map = new HashMap<>() ;
				while (rs.next()) {
					map.put(rs.getString("ip"), rs.getString("gatewayLocation")) ;
				}
				list.add(map) ;
				return list;
			}
		});
         Map map = dao().execute(sql).getList(Map.class).get(0); 
		return map;
	}

}
