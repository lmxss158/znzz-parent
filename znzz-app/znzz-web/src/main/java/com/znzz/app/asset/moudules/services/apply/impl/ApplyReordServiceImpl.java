package com.znzz.app.asset.moudules.services.apply.impl;

import com.znzz.app.asset.moudules.models.apply.Ins_apply_record;
import com.znzz.app.asset.moudules.services.apply.ApplyReordService;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;

import java.util.List;

@IocBean(args = {"refer:dao"})
public class ApplyReordServiceImpl extends BaseServiceImpl<Ins_apply_record> implements ApplyReordService {

    public ApplyReordServiceImpl(Dao dao) {
        super(dao);
    }

    /**
     * 审批履历记录
     *
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @param cnd
     * @return
     */
    @Override
    public NutMap queryRecords(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd) {
        String sqls = "SELECT\n" +
                "\tb.applyId,\n" +
                "\tb.operateTime,\n" +
                "\tc.assetCode,\n" +
                "\tc.assetName,\n" +
                "\tc.serialNumber,\n" +
                "\tc.deviceVersion,\n" +
                "\tc.specifications,\n" +
                "\tc.operateState,\n" +
                "\tc.lendDate,\n" +
                "\tc.returnDate,\n" +
                "\td.username proposer,\n" +
                "\te.`name` lenderUnit\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\ta.applyId,\n" +
                "\t\t\tMAX(a.operateTime) operateTime\n" +
                "\t\tFROM\n" +
                "\t\t\tins_apply_record a\n" +
                "\t\tWHERE\n" +
                "\t\t\ta.applyId IS NOT NULL\n" +
                "\t\tGROUP BY\n" +
                "\t\t\ta.applyId\n" +
                "\t) b\n" +
                "LEFT JOIN ins_apply_record c ON c.applyId = b.applyId AND b.operateTime = c.operateTime\n" +
                "LEFT JOIN sys_user d ON c.proposer = d.id\n" +
                "LEFT JOIN sys_unit e ON e.id = c.lenderUnit" + "$order";

        Sql sql = Sqls.queryRecord(sqls);
        if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                String orderRow = col.getData();
                // 单列排序处理
                String orderStr = "";
                if ("applyid".equals(orderRow)) {
                    orderStr = "b.applyId";
                } else if ("assetname".equals(orderRow)) {
                    orderStr = "c.assetName";
                } else if ("deviceversion".equals(orderRow)) {
                    orderStr = "c.deviceVersion";
                } else if ("serialnumber".equals(orderRow)) {
                    orderStr = "c.serialNumber";
                } else if ("lenddate".equals(orderRow)) {
                    orderStr = "c.lendDate";
                } else if ("returndate".equals(orderRow)) {
                    orderStr = "c.returnDate";
                } else if ("lenderunit".equals(orderRow)) {
                    orderStr = "lenderUnit";
                } else if ("proposer".equals(orderRow)) {
                    orderStr = "proposer";
                } else if ("operatestate".equals(orderRow)) {
                    orderStr = "c.operateState";
                } else if ("operatetime".equals(orderRow)) {
                    orderStr = "b.operateTime";
                } else {
                    // Nothing..
                }
                cnd.orderBy(Sqls.escapeSqlFieldValue(orderStr).toString(), or.getDir());
            }
        }
        NutMap re = new NutMap();
        sql.setVar("order", cnd);
        re = data(length, start, draw, sql, sql);
        return re;
    }

    /**
     * 操作履历
     *
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @return
     */
    @Override
    public NutMap queryDetail(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, String applyId) {
        String sqls = "SELECT\n" +
                "\ta.operateState,\n" +
                "\ta.entryNumber,\n" +
                "\tb.username operator,\n" +
                "\ta.operateTime,\n" +
                "\ta.number,\n" +
                "\ta.remark,\n" +
                "\ta.accessary\n" +
                "FROM\n" +
                "\tins_apply_record a\n" +
                "LEFT JOIN sys_user b ON a.operator = b.id\n" +
                "WHERE\n" +
                "\ta.operateState >= 0\n" +
                "AND a.applyId = @applyId\n" +
                "ORDER BY\n" +
                "\ta.operateTime DESC";

        Sql sql = Sqls.queryRecord(sqls);
        sql.setParam("applyId", applyId);
        NutMap re = new NutMap();
        re = data(length, start, draw, sql, sql);
        return re;
    }
}
