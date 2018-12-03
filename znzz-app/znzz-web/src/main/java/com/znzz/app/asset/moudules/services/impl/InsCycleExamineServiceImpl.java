package com.znzz.app.asset.moudules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsCycleExamineService;
import com.znzz.app.instrument.modules.models.Ins_CycleExamine;
import com.znzz.app.instrument.modules.models.Ins_SwitchingFlow;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.OffsetPager;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * @author chenzhongliang
 *
 */
@IocBean(args = {"refer:dao"})
public class InsCycleExamineServiceImpl extends BaseServiceImpl<Ins_Assets> implements InsCycleExamineService{
	
	public InsCycleExamineServiceImpl(Dao dao) {
		super(dao);
	}
	
	/* 
	 * 写入ins_examine_record记录表一条数据
	 */
	@Override
	public void insertExamineRecord(Ins_CycleExamine insCycleExamine) {
		dao().insert(insCycleExamine);
	}
	
	/* 
	 * 批量插入
	 */
	@Override
	public void insertExamineRecordList(List<Ins_CycleExamine> examineList) {
		dao().insert(examineList);
	}

	/* 
	 * 根据assetCode获取检定记录
	 */
	@Override
	public NutMap findList(String assetCode, int length, int start, int draw) {
		NutMap re = new NutMap();
		long count=0;
		List<Record> res = new ArrayList<Record>();
		if(StringUtils.isNoneBlank(assetCode)){
			Sql sql = Sqls.create("select assetCode,assetName,examineDate,dueDate,conclusion,examinePerson from ins_examine_record where assetCode='"+assetCode+"'");
			re = data(length,start,draw,sql,sql);
			
		}else{
			re.put("recordsFiltered", count);
			re.put("data", res);
			re.put("draw", draw);
			re.put("recordsTotal", length);
		}
		return re;
	}

	@Override
	public List<Object> findExaminebyId(Sql sql) {
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {
		@Override
		public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Object> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("id"));
					list.add(rs.getString("assetCode"));
					list.add(rs.getString("assetName"));
					list.add(rs.getDate("enableTime"));
					list.add(rs.getDate("examineDate"));
					list.add(rs.getDate("dueDate"));//下次检定日期
					list.add(rs.getString("conclusion"));//检定结论
					list.add(rs.getString("overdueReaspn"));//过期原因
					list.add(rs.getString("recordid"));
				}
				return list;
				}
		});
		return dao().execute(sql).getList(Object.class);
	}
	@Override
	public List<Object> findExaminebyId2(Sql sql) {
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {
		@Override
		public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Object> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("id"));
					list.add(rs.getString("assetCode"));
					list.add(rs.getString("assetName"));
					list.add(rs.getDate("enableTime"));
					list.add(rs.getDate("examineDate"));
					list.add(rs.getDate("dueDate"));//下次检定日期
					list.add(null);//检定结论
					list.add(null);//过期原因
					list.add(null);//记录表id
				}
				return list;
				}
		});
		return dao().execute(sql).getList(Object.class);
	}

	@Override
	public void updateAssetInfo(Integer id, Date examineDate, Date dueDate) {
		Sql sql = Sqls.create("update ins_assets_info set examineDate = @examineDate, dueDate = @dueDate where id=@id");
		sql.setParam("examineDate", examineDate);
		sql.setParam("dueDate", dueDate);
		sql.setParam("id", id);
		
		dao().execute(sql);
		
	}

	@Override
	public void updateExamine(String assetCode, String overdueReaspn) {
		Sql sql = Sqls.create("update ins_examine_record set overdueReaspn=@overdueReaspn where assetCode=@assetCode");
		sql.setParam("overdueReaspn", overdueReaspn);
		sql.setParam("assetCode", assetCode);
		
		dao().execute(sql);
	}

	@Override
	public List<Ins_Assets> getDuedateList() {
		Sql sql = Sqls.create("select id,isOverdue,dueDate from ins_assets_info");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {
		@Override
		public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Ins_Assets> list = new ArrayList<>();
				while (rs.next()) {
					Ins_Assets ic = new Ins_Assets();
					ic.setId(rs.getInt("id"));
					ic.setIsOverdue(rs.getInt("isOverdue"));
					ic.setDueDate(rs.getDate("dueDate"));
					list.add(ic);
				}
				return list;
				}
		});
		return dao().execute(sql).getList(Ins_Assets.class);
		
	}

	@Override
	public void updateIsover(Integer id,int i) {
		Sql sql = Sqls.create("update ins_assets_info set isOverdue = @isOverdue where id=@id");
		sql.setParam("isOverdue",i);
		sql.setParam("id", id);
		
		dao().execute(sql);
		
	}

	@Override
	public int countOver() {
		Sql sql = Sqls.create("select count(id) from ins_assets_info where isOverdue='0'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {
		@Override
		public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Integer> list = new ArrayList<>();
				while (rs.next()) {
						list.add(rs.getInt("count(id)"));
				}
				return list.get(0);
				}
		});
		List<Integer> list = dao().execute(sql).getList(Integer.class);
		if(!list.isEmpty() && list.size()>0){
			return list.get(0);
		}
		return 0;
				
	}

	@Override
	public int countOverByTime(Date startTime, Date endTime) {
		Sql sql = Sqls.create("select count(id) from ins_assets_info where isOverdue='1' and dueDate between @startTime and @endTime");
		sql.setParam("startTime", startTime);
		sql.setParam("endTime", endTime);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao().getEntity(Integer.class));
		dao().execute(sql);
		List<Integer> list = sql.getList(Integer.class);
		
		if(!list.isEmpty() && list.size()>0){
			return list.get(0);
		}
		return 0;
	}

	@Override
	public void insertList(List<Ins_Assets> assetList) {
		
		for (Ins_Assets a : assetList) {
			/*Sql sql = Sqls.create("update ins_assets_info set deviceVersion=@device,serialNumber=@serial,examineDate=@examine,dueDate=@due where assetCode=@assetCode");
			sql.setParam("device", a.getDeviceVersion());
			sql.setParam("serial", a.getSerialNumber());
			sql.setParam("examine", a.getExamineDate());
			sql.setParam("due", a.getDueDate());
			sql.setParam("assetCode", a.getAssetCode());
			dao().execute(sql);*/
			Cnd cnd = Cnd.where("assetCode", "=", a.getAssetCode());
			dao().update("ins_assets_info", Chain.make("deviceVersion", a.getDeviceVersion()), cnd);
			dao().update("ins_assets_info", Chain.make("serialNumber", a.getSerialNumber()), cnd);
			dao().update("ins_assets_info", Chain.make("examineDate", a.getExamineDate()), cnd);
			dao().update("ins_assets_info", Chain.make("dueDate", a.getDueDate()), cnd);
		}
	}
		
}

	
