package com.znzz.app.asset.moudules.services.apply.impl;

import com.znzz.app.asset.moudules.models.apply.Ins_Asset_Apply;
import com.znzz.app.asset.moudules.services.apply.ApplyAssetService;
import com.znzz.app.web.commons.util.ApplyUtils;
import com.znzz.app.web.modules.controllers.platform.apply.vo.ApplyAssetVo;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;

import java.util.Date;
import java.util.List;

@IocBean(args = {"refer:dao"})
public class ApplyAssetServiceImpl extends BaseServiceImpl<Ins_Asset_Apply> implements ApplyAssetService {


    public ApplyAssetServiceImpl(Dao dao) {
        super(dao);
    }

    @Override
    public NutMap queryApplyInfos(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd, String assetState) {
        String sqls = "SELECT\n" +
                "\ta.id id,\n" +
                "\ta.assetCode assetCode,\n" +
                "\ta.deviceVersion deviceVersion,\n" +
                "\ta.serialNumber serialNumber,\n" +
                "\ta.ggName ggName,\n" +
                "\tb.assetName assetName,\n" +
                "\ta.manageStatus manageStatus,\n" +
                "\ta.repairState repairState,\n" +
                "\ta.scrapState scrapState,\n" +
                "\ta.borrowDepart borrowDepart,\n" +
                "\ta.isLend isLend,\n" +
                "\n" +
                "IF (\n" +
                "\ta.forbidDate <= CURDATE(),\n" +
                "\t1,\n" +
                "\t0\n" +
                ") assetState,\n" +
                " (\n" +
                "\tSELECT\n" +
                "\t\te.returnDate\n" +
                "\tFROM\n" +
                "\t\tins_asset_apply e\n" +
                "\tWHERE\n" +
                "\t\te.assetCode = a.assetCode\n" +
                "\tAND e.applyState <> 3\n" +
                "\tAND e.applyState <> 4\n" +
                "\tAND\n" +
                "\tIF (\n" +
                "\t\te.applyState = 1,\n" +
                "\t\tCURDATE() >= e.lendDate,\n" +
                "\t\tCURDATE() BETWEEN e.lendDate\n" +
                "\tAND e.returnDate AND(e.deadline IS NULL or e.deadline > 0)\n" +
                "\t)\n" +
                ") returnDate,\n" +
                " (\n" +
                "\tSELECT\n" +
                "\t\t`name`\n" +
                "\tFROM\n" +
                "\t\tsys_unit\n" +
                "\tWHERE\n" +
                "\t\tid = (\n" +
                "\t\t\tSELECT DISTINCT\n" +
                "\t\t\t\t(x.lenderUnit)\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tins_asset_apply x\n" +
                "\t\t\tWHERE\n" +
                "\t\t\t\tx.assetCode = a.assetCode\n" +
                "\t\t\tAND x.applyState <> 3\n" +
                "\t\t\tAND x.applyState <> 4\n" +
                "\t\t\tAND\n" +
                "\t\t\tIF (\n" +
                "\t\t\t\tx.applyState = 1,\n" +
                "\t\t\t\tCURDATE() >= x.lendDate,\n" +
                "\t\t\t\tCURDATE() BETWEEN x.lendDate\n" +
                "\t\t\tAND x.returnDate\n" +
                "\t\t\t)\n" +
                "\t\t)\n" +
                ") lenderUnit,\n" +
                " (\n" +
                "\tSELECT\n" +
                "\t\tusername\n" +
                "\tFROM\n" +
                "\t\tsys_user\n" +
                "\tWHERE\n" +
                "\t\tid = (\n" +
                "\t\t\tSELECT DISTINCT\n" +
                "\t\t\t\t(v.proposer)\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tins_asset_apply v\n" +
                "\t\t\tWHERE\n" +
                "\t\t\t\tv.assetCode = a.assetCode\n" +
                "\t\t\tAND v.applyState <> 3\n" +
                "\t\t\tAND v.applyState <> 4\n" +
                "\t\t\tAND\n" +
                "\t\t\tIF (\n" +
                "\t\t\t\tv.applyState = 1,\n" +
                "\t\t\t\tCURDATE() >= v.lendDate,\n" +
                "\t\t\t\tCURDATE() BETWEEN v.lendDate\n" +
                "\t\t\tAND v.returnDate\n" +
                "\t\t\t)\n" +
                "\t\t)\n" +
                ") proposers,\n" +
                " (\n" +
                "\tSELECT DISTINCT\n" +
                "\t\t(t.number)\n" +
                "\tFROM\n" +
                "\t\tins_asset_apply t\n" +
                "\tWHERE\n" +
                "\t\tt.assetCode = a.assetCode\n" +
                "\tAND t.applyState <> 3\n" +
                "\tAND t.applyState <> 4\n" +
                "\tAND\n" +
                "\tIF (\n" +
                "\t\tt.applyState = 1,\n" +
                "\t\tCURDATE() >= t.lendDate,\n" +
                "\t\tCURDATE() BETWEEN t.lendDate\n" +
                "\tAND t.returnDate\n" +
                "\t)\n" +
                ") number,\n" +
                " (\n" +
                "\tSELECT\n" +
                "\n" +
                "\tIF (\n" +
                "\t\tc.powerOnTime > c.powerOffTime\n" +
                "\t\tAND c.powerOnTime > c.outLineTime,\n" +
                "\t\tNULL,\n" +
                "\n" +
                "\tIF (\n" +
                "\t\tc.outLineTime IS NULL,\n" +
                "\t\tc.powerOffTime,\n" +
                "\t\tc.outLineTime\n" +
                "\t)\n" +
                "\t)\n" +
                "\tFROM\n" +
                "\t\tins_switching_flow c\n" +
                "\tWHERE\n" +
                "\t\tc.deviceCode = a.assetCode\n" +
                "\tORDER BY\n" +
                "\t\tc.powerOnTime DESC\n" +
                "\tLIMIT 0,\n" +
                "\t1\n" +
                ") powerOffTime\n" +
                "FROM\n" +
                "\tins_assets_info a\n" +
                "LEFT JOIN ins_assets_version b ON a.deviceVersion = b.deviceVersionOrg\n" +
                "LEFT JOIN ins_asset_apply d ON d.assetCode = a.assetCode " + "$order";

        Sql sql = Sqls.queryRecord(sqls);

        Sql sql2 = Sqls.queryRecord("select * from (" + sqls + ") x where x.assetState = " + assetState);
        if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                String orderRow = col.getData();
                // 单列排序处理
                String orderStr = "";
                boolean flag = false;
                if ("assetcode".equals(orderRow)) {
                    orderStr = "a.assetCode";
                } else if ("deviceversion".equals(orderRow)) {
                    orderStr = "a.deviceVersion";
                } else if ("serialnumber".equals(orderRow)) {
                    orderStr = "a.serialNumber";
                } else if ("assetname".equals(orderRow)) {
                    orderStr = "b.assetName";
                } else if ("ggname".equals(orderRow)) {
                    orderStr = "a.ggName";
                } else if ("powerofftime".equals(orderRow)) {
                    orderStr = "powerOffTime";
                } else if ("returndate".equals(orderRow)) {
                    orderStr = "returnDate";
                } else if ("lenderunit".equals(orderRow)) {
                    orderStr = "lenderUnit";
                } else if ("proposer".equals(orderRow)) {
                    orderStr = "proposer";
                } else if ("number".equals(orderRow)) {
                    orderStr = "number";
                } else if ("assetstate".equals(orderRow)) {
                    orderStr = "assetState";
                    flag = true;
                    //sb.append("IF(proposers IS NULL, 0, 1) ASC, a.assetCode ASC");
                } else {
                    // Nothing..
                }
                cnd.orderBy(Sqls.escapeSqlFieldValue(orderStr).toString(), or.getDir());
                if (flag) {
                    cnd.asc("IF(proposers IS NULL, 0, 1)").asc("a.assetCode");
                }
            }
        }

        NutMap re = new NutMap();

        if (!Strings.isBlank(assetState)) {
            sql2.setVar("order", cnd);
            re = data(length, start, draw, sql2, sql2);
        } else {
            sql.setVar("order", cnd);
            re = data(length, start, draw, sql, sql);
        }
        return re;
    }

    /**
     * 新建申请回显信息
     *
     * @param assetCode
     * @return
     */
    @Override
    public ApplyAssetVo queryAddApplys(String assetCode) {
        Sql sql = Sqls.create("SELECT\n" +
                "\t\t\t\ta.assetCode assetCode,\n" +
                "\t\t\t\td.assetName assetName,\n" +
                "\t\t\t\ta.deviceVersion deviceVersion,\n" +
                "\t\t\t\ta.serialNumber serialNumber,\n" +
                "\t\t\t\ta.enableTime enableTime,\n" +
                "\t\t\t\ta.ggName ggName,\n" +
                "\t\t\t\ta.forbidDate forbidDate,\n" +
//                "\t\t\t\t(\n" +
//                "\t\t\t\t\tSELECT\n" +
//                "\t\t\t\t\t\tMAX(powerOffTime)\n" +
//                "\t\t\t\t\tFROM\n" +
//                "\t\t\t\t\t\tins_switching_flow c\n" +
//                "\t\t\t\t\tWHERE\n" +
//                "\t\t\t\t\t\tc.deviceCode = a.assetCode\n" +
//                "\t\t\t\t\tAND c.powerOffTime IS NOT NULL\n" +
//                "\t\t\t\t) powerOffTime\n"
                "(SELECT MAX(IF(c.powerOffTime IS NULL , c.outLineTime, c.powerOffTime)) powerOffTime FROM ins_switching_flow c WHERE c.deviceCode = a.assetCode\n" +
                ") powerOffTime\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tins_assets_info a\n" +
                "\t\t\tLEFT JOIN ins_assets_version d ON d.deviceVersionOrg = a.deviceVersion\n" +
                "\t\t\tWHERE a.assetCode =@assetCode ");
        sql.params().set("assetCode", assetCode);
        Entity<ApplyAssetVo> entity = dao().getEntity(ApplyAssetVo.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getObject(ApplyAssetVo.class);
    }

    /**
     * @param applyId
     * @return
     */
    @Override
    public ApplyAssetVo queryMyDelay(String applyId) {
        Sql sql = Sqls.create("SELECT\n" +
                "\ta.assetCode,\n" +
                "\ta.assetName,\n" +
                "\ta.deviceVersion,\n" +
                "\ta.serialNumber,\n" +
                "\ta.specifications ggName,\n" +
                "\tc.username proposer,\n" +
                "\ta.entryNumber entryNumber,\n" +
                "\ta.number phoneNumber,\n" +
                "\ta.applyId,\n" +
                "\ta.proposer proposerId,\n" +
                "\ta.approver,\n" +
                "\ta.id,\n" +
                "\ta.lendDate,\n" +
                "\ta.returnDate,\n" +
                "\ta.lenderUnit,\n" +
                "\ta.accessary,\n" +
                "\ta.remark,\n" +
                "\ta.number phoneNumber,\n" +
                "\tb.enableTime enableTime,\n" +
//                "\t(\n" +
//                "\t\tSELECT\n" +
//                "\t\t\tMAX(powerOffTime)\n" +
//                "\t\tFROM\n" +
//                "\t\t\tins_switching_flow c\n" +
//                "\t\tWHERE\n" +
//                "\t\t\tc.deviceCode = a.assetCode\n" +
//                "\t\tAND c.powerOffTime IS NOT NULL\n" +
//                "\t) powerOffTime\n" +
                "(SELECT MAX(IF(c.powerOffTime IS NULL , c.outLineTime, c.powerOffTime)) powerOffTime FROM ins_switching_flow c WHERE c.deviceCode = a.assetCode\n" +
                ") powerOffTime\n" +
                "FROM\n" +
                "\tins_asset_apply a\n" +
                "LEFT JOIN ins_assets_info b ON a.assetCode = b.assetCode\n" +
                "LEFT JOIN sys_user c ON c.id = a.proposer\n" +
                "WHERE\n" +
                "\ta.applyId =@applyId ");
        sql.params().set("applyId", applyId);
        Entity<ApplyAssetVo> entity = dao().getEntity(ApplyAssetVo.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getObject(ApplyAssetVo.class);
    }

    /**
     * 预约审批列表
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
    public NutMap queryApprovalInfos(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd) {
        String sqls = "SELECT\n" +
                "\ta.id,\n" +
                "\ta.applyId,\n" +
                "\ta.assetCode,\n" +
                "\ta.assetName,\n" +
                "\ta.deviceVersion,\n" +
                "\ta.specifications,\n" +
                "\ta.serialNumber,\n" +
                "\ta.lendDate,\n" +
                "\ta.returnDate,\n" +
                "\tc.`name` lenderUnit,\n" +
                "\td.username proposer,\n" +
                "\ta.applyState,\n" +
                "\n" +
                "IF (\n" +
                "\ta.applyState = 0,\n" +
                "\t0,\n" +
                "IF (a.returnDate < CURDATE(), 1, 0)\n" +
                ") assetState " +
                "FROM\n" +
                "\tins_asset_apply a\n" +
                "LEFT JOIN sys_unit c ON c.id = a.lenderUnit\n" +
                "LEFT JOIN sys_user d ON d.id = a.proposer " + "$order";

        Sql sql = Sqls.queryRecord(sqls);
        if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                ApplyUtils.orderByRow(or, columns, cnd);
//                DataTableColumn col = columns.get(or.getColumn());
//                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), or.getDir());
            }
        }
        NutMap re = new NutMap();
        sql.setVar("order", cnd);
        re = data(length, start, draw, sql, sql);
        return re;
    }

    /**
     * 我的预约
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
    public NutMap queryMyApply(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd) {
        String sqls = "SELECT\n" +
                "\ta.applyId,\n" +
                "\ta.assetCode,\n" +
                "\ta.assetName,\n" +
                "\ta.serialNumber,\n" +
                "\ta.deviceVersion,\n" +
                "\ta.lendDate,\n" +
                "\ta.returnDate,\n" +
                "\ta.deadline,\n" +
                "\ta.number,\n" +
                "\ta.proposer,\n" +
                "\tb.username approver,\n" +
                "\ta.applyState\n" +
                "FROM\n" +
                "\tins_asset_apply a\n" +
                "LEFT JOIN sys_user b ON b.id = a.approver " + "$order";

        Sql sql = Sqls.queryRecord(sqls);
        if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                // 排序
                ApplyUtils.orderByRow(or, columns, cnd);
            }
        }
        NutMap re = new NutMap();
        sql.setVar("order", cnd);
        re = data(length, start, draw, sql, sql);
        return re;
    }

    /**
     * 重复校验
     *
     * @param assetCode
     * @param lendDate
     * @param returnDate
     * @return
     */
    @Override
    public int countConflict(String assetCode, Date lendDate, Date returnDate) {
        Sql sql = Sqls.create("SELECT\n" +
                "\tCOUNT(1)\n" +
                "FROM\n" +
                "\tins_asset_apply\n" +
                "WHERE\n" +
                "\tassetCode = @assetCode\n" +
                "AND applyState <> 3\n" +
                "AND applyState <> 4\n" +
                "AND (\n" +
                "\t@lendDate BETWEEN lendDate\n" +
                "\tAND returnDate\n" +
                "\tOR @returnDate BETWEEN lendDate\n" +
                "\tAND returnDate\n" +
                ")");
        sql.params().set("assetCode", assetCode);
        sql.params().set("lendDate", lendDate);
        sql.params().set("returnDate", returnDate);

        sql.setCallback(Sqls.callback.integer());
        dao().execute(sql);
        return sql.getInt();
    }

}
