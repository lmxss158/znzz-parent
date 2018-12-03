package com.znzz.app.asset.moudules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import com.znzz.app.asset.moudules.models.Ins_Asset_Repair;
import com.znzz.app.asset.moudules.services.InsAssetRepairService;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 维修管理实现类
 * @author wangqiang
 *
 */
@IocBean(args = { "refer:dao" })
public class InsAssetRepairServiceImpl extends BaseServiceImpl<Ins_Asset_Repair> implements InsAssetRepairService {

	public InsAssetRepairServiceImpl(Dao dao) {
		super(dao);
	}
    
	/**
	 * 维修申请
	 */
	@Override
	public void insertRepair(Ins_Asset_Repair repair) {
		dao().insert(repair) ;
	}
	
	
	/**
	 * 根据assetCode 获取list
	 */
	@Override
	public List<Ins_Asset_Repair> getListByAssetCode(String assetCode) {
		Sql sql = Sqls.create("SELECT * from ins_assets_repair WHERE assetCode=@assetCode AND PID =0 ") ;
	    sql.setParam("assetCode", assetCode) ;
	    
	 // 设置回调函数
	    sql.setCallback(Sqls.callback.entities());
	    Entity<Ins_Asset_Repair> entity = dao().getEntity(Ins_Asset_Repair.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    return sql.getList(Ins_Asset_Repair.class);
	    
	    
	}
	
	/**
	 * 维修列表查询
	 */
	@SuppressWarnings("unused")
	@Override
	public NutMap getRepairList(String assetCode, String applyDepart, String applyPerson, Integer type, String opType,
			String time, int length, int start, int draw,List<DataTableOrder> orders,List<DataTableColumn> columns) {
		
		Sql sql = Sqls.create("select * from( SELECT a.id, a.assetCode,c.assetName,c.deviceVersion,a.`status`,d.`name`,e.username,a.linkMan,a.linkPhone,  f.repairCost,	IFNULL((SELECT MAX(operatorTime) from ins_assets_repair WHERE pid = a.id ),a.operatorTime) as operatorTime FROM 	ins_assets_repair a LEFT JOIN ins_assets_info b ON a.assetCode =  b.assetCode LEFT JOIN ins_assets_version c	ON b.deviceVersion = c.deviceVersionOrg LEFT JOIN sys_unit d	ON a.applyDepart = d.id LEFT JOIN sys_user e  ON a.applyPreson = e.id LEFT JOIN (SELECT	a.pid,	a.repairCost FROM	ins_assets_repair a ,(SELECT pid, MAX(operatorTime) as operatorTime from ins_assets_repair WHERE `status` IN(3,4) OR ext1=1 AND pid!=0 GROUP BY pid) b WHERE a.pid=b.pid AND	(a.`status` IN (3, 4) OR a.ext1 = 1) AND a.operatorTime =  b.operatorTime) f ON a.id = f.pid  where a.pid=0  $var $var1 ) mm $time $order") ;
		Cnd cri = Cnd.NEW() ;
		if(StringUtils.isNotBlank(assetCode)){//统一编号  , 设备名称  , 设备型号
			String str = "%"+assetCode.trim()+"%" ;
			Sql sql2 = Sqls.create("and (a.assetCode like @str or c.assetName like @str or b.deviceVersion like @str)") ;
			sql2.setParam("str", str) ;
			sql.setVar("var", sql2) ;
		}
		StringBuilder builder = new StringBuilder() ;
		
		
		if(type!=null){
			builder.append(" and b.assetType="+type) ;
		}
		if(StringUtils.isNotBlank(opType)){
			builder.append(" and a.`status` in ("+opType+")") ;
		}
		if(StringUtils.isNotBlank(applyDepart)){
			builder.append(" and a.applyDepart="+"'"+applyDepart.trim()+"' ") ;
		}
		
		if(StringUtils.isNotBlank(applyPerson)){
			builder.append(" and e.username like "+"'%"+applyPerson.trim()+"%'") ;
		}
		
		if(StringUtils.isNotBlank(time)){
			String[] t = time.split("-") ;
			String  timeStr = " where operatorTime between "+"'"+t[0]+"' and '"+t[1]+"'";
			sql.setVar("time",timeStr);
		}
		sql.setVar("var1", builder.toString());
		if (orders != null && orders.size() > 0) {
			Cnd  cri2 = Cnd.NEW() ;
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cri2.getOrderBy().orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
            sql.setVar("order", cri2) ;
       }
		NutMap nmap = data(length,start,draw,sql,sql)  ;
		return nmap;
	}

	/**
	 * 根据id获取一条维修详情
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getDetail(Integer id) {
		Sql sql = Sqls.create("SELECT a.id,a.status, a.assetCode ,a.ext2,a.operatorTime AS applyTime,b.assetType,c.assetName,b.serialNumber,c.deviceVersion,a.linkMan,a.linkPhone,a.remark, d.repairContent,d.remark as repairRemark ,d.remarkDepart,d.remarkMan,d.repairCost,d.ext1 AS hasMeasure,d.operatorTime AS repairDate,e.remarkMan AS sendPerson, e.remarkDepart As sendDepart ,e.operatorTime AS sendDate,f.remark AS measureResult,f.remarkMan AS measureMan ,f.operatorTime AS measureDate, g.remark AS acceptRemark,	g.remarkMan AS acceptMan,	g.ext2 AS acceptDate,	g.username AS getMan,h.`name` , i.username ,j.`name` AS applyDepartName , k.username AS applyPresonName FROM ins_assets_repair a LEFT JOIN ins_assets_info b ON a.assetCode = b.assetCode LEFT JOIN ins_assets_version c ON b.deviceVersion = c.deviceVersionOrg LEFT JOIN (SELECT oo.pid,oo.repairContent,oo.remark,oo.remarkDepart,pp.username AS remarkMan,oo.repairCost,oo.operatorTime,oo.ext1 FROM ins_assets_repair oo LEFT JOIN sys_user pp ON oo.operatorPerson = pp.id WHERE oo.`status` IN (3, 4) OR oo.ext1 = 1) d ON a.id = d.pid LEFT JOIN (SELECT pid,remarkDepart,remarkMan,operatorTime from ins_assets_repair WHERE `status` = 2) e ON a.id =  e.pid LEFT JOIN (SELECT pid,remark,remarkMan ,operatorTime from ins_assets_repair WHERE `status` =5 AND ext1 IS NULL) f ON a.id = f.pid LEFT JOIN (	SELECT	mm.pid,	mm.remark,	mm.remarkMan,	mm.ext2, nn.username FROM ins_assets_repair mm LEFT JOIN sys_user nn ON mm.operatorPerson = nn.id	WHERE	mm.`status` = 8) g ON a.id = g.pid LEFT JOIN sys_unit h ON e.remarkDepart = h.id LEFT JOIN sys_user i ON e.remarkMan = i.id LEFT JOIN sys_unit j ON a.applyDepart = j.id LEFT JOIN sys_user k ON a.applyPreson = k.id WHERE a.id = @repairId ORDER BY  d.operatorTime desc LIMIT 1") ;
		sql.setParam("repairId", id) ;
		
		 sql.setCallback(Sqls.callback.entities());
	    Entity<Record> entity = dao().getEntity(Record.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    return sql.getList(Record.class).get(0);
		
	}
	
	/**
	 * 获取实体bean
	 */
	
	@Override
	public Ins_Asset_Repair getDetailBean(Integer id) {
		return dao().fetch(Ins_Asset_Repair.class, id);
	}
    
	/**
	 * 更新实体bean
	 */
	@Override
	public void updateDetailBean(Ins_Asset_Repair repair) {
		dao().update(repair) ;
	}
	
	/**
	 * 流程查看
	 */
	@Override
	public List<Record> viewList(Integer id) {
		Sql sql = Sqls.create("SELECT a.assetCode , b.name ,c.username ,e.username as uname,d.name as dname,`status`,DATE_FORMAT(operatorTime,'%Y-%m-%d %H:%i:%S') as operatorTime,pid FROM ins_assets_repair a LEFT JOIN sys_unit b ON a.applyDepart= b.id LEFT JOIN sys_user c ON a.applyPreson = c.id LEFT JOIN sys_unit d ON a.operatorDepart = d.id LEFT JOIN sys_user e ON a.operatorPerson = e.id  WHERE a.id=@pid UNION ALL  SELECT a.assetCode , b.name ,c.username ,e.username,d.name,`status`,DATE_FORMAT(operatorTime,'%Y-%m-%d %H:%i:%S') as operatorTime ,pid FROM ins_assets_repair a LEFT JOIN sys_unit b ON a.applyDepart= b.id LEFT JOIN sys_user c ON a.applyPreson = c.id LEFT JOIN sys_unit d ON a.operatorDepart = d.id LEFT JOIN sys_user e ON a.operatorPerson = e.id  WHERE a.pid=@pid ORDER BY operatorTime DESC") ;
		sql.setParam("pid", id) ;
		// 设置回调函数
	    sql.setCallback(Sqls.callback.entities());
	    Entity<Record> entity = dao().getEntity(Record.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    return sql.getList(Record.class);
	}
	
	/**
	 * 查询各类型总数(非仪器室 查询本单位的)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getCount(String depart) {
		Sql sql = Sqls.create("SELECT b.assetType, a.`status` , COUNT(*) as num from ins_assets_repair a, ins_assets_info b WHERE a.assetCode=b.assetCode AND a.pid=0 $var GROUP BY a.`status` ,b.assetType") ;
		// 设置回调函数
		 sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				HashMap<String, Object> map = new HashMap<String ,Object>() ;	
				ArrayList<Object> list = new ArrayList<>() ;
				while (rs.next()) {
					int i = rs.getInt(1) ;
					String pre = i==1?"s":"y" ;
					
					map.put(pre+rs.getInt(2), rs.getInt(3));
					list.add(map) ;
				}
			 return list;
			}
		});
		 List<Map> list = dao().execute(sql).getList(Map.class) ;
		 Map map = new HashMap<>() ;
		 
		 if(list.size()>0){
			 map = list.get(0) ;
		 }
		 
		return map;
	}
	
	/**
	 * 资产详情中查询维修记录
	 */
	
	@Override
	public NutMap getRepairRecordForAssetInfo(String assetCode, int length, int start, int draw) {
		Sql sql = Sqls.create("SELECT a.id,	a.assetCode,	d.`name`,	e.username,	b.remarkDepart,	b.remarkMan,	b.repairContent,	b.repairCost,	"
				+ "b.remark,	c.operatorTime from ins_assets_repair a INNER JOIN ins_assets_repair b ON a.id = b.pid INNER JOIN ins_assets_repair c"
				+ " ON a.id = c.pid LEFT JOIN sys_unit d ON a.applyDepart = d.id LEFT JOIN sys_user e ON a.applyPreson = e.id WHERE a.`status`=8 "
				+ "AND c.`status` =8 AND (b.`status` in (3,4) OR (b.`status` =5 AND b.ext1=1) ) AND b.id in(SELECT MAX(b.id) from ins_assets_repair a "
				+ "INNER JOIN ins_assets_repair b ON a.id = b.pid AND a.`status`=8 AND (b.`status` in (3,4) OR (b.`status` =5 AND b.ext1=1) ) "
				+ " AND a.assetCode = @assetCode  GROUP BY b.pid) AND a.assetCode = @assetCode ORDER BY c.operatorTime DESC ") ;
		sql.setParam("assetCode", assetCode) ;
		NutMap nmap = data(length,start,draw,sql,sql)  ;
		return nmap;
	}
	/**
	 * 获取该条记录中维修中的子记录
	 * @param id
	 * @return
	 */
	@Override
	public Ins_Asset_Repair getRepairingBean(Integer id) {
		Sql sql = Sqls.create("SELECT * from ins_assets_repair WHERE pid=@id AND `status` = 2 ORDER BY id ASC LIMIT 1") ;
	    sql.setParam("id", id) ;
	    
	    // 设置回调函数
	    sql.setCallback(Sqls.callback.entities());
	    Entity<Ins_Asset_Repair> entity = dao().getEntity(Ins_Asset_Repair.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    List<Ins_Asset_Repair> list = sql.getList(Ins_Asset_Repair.class) ;
	    Ins_Asset_Repair re = null ;
	    if(list.size()>0){
	    	re = list.get(0) ;
	    }
	    return re;
	}

}
