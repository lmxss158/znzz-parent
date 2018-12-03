package com.znzz.app.ledger.modules.service.impl;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;

import com.znzz.app.ledger.modules.models.InsFixedAssetsLedger;
import com.znzz.app.ledger.modules.service.InsFixedAssetsLedgerService;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.OffsetPager;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

@IocBean(args = { "refer:dao" })
public class InsFixedAssetsLedgerServiceImpl extends BaseServiceImpl<InsFixedAssetsLedger>
		implements InsFixedAssetsLedgerService {

	public InsFixedAssetsLedgerServiceImpl(Dao dao) {
		super(dao);
	}

	@Override
	public NutMap getFixedAssetsData(InsFixedAssetsLedger fixedAssetsLedger, int length, int start, int draw,
			List<DataTableOrder> order, List<DataTableColumn> columns) {
		NutMap re = new NutMap();
		Cnd cnd = Cnd.NEW();
		String SQL = "SELECT a.* FROM ins_fixed_assets_ledger a";
		// 查询sql
		Sql sql = Sqls.queryRecord(SQL);
		// 添加排序功能
		if (order != null && order.size() > 0) {
			for (DataTableOrder or : order) {
				DataTableColumn col = columns.get(or.getColumn());
				String name = col.getData();
				cnd.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
			}
		}
		// sql.setVar("order", cnd);
		re = dataMy(length, start, draw, sql, sql);
		return re;
	}

	/**
     * DataTable Page SQL
     *
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param countSql 查询条件
     * @param orderSql 排序语句
     * @return
     */
    public NutMap dataMy(int length, int start, int draw, Sql countSql, Sql orderSql) {
        NutMap re = new NutMap();
        Pager pager = new OffsetPager(start, length);
        int recordCount = (int) Daos.queryCount(this.dao(), countSql);
        pager.setRecordCount(recordCount);// 记录数需手动设置
        pager.setPageSize(recordCount);
        orderSql.setPager(pager);
        orderSql.setCallback(Sqls.callback.records());
        this.dao().execute(orderSql);
        re.put("recordsFiltered", pager.getRecordCount());
        re.put("data", orderSql.getList(Record.class));
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

	@Override
	public void saveOrUpate(List<InsFixedAssetsLedger> fixedAssetsList) {
		dao().insertOrUpdate(fixedAssetsList);
	}
}
