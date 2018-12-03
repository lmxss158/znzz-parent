package com.znzz.app.asset.moudules.services.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.asset.moudules.models.Ins_Asset_Unit;
import com.znzz.app.asset.moudules.services.InsAssetUnitService;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacility;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsUnitVo;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

@IocBean(args = { "refer:dao" })
public class InsAssetUnitServiceImpl  extends BaseServiceImpl<Ins_Asset_Unit> implements InsAssetUnitService {
	
	public static int thrityTime = 30*8*60 ;
	
	public InsAssetUnitServiceImpl(Dao dao) {
		super(dao);
	}
	
	/**
	 * 查询单位资产列表
	 */
	
	@Override
	public NutMap getAssetUnitList(AssetsUnitVo vo,List<DataTableOrder> orders,  List<DataTableColumn> columns) {
		
		Sql sql = Sqls.create("SELECT a.id, a.assetCode,c.assetName ,c.deviceVersion,d.`name` as chargeDepartName ,a.chargeDepart ,e.username as chargeManName,f.`name` as userDepartName,a.userDepart,g.username as userManName,a.rate ,b.locationInfo AS position,a.operateTime FROM	ins_assets_unit a LEFT JOIN ins_assets_info b ON a.assetCode = b.assetCode LEFT JOIN ins_assets_version c ON b.deviceVersion = c.deviceVersionOrg LEFT JOIN sys_unit d ON a.chargeDepart = d.id  LEFT JOIN sys_user e ON a.chargeMan = e.id  LEFT JOIN sys_unit f ON a.userDepart = f.id  LEFT JOIN sys_user g ON a.userMan = g.id $condition $var $order") ;
		
		Criteria cri = Cnd.cri() ;
		if(StringUtils.isNotBlank(vo.getUseDepart())){
			cri.where().and("a.chargeDepart","=",vo.getUseDepart()) ;
		}
		
		if(StringUtils.isNotBlank(vo.getChargeMan())){
			cri.where().and("a.chargeMan","=",vo.getChargeMan()) ;
		}
		
		if(StringUtils.isNotBlank(vo.getPosition())){
			cri.where().andLike("b.locationInfo", vo.getPosition().trim());
		}
		
		if(StringUtils.isNotBlank(vo.getOperateTime())){
			String times = vo.getOperateTime() ;
			String[] time = times.split("-") ;
			cri.where().andBetween("a.operateTime", time[0], time[1]) ;
		}
		
		if(StringUtils.isNotBlank(vo.getAssetCode())){
			String code = vo.getAssetCode() ;
			String str = "%"+code.trim()+"%" ;
			String par="" ;
			if(StringUtils.isBlank(cri.toString())){
				par="where" ;
			}else{
				par="and" ;
			}
			Sql sql2 = Sqls.create(" $par (a.assetCode like @str or c.assetName like @str or b.deviceVersion like @str)") ;
			sql2.setParam("str", str) ;
			sql2.setVar("par", par) ;
			sql.setVar("var", sql2) ;
		}
		sql.setCondition(cri) ;
		
		if (orders != null && orders.size() > 0) {
			Cnd  cri2 = Cnd.NEW() ;
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cri2.getOrderBy().orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
            sql.setVar("order", cri2) ;
       }
		
		NutMap nmap = data(vo.getLength(),vo.getStart(),vo.getDraw(),sql,sql)  ;
		
		return nmap;
	}
	/**
	 * 获取单位资产信息
	 */
	@Override
	public Map getAssetUnitInfo(Integer id) {
		
		Sql sql = Sqls.create("SELECT a.id, a.assetCode,b.locationInfo AS position,c.assetName,c.deviceVersion,d.`name` ,e.`name` as chargeDepartName, f.username as chargeManName from ins_assets_unit a LEFT JOIN ins_assets_info b ON a.assetCode = b.assetCode LEFT JOIN ins_assets_version c ON b.deviceVersion = c.deviceVersion LEFT JOIN sys_unit d ON a.userDepart = d.id LEFT JOIN sys_unit e ON a.chargeDepart = e.id LEFT JOIN sys_user f ON a.chargeMan = f.id  WHERE a.id=@id") ;
		sql.setParam("id", id) ;
		
		sql.setCallback(Sqls.callback.entities());
	    Entity<Record> entity = dao().getEntity(Record.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    return sql.getList(Record.class).get(0);
	}
	
	/**
	 * 更新单位资产信息
	 */
	@Override
	public void updateAssetUnitInfo(Ins_Asset_Unit unit) {
		dao().update(unit) ;
	}
	/**
	 * 获取单位资产实体类bena
	 */
	@Override
	public Ins_Asset_Unit getAssetUnitInfoBean(Integer id) {

		Ins_Asset_Unit unit = dao().fetch(Ins_Asset_Unit.class, id) ;
		
		return unit;
	}
	
	/**
	 * 根据统一编码获取实体类bean
	 */
	@Override
	public Ins_Asset_Unit getAssetUnitInfoBean(String assetCode) {
		Ins_Asset_Unit ins_Asset_Unit = dao().fetch(Ins_Asset_Unit.class, Cnd.where("assetCode", "=", assetCode)) ;
		return ins_Asset_Unit;
	}
	
	
	/**
	 * 更新设备的使用率
	 */
	@Override
	public void UpdateAssetUnitRate(Map<String, Integer> map) {
		DecimalFormat df = new DecimalFormat("0.00%");  
		for (Map.Entry<String, Integer> entry : map.entrySet()) {  
		    Integer value = entry.getValue() ;
		    String ra = df.format((float)value/thrityTime) ;
			dao().update(Ins_Asset_Unit.class,Chain.make("rate", ra),Cnd.where("assetCode","=",entry.getKey()));
		}
		
	}

	@Override
	public void saveOrUpdate(List<Ins_Asset_Unit> assetUnitList) {
		//遍历单位资产表,进行插入或者更新
		for (Ins_Asset_Unit ins_Asset_Unit : assetUnitList) {
			//id为空,进行插入操作
			if (ins_Asset_Unit.getId() == null) {
				dao().insert(ins_Asset_Unit);
			}else {//进行更新操作
				dao().updateIgnoreNull(ins_Asset_Unit);
			}
			
		}
	}

	@Override
	public void insertList(List<Ins_Asset_Unit> unitList) {
		dao().insert(unitList);
	}

	@Override
	public void updateList(List<Ins_Asset_Unit> unitList) {
		dao().updateIgnoreNull(unitList);
	}


}
