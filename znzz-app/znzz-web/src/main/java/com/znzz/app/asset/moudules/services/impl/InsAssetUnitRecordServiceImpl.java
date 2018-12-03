package com.znzz.app.asset.moudules.services.impl;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import com.znzz.app.asset.moudules.models.Ins_Asset_Unit;
import com.znzz.app.asset.moudules.models.Ins_Asset_Unit_Record;
import com.znzz.app.asset.moudules.services.InsAssetUnitRecordService;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsUnitVo;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 单位资产借调记录service
 * 
 * @author wangqiang
 *
 */
@IocBean(args = { "refer:dao" })
public class InsAssetUnitRecordServiceImpl extends BaseServiceImpl<Ins_Asset_Unit>
		implements InsAssetUnitRecordService {

	public InsAssetUnitRecordServiceImpl(Dao dao) {
		super(dao);
	}

	/**
	 * 插入一条
	 */
	@Override
	public void insertRecord(Ins_Asset_Unit_Record record) {

		dao().insert(record);
	}

	/**
	 * 更新一条
	 */
	@Override
	public void updateRecord(Ins_Asset_Unit_Record record) {
		dao().update(record);
	}

	/**
	 * 查看 所有完结状态
	 */
	@Override
	public NutMap getRecordList(String assetCode, String operateType, int length, int start, int draw,
			List<DataTableOrder> orders, List<DataTableColumn> columns) {
		Sql sql = Sqls.create(
				"SELECT a.assetCode,IFNULL(f.operateTime,a.operateTime) as operateTime,b.`name`,c.username as loginname,d.`name` as chargeDepartName,e.username as chargeManName,a.position,a.operateType ,a.`status` from ins_assets_unit_record a LEFT JOIN sys_unit b ON a.userDepart = b.id LEFT JOIN sys_user  c ON a.userMan = c.id LEFT JOIN sys_unit d ON a.chargeDepart = d.id LEFT JOIN sys_user e ON a.chargeMan = e.id LEFT JOIN ins_assets_unit_record f ON a.id= f.pid WHERE (a.`status` != 0 OR a.`status` IS NULL ) AND a.pid=0 AND a.assetCode=@assetCode AND a.operateType IN($operateType) $order");
		sql.setParam("assetCode", assetCode);

		if (StringUtils.isNotBlank(operateType)) {
			sql.setVar("operateType", operateType);
		}

		if (orders != null && orders.size() > 0) {
			Cnd cri2 = Cnd.NEW();
			for (DataTableOrder order : orders) {
				DataTableColumn col = columns.get(order.getColumn());
				cri2.getOrderBy().orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
			}
			sql.setVar("order", cri2);
		}
		NutMap nmap = data(length, start, draw, sql, sql);
		return nmap;
	}

	/**
	 * 根据状态查询列表
	 */
	@Override
	public NutMap getAllApproveList(AssetsUnitVo vo, List<DataTableOrder> orders, List<DataTableColumn> columns) {
		Sql sql = Sqls.create(
				"SELECT a.id, a.assetCode,g.assetName,g.deviceVersion,$operateTime,b.`name` as useDepartName,c.username as useManName,d.`name` as chargeDepartName,e.username as chargeManName,a.position,a.operateType ,a.`status` as curStatus from ins_assets_unit_record a LEFT JOIN sys_unit b ON a.userDepart = b.id LEFT JOIN sys_user  c ON a.userMan = c.id LEFT JOIN sys_unit d ON a.chargeDepart = d.id LEFT JOIN sys_user e ON a.chargeMan = e.id LEFT JOIN ins_assets_info f ON a.assetCode = f.assetCode LEFT JOIN ins_assets_version g on f.deviceVersion = g.deviceVersionOrg $leftjoin $condition $var $order");

		Integer mark = vo.getMark();
		String operateTime = "a.operateTime";
		if (mark != null) {
			operateTime = "h.operateTime";
			sql.setVar("leftjoin", "LEFT JOIN ins_assets_unit_record h ON a.id=h.pid");
		}

		sql.setVar("operateTime", operateTime);

		Criteria cri = Cnd.cri();
		if (StringUtils.isNotBlank(vo.getStatus())) {
			String[] strarr = vo.getStatus().split(",");
			int[] arr = new int[strarr.length];
			for (int i = 0; i < strarr.length; i++) {
				arr[i] = Integer.parseInt(strarr[i]);
			}
			cri.where().andInIntArray("a.status", arr);
		}

		if (StringUtils.isNotBlank(vo.getChargeDepart())) {
			cri.where().and("a.chargeDepart", "=", vo.getChargeDepart().trim());
		}

		if (StringUtils.isNotBlank(vo.getChargeMan())) {
			cri.where().and("a.chargeMan", "=", vo.getChargeMan().trim());
		}

		if (StringUtils.isNotBlank(vo.getUseDepart())) {
			cri.where().and("a.userDepart", "=", vo.getUseDepart().trim());
		}

		if (StringUtils.isNotBlank(vo.getUseMan())) {
			cri.where().and("a.userMan", "=", vo.getUseMan().trim());
		}

		if (null != vo.getOpType()) {
			cri.where().and("a.operateType", "=", vo.getOpType());
		}

		if (StringUtils.isNotBlank(vo.getPosition())) {
			cri.where().andLike("a.position", vo.getPosition().trim());
		}

		if (StringUtils.isNotBlank(vo.getOperateTime())) {
			String times = vo.getOperateTime();
			String[] time = times.split("-");
			cri.where().andBetween(operateTime, time[0], time[1]);
		}

		cri.where().and("a.operateType", "!=", 0);
		cri.where().and("a.pid", "=", 0);

		if (StringUtils.isNotBlank(vo.getAssetCode())) {
			String code = vo.getAssetCode().trim();
			String str = "%" + code + "%";
			String par = "";
			if (StringUtils.isBlank(cri.toString())) {
				par = "where";
			} else {
				par = "and";
			}
			Sql sql2 = Sqls
					.create(" $par (a.assetCode like @str or g.assetName like @str or f.deviceVersion like @str)");
			sql2.setParam("str", str);
			sql2.setVar("par", par);
			sql.setVar("var", sql2);
		}
		sql.setCondition(cri);

		if (orders != null && orders.size() > 0) {
			Cnd cri2 = Cnd.NEW();
			for (DataTableOrder order : orders) {
				DataTableColumn col = columns.get(order.getColumn());
				cri2.getOrderBy().orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
			}
			sql.setVar("order", cri2);
		}

		NutMap nmap = data(vo.getLength(), vo.getStart(), vo.getDraw(), sql, sql);
		return nmap;
	}

	/**
	 * 获取单位资产记录表中的详情
	 */
	@Override
	public Map getAssetUnitRecordInfoBean(Integer id) {

		Sql sql = Sqls.create(
				"SELECT a.id, a.assetCode,c.assetName,b.deviceVersion,d.`name` as userDepartName ,g.username as userMan,e.`name` as chargeDepartName, f.username as chargeManName ,a.position ,a.remark from ins_assets_unit_record a LEFT JOIN ins_assets_info b ON a.assetCode = b.assetCode LEFT JOIN ins_assets_version c ON b.deviceVersion = c.deviceVersionOrg LEFT JOIN sys_unit d ON a.userDepart = d.id LEFT JOIN sys_unit e ON a.chargeDepart = e.id LEFT JOIN sys_user f ON a.chargeMan = f.id LEFT JOIN sys_user g ON a.userMan = g.id  WHERE a.id=@id");
		sql.setParam("id", id);

		sql.setCallback(Sqls.callback.entities());
		Entity<Record> entity = dao().getEntity(Record.class);
		sql.setEntity(entity);
		dao().execute(sql);
		return sql.getList(Record.class).get(0);
	}

	/**
	 * 获取实体类bean
	 */
	@Override
	public Ins_Asset_Unit_Record fetchBean(Integer id) {

		Ins_Asset_Unit_Record fetch = dao().fetch(Ins_Asset_Unit_Record.class, id);
		return fetch;
	}

	/**
	 * 获取申请审批全过程记录
	 */
	@Override
	public List<Record> getAssetUnitRecordInfoList(Integer id) {
		Sql sql = Sqls.create(
				"SELECT a.assetCode,c.assetName,b.deviceVersion,a.operateType,a.`status`,d.`name` as chargeDepartName ,e.username as chargeManName,f.`name` as useDepartName,g.username as useManName ,DATE_FORMAT(a.operateTime,'%Y-%c-%d %h:%i:%s') AS operateTime,a.pid from ins_assets_unit_record a LEFT JOIN ins_assets_info b  ON a.assetCode = b.assetCode LEFT JOIN ins_assets_version c ON b.deviceVersion = c.deviceVersionOrg LEFT JOIN sys_unit d ON a.chargeDepart = d.id LEFT JOIN sys_user e ON a.chargeMan = e.id LEFT JOIN sys_unit f ON a.userDepart = f.id LEFT JOIN sys_user g ON a.userMan = g.id WHERE a.id = @id OR a.pid = @id ORDER BY operateTime desc");
		sql.setParam("id", id);
		// 设置回调函数
		sql.setCallback(Sqls.callback.entities());
		Entity<Record> entity = dao().getEntity(Record.class);
		sql.setEntity(entity);
		dao().execute(sql);
		return sql.getList(Record.class);

	}

	/**
	 * 查询该单位下的要审批个数
	 */
	@Override
	public int getApproveCountByUnitId(String unitid) {
		int count = dao().count("ins_assets_unit_record", Cnd.where("status", "=", 0).and("chargeDepart", "=", unitid));
		return count;
	}

	/**
	 * 统计该单位借入申请/归还申请的个数
	 */
	@Override
	public int getApprovingCountByUnitId(String assetCode, String unitid, Integer operateType) {

		int count = dao().count(Ins_Asset_Unit_Record.class, Cnd.where("userDepart", "=", unitid)
				.and("operateType", "=", operateType).and("status", "=", 0).and("assetCode", "=", assetCode));

		return count;
	}

}
