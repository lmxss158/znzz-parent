package com.znzz.app.asset.moudules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.time.DateFormatUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import com.znzz.app.asset.moudules.models.Ins_Asset_Repair;
import com.znzz.app.asset.moudules.models.Ins_Asset_lend_record;
import com.znzz.app.asset.moudules.models.Ins_Month_report;
import com.znzz.app.asset.moudules.services.InsAssetLendRecordService;
import com.znzz.app.asset.moudules.services.InsAssetMouthReportService;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 *
 * @ClassName: InsAssetMouthReportServiceImpl   
 * @Description: TODO(月度报告接口)
 * @author fengguoxin
 * @date 2017年9月12日 上午10:50:29   
 */
@IocBean(args = { "refer:dao" })
public class InsAssetMouthReportServiceImpl extends BaseServiceImpl<Ins_Month_report> implements InsAssetMouthReportService {

	@Inject
	InsAssetLendRecordService assetLendRecordService;
	
	public InsAssetMouthReportServiceImpl(Dao dao) {
		super(dao);
	}
	/*
	 * 返回维修，借还，报废，封存启封四张表去重后的所有assetCode
	 * */
	@Override
	public List<String> assetCodeList(String startDate,String endDate){
		Sql sql = Sqls.create("select assetCode from ins_assets_seal_record $condition "+
							  "UNION select assetCode from ins_asset_lend_record $condition "+
							  "UNION select  assetCode from ins_assets_scrap_record $condition ");
		Cnd cnd = Cnd.NEW();
		if(startDate != null && endDate != null ){
			cnd.where().and("operateTime", "between", new Object[]{startDate,endDate});
		}
		sql.setCondition(cnd);
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("assetcode"));
				}
				return list;
			}
		});
		
		
		//借还、报废、封存启封三张表去重后的assetCode
		List<String> list = dao().execute(sql).getList(String.class);
		//维修记录去重后assetCode
		List<String> list2 = repairAssetCode(startDate,endDate);
		
		list.removeAll(list2);
		list.addAll(list2);
		
		return list;
	}

	@Override
	public NutMap  getAllRecodeList(String nianyue){

		//处理时间
		String[] daybyMonth = getDaybyMonth(nianyue);
		String startDate = daybyMonth[0];
		String endDate = daybyMonth[1];
		
		//借出查询
		String sqlLend = "SELECT * from ins_asset_lend_record where actionType='0' and assetCode NOT REGEXP \"^[jJ]\" and oprateTime between '" + startDate + "' and '"+endDate +"'";
		Sql sqlsLend = Sqls.create(sqlLend);
		sqlsLend = sqlsLend.setEntity(dao().getEntity(Ins_Asset_lend_record.class));
		sqlsLend.setCallback(Sqls.callback.entities());
		dao().execute(sqlsLend);
		List<Ins_Asset_lend_record> list_lend = sqlsLend.getList(Ins_Asset_lend_record.class);

		//归还查询
		String sqlRe = "SELECT * from ins_asset_lend_record where actionType='1' and assetCode NOT REGEXP \"^[jJ]\" and oprateTime between '" + startDate + "' and '"+endDate +"'";
		Sql sqlsRe = Sqls.create(sqlRe);
		sqlsRe = sqlsRe.setEntity(dao().getEntity(Ins_Asset_lend_record.class));
		sqlsRe.setCallback(Sqls.callback.entities());
		dao().execute(sqlsRe);
		List<Ins_Asset_lend_record> list_return = sqlsRe.getList(Ins_Asset_lend_record.class);

		//转账查询
		String sqlTransfer = "SELECT * from ins_asset_lend_record where actionType='2' and assetCode NOT REGEXP \"^[jJ]\" and oprateTime between '" + startDate + "' and '"+endDate +"'";
		Sql sqlTransferA = Sqls.create(sqlTransfer);
		sqlTransferA = sqlTransferA.setEntity(dao().getEntity(Ins_Asset_lend_record.class));
		sqlTransferA.setCallback(Sqls.callback.entities());
		dao().execute(sqlTransferA);
		List<Ins_Asset_lend_record> list_transfer = sqlTransferA.getList(Ins_Asset_lend_record.class);

		//送修查询
		String sqlSong = "SELECT * from ins_assets_repair where pid='0' and assetCode NOT REGEXP \"^[jJ]\" and operatorTime between '" + startDate + "' and '"+endDate +"'";
		Sql sqlsSong = Sqls.create(sqlSong);
		sqlsSong = sqlsSong.setEntity(dao().getEntity(Ins_Asset_Repair.class));
		sqlsSong.setCallback(Sqls.callback.entities());
		dao().execute(sqlsSong);
		List<Ins_Asset_Repair> list_repairSong = sqlsSong.getList(Ins_Asset_Repair.class);
		
		//维修好查询
		String sqlRep = "SELECT * from ins_assets_repair where pid !='0' and status='8' and assetCode NOT REGEXP \"^[jJ]\" and operatorTime between '" + startDate + "' and '"+endDate +"'";
		Sql sqlsRep = Sqls.create(sqlRep);
		sqlsRep = sqlsRep.setEntity(dao().getEntity(Ins_Asset_Repair.class));
		sqlsRep.setCallback(Sqls.callback.entities());
		dao().execute(sqlsRep);
		List<Ins_Asset_Repair> list_repair = sqlsRep.getList(Ins_Asset_Repair.class);

		/*********************************************************czl************************************************/

		//查询计量时间和计量结论
		for(Ins_Asset_Repair rep:list_repair){
			Integer pid = rep.getPid();
			Sql sql = Sqls.create("select * from ins_assets_repair where operatorTime = (select MAX(operatorTime) from ins_assets_repair where pid='"+pid+"' and status = '4' )");
			sql = sql.setEntity(dao().getEntity(Ins_Asset_Repair.class));
			sql.setCallback(Sqls.callback.entities());
			dao().execute(sql);
			List<Ins_Asset_Repair> list = sql.getList(Ins_Asset_Repair.class);
			
			if(list!=null && list.size()>0){	//stutas状态为4的记录存在，说明在维修完成后选择了计量操作
				Ins_Asset_Repair repair = list.get(0);
				//stutas为4的记录中operaterTime是维修完成时间，status为5的是计量时间
				rep.setOperatorTime(repair.getOperatorTime());
				
				Sql sql2 = Sqls.create("select * from ins_assets_repair where operatorTime = (select MAX(operatorTime) from ins_assets_repair where pid='"+pid+"' and status = '5' )");
				sql2 = sql2.setEntity(dao().getEntity(Ins_Asset_Repair.class));
				sql2.setCallback(Sqls.callback.entities());
				dao().execute(sql2);
				List<Ins_Asset_Repair> list2 = sql2.getList(Ins_Asset_Repair.class);
				if(list2!=null && list2.size()>0){
					String time = DateFormatUtils.format(list2.get(0).getOperatorTime(), "yyyy-MM-dd HH:mm:ss");
					rep.setExt2(time);
					rep.setRemark(list2.get(0).getRemark());
				}else{
					rep.setExt2("");
					rep.setRemark("");
				}
			}else{  //status为4的记录不存在
				Sql sql2 = Sqls.create("select * from ins_assets_repair where operatorTime = (select MAX(operatorTime) from ins_assets_repair where pid='"+pid+"' and status = '5' or status='3' )");
				sql2 = sql2.setEntity(dao().getEntity(Ins_Asset_Repair.class));
				sql2.setCallback(Sqls.callback.entities());
				dao().execute(sql2);
				List<Ins_Asset_Repair> list2 = sql2.getList(Ins_Asset_Repair.class);
				if(list2!=null && list2.size()>0){
					rep.setOperatorTime(list2.get(0).getOperatorTime());
					rep.setExt2("");
					rep.setRemark("");
				}else{
					rep.setOperatorTime(null);
					rep.setExt2("");
					rep.setRemark("");
				}
			}
		}

		/****************************************************czl*****************************************************/

		// 封装到nutMap里
		NutMap re = new NutMap();
		re.addv("lend", list_lend);
		re.addv("return", list_return);
		re.addv("repairing",list_repairSong );
		re.addv("repaired", list_repair);
		re.addv("transferred", list_transfer);
		return re;

	}

	public List<String> repairAssetCode(String startDate,String endDate){
		Sql sql = Sqls.create("SELECT	DISTINCT(a.assetCode) FROM	ins_assets_repair a INNER JOIN ins_assets_repair b ON a.id = b.pid " +
								"INNER JOIN ins_assets_repair c ON a.id = c.pid "+
								"WHERE a.`status`=8 "+
								"AND b.`status` in(3,4) AND c.`status`=8 AND a.operatorTime between '"+startDate+"' and '"+endDate+"'") ;
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("assetcode"));
				}
				return list;
			}
		});
		
		return dao().execute(sql).getList(String.class);
		
	}
	
	String[] getDaybyMonth(String date){
		/*
		 * 根据月度拿到从上月25号到本月25号时间跨度*/
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]);
		
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);	//当前月
		cal.set(Calendar.DAY_OF_MONTH, 25);
		String last = format.format(cal.getTime());
		
		cal.set(Calendar.MONTH, month-2);	//上个月（支持1月的上个月去年12月）
		cal.set(Calendar.DAY_OF_MONTH, 25);
		String first = format.format(cal.getTime());
		
		return new String[]{first,last};
	}
	
}
