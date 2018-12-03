package com.znzz.app.asset.moudules.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.NutConfig;

import com.mysql.jdbc.Connection;
import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.asset.moudules.models.Ins_Asset_Unit;
import com.znzz.app.asset.moudules.models.Ins_Asset_lend_record;
import com.znzz.app.asset.moudules.services.InsAssertRuleService;
import com.znzz.app.asset.moudules.services.InsAssetLendRecordService;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacility;
import com.znzz.app.instrument.modules.models.Ins_SwitchingFlow;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.DownloadUtil;
import com.znzz.app.web.commons.util.InsExcel;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsLend;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 
 * @ClassName: InsAssetLendRecordServiceImpl   
 * @Description: TODO(仪器室设备借调记录)
 * @author fengguoxin
 * @date 2017年8月23日 下午3:15:37   
 */
@IocBean(args = {"refer:dao"})
public class InsAssetLendRecordServiceImpl  extends BaseServiceImpl <Ins_Asset_lend_record> implements InsAssetLendRecordService {

	public InsAssetLendRecordServiceImpl(Dao dao) {
		super(dao);
	}
	
	 /**
     * 根据型号获取
     */
	@Override
	public Ins_Asset_Rule fetchIns_Asset_Rule(String deviceVersion) {
		Sql sql = Sqls.create("SELECT deviceVersion, assetName  FROM ins_assets_version WHERE  deviceVersionOrg = @deviceVersion");
		sql.setParam("deviceVersion", deviceVersion);
		Entity<Ins_Asset_Rule> entity = dao().getEntity(Ins_Asset_Rule.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
		return sql.getObject(Ins_Asset_Rule.class);
	}

	/**
     * 借出时向unit表插入数据
     */
	@Override
	public void insertAssetRecodeToUnit(Ins_Asset_Unit unit) {
		Ins_Asset_Unit unitFromDB = dao().fetch(Ins_Asset_Unit.class,Cnd.where("assetCode","=",unit.getAssetCode()));
		if (unitFromDB == null){
			dao().insert(unit) ;
		}else {
			unit.setId(unitFromDB.getId());
			dao().updateIgnoreNull(unit);
		}

	}
	/**
     * 归还时从unit表删除数据
     */
	@Override
	public void deleteAssetRecodeToUnit(String code) {
		dao().clear(Ins_Asset_Unit.class,Cnd.where("assetCode", "=", code));
		//dao().delete(Ins_Asset_Unit.class,code);
	}

	/**
     * 查询借调记录
     */
	@Override
	public	List<Ins_Asset_lend_record> getCurMonthReportList() {
		Sql sql = Sqls.create("SELECT * from ins_asset_lend_record WHERE DATE_FORMAT(oprateTime, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m')") ;
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao().getEntity(Ins_Asset_lend_record.class));
		dao().execute(sql);
		List<Ins_Asset_lend_record> list = sql.getList(Ins_Asset_lend_record.class);
		
		return list;
		
	}

	@Override
	public void saveLendRecord(List<Ins_Asset_lend_record> lendRecordList) {
		//插入借调记录
		dao().insert(lendRecordList);
	}

	@Override
	public Ins_Asset_lend_record getNewReturnInfo(String assetCode) {
		
		// 自定义sql
		Sql sql = Sqls.create("select * from ins_asset_lend_record WHERE  assetCode = '"+assetCode +"' and actionType='0' order by id desc limit 1");
		// 设置回调函数
		
		sql = sql.setCallback(new SqlCallback() {
		
			@Override
			public Object invoke(java.sql.Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Ins_Asset_lend_record> list = new ArrayList<>();
				while (rs.next()) {
					Ins_Asset_lend_record id = new Ins_Asset_lend_record();
					id.setApplyDepart(rs.getString("applyDepart"));
					id.setApplyPerson(rs.getString("applyPerson"));
					list.add(id);
				}
				return list;
			}
		});
		List<Ins_Asset_lend_record> list = dao().execute(sql).getList(Ins_Asset_lend_record.class);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public NutMap getLendRecord(String assetCode, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();

		Sql sql = Sqls.create("SELECT a.assetCode assetCode,b.assetName assetName,a.serialNumber serialNumber,b.deviceVersion deviceVersion,a.ggName ggName,a.remark remark" +
				" from ins_assets_info a LEFT JOIN ins_assets_version b ON a.deviceVersion = b.deviceVersionOrg $condition") ;
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao().getEntity(AssetsLend.class));
		String[] assetCodeArray = assetCode.split(",");
		cnd.and("a.assetCode","in",assetCodeArray);
		sql.setCondition(cnd);
		dao().execute(sql);
		List<AssetsLend> list = sql.getList(AssetsLend.class);
		NutMap re = new NutMap();
		re.put("data", list);
		re.put("draw", draw);
		re.put("recordsTotal", length);

		return re;
	}

	@Override
	public NutMap getReturnRecord(String assetCode, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();

		Sql sql = Sqls.create("SELECT s.* FROM ( SELECT a.assetCode assetCode, b.deviceVersion deviceVersion, " +
				" c.applyDepart applyDepart, c.applyPerson applyPerson, b.assetName assetName," +
				" a.ggName ggName, a.serialNumber serialNumber, c.remark remark, c.str1 str1 " +
				" FROM ins_assets_info a " +
				" LEFT JOIN ins_assets_version b ON a.deviceVersion = b.deviceVersionOrg " +
				" LEFT JOIN ins_asset_lend_record c ON a.assetCode = c.assetCode " +
				" $condition ) s GROUP BY s.assetCode ") ;
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao().getEntity(AssetsLend.class));
		String[] assetCodeArray = assetCode.split(",");
		cnd.and("a.assetCode","in",assetCodeArray);
		cnd.and("c.actionType","=","0");

		//cnd.limit(0,1);
		cnd.orderBy("c.opAt","DESC");
		sql.setCondition(cnd);
		dao().execute(sql);
		List<AssetsLend> list = sql.getList(AssetsLend.class);
		NutMap re = new NutMap();
		re.put("data", list);
		re.put("draw", draw);
		re.put("recordsTotal", length);
		return re;
	}

	@Override
	public void insertList(List<Ins_Asset_lend_record> lendRecordInfo) {
		dao().insert(lendRecordInfo);
	}


	@Override
	public void updateList(List<Ins_Asset_lend_record> lendRecordList) {
		for (Ins_Asset_lend_record lendRecord : lendRecordList) {
			dao().updateIgnoreNull(lendRecord);
		}
	}
}
